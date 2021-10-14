var vmMain = new Vue({
  el: '#main',
  data: {
    // 현재 적용되고있는 검색 파라미터
    searchParams: '',
    items: [],
  },
  created() {
    this.initSearchParams();
  },
  methods: {
    initSearchParams: function () {
      var sp = new URLSearchParams(location.search);
      if (!sp.has('page')) sp.append('page', 1);
      if (!sp.has('pageSize')) sp.append('pageSize', PAGE_SIZE);

      if (sp.has('searchKeyword')) document.title = sp.get('searchKeyword') + ' | 위마켓'

      this.searchParams = sp;
    },
    infiniteHandler: function ($state) {
      axios.get('/api/items', { params: this.searchParams }).then((response) => {
        if (response.data.list) {
          setTimeout(() => {
            this.items.push(...response.data.list);
            this.searchParams.set('page', parseInt(this.searchParams.get('page')) + 1);
            $state.loaded();

            if(response.data.page * response.data.pageSize >= response.data.totalCount){
              $state.complete();
            }
          }, 500);
        } else {
          $state.complete();
        }
      });
    },
    numberFormat(price) {
      return price.toLocaleString('en-US');
    },
  },
});

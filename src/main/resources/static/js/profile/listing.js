var vmProfileListing = new Vue({
  el: '#profileListing',
  data: {
    userId: userId,
    currUserId: currUserId,

    item: {
      list: [],
      page: 1,
    },
    itemCompleted: {
      list: [],
      page: 1,
    },
    isDeletedUser: false,
  },
  created(){
    axios
      .get('/api/users/' + this.userId)
      .then((response) => {
        if(response.data.data.status === 'DELETED')
          this.isDeletedUser = true;
      });
  },
  methods: {
    itemInfiniteHandler: function ($state) {
      var searchParams = new URLSearchParams();
      searchParams.append('pageSize', PAGE_SIZE);
      searchParams.append('page', this.item.page++);
      searchParams.append('searchTarget', 'USER');
      searchParams.append('searchKeyword', this.userId);
      searchParams.append('itemStatusList', 'OPEN');
      searchParams.append('itemStatusList', 'TRADE');

      axios.get('/api/items', { params: searchParams }).then((response) => {
        if (response.data.list) {
          setTimeout(() => {
            this.item.list.push(...response.data.list);
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
    itemCompletedInfiniteHandler: function ($state) {
      var searchParams = new URLSearchParams();
      searchParams.append('pageSize', PAGE_SIZE);
      searchParams.append('page', this.itemCompleted.page++);
      searchParams.append('searchTarget', 'USER');
      searchParams.append('searchKeyword', this.userId);
      searchParams.append('itemStatusList', 'COMPLETED');

      axios.get('/api/items', { params: searchParams }).then((response) => {
        if (response.data.list) {
          setTimeout(() => {
            this.itemCompleted.list.push(...response.data.list);
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
    itemCompletedLoading() {
      this.itemCompletedInfiniteHandler(this.$refs.itemCompletedInfiniteLoading.stateChanger);
    },
    numberFormat(price) {
      return price.toLocaleString('en-US');
    },
    dateFormat: function (date) {
      date = new Date(date);

      var year = date.getFullYear();
      var month = ('0' + (date.getMonth() + 1)).slice(-2);
      var day = ('0' + date.getDate()).slice(-2);
      var hours = ('0' + date.getHours()).slice(-2);
      var minutes = ('0' + date.getMinutes()).slice(-2);
      var seconds = ('0' + date.getSeconds()).slice(-2);

      var dateString = year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + seconds;
      return dateString;
    },
  },
});

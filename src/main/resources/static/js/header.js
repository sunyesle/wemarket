var vmHeader = new Vue({
  el: '#header',
  data: {
    currUserId: currUserId,
    // 검색 파라미터
    searchTarget: 'TITLE', //TITLE, TITLE_CONTENT, USER
    searchKeyword: '',
    categoryIdList: [],
    maxPrice: null,
    minPrice: null,
    itemStatusList: ['OPEN'],//OPEN, TRADE, COMPLETED

    categories: [],
  },
  async created() {
    await this.findAllCategory();
    await this.initFilter();
  },
  methods: {
    findAllCategory: function () {
      axios.get('/api/categories').then((response) => {
        this.categories = response.data.list;
        this.checkAllCategory = true;
      });
    },
    search: function () {
      var sp = new URLSearchParams();
      sp.append('searchTarget', this.searchTarget);
      if (this.searchKeyword != null) sp.append('searchKeyword', this.searchKeyword);
      if (this.categoryIdList != null && this.categoryIdList.length != this.categories.length) {
        this.categoryIdList.forEach(function (category) {
          sp.append('categoryIdList', category);
        });
      }
      if (this.maxPrice != null) sp.append('maxPrice', this.maxPrice);
      if (this.minPrice != null) sp.append('minPrice', this.minPrice);
      if (this.itemStatusList != null) {
        this.itemStatusList.forEach(function (itemStatus) {
          sp.append('itemStatusList', itemStatus);
        });
      }

      location.href = contextPath + '/?' + sp.toString();
    },
    initFilter: function () {
      var sp = new URLSearchParams(location.search);
      if (sp.has('searchTarget')) this.searchTarget = sp.get('searchTarget');
      if (sp.has('searchKeyword')) this.searchKeyword = sp.get('searchKeyword');
      if (sp.has('searchTarget')) this.searchTarget = sp.get('searchTarget');
      if (sp.has('categoryIdList')) {
        this.categoryIdList = [];
        this.categoryIdList = sp.getAll('categoryIdList');
      }
      if (sp.has('maxPrice')) this.maxPrice = sp.get('maxPrice');
      if (sp.has('minPrice')) this.minPrice = sp.get('minPrice');
      if (sp.has('itemStatusList')) {
        this.itemStatusList = [];
        this.itemStatusList = sp.getAll('itemStatusList');
      }
    },
    clickSignout: function () {
      axios.post('/api/users/signOut').then((response) => {
        location.href = contextPath + '/';
      });

    }
  },
  computed: {
    checkAllCategory: {
      get: function () {
        return this.categories ? this.categories.length == this.categoryIdList.length : false;
      },
      set: function (value) {
        var categoryIdList = [];

        if (value) {
          this.categories.forEach(function (c) {
            categoryIdList.push(c.id);
          });
        }

        this.categoryIdList = categoryIdList;
      },
    },
  },
});

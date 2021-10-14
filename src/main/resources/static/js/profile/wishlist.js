var vmProfileWishlist = new Vue({
  el: '#profileWishlist',
  data: {
    userId: userId,
    currUserId: currUserId,

    wishlist: {
      list: [],
      page: 1,
    },
  },
  created() {
    if (this.userId !== this.currUserId) {
      alert('접근 권한이 없습니다.');
      location.replace(contextPath + '/');
    }
  },
  methods: {
    wishlistInfiniteHandler: function ($state) {
      var searchParams = new URLSearchParams();
      searchParams.append('pageSize', PAGE_SIZE);
      searchParams.append('page', this.wishlist.page++);
      searchParams.append('userId', this.userId);

      axios.get('/api/wishlists', { params: searchParams }).then((response) => {
        if (response.data.list) {
          setTimeout(() => {
            this.wishlist.list.push(...response.data.list);
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

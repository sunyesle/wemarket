var vmProfileMyoffer = new Vue({
  el: '#profileMyoffer',
  data: {
    userId: userId,
    currUserId: currUserId,

    offerOpen: {
      list: [],
      page: 1,
    },
    offerAccepted: {
      list: [],
      page: 1,
    },
    offerDeclined: {
      list: [],
      page: 1,
    },
    offerCompleted: {
      list: [],
      page: 1,
    }
  },
  created() {
    if (this.userId !== this.currUserId) {
      alert('접근 권한이 없습니다.');
      location.replace(contextPath + '/');
    }
  },
  methods: {
    offerOpenInfiniteHandler: function ($state) {
      var searchParams = new URLSearchParams();
      searchParams.append('pageSize', PAGE_SIZE);
      searchParams.append('page', this.offerOpen.page++);
      searchParams.append('buyerId', this.userId);
      searchParams.append('accepted', 'OPEN');
      searchParams.append('completed', false);

      axios.get('/api/offers', { params: searchParams }).then((response) => {
        if (response.data.list) {
          setTimeout(() => {
            this.offerOpen.list.push(...response.data.list);
            $state.loaded();

            if (response.data.page * response.data.pageSize >= response.data.totalCount){
              $state.complete();
            }
          }, 500);
        } else {
          $state.complete();
        }
      });
    },
    offerAcceptedInfiniteHandler: function ($state) {
      var searchParams = new URLSearchParams();
      searchParams.append('pageSize', PAGE_SIZE);
      searchParams.append('page', this.offerAccepted.page++);
      searchParams.append('buyerId', this.userId);
      searchParams.append('accepted', 'TRUE');
      searchParams.append('completed', false);

      axios.get('/api/offers', { params: searchParams }).then((response) => {
        if (response.data.list) {
          setTimeout(() => {
            this.offerAccepted.list.push(...response.data.list);
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
    offerDeclinedInfiniteHandler: function ($state) {
      var searchParams = new URLSearchParams();
      searchParams.append('pageSize', PAGE_SIZE);
      searchParams.append('page', this.offerDeclined.page++);
      searchParams.append('buyerId', this.userId);
      searchParams.append('accepted', 'FALSE');
      searchParams.append('completed', false);

      axios.get('/api/offers', { params: searchParams }).then((response) => {
        if (response.data.list) {
          setTimeout(() => {
            this.offerDeclined.list.push(...response.data.list);
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
    offerCompletedInfiniteHandler: function ($state) {
      var searchParams = new URLSearchParams();
      searchParams.append('pageSize', PAGE_SIZE);
      searchParams.append('page', this.offerCompleted.page++);
      searchParams.append('buyerId', this.userId);
      searchParams.append('completed', true);

      axios.get('/api/offers', { params: searchParams }).then((response) => {
        if (response.data.list) {
          setTimeout(() => {
            this.offerCompleted.list.push(...response.data.list);
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
    loadOfferAccepted() {
      this.offerAcceptedInfiniteHandler(this.$refs.offerAcceptedInfiniteLoading.stateChanger);
    },
    loadOfferDeclined() {
      this.offerDeclinedInfiniteHandler(this.$refs.offerDeclinedInfiniteLoading.stateChanger);
    },
    loadOfferCompleted() {
      this.offerCompletedInfiniteHandler(this.$refs.offerCompletedInfiniteLoading.stateChanger);
    },
    cancelOffer: function (name, index, offerId) {
      axios
        .put('/api/offers/' + offerId + '/cancel')
        .then((response) => {
          if (name === 'open') {
            this.offerOpen.list.splice(index, 1);
          } else if ('accepted') {
            this.offerAccepted.list.splice(index, 1);
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

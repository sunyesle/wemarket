var vmDetail = new Vue({
  el: '#detail',
  data: {
    itemId: itemId,
    currUserId: currUserId,

    item: '',
    isWishlist: false,
    offer: {
      list: [],
      page: 1,
    },
    isDeletedUser: false,
    price: '',
  },
  async created() {
    await this.findItem();

    if (this.currUserId !== '' && this.currUserId !== this.item.userId) this.findIsWishlist();
  },
  methods: {
    findItem: async function () {
      await axios
        .get('/api/items/' + this.itemId)
        .then((response) => {
          this.item = response.data.data;
          this.isDeletedUser = (this.item.user.status === 'DELETED');
          document.title = this.item.title + ' | 위마켓'
        })
        .catch((error) => {
          location.replace(contextPath + '/');
        });
    },
    findIsWishlist: function () {
      axios.get('/api/wishlists/' + this.itemId + '/exists').then((response) => {
        this.isWishlist = response.data.data;
      });
    },
    toggleWishlist: function () {
      if (this.isWishlist) {
        // 관심상품에서 삭제
        axios.delete('/api/wishlists/' + this.itemId).then((response) => {
          this.isWishlist = false;
          alert('관심상품에서 삭제되었습니다.');
        });
      } else {
        const data = {
          itemId: this.itemId,
        };
        // 관심상품에 추가
        axios
          .post('/api/wishlists', data, {
            headers: {
              'Content-Type': `application/json`,
            },
          })
          .then((response) => {
            this.isWishlist = true;
            alert('관심상품에 추가되었습니다.');
          });
      }
    },
    deleteItem: function () {
      if (confirm('삭제하시겠습니까?')) {
        axios.delete('/api/items/' + this.itemId).then((response) => {
          alert('삭제되었습니다.');
          location.replace(contextPath + '/');
        });
      }
    },
    makeOffer: function () {
      if (this.price === '') {
        alert('가격을 입력해주세요.');
        return;
      }

      if (confirm(this.numberFormat(this.price) + '원으로 제안하시겠습니까?')) {
        const data = {
          itemId: this.itemId,
          price: this.price,
        };
        axios
          .post('/api/offers', data, {
            headers: {
              'Content-Type': `application/json`,
            },
          })
          .then((response) => {
            this.resetOfferList();
          });
      }
    },
    acceptOffer: function (offerId) {
      axios.put('/api/offers/' + offerId + '/accept').then((response) => {
        this.item.status = 'TRADE';
        this.resetOfferList();
      });
    },
    cancelOffer: function (offerId) {
      axios.put('/api/offers/' + offerId + '/cancel').then((response) => {
        this.item.status = 'OPEN';
        this.resetOfferList();
      });
    },
    completeOffer: function (offerId) {
      axios.put('/api/offers/' + offerId + '/complete').then((response) => {
        this.item.status = 'COMPLETED';
        this.resetOfferList();
      });
    },
    infiniteHandler: function ($state) {
      var searchParam = new URLSearchParams();
      searchParam.append('itemId', this.itemId);
      searchParam.append('page', this.offer.page++);
      searchParam.append('pageSize', PAGE_SIZE);

      axios.get('/api/offers', { params: searchParam }).then((response) => {
        if (response.data.list) {
          setTimeout(() => {
            this.offer.list.push(...response.data.list);
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
    resetOfferList() {
      this.offer.list = [];
      this.offer.page = 1;
      this.$refs.infiniteLoading.stateChanger.reset();
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
    numberFormat(price) {
      return price.toLocaleString('en-US');
    },
    toRatingStar: function (rating) {
      rating = Math.round(rating);
      var stars = '';

      for (var i = 0; i < rating; i++) stars += '★';

      for (var i = 0; i < 5 - rating; i++) stars += '☆';

      return stars;
    },
  },
});

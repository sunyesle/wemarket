var vmProfileReview = new Vue({
  el: '#profileReview',
  data: {
    userId: userId,
    currUserId: currUserId,

    review: {
      list: [],
      page: 1,
    },
    myReview: {
      id: '',
      rating: '',
      reviewType: '',
      content: '',
    },
    isDisabled: false,
    isDeletedUser: false,
  },
  async created() {
     await axios
       .get('/api/users/' + this.userId)
       .then((response) => {
         if(response.data.data.status === 'DELETED'){
           this.isDeletedUser = true;
           return;
         }
       });

    if (this.userId !== this.currUserId) this.findMyReview();
  },
  methods: {
    reviewInfiniteHandler: function ($state) {
      var searchParams = new URLSearchParams();
      searchParams.append('pageSize', PAGE_SIZE);
      searchParams.append('page', this.review.page++);
      searchParams.append('userId', this.userId);

      axios.get('/api/reviews', { params: searchParams }).then((response) => {
        if (response.data.list) {
          setTimeout(() => {
            this.review.list.push(...response.data.list);
            $state.loaded();

            if (response.data.page * response.data.pageSize >= response.data.totalCount) {
              $state.complete();
            }
          }, 500);
        } else {
          $state.complete();
        }
      });
    },
    findMyReview: function () {
      var searchParams = new URLSearchParams();
      searchParams.append('userId', this.userId);
      searchParams.append('writerId', this.currUserId);

      axios.get('/api/reviews', { params: searchParams }).then((response) => {
        if (response.data.list) {
          this.myReview = response.data.list[0];
        }
      });
    },
    writeReview: async function () {
      if (!this.validate()) return;

      this.isDisabled = true;

      if (this.myReview.id === '') {
        // 리뷰 작성
        const data = {
          userId: this.userId,
          rating: this.myReview.rating,
          reviewType: this.myReview.reviewType,
          content: this.myReview.content,
        };
        await axios
          .post('/api/reviews', data, {
            headers: {
              'Content-Type': `application/json`,
            },
          })
          .then((response) => {
            this.myReview.id = response.data.data.id;
            this.resetReviewList();
          })
          .finally((this.isDisabled = false));
      } else {
        // 리뷰 수정
        const data = {
          rating: this.myReview.rating,
          reviewType: this.myReview.reviewType,
          content: this.myReview.content,
        };
        await axios
          .put('/api/reviews/' + this.myReview.id, data, {
            headers: {
              'Content-Type': `application/json`,
            },
          })
          .then((response) => {
            this.resetReviewList();
          })
          .finally((this.isDisabled = false));
      }
    },
    validate: function () {
      if (this.myReview.rating === '') {
        alert('별점을 입력해주세요.');
        return false;
      }
      if (this.myReview.reviewType === '') {
        alert('구매자, 판매자 중 하나를 선택해주세요.');
        return false;
      }
      if (this.myReview.content === '') {
        alert('내용을 입력해주세요.');
        return false;
      }
      if (this.myReview.content.length > 400) {
        alert('내용은 최대 400자까지 입력할 수 있습니다.');
        return false;
      }
      return true;
    },
    deleteReview: async function () {
      if (confirm('삭제하시겠습니까?')) {
        this.isDisabled = true;

        await axios.delete('/api/reviews/' + this.myReview.id).then((response) => {
          alert('삭제되었습니다.');
          this.myReview.rating = '';
          this.myReview.reviewType = '';
          this.myReview.content = '';
          this.myReview.id = '';
          this.resetReviewList();
        })
        .finally((this.isDisabled = false));
      }
    },
    resetReviewList() {
      this.review.list = [];
      this.review.page = 1;
      this.$refs.reviewInfiniteLoading.stateChanger.reset();
    },

    rating(rating) {
      this.myReview.rating = rating;
    },
    getRating(index) {
      if (index <= this.myReview.rating) return '★';

      return '☆';
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
    toRatingStar: function (rating) {
      rating = Math.round(rating);
      var stars = '';

      for (var i = 0; i < rating; i++) stars += '★';

      for (var i = 0; i < 5 - rating; i++) stars += '☆';

      return stars;
    },
  },
});

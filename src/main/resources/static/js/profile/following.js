var vmFollowing = new Vue({
  el: '#profileFollowing',
  data: {
    userId: userId,
    currUserId: currUserId,

    following: {
      list: [],
      page: 1,
    },
    isSubmitting: false,
  },
  created() {
    if (this.userId !== this.currUserId) {
      alert('접근 권한이 없습니다.');
      location.replace(contextPath + '/');
    }
  },
  methods: {
    followingInfiniteHandler: function ($state) {
      var searchParams = new URLSearchParams();
      searchParams.append('pageSize', PAGE_SIZE);
      searchParams.append('page', this.following.page++);
      searchParams.append('followerId', this.userId);

      axios.get('/api/followers', { params: searchParams }).then((response) => {
        if (response.data.list) {
          setTimeout(() => {
            this.following.list.push(...response.data.list);
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
    unfollow: function (userId, index) {
      if (this.isSubmitting) return;

      this.isSubmitting = true;
      axios
        .delete('/api/followers/' + userId)
        .then((response) => {
          this.following.list.splice(index, 1);
          alert('언팔로우 했습니다.');
        })
        .finally((this.isSubmitting = false));
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

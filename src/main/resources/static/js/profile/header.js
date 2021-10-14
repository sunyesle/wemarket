var vmProfileHeader = new Vue({
  el: '#profileHeader',
  data: {
    userId: userId,
    currUserId: currUserId,

    user: '',
    isFollowing: '',
    isDeletedUser: false,
  },
  created() {
    this.findUser();
    if(currUserId !== '' && currUserId !== userId)
        this.findIsFollowing();
  },
  methods: {
    findUser: function () {
      axios
        .get('/api/users/' + this.userId)
        .then((response) => {
          this.user = response.data.data;
          this.isDeletedUser = (this.user.status === "DELETED");
          document.title = this.user.nickname + '님의 프로필 | 위마켓'
        })
        .catch((error) => {
          location.replace(contextPath + '/');
        });
    },
    findIsFollowing: function () {
      axios.get('/api/followers/' + this.userId + '/exists').then((response) => {
        this.isFollowing = response.data.data;
      });
    },
    toggleFollow: function (bool) {
      if (this.isFollowing) {
        // unfollow
        axios.delete('/api/followers/' + this.userId).then((response) => {
          this.isFollowing = false;
          alert('팔로우를 취소했습니다.');
        });
      } else {
        // follow
        const data = {
          userId: this.userId,
        };
        axios
          .post('/api/followers', data, {
            headers: {
              'Content-Type': `application/json`,
            },
          })
          .then((response) => {
            this.isFollowing = true;
            alert('팔로우 했습니다.');
          });
      }
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

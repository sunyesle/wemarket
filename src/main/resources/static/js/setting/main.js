var vmSetting = new Vue({
  el: '#setting',
  data: {
    currUserId: currUserId,

    user: '',
  },
  created() {
    this.findUser();
  },
  methods: {
    findUser: function () {
      axios.get('/api/users/' + this.currUserId).then((response) => {
        this.user = response.data.data;
      });
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

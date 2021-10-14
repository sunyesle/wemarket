var vmVerify = new Vue({
  el: '#verify',
  data: {
    code: code,

    isSuccess: false,
  },
  created() {
    this.verify();
  },
  methods: {
    verify: function () {
      axios
        .get('/api/users/verify/' + this.code)
        .then((response) => {
          this.isSuccess = true;
        })
        .catch((error) => {
          location.replace(contextPath + '/');
        });
    },
  },
});

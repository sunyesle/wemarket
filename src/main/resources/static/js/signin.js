var vmSignin = new Vue({
  el: '#signin',
  data: {
    id: '',
    password: '',
    isSubmitting: false,
  },
  methods: {
    signin: function () {
      if(this.isSubmitting) return;
      this.isSubmitting = true;
      const data = {
        id: this.id,
        password: this.password,
      };
      axios
        .post('/api/users/signIn', data, {
          headers: {
            'Content-Type': 'application/json',
          },
        })
        .then((response) => {
          if (document.referrer) {
            window.location = document.referrer;
          } else {
            location.href = contextPath + '/';
          }
        })
        .finally(() => {
          this.isSubmitting = false;
        });
    },
  },
});

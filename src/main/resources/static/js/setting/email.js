var vmSettingEditEmail = new Vue({
  el: '#settingEditEmail',
  data: {
    currUserId: currUserId,

    email: '',
    isSubmitting: false,
  },
  created() {
    this.findEmail();
  },
  methods: {
    findEmail: function () {
      axios.get('/api/users/' + this.currUserId).then((response) => {
        this.email = response.data.data.email;
      });
    },
    updateEmail: function () {
      if(this.isSubmitting) return;

      this.isSubmitting = true;
      const data = {
        email: this.email,
      };
      axios
        .put('/api/users/email', data, {
          headers: {
            'Content-Type': 'application/json',
          },
        })
        .then((response) => {
          alert('변경되었습니다.');
          location.href = contextPath + '/verify-email';
        })
        .finally(() => {
          this.isSubmitting = false;
        });
    },
  },
});

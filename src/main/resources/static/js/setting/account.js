var vmSettingDeleteAccount = new Vue({
  el: '#settingAccount',
  data: {
    currUserId: currUserId,

    password: '',
    isChecked: false,
    isSubmitting: false,
  },
  methods: {
    deleteAccount: function () {
      if(this.isSubmitting) return;
      if (!this.validate()) return;

      this.isSubmitting = true;
      const data = {
        password: this.password,
      };
      axios
        .delete(
          '/api/users',
          { data: data },
          {
            headers: {
              'Content-Type': 'application/json',
            },
          }
        )
        .then((response) => {
          location.href = contextPath + '/delete-account-success';
        })
        .finally(() => {
          this.isSubmitting = false;
        });
    },
    validate: function () {
      if(this.password === ''){
        alert('비밀번호를 입력해주세요.');
        return false;
      }
      if (!this.isChecked) {
        alert('계정 삭제 체크를 해주세요.');
        return false;
      }
      return true;
    },
  },
});

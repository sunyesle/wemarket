var vmSettingEditPassword = new Vue({
  el: '#settingEditPassword',
  data: {
    currUserId: currUserId,

    oldPassword: '',
    password: '',
    rePassword: '',
    isSubmitting: false,
  },
  methods: {
    updatePassword: function () {
      if(this.isSubmitting) return;
      if (!this.validate()) return;

      this.isSubmitting = true;
      const data = {
        oldPassword: this.oldPassword,
        password: this.password,
      };
      axios
        .put('/api/users/password', data, {
          headers: {
            'Content-Type': 'application/json',
          },
        })
        .then((response) => {
          alert('변경되었습니다.');
          location.href = contextPath + '/setting';
        })
        .finally(() => {
          this.isSubmitting = false;
        });
    },
    validate: function () {
      if (this.oldPassword === '') {
        alert('현재 비밀번호를 입력해주세요.');
        return false;
      }
      var passwordRegexp = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{8,16}$/;
      if (!passwordRegexp.test(this.password)) {
        alert('비밀번호는 8~16자 이내의 영문, 숫자 조합만 사용할 수 있습니다.');
        return false;
      }
      if (this.password !== this.rePassword) {
        alert('새 비밀번호와 비밀번화 확인 값이 일치하지 않습니다.');
        return false;
      }
      return true;
    },
  },
});

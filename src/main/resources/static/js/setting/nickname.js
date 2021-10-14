var vmSettingEditNickname = new Vue({
  el: '#settingEditNickname',
  data: {
    currUserId: currUserId,

    nickname: '',
    oldNickname: '',
    isSubmitting: false,
  },
  created() {
    this.findNickname();
  },
  methods: {
    findNickname: function () {
      axios.get('/api/users/' + this.currUserId).then((response) => {
        this.nickname = response.data.data.nickname;
        this.oldNickname = response.data.data.nickname;
      });
    },
    updateNickname: function () {
      if(this.isSubmitting) return;
      if (!this.validate()) return;

      this.isSubmitting = true;
      const data = {
        nickname: this.nickname.replace(/\s/gi, ''),
      };
      axios
        .put('/api/users/', data, {
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
      if (this.nickname === this.oldNickname) {
        alert('기존 값과 동일합니다.');
        return false;
      }
      if (this.nickname.length < 2 || this.nickname.length > 10) {
        alert('닉네임은 2~10자만 사용합니다.');
        return false;
      }
      return true;
    },
  },
});

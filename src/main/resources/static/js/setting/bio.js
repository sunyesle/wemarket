var vmSettingEditBio = new Vue({
  el: '#settingEditBio',
  data: {
    currUserId: currUserId,

    bio: '',
    oldBio: '',
    isSubmitting: false,
  },
  created() {
    this.findBio();
  },
  methods: {
    findBio: function () {
      axios.get('/api/users/' + this.currUserId).then((response) => {
        this.bio = response.data.data.bio;
        this.oldBio = response.data.data.bio;
      });
    },
    updateBio: function () {
      if(this.isSubmitting) return;
      if (!this.validate()) return;

      this.isSubmitting = true;
      const data = {
        bio: this.bio,
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
      if (this.bio === this.oldBio) {
        alert('기존 값과 동일합니다.');
        return false;
      }
      if (this.bio.length > 400) {
        alert('프로필설명은 400자 이내로 입력해주세요."');
        return false;
      }
      return true;
    },
  },
});

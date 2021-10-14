var vmSettingEditImage = new Vue({
  el: '#settingEditImage',
  data: {
    currUserId: currUserId,

    src: null,
    file: null,
    isSubmitting: false,
  },
  created() {
    //this.findImage();
  },
  methods: {
    findImage: function () {
      axios.get('/api/users/' + this.currUserId).then((response) => {
        this.src = response.data.data.profileImage;
      });
    },
    onFileSelected(event) {
      var input = event.target;
      if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = (e) => {
          this.src = e.target.result;
          this.file = input.files[0];
        };
        reader.readAsDataURL(input.files[0]);
      } else {
        this.src = null;
        this.file = null;
      }
    },
    updateImage: function () {
      this.isSubmitting = true;
      var formData = new FormData();
      formData.append('file', this.file);
      axios
        .put('/api/users/profile', formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
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
  },
});

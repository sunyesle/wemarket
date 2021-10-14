var vmWrite = new Vue({
  el: '#write',
  data: {
    itemId: itemId,

    categoryId: '',
    price: '',
    title: '',
    content: '',
    images: [],
    categories: '',
    isSubmitting: false,
  },
  created() {
    this.findAllCategory();
    if (this.itemId !== '') this.findItem();
  },
  methods: {
    findAllCategory: function () {
      axios.get('/api/categories').then((response) => {
        this.categories = response.data.list;
      });
    },
    findItem: function () {
      axios.get('/api/items/' + this.itemId).then((response) => {
        const item = response.data.data;
        this.categoryId = item.categoryId;
        this.price = item.price;
        this.title = item.title;
        this.content = item.content;

        axios
          .get('/api/items/' + this.itemId + '/imageIds')
          .then((idResponse) => {
            const imageIds = idResponse.data.list;

            for (let i = 0; i < item.images.length; i++) {
              var imageInfo = new Object();
              imageInfo.src = item.images[i];
              imageInfo.file = new File([], '');
              imageInfo.id = imageIds[i];

              this.images.push(imageInfo);
            }
          })
          .catch((error) => {
            location.replace(contextPath + '/');
          });
      });
    },
    addImage: function () {
      if (this.images.length >= 10) {
        alert('이미지는 최대 10개까지 업로드할 수 있습니다.');
        return;
      }
      var imageInfo = new Object();
      imageInfo.src = null;
      imageInfo.file = new File([], '');
      imageInfo.id = -1;

      this.images.push(imageInfo);
    },
    removeImage: function (index) {
      if (this.images.length <= 0) return;

      this.images.splice(index, 1);
    },
    changeImageOrder: function (index, moveValue) {
      if (this.images.length <= 0) return;

      const newPosition = index + moveValue;
      if (newPosition < 0 || newPosition >= this.images.length) return;

      const tempImage = this.images.splice(index, 1)[0];
      this.images.splice(newPosition, 0, tempImage);
    },
    selectFile: function (index) {
      this.$refs.file[index].click();
    },
    onFileSelected(index, event) {
      var input = event.target;
      if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = (e) => {
          var imageInfo = new Object();
          imageInfo.src = e.target.result;
          imageInfo.file = input.files[0];
          imageInfo.id = -1;

          Vue.set(vmWrite.images, index, imageInfo);
        };
        reader.readAsDataURL(input.files[0]);
      }
    },
    upload: async function () {
      if (!this.validate()) return;

      this.isSubmitting = true;

      const data = {
        categoryId: this.categoryId,
        price: this.price,
        title: this.title,
        content: this.content,
      };

      if (this.itemId === '') {
        // 글 쓰기
        await axios
          .post('/api/items', data, {
            headers: {
              'Content-Type': `application/json`,
            },
          })
          .then((response) => {
            this.itemId = response.data.data.id;
            this.uploadImage();
          })
          .catch((error) => {
            this.isSubmitting = false;
          });
      } else {
        // 글 수정
        await axios
          .put('/api/items/' + this.itemId, data, {
            headers: {
              'Content-Type': `application/json`,
            },
          })
          .then((response) => {
            this.uploadImage();
          })
          .catch((error) => {
            this.isSubmitting = false;
          });
      }
    },
    uploadImage: function () {
      var formData = new FormData();
      for (let i = 0; i < this.images.length; i++) {
        if (this.images[i].file.size <= 0 && this.images[i].id === -1) continue;

        formData.append('files', this.images[i].file);
        formData.append('imageIds', +this.images[i].id);
      }

      axios
        .put('/api/items/' + this.itemId + '/images', formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        })
        .then((response) => {
          location.replace(contextPath + '/detail/' + this.itemId);
        })
        .finally(() => {
          this.isSubmitting = false;
        });
    },
    validate: function () {
      if (this.categoryId === '') {
        alert('카테고리를 선택해주세요.');
        return false;
      }
      if (this.title === '') {
        alert('제목을 입력해주세요.');
        return false;
      }
      if (this.title.length > 40) {
        alert('제목은 최대 40자까지 입력할 수 있습니다.');
        return false;
      }
      if (this.price === '') {
        alert('가격을 입력해주세요');
        return false;
      }
      if (this.price < 0 || this.price > 999999999) {
        alert('가격은 0~999999999 사이의 값만 입력할 수 있습니다.');
        return false;
      }
      if (this.content === '') {
        alert('내용을 입력해주세요.');
        return false;
      }
      if (this.content.length > 4000) {
        alert('내용은 최대 4000자까지 입력할 수 있습니다.');
        return false;
      }
      if (this.images.length > 10) {
        alert('이미지는 최대 10장까지 업로드할 수 있습니다.');
        return false;
      }
      return true;
    },
  },
});

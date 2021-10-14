var vmSettingEditAddress = new Vue({
  el: '#settingEditAddress',
  data: {
    currUserId: currUserId,

    addressKeyword: '',
    addresses: [],
    selected: [],
    name: '',
    code: '',
    oldCode: '',
    isSubmitting: false,
  },
  created() {
    this.findAddress();
  },
  methods: {
    findAddress: function () {
      axios.get('/api/users/' + this.currUserId).then((response) => {
        this.name = response.data.data.address;
        this.code = response.data.data.code;
        this.oldCode = response.data.data.code;
      });
    },
    searchAddress: function () {
      if (this.addressKeyword.length < 2) {
        alert('2자 이상 입력해주세요.');
        return;
      }

      axios.get('/api/addresses?dongName=' + this.addressKeyword).then((response) => {
        if (response.status === 204) {
          alert('검색 결과가 없습니다.');
        } else {
          this.addresses = response.data.list;
          this.selected = { code: this.addresses[0].code, name: this.addresses[0].name };

          this.$refs.select.focus();
        }
      });
    },
    resetSearch: function () {
      this.addressKeyword = '';
      this.addresses = '';
      this.selected = '';
    },
    selectAddress: function () {
      this.code = this.selected.code;
      this.name = this.selected.name;
    },
    updateAddress: function () {
      if(this.isSubmitting) return;
      if (this.code === this.oldCode) {
        alert('기존 값과 동일합니다.');
        return;
      }

      this.isSubmitting = true;
      const data = {
        addressCode: this.code,
      };
      axios
        .put('/api/users/', data, { headers: {
          'Content-Type': 'application/json',
        } })
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

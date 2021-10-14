var vmSignup = new Vue({
  el: '#signup',
  data: {
    id: '',
    password: '',
    rePassword: '',
    email: '',
    nickname: '',
    address: {
      code: '',
      name: '',
    },

    addressKeyword: '',
    addresses: [],
    selectedAddress: [],
    code: '',
    name: '',

    isDuplicated: '',
    isSubmitting: false,
  },
  created() {},
  methods: {
    checkDuplicateId: function () {
      var idRegexp = /^[a-zA-Z0-9]{4,16}$/;
      if (!idRegexp.test(this.id)) {
        alert('아이디는 4~16자 이내의 영문, 숫자만 사용 가능합니다.');
        return;
      }

      axios.get('/api/users/' + this.id + '/exists').then((response) => {
        this.isDuplicated = response.data.data;
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
          this.selectedAddress = { code: this.addresses[0].code, name: this.addresses[0].name };
          this.$refs.select.focus();
        }
      });
    },
    resetSearch: function () {
      this.addressKeyword = '';
      this.addresses = '';
      this.selectedAddress = '';
    },
    selectAddress: function () {
      this.address = this.selectedAddress;
    },
    submit: async function () {
      if (!this.validate()) return;
      if (!confirm('회원가입 하시겠습니까? 입력하신 정보가 올바른지 확인해주세요.')) return;

      this.isSubmitting = true;
      const data = {
        id: this.id,
        password: this.password,
        nickname: this.nickname,
        email: this.email,
        addressCode: this.address.code,
      };
      await axios
        .post('/api/users', data, {
          headers: {
            'Content-Type': `application/json`,
          },
        })
        .then((response) => {
          location.replace(contextPath + '/verify-email');
        });
      this.isSubmitting = false;
    },

    validate: function () {
      var idRegexp = /^[a-zA-Z\d]{4,16}$/;
      if (!idRegexp.test(this.id)) {
        alert('아이디는 4~16자 이내의 영문, 숫자만 사용할 수 있습니다.');
        return false;
      }
      if (this.isDuplicated === '') {
        alert('아이디 중복체크를 진행해주세요.');
        return false;
      }
      if (this.isDuplicated) {
        alert('중복된 아이디입니다.');
        return false;
      }
      var passwordRegexp = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{8,16}$/;
      if (!passwordRegexp.test(this.password)) {
        alert('비밀번호는 8~16자 이내의 영문, 숫자 조합만 사용할 수 있습니다.');
        return false;
      }
      if (this.password !== this.rePassword) {
        alert('비밀번호와 비밀번호 확인 값이 일치하지 않습니다.');
        return false;
      }
      if (this.nickname.length < 2 || this.nickname.length > 10) {
        alert('닉네임은 2~10자만 사용합니다.');
        return false;
      }
      var emailRegexp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
      if (!emailRegexp.test(this.email)) {
        alert('이메일 형식이 올바르지 않습니다.');
        return false;
      }
      var reg = /^[0-9]{10}$/;
      if (reg.test(this.addressCode)) {
        alert('주소코드 형식이 올바르지 않습니다.');
        return false;
      }
      if (this.address.code == null || this.address.code === '') {
        alert('주소를 입력해주세요.');
        return false;
      }
      return true;
    },
  },
});

var vmChat = new Vue({
  el: '#chat',
  data: {
    chat: {
      list: [],
      page: 1,
      hasMore: true,
    },
  },
  created() {
    this.findChatList();
  },
  methods: {
    findChatList: function () {
      var searchParams = new URLSearchParams();
      searchParams.append('page', this.chat.page);
      searchParams.append('pageSize', PAGE_SIZE);

      axios.get('/api/chats', { params: searchParams }).then((response) => {
        if (response.data.list) {
          this.chat.list.push(...response.data.list);

          if (response.data.totalCount <= PAGE_SIZE * this.chat.page) this.chat.hasMore = false;

          this.chat.page++;
        } else {
          this.chat.hasMore = false;
        }
      });
    },
    dateFormat: function (date) {
      date = new Date(date);

      var year = date.getFullYear();
      var month = ('0' + (date.getMonth() + 1)).slice(-2);
      var day = ('0' + date.getDate()).slice(-2);
      var hours = ('0' + date.getHours()).slice(-2);
      var minutes = ('0' + date.getMinutes()).slice(-2);
      var seconds = ('0' + date.getSeconds()).slice(-2);

      var dateString = year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + seconds;
      return dateString;
    },
  },
});

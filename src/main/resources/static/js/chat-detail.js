// websocket & stomp initialize
var numberOfRetries = 5;
var sock;
var client;

var vmChatDetail = new Vue({
  el: '#chatDetail',
  data: {
    userId: userId,
    accessToken: accessToken,
    currUserId: currUserId,

    user: '',
    chatId: '',
    chat: [],
    messages: [],
    messageLimit: 10,
    hasMoreMessage: true,
    isConnected: false,
    message: '',
  },
  async created() {
    await this.findUser();
    await this.findChat();
    if (this.chatId === '') {
      await this.createChat();
      location.reload();
    }

    sock = new SockJS(contextPath + '/ws-stomp');
    client = Stomp.over(sock);
    client.connect(
      { token: this.accessToken },
      (frame) => {
        this.findMessages();
        this.isConnected = true;
        client.subscribe('/sub/chat/room/' + this.chatId, (message) => {
          var recv = JSON.parse(message.body);
          this.recvMessage(recv);
        });
      },
      (error) => {
        alert('서버 연결에 실패했습니다. 다시 접속해주세요.');
        location.href = contextPath + '/chat';
      }
    );

  },
  methods: {
    recvMessage: function (recv) {
      this.messages.unshift({
        from: recv.from,
        message: recv.message,
        createDate: new Date(),
      });
    },
    sendMessage: function () {
      if (this.message === '') {
        alert('채팅 내용을 입력해주세요.');
        return;
      }

      const data = {
        chatId: this.chatId,
        message: this.message,
      };
      this.message = '';
      client.send('/pub/chat/message', { token: this.accessToken }, JSON.stringify(data));
    },
    findChat: async function () {
      await axios
        .get('/api/chats/' + this.userId + "/id")
        .then((response) => {
          if (response.status !== 204) {
            this.chatId = response.data.data.id;
          }
        });
    },
    createChat: async function () {
      const data = { userId: this.userId };
      await axios
        .post('/api/chats', data, { headers: {
          'Content-Type': 'application/json'
        } })
        .then((response) => {
          this.chatId = response.data.data.id;
        });
    },
    findMessages: function () {
      var params = new URLSearchParams();
      params.append('limit', this.messageLimit);

      axios.get('/api/chats/' + this.chatId + '/messages', { params: params }).then((response) => {
        this.messages = response.data.list;
        if (this.messages.length <= this.messageLimit - 10) {
          this.hasMoreMessage = false;
        } else {
          this.hasMoreMessage = true;
          this.messageLimit += 10;
        }
      });
    },
    findUser: async function () {
      await axios.get('/api/users/' + this.userId).then((response) => {
        this.user = response.data.data;
        if(this.user.status !== "ACTIVE"){
          alert("탈퇴한 회원입니다.");
          location.href = contextPath + "/chat";
        }
      })
      .catch((error) => {
        location.replace(contextPath + '/chat');
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

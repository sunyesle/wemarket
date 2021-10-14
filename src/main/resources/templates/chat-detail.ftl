<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<!DOCTYPE html>
<@security.authorize access="hasAnyRole('ROLE_USER', 'ROLE_ADMIN')">
<html lang="ko">
<head>
  <#include "/head.ftl">
  <!-- websocket & stomp -->
  <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
  <title>위마켓</title>
</head>
<body style="height: 100vh">
<#include "/header.ftl">

<div class="container mt--6" style="height: 80%" id="chatDetail">
  <div class="card card-frame mx-lg-6 h-100">
    <div class="p-4 h-100">
      <div class="d-flex flex-column mb-3 h-100">
        <h4 class="pb-1 mb-0" style="border-bottom: 2px solid #5e72e4"><a v-bind:href="'${rc.getContextPath()}/profile/'+user.id+'/listing'">{{user.nickname}} ({{user.id}})</a></h4>
        <div class="h-100 px-sm-3 pt-3" style="overflow-y: scroll !important; display: flex; flex-direction: column-reverse">
          <template v-for="message in messages">
            <div class="d-flex justify-content-end py-2" v-if="currUserId === message.from">
              <div class="row w-75 mx-0">
                <div class="col-12 text-right pr-0">
                  <small class="text-muted">{{dateFormat(message.createDate)}}</small>
                </div>
                <div class="col-12 bg-chat-r rounded px-2 py-3 h4 font-weight-normal">{{message.message}}</div>
              </div>
            </div>
            <div class="d-flex justify-content-start py-2" v-else>
              <div class="row w-75 mx-0">
                <small class="col-auto text-left text-muted font-weight-bold px-0">{{user.nickname}}</small>
                <small class="col text-right text-muted px-0">{{dateFormat(message.createDate)}}</small>
                <div class="col-12 bg-lighter rounded px-2 py-3 h4 font-weight-normal">{{message.message}}</div>
              </div>
            </div>
          </template>
          <button type="button" class="btn border rounded w-100 mt-2" v-if="hasMoreMessage" v-on:click="findMessages">더보기</button>
          <h4 class="text-center text-muted" v-if="!isConnected">
            <span class="spinner-border spinner-border-sm mr-1" role="status" aria-hidden="true"></span>채팅방 연결중...
          </h4>
        </div>
        <div class="input-group">
          <input type="text" class="form-control" v-model="message" v-on:keypress.enter="sendMessage"/>
          <div class="input-group-append">
            <button class="btn btn-outline-primary" type="button" v-on:click="sendMessage">전송</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<script src="${rc.getContextPath()}/static/js/chat-detail.js"></script>

</body>
</html>
</@security.authorize>
<@security.authorize access="hasRole('ROLE_UNVERIFIED')">
<script>
  alert('접근 권한이 없습니다. 서비스 이용을 위해 이메일 인증을 진행해주세요.');
  location.replace("${rc.getContextPath()}/");
</script>
</@security.authorize>
<@security.authorize access="isAnonymous()">
<script>
  alert('접근 권한이 없습니다.');
  location.replace("${rc.getContextPath()}/");
</script>
</@security.authorize>
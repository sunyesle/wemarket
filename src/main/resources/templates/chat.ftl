<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<!DOCTYPE html>
<@security.authorize access="hasAnyRole('ROLE_USER', 'ROLE_ADMIN')">
<html lang="ko">
<head>
  <#include "/head.ftl">
  <title>위마켓</title>
</head>
<body>
<#include "/header.ftl">

<div class="container mt--6" id="chat" v-cloak>
  <div class="card card-frame mx-lg-6">
    <div class="p-4">
      <h4 class="pb-1 mb-3" style="border-bottom: 2px solid #5e72e4">채팅방</h4>
      <ul class="list-group list-group-flush list my--3" v-if="chat.list.length > 0">
        <li class="list-group-item px-0" v-for="chat in chat.list" v-bind:key="chat.id">
          <h4 class="mb-0">
            <a v-bind:href="'${rc.getContextPath()}/chat/'+chat.to">{{chat.toNickname}}</a>
          </h4>
          <small class="text-muted">{{dateFormat(chat.updateDate)}}</small>
        </li>
        <button type="button" class="btn border rounded w-100 mt-2" v-if="chat.hasMore" v-on:click="findChatList">더보기</button>
      </ul>
      <h4 class="text-muted font-weight-normal" v-else>개설된 채팅방이 없습니다.</h4>
    </div>
  </div>
</div>
<script src="${rc.getContextPath()}/static/js/chat.js"></script>

<#include "/footer.ftl">
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
<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<!DOCTYPE html>
<@security.authorize access="isAuthenticated()">
<html lang="ko">
<head>
  <#include "/head.ftl">
  <title>설정 | 위마켓</title>
</head>
<body>
<#include "/header.ftl">

<div class="container mt--6" id="setting" v-cloak>
  <div class="card card-frame mx-lg-6">
    <div class="p-4">
      <div class="row">
        <div class="col">
          <h4>프로필 이미지</h4>
        </div>
        <div class="col-auto">
          <a href="/setting/image" class="btn btn-outline-primary btn-sm">수정</a>
        </div>
      </div>
      <hr class="my-3">
      <div class="row">
        <div class="col">
          <h4>지역</h4>
          <h4 class="text-muted font-weight-normal mb-0">{{user.address}}</h4>
        </div>
        <div class="col-auto">
          <a href="/setting/address" class="btn btn-outline-primary btn-sm">수정</a>
        </div>
      </div>
      <hr class="my-3">
      <div class="row">
        <div class="col">
          <h4>이메일</h4>
          <h4 class="text-muted font-weight-normal mb-0">{{user.email}}</h4>
        </div>
        <div class="col-auto">
          <a href="/setting/email" class="btn btn-outline-primary btn-sm">수정</a>
        </div>
      </div>
      <hr class="my-3">
      <div class="row">
        <div class="col">
          <h4>프로필 설명</h4>
          <h4 class="text-muted font-weight-normal mb-0">{{user.bio}}</h4>
        </div>
        <div class="col-auto">
          <a href="/setting/bio" class="btn btn-outline-primary btn-sm">수정</a>
        </div>
      </div>
      <hr class="my-3">
      <div class="row">
        <div class="col">
          <h4>비밀번호</h4>
          <h4 class="text-muted font-weight-normal mb-0">마지막 변경일: {{dateFormat(user.passwordChangeDate)}}</h4>
        </div>
        <div class="col-auto">
          <a href="/setting/password" class="btn btn-outline-primary btn-sm">수정</a>
        </div>
      </div>
      <hr class="my-3">
      <div class="row">
        <div class="col">
          <h4>계정 삭제</h4>
        </div>
        <div class="col-auto">
          <a href="${rc.getContextPath()}/setting/account" class="btn btn-outline-primary btn-sm">수정</a>
        </div>
      </div>
    </div>
  </div>
</div>
<script src="${rc.getContextPath()}/static/js/setting/main.js"></script>

<#include "/footer.ftl">
</body>
</html>
</@security.authorize>
<@security.authorize access="isAnonymous()">
<script>
  alert('접근 권한이 없습니다.');
  location.replace("${rc.getContextPath()}/");
</script>
</@security.authorize>
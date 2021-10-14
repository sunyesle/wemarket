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

<div class="container mt--6" id="settingEditPassword" v-cloak>
  <div class="card card-frame mx-lg-6">
    <div class="p-4">
      <div class="form-group">
        <label class="form-control-label">현재 비밀번호</label>
        <input type="password" class="form-control" maxlength="16" v-model="oldPassword" />
      </div>
      <div class="form-group">
        <label class="form-control-label">새 비밀번호</label>
        <small>(8~16자 이내, 영문+숫자 조합)</small>
        <input type="password" class="form-control" maxlength="16" v-model="password" />
      </div>
      <div class="form-group">
        <label class="form-control-label">비밀번호 확인</label>
        <input type="password" class="form-control" maxlength="16" v-model="rePassword" />
      </div>
      <div class="text-center mt-3">
        <button type="button" class="btn btn-primary" v-on:click="updatePassword" v-bind:disabled="isSubmitting">
          <span class="spinner-border spinner-border-sm mr-1" role="status" aria-hidden="true" v-if="isSubmitting"></span>변경
        </button>
      </div>
    </div>
  </div>
</div>
<script src="${rc.getContextPath()}/static/js/setting/password.js"></script>

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
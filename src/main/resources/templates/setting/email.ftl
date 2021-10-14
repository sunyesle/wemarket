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

<div class="container mt--6" id="settingEditEmail" v-cloak>
  <div class="card card-frame mx-lg-6">
    <div class="p-4">
      <div class="form-group">
        <label class="form-control-label">이메일</label>
        <input type="text" class="form-control" maxlength="320" v-model.trim="email" />
      </div>
      <h4 class="font-weight-normal mb-3 text-danger">이메일 변경 시 새로운 이메일로 인증 메일이 발송됩니다. 인증 이전까지 서비스 이용이 제한됩니다.</h4>
      <div class="text-center mt-3">
        <button type="button" class="btn btn-primary" v-on:click="updateEmail" v-bind:disabled="isSubmitting">
          <span class="spinner-border spinner-border-sm mr-1" role="status" aria-hidden="true" v-if="isSubmitting"></span>변경
        </button>
      </div>
    </div>
  </div>
</div>
<script src="${rc.getContextPath()}/static/js/setting/email.js"></script>

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
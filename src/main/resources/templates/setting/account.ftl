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

<div class="container mt--6" id="settingAccount" v-cloak>
  <div class="card card-frame mx-lg-6">
    <div class="p-4">
      <h4>계정 삭제</h4>
      <h4 class="font-weight-normal mb-3 text-danger">계정 삭제 시 더 이상 서비스를 이용할 수 없습니다. 삭제한 계정은 복구할 수 없습니다.</h4>
      <div class="form-group">
        <label class="form-control-label">비밀번호 확인</label>
        <input type="password" class="form-control" maxlength="16" v-model="password">
      </div>
      <div class="custom-control custom-checkbox">
        <input type="checkbox" class="custom-control-input" id="deleteCheck" v-model="isChecked">
        <label class="custom-control-label" for="deleteCheck">계정을 삭제합니다.</label>
      </div>
      <div class="text-center mt-3">
        <button type="button" class="btn btn-primary" v-on:click="deleteAccount" v-bind:disabled="isSubmitting">
          <span class="spinner-border spinner-border-sm mr-1" role="status" aria-hidden="true" v-if="isSubmitting"></span>계정 삭제
        </button>
      </div>
    </div>
  </div>
</div>
<script src="${rc.getContextPath()}/static/js/setting/account.js"></script>

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
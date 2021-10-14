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

<div class="container mt--6" id="settingEditBio" v-cloak>
  <div class="card card-frame mx-lg-6">
    <div class="p-4">
      <div class="form-group">
        <label class="form-control-label">프로필 설명</label>
        <textarea class="form-control" id="exampleFormControlTextarea1" rows="3" placeholder="최대 100자" maxlength="100" onKeyPress="javascript: if (event.keyCode==13) return false;" v-model="bio"></textarea>
      </div>
      <div class="text-center mt-3">
        <button type="button" class="btn btn-primary" v-on:click="updateBio" v-bind:disabled="isSubmitting">
          <span class="spinner-border spinner-border-sm mr-1" role="status" aria-hidden="true" v-if="isSubmitting"></span>변경
        </button>
      </div>
    </div>
  </div>
</div>
<script src="${rc.getContextPath()}/static/js/setting/bio.js"></script>

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
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

<div class="container mt--6" id="settingEditImage">
  <div class="card card-frame mx-lg-6">
    <div class="p-4">
      <label class="form-control-label">프로필 이미지</label>

      <div class="mb-3">
        <img src="${rc.getContextPath()}/static/assets/img/common/default.png" class="rounded-circle img-center border ys-profile-img" v-if="src==null" />
        <img v-bind:src="src" class="rounded-circle img-center border ys-profile-img" onerror="this.src='${rc.getContextPath()}/static/assets/img/common/default.png';this.onerror=null;" v-else />
      </div>
      <form>
        <div class="custom-file">
          <input type="file" class="custom-file-input" accept=".jpg, .png" v-on:change="onFileSelected($event)" />
          <label class="custom-file-label" for="customFileLang">Select file</label>
        </div>
      </form>
      <div class="text-center mt-3">
        <button type="button" class="btn btn-primary" v-on:click="updateImage" v-bind:disabled="isSubmitting">
          <span class="spinner-border spinner-border-sm mr-1" role="status" aria-hidden="true" v-if="isSubmitting"></span>변경
        </button>
      </div>
    </div>
  </div>
</div>
<script src="${rc.getContextPath()}/static/js/setting/image.js"></script>

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
<!DOCTYPE html>
<html lang="ko">
<head>
  <#include "/head.ftl">
  <title>위마켓</title>
</head>
<body>
<#include "/header.ftl">

<div class="container mt--6" id="verify" v-if="isSuccess" v-cloak>
  <div class="card card-frame mx-lg-6">
    <div class="p-4">
      <div class="row justify-content-center align-items-center">
        <div class="col-auto">
          <img src="${rc.getContextPath()}/static/assets/img/common/049-kitty-1.svg" style="height: 160px" />
        </div>
        <div class="col-auto my-3">
          <h4>축하합니다! 이메일 인증이 완료되었습니다.</h4>
          <h4>지금 바로 위마켓을 이용해보세요.</h4>
        </div>
      </div>
      <div class="text-center mt-5">
        <a href="${rc.getContextPath()}/" class="btn btn-primary">홈으로</a>
      </div>
    </div>
  </div>
</div>
<script src="${rc.getContextPath()}/static/js/verify.js"></script>

<#include "/footer.ftl">
</body>
</html>
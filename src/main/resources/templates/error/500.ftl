<!DOCTYPE html>
<html lang="ko">
<head>
  <#include "/head.ftl">
  <title>위마켓</title>
</head>
<body>
<#include "/header.ftl">

<div class="container mt--6" id="verify">
  <div class="card card-frame mx-lg-6">
    <div class="p-4">
      <div class="row justify-content-center align-items-center">
        <div class="col-auto">
          <img src="${rc.getContextPath()}/static/assets/img/common/037-kitty-13.svg" style="height: 160px" />
        </div>
        <div class="col-auto my-3">
          <h2 class="text-primary" style="font-size: 2.75rem; font-weight: 600; line-height: 1.5">500 ERROR</h2>
          <h2 class="mb-4">서비스에 접속할 수 없습니다.</h2>
          <h4>서비스 이용에 불편을 드려 죄송합니다.</h4>
          <h4>잠시 후 다시 확인해주세요.</h4>
        </div>
      </div>
      <div class="text-center mt-5">
        <a href="${rc.getContextPath()}/" class="btn btn-primary">홈으로</a>
      </div>
    </div>
  </div>
</div>

<#include "/footer.ftl">
</body>
</html>

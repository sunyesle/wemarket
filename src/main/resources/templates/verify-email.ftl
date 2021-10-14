<!DOCTYPE html>
<html lang="ko">
<head>
  <#include "/head.ftl">
  <title>위마켓</title>
</head>
<body>
<#include "/header.ftl">

<div class="container mt--6">
  <div class="card card-frame mx-lg-6">
    <div class="p-4">
      <div class="row justify-content-center align-items-center">
        <div class="col-auto">
          <img src="${rc.getContextPath()}/static/assets/img/common/049-kitty-1.svg" style="height: 160px" />
        </div>
        <div class="col-auto my-3 text-center">
          <h4>입력하신 이메일로 인증 메일이 발송되었습니다.</h4>
          <h4>서비스 이용을 위해 이메일 인증을 진행해주세요.</h4>
        </div>
      </div>
      <div class="text-center mt-3">
        <a href="${rc.getContextPath()}/" class="btn btn-primary">홈으로</a>
      </div>
      <hr />
      <p class="h5 text-muted font-weight-normal">
        인증 메일이 도착하지 않으셨나요?<br />
        - 스팸 메일함을 확인해주세요.<br />
        - 로그인 후 설정 페이지(홈>우측상단 프로필 탭>설정)에서 이메일이 올바른지 확인해주세요.
      </p>
    </div>
  </div>
</div>

<#include "/footer.ftl">
</body>
</html>
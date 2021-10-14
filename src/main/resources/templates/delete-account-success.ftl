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
      <div class="my-3 text-center">
        <h4>계정 삭제가 완료되었습니다.</h4>
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
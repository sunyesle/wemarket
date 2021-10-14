<!DOCTYPE html>
<html lang="ko">
<head>
  <#include "/head.ftl">
  <title>로그인 | 위마켓</title>
</head>
<body>
<#include "/header.ftl">

<div class="container mt--6" id="signin" style="max-width: 500px;" v-cloak>
  <div class="card card-frame">
    <div class="p-4">
      <div class="card-body px-lg-5 py-lg-5">
        <h1 class="text-center mb-4 font-weight-light">SIGN IN</h1>
        <input type="text" class="form-control mb-3" placeholder="아이디" v-model="id">
        <input type="password" class="form-control" placeholder="비밀번호" v-model="password" v-on:keypress.enter="signin">
        <div class="text-right">
          <a class="small text-muted" href="${rc.getContextPath()}/signup">회원가입</a>
        </div>
        <div class="text-center">
          <button type="button" class="btn btn-primary mt-4" v-on:click="signin" v-bind:disabled="isSubmitting">로그인</button>
        </div>
      </div>
    </div>
  </div>
</div>
<script src="${rc.getContextPath()}/static/js/signin.js"></script>

<#include "/footer.ftl">
</body>
</html>
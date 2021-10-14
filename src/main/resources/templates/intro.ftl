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
    <div class="p-4 text-center" style="word-break: keep-all">
      <h1 class="ys-text-xxl mt-5"><span class="ys-text-highlight">우리</span>가 함께 만드는 <span class="ys-text-highlight">We</span>Market</h1>
      <h3 class="font-weight-normal mb-6">위마켓에서 중고거래를 시작해보세요</h3>

      <div class="row justify-content-between align-items-center mb-6 px-0 px-md-2 px-lg-5 px-xl-7">
        <div class="col-7 col-sm-6 px-1 px-sm-3">
          <img class="rounded shadow-sm" src="${rc.getContextPath()}/static/assets/img/common/intro_write.png" style="width: 100%; max-width: 360px" />
        </div>
        <div class="col-5 col-sm-6 text-left my-2 px-1 px-sm-3">
          <h1 class="ys-text-xl d-inline d-sm-block mb-0">사진과 함께</h1>
          <h1 class="ys-text-xl d-inline d-sm-block"><span class="ys-text-highlight">판매</span>글을 올려보세요</h1>
        </div>
      </div>

      <div class="row justify-content-between align-items-center mb-6 px-0 px-md-2 px-lg-5 px-xl-7">
        <div class="col-5 col-sm-6 text-left my-2 px-1 px-sm-3">
          <h1 class="ys-text-xl d-inline d-sm-block mb-0">마음에 드는 상품에</h1>
          <h1 class="ys-text-xl d-inline d-sm-block"><span class="ys-text-highlight">제안</span>해보세요</h1>
        </div>
        <div class="col-7 col-sm-6 px-1 px-sm-3">
          <img class="rounded shadow-sm" src="${rc.getContextPath()}/static/assets/img/common/intro_offer1.png" style="width: 100%; max-width: 360px" />
        </div>
      </div>

      <div class="row justify-content-between align-items-center mb-6 px-0 px-md-2 px-lg-5 px-xl-7">
        <div class="col-7 col-sm-6 px-1 px-sm-3">
          <img class="rounded shadow-sm" src="${rc.getContextPath()}/static/assets/img/common/intro_offer2.png" style="width: 100%; max-width: 360px" />
        </div>
        <div class="col-5 col-sm-6 text-left my-2 px-1 px-sm-3">
          <h1 class="ys-text-xl d-inline d-sm-block mb-0">판매자는 제안을</h1>
          <h1 class="ys-text-xl d-inline d-sm-block"><span class="ys-text-highlight">수락</span> 또는 <span class="ys-text-highlight">거절</span>할 수 있어요</h1>
        </div>
      </div>

      <div class="row justify-content-between align-items-center mb-6 px-0 px-md-2 px-lg-5 px-xl-7">
        <div class="col-5 col-sm-6 text-left my-2 px-1 px-sm-3">
          <h1 class="ys-text-xl d-inline d-sm-block mb-0">연락처 교환 없이</h1>
          <h1 class="ys-text-xl d-inline d-sm-block"><span class="ys-text-highlight">1:1채팅</span>을 통해 대화할 수 있어요</h1>
        </div>
        <div class="col-7 col-sm-6 px-1 px-sm-3">
          <img class="rounded shadow-sm" src="${rc.getContextPath()}/static/assets/img/common/intro_chat.png" style="width: 100%; max-width: 360px" />
        </div>
      </div>

      <div class="row justify-content-between align-items-center mb-6 px-0 px-md-2 px-lg-5 px-xl-7">
        <div class="col-7 col-sm-6 px-1 px-sm-3">
          <img class="rounded shadow-sm" src="${rc.getContextPath()}/static/assets/img/common/intro_review.png" style="width: 100%; max-width: 360px" />
        </div>
        <div class="col-5 col-sm-6 text-left my-2 px-1 px-sm-3">
          <h1 class="ys-text-xl d-inline d-sm-block mb-0">거래 후 상대방에게</h1>
          <h1 class="ys-text-xl d-inline d-sm-block"><span class="ys-text-highlight">리뷰</span>를 남겨보세요</h1>
        </div>
      </div>

      <div class="row justify-content-between mb-6 px-0 px-md-2 px-lg-5 px-xl-7">
        <div class="col-6 px-1 px-sm-3 d-flex align-items-end" style="z-index: 10">
          <img class="rounded shadow-sm" src="${rc.getContextPath()}/static/assets/img/common/intro_main_pc.png" style="width: 175%" />
        </div>
        <div class="col-6 px-1 px-sm-3 d-flex flex-row-reverse">
          <img class="rounded shadow-sm" src="${rc.getContextPath()}/static/assets/img/common/intro_main_mobile.png" style="width: 100%" />
        </div>
        <div class="col-12 text-center my-3">
          <h1 class="ys-text-xl mb-0"><span class="ys-text-highlight">PC</span>, <span class="ys-text-highlight">모바일</span> 어디에서나 위마켓!</h1>
        </div>
      </div>
      <a href="${rc.getContextPath()}/" class="btn btn-primary mb-5">홈으로</a>
    </div>
  </div>
</div>


<#include "/footer.ftl">
</body>
</html>
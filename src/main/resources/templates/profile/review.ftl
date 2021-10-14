<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<!DOCTYPE html>
<html lang="ko">
<head>
  <#include "/head.ftl">
  <title>위마켓</title>
</head>
<body>
<#include "/header.ftl">
<#include "/profile/header.ftl">

<div class="container" id="profileReview" v-if="!isDeletedUser" v-cloak>
  <!-- 탭 -->
  <div class="nav nav-pills nav-fill mx--2 px-lg-6 mt-3 mb-0 mb-sm-3" v-if="currUserId === '' || currUserId !== userId">
    <div class="nav-item col-4 col-sm-2 px-2">
      <a class="nav-link px-0" v-bind:href="'${rc.getContextPath()}/profile/' + userId + '/listing'">판매상품</a>
    </div>
    <div class="nav-item col-4 col-sm-2 px-2">
      <a class="nav-link px-0 active pointer">리뷰</a>
    </div>
  </div>
  <div class="nav nav-pills nav-fill mx--2 px-lg-6 mt-3 mb-0 mb-sm-3" v-else>
    <div class="nav-item col-4 col-sm-2 px-2">
      <a class="nav-link px-0" v-bind:href="'${rc.getContextPath()}/profile/' + userId + '/listing'">판매상품</a>
    </div>
    <div class="nav-item col-4 col-sm-2 px-2">
      <a class="nav-link px-0" v-bind:href="'${rc.getContextPath()}/profile/' + userId + '/offer'">받은제안</a>
    </div>
    <div class="nav-item col-4 col-sm-2 px-2">
      <a class="nav-link px-0" v-bind:href="'${rc.getContextPath()}/profile/' + userId + '/myoffer'">내제안</a>
    </div>
    <div class="nav-item col-4 col-sm-2 px-2">
      <a class="nav-link px-0" v-bind:href="'${rc.getContextPath()}/profile/' + userId + '/wishlist'">관심상품</a>
    </div>
    <div class="nav-item col-4 col-sm-2 px-2">
      <a class="nav-link px-0" v-bind:href="'${rc.getContextPath()}/profile/' + userId + '/following'">팔로잉</a>
    </div>
    <div class="nav-item col-4 col-sm-2 px-2">
      <a class="nav-link px-0 active pointer">리뷰</a>
    </div>
  </div>
  <div class="card mx-lg-6">
    <div class="card-body">
      <div class="border-bottom pb-3" v-if="currUserId !== '' && currUserId !== userId">
        <span class="h2 text-success pointer">
        <span v-on:click="rating(1)">{{getRating(1)}}</span><span v-on:click="rating(2)">{{getRating(2)}}</span><span v-on:click="rating(3)">{{getRating(3)}}</span><span v-on:click="rating(4)">{{getRating(4)}}</span><span v-on:click="rating(5)">{{getRating(5)}}</span>
        </span>
        <div class="mb-2">
          <div class="custom-control custom-radio custom-control-inline">
            <input type="radio" id="reviewTypeRadio1" name="reviewTypeRadio" class="custom-control-input" value="BUY" v-model="myReview.reviewType">
            <label class="custom-control-label" for="reviewTypeRadio1">구매자</label>
          </div>
          <div class="custom-control custom-radio custom-control-inline">
            <input type="radio" id="reviewTypeRadio2" name="reviewTypeRadio" class="custom-control-input" value="SELL" v-model="myReview.reviewType">
            <label class="custom-control-label" for="reviewTypeRadio2">판매자</label>
          </div>
        </div>
        <input type="text" class="form-control" maxlength="400" placeholder="내용" v-model.number="myReview.content">

        <div class="text-right mt-3">
          <button type="button" class="btn btn-default" v-on:click="writeReview" v-bind:disabled="isDisabled" v-if="myReview.id === ''">작성</button>
          <template v-else>
            <button type="button" class="btn btn-default" v-on:click="writeReview" v-bind:disabled="isDisabled">수정</button>
            <button type="button" class="btn btn-danger" v-on:click="deleteReview" v-bind:disabled="isDisabled">삭제</button>
          </template>
          </div>
      </div>
      <ul class="list-group list-group-flush">
        <li class="list-group-item px-0" v-for="review in review.list" v-bind:key="review.id">
          <div class="row">
            <div class="col">
              <h4 class="mb-0">
                <a v-bind:href="'${rc.getContextPath()}/profile/' + review.writerId + '/listing'">{{review.writerNickname}}</a>
                <span class="h5 font-weight-light" v-if="review.reviewType==='BUY'"> - 구매자</span>
                <span class="h5 font-weight-light" v-else-if="review.reviewType==='SELL'"> - 판매자</span>
              </h4>
              <h4 class="text-success mb-0">{{toRatingStar(review.rating)}}</h4>
              <h5 class="text-muted font-weight-light">{{dateFormat(review.createDate)}}</h5>
              <h4 class="mb-0 font-weight-normal">{{review.content}}</h4>
            </div>
          </div>
        </li>
      </ul>
      <infinite-loading @infinite="reviewInfiniteHandler" spinner="spiral" ref="reviewInfiniteLoading">
        <div slot="no-more"></div>
        <div slot="no-results"></div>
      </infinite-loading>
    </div>
  </div>
</div>
<script src="${rc.getContextPath()}/static/js/profile/review.js"></script>

<#include "/footer.ftl">
</body>
</html>
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

<div class="container" id="profileFollowing" v-cloak>
  <!-- 탭 -->
  <div class="nav nav-pills nav-fill mx--2 px-lg-6 mt-3 mb-0 mb-sm-3">
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
      <a class="nav-link px-0 active pointer">팔로잉</a>
    </div>
    <div class="nav-item col-4 col-sm-2 px-2">
      <a class="nav-link px-0" v-bind:href="'${rc.getContextPath()}/profile/' + userId + '/review'">리뷰</a>
    </div>
  </div>
  <div class="card mx-lg-6">
    <div class="card-body">
      <ul class="list-group list-group-flush">
        <li class="list-group-item px-0" v-for="(following, index) in following.list" v-bind:key="following.userId">
          <div class="row align-items-center">
            <div class="col">
              <h4 class="mb-1">
                <a v-bind:href="'${rc.getContextPath()}/profile/' + following.userId + '/listing'">{{following.userNickname}}</a>
              </h4>
              <small class="text-muted font-weight-light">{{dateFormat(following.createDate)}}</small>
            </div>
            <div class="col-auto">
              <button type="button" class="btn btn-outline-danger" v-on:click="unfollow(following.userId, index)">언팔로우</button>
            </div>
          </div>
        </li>
      </ul>
      <infinite-loading @infinite="followingInfiniteHandler" spinner="spiral">
        <div slot="no-more"></div>
        <div slot="no-results"></div>
      </infinite-loading>
    </div>
  </div>
</div>
<script src="${rc.getContextPath()}/static/js/profile/following.js"></script>

<#include "/footer.ftl">
</body>
</html>
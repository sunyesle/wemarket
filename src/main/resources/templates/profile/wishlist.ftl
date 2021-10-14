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

<div class="container" id="profileWishlist" v-cloak>
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
      <a class="nav-link px-0 active pointer">관심상품</a>
    </div>
    <div class="nav-item col-4 col-sm-2 px-2">
      <a class="nav-link px-0" v-bind:href="'${rc.getContextPath()}/profile/' + userId + '/following'">팔로잉</a>
    </div>
    <div class="nav-item col-4 col-sm-2 px-2">
      <a class="nav-link px-0" v-bind:href="'${rc.getContextPath()}/profile/' + userId + '/review'">리뷰</a>
    </div>
  </div>
  <div class="card mx-lg-6">
    <div class="card-body">
        <ul class="list-group list-group-flush">
          <li class="list-group-item px-0" v-for="item in wishlist.list" v-bind:key="wishlist.id">
            <div class="row align-items-center">
              <div class="col-auto">
                <img src="${rc.getContextPath()}/static/assets/img/common/default.png" class="rounded img-center border ys-list-img" v-if="item.thumbImage==null">
                <img v-bind:src="item.thumbImage" class="rounded img-center border ys-list-img" onerror="this.src='${rc.getContextPath()}/static/assets/img/common/default.png';this.onerror=null;" v-else>
              </div>
              <div class="col px-0">
                <h4 class="mb-1">
                  <a v-bind:href="'${rc.getContextPath()}/detail/' + item.id">{{item.title}}</a>
                </h4>
                <h4 class="mb-0 media align-items-center">
                  <span class="badge badge-success mr-1" v-if="item.status === 'TRADE'">거래중</span>
                  <span class="badge badge-default mr-1" v-else-if="item.status === 'COMPLETED'">거래완료</span>
                  {{numberFormat(item.price)}}원
                </h4>
                <small class="text-muted font-weight-light">{{dateFormat(item.createDate)}}</small>
              </div>
              <div class="col-auto" v-if="item.offerCount != 0">
                <h4 class="text-muted font-weight-light text-right media align-items-center">
                  <i class="ni ni-chat-round mr-1"></i>{{item.offerCount}}
                </h4>
              </div>
            </div>
          </li>
        </ul>
        <infinite-loading @infinite="wishlistInfiniteHandler" spinner="spiral">
          <div slot="no-more"></div>
          <div slot="no-results"></div>
        </infinite-loading>
    </div>
  </div>
</div>
<script src="${rc.getContextPath()}/static/js/profile/wishlist.js"></script>

<#include "/footer.ftl">
</body>
</html>
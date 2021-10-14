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

<div class="container" id="profileOffer" v-cloak>
  <!-- 탭 -->
  <div class="nav nav-pills nav-fill mx--2 px-lg-6 mt-3 mb-0 mb-sm-3">
    <div class="nav-item col-4 col-sm-2 px-2">
      <a class="nav-link px-0" v-bind:href="'${rc.getContextPath()}/profile/' + userId + '/listing'">판매상품</a>
    </div>
    <div class="nav-item col-4 col-sm-2 px-2">
      <a class="nav-link px-0 active pointer">받은제안</a>
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
      <a class="nav-link px-0" v-bind:href="'${rc.getContextPath()}/profile/' + userId + '/review'">리뷰</a>
    </div>
  </div>
  <div class="card mx-lg-6">
    <div class="card-body">
      <ul class="nav nav-tabs">
        <li>
          <a class="nav-link active" href="#offer-open" data-toggle="tab" aria-expanded="false">대기중 </a>
        </li>
        <li>
          <a class="nav-link" href="#offer-accepted" data-toggle="tab" aria-expanded="true" v-on:click.once="loadOfferAccepted"> 수락됨 </a>
        </li>
        <li>
          <a class="nav-link" href="#offer-declined" data-toggle="tab" aria-expanded="true" v-on:click.once="loadOfferDeclined"> 거절됨 </a>
        </li>
        <li>
          <a class="nav-link" href="#offer-history" data-toggle="tab" aria-expanded="true" v-on:click.once="loadOfferCompleted"> 히스토리 </a>
        </li>
      </ul>
      <div class="tab-content">
        <div class="tab-pane fade active show" id="offer-open">
          <ul class="list-group list-group-flush">
            <li class="list-group-item px-0" v-for="(offer, index) in offerOpen.list" v-bind:key="offer.id">
              <div class="row">
                <div class="col">
                  <h4 class="mb-0">
                    <span v-if="offer.itemTitle == null">삭제된 게시글</span>
                    <a v-bind:href="'${rc.getContextPath()}/detail/' + offer.itemId" v-else>{{offer.itemTitle}}</a>
                  </h4>
                  <h4 class="mb-0 text-primary">{{numberFormat(offer.price)}}원</h4>
                  <small class="text-muted font-weight-light">{{dateFormat(offer.createDate)}}</small>
                </div>
                <div class="col-auto">
                  <button type="button" class="btn btn-outline-danger" v-on:click="cancelOffer('open', index, offer.id)">취소</button>
                </div>
              </div>
            </li>
          </ul>
          <infinite-loading @infinite="offerOpenInfiniteHandler" spinner="spiral">
            <div slot="no-more"></div>
            <div slot="no-results"></div>
          </infinite-loading>
        </div>
        <div class="tab-pane fade in" id="offer-accepted">
          <ul class="list-group list-group-flush">
            <li class="list-group-item px-0" v-for="(offer, index) in offerAccepted.list" v-bind:key="offer.id">
              <div class="row">
                <div class="col">
                  <h4 class="mb-0 media align-items-center">
                    <span class="badge badge-success mr-1">거래중</span>
                    <span v-if="offer.itemTitle == null">삭제된 게시글</span>
                    <a v-bind:href="'${rc.getContextPath()}/detail/' + offer.itemId" v-else>{{offer.itemTitle}}</a>
                  </h4>
                  <h4 class="mb-0 text-success">{{numberFormat(offer.price)}}원&nbsp;<i class="fa fa-check"></i></h4>
                  <small class="text-muted font-weight-light">{{dateFormat(offer.createDate)}}</small>
                </div>
                <div class="col-auto">
                  <span class="text-success">
                    <a role="button" class="btn btn-outline-default mb-1 mr-0" v-bind:href="'${rc.getContextPath()}/chat/' + offer.sellerId">채팅</a>
                    <br>
                    <button type="button" class="btn btn-outline-danger" v-on:click="cancelOffer('accepted', index, offer.id)">취소</button>
                </div>
              </div>
            </li>
          </ul>
          <infinite-loading @infinite="offerAcceptedInfiniteHandler" spinner="spiral" ref="offerAcceptedInfiniteLoading">
            <div slot="no-more"></div>
            <div slot="no-results"></div>
          </infinite-loading>
        </div>
        <div class="tab-pane fade in" id="offer-declined">
          <ul class="list-group list-group-flush">
            <li class="list-group-item px-0" v-for="offer in offerDeclined.list" v-bind:key="offer.id">
              <div class="row">
                <div class="col">
                  <h4 class="mb-0 media align-items-center">
                    <span class="badge badge-success mr-1">거래중</span>
                    <span v-if="offer.itemTitle == null">삭제된 게시글</span>
                    <a v-bind:href="'${rc.getContextPath()}detail/' + offer.itemId" v-else>{{offer.itemTitle}}</a>
                  </h4>
                  <h4 class="mb-0 text-danger">{{numberFormat(offer.price)}}원&nbsp;<i class="fa fa-times"></i></h4>
                  <small class="text-muted font-weight-light">{{dateFormat(offer.createDate)}}</small>
                </div>
              </div>
            </li>
          </ul>
          <infinite-loading @infinite="offerDeclinedInfiniteHandler" spinner="spiral" ref="offerDeclinedInfiniteLoading">
            <div slot="no-more"></div>
            <div slot="no-results"></div>
          </infinite-loading>
        </div>
        <div class="tab-pane fade in" id="offer-history">
          <ul class="list-group list-group-flush">
            <li class="list-group-item px-0" v-for="offer in offerCompleted.list" v-bind:key="offer.id">
              <div class="row">
                <div class="col">
                  <h4 class="mb-0 media align-items-center">
                    <span class="badge badge-default mr-1">거래완료</span>
                    <span v-if="offer.itemTitle == null">삭제된 게시글</span>
                    <a v-bind:href="'${rc.getContextPath()}/detail/' + offer.itemId" v-else>{{offer.itemTitle}}</a>
                  </h4>
                  <h4 class="mb-0 text-success" v-if="offer.isAccepted">{{numberFormat(offer.price)}}원&nbsp;<i class="fa fa-check"></i></h4>
                  <h4 class="mb-0 text-danger" v-else>{{numberFormat(offer.price)}}원&nbsp;<i class="fa fa-times"></i></h4>
                  <small class="text-muted font-weight-light">{{dateFormat(offer.createDate)}}</small>
                </div>
              </div>
            </li>
          </ul>
          <infinite-loading @infinite="offerCompletedInfiniteHandler" spinner="spiral" ref="offerCompletedInfiniteLoading">
            <div slot="no-more"></div>
            <div slot="no-results"></div>
          </infinite-loading>
        </div>
      </div>
    </div>
  </div>
</div>
<script src="${rc.getContextPath()}/static/js/profile/offer.js"></script>

<#include "/footer.ftl">
</body>
</html>
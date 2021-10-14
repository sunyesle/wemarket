<!DOCTYPE html>
<html lang="ko">
<head>
  <#include "/head.ftl">
  <title>위마켓</title>
</head>
<body>
<#include "/header.ftl">

<div class="container mt--6" id="detail" v-cloak>
  <!-- 글 내용 -->
  <div class="card card-frame mx-lg-6" v-if="item !== ''">
    <div class="p-4">
      <template v-if="item != null && item.images.length != 0">
        <div id="carouselExampleIndicators" class="carousel slide" data-ride="carousel" style="max-height:500px; height: 50vw;">
          <ol class="carousel-indicators">
            <template v-for="index in item.images.length">
              <li v-if="index === 1" data-target="#carouselExampleIndicators" v-bind:data-slide-to="index-1" class="active"></li>
              <li v-else data-target="#carouselExampleIndicators" v-bind:data-slide-to="index-1"></li>
            </template>
          </ol>
          <div class="carousel-inner h-100 bg-dark rounded">
            <template v-for="(image, index) in item.images">
              <div v-if="index === 0" class="carousel-item active">
                <img class="d-block w-100" v-bind:src="image">
              </div>
              <div v-else class="carousel-item">
                <img class="d-block w-100" v-bind:src="image">
              </div>
            </template>
          </div>
          <a class="carousel-control-prev" href="#carouselExampleIndicators" role="button" data-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="sr-only">Previous</span>
          </a>
          <a class="carousel-control-next" href="#carouselExampleIndicators" role="button" data-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="sr-only">Next</span>
          </a>
        </div>
        <div class="mb-3"></div>
      </template>
      <div class="mb-3 mt-0">
        <h4 class="mb-0">
          <a v-bind:href="'${rc.getContextPath()}/profile/' + item.user.id + '/listing'" v-if="!isDeletedUser">{{item.user.nickname}}</a>
          <span class="text-muted" v-else>{{item.user.nickname}}</span>
          <span class="text-success" v-bind:class="{'text-muted' : isDeletedUser}"> {{toRatingStar(item.user.rating)}}
        </h4>
        <h4 class="text-muted font-weight-light">{{item.user.address}}</h4>
      </div>
      <hr class="my-2">
      <div class="mt-4">
        <div class="row">
          <div class="col">
            <h2 class="mb-0 media align-items-center">
              <span v-bind:class="{'text-muted' : isDeletedUser}">{{item.title}}</span>
              <span class="badge badge-success ml-1" v-if="item.status === 'TRADE'">거래중</span>
              <span class="badge badge-default ml-1"v-else-if="item.status === 'COMPLETED'">거래완료</span>
            </h2>
            <h4 class="text-muted font-weight-light mb-0">{{item.category}} ∙ {{dateFormat(item.createDate)}}</h4>
            <h2 class="mb-4 text-primary" v-bind:class="{'text-muted' : isDeletedUser}">{{numberFormat(item.price)}}원</h2>
            <h2 class="mb-4 text-muted" v-else>{{numberFormat(item.price)}}원</h2>
          </div>
          <div class="col-auto" v-if="currUserId !== '' && currUserId !== item.userId">
            <button class="btn btn-outline-danger btn-icon-only rounded-circle" v-bind:class="{'active': isWishlist}" v-on:click="toggleWishlist()">
              <span class="btn-inner--icon"><i class="ni ni-favourite-28"></i></span>
            </button>
          </div>
        </div>
        <p style="white-space:pre-wrap;" v-bind:class="{'text-muted' : isDeletedUser}">{{item.content}}</p>
        <div class="text-right mt-3" v-if="currUserId === item.userId">
          <a class="btn btn-default" v-bind:href="'${rc.getContextPath()}/write/' + itemId">수정</a>
          <button type="button" class="btn btn-danger" v-on:click="deleteItem">삭제</button>
        </div>
      </div>
    </div>
  </div>
  <br/>
  <!-- 제안 -->
  <div class="card card-frame mx-lg-6" v-if="currUserId !== '' && !isDeletedUser">
    <div class="card-body">
      <div class="input-group" v-if="currUserId !== item.userId && item.status === 'OPEN'">
        <div class="input-group-prepend">
          <button class="btn btn-outline-primary" type="button" v-on:click="makeOffer">제안하기</button>
        </div>
        <input type="text" class="form-control" maxlength="9" placeholder="가격" v-model.number="price" oninput="javascript: this.value = this.value.replace(/[^0-9]/g, '');">
      </div>
      <ul class="list-group list-group-flush">
        <li class="list-group-item px-0" v-for="offer in offer.list" v-bind:key="offer.id">
          <div class="row">
            <div class="col pr-0">
              <h4 class="mb-0 text-primary" v-if="offer.isAccepted == null">{{numberFormat(offer.price)}}원</h4>
              <h4 class="mb-0 text-success" v-else-if="offer.isAccepted">{{numberFormat(offer.price)}}원&nbsp;<i class="fa fa-check"></i></h4>
              <h4 class="mb-0 text-danger" v-else>{{numberFormat(offer.price)}}원&nbsp;<i class="fa fa-times"></i></h4>
              <template v-if="currUserId === item.user.id || currUserId === offer.buyer.id">
                <h5 class="mt-1 mb-0"><a v-bind:href="'${rc.getContextPath()}/profile/' + offer.buyer.id + '/listing'">{{offer.buyer.nickname}}</a><span class="text-success"> {{toRatingStar(offer.buyer.rating)}}</h5>
                  <h5 class="mb-0 text-muted font-weight-light">{{offer.buyer.address}}</h5>
                </template>
                <small class="text-muted font-weight-light">{{dateFormat(offer.createDate)}}</small>
              </div>
              <div class="col-auto pl-1">
                <template v-if="item.status === 'OPEN' && offer.isAccepted == null">
                  <!-- OPEN -->
                  <template v-if="currUserId === item.user.id">
                    <a class="btn btn-block btn-outline-default mb-1 mr-0" v-bind:href="'${rc.getContextPath()}/chat/' + offer.buyerId">채팅</a>
                    <button type="button" class="btn btn-block btn-outline-success mb-1 mr-0" v-on:click="acceptOffer(offer.id)">수락</button>
                    <button type="button" class="btn btn-block btn-outline-danger" v-on:click="cancelOffer(offer.id)">거절</button>
                  </template>
                  <template v-else-if="currUserId === offer.buyer.id">
                    <button type="button" class="btn btn-block btn-outline-danger" v-on:click="cancelOffer(offer.id)">취소</button>
                  </template>
                </template>
                <template v-if="item.status === 'TRADE'">
                  <!-- TRADE -->
                  <template v-if="currUserId === item.user.id && offer.isAccepted != null && offer.isAccepted">
                    <a class="btn btn-block btn-outline-default mb-1 mr-0" v-bind:href="'${rc.getContextPath()}/chat/' + offer.buyerId">채팅</a>
                    <button type="button" class="btn btn-block btn-outline-success mb-1 mr-0" v-on:click="completeOffer(offer.id)">완료</button>
                    <button type="button" class="btn btn-block btn-outline-danger" v-on:click="cancelOffer(offer.id)">거절</button>
                  </template>
                  <template v-else-if="currUserId === offer.buyer.id">
                    <button type="button" class="btn btn-block btn-outline-danger" v-if="offer.isAccepted == null" v-on:click="cancelOffer(offer.id)">취소</button>
                    <template v-else-if="offer.isAccepted">
                      <a class="btn btn-block btn-outline-default mb-1 mr-0" v-bind:href="'${rc.getContextPath()}/chat/' + offer.buyerId">채팅</a>
                      <button type="button" class="btn btn-block btn-outline-danger" v-on:click="cancelOffer(offer.id)">취소</button>
                    </template>
                  </template>
                </template>
              </div>
            </div>
          </li>
        </ul>
      <infinite-loading @infinite="infiniteHandler" spinner="spiral" ref="infiniteLoading">
        <div slot="no-more"></div>
        <div slot="no-results"></div>
      </infinite-loading>
    </div>
  </div>
</div>
<script src="${rc.getContextPath()}/static/js/detail.js"></script>

<#include "/footer.ftl">
</body>
</html>

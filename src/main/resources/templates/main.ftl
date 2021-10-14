<!DOCTYPE html>
<html lang="ko">
<head>
  <#include "/head.ftl">
  <title>위마켓</title>
</head>
<body>
<#include "/header.ftl">

<div class="container mt--6" id="main" v-cloak>
  <div class="row">
    <div class="col-lg-3 col-md-4 col-sm-6 mb-30px" v-for="item in items" v-bind:key="item.id">
      <div class="card card-stats">
        <div class="card-body">
          <div class="row">
            <div class="col-sm-12 col-4 pr-0 pr-sm-3">
              <img v-if="item.thumbImage == null" src="${rc.getContextPath()}/static/assets/img/common/default.png" class="rounded img-center border w-100" />
              <img v-else v-bind:src="item.thumbImage" class="rounded img-center border w-100" onerror="this.src='${rc.getContextPath()}/static/assets/img/common/default.png';this.onerror=null;"/>
            </div>
            <div class="col">
              <h4 class="mt-sm-3 mt-0 mb-0"><a v-bind:href="'${rc.getContextPath()}/detail/' + item.id">{{item.title}}</a></h4>
              <h4 class="mb-0"><span class="h5 text-muted font-weight-light">{{item.address}}</span></h4>
              <div class="row">
                <h4 class="col media align-items-center mb-0 pr-0">
                  <span class="badge badge-success mr-1" v-if="item.status === 'TRADE'">거래중</span>
                  <span class="badge badge-default mr-1" v-else-if="item.status === 'COMPLETED'">거래완료</span>
                  {{numberFormat(item.price)}}원
                </h4>
                <h4 class="text-muted pl-0 font-weight-light text-right col-auto"><i class="ni ni-chat-round"></i> {{item.offerCount}}</h4>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <infinite-loading @infinite="infiniteHandler" spinner="spiral">
    <div slot="no-more"></div>
  </infinite-loading>
</div>
<script src="${rc.getContextPath()}/static/js/main.js"></script>

<#include "/footer.ftl">
</body>
</html>

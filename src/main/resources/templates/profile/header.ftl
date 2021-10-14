<!-- 프로필 헤더 -->
<div class="container mt--6" id="profileHeader" v-cloak>
  <div class="card card-frame mx-lg-6">
    <div class="p-4">
      <div class="row align-items-center">
        <div class="col-12   col-sm-auto mb-auto">
          <img src="${rc.getContextPath()}/static/assets/img/common/default.png" class="rounded-circle img-center border ys-profile-img" v-if="user.profileImage==null">
          <img v-bind:src="user.profileImage" class="rounded-circle img-center border ys-profile-img" onerror="this.src='${rc.getContextPath()}/static/assets/img/common/default.png';this.onerror=null;" v-else>
        </div>
        <div class="col-sm col-12 ml ml-sm--2">
          <div class="row">
            <div class="text-center text-sm-left col-12 col-sm">
              <h3 class="mb-0" v-bind:class="{'text-muted' : isDeletedUser}">{{user.nickname}} ({{user.id}})</h3>
              <h3 class="text-success mb-0" v-bind:class="{'text-muted' : isDeletedUser}">{{toRatingStar(user.rating)}}</h3>
              <h4 class="text-muted font-weight-light">{{user.address}}</h4>
            </div>
            <div class="col-12 col-sm-auto mb-2 text-center" v-if="currUserId !== '' && currUserId !== userId">
              <button class="btn btn-outline-primary btn-icon-only rounded-circle" v-bind:class="{'active': isFollowing}" v-on:click="toggleFollow">
                <i class="fa fa-user-plus" style="position: relative; left: 2px;"></i>
              </button>
              <a class="btn btn-outline-default btn-icon-only rounded-circle" v-bind:href="'${rc.getContextPath()}/chat/'+userId" v-if="!isDeletedUser">
                <span class="btn-inner--icon"><i class="ni ni-chat-round"></i></span>
              </a>
            </div>
          </div>
          <p class="ys-bio mb-0" style="font-size: 0.875rem; line-height: 1.428;" v-if="user.bio!=null && user.bio!==''">{{user.bio}}</p>
        </div>
      </div>
    </div>
  </div>
</div>
<script src="${rc.getContextPath()}/static/js/profile/header.js"></script>

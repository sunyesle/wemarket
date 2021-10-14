<header id="header" v-cloak>
  <!-- 네비게이션 바 -->
  <nav class="navbar navbar-top navbar-expand navbar-dark bg-primary border-bottom fixed-top px-0">
    <div class="container">
      <div class="collapse navbar-collapse">
        <!-- 로고 -->
        <ul class="navbar-nav align-items-center mr-auto">
          <a href="${rc.getContextPath()}/"><img src="${rc.getContextPath()}/static/assets/img/common/logo_white.svg" style="height: 30px;"/></a>
        </ul>
        <ul class="navbar-nav align-items-center" v-if="currUserId !== ''">
          <!-- 판매하기 -->
          <li class="nav-item">
            <a class="nav-link pr-0" href="${rc.getContextPath()}/write">
              <div class="media align-items-center">
                <i class="ni ni-cart mr-1"></i><span class="mb-0 text-sm d-none d-sm-block">판매하기</span>
              </div>
            </a>
          </li>
          <!-- 내상점 -->
          <li class="nav-item">
            <a class="nav-link pr-0" v-bind:href="'${rc.getContextPath()}/profile/'+currUserId+'/listing'">
              <div class="media align-items-center">
                <i class="ni ni-single-02 mr-1"></i><span class="mb-0 text-sm d-none d-sm-block">내상점</span>
              </div>
            </a>
          </li>
          <!-- 채팅 -->
          <li class="nav-item">
            <a class="nav-link pr-0" href="${rc.getContextPath()}/chat">
              <div class="media align-items-center">
                <i class="ni ni-chat-round mr-1"></i><span class="mb-0 text-sm d-none d-sm-block">채팅</span>
              </div>
            </a>
          </li>
          <li class="nav-item dropdown">
            <a class="nav-link pr-0" href="#" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
              <i class="fa fa-bars"></i>
            </a>
            <div class="dropdown-menu dropdown-menu-right">
              <a class="dropdown-item" v-bind:href="'${rc.getContextPath()}/profile/'+currUserId+'/listing'">
                <i class="ni ni-single-02"></i>
                <span>내상점</span>
              </a>
              <a class="dropdown-item" href="${rc.getContextPath()}/setting">
                <i class="ni ni-settings-gear-65"></i>
                <span>설정</span>
              </a>
              <div class="dropdown-divider"></div>
              <a href="#" class="dropdown-item" v-on:click.prevent="clickSignout">
                <i class="fa fa-sign-out-alt align-baseline"></i>
                <span>로그아웃</span>
              </a>
            </div>
          </li>
        </ul>
        <ul class="navbar-nav align-items-center" v-else>
          <li class="nav-item">
            <a class="nav-link" href="${rc.getContextPath()}/signin" role="button">
              <i class="fa fa-sign-in-alt align-baseline"></i>&nbsp;
              <span class="mb-0 text-sm font-weight-bold">로그인</span>
            </a>
          </li>
        </ul>
      </div>
    </div>
  </nav>

  <!-- 검색 -->
  <div class="header bg-primary pb-6" style="padding-top: 56px">
    <div class="container">
      <div class="row align-items-center py-4">
        <div class="col pr-0">
          <form class="navbar-search navbar-search-light form-inline">
            <div class="form-group mb-0 w-100">
              <div class="input-group input-group-alternative input-group-merge w-100">
                <div class="input-group-prepend">
                  <span class="input-group-text"><i class="fas fa-search"></i></span>
                </div>
                <input class="form-control" placeholder="Search" type="text" v-model="searchKeyword" v-on:keypress.enter.prevent="search" />
              </div>
            </div>
          </form>
        </div>
        <div class="col-auto text-right">
          <button class="btn btn-sm btn-neutral" v-on:click="search">검색</button>
          <button class="btn btn-sm btn-neutral" data-toggle="modal" data-target="#exampleModal">Filters</button>
          <!-- Modal -->
          <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered" role="document">
              <div class="modal-content">
                <div class="modal-header">
                  <h5 class="modal-title" id="exampleModalLabel">필터</h5>
                  <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                  </button>
                </div>
                <div class="modal-body text-left">
                  <h4>검색 대상</h4>
                  <div class="custom-control custom-radio custom-control-inline">
                    <input type="radio" id="searchTargetRadio1" name="searchTargetRadio" class="custom-control-input" value="TITLE" v-model="searchTarget" />
                    <label class="custom-control-label" for="searchTargetRadio1">제목</label>
                  </div>
                  <div class="custom-control custom-radio custom-control-inline">
                    <input type="radio" id="searchTargetRadio2" name="searchTargetRadio" class="custom-control-input" value="TITLE_CONTENT" v-model="searchTarget" />
                    <label class="custom-control-label" for="searchTargetRadio2">제목+내용</label>
                  </div>
                  <div class="custom-control custom-radio custom-control-inline">
                    <input type="radio" id="searchTargetRadio3" name="searchTargetRadio" class="custom-control-input" value="USER" v-model="searchTarget" />
                    <label class="custom-control-label" for="searchTargetRadio3">유저아이디</label>
                  </div>

                  <hr class="my-3" />
                  <h4>카테고리</h4>
                  <div class="row mx-0">
                    <div class="custom-control custom-checkbox col-6">
                      <input type="checkbox" class="custom-control-input" id="allCategory" v-model="checkAllCategory" />
                      <label class="custom-control-label" for="allCategory">전체</label>
                    </div>
                    <div class="custom-control custom-checkbox col-6" v-for="category in categories">
                      <input type="checkbox" class="custom-control-input" v-bind:id="'category'+category.id" v-bind:value="category.id" v-model="categoryIdList" number />
                      <label class="custom-control-label" v-bind:for="'category'+category.id">{{category.name}}</label>
                    </div>
                  </div>

                  <hr class="my-3" />
                  <h4>상태</h4>
                  <div class="custom-control custom-checkbox custom-control-inline">
                    <input type="checkbox" class="custom-control-input" id="itemStatus1" name="itemStatus" value="OPEN" v-model="itemStatusList" />
                    <label class="custom-control-label" for="itemStatus1">판매중</label>
                  </div>
                  <div class="custom-control custom-checkbox custom-control-inline">
                    <input type="checkbox" class="custom-control-input" id="itemStatus2" name="itemStatus" value="TRADE" v-model="itemStatusList" />
                    <label class="custom-control-label" for="itemStatus2">거래중</label>
                  </div>
                  <div class="custom-control custom-checkbox custom-control-inline">
                    <input type="checkbox" class="custom-control-input" id="itemStatus3" name="itemStatus" value="COMPLETED" v-model="itemStatusList" />
                    <label class="custom-control-label" for="itemStatus3">판매완료</label>
                  </div>

                  <hr class="my-3" />
                  <h4>가격</h4>
                  <div class="custom-control-inline mr-2">
                    <input class="form-control" type="text" placeholder="최소" maxlength="9" oninput="javascript: this.value = this.value.replace(/[^0-9]/g, '');" v-model.number="minPrice" style="width: 7rem" />
                  </div>
                  ~
                  <div class="custom-control-inline ml-2">
                    <input class="form-control" type="text" placeholder="최대" maxlength="9" oninput="javascript: this.value = this.value.replace(/[^0-9]/g, '');" v-model.number="maxPrice" style="width: 7rem" />
                  </div>
                </div>
                <div class="modal-footer">
                  <button type="button" class="btn btn-secondary" data-dismiss="modal">닫기</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

<@security.authorize access="hasRole('ROLE_UNVERIFIED')">
      <div class="alert alert-info alert-dismissible fade show" role="alert">
        <span class="alert-icon"><i class="ni ni-email-83"></i></span>
        <span class="alert-text">입력하신 이메일로 인증 메일이 발송되었습니다. 서비스 이용을 위해 이메일 인증을 진행해주세요.</span>
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
</@security.authorize>

    </div>
  </div>
</header>
<script src="${rc.getContextPath()}/static/js/header.js"></script>
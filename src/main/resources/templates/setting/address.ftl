<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<!DOCTYPE html>
<@security.authorize access="isAuthenticated()">
<html lang="ko">
<head>
  <#include "/head.ftl">
  <title>설정 | 위마켓</title>
</head>
<body>
<#include "/header.ftl">

<div class="container mt--6" id="settingEditAddress" v-cloak>
  <div class="card card-frame mx-lg-6">
    <div class="p-4">
      <div class="form-group">
        <label class="form-control-label">주소</label>
        <div class="row">
          <div class="col">
            <input type="text" class="form-control" v-bind:value="name" disabled>
          </div>
          <div class="col-auto pl-0">
            <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#addressModal" v-on:click="resetSearch">주소찾기</button>
          </div>
        </div>
        <div class="modal fade" id="addressModal" tabindex="-1" role="dialog" aria-labelledby="addressModalLabel" aria-hidden="true">
          <div class="modal-dialog modal-dialog-centered" role="document">
            <div class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title" id="addressModalLabel">주소찾기</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
              <div class="modal-body">
                <div class="row">
                  <div class="col pr-0">
                    <div class="form-group">
                      <div class="input-group input-group-merge w-100">
                        <div class="input-group-prepend">
                          <span class="input-group-text"><i class="fas fa-search"></i></span>
                        </div>
                        <input class="form-control" placeholder="동, 읍, 면으로 검색" type="text" v-model="addressKeyword" v-on:keypress.enter="searchAddress">
                      </div>
                    </div>
                  </div>
                  <div class="col-auto">
                    <button type="button" class="btn btn-outline-primary text-align-right" v-on:click="searchAddress">검색</button>
                  </div>
                </div>
                <div class="form-group">
                  <select class="form-control" v-model="selected" ref="select">
                    <template v-for="address in addresses">
                      <option v-bind:value="{code: address.code, name: address.name}">{{address.name}}</option>
                    </template>
                  </select>
                </div>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">닫기</button>
                <button type="button" class="btn btn-primary" data-dismiss="modal" v-on:click="selectAddress">확인</button>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="text-center mt-3">
        <button type="button" class="btn btn-primary" v-on:click="updateAddress" v-bind:disabled="isSubmitting">
          <span class="spinner-border spinner-border-sm mr-1" role="status" aria-hidden="true" v-if="isSubmitting"></span>변경
        </button>
      </div>
    </div>
  </div>
</div>
<script src="${rc.getContextPath()}/static/js/setting/address.js"></script>

<#include "/footer.ftl">
</body>
</html>
</@security.authorize>
<@security.authorize access="isAnonymous()">
<script>
  alert('접근 권한이 없습니다.');
  location.replace("${rc.getContextPath()}/");
</script>
</@security.authorize>
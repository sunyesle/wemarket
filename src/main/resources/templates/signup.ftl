<!DOCTYPE html>
<html lang="ko">
<head>
  <#include "/head.ftl">
  <title>회원가입 | 위마켓</title>
</head>
<body>
<#include "/header.ftl">

<div class="container mt--6" id="signup" v-cloak>
  <div class="card card-frame mx-lg-6">
    <div class="p-4">
      <div class="form-group">
        <label class="form-control-label">아이디</label>
        <small>(4~16자 이내, 영문, 숫자 사용 가능)</small>
        <div class="row">
          <div class="col">
            <input type="text" class="form-control" maxlength="16" v-model="id" v-on:change="isDuplicated=''" />
          </div>
          <div class="col-auto pl-0">
            <button type="button" class="btn btn-primary" v-on:click="checkDuplicateId">중복검사</button>
          </div>
        </div>
        <small class="text-success" v-if="isDuplicated === false">사용 가능한 아이디입니다.</small>
        <small class="text-danger" v-else-if="isDuplicated === true">중복된 아이디입니다.</small>
      </div>
      <div class="form-group">
        <label class="form-control-label">비밀번호</label>
        <small>(8~16자 이내, 영문+숫자 조합)</small>
        <input type="password" class="form-control" maxlength="16" v-model="password" />
      </div>
      <div class="form-group">
        <label class="form-control-label">비밀번호 확인</label>
        <input type="password" class="form-control" maxlength="16" v-model="rePassword" />
      </div>
      <div class="form-group">
        <label class="form-control-label">닉네임</label>
        <small>(2~10자 이내)</small>
        <input type="text" class="form-control" maxlength="10" v-model="nickname" />
      </div>
      <div class="form-group">
        <label class="form-control-label">이메일</label>
        <input type="text" class="form-control" maxlength="320" v-model="email" />
      </div>
      <div class="form-group">
        <label class="form-control-label">주소</label>
        <div class="row">
          <div class="col">
            <input type="text" class="form-control" v-bind:value="address.name" disabled />
          </div>
          <div class="col-auto pl-0">
            <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#addressModal" v-on:click="resetSearch">주소찾기</button>
          </div>
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
                      <input class="form-control" placeholder="동, 읍, 면으로 검색" type="text" v-model="addressKeyword" v-on:keypress.enter="searchAddress" />
                    </div>
                  </div>
                </div>
                <div class="col-auto">
                  <button type="button" class="btn btn-outline-primary text-align-right" v-on:click="searchAddress">검색</button>
                </div>
              </div>
              <div class="form-group">
                <select class="form-control" v-model="selectedAddress" ref="select">
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
      <div class="text-center mt-3">
        <button type="button" class="btn btn-primary" v-on:click="submit" v-bind:disabled="isSubmitting">
          <span class="spinner-border spinner-border-sm mr-1" role="status" aria-hidden="true" v-if="isSubmitting"></span>확인
        </button>
      </div>
    </div>
  </div>
</div>
<script src="${rc.getContextPath()}/static/js/signup.js"></script>

<#include "/footer.ftl">
</body>
</html>
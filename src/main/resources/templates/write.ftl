<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<!DOCTYPE html>
<@security.authorize access="hasAnyRole('ROLE_USER', 'ROLE_ADMIN')">
<html lang="ko">
<head>
  <#include "/head.ftl">
  <title>글쓰기 | 위마켓</title>
</head>
<body>
<#include "/header.ftl">

<div class="container mt--6" id="write" v-cloak>
  <div class="card card-frame mx-lg-6">
    <div class="p-4">
      <h4 class="pb-1 mb-3" style="border-bottom: 2px solid #5e72e4">글쓰기</h4>
      <div class="row mb-2">
        <div class="col-sm-auto mb-2 mb-sm-0">
          <select class="form-control" id="addressSelect" v-model="categoryId">
            <option value="" disabled selected>카테고리</option>
            <template v-for="c in categories">
              <option v-bind:value="c.id">{{c.name}}</option>
            </template>
          </select>
        </div>
        <div class="col-12 col-sm pl-sm-0">
          <input type="text" class="form-control" placeholder="제목" maxlength="40" v-model="title">
        </div>
      </div>
      <div class="d-flex align-items-center">
        <input class="form-control mb-2 w-25" type="text" placeholder="가격"  maxlength="9" v-model.number="price" oninput="javascript: this.value = this.value.replace(/[^0-9]/g, '');">
        <span class="h4 text-muted ml-2">원</span>
      </div>
      <textarea class="form-control mb-2" id="exampleFormControlTextarea1" rows="20" placeholder="내용" maxlength="4000" v-model="content"></textarea>
      <form>
        <div class="row align-items-center" v-for="index in images.length" v-bind:key="index">
          <div class="col-auto pr-0">
            <div class="rounded border" style="width: 80px; height: 80px;">
              <img src="${rc.getContextPath()}/static/assets/img/common/default.png" class="w-100 h-100" style="object-fit: cover;" v-if="images[index-1].src==null">
              <img v-bind:src="images[index-1].src" class="w-100 h-100" style="object-fit: cover;" v-else>
            </div>
          </div>
          <div class="col pr-0">
            <button class="btn border rounded w-100 px-0" v-on:click.prevent="selectFile(index-1)">
              <template v-if="images[index-1]==null">파일선택</template>
              <template v-else>파일변경</template>
            </button>
            <input type="file" name="files" ref="file" accept=".jpg, .png" v-on:change="onFileSelected(index-1, $event)" hidden>
          </div>
          <div class="col-auto pr-0">
            <button class="btn btn-block btn-sm border rounded" v-on:click.prevent="changeImageOrder(index-1, -1)" v-bind:disabled="index===1">▲</button>
            <button class="btn btn-block btn-sm border rounded mt-0" v-on:click.prevent="changeImageOrder(index-1, 1)" v-bind:disabled="index===images.length">▼</button>
          </div>
          <div class="col-auto">
            <a class="nav-link" href="#" v-on:click.prevent="removeImage(index-1)">
              <i class="fa fa-times"></i>
            </a>
          </div>
        </div>
      </form>
      <button class="btn border rounded w-100" data-toggle="button" v-on:click="addImage" v-if="images.length < 10">이미지 추가</button>
      <div class="text-center mt-3">
        <button type="button" class="btn btn-primary" v-on:click="upload" v-bind:disabled="isSubmitting">
          <span class="spinner-border spinner-border-sm mr-1" role="status" aria-hidden="true" v-if="isSubmitting"></span>확인
        </button>
      </div>
    </div>
  </div>
</div>
<script src="${rc.getContextPath()}/static/js/write.js"></script>

<#include "/footer.ftl">
</body>
</html>
</@security.authorize>
<@security.authorize access="hasRole('ROLE_UNVERIFIED')">
<script>
  alert('접근 권한이 없습니다. 서비스 이용을 위해 이메일 인증을 진행해주세요.');
  location.replace("${rc.getContextPath()}/");
</script>
</@security.authorize>
<@security.authorize access="isAnonymous()">
<script>
  alert('접근 권한이 없습니다.');
  location.replace("${rc.getContextPath()}/");
</script>
</@security.authorize>
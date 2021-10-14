<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<meta charset="UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1, minimum-scale=1" />
<link rel="icon" href="${rc.getContextPath()}/static/assets/img/common/favicon.png" type="image/png" />
<!-- Fonts -->
<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@300;400;600;700" />
<!-- Icons -->
<link rel="stylesheet" href="${rc.getContextPath()}/static/assets/vendor/nucleo/css/nucleo.css" type="text/css" />
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@5.15.4/css/all.min.css" integrity="sha256-mUZM63G8m73Mcidfrv5E+Y61y7a12O5mW4ezU3bxqW4=" crossorigin="anonymous">
<!-- Page plugins -->
<!-- Argon CSS -->
<link rel="stylesheet" href="${rc.getContextPath()}/static/assets/css/argon.min.css?v=1.2.0" type="text/css" />
<!-- Argon Core -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js" integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/js-cookie/2.2.1/js.cookie.min.js" integrity="sha512-Meww2sXqNHxI1+5Dyh/9KAtvI9RZSA4c1K2k5iL02oiPO/RH3Q30L3M1albtqMg50u4gRTYdV4EXOQqXEI336A==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script src="https://cdn.jsdelivr.net/npm/jquery.scrollbar@0.2.11/jquery.scrollbar.min.js" integrity="sha256-lcr7XHKry+oEoDi/xxl+/w+LXXAwQla+erxqxgCm7m0=" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/jquery-scroll-lock@3.1.3/jquery-scrollLock.min.js" integrity="sha256-OGBqkSG4kYQ0UbrT7juDZpE/VqIjMPuu6CVKQ1KaTEA=" crossorigin="anonymous"></script>
<!-- Argon JS -->
<script src="${rc.getContextPath()}/static/assets/js/argon.min.js?v=1.2.0"></script>
<!-- Axios -->
<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
<!-- Vue JS -->
<script src="https://cdn.jsdelivr.net/npm/vue@2.6.0"></script>
<!-- Vue infinite scrolling -->
<script src="https://cdn.jsdelivr.net/npm/vue-infinite-loading@2.4.5/dist/vue-infinite-loading.min.js"></script>

<script>
const PAGE_SIZE = 24;
var contextPath = "${rc.getContextPath()}";
<@security.authorize access="isAuthenticated()">
const currUserId = "<@security.authentication property="principal.id"/>";
</@security.authorize>
<@security.authorize access="isAnonymous()">
const currUserId = "";
</@security.authorize>
<#if data??>
<#list data? keys as key>
const ${key} = "${data[key]}";
</#list>
</#if>
</script>
<script src="${rc.getContextPath()}/static/js/head.js"></script>
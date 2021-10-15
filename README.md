# WeMarket

> 우리가 함께 만드는 WeMarket! 위마켓에서 중고거래를 시작해보세요.

중고 거래 사이트입니다.

- 사진과 함께 판매글을 올릴 수 있습니다.
- 구매자는 상품에 자신이 원하는 구매 가격으로 제안할 수 있습니다.
- 판매자는 제안을 수락, 거절 할 수 있습니다.
- 1:1채팅을 통해 상대방과 대화할 수 있습니다.

## 사용한 기술 스택

### backend

- IntelliJ community edition
- Spring Boot 2.4.3
- Spring Security + JWT
- Spring WebSocket
- MySQL 8.0
- MyBatis
- Redis (로그인 정보 저장, message broker)
- Firebase storage (이미지 저장)
- Java 11
- Gradle

### frontend

- Visual Studio Code
- Vue
- Ajax
- Bootstrap
- Freemaker

### server

- Oracle Cloud Infrastructure
- Web Server
  - ubuntu 20.04
  - openjdk 11
  - tomcat9
  - SSL
- MySQL Server
  - ubuntu 20.04
  - mysql 8.0

## 디렉토리 구조

- **advice** : 예외처리
- **config**
  - **security** : security, jwt 관련 설정
  - freemarket, swagger 관련 설정
- **controller**
  - **api** : RESTful API 컨트롤러. response, request DTO를 inner class로 가지고있다.
  - view, exception 컨트롤러
- **dto**
  - **criteria** : 검색 조건, 페이징 정보
  - **result** : response 객체
- **entity** : MySQL 테이블과 매칭되는 클래스
- **enums**
- **mapper** : MyBatis mapper interface
- **repository** : Redis repository
- **service**
- **util**
- **websocket** : websocket 관련 클래스

## API 문서

https://sunys.xyz/swagger-ui.html

## DB ERD

![erd](https://user-images.githubusercontent.com/45172865/137163207-585b5191-55c2-491a-8647-bc0b98f84459.png)

# 사용방법

https://sunys.xyz

게스트 계정

- id : guest1~5
- password : password1~5

※ 게스트 계정으로는 이메일 수정, 비밀번호 수정, 계정 삭제 기능을 사용할 수 없습니다.

## 1. 로그인

![로그인](https://user-images.githubusercontent.com/45172865/137164078-778ed62e-e1f9-4f81-921c-ca30861a0436.png)

## 2. 회원가입

![회원가입](https://user-images.githubusercontent.com/45172865/137164159-1434ae52-4a28-4c61-ae0e-5c9d01cc1347.png)

> 입력한 이메일로 인증메일이 발송됩니다. 이메일 인증을 진행하면 회원가입이 완료됩니다.

![이메일인증](https://user-images.githubusercontent.com/45172865/137164256-92f77a52-5e0d-4470-b42e-3c7fcd03447c.png)

## 3. 설정

> 로그인 후 설정 페이지(메뉴>설정)에서 프로필 이미지, 지역, 이메일, 프로필 설명, 비밀번호를 변경하거나 계정을 삭제할 수 있습니다.

![설정](https://user-images.githubusercontent.com/45172865/137164323-8817c736-1582-4277-8b99-ab288970e09d.png)

## 4. 홈

> 판매글들을 조회할 수 있습니다. 

![메인](https://user-images.githubusercontent.com/45172865/137164373-cd5ec80d-008c-4378-bf5a-85d3529e9724.png)

> 키워드 검색, 필터를 통해 판매글을 조회할 수 있습니다.

![메인 필터](https://user-images.githubusercontent.com/45172865/137164376-55024787-bd89-4a23-8410-bc00338c16b8.png)

## 5. 판매글 쓰기

> 상단 '판매하기' 버튼을 클릭해서 새로운 판매글을 작성할 수 있습니다.

![글쓰기](https://user-images.githubusercontent.com/45172865/137164469-b6be9f8c-0f35-4205-9484-56211f4356f2.png)

## 6. 판매글 디테일

### 구매자 시점

> 하트 버튼을 클릭해 관심상품에 추가, 삭제 할 수 있습니다.

![구매자](https://user-images.githubusercontent.com/45172865/137164525-ab71e050-1154-4836-a6d0-a9edde5100cf.png)

### 판매자 시점

> 판매글을 수정, 삭제 할 수 있습니다.

![판매자](https://user-images.githubusercontent.com/45172865/137164603-6635c9d1-2816-47ea-b626-b21f8210a2ce.png)

## 7. 제안

> 판매글 디테일 페이지 하단에서 제안을 확인할 수 있습니다. 로그인한 사용자만 확인 가능합니다.

### 구매자 시점

> 구매 가격을 입력하고 '제안하기' 버튼을 클릭해 제안할 수 있습니다. '취소' 버튼을 클릭해 제안을 취소할 수 있습니다.
>
> 상품에 수락, 거절(취소) 되지 않은 자신의 제안이 1개 이상 있을 경우 새로운 제안을 할 수 없습니다.

![구매자 제안](https://user-images.githubusercontent.com/45172865/137164640-7eb55cd8-38d0-4280-9a14-f33d1cdb2a83.png)

> 제안이 수락, 거절되면 다음과 같이 표시됩니다.
>
> 수락한 제안이 있는 상품에는 제안할 수 없습니다.

![구매자 제안2](https://user-images.githubusercontent.com/45172865/137164648-e7e438b6-1f9b-4c5d-ab9a-3f4e4e11aa9a.png)

### 판매자 시점

> 모든 제안의 사용자 정보를 확인할 수 있습니다.

![판매자 제안](https://user-images.githubusercontent.com/45172865/137164753-85fc7d19-fcc2-4a39-90f8-0284c8157bd0.png)

> 제안을 수락, 거절하면 다음과 같이 표시됩니다.
>
> 제안 수락 시 다른 제안들은 자동으로 거절되고, 상품이 '거래 중' 상태로 변경됩니다.

![거래중](https://user-images.githubusercontent.com/45172865/137164845-24511fca-5043-4efe-b224-5dc50983bf11.png)

![판매자 제안2](https://user-images.githubusercontent.com/45172865/137164857-9b729a10-13b0-416d-8cb8-e48d23675289.png)

> 거래가 끝나고 '완료' 버튼을 클릭하면 상품이 '거래 완료' 상태로 변경됩니다.

![판매완료](https://user-images.githubusercontent.com/45172865/137164925-89adf224-b272-40e7-b3ef-dfa8cd8135c1.png)

## 8. 프로필

### 내 프로필(내상점)

> 상단의 '내 상점' 버튼을 클릭해서 자신의 프로필 페이지로 이동할 수 있습니다.
>
> 판매 상품, 받은 제안, 내가 한 제안, 관심 상품, 팔로잉, 리뷰를 확인할 수 있습니다.

![프로필 내가볼때](https://user-images.githubusercontent.com/45172865/137165053-6dbd82ea-59c0-48fb-805d-23af6ae91a8d.png)

![받은제안](https://user-images.githubusercontent.com/45172865/137165021-2d2e95be-2f19-4453-a635-7ea215b6fe7e.png)

![내제안](https://user-images.githubusercontent.com/45172865/137165049-3eedc790-710b-4b7f-9246-de6c4efcf0b5.png)

![관심상품](https://user-images.githubusercontent.com/45172865/137165048-9f71de2e-eeec-4090-9d42-deb7bc44e609.png)

![팔로잉](https://user-images.githubusercontent.com/45172865/137165018-05e78e7b-6be4-4078-bfbd-8ed1ae684ff7.png)

![리뷰](https://user-images.githubusercontent.com/45172865/137165255-8d9b06a9-196e-451a-b82c-f8c19c6a5ae8.png)

### 다른 사용자의 프로필

> 다른 사용자의 닉네임을 클릭하면 프로필 페이지로 이동할 수 있습니다. 판매 상품, 리뷰를 확인할 수 있습니다.
>
> 프로필 상단의 '👤+' 버튼을 클릭해 팔로우, 언팔로우 할 수 있습니다. '🗨' 버튼을 클릭해 채팅을 할 수 있습니다. 

![프로필 남이볼때](https://user-images.githubusercontent.com/45172865/137165258-0ae81df7-0cc9-4da6-a7b6-369c37b80cbe.png)

> 리뷰탭에서 리뷰를 작성할 수 있습니다.

![리뷰작성](https://user-images.githubusercontent.com/45172865/137165241-9bc5525f-b119-438a-9065-895a6138a504.png)

## 9. 채팅

> 제안 옆의 '채팅' 버튼, 프로필의 '🗨' 버튼을 클릭해서 채팅을 시작할 수 있습니다.
>
> 상단의 '채팅' 버튼을 클릭하면 만들어진 채팅방들을 확인할 수 있습니다.

![채팅방](https://user-images.githubusercontent.com/45172865/137165366-51cb9366-5ea4-4e99-aefb-59ccad4e9526.png)

![채팅 판매자 입장](https://user-images.githubusercontent.com/45172865/137165430-c4dbfc37-89be-482b-9756-25c7a1c6796c.png)

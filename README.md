## 소개
### 🥕 당근마켓 클론 앱
### ProjectGoal
- 당근마켓의 백엔드 기능을 구현해본다.
- 효율적인 설계를 고민하며 구현하여본다.
- 성능 최적화를 고민한다.
- 다양한 기능을 사용하여 구현한다.
- 코드 리펙토링
- 개인프로젝트이지만, 협업에 초점을 맞춰 개발 프로세스를 가져간다.
- 개발하면서 추가적인 목표 작성..

---
### Backend
- Java 17
- Spring Boot 3.2.0
- Spring Security + jwt
- Spring Data JPA + MySQL8.0
- Redis

---

### 🛠 구현 기능 / 사용 기술 정리 ( 향후 정리 필요 )
- 회원
  - 회원가입
  - 메일인증
    - redis 사용하여 인증 데이터 저장
  - 로그인, 로그아웃
    - Spring security + Jwt 사용 -> API 호출 token 필요
  - 마이페이지 회원과 관련된 데이터 조회
  - 권한 ( 관리자 / 유저 )
    - Spring security 권한 부여
- 게시판
  - 일반 게시판 기능
  - 페이징
  - 검색
- 거래
  - 게시물 등록, 수정, 삭제
  - 페이징 (Slice)
  - 검색
  - 키워드 알림 (SSE 기능 사용) 
    - SSE(Server Sent Events) 기능 사용
  - 카테고리별 검색 / 일반 검색
    - querydsl 사용으로 효율적인 동적쿼리 작성
- 채팅
  - 실시간 채팅 기능 구현
    - Spring websocket 사용
    - STOMP Protocol 사용
      - redis를 메세지 브로커로 사용
  - 채팅방 생성
- 최종적으로 코드 리펙토링을 통하여 완성도 높은 코드로 만드는게 계획
--- 

### 설계

![캡처](https://github.com/user-attachments/assets/3c722657-0a0c-4945-a3c5-30daa16af284)
![member](https://github.com/user-attachments/assets/aff82f74-513b-41c9-aabe-d874aa994332)
![board](https://github.com/user-attachments/assets/bd23f1b6-227d-4359-b737-06452fbd1a7a)
![post](https://github.com/user-attachments/assets/3d30b8ea-83db-4b6a-9323-598107cd7ff0)
---
### 개발에 따른 커밋 메시지 타입(Type)

| **타입 이름** | **내용**                                      |
|:--------------|:----------------------------------------------|
| feat          | 새로운 기능에 대한 커밋                       |
| fix           | 버그 수정에 대한 커밋                         |
| build         | 빌드 관련 파일 수정 / 모듈 설치 또는 삭제에 대한 커밋 |
| chore         | 그 외 자잘한 수정에 대한 커밋                 |
| ci            | CI 관련 설정 수정에 대한 커밋                |
| docs          | 문서 수정에 대한 커밋                         |
| style         | 코드 스타일 혹은 포맷 등에 관한 커밋           |
| refactor      | 코드 리팩토링에 대한 커밋                     |
| test          | 테스트 코드 수정에 대한 커밋                  |
| perf          | 성능 개선에 대한 커밋                         |

---

 

# NoteForIOS
아이패드나 아이폰에서 사용할 수 있는 노트 \
자신만의 아이디어로 꾸미기 가능

## 기술 스택
Backend : Java, Spring Boot, JPA, Docker, Mysql \
DevOps : AWS EC2, AWS RDS, AWS S3, Docker, Jenkins

## ERD
![image](https://github.com/Nokchamat/NoteForIOS/assets/107979129/96b4c697-873d-4f38-8f07-1653b35e59e4)


## 프로젝트 주요 기능
### 회원가입 / 로그인
    회원가입
    - 중복된 이메일로 회원가입 불가
    - 게시물 작성하기 위해서는 이메일 인증 필요. Mailgun api 사용 예정
    - data input 값은 validation을 사용하여 체크
    
    로그인
    - 이메일과 비밀번호를 통해 로그인
    - 로그인 시 Jwt Token 발행
    - 게시물을 작성하거나 다운은 Token 필요하나 보는 것은 불필요
### 게시물
    작성
    - 게시물 작성 시에 PDF로 노트의 속지 업로드 가능

    조회
    - 조회는 회원가입 없이 조회 가능하나, 다운 시에는 회원가입 및 이메일 인증 필요
    - 상세 조회 시 조회수 증가

### 좋아요
    - 게시물에 대해 좋아요 등록, 등록 취소, 조회

### 팔로우
    - 사람에 대해 팔로우 가능
    - 팔로우 한 사람이 게시물 작성 시 알림에 메시지 제공

### 계획
    - 회원가입, 로그인, 게시물 구현 후 swift로 간단하게 개발하여 ios 어플로 등록할 수 있도록 할 예정
    - 이후 채팅 등 업데이트 시도 예정

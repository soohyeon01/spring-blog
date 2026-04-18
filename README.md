
### 🌱 Spring Blog Project

> Spring Boot 기반 블로그 웹 애플리케이션
> 사용자 인증/인가 기능 포함, 계층형 아키텍쳐와 JPA를 활용한 CRUD 중심 프로젝트

---

### 📌 프로젝트 소개

Spring Boot를 기반으로 블로그 서비스를 구현한 웹 애플리케이션입니다. 
사용자는 게시글을 작성하고 수정/삭제할 수 있으며, Spring Security를 활용하여 로그인한 사용자만 특정 기능을 수행할 수 있도록 권한을 제어했습니다.

**계층형 구조(Controller → Service → Repository)를 명확히 분리하여 유지보수성과 확장성을 고려해 설계했습니다.**

**이 프로젝트는 백엔드 설계 및 웹 애플리케이션 흐름 이해를 목표로 [스프링부트3 백엔드 개발자 되기 / 신선영] 을 바탕으로 제작되었습니다.**

---

### 🛠 기술 스택

#### Backend
- Java 21
- Spring Boot
- Spring MVC
- Spring Data JPA
- Spring Security
  
#### Database
- MySQL / H2

#### Frontend
- Thymeleaf
- HTML / CSS
  
#### Build Tool
- Gradle

---

### 🧩 주요 기능

#### 👤 회원 기능
- 회원가입 / 로그인
- 비밀번호 암호화 (BCrypt)
- 사용자 권한 관리 (USER / ADMIN)
  
#### 📝 게시글 기능
- 게시글 작성 / 조회 / 수정 / 삭제 (CRUD)
- 게시글 목록 조회 ~~(페이징 가능하면 추가)~~

~~#### 💬 댓글 기능~~
~~- 댓글 작성 / 삭제~~
~~- 게시글과 댓글 연관 관계 매핑 (1:N)~~
  
#### 🔐 인증 / 인가
- Spring Security 기반 로그인 처리
- 권한별 접근 제어 (관리자 / 일반 사용자)

---

### 🏗 아키텍처
```java
Controller → Service → Repository → DB
```
- Controller: 요청/응답 처리
- Service: 비즈니스 로직
- Repository: 데이터 접근 (JPA)
- Entity: 도메인 모델

---

### 📂 프로젝트 구조
```
spring-blog
 ├── src
 │   └── main
 │       ├── java
 │       │   └── springboot_developer.spring_blog
 │       │       ├── config        # Spring 설정 (Security 등)
 │       │       ├── controller    # 요청 처리 (MVC Controller)
 │       │       ├── domain        # Entity 클래스
 │       │       ├── dto           # 데이터 전달 객체
 │       │       ├── repository    # JPA Repository
 │       │       ├── service       # 비즈니스 로직
 │       │       ├── util          # 쿠키 관리 클래스
 │       │       ├── InitDb        # 초기 데이터 세팅
 │       │       └── SpringBlogApplication
 │       │
 │       └── resources
 │           ├── static           # 정적 리소스 (CSS, JS)
 │           ├── templates        # Thymeleaf 템플릿
 │           └── application.yml  # 설정 파일
 │
 ├── build.gradle
 ├── gradlew
 └── gradlew.bat

```

### 💡 학습 포인트
- Spring MVC 구조 이해
- 계층형 아키텍처 설계 (Controller / Service / Repository 분리)
- JPA Entity 설계 및 연관관계 매핑
- DTO를 활용한 계층 간 데이터 전달
- Spring Security 기반 인증/인가 흐름 이해
- 계층형 아키텍처 설계 경험
- Thymeleaf를 활용한 서버 사이드 렌더링



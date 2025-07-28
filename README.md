# 코딩 메이트 - 백준 답 모음 사이트
* 백준 답을 모아서 내가 풀이한 답을 다른 사람들과 쉽게 공유할 수 있도록 만든 웹 애플리케이션

# 개발 파트

| 역할        | 이름      |
|:----------|:--------|
| 백앤드 개발자   | 김기현     |
| 프론트앤드 개발자 | 구글 제미나이 |

<br>

# 아키텍처 다이어그램

![img.png](/.img/프로젝트%20다이어그램.jpg)

이 아키텍처는 SpringBoot로 구현된 애플리케이션 서버를 기반으로 한다. Github를 코드 저장소로 활용하며, 
Github Actions를 통해 CI/CD 파이프라인 및 워크플로우를 구성하였습니다.
배포는 AWS EC2 서버에서 이루어지며 Docker를 배포 툴로 사용한다. 데이터 캐싱을 위해 Redis를 활용하고, 데이터베이스는 AWS RDS 클러스터에 MySQL을 사용하여 구축했습니다.

<br>

# 프론트앤드 구현

## 로그인되지 않은 뷰

### 홈
![img.png](.img/non-auth/홈.png)

### 풀이 목록

![img.png](.img/non-auth/풀이목록.png)

### 풀이 조회

![img.png](.img/non-auth/풀이%20조회.png)

### 회원가입

![img.png](.img/non-auth/회원가입.png)

### 로그인

![img.png](.img/non-auth/로그인.png)

## 로그인된 뷰

### 홈
![img.png](.img/auth/홈.png)

### 풀이 등록

![img.png](.img/auth/풀이%20등록.png)

### 풀이 조회

![img.png](.img/auth/풀이%20조회.png)

### 풀이 수정

![img.png](.img/auth/풀이%20수정.png)

### 풀이 삭제

![img.png](.img/auth/풀이%20삭제.png)

### 프로필

![img.png](.img/auth/프로필.png)
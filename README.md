# FOOD-RUN

---

<div align="center">

![image1](title.png)

### 쿠팡 이츠, 배달의 민족 앱을 모티브로 하여 만든 배달 애플리케이션 프로젝트입니다.

</div>

---

## 사용 기술 및 개발 환경

- Java 17
- Spring Boot 2.6.7
- MyBatis
- Junit5
- IntelliJ
- gradle
- GitHub Actions
- Naver Cloud Platform
- JaCoCo
- SonarQube
- Slack


## 개발 규칙7

- [Java Coding Convention](https://github.com/f-lab-edu/food-run/wiki/3.Convention#java-coding-convention)
- [Git Branch 전략](https://github.com/f-lab-edu/food-run/wiki/3.Convention#git-branch-%EC%A0%84%EB%9E%B5)
- [Git Commit Message Convention](https://github.com/f-lab-edu/food-run/wiki/3.Convention#commit-message-convention)
- 목표 테스트 커버리지 : **LINE** 기준, **70% 이상**

|                 실시간 측정                 |      
|:--------------------------------------:|
| ![coverage](.github/badges/jacoco.svg) |

## 주요 기능

- [기능 정리](https://github.com/f-lab-edu/food-run/wiki/1.Home#2-%EA%B8%B0%EB%8A%A5-%EC%86%8C%EA%B0%9C)
- [유스케이스](https://github.com/f-lab-edu/food-run/wiki/2.Use-Case)

## ERD

- [ERD 원본 - diagram.io](https://dbdiagram.io/d/627e692a7f945876b61451b4)

![](ERD.png)

## 기술 이슈 및 해결 과정

- [UserService 테스트의 Mock 객체를 이용한 단위 테스트 작성](https://velog.io/@dailyzett/%EB%8B%A8%EC%9C%84-%ED%85%8C%EC%8A%A4%ED%8A%B8)
- [네이버 맵 API 적용 및 서킷 브레이커 패턴 적용](https://github.com/f-lab-edu/food-run/pull/27)
- [GitHub Actions CI 적용](https://github.com/f-lab-edu/food-run/pull/19)
- [Redis 세션 스토리지 적용](https://github.com/f-lab-edu/food-run/pull/32)
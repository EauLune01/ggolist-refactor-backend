Language: Java 21

Framework: Spring Boot 3.x

Build Tool: Gradle

<Data & Database>
RDBMS: MySQL 8.0

NoSQL/Cache: Redis (Docker) — 토큰 블랙리스트 등록

Persistence: Spring Data JPA

Query Optimization: QueryDSL 5.0.0 (Jakarta): 다중 필터링 및 복잡한 정렬 조건이 필요한 메인 피드 조회를 위한 동적 쿼리 구현

Fetch Join (@Query): 고정된 연관 관계 조회가 필요한 경우 JPQL Fetch Join을 활용해 N+1 문제 해결 및 DB I/O 최적화

<Security & Auth>
Authentication: Spring Security

Token: JWT (jjwt 0.12.5)

Access / Refresh Token 기반의 Custom 인증 Flow 구현

Redis 연동을 통한 Logout 블랙리스트 처리 및 보안성 강화

<AI & Translation>
Translation Engine: Google Cloud Translation API — 카테고리 및 핵심 데이터의 다국어 번역 자동화

<API & Documentation>
API Specs: Swagger / SpringDoc OpenAPI 2.4.0

Validation: Hibernate Validation — DTO 레벨의 유효성 검증을 통한 데이터 무결성 확보

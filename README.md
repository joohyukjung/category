# Category for Musinsa project
#### 개발시 반드시 포함되어야 하는 항목
1. RDBMS (과제 작성시 H2 DB 사용 권장)
2. REST API

#### 개발시 반드시 구현되어야 하는 내용
1. 카테고리 등록 API
   1. 카테고리를 등록/수정/삭제 할 수 있어야 합니다.
2. 카테고리 조회 API
   1. 상위 카테고리를 이용해, 해당 카테고리의 하위의 모든 카테고리를 리턴 가능해야 합니다. 
   2. 상위 카테고리를 지정하지 않을 시, 전체 카테고리를 리턴해야 합니다.

## Installation and Getting Started
### 1. git install
    $ sudo yum install git -y
### 2. java install
    $ sudo yum install java-1.8.0-openjdk java-1.8.0-openjdk-devel -y
### 3. clone
    $ git clone https://github.com/joohyukjung/category.git
    $ cd category
### 4. maven build and server run
    $ ./mvnw spring-boot:run

    http://127.0.0.1:8080

## data.sql을 이용한 샘플 데이터 자동생성
    // 초기 사용자 생성
    INSERT INTO USER (NAME, EMAIL, PASSWORD) VALUES ('admin', 'admin@musinsa.com', '$2a$10$Jnq1GIZc7fZpQ2G70/VqK.q8BWVSi0K/Hl3BLvBPyyolcqdUB483.');
    
    // 초기 category 생성
    INSERT INTO CATEGORY (ID, VALUE, PARENTID) VALUES (1, '의류', 0); 
    INSERT INTO CATEGORY (ID, VALUE, PARENTID) VALUES (2, '상의', 1);
    INSERT INTO CATEGORY (ID, VALUE, PARENTID) VALUES (3, '반팔 티셔츠', 2);
    INSERT INTO CATEGORY (ID, VALUE, PARENTID) VALUES (4, '긴팔 티셔츠', 2);
    INSERT INTO CATEGORY (ID, VALUE, PARENTID) VALUES (5, '아우터', 1);
    INSERT INTO CATEGORY (ID, VALUE, PARENTID) VALUES (6, '후드 집업', 5);
    
    INSERT INTO CATEGORY (ID, VALUE, PARENTID) VALUES (7, '바지', 0);
    INSERT INTO CATEGORY (ID, VALUE, PARENTID) VALUES (8, '데님 팬츠', 7);
    INSERT INTO CATEGORY (ID, VALUE, PARENTID) VALUES (16, '데님 숏 팬츠', 8);
    INSERT INTO CATEGORY (ID, VALUE, PARENTID) VALUES (9, '코튼 팬츠', 7);
    INSERT INTO CATEGORY (ID, VALUE, PARENTID) VALUES (10, '숏 팬츠', 7);
    
    INSERT INTO CATEGORY (ID, VALUE, PARENTID) VALUES (11, '신발', 0);
    INSERT INTO CATEGORY (ID, VALUE, PARENTID) VALUES (12, '스니커츠', 11);
    INSERT INTO CATEGORY (ID, VALUE, PARENTID) VALUES (13, '캔버스/단화', 12);
    INSERT INTO CATEGORY (ID, VALUE, PARENTID) VALUES (14, '농구화', 12);
    
    INSERT INTO CATEGORY (ID, VALUE, PARENTID) VALUES (15, '뷰티', 0);
    
### API 설명
#### Default API
    POST, /signup
    사용자 회원가입
    
    POST, /auth
    jwt 토큰 생성 - category api(1~6)는 bearer token 필수

#### Category API 
    GET, /categories
    전체 categories 조회
    
    GET, /categories/{id}
    해당 id에 해당하는 category 조회 (하위 categories 포함)
    
    POST, /categories
    카테고리 생성
    
    PUT, /categories/{id}
    해당 id에 해당하는 category 수정
    
    DELETE, /categories/{id}
    해당 id에 해당하는 category 삭제 (하위 categories 포함)
    
    GET, /categories-not-hierarchy
    전체 categories 조회 (비계층구조, 전체나열)
    

    

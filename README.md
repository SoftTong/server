# Sotong - \***\*SW중심사업단 활동 신청 및 서류 제출 서비스\*\*** 🖥

## 💡 서비스 소개

\***\*SW중심사업단 활동 신청 및 서류 제출 서비스\*\***

> 충북대학교 학생들이 SW중심사업단 프로그램을 신청하고 관련 보고서 제출을 편리하게 할 수 있도록 개발한 프로젝트입니다.

## 🛠️ 기술 스택

| FrontEnd                                                                                             | BackEnd                                                                                                                                                                                                                                                                                                                                                                                                                                | DevOps                                                                                                       |
| ---------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------ |
| <img src="https://img.shields.io/badge/react-61DAFB?style=for-the-badge&logo=react&logoColor=black"> | <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> ![JPA](https://img.shields.io/badge/jpa-6DA55F.svg?style=for-the-badge&logo=springdatajpa&logoColor=white) ![MySQL](https://img.shields.io/badge/mysql-%230769AD.svg?style=for-the-badge&logo=mysql&logoColor=white) | ![Docker](https://img.shields.io/badge/docker-%23007ACC.svg?style=for-the-badge&logo=docker&logoColor=white) |

## 🗃️ 프로젝트 구조

### 🔗 ERD

![1](https://user-images.githubusercontent.com/57143818/190411772-f1d570a3-09b2-4c18-a689-8ec26dcf258f.png)

# 💻 주요 기능 및 데모 사진

## 👩🏻 사용자 페이지

### 로그인&회원가입

- JWT와 Spring Security를 이용해 로그인과 회원가입 기능을 구현하였습니다.
- 로그인한 사용자는 SW중심사업단의 프로그램을 신청할 수 있습니다.
  ![2](https://user-images.githubusercontent.com/57143818/190411785-23e9ab1e-6d6c-4857-bc03-2f26a7d64af9.png)

### 메인화면

- 메인 화면에서 사용자의 정보와 신청할 수 있는 프로그램, 사용자의 제출 현황을 확인할 수 있습니다.
  ![3](https://user-images.githubusercontent.com/57143818/190411802-d9472ba9-8475-491a-848b-3c1a016e8eab.png)

### 서류 제출&폼 작성

- 사용자는 신청하려는 프로그램에 대해 요구되는 파일 또는 폼을 작성하여 제출할 수 있습니다.
  ![4](https://user-images.githubusercontent.com/57143818/190411823-30d6728e-87ba-4ac7-a4a0-3b562d2c1c2b.png)

## 프로그램 신청 목록

- 사용자는 자신이 제출한 폼이나 파일을 확인할 수 있습니다.
- 사용자는 본인이 신청한 프로그램을 취소할 수 있습니다.
- 사용자는 본인이 신청한 프로그램이 승인되었는지 반려되었는지, 보류되었는지 알 수 있습니다.

![5](https://user-images.githubusercontent.com/57143818/190411851-01e3f80c-29af-40d0-9110-6713bc7fe024.png)
![6](https://user-images.githubusercontent.com/57143818/190411859-ee5130e5-4f26-4cb1-abde-9e590d4d2781.png)

---

## ⚙ 관리자 페이지

## 프로그램 공지 글 작성

- 관리자는 학생들을 모집할 프로그램 공지글을 작성할 수 있습니다.
- 태그는 최대 3개까지 설정가능 하며 URL 미리보기를 통해 보여줄 홈페이지를 미리 확인할 수 있습니다.

![7](https://user-images.githubusercontent.com/57143818/190411869-7ef035b4-e920-41c8-ad13-e1091147476d.png)

## 프로그램 신청서 제작

- 관리자는 학생들이 신청할 때 사용할 신청 폼의 질문을 추가하고 작성할 수 있습니다.

![8](https://user-images.githubusercontent.com/57143818/190411881-e12d3e67-6b71-44bd-8ce5-846ab8f96eb7.png)

## 프로그램 신청서 확인

- 관리자는 해당 프로그램을 신청한 학생들의 파일을 다운로드 받거나 제출 폼을 확인할 수 있습니다.
  ![9](https://user-images.githubusercontent.com/57143818/190411894-64b5be89-d8e7-474c-a524-d8c781188cfa.png)

## 프로그램 신청 승인, 보류, 거절

- 관리자는 각각의 프로그램을 신청한 사용자의 제출을 승인하거나 보류하거나 거절할 수 있습니다.
  ![10](https://user-images.githubusercontent.com/57143818/190411905-bab61a7c-c985-49a9-b213-1a06c4cc02e8.png)

# Getting Started

## Prerequisites

- Docker > 19.x

## Installation

1. 소스코드 다운로드 - SoTong 클론 후 해당 디렉토리에서 서브 모듈 server, client 클론 진행
   ```shell
   $ git clone https://github.com/SoftTong/SoTong.git
   $ cd SoTong
   SoTong$ git clone https://github.com/SoftTong/client.git
   SoTong$ git clone https://github.com/SoftTong/server.git
   ```
   or
   ```shell
   $ git clone https://github.com/SoftTong/SoTong.git
   $ cd SoTong
   $ git submodule init
   $ git submodule update
   ```
2. 리액트 npm 패키지 설치
   ```shell
   $ cd client
   client$ npm install
   ```
3. 스프링부트 gradle 빌드
   ```shell
   $ cd server
   server$ ./gradlew build
   ```

# Usage

- 도커 이미지 빌드 및 컨테이너 실행
  ```shell
  $ docker-compose up --build
  ```
- 컨테이너 실행
  ```shell
  $ docker-compose up
  ```
- 컨테이너 다운
  ```shell
  $ docker-compose down
  ```

# server

sotong 프로젝트 server repository <br>
[client repository](https://github.com/SoftTong/client)

# Getting Started

## Prerequisites

- java > 11
- springframework boot > 2.5.2

## build

- 스프링부트 gradle 빌드
  - 윈도우
    ```shell
    $ gradlew build
    ```
  - 리눅스
    ```shell
    $ ./gradlew build
    ```

## run

- 스프링부트 실행
  ```shell
  $ cd build/libs
  build/libs$ java -jar demo-0.0.1-SNAPSHOT.jar
  ```

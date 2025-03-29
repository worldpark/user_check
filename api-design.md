# API 설계

---
## 공통

### 유저 리스트

- /api/users
  - [GET]
    - 유저 목록 출력

### 유저정보

- /api/{userId}/user
  - [GET] 
    - 유저 상세 정보
  - [POST] 
    - 유저 생성
  - [PUT]
    - 유저 정보 수정
  - [DELETE]
    - 유저 삭제
---

## 유저

### 자신의 출결 리스트

- /api/user/{userId}/{nowDate}/attendances
  - [GET]
    - 자신의 출결 리스트 및 상태 출력

### 출결 체크

- /api/user/{userId}/{statusId}/check
  - [PUT]
    - 출결체크

---

## 관리자

### 출결 프로젝트 관리

- /api/admin/{userId}/attendance
  - [GET]
    - 관리할 출결 리스트 출력
  - [POST]
    - 출결 생성
  - [PUT]
    - 출결 수정
  - [DELETE]
    - 출결 삭제

### 출결 대상 관리

- /api/admin/{attendanceId}/attendance-target
  - [GET]
    - 출결 대상 출력
  - [POST]
    - 출결 대상 등록
  - [PUT]
    - 출결 대상 수정
  - [DELETE]
    - 출결 대상 삭제

### 출결 시간 관리

- /api/admin/{attendanceId}/attendance-time
  - [GET]
    - 출결 시간 출력
  - [POST]
    - 출결 시간 생성
  - [PUT]
    - 출결 시간 수정
  - [DELETE]
    - 출결 시간 삭제

### 출결 상태 관리

- /api/admin/{attendanceTimeId}/attendace-status
    - [GET]
        - 출결 상태 출력
    - [POST]
        - 출결 상태 생성
    - [PUT]
        - 출결 상태 수정
    - [DELETE]
        - 출결 상태 삭제
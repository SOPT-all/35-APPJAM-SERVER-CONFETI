![image](https://github.com/user-attachments/assets/14525600-f778-424c-a756-b56afd75da52)

<br/> <strong> "콘서트, 페스티벌 티켓 정보를 한눈에, 손쉽게!"</strong>

1. 내가 원하는 콘서트 및 페스티벌 정보를 모아서

2. 찜한 공연들을 바탕으로 나만의 타임테이블을 만들어

3. 중요한 일정들을 한눈에 확인할 수 있는 곳, **confeti**

---

## 📌 Main Function
![image](https://github.com/user-attachments/assets/045e2344-fff3-459d-b419-d11628ad3fa8)
![image](https://github.com/user-attachments/assets/ed37656e-0f04-4e37-81f1-efdebe0ed554)
![image](https://github.com/user-attachments/assets/df919bd8-8be6-44b8-b55e-c47335529d8a)
![image](https://github.com/user-attachments/assets/304485c6-823f-4529-9eb9-5476c22cd810)
![image](https://github.com/user-attachments/assets/5729826e-dd23-4f1c-af0b-92e413d9b3e2)
![image](https://github.com/user-attachments/assets/64bcdf03-75f9-4217-95c1-836b40f61796)
![image](https://github.com/user-attachments/assets/c5b0e20c-c423-4801-ae3d-36c13f5fc148)
![image](https://github.com/user-attachments/assets/f5fc6ff3-53ee-4ffa-8ffd-8edab28c1749)
![image](https://github.com/user-attachments/assets/3f0ddc75-1792-4c8b-aa64-63b95d9579b9)
![image](https://github.com/user-attachments/assets/40b3facd-4fa6-49af-b3d8-c7c4be93ec6f)

---

## ✨ Contributors

| 오치현 | 박상아 | 정정교 |
| :---: | :---: | :---: |
|![치현](https://github.com/user-attachments/assets/41903db0-9064-43b7-8490-66218225899a)|![상아](https://github.com/user-attachments/assets/ca09e254-24ee-440d-9c09-f8201913c267)|![image](https://github.com/user-attachments/assets/4aec7630-77a6-48a2-8802-26bf8f222152)|
|[@chihyun](https://github.com/ch1hyun)|[@Ivoryeee](https://github.com/Ivoryeee)|[@junggyo1020](https://github.com/junggyo1020)|

## 🔗 Architecture
![image](https://github.com/user-attachments/assets/99057285-6e07-4ff6-8218-d1d65545ca74)

---

## 💿 ERD
<img width="1134" alt="image" src="https://github.com/user-attachments/assets/a25eb17d-6fa2-4255-9c6f-f55eb96c78c6" />

---

## 🔖 Commit Convention
- **feat**: 새로운 기능 추가 (feature)
- **fix**: 버그 수정 (bug fix)
- **docs**: 문서 변경 (documentation)
- **style**: 코드 포맷팅, 세미콜론 누락 등 코드 변경이 없는 경우
- **refactor**: 코드 리팩토링
- **test**: 테스트 코드 추가, 수정
- **chore**: 빌드 프로세스나 도구 설정 변경
- **ci**: CI 관련 설정 변경
- **cd**: CD 관련 설정 변경
- **perf**: 성능 개선 관련 변경

---

## 🔥 Git Convention
[Git Convention](https://wonderful-celestite-e3c.notion.site/Git-Convention-165210e281b0802588eee44841095e16?pvs=4)

---

## 🔥 Code Convention
[Code Convention](https://wonderful-celestite-e3c.notion.site/Code-Convention-165210e281b080368966ccf6087e07ff?pvs=4)

---

## 📚 Tech Stack
[![My Skills](https://skillicons.dev/icons?i=java,gradle,spring,mysql,aws,nginx,docker,githubactions)](https://skillicons.dev)

---

## 📁 Folder Structure
```
📁 api
┣ 📁 controller
┣ 📁 dto
┃┣ 📁 response
┃┣ 📁 request
┣ 📁 facade
┣ 📁 vo (값 객체)
📁 auth
┣ 📁 command
┣ 📁 dto
┣ 📁 jwt
📁 domain
┣ 📁 model (entity 이름)
┃┣ 📁 application (해당 서비스들이나, 비즈니스 로직들)
┃┃┣ 📁 dto
┃┃┃┣ 📁 response
┃┃┃┣ 📁 request
┃┣ 📁 infra (외부 의존성과 연결된 친구들 jpa repository 같은 친구, or aws 서비스들)
┃┃┣ 📁 repository
📁 global
┣ 📁 annotation (커스텀 어노테이션)
┣ 📁 common (공통 로직)
┃┣ 📁 constant (상수값)
┣ 📁 message (응답 메세지)
┣ 📁 resolver (커스텀 리졸버)
┃┣ 📁 artist (외부 음악 스트리밍 Open API 연동 로직)
┣ 📁 util (유틸 객체, 모두가 사용할 수 있는 객체들)
┣ 📁 exception (exception 및 exceptionHandler 관리)
┣ 📁 config (설정 파일)
```

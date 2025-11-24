<br>

![로고](https://github.com/user-attachments/assets/3a7004f8-f6ed-46f9-9e58-f1f43a0b87c1)

**공연의 설렘부터 감동까지, 공연의 유기적인 흐름을 설계하다.**
> 콘페티는 페스티벌과 공연 정보를 한눈에 보고, 예매 일정과 아티스트 소식을 효율적으로 관리할 수 있는 플랫폼이에요. <br>
> 공연을 준비·관람·공유까지 이어지는 전 과정을 담아낼 수 있는 플랫폼으로 자리매김하고, 공연 경험의 감동과 여운을 확장할 수 있는 방향을 고려했어요.

<br>

[![CONFETI 바로가기](https://img.shields.io/badge/🔗%20CONFETI-바로가기-B5F602?style=for-the-badge&logoColor=white&labelColor=323339)](https://www.confeti.co.kr/)
[![CONFETI 팀블로그 바로가기](https://img.shields.io/badge/🔗%20CONFETI%20팀%20블로그-바로가기-B5F602?style=for-the-badge&logoColor=white&labelColor=323339)](https://confeti.palms.blog/)
![앱잼 35th 우수상](https://img.shields.io/badge/🏆%20SOPT%2035기%20앱잼-우수상%20수상작-B5F602?style=for-the-badge&logoColor=white&labelColor=323339)

<br />

## Service Overview

<img width="100%" alt="서비스설명1" src="https://github.com/user-attachments/assets/06828f33-cd73-48bf-95f6-6360b380a9a2" />
<img idth="100%" alt="서비스설명2" src="https://github.com/user-attachments/assets/798a2ef3-d5af-4257-81f8-d2ad752a2dda" />
<img width="100%" alt="서비스설명3" src="https://github.com/user-attachments/assets/cc9a2004-8ac7-42a3-aca6-7cab86b396c4" />
<img width="100%" alt="서비스설명4" src="https://github.com/user-attachments/assets/6ba1c190-9c3c-4a7c-910c-d63319d45a54" />

<br />

## ✨ Contributors

|                                          오치현                                           |                                          박상아                                           |                                            정정교                                            |                                            김재헌                                            |
|:--------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------:|:-----------------------------------------------------------------------------------------:|:-----------------------------------------------------------------------------------------:|
| ![치현](https://github.com/user-attachments/assets/41903db0-9064-43b7-8490-66218225899a) | ![상아](https://github.com/user-attachments/assets/ca09e254-24ee-440d-9c09-f8201913c267) | ![image](https://github.com/user-attachments/assets/4aec7630-77a6-48a2-8802-26bf8f222152) | ![image](https://github.com/user-attachments/assets/96cce384-f842-48c5-8eb3-39c16911a953)
|                         [@chihyun](https://github.com/ch1hyun)                         |                        [@Ivoryeee](https://github.com/Ivoryeee)                        |                      [@junggyo1020](https://github.com/junggyo1020)                       |                      [@jher235](https://github.com/jher235)                       |

## 🔗 Architecture

<img width="1081" height="721" alt="아키텍처 drawio (1)" src="https://github.com/user-attachments/assets/948dd9aa-83d2-44c2-99ed-7c6ef2e2208e" />

---

## 💿 ERD

<img width="1840" height="972" alt="CONFETI ERD" src="https://github.com/user-attachments/assets/d40c6dc3-426c-4535-8c4c-37a6d8d8d17f" />

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

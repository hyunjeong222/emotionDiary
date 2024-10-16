# 😊😭😣 EmotionDiary - 감정 일기장


### 📅 프로젝트 진행 기간

2024.07.14(일) ~ 2024.07.29(월) (16일간 진행)
<br>
<br>

### 🚩 감정 일기장 소개

그날그날 겪은 일에 대해 느낀 나의 감정과 생각을 기록해 보세요!

일기를 쓰면 스트레스 해소에 도움이 되고, 자존감과 회복 탄력성을 기를 수 있어요.

감정 일기장을 통해, 감정에 휘둘리지 않고 내 마음을 스스로 잘 이해하고 표현해 보세요.
<br>
<br>

### ✨ 주요 기능

- #### 📕 일기 생성
	- 오늘 느낀 감정을 선택하고, 날짜, 내용을 지정하여 새로운 일기를 생성할 수 있습니다.

- #### 📗 일기 조회
	- 연/월을 선택하여 해당 월에 작성했던 모든 일기를 조회할 수 있습니다.
<br>

### 💫 개발환경 & 주요 기술

#### Backend

- Java : 17
- SpringBoot : 3.3.2
- Hibernate : 6.5.2.Final
- JPA : 3.1.0

#### DataBase

- MySQL : 8.0.34

#### Frontend

- Thymeleaf 3.3.2
- jquery 3.5.1

#### IDE

- Visual Studio Code IDE
- HeidiSQL
- InteliJ
<br>

### 👨‍💻 팀원

| 이름   | GitHub                                         | 개발  | 역할                                  |
| ------ | ---------------------------------------------- | ------- | -------------------------------------|
| 정현정 | <a href="https://github.com/hyunjeong222"><img src="https://github.com/hyunjeong222.png" width="100"></a> | BE | <ul><br><li>JWT 로그인 구현</li> <li>API 설계, 구현, 유지보수</li> <li>프론트와 백단 API 적용 (fetch 활용)</li> <li>회원, 일기 정보 등 RDBMS(MySQL)에 저장</li><br></ul> |
<br>

### 🌟 ERD

![erd](https://github.com/user-attachments/assets/d94dc285-32af-4817-90f2-91f516cb0df6)
<br>
<br>

### 🌟 서비스 화면

#### 💡 회원가입

- 아이디, 비밀번호, 이름, 닉네임, 개인정보 수집 및 이용약관 동의를 입력받게 됩니다.

- 개인정보 수집 및 이용약관 동의는 필수 동의 사항입니다.

- '가입하기' 버튼을 누르면 로그인 페이지로 이동합니다.

![회원가입](https://github.com/user-attachments/assets/e9abcef0-1df5-4f0e-b040-9bac84576fd0)


#### 💡 로그인

- 아이디와 비밀번호를 입력한 후 '로그인 버튼'을 누르면 메인 페이지로 이동합니다.

- 가입한 회원이 아니거나 아이디, 비밀번호가 틀리다면 로그인을 할 수 없습니다.

![로그인](https://github.com/user-attachments/assets/92a6cd99-8ed8-45c3-8d09-ccedb6b43920)


#### 💡 메인 페이지

- 상단의 '<', '>' 버튼을 클릭하여 선택된 연/월에 작성된 모든 일기를 조회할 수 있습니다.

- 일기를 작성한 날의 감정, 작성 날짜, 내용을 간략하게 확인할 수 있습니다.

- 연/월을 선택하지 않은 경우 현재 날짜를 기준으로 조회됩니다.

![메인페이지02](https://github.com/user-attachments/assets/278deae7-22f4-423f-af97-1e368bdb4a7c)
![메인페이지](https://github.com/user-attachments/assets/0163c967-685c-497f-8997-30039845bac9)


#### 💡 생성 페이지

- 메인 페이지의 '새 일기 쓰기' 버튼을 통해 새 일기를 생성할 수 있습니다.

- '새 일기 쓰기'는 회원에게만 제공되는 서비스며, 회원이 아닌 경우 로그인 페이지로 이동하게 됩니다.

- 작성 날짜, 감정을 선택하고 내용을 작성한 후 일기를 생성하게 되면 메인 페이지로 이동하게 됩니다.

![작성페이지](https://github.com/user-attachments/assets/c7af830f-f6c6-4a86-aa6b-7e9e134d890d)


#### 💡 상세 페이지

- 메인 페이지에서 일기를 선택하면 해당 일기의 상세 페이지로 이동하게 됩니다.

- 선택한 일기의 작성 날짜, 감정, 내용을 확인할 수 있습니다.

- 오른쪽 상단의 '수정하기' 버튼을 누르면 수정 페이지로 이동하게 됩니다.

![상세페이지](https://github.com/user-attachments/assets/9a337a36-31e6-41a4-b049-c8e14a38aad0)


#### 💡 수정 페이지

- 일기의 작성 날짜, 감정, 내용을 수정할 수 있습니다.

- 수정 후 '수정 완료' 버튼을 누르면 상세 페이지로 이동되며, 수정된 일기를 확인할 수 있습니다.

- 오른쪽 상단의 '삭제하기' 버튼을 누르면 일기 삭제 후 메인 페이지로 이동하게 됩니다.

![수정페이지](https://github.com/user-attachments/assets/2f108b7e-0c91-41d7-bc8f-5dc332d9f586)





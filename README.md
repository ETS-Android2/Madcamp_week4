# MadCamp-week4

## 팀원 : 이주은 김경하 송재현

## Development Environment

#### *Android version 4.2.1*

  * compileSdkVersion version : 30
  * buildToolsVersion version : 30.0.3
  * minSdkVersion version : 26
  * targetSdkVersion : 30
  * MongoDB version : 
  * Ubuntu : 18.04.2

## Introduction
여행 경로를 탐색하고 조회할 수 있으며, 다른 사용자와 함께 여행 계획과 사진을 기록하는 여행 앱

## 기능
<img src="https://user-images.githubusercontent.com/77712822/127565274-8a28aaa3-49c2-4c77-a9ca-c4e3b1332e0a.png" width="300" height="600">

1. 로그인과 자동로그인, 로그아웃
> 로그인이 가능하고, 이후 로그아웃 버튼으로 로그아웃 하기 전까지는 자동로그인이 가능하다.
> 
> 로그인 후 세개의 메뉴 버튼이 있어 각각 다른 기능들로 이어진다.

### 2. 여행 경로 탐색 및 저장

<img src="https://user-images.githubusercontent.com/77712822/127565637-5885c134-73f1-45e1-8e58-34d669229107.png" width="200" height="500"><img src="https://user-images.githubusercontent.com/77712822/127565714-401ce6ec-521e-4ed1-b322-8d3a937df11d.png" width="200" height="500"><img src="https://user-images.githubusercontent.com/77712822/127565796-04a07fd1-1023-456a-b194-a308c0e06f23.png" width="200" height="500"><img src="https://user-images.githubusercontent.com/77712822/127565921-cfd1f58f-b73e-466d-b203-ea8729e02d3f.png" width="200" height="500"><img src="https://user-images.githubusercontent.com/77712822/127566676-d1b3ff13-c77f-463a-a8eb-1909adc090d4.png" width="200" height="500">

> 먼저 여행 인원을 정한다. 혼자하는 여행이라면 바로 장소를 고르게되고, 인원이 두명 이상이라면 함께할 사용자를 초대하게된다. 
> 
> 사용자를 검색해 사진을 클릭하면 추가되고, 설정한 인원까지 추가할 수 있다. 

> 지도에서 가고싶은 장소를 추가하고, 경로조회 버튼을 통해 경로를 확인할 수 있다.
> 
> 캘린더에 들어가면, 미리 기록된 각 사용자의 여행을 갈 수 있는 날짜들을 조합해 모두가 가능한 날을 보여준다. 그 날짜 중에서 여행을 가는 날짜를 눌러 여행 일정을 잡을 수 있다.
> 
> 계획한 일정을 여행제목과 함께 저장하면 마이페이지로 옮겨진다.
> 

### 3. 다른 사용자의 경로 탐색 가능

<img src="https://user-images.githubusercontent.com/77712822/127566242-f75dc425-3085-4eb6-8713-8f8cbbe22b3f.png" width="200" height="600">

> 모든 사용자들의 여행 경로들을 조회할 수 있다.
> 
> 각 일정을 클릭하면 상세 장소들을 볼 수 있다.

### 4. 본인의 프로필과 여행 기록 조회 및 사진 저장

<img src="https://user-images.githubusercontent.com/77712822/127566399-0cdb8529-2635-4a32-a733-bb05bb65af9b.png" width="200" height="500"><img src="https://user-images.githubusercontent.com/77712822/127566466-1ede7920-57be-419b-96e3-6bd6b3f9fe1f.png" width="200" height="500"><img src="https://user-images.githubusercontent.com/77712822/127566515-582ad076-b04b-4f39-b770-aa711721b1df.png" width="200" height="500">

> 본인이 참가한 여행기록이 모두 보여진다. 간 여행 횟수와 친구들의 수도 표시된다.
> 
> 프로필 사진을 추가, 수정 및 삭제할 수 있다.
> 
> 아래의 달력 버튼을 누르면 본인이 불가능한 날짜를 골라 일정을 기록할 수 있다. 저장 후 새로고침 버튼을 통해 업데이트 할 수 있다.
> 
> 여행 기록을 클릭하면 상세 장소가 나오고, 대표사진을 설정, 삭제할 수 있다.
> 
> 각각의 장소를 클릭하면 장소 별 사진과 메모를 볼 수 있다.


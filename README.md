# LLM_PRJ
Pet Dog Growth Web

## 개요
해당 프로젝트는 반려 강아지의 성장, 훈련 등 생애주기에 따른 필요한 정보를 알려주고 로드맵을 제공하는 웹이다.

## 주요 기능
 - 반려견을 키우는데 필요한 정보를 사용자에게 체크리스트 형태로 로드맵 제공
 - 사용자의 계정을 관리하고 JWT 토큰으로 권한 제공
 - Cloudflare와 EC2로 나눠 배포함으로 비용 효율성과 보안이 높음

## 기술 스택
|구분|기술|
|:---:|:---|
|**FRONT**|React + Vite|
|**BACK & DB**|Spring / PostgreSQL|
|**Deploy**|Docker / Github Actions / Cloudflare Pages / AWS EC2|

## k6 성능 평가
 ```
http_req_duration..............: avg=268.81ms min=142.25ms med=241.51ms max=2.65s    p(90)=361.53ms p(95)=409.7ms
  { endpoint:signup }..........: avg=245.29ms min=172.2ms  med=240.89ms max=2.65s    p(90)=268.16ms p(95)=283.74ms 
  { endpoint:login }...........: avg=366.03ms min=250.86ms med=336.64ms max=2.28s    p(90)=435.4ms  p(95)=505.97ms
  { endpoint:me }..............: avg=195.11ms min=142.25ms med=198.33ms max=819.73ms p(90)=216.66ms p(95)=220.03ms
 ```

## 실제 화면
|메인|내용|
|:---:|:---:|
|<img width="400" alt="Image" src="https://github.com/user-attachments/assets/9f178bc6-9ddf-4794-a350-915a5540d6f2" />|<img width="400" alt="Image" src="https://github.com/user-attachments/assets/aedde448-61f9-459c-8756-76433ac5acc0" />|
|메뉴|푸터|
|<img width="400" alt="Image" src="https://github.com/user-attachments/assets/cdd9a845-2220-4f52-970c-205bf07d78f0" />|<img width="400" alt="Image" src="https://github.com/user-attachments/assets/4e306f13-25c0-4fb4-9279-ebb6e01b9a57" />|

## 폴더 구조
```
LLM-PRJ
+---BACKEND
|   +---src
|       +---main
|           +---java/com/example/HaruDang
|               +---config
|               +---controller
|               +---dto
|               +---entity
|               +---exception
|               +---repository
|               +---security
|               +---service
|           +---resources
+---FRONTEND
|   +---public
|   +---src
|       +---api
|       +---assets
|       +---components
|       +---context
|       +---hooks
|       +---pages
+---LLM
|   +---...
+---NGINX
|   +---...
+---DATA
|   +---...
```

## 데이터 출처
 - 반려견 성장 및 질병관련 말뭉치 데이터 : https://www.aihub.or.kr/aihubdata/data/view.do?currMenu=115&topMenu=100&aihubDataSe=data&dataSetSn=71879
 - 국가동물보호정보시스템 : https://www.animal.go.kr/front/index.do
 - 동물사랑배움터 : https://apms.epis.or.kr/home/kor/main.do

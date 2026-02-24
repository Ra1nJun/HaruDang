# LLM_PRJ
Pet Dog Growth Chatbot

## 개요
해당 프로젝트는 반려 강아지의 성장, 훈련 등 생애주기에 따른 필요한 정보를 알려주고 로드맵을 제공하는 웹이다.

## 주요 기능
 - 반려견을 키우는데 필요한 정보를 사용자에게 체크리스트 형태로 로드맵 제공
 - 사용자의 계정을 관리하고 JWT 토큰으로 권한 제공

## 기술 스택
|Front|Back & DB|Deploy|
|:---:|:---:|:---:|
|React + Vite|Python / FastAPI / Spring / PostgreSQL| Docker / Github Actions / Cloudflare Pages / AWS EC2 |

## k6 성능 평가
 ```
    http_req_duration..............: avg=190.44ms min=146.57ms med=157.81ms max=689.28ms p(90)=255.98ms p(95)=285.79ms
      { endpoint:login }...........: avg=258.09ms min=225.4ms  med=240.59ms max=689.28ms p(90)=308.53ms p(95)=348.32ms
      { endpoint:me }..............: avg=156.32ms min=147.62ms med=156.69ms max=375.93ms p(90)=161.26ms p(95)=163.44ms
      { endpoint:signup }..........: avg=156.91ms min=146.57ms med=155.7ms  max=370.21ms p(90)=160.27ms p(95)=169.57ms
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
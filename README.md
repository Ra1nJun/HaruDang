# LLM_PRJ
Pet Dog Growth Chatbot

## 개요
해당 프로젝트는 반려 강아지의 성장, 훈련 등 생애주기에 따른 필요한 정보를 알려주고 답해주는 챗봇 웹이다.

## 주요 기능
 - 사용자에 질의에 응답해주는 RAG 기반 LLM
 - 자체적인 사용자의 계정 관리

## 기술 스택
|Front|Back|DB|
|:---:|:---:|:---:|
|React + Vite|Python / FastAPI / Spring|ElasticSearch / PostgreSQL|

## k6 성능 평가
 ```
    http_req_duration..............: avg=190.44ms min=146.57ms med=157.81ms max=689.28ms p(90)=255.98ms p(95)=285.79ms
      { endpoint:login }...........: avg=258.09ms min=225.4ms  med=240.59ms max=689.28ms p(90)=308.53ms p(95)=348.32ms
      { endpoint:me }..............: avg=156.32ms min=147.62ms med=156.69ms max=375.93ms p(90)=161.26ms p(95)=163.44ms
      { endpoint:signup }..........: avg=156.91ms min=146.57ms med=155.7ms  max=370.21ms p(90)=160.27ms p(95)=169.57ms
 ```

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
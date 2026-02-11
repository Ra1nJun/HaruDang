# LLM_PRJ
Pet Dog Growth Chatbot

## 개요
해당 프로젝트는 반려 강아지의 성장, 훈련 등 생애주기에 따른 필요한 정보를 알려주고 답해주는 챗봇 웹이다.

## 주요 기능
 - 사용자에 질의에 응답해주는 RAG 기반 LLM
 - 자체적인 사용자의 계정 관리

## 기술 스택
|Front|Back|DB|
|:---:|:---:|:---:|:---:|
|React + Vite|Python / FastAPI / Spring|ElasticSearch / PostgreSQL|

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
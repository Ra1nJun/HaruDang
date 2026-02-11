FROM python:3.12-slim

# 1. uv 설치
COPY --from=ghcr.io/astral-sh/uv:latest /uv /uvx /bin/

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. C 빌드 도구 및 필요한 라이브러리 설치 (zlib-state 빌드 필수 환경)
RUN apt-get update && apt-get install -y --no-install-recommends \
    build-essential \
    gcc \
    libz-dev \
    && rm -rf /var/lib/apt/lists/*

# 4. 환경 변수 설정
ENV UV_COMPILE_BYTECODE=1
ENV UV_LINK_MODE=copy

# 5. 설정 파일 복사
COPY pyproject.toml uv.lock* ./

# 6. 의존성 설치
RUN uv sync --frozen --no-cache

# 7. 소스 코드 복사 및 최종 설치
COPY __init__.py ./
COPY Agent.py ./
COPY Dto.py ./
COPY Serve.py ./

# 8. 비루트 사용자 생성
RUN groupadd --gid 1000 appgroup \
    && useradd --uid 1000 --gid appgroup --create-home --shell /bin/bash appuser

# 8-1. 비루트 사용자용 HF 캐시 경로
ENV HF_HOME=/app/hf_home
ENV TRANSFORMERS_CACHE=/app/hf_home/transformers
ENV HUGGINGFACE_HUB_CACHE=/app/hf_home/hub

# 8-2. 디렉터리 만들고 비루트 사용자에게 소유권 넘김
RUN mkdir -p /app/hf_home /app/hf_home/transformers /app/hf_home/hub \
 && chown -R appuser:appgroup /app/hf_home

# 9. 실행
User appuser
ENV PYTHONUNBUFFERED=1
ENTRYPOINT ["uv", "run", "python", "Serve.py"]
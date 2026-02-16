FROM python:3.12-slim

# 1. uv 설치
COPY --from=ghcr.io/astral-sh/uv:latest /uv /uvx /bin/

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. 필수 빌드 도구 설치
RUN apt-get update && apt-get install -y --no-install-recommends \
    build-essential \
    gcc \
    libz-dev \
    && rm -rf /var/lib/apt/lists/*

# 4. 환경 변수 설정
ENV UV_COMPILE_BYTECODE=1
ENV UV_LINK_MODE=copy
ENV PYTHONUNBUFFERED=1

# 5. 비루트 사용자 및 그룹 생성
RUN groupadd --gid 1000 appgroup \
    && useradd --uid 1000 --gid appgroup --create-home appuser

# 6. 설정 파일 복사 및 소유권 변경
COPY --chown=appuser:appgroup pyproject.toml uv.lock* ./

# 7. 허깅페이스 캐시 디렉토리 생성 및 권한 부여
ENV HF_HOME=/app/hf_home
RUN mkdir -p /app/hf_home && chown -R appuser:appgroup /app

# 8. 이제부터 모든 작업은 appuser로 수행
USER appuser

# 9. 의존성 설치
RUN uv sync --frozen --no-cache

# 10. 소스 코드 복사
COPY --chown=appuser:appgroup __init__.py Agent.py Dto.py Serve.py ./

# 11. 실행
ENTRYPOINT ["uv", "run", "python", "Serve.py"]
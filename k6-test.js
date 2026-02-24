// signup-login-test.js
import http from 'k6/http';
import { check, sleep } from 'k6';

const BASE_URL = __ENV.BACK_DOMAIN; // .env에서 못 읽기 때문에 실행할 때 같이 넘겨줘야 함

// 테스트 계정 목록
const USER = { email: 'test01@example.com', password: 'test1234!', nickname: 'Tester01' };

export const options = {
  insecureSkipTLSVerify: true, // TLS 인증 건너뛰기
  stages: [
    { duration: '1m', target: 50 }, // 1분 동안 0명에서 20명까지 서서히 증가
    { duration: '3m', target: 50 }, // 3분 동안 20명 유지
    { duration: '1m', target: 0 },  // 1분 동안 0명으로 서서히 감소
  ],
  thresholds: {
    'http_req_duration': ['p(95)<500'], // 전체 요청
    'http_req_duration{endpoint:signup}': ['p(95)<300'], // signup
    'http_req_duration{endpoint:login}': ['p(95)<400'], // login
    'http_req_duration{endpoint:me}': ['p(95)<300'], // me
  },
};

export default function () {
  const user = USER;

  // signup
  const signupRes = http.post(
    `${BASE_URL}/users/register`,
    JSON.stringify({ email: `test_${__VU}@example.com`, password: user.password, nickname: `${user.nickname}_${__VU}` }),
    {
      headers: { 'Content-Type': 'application/json' },
      tags: { endpoint: 'signup' },
    },
  );
  check(signupRes, {
    'signup ok or already exists': (r) => {
      const okStatus = r.status === 201 || r.status === 200;
      let okCode = false;
      try {
        const body = JSON.parse(r.body);
        okCode = body.code === 'U002'; // 이미 존재하는 유저 코드
      } catch (e) {}
      return okStatus || okCode;
    },
  });

  // login
  const loginRes = http.post(
    `${BASE_URL}/auth/login`,
    JSON.stringify({ email: `test_${__VU}@example.com`, password: user.password }),
    {
      headers: { 'Content-Type': 'application/json' },
      tags: { endpoint: 'login' },
    },
  );
  
  const loginCheck = check(loginRes, { 'login 200': (r) => r.status === 200 });

  // me
  if (loginCheck) {
    const meRes = http.get(`${BASE_URL}/auth/me`, {
      tags: { endpoint: 'me' },
      cookies: loginRes.cookies
    });
    
    check(meRes, { 'me 200': (r) => r.status === 200 });
  }

  sleep(1);
}
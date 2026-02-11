import React from 'react';
import './InfoPage.css';

const PrivacyPolicyPage = () => {
  return (
    <div className="info-page-container">
      <header className="info-header">
        <h1>개인정보 처리방침</h1>
        <p>HARU DANG은 사용자의 개인정보를 소중히 여기며, 관련 법령을 준수합니다.</p>
      </header>
      <main className="info-content">
        <section>
          <h2>1. 수집하는 개인정보</h2>
          <p>HARU DANG은 다음과 같은 개인정보를 수집할 수 있습니다:</p>
          <ul>
            <li>이름, 이메일 주소</li>
            <li>계정 정보 (사용자명, 비밀번호)</li>
            <li>문의 관련 정보</li>
            <li>IP 주소, 접속 기록, 쿠키</li>
          </ul>
        </section>

        <section>
          <h2>2. 개인정보의 이용 목적</h2>
          <p>수집된 개인정보는 다음의 목적으로만 사용됩니다:</p>
          <ul>
            <li>회원 서비스 제공 및 관리</li>
            <li>고객 문의 응답</li>
            <li>서비스 개선 및 통계 분석</li>
            <li>법적 의무 이행</li>
          </ul>
        </section>

        <section>
          <h2>3. 개인정보의 보호</h2>
          <p>HARU DANG은 사용자의 개인정보를 보호하기 위해 다음과 같은 조치를 취합니다:</p>
          <ul>
            <li>암호화된 통신 (HTTPS 사용)</li>
            <li>접근 권한 제한</li>
            <li>정기적인 보안 감시</li>
            <li>개인정보 보호 정책 준수</li>
          </ul>
        </section>

        <section>
          <h2>4. 개인정보의 제3자 공유</h2>
          <p>HARU DANG은 사용자의 명시적 동의가 없는 한 개인정보를 제3자에게 공유하지 않습니다. 단, 법적 의무가 있을 경우 예외입니다.</p>
        </section>

        <section>
          <h2>5. 개인정보의 보유 기간</h2>
          <p>개인정보는 서비스 제공 목적을 달성하기 위해 필요한 기간 동안 보유되며, 그 이후 안전한 방법으로 삭제됩니다.</p>
        </section>

        <section>
          <h2>6. 사용자의 권리</h2>
          <p>사용자는 다음의 권리를 가집니다:</p>
          <ul>
            <li>개인정보 조회 및 수정 요청</li>
            <li>개인정보 삭제 요청</li>
            <li>개인정보 처리 중단 요청</li>
            <li>개인정보 이전 요청</li>
          </ul>
        </section>

        <section>
          <h2>7. 쿠키 사용</h2>
          <p>HARU DANG은 사용자 경험 개선을 위해 쿠키를 사용합니다. 브라우저 설정을 통해 쿠키 사용을 거부할 수 있습니다.</p>
        </section>

        <section>
          <h2>8. 광고</h2>
          <p>HARU DANG은 Google AdSense를 통해 광고를 표시할 수 있습니다. Google은 쿠키를 사용하여 사용자의 관심사에 맞는 광고를 제공합니다.</p>
        </section>

        <section>
          <h2>9. 정책 변경</h2>
          <p>이 개인정보 처리방침은 사전 공지 없이 변경될 수 있습니다. 변경된 사항은 본 페이지에 게시됩니다.</p>
        </section>

        <section>
          <h2>10. 문의</h2>
          <p>개인정보 처리에 관한 문의는 contact@harudang.com으로 연락주시기 바랍니다.</p>
        </section>
      </main>
    </div>
  );
};

export default PrivacyPolicyPage;

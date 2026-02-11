import React from 'react';
import './InfoPage.css';

const TermsOfServicePage = () => {
  return (
    <div className="info-page-container">
      <header className="info-header">
        <h1>이용약관</h1>
        <p>HARU DANG 서비스 이용을 위한 약관입니다.</p>
      </header>
      <main className="info-content">
        <section>
          <h2>1. 서비스 개요</h2>
          <p>HARU DANG은 반려견 양육에 관한 정보, 훈련 가이드, 건강 관리 조언 등을 제공하는 교육 웹사이트입니다.</p>
        </section>

        <section>
          <h2>2. 서비스 이용 약관</h2>
          <p>사용자는 다음 조건에 동의함으로써 본 서비스를 이용할 수 있습니다:</p>
          <ul>
            <li>본 약관의 모든 조항을 준수합니다.</li>
            <li>법적 연령(만 18세 이상)에 도달했습니다.</li>
            <li>서비스 이용을 위해 제공한 정보는 정확하고 최신상태입니다.</li>
            <li>타인의 정보를 도용하여 이용하지 않습니다.</li>
          </ul>
        </section>

        <section>
          <h2>3. 사용자의 책임</h2>
          <p>사용자는 다음과 같은 행동을 하면 안 됩니다:</p>
          <ul>
            <li>불법적인 콘텐츠 배포 또는 접근</li>
            <li>해킹, 바이러스, 악성코드 배포</li>
            <li>스팸, 광고, 폭력적 콘텐츠 전송</li>
            <li>타인의 지적재산권 침해</li>
            <li>시스템에 과도한 부하 유발</li>
          </ul>
        </section>

        <section>
          <h2>4. 콘텐츠 사용권</h2>
          <p>HARU DANG이 제공하는 모든 콘텐츠는 저작권으로 보호됩니다. 사용자는 개인적, 비상업적 목적으로만 사용할 수 있습니다.</p>
        </section>

        <section>
          <h2>5. 서비스 부인</h2>
          <p>HARU DANG은 다음 사항에 대해 책임을 지지 않습니다:</p>
          <ul>
            <li>서비스의 중단 또는 오류</li>
            <li>사용자가 입은 직간접적 손해</li>
            <li>제3자 콘텐츠의 정확성</li>
            <li>서비스에 포함된 정보의 의료적 효과</li>
          </ul>
        </section>

        <section>
          <h2>6. 의료 및 법률 면책</h2>
          <p>HARU DANG의 모든 정보는 교육 목적으로만 제공됩니다. 반려견의 건강 문제는 반드시 수의사와 상담하세요. 법적 문제는 변호사와 상담하시기 바랍니다.</p>
        </section>

        <section>
          <h2>7. 광고</h2>
          <p>HARU DANG은 Google AdSense를 통해 광고를 표시할 수 있습니다. 광고 클릭이나 제품 구매는 사용자의 선택이며, 광고주에 대한 책임은 HARU DANG에 있지 않습니다.</p>
        </section>

        <section>
          <h2>8. 약관 변경</h2>
          <p>HARU DANG은 이 약관을 언제든지 변경할 수 있습니다. 변경된 약관은 즉시 적용되며, 계속 서비스를 이용하면 변경된 약관을 수락한 것으로 간주합니다.</p>
        </section>

        <section>
          <h2>9. 계정 정지 및 삭제</h2>
          <p>HARU DANG은 약관 위반, 부정행위, 기술적 이유 등으로 사용자 계정을 일시 정지하거나 삭제할 수 있습니다.</p>
        </section>

        <section>
          <h2>10. 준거법 및 분쟁 해결</h2>
          <p>본 약관은 대한민국 법률에 따라 해석됩니다. 분쟁은 협의로 해결하며, 필요시 대한민국 법원의 관할을 받습니다.</p>
        </section>
      </main>
    </div>
  );
};

export default TermsOfServicePage;

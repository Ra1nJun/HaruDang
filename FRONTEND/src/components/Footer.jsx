import React from 'react';
import { Link } from 'react-router-dom';
import './Footer.css';

const Footer = () => {
  const currentYear = new Date().getFullYear();

  return (
    <footer className="footer">
      <div className="footer-container">
        {/* 회사 정보 */}
        <div className="footer-section">
          <h3>HARU DANG</h3>
          <strong>자료 참고:</strong>
          <a href="https://apms.epis.or.kr/home/kor/main.do" target="_blank" rel="noopener noreferrer">동물사랑배움터</a>
          <a href="https://www.animal.go.kr/front/index.do" target="_blank" rel="noopener noreferrer">국가동물보호정보시스템</a>
          <a href="https://aihub.or.kr/aihubdata/data/view.do?currMenu=115&topMenu=100&aihubDataSe=data&dataSetSn=71879" target="_blank" rel="noopener noreferrer">AI Hub</a>
        </div>

        {/* 가이드 링크 */}
        <div className="footer-section">
          <h4>가이드</h4>
          <ul>
            <li><Link to="/pre-adoption">입양 전</Link></li>
            <li><Link to="/first-2-months">초기 2개월</Link></li>
            <li><Link to="/2-4-months">사회화</Link></li>
            <li><Link to="/training-guide">훈련 가이드</Link></li>
            <li><Link to="/petiquette">펫티켓</Link></li>
            <li><Link to="/checklist">체크리스트</Link></li>
            <li><Link to="/chat-ai">AI 챗봇</Link></li>
            <li><Link to="/about">소개</Link></li>
          </ul>
        </div>

        {/* 정책 및 정보 */}
        <div className="footer-section">
          <h4>정책</h4>
          <ul>
            <li><Link to="/privacy">개인정보 처리방침</Link></li>
            <li><Link to="/terms">이용약관</Link></li>
            <li><Link to="/contact">문의</Link></li>
          </ul>
        </div>
      </div>

      {/* 저작권 및 하단 정보 */}
      <div className="footer-bottom">
        <div className="footer-copyright">
          <p>&copy; {currentYear} HARU DANG. All rights reserved.</p>
        </div>
        <div className="footer-disclaimer">
          <p>
            HARU DANG의 모든 정보는 교육 목적으로만 제공됩니다. 
            반려견의 건강 문제는 반드시 수의사와 상담하세요.
          </p>
        </div>
      </div>
    </footer>
  );
};

export default Footer;

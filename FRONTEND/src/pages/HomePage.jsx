import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import './InfoPage.css';
import SplitText from '../components/SplitText';

const HomePage = () => {
  const [isVisible, setIsVisible] = useState(true);

  useEffect(() => {
    const handleScroll = () => {
      // 스크롤이 50px 이상 내려가면 사라지게 설정
      if (window.scrollY > 50) {
        setIsVisible(false);
      } else {
        setIsVisible(true);
      }
    };

    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);
  return (
    <div className="home-page-wrapper">
      <section className="hero-section">
        <SplitText
          text="Welcome to Haru Dang"
          className="hero-text"
          delay={50}
          duration={0.8}
          ease="power3.out"
          splitType="chars"
          from={{ opacity: 0, y: 40 }}
          to={{ opacity: 1, y: 0 }}
          threshold={0.1}
          rootMargin="-100px"
          textAlign="center"
        />
        <div className={`scroll-indicator ${!isVisible ? 'hidden' : ''}`}>
          Scroll Down ↓
        </div>
      </section>
      <div className="info-page-container">
        <header className="info-header">
          <h1>Haru Dang : 반려견과 행복한 하루하루</h1>
          <p>반려견을 기르는데 필요한 것을 알려드릴게요.</p>
        </header>
        <main className="info-content">
          <section>
            <h2>어서오세요!</h2>
            <p>반려가구가 꾸준히 증가하고 반려 동물에 대한 관심이 커지는 만큼 체계적인 양육 가이드가 필요하다고 생각했습니다.</p>
            <p>그래서 <b>“처음 키우는 사람도 쉽게 따라갈 수 있는 간단한 로드맵”</b> 웹페이지를 만들었습니다.</p>
          </section>
          <section>
            <h2>이곳엔 무엇이 있나요?</h2>
            <p>저희 가이드를 통해 반려견의 생애 각 단계에 대해 알아볼 수 있습니다:</p>
            <ul>
              <li><Link to="/checklist"><b>↗︎체크리스트:</b></Link> 필요한 모든 사항을 빠르게 확인할 수 있는 간편 참고 자료입니다.</li>
              <li><Link to="/pre-adoption"><b>↗︎입양 전:</b></Link> 강아지를 기르기 위해서 어떤 준비가 필요할까요?</li>
              <li><Link to="/first-2-months"><b>↗︎초기 2개월:</b></Link> 강아지의 나이가 생후 2개월 이하이거나, 강아지를 처음 키우는 경우에는 이곳이 도움이 될 것입니다.</li>
              <li><Link to="/2-4-months"><b>↗︎사회화:</b></Link> 강아지의 나이가 생후 2-4개월이 적합하지만, 그렇지 않더라도 강아지의 사회화에 도움이 될 수 있습니다.</li>
              <li><Link to="/training-guide"><b>↗︎훈련 가이드:</b></Link> 좀 더 세분화된 훈련 방법을 배워보세요.</li>
              <li><Link to="/petiquette"><b>↗︎펫티켓:</b></Link> 공공장소에서 반려견과 같이 할 때 지켜야 할 기본적인 에티켓을 배워보세요.</li>
            </ul>
          </section>
          <section>
            <h2>자료 참고</h2>
            <p>저희 가이드는 아래의 자료를 참고하여 제작되었습니다:</p>
            <ul>
              <li><a href="https://apms.epis.or.kr/home/kor/main.do" target="_blank" rel="noopener noreferrer">↗︎동물사랑배움터</a></li>
              <li><a href="https://www.animal.go.kr/front/index.do" target="_blank" rel="noopener noreferrer">↗︎국가동물보호정보시스템</a></li>
              <li><a href="https://aihub.or.kr/aihubdata/data/view.do?currMenu=115&topMenu=100&aihubDataSe=data&dataSetSn=71879" target="_blank" rel="noopener noreferrer">↗︎AI Hub</a> (AI)</li>
            </ul>
          </section>
        </main>
      </div>
    </div>
  );
};

export default HomePage;
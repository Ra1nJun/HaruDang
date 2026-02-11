import './AboutPage.css'
import { HiDocumentSearch } from "react-icons/hi";
import { SiJfrogpipelines, SiDatadog } from "react-icons/si";
import { Link } from 'react-router-dom';

const AboutPage = () => {
    return(
        <div className="info-page-container">
            <header className="info-header">
                <h1>Haru Dang에 대해</h1>
                <p>반려견 양육 가이드 사이트 소개</p>
            </header>
            <main className="info-content">
                <div className="cards">
                    <div className="card" style={{background: '#D9874D'}}>
                        <div className="flex-container">
                            <div className="text-content">
                                <p className="tip">HARU DANG 소개</p>
                                <p className="second-text">
                                    HARU DANG은 반려견 양육에 필요한 정보를 제공하는 종합 가이드 사이트입니다.<br />
                                    입양 전부터 성견까지 각 단계별 전문 정보, 훈련 방법, 건강관리, 사회화, 펫티켓 등을 다룹니다.<br />
                                    우리의 목표는 반려견 주인들이 전문적이고 신뢰할 수 있는 정보에 접근하여 <br />
                                    반려견과 함께 행복한 삶을 만드는 것입니다.
                                </p>
                            </div>
                            <div className="media-content">
                                <SiDatadog className='icon'/>
                            </div>
                        </div>
                    </div>
                    <div className="card" style={{background: '#D07135'}}>
                        <div className="flex-container">
                            <div className="text-content">
                                <p className="tip">AI 개요</p>
                                <p className="second-text">
                                    이 웹페이지의 AI는 RAG(검색 증강 생성) 시스템으로, <br />
                                    단순히 저장된 답이 아닌 실제 지식문서 기반 답변을 생성합니다.<br />
                                    데이터는 AI HUB의 <a className='about-page-link' href='https://www.aihub.or.kr/aihubdata/data/view.do?currMenu=115&topMenu=100&aihubDataSe=data&dataSetSn=71879' target="_blank" rel="noopener noreferrer">↗︎반려견 성장 및 질병관련 말뭉치 데이터</a>를 사용했습니다.<br />
                                    해당 데이터는 강아지나 소형 동물에 대한 논문, 서적, 수의사의 답변으로 구성되었습니다.
                                </p>
                            </div>
                            <div className="media-content">
                                <HiDocumentSearch className='icon'/>
                            </div>
                        </div>
                    </div>
                    <div className="card" style={{background: '#C85A19'}}>
                        <div className="flex-container">
                            <div className="text-content">
                                <p className="tip">이용약관 및 개인정보보호</p>
                                <p className="second-text">
                                    HARU DANG은 사용자의 개인정보를 보호하고 투명한 서비스 운영을 약속합니다.<br />
                                    <Link to="/privacy" className='about-page-link'>↗︎개인정보 처리방침</Link> - 개인정보 수집, 이용, 보호에 대한 정책<br />
                                    <Link to="/terms" className='about-page-link'>↗︎이용약관</Link> - 서비스 이용 시 준수해야 할 약관<br />
                                    문의는 <Link to="/contact" className='about-page-link'>↗︎문의페이지</Link>를 이용해 주세요.
                                </p>
                            </div>
                            <div className="media-content">
                                <SiJfrogpipelines className='icon'/>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    );
};

export default AboutPage

import React, { useState } from 'react';
import './InfoPage.css';
import './ChecklistPage.css';

const ChecklistPage = () => {
  const [checklist, setChecklist] = useState({
    preadoption: [
      { id: 1, label: '가족 전체 동의 및 15년 책임 능력 확인', checked: false },
      { id: 2, label: '월 평균 양육비 및 초기 준비 비용 계산', checked: false },
      { id: 3, label: '거주지 반려동물 허용 여부 확인', checked: false },
      { id: 4, label: '가족 모두 알레르기 유무 확인', checked: false },
      { id: 5, label: '견종별 특성 및 에너지 수준 이해', checked: false },
      { id: 6, label: '합법적의 입양 경로 선택 (보호시설, 브리더 등)', checked: false },
      { id: 7, label: '기본 준비물 구입 (크레이트, 사료, 장난감, 산책도구)', checked: false },
      { id: 8, label: '동물등록 절차 및 법적 의무 이해', checked: false },
    ],
    firstTwoMonths: [
      { id: 1, label: '생후 2개월 미만은 어미와 함께 두어 모유 수유 권장 혹은 수의사 상담', checked: false },
      { id: 2, label: '생후 2개월 이상이라면 반려 동물 등록', checked: false },
      { id: 3, label: '예방접종 일정은 수의사와 상담하여 계획', checked: false },
      { id: 4, label: '기생충 예방 및 정기 건강검진 시작 계획', checked: false },
      { id: 5, label: '새로운 곳에 적응할 수 있도록 안정적인 환경 제공', checked: false },
      { id: 6, label: '짧고 잦은 상호작용으로 신뢰 형성, 점진적으로 혼자 있는 시간 늘리기', checked: false },
      { id: 7, label: '강아지가 장소를 구분할 수 있도록 배변 및 크레이트 교육', checked: false }
    ],
    socialization: [
      { id: 1, label: '1차 예방접종 이후 외부 세계 경험', checked: false },
      { id: 2, label: '다양한 사람, 견종, 소리, 장소에 노출', checked: false },
      { id: 3, label: '기다려 교육 (거리, 시간, 방해요소 점진적 확대)', checked: false },
      { id: 4, label: '매일 산책 (짧아도 꾸준히)', checked: false },
      { id: 5, label: '산책 중 리드 사용 및 핫피킹 지키기', checked: false },
      { id: 6, label: '이갈이 시기 입질 장난감으로 해소', checked: false },
      { id: 7, label: '과도한 짖음 관리 (원인 파악 및 둔감화 훈련)', checked: false },
      { id: 8, label: '분리불안 예방 (외출 루틴 및 간식 장난감)', checked: false },
      { id: 9, label: '월령에 맞는 사료 선택 및 제한급식 기본화', checked: false },
      { id: 10, label: '목욕, 발톱 관리, 항문낭 등등 그루밍 루틴 확립', checked: false },
      { id: 11, label: '수의사와 중성화수술 시기 상담', checked: false }
    ],
    training: [
      { id: 1, label: '리더 워킹, 노즈 워크 등등 산책 훈련 마스터', checked: false },
      { id: 2, label: '앉아, 기다려 등등 기초 훈련 마스터', checked: false },
      { id: 3, label: '배변, 짖음 등등 문제 행동 훈련 마스터', checked: false },
      { id: 4, label: '생후 100일 이전 사회화', checked: false },
      { id: 5, label: '생후 100일 이후 사회화', checked: false }
    ],
    petiquette: [
      { id: 1, label: '산책 중 배변봉투 항상 지참 및 치우기', checked: false },
      { id: 2, label: '인식표 착용 (연락처 포함)', checked: false },
      { id: 3, label: '산책 시 항상 목줄 사용', checked: false },
      { id: 4, label: '적당한 거리 유지하며 보행', checked: false },
      { id: 5, label: '출입구 앞에서 앉기/기다려 시키기', checked: false },
      { id: 6, label: '신체 전체 터치에 대한 편안함 훈련', checked: false },
      { id: 7, label: '일관된 호출 신호 정해서 사용하고 불리면 즉시 돌아오기 훈련', checked: false }
    ],
  });

  const handleCheck = (category, id) => {
    setChecklist(prev => ({
      ...prev,
      [category]: prev[category].map(item =>
        item.id === id ? { ...item, checked: !item.checked } : item
      )
    }));
  };

  const ChecklistSection = ({ title, category, items }) => (
    <section>
      <h2>{title}</h2>
      <div className="checklist-items">
        {items.map(item => (
          <div key={item.id} className="checklist-item">
            <label className="container">
              <input
                type="checkbox"
                checked={item.checked}
                onChange={() => handleCheck(category, item.id)}
              />
              <div className="checkmark"></div>
              <span className="checkbox-label">{item.label}</span>
            </label>
          </div>
        ))}
      </div>
    </section>
  );

  return (
    <div className="info-page-container">
      <header className="info-header">
        <h1>반려견 양육 체크리스트</h1>
        <p>각 단계별로 확인해야 할 항목들을 체크해 보세요.</p>
      </header>
      <main className="info-content">
        <ChecklistSection
          title="입양 전 준비물 및 책임"
          category="preadoption"
          items={checklist.preadoption}
        />
        <ChecklistSection
          title="초기 2개월"
          category="firstTwoMonths"
          items={checklist.firstTwoMonths}
        />
        <ChecklistSection
          title="사회화"
          category="socialization"
          items={checklist.socialization}
        />
        <ChecklistSection
          title="기본 훈련"
          category="training"
          items={checklist.training}
        />
        <ChecklistSection
          title="펫티켓"
          category="petiquette"
          items={checklist.petiquette}
        />
      </main>
    </div>
  );
};

export default ChecklistPage;

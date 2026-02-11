import React, { useRef, useEffect } from 'react';
import { useForm, ValidationError } from '@formspree/react';
import './InfoPage.css';
import { Link } from 'react-router-dom';
import Toast from '../components/Toast';

const ContactPage = () => {
  const [state, handleSubmit] = useForm(import.meta.env.VITE_FORMSPREE_ID);
  const formRef = useRef(null);
  const [toastMessage, setToastMessage] = React.useState('');
  const [toastType, setToastType] = React.useState('success');

  useEffect(() => {
    if (state.succeeded) {
      setToastMessage('문의가 성공적으로 전송되었습니다. 감사합니다!');
      setToastType('success');
      formRef.current.reset();
      // Reset succeeded state after showing toast
      setTimeout(() => {
        setToastMessage('');
      }, 3000);
    }
  }, [state.succeeded]);

  return (
    <div className="info-page-container">
      {toastMessage && <Toast message={toastMessage} type={toastType} />}
      <header className="info-header">
        <h1>문의하기</h1>
        <p>HARU DANG에 대한 의견과 제안을 환영합니다.</p>
      </header>
      <main className="info-content">
        <section>
          <h2>문의 방법</h2>
          <p>웹사이트에 대한 질문, 피드백, 제안이 있으신가요? 아래 폼을 통해 연락주세요. <br />
          저희는 지속적으로 콘텐츠를 개선하고 반려견 주인들을 위한 최고의 자료를 제공하기 위해 노력하고 있습니다.</p>
          
          <form ref={formRef} onSubmit={handleSubmit} className="contact-form">
            <div className="form-group">
              <label htmlFor="email">이메일</label>
              <input
                id="email"
                type="email" 
                name="email"
                required
              />
              <ValidationError prefix="Email" field="email" errors={state.errors} />
            </div>
            <div className="form-group">
              <label htmlFor="message">메시지</label>
              <textarea
                id="message"
                name="message"
                rows="5"
                required
              />
              <ValidationError prefix="Message" field="message" errors={state.errors} />
            </div>
            <button type="submit" disabled={state.submitting} className="submit-btn">
              {state.submitting ? '전송 중...' : '문의 보내기'}
            </button>
          </form>
        </section>

        <section>
          <h2>중요한 알림</h2>
          <p><strong>의료 상담 불가:</strong> HARU DANG은 의료 전문 사이트가 아닙니다. 반려견의 건강 문제나 질병 치료에 관한 조언은 제공할 수 없습니다. <br />
          반려견이 의료상 응급 상황에 처해 있다면 즉시 지역 수의사나 응급 동물병원에 연락하세요.</p>

          <p><strong>법적 면책:</strong> HARU DANG에 제공된 모든 정보는 교육 목적으로만 사용됩니다. 특정 상황에 대한 법적 조언이 필요하신 경우 변호사와 상담하세요.</p>
        </section>

        <section>
          <h2>개인정보 보호</h2>
          <p>문의 시 제공되는 개인정보는 <Link to="/privacy">개인정보 처리방침</Link>에 따라 보호됩니다.</p>
        </section>
      </main>
    </div>
  );
};

export default ContactPage;

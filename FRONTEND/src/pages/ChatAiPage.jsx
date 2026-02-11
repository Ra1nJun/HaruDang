import { useState, useRef, useEffect, lazy, Suspense } from "react";
import AnimatedList from "../components/AnimatedList";
import "./ChatAiPage.css";
import { IoMdSend } from "react-icons/io";
import { chat } from "../api/chatApi";

const DisclaimerPopup = lazy(() => import("../components/DisclaimerPopup"));

const ChatAiPage = () => {
  const [messages, setMessages] = useState([]);
  const [inputValue, setInputValue] = useState("");
  const [loading, setLoading] = useState(false);
  const [showPopup, setShowPopup] = useState(true);
  const chatEndRef = useRef(null);

  useEffect(() => {
    setMessages([
      {
        id: Date.now(),
        text: "안녕하세요! 반려견을 기르는데 있어 궁금한 것이 있다면 언제든지 물어보세요.",
        sender: "bot",
      },
    ]);
  }, []);

  useEffect(() => {
    if (chatEndRef.current) {
      setTimeout(() => {
        chatEndRef.current?.scrollIntoView({ behavior: "smooth", block: "end" });
      }, 100);
    }
  }, [messages]);

  const sendMessageToBackend = async (text) => {
    setLoading(true);
    try {
      const res = await chat(text);
      const botMessage = {
        id: Date.now() + 1,
        text: res.data.answer || "답변을 받지 못 했어요.",
        sender: "bot",
      };
      setMessages((prev) => [...prev, botMessage]);
    } catch (err) {
      console.error(err);
      setMessages((prev) => [
        ...prev,
        { id: Date.now() + 2, text: "서버의 문제로 답변을 불러오지 못 했어요.", sender: "bot" },
      ]);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async () => {
    if (loading) return;

    const text = inputValue.trim();
    if (!text) return;

    const userMessage = { id: Date.now(), text, sender: "user" };
    setMessages((prev) => [...prev, userMessage]);
    setInputValue("");
    await sendMessageToBackend(text);
  };

  return (
    <div className="chat-container">
      {showPopup && (
        <Suspense fallback={<div>Loading...</div>}>
          <DisclaimerPopup
            onClose={() => setShowPopup(false)}
            content={[
              "본 서비스의 AI 상담은 반려견의 건강·성장에 대한 일반적인 정보 제공을 목적으로 하며, 수의사의 진료·진단·치료를 대체하지 않습니다.",
              "실제 건강 상태의 평가는 반드시 수의사와의 직접 진료를 통해 이루어져야 합니다.",
              "본 서비스에서 제공되는 모든 답변은 참고용 정보일 뿐이며, 이를 바탕으로 한 보호자님의 의사결정과 그 결과에 대한 책임은 전적으로 보호자님께 있습니다.",
              "서비스 운영자는 제공 정보의 정확성·완전성을 보장하지 않으며, 이 정보를 이용함으로써 발생하는 직접·간접적인 손해에 대해 책임을 지지 않습니다.",
              "호흡 곤란, 지속적인 구토·설사, 경련, 의식 저하, 심한 통증 등 응급이 의심되는 증상이 보일 경우, 이 채팅을 이용하지 마시고 즉시 가까운 24시간 동물병원 또는 응급 진료 기관에 내원하시기 바랍니다.",
              "이 채팅은 AI 모델이 학습된 데이터를 기반으로 자동 생성한 답변을 제공하므로, 사실과 다르거나 최신 수의학 지식과 일치하지 않는 내용이 포함될 수 있습니다.",
              "서비스 품질 개선을 위해 상담 내용 일부가 저장·분석될 수 있으며, 주민등록번호, 상세 주소, 연락처 등 민감한 개인정보의 입력은 피해 주시기 바랍니다."
            ]}
          />
        </Suspense>
      )}
      <div className="chat-window">
        <AnimatedList messages={messages} />
        {loading && (
          <div className="loading">
            <span>응답 생성 중...</span>
            <div className="dot-spinner">
              {[...Array(8)].map((_, i) => (
                <div key={i} className="dot-spinner__dot"></div>
              ))}
            </div>
          </div>
        )}
        <div ref={chatEndRef} />
      </div>

      <div className="chat-input-area">
        <textarea
          className="chat-input"
          value={inputValue}
          onChange={(e) => setInputValue(e.target.value)}
          placeholder="이곳에 질문을 입력하세요."
          rows={1}
          onKeyDown={(e) => {
            if (e.key === "Enter" && !e.shiftKey) {
              e.preventDefault();
              handleSubmit();
            }
          }}
          disabled={loading}
        />
        <IoMdSend className="send-btn" onClick={handleSubmit} disabled={loading} />
      </div>
      <p className="chat-disclaimer">이 채팅은 AI 기반 참고용 정보로, 수의사 진단·치료를 대체하지 않습니다. 긴급 상황에서는 즉시 가까운 동물병원에 내원해 주세요. <br />
      현재 이 AI 기능은 개발 중으로 토큰이 다하여 사용이 제한될 수 있습니다.</p>
    </div>
  );
};

export default ChatAiPage;
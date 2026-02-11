import React, { useState, useEffect } from 'react';
import './DisclaimerPopup.css';

const DisclaimerPopup = ({ onClose, content }) => {
  const [showToday, setShowToday] = useState(true);

  useEffect(() => {
    const lastShown = localStorage.getItem('disclaimerLastShown');
    const today = new Date().toDateString();
    if (lastShown === today) {
      setShowToday(false);
    }
  }, []);

  const handleClose = () => {
    onClose();
  };

  const handleDontShowToday = () => {
    const today = new Date().toDateString();
    localStorage.setItem('disclaimerLastShown', today);
    onClose();
  };

  if (!showToday) return null;

  return (
    <div className="disclaimer-popup-overlay">
      <div className="disclaimer-popup">
        <div className="popup-header">
          <b>알림</b>
          <button className="close-btn" onClick={handleClose}>×</button>
        </div>
        <div className="popup-content">
          {content.map((paragraph, index) => (
            <p key={index}>{paragraph}</p>
          ))}
          <button className="dont-show-btn" onClick={handleDontShowToday}>오늘 하루 열지 않기</button>
        </div>
      </div>
    </div>
  );
};

export default DisclaimerPopup;
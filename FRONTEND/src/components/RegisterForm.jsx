import './RegisterForm.css';
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { register } from "../api/userApi";
import { useToast } from '../context/ToastContext';

const RegisterForm = () => {
  const { showToast } = useToast();
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    nickname: "",
    email: "",
    password: "",
    confirm_password: "",
  });
  const [passwordError, setPasswordError] = useState("");
  const [emailError, setEmailError] = useState(""); // New state for email error

  const validatePassword = (password) => {
    if (!password) {
      return "";
    }
    const regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/;
    if (!regex.test(password)) {
      return "비밀번호는 최소 8자 이상, 영문과 숫자 그리고 특수문자를 각각 하나씩 포함해야 합니다.";
    }
    return "";
  };

  const validateEmail = (email) => {
    if (!email) {
      return "";
    }
    // Basic email regex for format validation
    const regex = /^\S+@\S+\.\S+$/;
    if (!regex.test(email)) {
      return "이메일을 입력해 주세요.";
    }
    return "";
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
    
    if (name === "password") {
      setPasswordError(validatePassword(value));
    } else if (name === "email") {
      setEmailError(validateEmail(value));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const passwordValidationError = validatePassword(formData.password);
    if (passwordValidationError) {
      setPasswordError(passwordValidationError);
      return;
    }

    const emailValidationError = validateEmail(formData.email);
    if (emailValidationError) {
      setEmailError(emailValidationError);
      return;
    }

    if (formData.password !== formData.confirm_password) {
      showToast("비밀번호가 일치하지 않습니다.", "warning");
      return;
    }

    try {
      await register(
        formData.nickname,
        formData.email,
        formData.password
      );

      showToast("회원가입 성공!", "success");
      navigate("/login");
    } catch (error) {
      console.error(error);
      let errorMessage = "회원가입 실패";
      if (error.response && error.response.data && error.response.data.message) {
        errorMessage = error.response.data.message;
      } else if (error.message) {
        errorMessage = error.message;
      }
      showToast(errorMessage, "error");
    }
  };

  return (
    <div className="register-form-container">
      <div className="hader">
        <span>Create an account</span>
      </div>

      <form className="register" onSubmit={handleSubmit}>
        <label>Name</label>
        <input
          type="text"
          name="nickname"
          placeholder="이름을 입력하세요."
          value={formData.nickname}
          onChange={handleChange}
          required
        />

        <label>Email</label>
        <input
          type="email"
          name="email"
          placeholder="이메일을 입력하세요."
          value={formData.email}
          onChange={handleChange}
          required
        />
        {emailError && <p className="password-error">{emailError}</p>} {/* Re-using password-error class for now */}

        <label>Password</label>
        <input
          type="password"
          name="password"
          placeholder="비밀번호를 입력하세요."
          value={formData.password}
          onChange={handleChange}
          required
        />
        {passwordError && <p className="password-error">{passwordError}</p>}

        <label>Confirm Password</label>
        <input
          type="password"
          name="confirm_password"
          placeholder="비밀번호를 재입력하세요."
          value={formData.confirm_password}
          onChange={handleChange}
          required
        />

        <button type="submit">회원가입</button>

        <span>
          계정이 있으신가요?
          <Link to="/login"> 로그인</Link>
        </span>
      </form>
    </div>
  );
};

export default RegisterForm;

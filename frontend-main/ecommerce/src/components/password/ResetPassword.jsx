import React from "react";
import fg from "../../assets/images/resetpassword.jpg";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faLock } from "@fortawesome/free-solid-svg-icons";
import { useState } from "react";
import "./ResetPassword.css";
import Navbar from "../header/Navbar";
import Header from "../header/Header";
import Footer from "../footer/Footer";
import axios from "axios";
import { Link, useLocation } from "react-router-dom";
import PasswordStrengthBar from "react-password-strength-bar";
import PasswordChecklist from "react-password-checklist";
import { FaEye, FaEyeSlash } from "react-icons/fa";
import {
  SIGNUP_HANDLESUBMIT_PASSWORD,
  SIGNUP_HANDLESUBMIT_EMPTY_PASSWORD,
  SIGNUP_HANDLESUBMIT_NOSPECIALCHAR_PASSWORD,
  SIGNUP_HANDLESUBMIT_CONFIRM_PASSWORD,
  SIGNUP_HANDLESUBMIT_NOUPPERCASE_PASSWORD,
} from "../../globalData/constant.js";

function ResetPassword() {
  const [formData, setFormData] = useState({
    password: "",
    confirmPassword: "",
  });

  const [showPassword, setShowPassword] = useState(false);
  const [errors, setErrors] = useState({});
  const [enableStrength, setEnableStrength] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState(false);
  const [expire, setExpire] = useState(false);

  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const token = queryParams.get("token");

  const toggleShowPassword = () => {
    setShowPassword(!showPassword);
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const validate = () => {
    const errors = {};
    if (!formData.password) {
      errors.password = SIGNUP_HANDLESUBMIT_EMPTY_PASSWORD;
    } else if (formData.password.length < 8) {
      errors.password = SIGNUP_HANDLESUBMIT_PASSWORD;
    } else if (!/[A-Z]/.test(formData.password)) {
      errors.password = SIGNUP_HANDLESUBMIT_NOUPPERCASE_PASSWORD;
    } else if (!/[!@#$%^&*(),.?":{}|<>]/.test(formData.password)) {
      errors.password = SIGNUP_HANDLESUBMIT_NOSPECIALCHAR_PASSWORD;
    } else if (!/[0-9]/.test(formData.password)) {
      errors.password = SIGNUP_HANDLESUBMIT_NOSPECIALCHAR_PASSWORD;
    } else if (formData.password !== formData.confirmPassword) {
      errors.confirmPassword = SIGNUP_HANDLESUBMIT_CONFIRM_PASSWORD;
    }
    return errors;
  };

  const onSubmit = async (e) => {
    e.preventDefault();
    const validationErrors = validate();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    try {
      const response = await axios.post(
        "http://localhost:8072/user/reset-password",
        {
          token: token,
          newPassword: formData.password,
        }
      );

      // //console.log("response status:" + response.status);

      if (response.data.status === "success") {
        setSuccess(response.data.message);
      } else {
        setError(response.data.message);
      }
    } catch (error) {
      //console.log("internal server error", error);
      setError(" Reset link has expired. Please request a new password reset.");
    }
  };

  return (
    <>
      <Header />
      <Navbar />
      <div className="reset_password_container">
        <img src={fg} alt="Forgot Password" />
        <form onSubmit={onSubmit} className="sign-in-form forms-fg">
          <h3 className="title_rp">Reset Password</h3>
          <div className="password_rp">
            <FontAwesomeIcon icon={faLock} className="my-auto mx-auto" />
            <input
              type={showPassword ? "text" : "password"}
              name="password"
              placeholder="Enter New Password"
              value={formData.password}
              onChange={handleChange}
              onFocus={(e) => setEnableStrength(true)}
              required
            />
            <span
              onClick={toggleShowPassword}
              className="position-absolute top-50 end-0 translate-middle-y px-2"
              style={{ cursor: "pointer" }}
            >
              {showPassword ? <FaEyeSlash /> : <FaEye />}
            </span>
          </div>
          {enableStrength && (
            <PasswordStrengthBar
              password={formData.password}
              className="passwordBar"
            />
          )}

          {enableStrength && (
            <PasswordChecklist
              rules={["minLength", "specialChar", "number", "capital", "match"]}
              minLength={8}
              value={formData.password}
              valueAgain={formData.confirmPassword}
            />
          )}

          <div className="password_rp">
            <FontAwesomeIcon icon={faLock} className="my-auto mx-auto" />
            <input
              type={showPassword ? "text" : "password"}
              name="confirmPassword"
              placeholder="Confirm Password"
              value={formData.confirmPassword}
              onChange={handleChange}
              required
            />
            <span
              onClick={toggleShowPassword}
              className="position-absolute top-50 end-0 translate-middle-y px-2"
              style={{ cursor: "pointer" }}
            >
              {showPassword ? <FaEyeSlash /> : <FaEye />}
            </span>
          </div>

          <button type="submit" className="rp_btn">
            Reset Password
          </button>
          {success && <div className="alert alert-success mt-3">{success}</div>}
          {error && <div className="alert alert-danger mt-3">{error}</div>}
        </form>
      </div>
      <Footer />
    </>
  );
}
export default ResetPassword;

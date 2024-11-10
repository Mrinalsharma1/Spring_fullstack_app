import React from "react";
import fg from "../../assets/images/forgot_password.jpg";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faUser } from "@fortawesome/free-solid-svg-icons";
import { useState } from "react";
import "./ForgotPassword.css";
import Navbar from "../header/Navbar";
import Header from "../header/Header";
import Footer from "../footer/Footer";
import axios from "axios";
import { Link } from "react-router-dom";

import spinner from "../../assets/images/spinner.svg";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

function ForgotPassword() {
  const [formData, setFormData] = useState({
    email: "",
  });
  const [validEmail, setValidEmail] = useState(false);
  const [invalidEmail, setInvalidEmail] = useState(false);
  const [showImg, setShowImg] = useState(false); // Set this to false initially
  const [buttonDisable, setButtonDisable] = useState(false); // Button is enabled initially
  const [error, setError] = useState("");
  const handleChange = (e) => {
    setFormData({
      ...formData,
      email: e.target.value,
    });
  };

  const validateEmail = (email) => {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(String(email).toLowerCase());
  };

  const onSubmit = async (e) => {
    e.preventDefault();
    showMessage();

    if (!validateEmail(formData.email)) {
      setInvalidEmail(true);
      setValidEmail(false);
      return;
    }

    try {
      const response = await axios.post(
        "http://localhost:8072/user/forgot-password",
        null, // No data in the body
        {
          params: {
            email: formData.email,
          },
        }
      );

      //console.log(response);
      setError(response);

      if (response.status === 200) {
        setValidEmail(true);
        setInvalidEmail(false);
      } else {
        setValidEmail(false);
        setInvalidEmail(true);
      }
    } catch (error) {
      console.error("Error sending reset link:", error);
      setValidEmail(false);
      setInvalidEmail(true);
    }
  };

  //timer
  const showMessage = () => {
    setShowImg(true); // Show image after button click
    setButtonDisable(true); // Disable button to prevent multiple clicks
    toast.success("Please wait", { autoClose: 2000 });
    setTimeout(() => {
      setShowImg(false); // Hide image after 3 seconds
      setButtonDisable(false); // Enable button again
    }, 3000);
  };

  return (
    <>
      <Header />
      <Navbar />
      <div className="forgot_password_container">
        <img src={fg} alt="Forgot Password" />
        <form onSubmit={onSubmit} className="sign-in-form forms-fg">
          <h3 className="title_fg">Forgot Password</h3>
          <div className="email_fg">
            <FontAwesomeIcon icon={faUser} className="my-auto mx-auto" />
            <input
              type="email"
              name="email"
              placeholder="Enter Email"
              value={formData.email}
              onChange={handleChange}
              required
              className="form-control"
            />
          </div>

          <button type="submit" className="fg_btn" disabled={buttonDisable}>
            {showImg ? (
              <img src={spinner} alt="spiiner" />
            ) : (
              <p> Send Reset Link</p>
            )}
          </button>

          <Link to="/login" className="fg_login">
            Login
          </Link>
          {validEmail && (
            <div className="alert alert-success mt-3">
              Reset link sent to your email!
            </div>
          )}
          {invalidEmail && (
            <div className="alert alert-danger mt-3">
              Failed to send reset link. Please try again.
            </div>
          )}
        </form>
        <ToastContainer />
      </div>
      <Footer />
    </>
  );
}

export default ForgotPassword;

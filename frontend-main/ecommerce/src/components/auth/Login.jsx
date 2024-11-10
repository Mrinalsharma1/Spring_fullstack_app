import React, { useEffect, useState } from "react";
import { FaEye, FaEyeSlash } from "react-icons/fa";
import axios from "axios";
import { useDispatch } from "react-redux";
import { Link, Router, useNavigate } from "react-router-dom";
import { login } from "../../redux/userSlice.js";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faUser, faLock } from "@fortawesome/free-solid-svg-icons";
import { faGithub, faGoogle } from "@fortawesome/free-brands-svg-icons";
import "./LoginPage.css";
import loginimg from "../../assets/images/loginimg.jpg";
import { LOGIN_HANDLELOGIN_FAILURE } from "../../globalData/constant";
import Navbar from "../header/Navbar";
import Header from "../header/Header";
import { useSelector } from "react-redux";

function Login({ isAuthenticated, setIsAuthenticated }) {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [showPassword, setShowPassword] = useState(false);
  const [loginFailed, setLoginFailed] = useState(false);
  const [isLoggedIn, setisLoggedIn] = useState(false);
  const [success, setSuccess] = useState(false);
  const [message, setMessage] = useState("");
  const url = "http://localhost:8071/login";

  const reduxData = useSelector((state) => state.login);
  const isLoggedInRedux = reduxData.isLoggedIn;

  const [formData, setFormData] = useState({
    username: "",
    password: "",
    rememberMe: false,
  });

  useEffect(() => {
    console.log("isAuthenticated ", isAuthenticated);
    if (isLoggedInRedux) return navigate("/");
    const savedLoginData = localStorage.getItem("loginData");
    if (savedLoginData) {
      setFormData(JSON.parse(savedLoginData));
    }
  }, []);

  const handleChange = (e) => {
    const { name, type, checked, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const toggleShowPassword = () => {
    setShowPassword(!showPassword);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoginFailed(false);

    try {
      const response = await axios.post(url, {
        username: formData.username,
        password: formData.password,
      });
      const users = response.data.userdata;

      setTimeout(() => {
        setSuccess(response.data.message);
      }, 3000);

      setSuccess(false);

      if (users) {
        dispatch(
          login({
            id: users.id,
            name: users.profilename,
            email: users.username,
            role: users.role,
            token: response?.data?.token,
            isLoggedIn: users.status === "success" ? true : false,
          })
        );

        if (formData.rememberMe) {
          localStorage.setItem("loginData", JSON.stringify(formData));
        } else {
          localStorage.removeItem("loginData");
        }
      }
      setIsAuthenticated(true);

      if (users.role === "user") {
        navigate("/");
      } else {
        navigate("/dashboard");
      }
    } catch (error) {
      console.error(
        "Error response:",
        error.response ? error.response.data : error.message
      );
      setTimeout(() => {
        setLoginFailed(false);
      }, 3000);
      setLoginFailed(true);
      setisLoggedIn(false);
    }
  };

  useEffect(() => {
    if (isLoggedIn) {
      handleSubmit();
    }
  }, [isLoggedIn]);

  const onSubmit = (event) => {
    event.preventDefault();
    handleSubmit(event);
  };

  return (
    <>
      <Header />
      <Navbar />
      <div className="mycontainer">
        <img src={loginimg} alt="Login" />
        <form onSubmit={onSubmit} className="sign-in-form forms-mycontainer">
          <h2 className="title">Sign in</h2>
          <div className="input-field">
            <FontAwesomeIcon icon={faUser} className="my-auto mx-auto" />
            <input
              type="text"
              name="username"
              placeholder="Enter Email"
              value={formData.username}
              onChange={handleChange}
              required
            />
          </div>
          <div className="input-field">
            <FontAwesomeIcon icon={faLock} className="my-auto mx-auto" />
            <input
              type={showPassword ? "text" : "password"}
              name="password"
              placeholder="Enter Password"
              value={formData.password}
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
          <div className="fg_container">
            <div className="row">
              <div className="col-md-6">
                <input
                  type="checkbox"
                  name="rememberMe"
                  checked={formData.rememberMe}
                  onChange={handleChange}
                  className="password_checkbox"
                />
                <label>Remember Me</label>
              </div>
              <div className="col-md-6">
                <Link
                  to="/forgotpassword"
                  className="forgot_password"
                  style={{ textDecoration: "none" }}
                >
                  Forgot Password?
                </Link>
              </div>
            </div>
          </div>
          {loginFailed && (
            <div className="alert alert-danger mt-3">
              {LOGIN_HANDLELOGIN_FAILURE}
            </div>
          )}
          <button type="submit" className="mybtn">
            Sign in
          </button>
          {success && <div className="alert alert-success mt-3">{success}</div>}
          {/* <p className="social-text">Or Sign in with social platforms</p>
          <div className="social-media">
            <a href="#" className="social-icon">
              <FontAwesomeIcon icon={faGithub} className="my-auto mx-auto" />
            </a>
            <a href="#" className="social-icon">
              <FontAwesomeIcon icon={faGoogle} className="my-auto mx-auto" />
            </a>
          </div> */}
          <div className="signup_footer pt-3 text-center">
            <p>
              Don't have an account?{" "}
              <Link to="/signup" style={{ textDecoration: "none" }}>
                Sign up
              </Link>
            </p>
          </div>
        </form>
      </div>
    </>
  );
}

export default Login;

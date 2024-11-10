import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { FaEye, FaEyeSlash } from "react-icons/fa";
import {
  SIGNUP_HANDLESUBMIT_PASSWORD,
  SIGNUP_HANDLESUBMIT_ACCOUNT_EXISTS,
  SIGNUP_HANDLESUBMIT_ACCOUNT_SUCCESS,
  SIGNUP_HANDLESUBMIT_ACCOUNT_FAILURE,
  SIGNUP_HANDLESUBMIT_INVALID_EMAIL,
  SIGNUP_HANDLESUBMIT_EMPTY_EMAIL,
  SIGNUP_HANDLESUBMIT_EMPTY_PASSWORD,
  SIGNUP_HANDLESUBMIT_NOSPECIALCHAR_PASSWORD,
  SIGNUP_HANDLESUBMIT_CONFIRM_PASSWORD,
  SIGNUP_HANDLESUBMIT_NOUPPERCASE_PASSWORD,
} from "../../globalData/constant.js";

import PasswordStrengthBar from "react-password-strength-bar";
import PasswordChecklist from "react-password-checklist";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faUser, faLock, faEnvelope } from "@fortawesome/free-solid-svg-icons";
import { faGithub, faGoogle } from "@fortawesome/free-brands-svg-icons";
import { v4 as uuidv4 } from "uuid";
import axios from "axios";
import signupimg from "../../assets/images/signupimg.jpg";
import Header from "../header/Header";
import Navbar from "../header/Navbar.jsx";

const SignUp = () => {
  const [formData, setFormData] = useState({
    profilename: "",
    username: "",
    password: "",
    confirmPassword: "",
  });
  const [showPassword, setShowPassword] = useState(false);
  const [success, setSuccess] = useState("");
  const [errors, setErrors] = useState({});
  const [duplicateEmail, setDuplicateEmail] = useState(false);
  const [enableStrength, setEnableStrength] = useState(false);
  const [signUpButton, setSignUpButton] = useState(false);

  const toggleShowPassword = () => {
    setShowPassword(!showPassword);
  };

  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
    if (
      formData.name &&
      formData.email &&
      formData.password &&
      formData.confirmPassword
    ) {
      setSignUpButton(true);
    }
  };

  // function to validate password
  const validate = () => {
    const errors = {};

    if (!formData.email) {
      errors.email = SIGNUP_HANDLESUBMIT_EMPTY_EMAIL;
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      errors.email = SIGNUP_HANDLESUBMIT_INVALID_EMAIL;
    }
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

  // function to execute signup
  // const handleSubmit = async (e) => {
  //   e.preventDefault();

  //   const validationErrors = validate();
  //   setErrors(validationErrors);

  //   const url = "http://localhost:8072/user/register";
  //   const newUser = {
  //     profilename: formData.name,
  //     username: formData.email,
  //     password: formData.password,
  //     role: "user",
  //   };

  //   axios
  //     .post(url, newUser)
  //     .then((response) => {

  //       setSuccess(response.data.message);
  //     })
  //     .catch((error) => {
  //       console.error("There was an error registering the user:", error);
  //     });

  //   try {
  //     if (Object.keys(validationErrors).length === 0) {
  //       const response = await axios.get(url);
  //       const users = response.data;

  //       const emailExists = users.some(
  //         (user) => user.email.toLowerCase() === formData.email.toLowerCase()
  //       );

  //       if (emailExists) {
  //         setDuplicateEmail(true);
  //         setSuccess(false);
  //       } else {
  //         await axios.post(url, newUser);
  //         setSuccess(true);
  //         setDuplicateEmail(false);
  //         setFormData({
  //           profilename: "",
  //           username: "",
  //           password: "",
  //           confirmPassword: "",
  //         })
  //         setTimeout(setSuccess(false), 3000);
  //       }
  //     } else {
  //       console.error("The response does not contain 'user' property.");
  //     }
  //   } catch (error) {
  //     //console.log("There was an error saving the user!", error);
  //     //console.log(SIGNUP_HANDLESUBMIT_ACCOUNT_FAILURE);
  //   }
  // };

  const handleSubmit = async (e) => {
    e.preventDefault();

    console.log("clicked");

    // Validate form data
    const validationErrors = validate();
    setErrors(validationErrors);

    if (Object.keys(validationErrors).length > 0) {
      return; // Early exit if validation fails
    }

    const url = "http://localhost:8072/user/register";
    const newUser = {
      profilename: formData.name,
      username: formData.email,
      password: formData.password,
      role: "user",
    };

    try {
      // Check for email duplication in a single request
      const response = await axios.post(url, newUser);

      console.log(response.data);

      if (response.data.message === "Error occurred while registering user") {
        setDuplicateEmail(true);
        setSuccess(false);
        return; // Early exit if email already exists
      }

      setSuccess(response.data.message);
      setDuplicateEmail(false);
      setFormData({
        profilename: "",
        username: "",
        password: "",
        confirmPassword: "",
      });

      setTimeout(() => setSuccess(false), 3000);
      navigate("/login");
    } catch (error) {
      console.error("Error registering user:", error);
      setDuplicateEmail(true);
      setSuccess(false);
      // Handle other errors (e.g., network issues, server errors)
    }
  };

  return (
    <>
      <Header />
      <Navbar />
      <div className="mycontainer">
        <img src={signupimg} alt="woker"></img>
        <form
          onSubmit={handleSubmit}
          className="sign-up-form forms-mycontainer"
          autoComplete="off"
        >
          <h2 className="title">Sign up</h2>

          <div className="input-field">
            <FontAwesomeIcon icon={faUser} className="my-auto mx-auto" />
            <input
              type="text"
              name="name"
              placeholder=" Enter Profilename"
              value={formData.name}
              onChange={handleChange}
              required
            />
          </div>
          <div className="input-field">
            <FontAwesomeIcon icon={faEnvelope} className="my-auto mx-auto" />
            <input
              type="email"
              name="email"
              placeholder="Enter Email"
              value={formData.email}
              onChange={handleChange}
              required
            />
          </div>
          {errors.email && <div className="text-danger">{errors.email}</div>}
          <div className="input-field">
            <FontAwesomeIcon icon={faLock} className="my-auto mx-auto" />
            <input
              type={showPassword ? "text" : "password"}
              name="password"
              placeholder="Enter Password"
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
          {errors.password && (
            <div className="text-danger">{errors.password}</div>
          )}

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

          <div className="input-field">
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
          {errors.confirmPassword && (
            <div className="text-danger">{errors.confirmPassword}</div>
          )}

          <button type="submit" className=" mybtn">
            Sign up
          </button>

          {success && <div className="alert alert-success mt-3">{success}</div>}
          {duplicateEmail && (
            <div className="alert alert-danger mt-3">
              {SIGNUP_HANDLESUBMIT_ACCOUNT_EXISTS}
            </div>
          )}
          {/* <p className="social-text">Or Sign up with social platforms</p>
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
              Already have an account?{" "}
              <Link to="/login" style={{ textDecoration: "none" }}>
                Login
              </Link>
            </p>
          </div>
        </form>
      </div>
    </>
  );
};

export default SignUp;

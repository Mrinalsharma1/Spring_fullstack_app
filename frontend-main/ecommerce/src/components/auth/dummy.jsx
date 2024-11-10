import React, { useState } from "react";
import "./AuthForm.css";
// import { FaGofore, FaExplosion } from "react-icons/fa6";
import {
  SIGNUP_HANDLESUBMIT_PASSWORD,
  SIGNUP_HANDLESUBMIT_ACCOUNT_EXISTS,
  SIGNUP_HANDLESUBMIT_ACCOUNT_SUCCESS,
  SIGNUP_HANDLESUBMIT_ACCOUNT_FAILURE,
  THEME_COLORS,
} from "../GlobalData/Constant.js";
import { Link } from "react-router-dom";
import { v4 as uuidv4 } from "uuid";
import axios from "axios";
import NavBar from "../header/NavBar.jsx";
import log from "loglevel";

// this is to generate unique id
function Signup() {
  log.setLevel("info"); // Set the logging level
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
  });
  const [error, setError] = useState(false);
  const [success, setSuccess] = useState(false);
  const [passError, setPassError] = useState(false);
  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Validate password length and special character
    const passwordRegex = /^(?=.*[!@#$%^&*(),.?":{}|<>]).{8,}$/;
    if (!passwordRegex.test(formData.password)) {
      setPassError(true);
      setTimeout(() => {
        setPassError(false);
      }, 3000);
      log.error("Password validation failed");
      return;
    }

    try {
      // Check if the user already exists
      const existingUsers = await axios.get("http://localhost:5001/users");
      const userExists = existingUsers.data.some(
        (user) => user.email === formData.email
      );

      if (userExists) {
        setError(true);
        setTimeout(() => {
          setError(false);
        }, 3000);
        log.info("Account already exists for email: " + formData.email);
      } else {
        const userData = {
          id: uuidv4(),
          name: formData.name.toUpperCase(),
          email: formData.email,
          password: formData.password,
          role: "user",
        };

        // Store the new user data
        const response = await axios.post(
          "http://localhost:5001/users",
          userData
        );
        setSuccess(true);
        setTimeout(() => {
          setSuccess(false);
        }, 3000);
        log.info(
          "Account registered successfully for email: " + formData.email
        );
        // //console.log(response.data);
      }
    } catch (error) {
      console.error(SIGNUP_HANDLESUBMIT_ACCOUNT_FAILURE);
      log.error("Error during account registration: " + error.message);
    }
  };

  return (
    <div>
      <NavBar />
      <div className="container-fluid">
        <div className="row">
          <div className="col-md-8 mx-auto mt-4 ">
            <div className="row">
              <div className="col-md-6 p-3 login-box rounded">
                <div className=" box_left_side">
                  <div className="">
                    <div className=" logo  text-start">
                      <h2>Chronicle</h2>
                    </div>
                    <div className="header">
                      <h5 className="text-start">Create your account</h5>
                      <p className="text-start">
                        Let's get started with your 30 days free trial
                      </p>
                    </div>
                    <form onSubmit={handleSubmit}>
                      <div className="mb-3">
                        <input
                          type="text"
                          name="name"
                          className="form-control"
                          placeholder="Enter Username"
                          value={formData.name}
                          onChange={handleChange}
                        />
                      </div>
                      <div className="mb-3">
                        <input
                          type="email"
                          name="email"
                          className="form-control"
                          placeholder="Enter Email"
                          value={formData.email}
                          onChange={handleChange}
                        />
                      </div>
                      <div className="mb-3">
                        <input
                          type="password"
                          name="password"
                          className="form-control"
                          placeholder="Enter Password"
                          value={formData.password}
                          onChange={handleChange}
                        />
                      </div>
                      <button
                        type="submit"
                        className="form-control"
                        onClick={() => {}}
                        style={{
                          backgroundColor: THEME_COLORS.login_background_color,
                          color: THEME_COLORS.text_color,
                        }}
                      >
                        Sign in
                      </button>
                      {passError && (
                        <div className="alert alert-danger mt-3">
                          {SIGNUP_HANDLESUBMIT_PASSWORD}
                        </div>
                      )}

                      {error && (
                        <div className="alert alert-danger mt-3">
                          {SIGNUP_HANDLESUBMIT_ACCOUNT_EXISTS}
                        </div>
                      )}
                      {success && (
                        <div className="alert alert-success mt-3">
                          {SIGNUP_HANDLESUBMIT_ACCOUNT_SUCCESS}
                        </div>
                      )}
                      <div className="separator">
                        <span>or</span>
                      </div>
                      <button
                        style={{ marginTop: "-0px" }}
                        className="btn btn-light form-control"
                      >
                        <span className="px-1">Sign in with Google</span>
                      </button>
                      <div className="signup_footer pt-2">
                        <p>
                          Already have an account?{" "}
                          <Link
                            style={{
                              textDecoration: "none",
                              color: THEME_COLORS.login_text_color,
                            }}
                            to="/login"
                          >
                            Login
                          </Link>
                        </p>
                      </div>
                    </form>
                  </div>
                </div>
              </div>
              <div className="col-md-6  mt-4">
                <div className="box_right_side">
                  <img
                    className="img-fluid"
                    src="./utils/login.jpg"
                    alt="Login"
                  />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Signup;

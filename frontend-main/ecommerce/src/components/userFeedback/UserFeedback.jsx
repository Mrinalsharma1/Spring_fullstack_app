import React from "react";
import "./UserFeedback.css";
import Header from "../header/Header";
import Navbar from "../header/Navbar";
import { useSelector } from "react-redux";
import Footer from "../footer/Footer";
import review from "../../assets/images/review.jpg";
import { useState } from "react";
import axios from "axios";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
function UserFeedback() {
  const [pincodes, setPincodes] = useState("");
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    pincode: "",
    review: "",
  });
  const navigate = useNavigate();

  const send_feedback = async () => {
    try {
      console.log(formData);
      await axios.post(
        "http://localhost:8075/feedback/submitfeedback",
        formData
      );
      navigate("/");
    } catch (error) {
      console.error(error);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  return (
    <>
      <Header />
      <Navbar />
      <div className="user-feedback">
        <div className="feedback-image">
          <img src={review} alt="Feedback" />
        </div>
        <div className="feedback-fields">
          <div className="headings">
            <h4>Got something to share?</h4>
            <h6>Your review is extremely important to us</h6>
          </div>
          <div className="review-field">
            <label>Name: </label>
            <input
              type="text"
              name="name"
              value={formData.name}
              onChange={handleChange}
              required
            />
          </div>
          <div className="review-field">
            <label>Email: </label>
            <input
              type="text"
              name="email"
              value={formData.email}
              onChange={handleChange}
              required
            />
          </div>
          <div className="review-field">
            <label>Pincode: </label>
            <input
              type="text"
              name="pincode"
              value={formData.pincode}
              onChange={handleChange}
              required
            />
          </div>
          <div className="review-field">
            <label>Review: </label>
            <textarea
              name="review"
              value={formData.review}
              onChange={handleChange}
            />
          </div>
          <div className="review-action">
            <button className="review-send-button" onClick={send_feedback}>
              Send
            </button>
            <button className="review-cancel-button">Cancel</button>
          </div>
        </div>
      </div>
      <Footer />
    </>
  );
}

export default UserFeedback;

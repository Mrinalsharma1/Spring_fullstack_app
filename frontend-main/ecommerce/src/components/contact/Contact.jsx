import React, { useState } from "react";
import { FaMobileAlt } from "react-icons/fa";
import { LuMailOpen } from "react-icons/lu";
import { GrLocation } from "react-icons/gr";
import "./Contact.css";
import { Link } from "react-router-dom";
import { FaAnglesRight } from "react-icons/fa6";
import Header from "../header/Header";
import Navbar from "../header/Navbar";
import Footer from "../footer/Footer";
import contact2 from "../../assets/images/contact-1.jpg";
import axios from "axios";

function Contact() {
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    phone: "",
    subject: "",
    message: "",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      // Send POST request with Axios
      const response = await axios.post(
        "http://localhost:8072/contact/addmessage",
        formData,
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (response.data.status === "success") {
        alert(response.data.message);
        setFormData({
          name: "",
          email: "",
          phone: "",
          subject: "",
          message: "",
        });
      } else {
        alert("Something went wrong!");
      }
    } catch (error) {
      console.error(
        "Error:",
        error.response ? error.response.data : error.message
      );
      alert("An error occurred while sending your message.");
    }
  };

  return (
    <>
      <Header />
      <Navbar />
      <div className="contact-container">
        <div className="contact_bg">
          <div className="contact_title">
            <h2>Contact Us</h2>
          </div>
          <p className="text-center home-link text-white">
            <Link to="/" style={{ textDecoration: "none" }}>
              Home
            </Link>
            <span className="px-2 contact-icon contact-icon">
              <FaAnglesRight />
            </span>

            <span>Contact</span>
          </p>
        </div>

        <div className="contact-info mx-auto">
          <div className="contact-phone">
            <div className="contact_icon_box">
              <FaMobileAlt size={38} style={{ color: "#60c5e4" }} />
            </div>

            <div className="contact-title ">Phone Number</div>
            <div className="contact-text">+91-XXXXXXXX</div>
          </div>
          <div className="contact-email">
            <div className="contact_icon_box">
              <LuMailOpen size={38} style={{ color: "#60c5e4" }} />
            </div>
            <div className="contact-title ">Email</div>
            <div className="contact-text">swiftqsolutions@gmail.com</div>
          </div>
          <div className="contact-location">
            <div className="contact_icon_box">
              <GrLocation size={38} style={{ color: "#60c5e4" }} />
            </div>
            <div className="contact-title ">Location</div>
            <div className="contact-text">Electronic City,Bengaluru</div>
          </div>
        </div>
        <hr className="contact-divider"></hr>
        <div className="contact-message-container">
          <div className="container-fluid">
            <div className="row">
              <div className="col-md-5">
                <div className="contact-image">
                  <img src={contact2} alt="" />
                </div>
              </div>
              <div className="col-md-7">
                <div className="contact-form">
                  <form onSubmit={handleSubmit}>
                    <div className="form-heading">
                      <h2>Get in Touch</h2>
                      <p className="mb-4">
                        Have questions or need support? Connect with us, and
                        we'll be happy to assist.
                      </p>
                    </div>
                    <div className="row">
                      <div className="col-md-6 mb-3">
                        <input
                          type="text"
                          className="form-control"
                          placeholder="Name"
                          name="name"
                          value={formData.name}
                          onChange={handleChange}
                          required
                        />
                      </div>

                      <div className="col-md-6 mb-3">
                        <input
                          type="email"
                          className="form-control"
                          placeholder="Email"
                          name="email"
                          value={formData.email}
                          onChange={handleChange}
                          required
                        />
                      </div>
                    </div>
                    <div className="row">
                      <div className="col-md-6 mb-3">
                        <input
                          type="text"
                          className="form-control"
                          placeholder="Phone"
                          name="phone"
                          value={formData.phone}
                          onChange={handleChange}
                          required
                        />
                      </div>

                      <div className="col-md-6 mb-3">
                        <input
                          type="text"
                          className="form-control"
                          placeholder="Subject"
                          name="subject"
                          value={formData.subject}
                          onChange={handleChange}
                          required
                        />
                      </div>
                    </div>

                    <div className="mb-3">
                      <textarea
                        className="form-control"
                        rows="5"
                        placeholder="Message"
                        name="message"
                        value={formData.message}
                        onChange={handleChange}
                        required
                      ></textarea>
                    </div>

                    <div className="text-center">
                      <button
                        type="submit"
                        className="btn submit-button btn-lg form-control"
                      >
                        SEND MESSAGE
                      </button>
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <Footer />
    </>
  );
}

export default Contact;

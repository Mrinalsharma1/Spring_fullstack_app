import React, { useEffect, useState } from "react";
import "./OurServices.css";
import Navbar from "../header/Navbar";
import Header from "../header/Header";

import Footer from "../footer/Footer";

import { Link, useNavigate } from "react-router-dom";
import { FaAnglesRight } from "react-icons/fa6";
import axios from "axios";
import img1 from "../../assets/images/1.jpg";
import img2 from "../../assets/images/2.jpg";
import img3 from "../../assets/images/3.jpg";
import img4 from "../../assets/images/4.jpg";
import img5 from "../../assets/images/5.jpg";
import img6 from "../../assets/images/6.jpg";
import { useSelector } from "react-redux";
function OurServices() {
  const isLoggedIn = useSelector((state) => state.login.isLoggedIn);
  const [services, setServices] = useState([]);
  const serviceData = [];
  const fetchServices = async () => {
    const response = await axios.get(
      "http://localhost:8074/servicetype/getallservices"
    );
    //console.log(response.data);
    setServices(response.data);
  };

  const navigate = useNavigate();
  const serviceImages = [img1, img2, img3, img4, img5, img6];

  useEffect(() => {
    fetchServices();
  }, []);

  const truncateMessage = (message, wordLimit) => {
    const words = message.split(" ");
    if (words.length > wordLimit) {
      return words.slice(0, wordLimit).join(" ") + "...";
    }
    return message;
  };

  return (
    <>
      <Header />
      <Navbar />
      <div className="services_bg">
        <div className="services_title">
          <h2>Our Services</h2>
        </div>
        <p className="text-center fs-6 home-link text-white">
          <Link to="/" style={{ textDecoration: "none" }}>
            Home
          </Link>
          <span className="px-2 contact-icon ">
            <FaAnglesRight />
          </span>
          <span>Service</span>
        </p>
      </div>
      <div className="container ">
        <div className="row">
          {services.length > 1 &&
            services.map((item, index) => {
              return (
                // Make sure to add the return statement here
                <div className="col-md-4" key={index}>
                  <div className="our-services-container">
                    <div className="service-cardp">
                      <div className="service_card ">
                        <img
                          src={serviceImages[index]}
                          alt={item.servicename}
                          className="img-fluid our-services-image"
                        />
                        <div className="service-overlay">
                          <div className="our-services-text">
                            {truncateMessage(item.description, 11)}
                          </div>
                          {isLoggedIn ? (
                            <button
                              className="services-button"
                              onClick={() => {
                                navigate("/bookappointment");
                              }}
                            >
                              Book Now
                            </button>
                          ) : (
                            <button
                              className="services-button"
                              onClick={() => {
                                navigate("/login");
                              }}
                            >
                              Sign in
                            </button>
                          )}
                        </div>
                      </div>
                      <div className="our-services-head">
                        <h4 className="our-services-title">
                          {item.servicename}
                        </h4>
                      </div>
                    </div>
                  </div>
                </div>
              );
            })}
        </div>
      </div>
      <Footer />
    </>
  );
}

export default OurServices;

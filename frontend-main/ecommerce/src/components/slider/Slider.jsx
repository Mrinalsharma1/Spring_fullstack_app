import React from "react";
import slider1 from "../../assets/images/slider-1.jpg"; // Replace with actual path
import slider2 from "../../assets/images/slider-2.jpg"; // Replace with actual path
import "./Slider.css"; // Custom styles
import { FaChevronLeft, FaChevronRight } from "react-icons/fa6";
import "animate.css"; // Ensure animate.css is imported
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";

function Slider() {
  const isLoggedin = useSelector((state) => state.login.isLoggedIn);
  const navigate = useNavigate();
  return (
    <div className="my-slider">
      <div
        id="carouselExampleRide"
        className="carousel slider-carousel slide"
        data-bs-ride="carousel"
      >
        <div className="carousel-inner ">
          <div
            className="carousel-item slider-carousel-item active"
            style={{ backgroundImage: `url(${slider1})` }} // Background image applied here
          >
            {/* <div className="carousel-overlay"></div> Overlay effect */}
            <div className="slider-content  d-md-block">
              <h2
                className="animated fadeInLeft"
                style={{ animationDelay: "1s" }}
              >
                Want to Repair
              </h2>
              <h1
                className="animated fadeInRight"
                style={{ animationDelay: "2s" }}
              >
                Laptop and Computer
              </h1>
              <p className="animated fadeInUp">
                SwiftQSolutions offers hassle-free repair services for laptops,
                computers, desktops, tablets, mobiles, and other electronic
                gadgets. Book a service appointment online by selecting a
                convenient date and time, and get your device repaired by our
                experts with care and precision.
              </p>
              {isLoggedin ? (
                <button
                  to=""
                  className="animated fadeInUp custom-button"
                  onClick={() => {
                    navigate("/bookappointment");
                  }}
                >
                  Book now
                </button>
              ) : (
                <button
                  to="login"
                  className="animated fadeInUp custom-button"
                  onClick={() => {
                    navigate("/login");
                  }}
                >
                  Book now
                </button>
              )}
            </div>
          </div>
          <div
            className="carousel-item "
            style={{ backgroundImage: `url(${slider2})` }} // Background image applied here
          >
            {/* <div className="carousel-overlay"></div> Overlay effect */}
            <div className="slider-content  d-md-block">
              <h2
                className="animated fadeInLeft"
                style={{ animationDelay: "1s" }}
              >
                Want to Repair
              </h2>
              <h1
                className="animated fadeInRight"
                style={{ animationDelay: "2s" }}
              >
                Laptop and Computer
              </h1>
              <p className="animated fadeInUp">
                SwiftQSolutions offers hassle-free repair services for laptops,
                computers, desktops, tablets, mobiles, and other electronic
                gadgets. Book a service appointment online by selecting a
                convenient date and time, and get your device repaired by our
                experts with care and precision.
              </p>
              {isLoggedin ? (
                <button
                  to=""
                  className="animated fadeInUp custom-button"
                  onClick={() => {
                    navigate("/bookappointment");
                  }}
                >
                  Book now
                </button>
              ) : (
                <button
                  to="login"
                  className="animated fadeInUp custom-button"
                  onClick={() => {
                    navigate("/login");
                  }}
                >
                  Book now
                </button>
              )}
            </div>
          </div>
        </div>
        <button
          className="my-carousel-control-prev"
          type="button"
          data-bs-target="#carouselExampleRide"
          data-bs-slide="prev"
        >
          <span className="my-carousel-control-prev-icon" aria-hidden="true">
            <FaChevronLeft style={{ fontSize: "24px", color: "white" }} />
          </span>
          <span className="visually-hidden">Previous</span>
        </button>
        <button
          className="my-carousel-control-next"
          type="button"
          data-bs-target="#carouselExampleRide"
          data-bs-slide="next"
        >
          <span className="my-carousel-control-next-icon" aria-hidden="true">
            <FaChevronRight style={{ fontSize: "24px", color: "white" }} />
          </span>
          <span className="visually-hidden">Next</span>
        </button>
      </div>
    </div>
  );
}
export default Slider;

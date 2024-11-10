import React from "react";
import cta1 from "../../assets/images/cta-1.png";
import "./InfoCard.css";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
function InfoCard() {
  const isLoggedin = useSelector((state) => state.login.isLoggedIn);
  const navigate = useNavigate();
  return (
    <div className="info-card-container">
      <div className="info-card-image ">
        <img src={cta1} alt="..." style={{ height: "270px", width: "300px" }} />
      </div>
      <div className="info-card-content">
        <h2>
          Need a <span className="heading-color">Computer</span> Repair?
        </h2>
        <p>Expert solutions, restoring your devices flawlessly and securely.</p>
      </div>
      <div className="info-card-button ">
        {isLoggedin ? (
          <button
            to=""
            className="animated fadeInUp custom-button"
            onClick={() => {
              navigate("/bookappointment");
            }}
          >
            BOOK APPOINTMENT
          </button>
        ) : (
          <button
            to="login"
            className="animated fadeInUp custom-button"
            onClick={() => {
              navigate("/login");
            }}
          >
            BOOK APPOINTMENT
          </button>
        )}
      </div>
    </div>
  );
}

export default InfoCard;

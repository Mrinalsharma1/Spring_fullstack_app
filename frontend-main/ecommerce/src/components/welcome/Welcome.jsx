import React from "react";
import "./Welcome.css";
import keyboard from "../../assets/images/keyboard.jpg";
import { useNavigate } from "react-router-dom";
function Welcome() {
  const navigate = useNavigate();
  return (
    <div className="welcome-container">
      <div className="welcome-content">
        <h2 className="welcome-msg">Welcome to Our </h2>
        <h1 className="company-name">SwiftQSolution</h1>
        <p className="para-bold">
          Trusted partner for reliable and efficient electronic device repairs.
          We specialize in servicing a wide range of devices, including laptops,
          computers, tablets, mobiles, and other electronic gadgets. Our
          platform allows you to conveniently book repair appointments at your
          preferred date and time. With a dedicated team of professionals, we
          ensure that your devices are repaired swiftly and with precision.
        </p>
        <p className="para-light">
          We ensure responsible disposal of non-repairable parts, aligning with
          environmental standards to help protect our planet.
        </p>
        <div className="buttons">
          <button
            className="contact-button"
            onClick={() => {
              navigate("/contact");
            }}
          >
            CONTACT US
          </button>
          <button
            className="explore-button"
            onClick={() => {
              navigate("/ourservices");
            }}
          >
            EXPLORE MORE
          </button>
        </div>
      </div>
      <div className="welcome-image">
        <img src={keyboard} alt="Welcome" />
      </div>
    </div>
  );
}

export default Welcome;

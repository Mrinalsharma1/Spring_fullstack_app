import React from "react";
import { FaMobileAlt } from "react-icons/fa";
import iphone1 from "../../assets/images/iphone-1.png";
import { FaTools } from "react-icons/fa";
import "./Assurance.css";

function Assurance() {
  return (
    <div className="container-fluid assurance_top_margin">
      <div className=" row assurance-heading">
        <div className="middle-title">
          <h2>Why Choose Us?</h2>
        </div>

        <div className="middle-logo">
          <FaTools size={30} style={{ color: "#60c5e4" }} />
        </div>
      </div>
      <div className="row assurance-block">
        <div className="col-md-4 left-column">
          <div className="assurance">
            <div className="assurance-text">
              <h4 className="assurance-title">Low Cost</h4>
              <p className="assurance-content">
                Enjoy affordable, high-quality repairs crafted to keep your
                budget intact while ensuring premium service.
              </p>
            </div>
            <div className="assurance-logo">
              <FaMobileAlt size={36} style={{ color: "#60c5e4" }} />
            </div>
          </div>

          <div className="assurance">
            <div className="assurance-text">
              <h4 className="assurance-title">Best Materials</h4>
              <p className="assurance-content">
                We use premium-grade parts and materials for enhanced
                durability, ensuring repairs that truly last.
              </p>
            </div>
            <div className="assurance-logo">
              <FaMobileAlt size={36} style={{ color: "#60c5e4" }} />
            </div>
          </div>
        </div>

        <div className="col-md-4 middle-column">
          <div className="assurance-image">
            <img src={iphone1} className="d-block" alt="..." />
          </div>
        </div>

        <div className="col-md-4 right-column">
          <div className="assurance">
            <div className="assurance-logo">
              <FaMobileAlt size={36} style={{ color: "#60c5e4" }} />
            </div>
            <div className="assurance-text">
              <h4 className="assurance-title">Expert Technicians</h4>
              <p className="assurance-content">
                Our team of skilled experts provides reliable, precise repairs,
                prioritizing quality every time.
              </p>
            </div>
          </div>

          <div className="assurance">
            <div className="assurance-logo">
              <FaMobileAlt size={32} style={{ color: "#60c5e4" }} />
            </div>
            <div className="assurance-text">
              <h4 className="assurance-title">Quick Turnaround</h4>
              <p className="assurance-content">
                Efficient, rapid service minimizes downtime, returning your
                device to you as quickly as possible.
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Assurance;

import React, { useState } from "react";
import "./ServiceCard.css";
import ServiceCard from "./ServiceCard";
import { FaTools } from "react-icons/fa";
function Services() {
  const serviceData = [
    {
      title: "Phone Repair",
      description:
        "Expert repair for all phone issues, from cracked screens to battery replacements.",
      icon: <i className="fas fa-mobile-alt"></i>,
    },
    {
      title: "Desktop Repair",
      description:
        "Comprehensive solutions for desktop repairs, upgrades, and diagnostics.",
      icon: <i className="fa fa-desktop"></i>,
    },
    {
      title: "Tablet Repair",
      description:
        "Reliable service for tablet screen repairs, software updates, and more.",
      icon: <i className="fas fa-tablet-alt"></i>,
    },
    {
      title: "Console Repair",
      description:
        "Professional repair for gaming consoles, including hardware and software fixes.",
      icon: <i className="fa fa-gamepad"></i>,
    },
    {
      title: "Headphone Repair",
      description:
        "High-quality repair for headphones, addressing wiring, sound, and connectivity issues.",
      icon: <i className="fa fa-headphones"></i>,
    },
    {
      title: "Laptop Repair",
      description:
        "Complete laptop services, from screen replacement to performance upgrades.",
      icon: <i className="fa fa-laptop"></i>,
    },
    // ... other service data
  ];

  return (
    <div className="service-container">
      <h2 className="service-title">Our Services</h2>
      <div className="service-logo">
        <FaTools size={28} style={{ color: "#60c5e4" }} />
      </div>
      <div className="card-grid">
        {serviceData.map((service, index) => (
          <ServiceCard key={index} {...service} />
        ))}
      </div>
    </div>
  );
}

export default Services;

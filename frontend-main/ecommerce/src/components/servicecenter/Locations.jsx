import React, { useState, useEffect } from "react";
import axios from "axios";
import SelectCenter from "./SelectCenter";
import Navbar from "../header/Navbar";
import Header from "../header/Header";
import Footer from "../footer/Footer";
import { Link } from "react-router-dom";
import { FaAnglesRight } from "react-icons/fa6";
import { useSelector } from "react-redux";

function Locations() {
  const token = useSelector((state) => state.login.token);

  const [states, setStates] = useState({});

  useEffect(() => {
    const fetchData = async () => {
      const data = await fetchAddresses();
      setStates(data);
    };
    fetchData();
  }, []);

  const fetchAddresses = async () => {
    try {
      const response = await axios.get(
        "http://localhost:8073/service-center/getallservicecenters"
      );
      const addresses = response.data.serviceCenters;

      return addresses;
    } catch (error) {
      console.error("Error fetching data from db.json:", error);
      return [];
    }
  };

  return (
    <div>
      <Header />
      <Navbar />
      <div className="location-hero-bg">
        <div className="location-title">
          <h2>Search Location</h2>
        </div>
        <p className="text-center text-white">
          <Link to="/" className="servicep" style={{ textDecoration: "none" }}>
            Home
          </Link>
          <span className="px-2">
            <FaAnglesRight />
          </span>

          <Link
            to="/servicecenter"
            className="text-white"
            style={{ textDecoration: "none" }}
          >
            Service Centers
          </Link>
        </p>
      </div>

      {states.length >= 1 && <SelectCenter states={states}></SelectCenter>}
      <Footer />
    </div>
  );
}

export default Locations;

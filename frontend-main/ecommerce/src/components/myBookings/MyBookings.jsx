import axios from "axios";
import React, { useEffect, useState } from "react";
import BookingsComp from "./BookingsComp";
import Navbar from "../header/Navbar";
import Header from "../header/Header";
import { Link } from "react-router-dom";
import { FaAnglesRight } from "react-icons/fa6";
import "./Bookings.css";
import { useSelector } from "react-redux";

function MyBookings() {
  const token = useSelector((state) => state.login.token);
  const [bookings, setBookings] = useState([]);

  useEffect(() => {
    fetchSubmissions();
  }, []);

  const fetchSubmissions = async () => {
    try {
      const response = await axios.get(
        "http://localhost:8071/user-booking-details",
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      setBookings(response.data.data);
    } catch (error) {
      console.error("Error fetching data from db.json:", error);
    }
  };

  return (
    <>
      <Header />
      <Navbar />
      <div className="location-hero-bg">
        <div className="location-title">
          <h2>Bookings</h2>
        </div>
        <p className="text-center text-white">
          <Link
            to="/"
            className="text-white"
            style={{ textDecoration: "none" }}
          >
            Home
          </Link>
          <span className="px-2">
            <FaAnglesRight />
          </span>

          <Link
            to="/bookings"
            className="text-white"
            style={{ textDecoration: "none" }}
          >
            My Bookings
          </Link>
        </p>
      </div>
      {bookings && (
        <BookingsComp bookings={bookings} fetchSubmissions={fetchSubmissions} />
      )}
    </>
  );
}

export default MyBookings;

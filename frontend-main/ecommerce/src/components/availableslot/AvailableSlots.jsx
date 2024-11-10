import React, { useState, useEffect } from "react";
import Calender from "./Calender/Calender";
import "react-calendar/dist/Calendar.css";
import "./AvailableSlots.css"; // Ensure your CSS file is imported
import Header from "../header/Header";
import Navbar from "../header/Navbar";
import Footer from "../footer/Footer";
import { Link } from "react-router-dom";
import { FaAnglesRight } from "react-icons/fa6";
import axios, { toFormData } from "axios";

function AvailableSlots() {
  const [date, setDate] = useState(new Date());
  const [showCalendar, setShowCalendar] = useState(false);
  const [selectedDate, setSelectedDate] = useState(null);
  const [isSlotEnabled, setIsSlotEnabled] = useState(false);
  const [givenDateAllSlotTime, setGivenDateAllSlotTime] = useState([]);

  const bookedSlots = [
    new Date(2024, 9, 15), // October 15, 2024
    new Date(2024, 9, 10), // October 10, 2024
  ];

  const [products, setProducts] = useState([]);
  // const [timeslots, setTimeslots] = useState([]);
  const [pincode, setPincode] = useState("");

  //to call products endpoint
  useEffect(() => {
    axios
      .get("http://localhost:8074/product/getallproducts")
      .then((response) => {
        setProducts(response.data);
      })
      .catch((error) => {
        console.error("There was an error fetching the product data!", error);
      });
  }, []);

  useEffect(() => {
    console.log("Re render calls");
    if (selectedDate) {
      fetchTimeSlots();
    }
  }, [selectedDate]);

  // this is the function to change date format
  const formatDate = (date) => {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0"); // Months are 0-based, so add 1
    const day = String(date.getDate()).padStart(2, "0"); // Pad day with leading zero if needed
    return `${year}-${month}-${day}`;
  };

  //to call timeslots endpoint
  const fetchTimeSlots = async () => {
    console.log("pincode", pincode);
    console.log("selected date is", selectedDate);

    try {
      const response = await axios.get(
        `http://localhost:8074/booking/timings?pincode=${pincode}&date=${selectedDate}`
      );
      console.log(response.status);
      if (response.status === 200) {
        const filteredTimings = response.data.timings.filter(
          (slot) => slot.status !== "completed"
        );
        setGivenDateAllSlotTime(filteredTimings);
      }
    } catch (error) {
      console.error("Failed to fetch timeslots", error);
    }
  };

  // Function to check if a date is booked
  const isBooked = (date) => {
    const formattedDate = formatDate(date);
    // Check if the date exists in bookedSlots
    const isSlotBooked = bookedSlots.some(
      (bookedDate) => formatDate(bookedDate) === formattedDate
    );

    // Check if booking count data is available
    if (givenDateAllSlotTime.length > 0) {
      const matchingDate = givenDateAllSlotTime.find(
        (slot) => slot.date === formattedDate
      );
      // If date exists and count is 4, consider it booked
      return isSlotBooked || (matchingDate && matchingDate.count === 4);
    }

    // If no booking count data, return based on bookedSlots
    return isSlotBooked;
  };

  // Function to handle date selection
  const handleDateChange = (newDate) => {
    console.log("------------------------", newDate);
    setDate(newDate);

    setSelectedDate(formatDate(newDate)); // this is the function to change date format
    fetchTimeSlots();

    // Enable time slots only if the selected date is available
    if (!isBooked(newDate)) {
      setIsSlotEnabled(true);
    } else {
      setIsSlotEnabled(false);
    }
    setShowCalendar(false);
  };

  console.log("selected  ======> ", selectedDate);

  // Function to handle input click
  const handleInputClick = () => {
    setShowCalendar(true);
  };

  const handlePincodeChange = (e) => {
    const { value } = e.target;
    setPincode(value);

    if (value.length === 6) {
      apiCall(value);
    }
  };

  function getNextFiveDates(startDate) {
    const dates = [];
    const start = new Date(startDate);
    for (let i = 0; i < 6; i++) {
      const nextDate = new Date(start);
      nextDate.setDate(start.getDate() + i);
      dates.push(nextDate.toISOString().split("T")[0]);
    }
    return dates[4];
  }
  //-----------------------------------------------------------------------
  const [calendarData, setCalendarData] = useState(null);

  const apiCall = async (pinCode) => {
    try {
      const todaysDate = formatDate(new Date());
      const nextFiveDays = getNextFiveDates(todaysDate);
      console.log(Date.now());
      const response = await axios.get(
        `http://localhost:8074/booking/between-dates?startDate=${todaysDate}&endDate=${nextFiveDays}`
      );
      const data = response?.data?.bookings?.filter(
        (ele) => pinCode == ele?.slot.serviceCenter.pincode
      );

      const bookingCountsObject = data.reduce((acc, booking) => {
        acc[booking.bdate] = (acc[booking.bdate] || 0) + 1;
        return acc;
      }, {});

      // Convert object to array format
      const bookingCountsArray = Object.entries(bookingCountsObject).map(
        ([date, count]) => ({
          date,
          count,
        })
      );
      console.log("------after reducer", bookingCountsArray);
      setCalendarData(bookingCountsArray);
    } catch (error) {
      console.log("error: ", error);
    }
  };

  return (
    <div>
      <Header />
      <Navbar />

      <div className="available_slot">
        <div className="available_slot_bg">
          <div className="slot_title">
            <h2>Available Slots</h2>
          </div>
          <p className="text-center text-white">
            <Link
              to="/"
              className="availablep"
              style={{ textDecoration: "none" }}
            >
              Home
            </Link>
            <span className="px-2">
              <FaAnglesRight />
            </span>
            <Link
              to="/availableslot"
              className="text-white"
              style={{ textDecoration: "none" }}
            >
              Available Slot
            </Link>
          </p>
        </div>

        <div className="container-fluid">
          <div className="row">
            <div className="col-md-10 mx-auto pt-5">
              <div className="row g-2">
                <div className="col-md px-2">
                  <label className="form-label">Product</label>
                  <select className="form-select" id="floatingSelectGrid">
                    <option selected>Select Product</option>
                    {products.map((product) => (
                      <option key={product.productid} value={product.productid}>
                        {product.productname}
                      </option>
                    ))}
                  </select>
                </div>
                <div className="col-md px-2">
                  <label className="form-label">Pin</label>
                  <input
                    type="number"
                    className="form-control"
                    id="floatingInputGrid"
                    placeholder="Enter Pin Code"
                    onChange={handlePincodeChange}
                    onWheel={(e) => e.target.blur()}
                  />
                </div>
              </div>
              <div className="row g-2 mt-4 mb-5">
                <div className="col-md px-2 pb-3">
                  <label className="form-label">Preferred Date</label>
                  <input
                    type="text"
                    className="form-control"
                    placeholder="Select Preferred Date"
                    onClick={handleInputClick}
                    value={date.toDateString()}
                    readOnly
                  />
                  {showCalendar && (
                    <Calender
                      bookedSlots={calendarData}
                      handleDateChange={handleDateChange}
                    />
                  )}
                </div>
                <div className="col-md px-2">
                  <label className="form-label">Preferred Time Slot </label>
                  <select
                    className="form-select"
                    id="floatingSelectGrid"
                    disabled={!isSlotEnabled}
                  >
                    <option selected>Select Time Slot</option>
                    {givenDateAllSlotTime.map((item, index) => (
                      <option
                        key={index}
                        defaultValue="select slot"
                        disabled={item.status === "Confirmed"}
                        style={{
                          backgroundColor:
                            item.status === "Confirmed" ? "#D3D3D3" : "white",
                        }}
                      >
                        {item.timing}
                      </option>
                    ))}
                  </select>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <Footer />
    </div>
  );
}

export default AvailableSlots;

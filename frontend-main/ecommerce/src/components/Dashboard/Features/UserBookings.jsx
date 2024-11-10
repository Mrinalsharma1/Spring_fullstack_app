import React, { useState, useEffect } from "react";
import axios from "axios";
import DashboardLayout from "../Layout/DashboardLayout";
import { Button } from "@mui/material";
import { useSelector } from "react-redux";
import "./UserBookings.css";
import Swal from "sweetalert2";
import empty from "../../../assets/images/empty.svg";

function UserBookings() {
  const [bookings, setBookings] = useState([]);
  const reduxData = useSelector((state) => state.login);
  const [managerPinCode, setManagerPinCode] = useState("");
  const [message, setMessage] = useState("");
  const [success, setSuccess] = useState(null);
  const [error, setError] = useState(false);

  const token = reduxData.token;
  const userId = reduxData.id;

  // Fetch all the Pin code
  useEffect(() => {
    console.log("Fetch all the Pin Code");
    fetchPinCode();
  }, []);

  useEffect(() => {
    console.log("Data of the this pin code");
    fetchBookingsEverySecond();

    const interval = setInterval(fetchBookingsEverySecond, 5000);

    return () => clearInterval(interval);
  }, [managerPinCode]);

  console.log("count");

  // useEffect(() => {
  //   console.log("Re render calls");
  //   if (selectedDate) {
  //     // fetchTimeSlots();
  //   }
  // }, [selectedDate]);

  const fetchPinCode = async () => {
    try {
      const pincodeResponse = await axios.get(
        `http://localhost:8071/pincode/${userId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      if (pincodeResponse.data.status === "success") {
        console.log(pincodeResponse.data);
        setManagerPinCode(pincodeResponse.data.pincode);
        //console.log(pincodeResponse.data.pincode);
      }
    } catch (error) {
      console.error("Error fetching bookings data:", error);
      if (error.response && error.response.status === 401) {
        setSuccess(401);
      } else {
        setError(true);
        setMessage("Failed to fetch bookings");
        setTimeout(() => {
          setError(false);
        }, 3000);
      }
    }
  };

  const handleComplete = async (id, username) => {
    try {
      // Show loading message
      Swal.fire({
        title: "Please wait...",
        text: "Completing your booking",
        allowOutsideClick: false,
        didOpen: () => {
          Swal.showLoading();
        },
      });

      // Make API request
      await axios.put(
        `http://localhost:8071/update-status/${id}/${username}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      // Update state on success
      setBookings(
        bookings.map((booking) =>
          booking.id === id ? { ...booking, status: "completed" } : booking
        )
      );
      setSuccess(200);
      setMessage("Booking completed successfully");

      // Show success alert
      Swal.fire({
        icon: "success",
        title: "Success",
        text: "Booking completed successfully",
        timer: 3000,
        showConfirmButton: false,
      });
    } catch (error) {
      console.error("Error completing booking:", error);
      if (error.response && error.response.status === 401) {
        setSuccess(401);
        Swal.fire({
          icon: "error",
          title: "Unauthorized",
          text: "You are not authorized to complete this booking.",
          timer: 3000,
          showConfirmButton: false,
        });
      } else {
        setError(true);
        setMessage("Failed to complete booking");

        Swal.fire({
          icon: "error",
          title: "Error",
          text: "Failed to complete booking. Please try again.",
          timer: 3000,
          showConfirmButton: false,
        });
      }
    }
  };

  const fetchBookingsEverySecond = async () => {
    try {
      const response = await axios.get("http://localhost:8071/bookings", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (response.data.status === "success") {
        //console.log("=====> ", response.data.data);
        const filteredBookings = response.data.data.filter(
          (booking) =>
            booking.pincode === managerPinCode && booking.status !== "completed"
        );
        setBookings(filteredBookings);
        setMessage(response.data.message);
      } else {
        setMessage(response.data.message);
      }
    } catch (error) {
      if (error.response && error.response.status === 401) {
        setTimeout(() => {
          setSuccess(false);
        }, 3000);
        setSuccess(401);
      }
      console.error("Error fetching bookings: ", error);
      setMessage("Failed to fetch bookings.");
    }
  };

  return (
    <div>
      <DashboardLayout>
        {success === 401 && (
          <div className="alert alert-danger mt-3 text-center">
            Please login again
          </div>
        )}
        {/* {success === 200 && (
          <div className="alert alert-success mt-3 text-center">
            Data updated successfully
          </div>
        )} */}
        {error && (
          <div className="alert alert-danger mt-3 text-center">{message}</div>
        )}
        <div className="userbookings-container">
          <h2 className="userbookings-h2">User Bookings</h2>
          {bookings.length === 0 && (
            <img className="empty-img" src={empty} alt="No Data Found" />
          )}
          {bookings.length > 0 && (
            <table className="userbookings-table table table-hover">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Email</th>
                  <th>Booking Date</th>
                  <th>Timing</th>
                  <th>Action</th>
                </tr>
              </thead>
              <tbody>
                {!bookings && <h4>No Data Found</h4>}
                {bookings &&
                  bookings.map((booking, index) => (
                    <tr key={index}>
                      <td>{booking.bookingid}</td>
                      <td>{booking.username}</td>
                      <td>{booking.bdate}</td>
                      <td>{booking.timing}</td>
                      <td>
                        <div className="userbookings-action">
                          <Button
                            variant="contained"
                            color="secondary"
                            onClick={() =>
                              handleComplete(
                                booking.bookingid,
                                booking.username
                              )
                            }
                            style={{ marginLeft: "10px" }}
                            disabled={booking.status === "completed"}
                          >
                            Complete
                          </Button>
                        </div>
                      </td>
                    </tr>
                  ))}
              </tbody>
            </table>
          )}
        </div>
      </DashboardLayout>
    </div>
  );
}

export default UserBookings;

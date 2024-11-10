import React from "react";
import { useSelector } from "react-redux";
import { useState } from "react";
import axios from "axios";
import Swal from 'sweetalert2';
function BookingsComp(props) {
  //   const [filtered_bookings, setFilteredBookings] = useState("");
  const { bookings, fetchSubmissions } = props;
  const token = useSelector((state) => state.login.token);
  console.log(bookings);
  const userId = useSelector((state) => state.login.id);
  const username = useSelector((state) => state.login.name);

  const filtered_bookings = bookings.filter((ele) => ele.userId === userId);
  console.log(filtered_bookings);

  const handleCancelClick = (bookingId) => {
    Swal.fire({
      title: 'Are you sure?',
      text: 'You are about to cancel this booking.',
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Yes, cancel it!',
      cancelButtonText: 'No, keep it',
    }).then((result) => {
      if (result.isConfirmed) {
        cancelBooking(bookingId);
        Swal.fire('Cancelled!', 'Your booking has been cancelled.', 'success');
      }
    });
  };

  const handleRescheduleClick = () => {
    Swal.fire({
      icon: 'info',
      title: 'To Reschedule',
      text: 'To reschedule, you’ll have to cancel this booking and create a new one.',
      confirmButtonText: 'OK'
    });
  };


  const cancelBooking = async (bookingId) => {
    console.log(bookingId);
    try {
      const response = await axios.delete(
        "http://localhost:8071/delete-booking/" + bookingId,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      fetchSubmissions();
    } catch (error) {
      console.error("Error deleting data " + error);
    }
  };

  return (
    <div className="bookings">
      <div className="bookings-list">
        {filtered_bookings.map((item, index) => (
          <div key={index} className="bookings-card">
            <div className="bookings-box">
              <div className="bookings-container">
                <p>
                  <span>BookingId </span>
                </p>
                <input type="text" disabled value={item.bookingId} />
                <p>
                  <span>Date </span>
                </p>
                <input type="text" disabled value={item.bdate} />
                <p>
                  <span>Username </span>
                </p>
                <input type="text" disabled value={username} />
              </div>
              <div className="bookings-container">
                <p>
                  <span>Time </span>
                </p>
                <input type="text" disabled value={item.timing} />

                <p>
                  <span>Service Center Name </span>
                </p>
                <input type="text" disabled value={item.name} />

                <p>
                  <span>Address </span>
                </p>
                <input type="text" disabled value={`${item.address}`} />

                <p>
                  <span>Status </span>
                </p>
                <input type="text" disabled value={item.status} />
              </div>
              <div className="bookings-container bookings-control">
                <button
                  type="button"
                  className="cancel-button"
                  onClick={() => { handleCancelClick(item.bookingId) }}
                >
                  Cancel Booking
                </button>

                <button
                  className="reschedule-button"
                  onClick={handleRescheduleClick}
                >
                  Re-schedule
                </button>

              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
export default BookingsComp;

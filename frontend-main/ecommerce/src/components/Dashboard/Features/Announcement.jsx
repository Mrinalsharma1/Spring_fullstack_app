import React, { useEffect, useState } from "react";
import DashboardLayout from "../Layout/DashboardLayout";
import "./Announcement.css";
import axios from "axios";
import { Stack, Alert } from "@mui/material";
import Swal from 'sweetalert2';


function Announcement() {
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState(false);
  const [message, setMessage] = useState("");

  const [announcementData, setAnnouncementData] = useState({
    subject: "",
    message: "",
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setAnnouncementData({ ...announcementData, [name]: value });
  };

  const handleSendClick = async (e) => {
    e.preventDefault();

    // Show "Please wait" message with loading indicator
    Swal.fire({
      title: 'Please wait...',
      text: 'Sending your announcement',
      allowOutsideClick: false,
      didOpen: () => {
        Swal.showLoading();
      },
    });

    try {
      const response = await axios.post(
        "http://localhost:8072/newsletter/send-announcement",
        announcementData
      );

      // Close "Please wait" message
      Swal.close();

      if (response.data.status === "success") {
        setMessage(response.data.message);

        // Display success message
        Swal.fire({
          icon: 'success',
          title: 'Announcement Sent',
          text: response.data.message,
          timer: 5000,
          timerProgressBar: true,
          showConfirmButton: false,
        });
      } else {
        setMessage(response.data.message);

        // Display error message if not successful
        Swal.fire({
          icon: 'error',
          title: 'Announcement Failed',
          text: response.data.message,
        });
      }

      setAnnouncementData({ subject: "", message: "" });
      setSuccess(true);
      setTimeout(() => {
        setSuccess(false);
      }, 5000);
    } catch (error) {
      // Close "Please wait" message if an error occurs
      Swal.close();

      setError(true);

      // Display error alert for unexpected error
      Swal.fire({
        icon: 'error',
        title: 'Error Sending Announcement',
        text: 'An error occurred while sending the announcement. Please try again.',
      });

      setTimeout(() => {
        setError(false);
      }, 5000);

      console.error("Error sending the announcement", error);
    }
  };


  return (
    <DashboardLayout>
      {success && (
        <Stack sx={{ width: "100%" }} spacing={2}>
          <Alert variant="filled" severity="success">
            {message}
          </Alert>
        </Stack>
      )}
      {error && (
        <Stack sx={{ width: "100%" }} spacing={2}>
          <Alert variant="filled" severity="error">
            {message}
          </Alert>
        </Stack>
      )}
      <div className="announcement-container">
        <div className="announcement-container-inner">
          <div className="announcement-row">
            <h3 className="announcement-heading">Announcement</h3>
            <div className="announcement-col">
              <form>
                <div className="announcement-form-group">
                  <p className="announcement-label">Subject :</p>
                  <input
                    type="text"
                    name="subject"
                    className="announcement-input"
                    value={announcementData.subject}
                    onChange={handleInputChange}
                  />
                </div>
                <div className="announcement-form-group">
                  <p className="announcement-label">Message :</p>
                  <textarea
                    name="message"
                    className="announcement-input"
                    value={announcementData.message}
                    onChange={handleInputChange}
                  />
                </div>
                <div className="announcement-buttons-container">
                  <button
                    type="button"
                    className="announcement-btn announcement-btn-primary"
                    onClick={handleSendClick}
                  >
                    Send
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
}

export default Announcement;

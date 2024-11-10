import React, { useState, useEffect } from "react";
import axios from "axios";
import DashboardLayout from "../Layout/DashboardLayout";
import log from "loglevel";
import Swal from 'sweetalert2';

import {
  FormControl,
  Select,
  MenuItem,
  Button,
  TextField,
} from "@mui/material";
import "./AddSlot.css";
import { useSelector } from "react-redux";

function AddSlot() {
  const [formData, setFormData] = useState({
    timing: "",
    pincode: "",
  });

  const [success, setSuccess] = useState(null);
  const [error, setError] = useState(null);
  const [message, setMessage] = useState("");
  const [managerPinCode, setManagerPinCode] = useState("");

  const reduxdata = useSelector((state) => state.login);
  const token = reduxdata.token;
  const userid = reduxdata.id;

  //console.log(token);

  useEffect(() => {
    const fetchData = async () => {
      try {
        // Fetch pincode
        const pincodeResponse = await axios.get(
          `http://localhost:8071/pincode/${userid}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        if (pincodeResponse.data.status === "success") {
          //console.log(pincodeResponse.data);
          setManagerPinCode(pincodeResponse.data.pincode);
          //console.log(pincodeResponse.data.pincode);
        } else {
          console.error(
            "Failed to fetch pincode: ",
            pincodeResponse.data.message
          );
        }

        // Fetch slots
        const slotsResponse = await axios.get(
          "http://localhost:8071/pincodetimings?pincode=" +
          pincodeResponse.data.pincode,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        //console.log("data", slotsResponse.data);
        if (slotsResponse.data.status === "success") {
          setMessage(slotsResponse.data.message);
        } else {
          setMessage(slotsResponse.data.message);
        }
      } catch (error) {
        console.error("Error fetching data:", error);
        if (error.response && error.response.status === 401) {
          setSuccess(401);
        } else {
          setError(true);
          setMessage("Failed to fetch data");
          setTimeout(() => {
            setError(false);
          }, 3000);
        }
      }
    };

    fetchData();
  }, [token, userid]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    if (name === "pincode" && (!/^\d*$/.test(value) || value.length > 6)) {
      return;
    }

    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const NewAddedSlot = {
      timing: formData.timing,
      serviceCenter: {
        pincode: managerPinCode,
      },
    };
    try {
      const response = await axios.post(
        "http://localhost:8071/addslot",
        NewAddedSlot,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      setSuccess(response.status);
      setMessage(response.data.message);
      log.info("Slot added successfully by:" + managerPinCode);

      // Display SweetAlert2 success message
      Swal.fire({
        icon: 'success',
        title: 'Slot Added Successfully',
        text: response.data.message,
        timer: 4000,
        timerProgressBar: true,
        showConfirmButton: false,
      });

      setTimeout(() => {
        setSuccess(null);
      }, 4000);

    } catch (error) {
      if (error.response) {
        console.error("Error response from server:", error.response);
        if (error.response.status === 401) {
          setError(401);

          // Display SweetAlert2 unauthorized error
          Swal.fire({
            icon: 'error',
            title: 'Unauthorized',
            text: 'You do not have permission to add this slot.',
          });
        } else {
          setError(error.response.data.message || "Failed to add Slot");

          // Display SweetAlert2 error with server response message
          Swal.fire({
            icon: 'error',
            title: 'Failed to Add Slot',
            text: error.response.data.message || "Failed to add Slot",
          });
        }
      } else {
        console.error("Error making request:", error);
        setError("Failed to add Slot");

        // Display SweetAlert2 error for network or other issues
        Swal.fire({
          icon: 'error',
          title: 'Failed to Add Slot',
          text: 'An unexpected error occurred. Please try again later.',
        });
      }

      log.info("Failed to add Slot");
      setTimeout(() => {
        setError(null);
      }, 3000);
    }
  };


  const timeOptions = [
    "10:00 AM - 12:00 PM",
    "12:00 PM - 02:00 PM",
    "02:00 PM - 04:00 PM",
    "04:00 PM - 06:00 PM",
  ];

  return (
    <div>
      <DashboardLayout>
        <div className="addslot-form-container">
          <h2 className="addslot-h2">Add Slot</h2>
          <form className="addslot-form" onSubmit={handleSubmit}>
            <div className="addslot-form-group">
              <div className="addslot-label-text">
                <label className="addslot-label">Timing</label>
              </div>
              <FormControl fullWidth>
                <Select
                  name="timing"
                  value={formData.timing}
                  onChange={handleInputChange}
                  displayEmpty
                  required
                >
                  <MenuItem value="" disabled>
                    <em>Select Time Slot</em>
                  </MenuItem>
                  {timeOptions.map((time) => (
                    <MenuItem key={time} value={time}>
                      {time}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </div>
            <div className="addslot-form-group">
              <div className="addslot-label-text">
                <label className="addslot-label">Pincode</label>
              </div>
              <TextField
                className="addslot-text"
                type="text"
                name="pincode"
                value={managerPinCode}
                onChange={handleInputChange}
                fullWidth
                disabled
              />
            </div>
            <Button
              type="submit"
              className="addslot-submit-btn"
              variant="contained"
            >
              Add Slot
            </Button>
            {/* {success === 201 && (
              <div className="alert alert-success mt-3 text-center">
                {message || "Data updated successfully"}
              </div>
            )}
            {error === 401 && (
              <div className="alert alert-danger mt-3 text-center">
                Please login again
              </div>
            )}
            {error && error !== 401 && (
              <div className="alert alert-danger mt-3 text-center">{error}</div>
            )} */}
          </form>
        </div>
      </DashboardLayout>
    </div>
  );
}

export default AddSlot;

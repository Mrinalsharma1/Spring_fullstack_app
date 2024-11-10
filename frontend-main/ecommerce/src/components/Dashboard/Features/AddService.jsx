import React, { useState } from "react";
import axios from "axios";
import "./AddService.css";
import DashboardLayout from "../Layout/DashboardLayout";
import log from "loglevel";
import Swal from "sweetalert2";

import {
  Stack,
  Alert,
  FormControl,
  Select,
  MenuItem,
  Grid,
} from "@mui/material";
import { useSelector } from "react-redux";

function AddService() {
  const [formData, setFormData] = useState({
    pincode: "",
    name: "",
    address: "",
    open_from: "",
    open_to: "",
    city: "",
    state: "",
    service_type: "All",
    location: "",
    managerId: "",
    createdAt: "",
  });

  const [success, setSuccess] = useState("");
  const [error, setError] = useState("");

  const token = useSelector((state) => state.login.token);

  //console.log(token);

  // Update the form data state on change
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

    const NewCenterData = {
      pincode: formData.pincode,
      name: `${formData.pincode}_${formData.city}`,
      address: formData.address,
      open_from: formData.open_from,
      open_to: formData.open_to,
      city: formData.city,
      state: formData.state,
      service_type: "Repair",
      location: formData.location,
      managerId: formData.managerId,
      createdAt: new Date().toISOString(),
    };

    try {
      const response = await axios.post(
        "http://localhost:8071/addservicecenter",
        NewCenterData,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      setFormData({
        pincode: "",
        name: "",
        address: "",
        open_from: "",
        open_to: "",
        city: "",
        state: "",
        service_type: "",
        location: "",
        managerId: "",
        createdAt: "",
      });

      setSuccess(response.data.message);
      log.info("Service Center added successfully by:" + formData.pincode);

      // Show SweetAlert2 success message
      Swal.fire({
        icon: "success",
        title: "Service Center Added",
        text: response.data.message,
        timer: 3000,
        timerProgressBar: true,
        showConfirmButton: false,
      });

      // Show SweetAlert2 success message
      Swal.fire({
        icon: "success",
        title: "Service Center Added",
        text: response.data.message,
        timer: 3000,
        timerProgressBar: true,
        showConfirmButton: false,
      });

      setTimeout(() => {
        setSuccess("");
      }, 3000);
    } catch (error) {
      setError("Failed to add New Service-Center");
      log.info("Failed to add New Service-Center");

      // Show SweetAlert2 error message
      Swal.fire({
        icon: "error",
        title: "Failed to Add Service Center",
        text: "An error occurred while adding the service center. Please try again.",
      });

      // Show SweetAlert2 error message
      Swal.fire({
        icon: "error",
        title: "Failed to Add Service Center",
        text: "An error occurred while adding the service center. Please try again.",
      });

      setTimeout(() => {
        setError("");
      }, 3000);
    }
  };

  const timeOptions = [
    "01:00 AM",
    "02:00 AM",
    "03:00 AM",
    "04:00 AM",
    "05:00 AM",
    "06:00 AM",
    "07:00 AM",
    "08:00 AM",
    "09:00 AM",
    "10:00 AM",
    "11:00 AM",
    "12:00 PM",
    "01:00 PM",
    "02:00 PM",
    "03:00 PM",
    "04:00 PM",
    "05:00 PM",
    "06:00 PM",
    "07:00 PM",
    "08:00 PM",
    "09:00 PM",
    "10:00 PM",
    "11:00 PM",
    "12:00 AM",
  ];

  return (
    <div>
      <DashboardLayout>
        <div className="service-form-container">
          <h2 className="service-h2">Add a New Center</h2>
          <form className="service-center-form" onSubmit={handleSubmit}>
            <div className="service-form-group">
              <div className="service-label-text">
                <label className="service-label">Address</label>
              </div>
              <input
                className="service-text"
                type="text"
                name="address"
                value={formData.address}
                onChange={handleInputChange}
                required
              />
            </div>
            <div className="service-form-group">
              <div className="service-label-text">
                <label className="service-label">Open Hours</label>
              </div>
              <Grid container spacing={2}>
                <Grid item xs={6}>
                  <FormControl fullWidth>
                    <Select
                      name="open_from"
                      value={formData.open_from}
                      onChange={handleInputChange}
                      displayEmpty
                      required
                    >
                      <MenuItem value="" disabled>
                        <em>From</em>
                      </MenuItem>
                      {timeOptions.map((time) => (
                        <MenuItem key={time} value={time}>
                          {time}
                        </MenuItem>
                      ))}
                    </Select>
                  </FormControl>
                </Grid>
                <Grid item xs={6}>
                  <FormControl fullWidth>
                    <Select
                      name="open_to"
                      value={formData.open_to}
                      onChange={handleInputChange}
                      displayEmpty
                      required
                    >
                      <MenuItem value="" disabled>
                        <em>To</em>
                      </MenuItem>
                      {timeOptions.map((time) => (
                        <MenuItem key={time} value={time}>
                          {time}
                        </MenuItem>
                      ))}
                    </Select>
                  </FormControl>
                </Grid>
              </Grid>
            </div>
            <div className="service-form-group">
              <div className="service-label-text">
                <label className="service-label">City</label>
              </div>
              <input
                className="service-text"
                type="text"
                name="city"
                value={formData.city}
                onChange={handleInputChange}
                required
              />
            </div>
            <div className="service-form-group">
              <div className="service-label-text">
                <label className="service-label">State</label>
              </div>
              <input
                className="service-text"
                type="text"
                name="state"
                value={formData.state}
                onChange={handleInputChange}
                required
              />
            </div>
            <div className="service-form-group">
              <div className="service-label-text">
                <label className="service-label">Pincode</label>
              </div>
              <input
                className="service-text"
                type="text"
                name="pincode"
                value={formData.pincode}
                onChange={handleInputChange}
                required
              />
            </div>
            {/* <div className="service-form-group">
              <div className="service-label-text">
                <label className="service-label">Service Type</label>
              </div>
              <input
                className="service-text"
                type="text"
                name="service_type"
                value={formData.service_type}
                disabled


              />
            </div> */}
            <div className="service-form-group">
              <div className="service-label-text">
                <label className="service-label">Center Location</label>
              </div>
              <input
                className="service-text"
                type="text"
                name="location"
                value={formData.location}
                onChange={handleInputChange}
                required
              />
            </div>

            {success && (
              <Stack sx={{ width: "100%" }} spacing={2}>
                <Alert variant="filled" severity="success">
                  {success}
                </Alert>
              </Stack>
            )}
            {error && (
              <Stack sx={{ width: "100%" }} spacing={2}>
                <Alert variant="filled" severity="error">
                  {error}
                </Alert>
              </Stack>
            )}

            <button type="submit" className="service-submit-btn">
              Add Center
            </button>
          </form>
        </div>
      </DashboardLayout>
    </div>
  );
}

export default AddService;

import React, { useState, useEffect } from "react";
import axios from "axios";
import "./FormComponent.css";
import { FaCalendarAlt } from "react-icons/fa";
import { Link, useNavigate } from "react-router-dom";
import Header from "../header/Header";
import Navbar from "../header/Navbar";
import { FaAnglesRight } from "react-icons/fa6";
import Calendar from "react-calendar";
import "react-calendar/dist/Calendar.css";
import Footer from "../footer/Footer";
import AvailableSlots from "../availableslot/AvailableSlots";
import { useSelector } from "react-redux";
import Calender from "../availableslot/Calender/Calender";
import Swal from "sweetalert2";

const FormComponent = () => {
  // State for form data
  const [centers, setCenters] = useState([]);
  const [timeSlots, setTimeSlots] = useState([]);
  const [bookedSlots, setBookedSlots] = useState([]);
  const [showSuccessMessage, setShowSuccessMessage] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [suggestions, setSuggestions] = useState([]);
  const [citySuggestions, setCitySuggestions] = useState([]);
  const [stateSuggestions, setStateSuggestions] = useState([]);
  const [date, setDate] = useState(new Date());
  const [showCalendar, setShowCalendar] = useState(false);
  const [slotId, setSlotId] = useState("");

  const [formData, setFormData] = useState({
    product: "",
    purchasedFrom: "",
    serviceType: "",
    pincode: "",
    customerName: "",
    email: "",
    mobile: "",
    city: "",
    state: "",
    address: "",
    preferredDate: "",
    preferredTimeSlot: "",
    faultDescription: "",
  });

  // State for dropdown options
  const [products, setProducts] = useState([]);
  const serviceTypes = [
    {
      id: 1,
      type: "Repair",
    },
    {
      id: 2,
      type: "Maintenance",
    },
  ];

  const userId = useSelector((state) => state.login.id);
  const token = useSelector((state) => state.login.token);
  const userName = useSelector((state) => state.login.name);
  const userEmail = useSelector((state) => state.login.email);
  const navigate = useNavigate();

  // Fetch initial data for dropdowns

  // step 1
  const fetchAllCenters = async () => {
    try {
      await axios
        .get("http://localhost:8071/getallservicecenters", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })
        .then((response) => {
          setCenters(response.data.serviceCenters);
        })
        .catch((error) => {
          console.error("There was an error fetching the centers data!", error);
        });

      setTimeSlots(["", ""]);
    } catch (error) {
      console.error("Error fetching data:", error);
    }
    fetchProducts();
  };

  // step 2
  const fetchProducts = async () => {
    try {
      await axios
        .get("http://localhost:8074/product/getallproducts")
        .then((response) => {
          setProducts(response.data);
        })
        .catch((error) => {
          console.error("There was an error fetching the product data!", error);
        });

      setTimeSlots(["", ""]);
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  const fetchAllData = async () => {
    fetchAllCenters();
  };

  useEffect(() => {
    fetchAllData();
  }, []);
  // Handle form input changes
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });

    if (name === "city") {
      handleCityChange(value);
    } else if (name === "state") {
      handleStateChange(value);
    }
  };

  // Handle city input changes
  const handleCityChange = async (value) => {
    // setFormData({ ...formData, city: value });
    // if (value.length > 0) {
    //   try {
    //     const response = await axios.get(`${url}/address`);
    //     const allCities = response.data.map((address) => address.city);
    //     const filteredSuggestions = allCities.filter((city) =>
    //       city.toLowerCase().startsWith(value.toLowerCase())
    //     );
    //     setCitySuggestions(filteredSuggestions);
    //     // Fetch and update form data based on city
    //     const addressDetails = response.data.find(
    //       (address) => address.city.toLowerCase() === value.toLowerCase()
    //     );
    //     if (addressDetails) {
    //       setFormData((prevFormData) => ({
    //         ...prevFormData,
    //         city: addressDetails.city,
    //         state: addressDetails.state,
    //         pincode: addressDetails.pin,
    //         address: addressDetails.addressLine,
    //       }));
    //     }
    //   } catch (error) {
    //     console.error("Error fetching data:", error);
    //   }
    // } else {
    //   setCitySuggestions([]);
    // }
  };

  // Handle state input changes
  const handleStateChange = async (value) => {
    setFormData({ ...formData, state: value });

    if (value.length > 0) {
      const allStates = centers.map((address) => address.state);
      const filteredSuggestions = [
        ...new Set(
          allStates.filter((state) =>
            state.toLowerCase().startsWith(value.toLowerCase())
          )
        ),
      ];

      setStateSuggestions(filteredSuggestions);
    } else {
      setStateSuggestions([]);
    }
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

  // Handle date changes
  const handleDateChange = async (date) => {
    setDate(date);
    console.log("calender date is", formatDate(date));
    setSelectedDate(formatDate(date)); // this is the function to change date format
    fetchTimeSlots();

    // Enable time slots only if the selected date is available
    if (!isBooked(date)) {
      setIsSlotEnabled(true);
    } else {
      setIsSlotEnabled(false);
    }
    setShowCalendar(false);

    setFormData({ ...formData, preferredDate: date });
  };

  // Function to check if a date is booked
  const isBooked = (date) => {
    return bookedSlots.some(
      (bookedDate) => bookedDate.toDateString() === date.toDateString()
    );
  };
  // // Function to apply class based on booking status
  // const tileClassName = ({ date }) => {
  //   if (isBooked(date)) {
  //     return "booked";
  //   }
  //   return "available";
  // };

  // // Function to disable certain dates
  // const tileDisabled = ({ date }) => {
  //   const today = new Date();
  //   const tenDaysFromNow = new Date();
  //   tenDaysFromNow.setDate(today.getDate() + 10);

  //   // Disable dates outside the range of today to the next 10 days
  //   if (date < today || date > tenDaysFromNow) {
  //     return true;
  //   }

  //   // Disable Sundays
  //   if (date.getDay() === 0) {
  //     return true;
  //   }
  //   return false;
  // };

  // Handle time slots based on date
  // const handleTimeSlots = async (date) => {
  //   try {
  //     const response = await axios.get(`${url}/submissions`);
  //     const bookedSlotsForDate = response.data
  //       .filter(
  //         (submission) =>
  //           submission.preferredDate === date.toISOString().split("T")
  //       )
  //       .map((submission) => submission.preferredTimeSlot);

  //     setBookedSlots(bookedSlotsForDate);
  //   } catch (error) {
  //     console.error("Error fetching booked slots:", error);
  //   }
  // };

  // const handleSubmit = async (e) => {
  //   e.preventDefault();
  //   setIsSubmitting(true);
  //   const bookingObject = {
  //     bdate: selectedDate,
  //     status: "Confirmed",
  //     slot: {
  //       slotid: slotId
  //     },
  //     user: {
  //       id: userId
  //     }
  //   }
  //   console.log(bookingObject);
  //   try {
  //     await axios.post('http://localhost:8071/bookslot/' + userEmail, bookingObject, {
  //       headers: {
  //         'Authorization': `Bearer ${token}`,
  //         'Content-Type': 'application/json'
  //       },
  //     });
  //   } catch (error) {
  //     console.error("Error submitting form:", error);
  //   }
  // };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmitting(true);

    const bookingObject = {
      bdate: selectedDate,
      status: "Confirmed",
      slot: {
        slotid: slotId,
      },
      user: {
        id: userId,
      },
    };

    try {
      Swal.fire({
        title: "Please wait...",
        text: "Booking your slot...",
        showConfirmButton: false,
        allowOutsideClick: false,
      });
      const response = await axios.post(
        "http://localhost:8071/bookslot/" + userEmail,
        bookingObject,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }
      );

      setIsSubmitting(false); // Set submitting state to false after success

      await Swal.fire({
        icon: "success",
        title: "Booking Successful!",
        text:
          response.data.message || "Your slot has been booked successfully.",
      });
      navigate("/bookings");
    } catch (error) {
      setIsSubmitting(false); // Set submitting state to false after error

      console.error("Error submitting form:", error);

      if (error.response) {
        // Server-side error (e.g., 401 Unauthorized, 400 Bad Request)
        await Swal.fire({
          icon: "error",
          title: "Booking Failed",
          text:
            error.response.data.message ||
            "An error occurred while booking the slot.",
        });
      } else {
        // Network error or other client-side issue
        await Swal.fire({
          icon: "error",
          title: "Booking Failed",
          text: "There was a problem booking the slot. Please check your network connection or try again later.",
        });
      }
    }
  };
  // ----------------------------calendar code ------
  const [calendarData, setCalendarData] = useState(null);
  const [pincode, setPincode] = useState("");
  const [selectedDate, setSelectedDate] = useState(null);
  const [isSlotEnabled, setIsSlotEnabled] = useState(false);
  const [givenDateAllSlotTime, setGivenDateAllSlotTime] = useState([]);

  const handlePincodeUpdate = (e) => {
    const { value } = e.target;
    setPincode(value);

    if (value.length === 6) {
      apiCall(value);
    }
    const inputPincode = e.target.value;
    setFormData({ ...formData, pincode: inputPincode });

    const addressDetails = centers.find(
      (ele) => ele.pincode.toString() === inputPincode
    );

    if (addressDetails) {
      setFormData((prevFormData) => ({
        ...prevFormData,
        city: addressDetails.city,
        state: addressDetails.state,
        address: addressDetails.address,
        pincode: inputPincode,
      }));
    }
  };

  console.log("date of selected -----------------", selectedDate);

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

  // this is the function to change date format
  const formatDate = (date) => {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0"); // Months are 0-based, so add 1
    const day = String(date.getDate()).padStart(2, "0"); // Pad day with leading zero if needed
    return `${year}-${month}-${day}`;
  };

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

  const findSlotId = async (pincode, timing) => {
    try {
      const response = await axios.get(
        "http://localhost:8074/slot/find-slot-id",
        {
          params: {
            pincode: pincode,
            timing: timing,
          },
        }
      );
      setSlotId(response.data.slotId);
    } catch (error) {
      console.error("Error fetching slot ID:", error);
      throw error;
    }
  };

  useEffect(() => {
    console.log("Re render calls");
    if (selectedDate) {
      fetchTimeSlots();
    }
  }, [selectedDate]);

  return (
    <>
      <div className="bookingSlot-parent">
        <div>
          <Header />
          <Navbar />
          <div className="available_slot">
            <div className="available_slot_bg">
              <div className="slot_title">
                <h2 className="text-white"> Slot Booking Form </h2>
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
                  to="/availableslot"
                  className="text-white"
                  style={{ textDecoration: "none" }}
                >
                  Book Appointment
                </Link>
              </p>
            </div>
          </div>
        </div>

        <div className="container" style={{ padding: "40px" }}>
          {showSuccessMessage && (
            <div className="alert alert-success">
              Slot is booked successfully!!
            </div>
          )}
          <form onSubmit={handleSubmit}>
            <div className="row">
              <div className="col-md-6">
                <div className="form-group">
                  <label className="mb-2">
                    Product<span className="text-danger">*</span>
                  </label>
                  <select
                    className=" bookingSlot-form"
                    name="product"
                    value={formData.product}
                    onChange={handleChange}
                    required
                  >
                    <option defaultValue="select product">
                      Select Product
                    </option>
                    {products.length > 0 &&
                      products.map((product, index) => (
                        <option key={index} value={product.productname}>
                          {product.productname}
                        </option>
                      ))}
                  </select>
                </div>
              </div>
            </div>
            <div className="row">
              <div className="col-md-6">
                <div className="form-group">
                  <label className="mb-2">
                    Service Type<span className="text-danger">*</span>
                  </label>
                  <select
                    className="bookingSlot-form"
                    name="serviceType"
                    value={formData.serviceType}
                    onChange={handleChange}
                    required
                  >
                    <option value="">Select Service Type</option>
                    {serviceTypes.map((type) => (
                      <option key={type.id} value={type.type}>
                        {type.type}
                      </option>
                    ))}
                  </select>
                </div>
              </div>

              <div className="col-md-6">
                <div className="form-group">
                  <label className="mb-2">
                    Pincode<span className="text-danger">*</span>
                  </label>
                  <input
                    type="text"
                    className="bookingSlot-form"
                    name="pincode"
                    onChange={handlePincodeUpdate}
                    onWheel={(e) => e.target.blur()}
                    required
                    list="pincodeSuggestions"
                  />
                  <datalist id="pincodeSuggestions">
                    {suggestions.map((suggestion) => (
                      <option key={suggestion} value={suggestion} />
                    ))}
                  </datalist>
                </div>
              </div>
            </div>
            <div className="row">
              <div className="col-md-6">
                <div className="form-group">
                  <label className="mb-2">
                    Customer Name<span className="text-danger">*</span>
                  </label>
                  <input
                    type="text"
                    className="bookingSlot-form"
                    name="customerName"
                    value={userName}
                    onChange={handleChange}
                    required
                  />
                </div>
              </div>
              <div className="col-md-6">
                <div className="form-group">
                  <label className="mb-2">
                    Email Address<span className="text-danger">*</span>
                  </label>
                  <input
                    type="email"
                    className="bookingSlot-form"
                    name="email"
                    value={userEmail}
                    onChange={handleChange}
                    required
                  />
                </div>
              </div>
            </div>
            {/* <div className="row">
              <div className="col-md-6">
                <div className="form-group">
                  <label className="mb-2">
                    Mobile<span className="text-danger">*</span>
                  </label>
                  <input
                    type="text"
                    className="bookingSlot-form"
                    name="mobile"
                    value={formData.mobile}
                    onChange={handleChange}
                    required
                  />
                </div>
              </div>
              <div className="col-md-6">
                <div className="form-group">
                  <label className="mb-2">
                    State<span className="text-danger">*</span>
                  </label>
                  <input
                    type="text"
                    className="bookingSlot-form"
                    name="state"
                    value={formData.state}
                    onChange={handleChange}
                    required
                    list="stateSuggestions"
                  />
                  <datalist id="stateSuggestions">
                    {stateSuggestions.map((suggestion) => (
                      <option key={suggestion} value={suggestion} />
                    ))}
                  </datalist>
                </div>
              </div>
            </div> */}
            <div className="row">
              <div className="col-md-6">
                <div className="form-group">
                  <label className="mb-2">
                    City<span className="text-danger">*</span>
                  </label>
                  <input
                    type="text"
                    className="bookingSlot-form"
                    name="city"
                    value={formData.city}
                    onChange={handleChange}
                    required
                    list="citySuggestions"
                  />
                  <datalist id="citySuggestions">
                    {citySuggestions.map((suggestion) => (
                      <option key={suggestion} value={suggestion} />
                    ))}
                  </datalist>
                </div>
              </div>
              <div className="col-md-6">
                <div className="form-group">
                  <label className="mb-2">
                    Service Center Address<span className="text-danger">*</span>
                  </label>
                  <input
                    type="text"
                    className="bookingSlot-form"
                    name="address"
                    value={formData.address}
                    readOnly
                    required
                  />
                </div>
              </div>
            </div>
            <div className="row">
              <div className="col-md-6">
                <div className="form-group">
                  <label className="mb-2">
                    Preferred Date<span className="text-danger">*</span>
                  </label>
                  <div className="date-picker-wrapper">
                    <input
                      type="text"
                      className="bookingSlot-form"
                      placeholder="Select Preferred Date"
                      value={date.toDateString()} // Display selected date
                      readOnly // Make the input read-only to avoid typing
                      onClick={() => setShowCalendar(!showCalendar)} // Toggle calendar on input click
                    />
                    <FaCalendarAlt
                      className="calendar-icon"
                      onClick={() => setShowCalendar(!showCalendar)} // Toggle calendar on icon click
                    />
                    {showCalendar && (
                      <Calender
                        bookedSlots={calendarData}
                        handleDateChange={handleDateChange}
                      />
                    )}
                  </div>
                </div>
              </div>
              <div className="col-md-6">
                <div className="form-group">
                  <label className="mb-2">
                    Available Slot<span className="text-danger">*</span>
                  </label>
                  <select
                    className="bookingSlot-form"
                    name="preferredTimeSlot"
                    value={formData.preferredTimeSlot}
                    onChange={(event) => {
                      handleChange(event);
                      findSlotId(formData.pincode, event.target.value);
                    }}
                    disabled={!isSlotEnabled}
                    required
                  >
                    <option value="">Select Time Slot</option>
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
            {/* <div>
              <div className="fault-description-text">
                <div className="form-group ">
                  <label className="mb-2">
                    Fault Description<span className="text-danger">*</span>
                  </label>
                  <textarea
                    className={"fault-description-textarea"}
                    name="faultDescription"
                    value={formData.faultDescription}
                    onChange={handleChange}
                    required
                  />
                </div>
              </div>
            </div> */}
            <div className="d-flex justify-content-center">
              <button
                className="btn bookingSlot-button mt-1"
                type="submit"
                disabled={isSubmitting}
              >
                Submit
              </button>
            </div>
          </form>
        </div>
        <Footer />
      </div>
    </>
  );
};

export default FormComponent;

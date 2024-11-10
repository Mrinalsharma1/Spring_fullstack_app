import React, { useEffect, useState } from "react";
import DashboardLayout from "./Layout/DashboardLayout";
import { useNavigate } from "react-router-dom";
import axios from "axios";

import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";
import { useSelector } from "react-redux";
import BarChart from "./BarChart";
import { logout } from "../../redux/userSlice";
import { useDispatch } from "react-redux";

// Register Chart.js components
ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

const Dashboard = ({ children }) => {
  //console.log("hey i am in dashboard");

  const userid = useSelector((state) => state.login.id);
  const token = useSelector((state) => state.login.token);
  const role = useSelector((state) => state.login.role);

  const [userCount, setUserCount] = useState("");
  const [pinCode, SetPinCode] = useState("");
  const [serviceCount, setServiceCount] = useState("");
  const [feedbackCount, setFeedbackCount] = useState("");
  const [managerCount, setManagerCount] = useState("");
  const [centerCount, setCenterCount] = useState("");
  const [successfulServices, setSuccessfulServicesCount] = useState("");
  const [error, setError] = useState(false);
  const navigate = useNavigate();

  // if token expire it will logout
  const dispatch = useDispatch();
  const handleLogout = () => {
    dispatch(logout());
    navigate("/");
  };

  if (error.response && error.response.status === 401) {
    handleLogout();
  }

  // function to find pincode
  const fetchPin = async () => {
    try {
      const pincodeResponse = await axios.get(
        `http://localhost:8071/pincode/${userid}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      console.log("hey error", error.status);

      console.log("response is", pincodeResponse);

      SetPinCode(pincodeResponse.data.pincode);
    } catch (e) {}
  };

  // function to fetch service count
  const fetchServiceCount = async () => {
    try {
      const response = await axios.get(
        `http://localhost:8071/completed/count?pincode=${pinCode}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (response.data.status === "success") {
        setServiceCount(response.data.count);
        setError(false);
      } else {
        setServiceCount(0);
        setError(true);
      }
    } catch (error) {}
  };

  // function to fetch feedback count
  const fetchFeedbackCount = async () => {
    try {
      const response = await axios.get(
        `http://localhost:8071/feedback/count?pincode=${pinCode}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      console.log(response.data);
      if (response.data.status === "success") {
        setFeedbackCount(response.data.count);
        setError(false);
      } else {
        setServiceCount(0);
        setError(true);
      }
    } catch (error) {}
  };

  // function to fetch usercount
  const fetchUserCount = async () => {
    try {
      const response = await axios.get(`http://localhost:8071/users/count`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (response.data.status === "success") {
        setUserCount(response.data.count);
        setError(false);
      } else {
        setServiceCount(0);

        setError(true);
      }
    } catch (error) {}
  };
  // function to fetch manager count
  const fetchManagerCount = async () => {
    try {
      const response = await axios.get(`http://localhost:8071/managers/count`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (response.data.status === "success") {
        setManagerCount(response.data.count);
        setError(false);
      } else {
        setServiceCount(0);

        setError(true);
      }
    } catch (error) {}
  };
  // function to fetch center count
  const fetchCenterCount = async () => {
    try {
      const response = await axios.get(
        `http://localhost:8071/servicecenters/count`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (response.data.status === "success") {
        setCenterCount(response.data.count);
        setError(false);
      } else {
        setServiceCount(0);

        setError(true);
      }
    } catch (error) {}
  };
  // function to fetch total services count
  const fetchSuccessfulServicesCount = async () => {
    try {
      const response = await axios.get(
        `http://localhost:8071/bookings/count/completed`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (response.data.status === "success") {
        setSuccessfulServicesCount(response.data.count);
        setError(false);
      } else {
        setServiceCount(0);

        setError(true);
      }
    } catch (error) {}
  };

  useEffect(() => {
    fetchPin();
    fetchUserCount();
    fetchManagerCount();
    fetchCenterCount();
    fetchSuccessfulServicesCount();
    if (pinCode) {
      fetchServiceCount();
      fetchFeedbackCount();
    }
  }, [pinCode]);

  // Function to render the main content based on active state
  const getContent = () => {
    return (
      <>
        <div className="dashboard-content">
          <div className="cards">
            <div className="card-single">
              {/* first card for admin and user */}
              {role === "admin" && (
                <div>
                  <h1>{userCount}</h1>
                  <span>Total Users</span>
                </div>
              )}
              {role === "manager" && (
                <div>
                  <h1>{serviceCount}</h1>
                  <span>Completed Services</span>
                </div>
              )}
              <div>
                <span className="las la-users"></span>
              </div>
            </div>

            <div className="card-single">
              {/* second card for admin and user */}
              {role === "admin" && (
                <div>
                  <h1>{managerCount}</h1>
                  <span>Total Managers</span>
                </div>
              )}
              {role === "manager" && (
                <div style={{ cursor: "pointer" }}>
                  <h1>6</h1>
                  <span>Service Types </span>
                </div>
              )}
              <div>
                <span className="las la-clipboard-list"></span>
              </div>
            </div>

            <div className="card-single">
              {/* second card for admin and user */}

              {role === "admin" && (
                <div>
                  <h1>{centerCount}</h1>
                  <span>Total Service Centers</span>
                </div>
              )}
              {role === "manager" && (
                <div>
                  <h1>4</h1>
                  <span>Number of slots</span>
                </div>
              )}
              <div>
                <span className="las la-shopping-bag"></span>
              </div>
            </div>
            <div className="card-single">
              {/* second card for admin and user */}
              {role === "admin" && (
                <div>
                  <h1>{successfulServices}</h1>
                  <span>Successful Services</span>
                </div>
              )}
              {role === "manager" && (
                <div
                  style={{ cursor: "pointer" }}
                  onClick={() => {
                    navigate("/feedback");
                  }}
                >
                  {role === "admin" ? <h1>$2k</h1> : <h1>{feedbackCount}</h1>}
                  <span>Total Feedbacks</span>
                </div>
              )}
              <div>
                <span className="lab la-google-wallet"></span>
              </div>
            </div>
          </div>
        </div>

        {role === "admin" && (
          <div className="container">
            <div className="row pt-4 ">
              <h3>Weekly Bookings</h3>
              <div className="col-md-8 mx-auto">
                <BarChart />
              </div>
            </div>
          </div>
        )}
      </>
    );
  };

  return <DashboardLayout>{getContent()}</DashboardLayout>;
};

export default Dashboard;

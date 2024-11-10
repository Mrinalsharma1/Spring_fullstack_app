import React, { useEffect, useState } from "react";
import DashboardLayout from "../Layout/DashboardLayout";
import "./AddManager.css";
import axios from "axios";
import { SEARCH_USER_BY_EMAIL } from "../../../globalData/constEndPoints";
import { useSelector } from "react-redux";
import Swal from "sweetalert2";

function AddManager() {
  const [username, setUsername] = useState("");
  const [pincode, setPinCode] = useState("");
  const [message, setMessage] = useState("");
  const [status, setStatus] = useState("");
  const token = useSelector((state) => state.login.token);

  const [user, setUser] = useState({
    id: "",
    profilename: "",
    username: "",
    password: "",
    role: "",
    reset_token: null,
    reset_token_expiry: null,
  });
  const [centers, setCenters] = useState([]);

  // step 1 - to search centers
  const fetchCenters = async () => {
    try {
      const response = await axios.get("http://localhost:8071/no-manager", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setCenters(response.data.serviceCenters);
    } catch (error) {
      console.error("Error fetching centers:", error);
    }
  };

  useEffect(() => {
    fetchCenters();
  }, []);

  // step 2 - search users
  const handleSearchUser = async () => {
    try {
      const response = await axios.get(SEARCH_USER_BY_EMAIL + "/" + username, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      setUser(response.data.user);
      setStatus("found user");
      setMessage("Found user with corresponding mail");
      setTimeout(() => {
        setStatus("");
        setMessage("");
      }, 3000);
    } catch (error) {
      setStatus("no user found");
      setMessage("Could not find manager. Please try again ");
      console.error("Error:", error);
      setTimeout(() => {
        setStatus("");
        setMessage("");
      }, 3000);
    }
  };

  const changeRole = (newRole) => {
    if (user) {
      setUser({ ...user, role: newRole });
    }
  };
  const handleSubmit = async () => {
    try {
      // Show loader
      Swal.fire({
        title: "Please wait...",
        text: "Processing your request",
        allowOutsideClick: false,
        didOpen: () => {
          Swal.showLoading();
        },
      });

      const response1 = await axios.put(
        `http://localhost:8071/changeRole/${user.id}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      const response2 = await axios.put(
        `http://localhost:8071/assign-manager/${pincode}/${user.id}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      console.log(response1.data.status);
      console.log(response2.data.status);

      // Close loader
      Swal.close();

      if (response2.data.status === "success") {
        Swal.fire({
          icon: "success",
          title: "Success",
          text: "Assigned manager successfully!",
          timer: 3000,
          showConfirmButton: false,
        });
        setStatus("passed");
        setTimeout(() => {
          setStatus("");
        }, 3000);
      }
      fetchCenters();
    } catch (error) {
      // Close loader
      Swal.close();

      Swal.fire({
        icon: "error",
        title: "Error",
        text: "Could not assign manager. Please try again",
        timer: 3000,
        showConfirmButton: false,
      });
      setStatus("failed");
      console.error("Error:", error);
      setTimeout(() => {
        setStatus("");
      }, 3000);
    }
  };

  return (
    <div>
      <DashboardLayout>
        <div className="addManagerComponent">
          <h2>Add Manager</h2>
          <div className="search-user">
            <label>Enter email of the manager:</label>
            <input
              type="text"
              className="search-user-input"
              placeholder="Enter email"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
            <button className="search-user-button" onClick={handleSearchUser}>
              Search
            </button>
            {status === "no user found" && (
              <div className="alert alert-danger mt-3 text-center">
                {message}
              </div>
            )}
            {status === "found user" && (
              <div className="alert alert-success mt-3 text-center">
                {message}
              </div>
            )}
          </div>
          {user && (
            <div className="manager-info">
              <table>
                <thead>
                  <tr>
                    <th className="th-id">ID</th>
                    <th className="th-name">Name</th>
                    <th className="th-email">Email</th>
                    <th className="th-role">Role</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td className="td-id">{user.id}</td>
                    <td className="td-name">{user.profilename}</td>
                    <td className="td-email">{user.username}</td>
                    <td className="td-role">
                      <button
                        className={
                          user.role === "user"
                            ? "btn btn-success"
                            : "btn btn-secondary"
                        }
                        onClick={() => changeRole("user")}
                      >
                        User
                      </button>
                      <button
                        className={
                          user.role === "manager"
                            ? "btn btn-success"
                            : "btn btn-secondary"
                        }
                        onClick={() => changeRole("manager")}
                      >
                        Manager
                      </button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          )}
          <div className="select-center">
            <label htmlFor="pincode-dropdown">Select Pincode:</label>
            <select
              id="pincode-dropdown"
              onChange={(e) => {
                setPinCode(e.target.value);
              }}
            >
              <option value="">Select a Pincode</option>
              {centers.length > 0 &&
                centers.map((center, index) => (
                  <option key={index} value={center.pincode}>
                    {center.pincode}
                  </option>
                ))}
            </select>
          </div>
          <button onClick={handleSubmit} className="addmanager-submit-button">
            Save
          </button>
        </div>
      </DashboardLayout>
    </div>
  );
}

export default AddManager;

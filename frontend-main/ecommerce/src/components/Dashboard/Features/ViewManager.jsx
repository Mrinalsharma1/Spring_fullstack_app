import React, { useEffect, useState } from "react";
import DashboardLayout from "../Layout/DashboardLayout";
import "./ViewManager.css";
import Swal from "sweetalert2";
import empty from "../../../assets/images/empty.svg";
import loadingdata from "../../../assets/images/loadingdata.svg";
import axios from "axios";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
function ViewManager() {
  const token = useSelector((state) => state.login.token);

  const [managerList, setManagerList] = useState([]);

  const [disableToggleIndex, setDisableToggleIndex] = useState(null);

  useEffect(() => {
    fetchManagers();
  }, []);

  const navigate = useNavigate();

  const fetchManagers = async () => {
    try {
      const response = await axios.get(
        "http://localhost:8071/user-service-center-details",
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      setManagerList(response.data.data);
      console.log(response.data.data[0].pincode);
    } catch (error) {}
  };

  // step2
  const handleUnassignClick = async (pincode) => {
    console.log(typeof pincode);
    console.log(pincode);
    try {
      const response = await axios.delete(
        "http://localhost:8071/detach-manager/" + pincode,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      // setManagerList(response.data.data);
      fetchManagers();
      console.log(response.status);
    } catch (error) {
      console.log("error in unassigning" + error);
    }
  };
  const handleAssignClick = () => {
    navigate("/addmanager");
  };

  return (
    <div>
      <DashboardLayout>
        <div className="viewManagerComponent table-responsive">
          {managerList.length > 0 && <h2>Manager Details</h2>}
          {managerList.length === 0 && <h2>Data Not Found</h2>}

          <div className="manager-details">
            {managerList.length === 0 && (
              <img className="empty-img" src={empty} alt="No Data Found" />
            )}
            {managerList.length > 0 && (
              <table className="table  table-hover">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Center Name</th>
                    <th>City</th>
                    <th>State</th>
                    <th className="centered-row">Action</th>
                  </tr>
                </thead>
                <tbody>
                  {managerList.length > 0 &&
                    managerList.map((manager, index) => (
                      <tr
                        key={index}
                        className={
                          manager.address ? "row-assigned" : "row-unassigned"
                        }
                      >
                        <td>{manager.id.toString()}</td>
                        <td>{manager.profileName}</td>

                        <td>
                          {manager.address ? (
                            manager.address
                          ) : (
                            <p className="not-assigned">Not Assigned</p>
                          )}
                        </td>
                        <td>
                          {manager.city ? (
                            manager.city
                          ) : (
                            <p className="not-assigned">Not Assigned </p>
                          )}
                        </td>
                        <td>
                          {manager.state + disableToggleIndex ? (
                            manager.state
                          ) : (
                            <p className="not-assigned" onClick={() => {}}>
                              Not Assigned{" "}
                            </p>
                          )}
                        </td>

                        {/* <td className="centered-row">{
                      manager.pincode === 0 ? <button className="manager-assign-btn" onClick={() => {
                        handleUnassignClick(manager.id);
                      }}>Assign</button>
                        :
                        <button className="manager-unassign-btn" onClick={() => {
                          handleUnassignClick(manager.pincode)
                        }}>Unassign</button>
                    }

                    </td> */}
                        <td
                        // className="centered-row"
                        >
                          {manager.pincode === 0 ? (
                            <button
                              className="manager-assign-btn"
                              onClick={() => {
                                Swal.fire({
                                  title: "Assign branch",
                                  text: "You will be re-routed to a different page. Do you want to proceed?",
                                  icon: "question",
                                  showCancelButton: true,
                                  confirmButtonColor: "#3085d6",
                                  cancelButtonColor: "#d33",
                                  confirmButtonText: "Yes",
                                }).then((result) => {
                                  if (result.isConfirmed) {
                                    handleAssignClick(manager.id); // Assuming assign uses manager.id
                                  }
                                });
                              }}
                            >
                              Assign
                            </button>
                          ) : (
                            <button
                              className="manager-unassign-btn"
                              onClick={() => {
                                Swal.fire({
                                  title: "Are you sure?",
                                  text: "Do you want to unassign this manager?",
                                  icon: "question",
                                  showCancelButton: true,
                                  confirmButtonColor: "#3085d6",
                                  cancelButtonColor: "#d33",
                                  confirmButtonText: "Yes, unassign!",
                                }).then((result) => {
                                  if (result.isConfirmed) {
                                    handleUnassignClick(manager.pincode); // Assuming unassign uses manager.pincode
                                    Swal.fire(
                                      "Unassigned!",
                                      "The manager has been unassigned.",
                                      "success"
                                    );
                                  }
                                });
                              }}
                            >
                              Unassign
                            </button>
                          )}
                        </td>
                      </tr>
                    ))}
                </tbody>
              </table>
            )}
          </div>
        </div>
      </DashboardLayout>
    </div>
  );
}

export default ViewManager;

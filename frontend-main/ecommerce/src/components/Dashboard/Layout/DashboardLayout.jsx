import React, { useState, useEffect } from "react";
import "./Dashboard.css";
import MainBody from "../MainBody";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { logout } from "../../../redux/userSlice";
import { FaPersonWalkingDashedLineArrowRight } from "react-icons/fa6";

const DashboardLayout = ({ children }) => {
  // const [active, setActive] = useState("dashboard");
  const [urlActive, setUrlActive] = useState("");
  const location = useLocation();
  const navigate = useNavigate();

  const dispatch = useDispatch();
  const role = useSelector((state) => state.login.role);
  //console.log("layout", role);

  const handleLogout = () => {
    dispatch(logout());
    navigate("/");
  };

  useEffect(() => {
    const pathname = location.pathname;
    const pathParts = pathname.split("/");
    const dashboardPath = pathParts[pathParts.length - 1];
    setUrlActive(dashboardPath);
  }, [location]);

  return (
    <>
      <input type="checkbox" id="nav-toggle" />
      <div className="sidebar">
        <div className="sidebar-brand">
          <h2>
            <span className="lab la-accusoft"></span>
            <span>SwiftQSolution</span>
          </h2>
        </div>

        <div className="sidebar-menu">
          <ul>
            <li>
              <Link
                to="/dashboard"
                id="dashboard"
                className={urlActive === "dashboard" ? "active" : ""}
              >
                <span className="las la-igloo"></span>
                <span>Dashboard</span>
              </Link>
            </li>

            {role === "manager" ? (
              <li>
                <Link
                  to="/userbookings"
                  className={urlActive === "userbookings" ? "active" : ""}
                >
                  <span className="las la-users"></span>
                  <span>My Bookings</span>
                </Link>
              </li>
            ) : (
              ""
            )}

            {role === "admin" ? (
              <li>
                <Link
                  to="/addservicecenter"
                  className={urlActive === "addservicecenter" ? "active" : ""}
                >
                  <span className="las fa-square-plus"></span>
                  <span>Add Service</span>
                </Link>
              </li>
            ) : (
              ""
            )}

            {role === "manager" ? (
              <li>
                <Link
                  to="/addslot"
                  className={urlActive === "addslot" ? "active" : ""}
                >
                  <span className="las fa-check-to-slot"></span>
                  <span>Add Slot</span>
                </Link>
              </li>
            ) : (
              ""
            )}

            {role === "manager" ? (
              <li>
                <Link
                  to="/viewslot"
                  className={urlActive === "viewslot" ? "active" : ""}
                >
                  <span className="las fa-street-view"></span>
                  <span>View Slot</span>
                </Link>
              </li>
            ) : (
              ""
            )}

            {role === "manager" ? (
              <li>
                <Link
                  to="/feedback"
                  className={urlActive === "feedback" ? "active" : ""}
                >
                  <span className="las fa-comment"></span>
                  <span>Feedbacks</span>
                </Link>
              </li>
            ) : (
              ""
            )}

            {role === "admin" ? (
              <li>
                <Link
                  to="/announcement"
                  id="viewblog"
                  className={urlActive === "announcement" ? "active" : ""}
                >
                  <span className="las fa-bullhorn"></span>
                  <span>Announcement</span>
                </Link>
              </li>
            ) : (
              ""
            )}

            {role === "admin" ? (
              <li>
                <Link
                  to="/addmanager"
                  id="viewblog"
                  className={urlActive === "addmanager" ? "active" : ""}
                >
                  <span className="las fa-user-plus"></span>
                  <span>Add Manager</span>
                </Link>
              </li>
            ) : (
              ""
            )}

            {role === "admin" ? (
              <li>
                <Link
                  to="/viewmanager"
                  id="viewblog"
                  className={urlActive === "viewmanager" ? "active" : ""}
                >
                  <span className="las fa-street-view"></span>
                  <span>View Manager</span>
                </Link>
              </li>
            ) : (
              ""
            )}

            {role === "admin" ? (
              <li>
                <Link
                  to="/enquiries"
                  id="viewblog"
                  className={urlActive === "enquiries" ? "active" : ""}
                >
                  <span className="las la-clipboard-list"></span>
                  <span>Enquiries</span>
                </Link>
              </li>
            ) : (
              ""
            )}

            <li>
              <Link
                to="/accounts"
                id="accounts"
                className={urlActive === "accounts" ? "active" : ""}
              >
                <span className="las la-user-circle"></span>
                <span>Accounts</span>
              </Link>
            </li>

            {role === "manager" ? (
              <li>
                <Link
                  to="/help"
                  id="help"
                  className={urlActive === "help" ? "active" : ""}
                >
                  <span className="las la-clipboard-list"></span>
                  <span>Help</span>
                </Link>
              </li>
            ) : (
              ""
            )}

            <li>
              <div
                id="logout"
                // className={active === "logout" ? "active" : ""}
                onClick={handleLogout}
                className="sidebar_logout_btn"
              >
                <span className="las">
                  <FaPersonWalkingDashedLineArrowRight
                    style={{ fontSize: "23px" }}
                  />
                </span>
                <span className="text-light">Logout</span>
              </div>
            </li>
          </ul>
        </div>
      </div>
      <MainBody>{children}</MainBody>
    </>
  );
};

export default DashboardLayout;

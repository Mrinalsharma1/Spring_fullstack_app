import React from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { RxHamburgerMenu } from "react-icons/rx";
import "./Navbar.css";
import { useSelector } from "react-redux";
import UserAvatar from "../avatar/UserAvatar";
import { logout } from "../../redux/userSlice";
import { useDispatch } from "react-redux";

function Navbar() {
  const user_name = useSelector((state) => state.login.name);
  const isLoggedIn = useSelector((state) => state.login.isLoggedIn);
  const role = useSelector((state) => state.login.role);

  const location = useLocation();
  const currentPath = location.pathname;
  //console.log(currentPath);

  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleLogout = () => {
    dispatch(logout());
    console.log("hey i am logout");

    navigate("/");
  };

  const isActive = (path) => {
    return currentPath === path ? "nav-link-custom" : "";
  };

  return (
    <nav className="navbar navbar-expand-lg ">
      <div className="container-fluid">
        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarSupportedContent"
          aria-controls="navbarSupportedContent"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="">
            <RxHamburgerMenu className="text-light" />
          </span>
        </button>
        <div className="collapse navbar-collapse" id="navbarSupportedContent">
          <ul className="navbar-nav me-auto mb-2 mb-lg-0">
            <li className="nav-item">
              <Link to="/" className={`nav-link ${isActive("/")}`}>
                HOME
              </Link>
            </li>
            <li className="nav-item">
              <Link
                to="/ourservices"
                className={`nav-link ${isActive("/ourservices")}`}
              >
                SERVICES
              </Link>
            </li>
            <li className="nav-item">
              <Link to="/about" className={`nav-link ${isActive("/about")}`}>
                ABOUT
              </Link>
            </li>
            <li className="nav-item">
              <Link
                to="/servicecenter"
                className={`nav-link ${isActive("/servicecenter")}`}
              >
                SERVICE CENTER
              </Link>
            </li>
            <li className="nav-item">
              <Link
                to="/availableslot"
                className={`nav-link ${isActive("/availableslot")}`}
              >
                AVAILABLE SLOTS
              </Link>
            </li>

            {isLoggedIn && role === "user" && (
              <li className="nav-item ">
                <Link
                  to="/bookappointment"
                  className={`nav-link ${isActive("/bookappointment")}`}
                >
                  Book Appointment
                </Link>
              </li>
            )}

            <li className="nav-item ">
              {true && (
                <Link
                  to="/contact"
                  className={`nav-link ${isActive("/contact")}`}
                >
                  CONTACT
                </Link>
              )}
            </li>
          </ul>
        </div>
        {!isLoggedIn && (
          <div>
            <div className="nav_main_btn">
              <Link to="/signup" className="signup_btn me-2">
                Signup
              </Link>
              <Link to="/login" className="login_btn">
                Login
              </Link>
            </div>
          </div>
        )}
        {isLoggedIn ? (
          role === "user" ? (
            <UserAvatar user={user_name} />
          ) : role === "admin" || role === "manager" ? (
            <div>
              <div className="nav_main_btn">
                <Link onClick={handleLogout} className="signup_btn me-2">
                  Logout
                </Link>
              </div>
            </div>
          ) : null
        ) : null}
      </div>
    </nav>
  );
}

export default Navbar;

import React from "react";
import Avatar from "@mui/material/Avatar";
import { styled } from "@mui/material/styles";
import Badge from "@mui/material/Badge";
import { Link, useNavigate } from "react-router-dom";
import "./UserAvatar.css";
import { useDispatch } from "react-redux";
import { logout } from "../../redux/userSlice";

function UserAvatar(props) {
  const { user } = props;
  const initial = user.charAt(0).toUpperCase();
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const StyledBadge = styled(Badge)(({ theme }) => ({
    "& .MuiBadge-badge": {
      backgroundColor: "#44b700",
      color: "#44b700",
      boxShadow: `0 0 0 2px ${theme.palette.background.paper}`,
      "&::after": {
        position: "absolute",
        top: 0,
        left: 0,
        width: "100%",
        height: "100%",
        borderRadius: "50%",
        animation: "ripple 1.2s infinite ease-in-out",
        border: "1px solid currentColor",
        content: '""',
      },
    },
    "@keyframes ripple": {
      "0%": {
        transform: "scale(.8)",
        opacity: 1,
      },
      "100%": {
        transform: "scale(2.4)",
        opacity: 0,
      },
    },
  }));

  const handleLogout = async () => {
    await dispatch(logout());
    console.log("HO GAYA LOGOUT");
    navigate("/");
  };

  return (
    <div>
      <StyledBadge
        overlap="circular"
        anchorOrigin={{ vertical: "bottom", horizontal: "right" }}
        variant="dot"
      >
        <div className="btn-group  myAvatar">
          <Avatar
            sx={{
              backgroundColor: "primary.main",
              color: "secondary.contrastText",
            }}
            data-bs-toggle="dropdown"
            aria-expanded="false"
          >
            {initial}
          </Avatar>

          <ul className="dropdown-menu dropdown-menu-end mt-3  ">
            <li>
              <Link to="/profile" className="dropdown-item" href="#">
                <i className="fa-duotone fa-solid fa-user"></i>My Profile
              </Link>
            </li>
            <div className="separator-line"></div>
            <li>
              <Link to="/bookings" className="dropdown-item" href="#">
                <i className="fa-solid fa-screwdriver-wrench"></i>My Booking
              </Link>
            </li>
            <div className="separator-line"></div>
            <li>
              <Link className="dropdown-item" href="#" onClick={handleLogout}>
                <i className="fa-solid fa-right-from-bracket"></i>Logout
              </Link>
            </li>
          </ul>
        </div>
      </StyledBadge>
    </div>
  );
}

export default UserAvatar;

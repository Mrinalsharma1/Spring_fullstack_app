import React from "react";
import { useSelector } from "react-redux";
import Avatar from "@mui/material/Avatar";
import { Stack } from "@mui/material";

// import { deepOrange, deepPurple } from "@mui/material/colors";
import Badge from "@mui/material/Badge";
import { styled } from "@mui/material/styles";

function MainBody({ children }) {
  const user = useSelector((state) => state.login);

  function getInitials(name) {
    return name
      ?.split(" ")
      ?.map((word) => word[0].toUpperCase())
      ?.join("");
  }

  // mui code for profile icons
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

  return (
    <div>
      <div className="main-content">
        <header>
          <h2>
            <label htmlFor="nav-toggle">
              <span className="las la-bars"></span>
            </label>
            Dashboard
          </h2>
          {/* <div className="search-wrapper1">
            <span className="las la-search"></span>
            <input type="search" placeholder="Search here" />
            
          </div> */}
          <div className="user-wrapper">
            <div className="d-flex">
              {/* <p className="p-2">{user.name}</p> */}
              <StyledBadge
                overlap="circular"
                anchorOrigin={{ vertical: "bottom", horizontal: "right" }}
                variant="dot"
              >
                <Stack direction="row" spacing={2}>
                  <Avatar sx={{ bgcolor: "#735DA5" }}>
                    {getInitials(user.name)}
                  </Avatar>
                </Stack>
              </StyledBadge>
            </div>
          </div>
        </header>
        <main>{children}</main>
      </div>
    </div>
  );
}

export default MainBody;

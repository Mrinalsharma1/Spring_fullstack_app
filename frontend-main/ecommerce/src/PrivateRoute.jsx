import React from "react";
import { Navigate } from "react-router-dom";
import { useSelector } from "react-redux";

const PrivateRoute = ({ children }) => {
  const reduxdata = useSelector((state) => state.login);
  const isLoggedIn = reduxdata.isLoggedIn;
  const role = reduxdata.role;

  return isLoggedIn && (role === "admin" || role === "manager") ? (
    children
  ) : (
    <Navigate to="/" />
  );
};

export default PrivateRoute;

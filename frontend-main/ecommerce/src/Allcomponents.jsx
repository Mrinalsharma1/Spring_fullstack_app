import React, { useEffect, useState } from "react";
import Home from "./pages/Home";
import Login from "./components/auth/Login";
import SignUp from "./components/auth/SignUp";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import "../src/assets/styles/style.css";
import Locations from "./components/servicecenter/Locations";
import AvailableSlots from "./components/availableslot/AvailableSlots";
import Contact from "../src/components/contact/Contact";
import PrivateRoute from "./PrivateRoute";
import Dashboard from "./components/Dashboard/Dashboard";
import Temp from "./Temp";
import OurServices from "./components/services/OurServices";
import About from "./pages/about/About";
import UserProfile from "./components/userprofile/UserProfile";
import FormComponent from "./components/bookingslot/FormComponent";
import AddService from "./components/Dashboard/Features/AddService";
import ForgotPassword from "./components/password/ForgotPassword";
import AddManager from "./components/Dashboard/Features/AddManager";
import ViewManager from "./components/Dashboard/Features/ViewManager";
import Feedback from "./components/Dashboard/Features/Feedback";
import ResetPassword from "./components/password/ResetPassword";
import MyBookings from "./components/myBookings/MyBookings";
import Help from "./components/Dashboard/Features/Help";
import AccountSetting from "./components/Dashboard/Features/AccountSetting";
import AddSlot from "./components/Dashboard/Features/AddSlot";
import ViewSlot from "./components/Dashboard/Features/ViewSlot";
import Announcement from "./components/Dashboard/Features/Announcement";
import UserBookings from "./components/Dashboard/Features/UserBookings";
import UserFeedback from "./components/userFeedback/UserFeedback";
import Enquiries from "./components/Dashboard/Features/Enquiries";
import PageNotFound from "./components/pagenotfound/PageNotFound";
import { useSelector } from "react-redux";

function Allcomponents() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  const reduxdata = useSelector((state) => state.login);
  const token = reduxdata.token;

  useEffect(() => {
    setIsAuthenticated(!!token);
  }, []);

  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/availableslot" element={<AvailableSlots />} />
        <Route
          path="/login"
          element={
            <Login
              isAuthenticated={isAuthenticated}
              setIsAuthenticated={setIsAuthenticated}
            />
          }
        />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/servicecenter" element={<Locations />} />
        <Route path="/contact" element={<Contact />} />
        <Route path="/about" element={<About />} />
        <Route path="/ourservices" element={<OurServices />} />
        <Route path="/bookappointment" element={<FormComponent />} />
        <Route path="/test" element={<Temp />} />
        <Route path="/forgotpassword" element={<ForgotPassword />} />

        <Route path="/resetpassword" element={<ResetPassword />} />
        <Route path="/userfeedback" element={<UserFeedback />} />

        {/* 404 Not Found route - placed at the end */}
        <Route path="*" element={<PageNotFound />} />

        {/*this temp component is for testing redux and other things..*/}

        {/* Dashboard Routes */}

        <Route
          path="/dashboard"
          element={
            <PrivateRoute>
              <Dashboard />
            </PrivateRoute>
          }
        />
        <Route
          path="/userbookings"
          element={
            <PrivateRoute>
              <UserBookings />
            </PrivateRoute>
          }
        />

        <Route
          path="/addservicecenter"
          element={
            <PrivateRoute>
              <AddService />
            </PrivateRoute>
          }
        />
        <Route
          path="/addmanager"
          element={
            <PrivateRoute>
              <AddManager />
            </PrivateRoute>
          }
        />
        <Route
          path="/viewmanager"
          element={
            <PrivateRoute>
              <ViewManager />
            </PrivateRoute>
          }
        />

        <Route
          path="/addslot"
          element={
            <PrivateRoute>
              <AddSlot />
            </PrivateRoute>
          }
        />

        <Route
          path="/viewslot"
          element={
            <PrivateRoute>
              <ViewSlot />
            </PrivateRoute>
          }
        />

        <Route
          path="/feedback"
          element={
            <PrivateRoute>
              <Feedback />
            </PrivateRoute>
          }
        />

        <Route
          path="/announcement"
          element={
            <PrivateRoute>
              <Announcement />
            </PrivateRoute>
          }
        />

        <Route
          path="/help"
          element={
            <PrivateRoute>
              <Help />
            </PrivateRoute>
          }
        />

        <Route
          path="/accounts"
          element={
            <PrivateRoute>
              <AccountSetting />
            </PrivateRoute>
          }
        />

        <Route
          path="/enquiries"
          element={
            <PrivateRoute>
              <Enquiries />
            </PrivateRoute>
          }
        />

        {/* user profile route */}
        <Route path="/profile" element={<UserProfile />} />
        <Route path="/bookings" element={<MyBookings />} />
      </Routes>
    </Router>
    // <Locations></Locations>
  );
}

export default Allcomponents;

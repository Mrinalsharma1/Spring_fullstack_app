import axios from "axios";
import React, { useEffect, useState } from "react";
import { Button } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import "./UserProfile.css";
import Header from "../header/Header";
import NavBar from "../header/Navbar";
import { login } from "../../redux/userSlice";
import Footer from "../footer/Footer";
function UserProfile() {
  const userId = useSelector((state) => state.login.id);
  const token = useSelector((state) => state.login.token);
  const isLoggedin = useSelector((state) => state.login.isLoggedIn);
  const [enableEdit, setEnableEdit] = useState(false);
  const [userData, setUserData] = useState({});
  const dispatch = useDispatch();
  const [success, setSuccess] = useState("");
  // Fetch user data from the backend
  useEffect(() => {
    fetch(`http://localhost:8072/user/findUserById/${userId}`)
      .then((response) => response.json())
      .then((data) => {
        setUserData(data.user);
      })
      .catch((error) => console.error("Error fetching data:", error));
  }, []);
  //console.log(userData);

  const patchUser = async () => {
    try {
      dispatch(
        login({
          id: userData.id,
          name: userData.profilename,
          email: userData.username,
          role: userData.role,
          token: token,
          isLoggedIn: true,
        })
      );
      const updateFieldRole = {
        profilename: userData.profilename,
        email: userData.username,
      };

      const response = await axios.put(
        "http://localhost:8072/user/updateuser/" + userId,
        updateFieldRole
      );
      //console.log(response.status)
      setSuccess(response.status);
    } catch (error) {
      console.error("Error updating the user name:", error);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setUserData({ ...userData, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (isLoggedin) {
      patchUser(userId);
    } else {
      setSuccess(400);
    }
  };

  return (
    <>
      <Header />
      <NavBar />
      <div className="d-flex justify-content-center align-items-center flex-column">
        <h2 style={{ marginTop: "40px", marginBottom: "-20px" }}>
          Account Settings
        </h2>
        <div className="userProfile">
          <div className="profile-row">
            <h5 style={{ textAlign: "left" }}>Name : </h5>
            <input
              type="text"
              name="profilename"
              defaultValue={userData.profilename}
              disabled={!enableEdit}
              onChange={handleChange}
            />
          </div>
          <div className="profile-row">
            <h5>Email : </h5>
            <input
              type="text"
              name="username"
              defaultValue={userData.username}
              disabled={!enableEdit}
              onChange={handleChange}
            />
          </div>

          <div className="profile-row">
            <h5>Role : </h5>
            <input
              type="text"
              name="role"
              defaultValue={userData.role}
              disabled
              onChange={handleChange}
            />
          </div>

          <div className="profile-setting-button">
            {!enableEdit && (
              <Button
                className="profile-edit"
                onClick={() => {
                  setEnableEdit(true);
                }}
              >
                Edit
              </Button>
            )}
            {enableEdit && (
              <Button
                className="profile-submit"
                onClick={(e) => {
                  setEnableEdit(false);
                  handleSubmit(e);
                }}
              >
                Save
              </Button>
            )}
            {enableEdit && (
              <Button
                className="profile-submit"
                onClick={() => {
                  setEnableEdit(false);
                }}
              >
                Cancel
              </Button>
            )}
          </div>
          {success === 400 && (
            <div className="alert alert-danger mt-3 text-center">
              Please login again
            </div>
          )}
          {success === 200 && (
            <div className="alert alert-success mt-3 text-center">
              Data updated successfully
            </div>
          )}
        </div>
      </div>
      <Footer />
    </>
  );
}

export default UserProfile;

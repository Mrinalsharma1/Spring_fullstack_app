import React, { useEffect, useState } from "react";
import DashboardLayout from "../Layout/DashboardLayout";
import "./AccountSetting.css";
import { useDispatch, useSelector } from "react-redux";
import { login } from "../../../redux/userSlice";
import axios from "axios";

function AccountSetting() {
  const userId = useSelector((state) => state.login.id);
  const token = useSelector((state) => state.login.token);

  const [enableEdit, setEnableEdit] = useState(false);
  const [userData, setUserData] = useState({});
  const [success, setSuccess] = useState("");
  const dispatch = useDispatch();

  // Fetch user data from the backend
  useEffect(() => {
    fetch(`http://localhost:8072/user/findUserById/${userId}`)
      .then((response) => response.json())
      .then((data) => {
        setUserData(data.user);
      })
      .catch((error) => console.error("Error fetching data:", error));
  }, []);

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

      setSuccess(response.status);
    } catch (error) {
      console.error("Error updating the user name:", error);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setUserData({ ...userData, [name]: value });
  };
  console.log(userData);

  const handleEditClick = (e) => {
    e.preventDefault();
    setEnableEdit(true);
  };

  const handleSaveClick = (e) => {
    e.preventDefault();
    patchUser();
    setEnableEdit(false);
  };

  const handleCancelClick = (e) => {
    e.preventDefault();
    setEnableEdit(false);
  };

  return (
    <DashboardLayout>
      <div className="accountsetting-container">
        <div className="accountsetting-container-inner">
          <div className="accountsetting-row">
            <h3 className="accountsetting-heading">Account Setting</h3>
            <div className="accountsetting-col">
              <form>
                <div className="accountsetting-form-group">
                  <p className="accountsetting-label">Name :</p>
                  <input
                    type="text"
                    name="profilename"
                    className="accountsetting-input"
                    defaultValue={userData.profilename}
                    onChange={handleInputChange}
                    disabled={!enableEdit}
                  />
                </div>
                <div className="accountsetting-form-group">
                  <p className="accountsetting-label">Email :</p>
                  <input
                    type="email"
                    name="username"
                    className="accountsetting-input"
                    defaultValue={userData.username}
                    onChange={handleInputChange}
                    disabled={!enableEdit}
                  />
                </div>
                <div className="accountsetting-form-group">
                  <p className="accountsetting-label">Role :</p>
                  <input
                    type="text"
                    name="role"
                    className="accountsetting-input"
                    defaultValue={userData.role}
                    onChange={handleInputChange}
                    disabled
                  />
                </div>
                <div className="accountsetting-buttons-container">
                  {!enableEdit ? (
                    <button
                      type="button"
                      className="accountsetting-btn accountsetting-btn-secondary"
                      onClick={handleEditClick}
                    >
                      Edit
                    </button>
                  ) : (
                    <button
                      type="button"
                      className="accountsetting-btn accountsetting-btn-success"
                      onClick={handleSaveClick}
                    >
                      Save
                    </button>
                  )}
                  <button
                    type="button"
                    className="accountsetting-btn accountsetting-btn-danger"
                    onClick={handleCancelClick}
                  >
                    Cancel
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
}

export default AccountSetting;

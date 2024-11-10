import React, { useEffect, useState } from "react";
import DashboardLayout from "../Layout/DashboardLayout";
import axios from "axios";
import "./Feedback.css";
import Swal from "sweetalert2";
import empty from "../../../assets/images/empty.svg";
import { useSelector } from "react-redux";
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import DialogTitle from "@mui/material/DialogTitle";

function Feedback() {
  const [feedbacks, setFeedbacks] = useState([]);
  const [managerPinCode, setManagerPinCode] = useState("");
  const id = useSelector((state) => state.login.id);
  const token = useSelector((state) => state.login.token);

  const [selectedFeedback, setSelectedFeedback] = useState(null);

  const [open, setOpen] = useState(false);

  const fetchFeedbacks = async () => {
    try {
      const response = await axios.get("http://localhost:8071/getfeedbacks", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      console.log(response.data);

      const filteredData = response.data.data.filter(
        (feedback) => feedback.pincode === managerPinCode
      );
      console.log(filteredData);

      setFeedbacks(filteredData);
    } catch (error) {
      console.error("Error fetching feedbacks:", error);
    }
  };

  const getpincode = async () => {
    const pincodeResponse = await axios.get(
      `http://localhost:8071/pincode/${id}`,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    if (pincodeResponse.data.status === "success") {
      setManagerPinCode(pincodeResponse.data.pincode);
    }
  };

  useEffect(() => {
    getpincode();
    fetchFeedbacks();
  }, [managerPinCode]);

  const truncateMessage = (message, wordLimit) => {
    const words = message.split(" ");
    if (words.length > wordLimit) {
      return words.slice(0, wordLimit).join(" ") + "...";
    }
    return message;
  };
  const handleView = (feedback) => {
    setSelectedFeedback(feedback);
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
    setSelectedFeedback(null);
  };

  return (
    <div>
      <DashboardLayout>
        <div className="feedbacks">
          <h2>Feedbacks</h2>
          <div className="viewFeedbacks">
            {feedbacks.length > 0 ? (
              <table className="table feedback-table table-hover">
                <thead>
                  <tr>
                    <th className="rowf-id">ID</th>
                    <th className="rowf-name">Name</th>
                    <th className="rowf-email">Email</th>
                    <th className="row-review">Review</th>
                    <th className="feedback-action">Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {feedbacks.map((feedback) => (
                    <tr key={feedback.fid}>
                      <td className="rowf-id">{feedback.fid}</td>
                      <td className="rowf-name">{feedback.name}</td>
                      <td className="rowf-email">{feedback.email}</td>
                      <td className="row-message">
                        {truncateMessage(feedback.review, 3)}
                      </td>
                      <td>
                        <i
                          className="fa-sharp fa-solid fa-eye"
                          style={{ color: "#0c0D4D", cursor: "pointer" }}
                          onClick={() => handleView(feedback)}
                        ></i>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            ) : (
              <img className="empty-img" src={empty} alt="No Data Found" />
            )}
          </div>
          <Dialog
            className="ReplyModal"
            open={open}
            onClose={handleClose}
            aria-labelledby="alert-dialog-title"
            aria-describedby="alert-dialog-description"
            sx={{
              "& .MuiBackdrop-root": {
                backgroundColor: "rgba(0, 0, 0, 0.1)",
              },
            }}
            PaperProps={{
              style: {
                backgroundColor: "rgba(255, 255, 255, 0.9)",
                boxShadow: "none",
                borderRadius: "8px",
                width: "500px", // Set your desired width here
                maxWidth: "none",
              },
            }}
          >
            <DialogTitle>
              Review
              <hr />
            </DialogTitle>
            <DialogContent className="dialog-content-container">
              <DialogContentText className="dialog-content">
                <span
                  style={{
                    fontSize: "1.2rem",
                    color: "#0c0D4D",
                    paddingLeft: "5px",
                  }}
                >
                  {selectedFeedback?.review}
                </span>
              </DialogContentText>
            </DialogContent>
            <DialogActions>
              <Button onClick={handleClose} autoFocus>
                Close
              </Button>
            </DialogActions>
          </Dialog>
        </div>
      </DashboardLayout>
    </div>
  );
}

export default Feedback;

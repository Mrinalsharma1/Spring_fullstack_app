import React, { useEffect, useState } from "react";
import DashboardLayout from "../Layout/DashboardLayout";
import axios from "axios";
import "./Enquiries.css";
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import DialogTitle from "@mui/material/DialogTitle";
import Swal from "sweetalert2";
import empty from "../../../assets/images/empty.svg";

function Enquiries() {
  const [open, setOpen] = useState(false);
  const [messages, setMessages] = useState([]);
  const [replyMessage, setReplyMessage] = useState("");
  const fetchMessages = async () => {
    try {
      const response = await axios.get(
        "http://localhost:8072/contact/getallmessages"
      );

      setMessages(response.data.messages);
      console.log(messages);
    } catch (error) {
      console.log("error fetching messages: " + error);
    }
  };
  useEffect(() => {
    fetchMessages();
  }, []);

  const handleReply = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setReplyMessage("");
    setOpen(false);
  };

  const handleSend = async (useremail) => {
    const req_body = {
      email: useremail,
      message: replyMessage,
    };

    // Display "Please wait" alert
    Swal.fire({
      title: "Please wait...",
      text: "Sending email...",
      icon: "info",
      allowOutsideClick: false,
      showConfirmButton: false,
      didOpen: () => {
        Swal.showLoading();
      },
    });

    try {
      await axios.post("http://localhost:8072/contact/reply", req_body);

      // Close the loading alert and show success alert
      Swal.fire({
        title: "Success!",
        text: "Email sent successfully.",
        icon: "success",
        confirmButtonText: "OK",
      });
    } catch (error) {
      // Close the loading alert and show error alert
      Swal.fire({
        title: "Error!",
        text: "Failed to send the email. Please try again.",
        icon: "error",
        confirmButtonText: "OK",
      });
    }
  };

  const handleCancel = () => {
    setReplyMessage("");
    setOpen(false);
  };

  const truncateMessage = (message, wordLimit) => {
    const words = message.split(" ");
    if (words.length > wordLimit) {
      return words.slice(0, wordLimit).join(" ") + "...";
    }
    return message;
  };

  return (
    <div>
      <DashboardLayout>
        <div className="Messages">
          <h2>Messages</h2>
          <div className="viewMessages">
            {messages.length > 0 && (
              <table className="table  table-hover">
                <thead>
                  <tr>
                    <th className="row-id">ID</th>
                    <th className="row-name">Name</th>
                    <th className="row-email">Email</th>
                    <th className="row-phone">Phone</th>
                    <th className="row-subject">Subject</th>
                    <th className="row-message">Message</th>
                    <th className="row-reply">Action</th>
                  </tr>
                </thead>
                <tbody>
                  {messages.map((message, index) => (
                    <tr key={index}>
                      <td className="row-id">{message.contactid}</td>
                      <td className="row-name">{message.name}</td>
                      <td className="row-email">{message.email}</td>
                      <td className="row-phone" style={{ width: "300px" }}>
                        {message.phone}
                      </td>
                      <td className="row-subject">{message.subject}</td>
                      <td className="row-message">
                        {truncateMessage(message.message, 3)}
                      </td>
                      <td className="row-reply">
                        <Button onClick={handleReply}>Reply</Button>
                      </td>
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
                          },
                        }}
                      >
                        <DialogTitle>
                          {"Send reply"}
                          <hr />
                        </DialogTitle>
                        <DialogContent className="dialog-content-container">
                          <DialogContentText className="dialog-content">
                            <span>To: </span>{" "}
                            <span
                              style={{ fontSize: "1rem", paddingLeft: "5px" }}
                            >
                              {message.email}
                            </span>
                          </DialogContentText>
                          <DialogContentText className="dialog-content">
                            <span>Message:</span>{" "}
                            <textarea
                              className="message-textarea"
                              value={message.message}
                              disabled
                            ></textarea>
                          </DialogContentText>
                          <DialogContentText className="dialog-content">
                            <span>Reply: </span>
                            <textarea
                              className="reply-textarea"
                              onChange={(e) => {
                                setReplyMessage(e.target.value);
                              }}
                            ></textarea>
                          </DialogContentText>
                        </DialogContent>
                        <DialogActions>
                          <Button
                            onClick={() => {
                              handleClose();
                              handleSend(message.email);
                            }}
                          >
                            Send
                          </Button>
                          <Button
                            onClick={() => {
                              handleClose();
                              handleCancel();
                            }}
                            autoFocus
                          >
                            Cancel
                          </Button>
                        </DialogActions>
                      </Dialog>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
            {messages.length === 0 && (
              <img className="empty-img" src={empty} alt="No Data Found" />
            )}
          </div>
        </div>
      </DashboardLayout>
    </div>
  );
}

export default Enquiries;

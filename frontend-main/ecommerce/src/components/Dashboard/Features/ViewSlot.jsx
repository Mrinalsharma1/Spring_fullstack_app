import React, { useState, useEffect } from "react";
import axios from "axios";
import DashboardLayout from "../Layout/DashboardLayout";
import { IconButton, TextField, Button } from "@mui/material";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEdit } from "@fortawesome/free-solid-svg-icons";
import "./ViewSlot.css";
import { useSelector } from "react-redux";
import Swal from 'sweetalert2';
import { log } from "loglevel";

function ViewSlot() {
  const [success, setSuccess] = useState(null);
  const [error, setError] = useState(false);
  const [slots, setSlots] = useState([]);
  const [editingSlotId, setEditingSlotId] = useState(null);
  const [editedTiming, setEditedTiming] = useState("");
  const [message, setMessage] = useState("");
  const [managerPinCode, setManagerPinCode] = useState("");


  const token = useSelector((state) => state.login.token);
  const userid = useSelector((state) => state.login.id);

  useEffect(() => {
    const fetchData = async () => {
      try {
        // Fetch pincode
        const pincodeResponse = await axios.get(
          `http://localhost:8071/pincode/${userid}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        if (pincodeResponse.data.status === "success") {
          //console.log(pincodeResponse.data);
          setManagerPinCode(pincodeResponse.data.pincode);
          //console.log(pincodeResponse.data.pincode)
          // Fetch slots
          const slotsResponse = await axios.get(
            `http://localhost:8071/pincodetimings?pincode=${pincodeResponse.data.pincode}`,
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
          );
          //console.log(pincodeResponse);
          //console.log(slots);

          if (slotsResponse.data.status === "success") {
            setSlots(slotsResponse.data.timings);
            setMessage(slotsResponse.data.message);
          } else {
            setMessage(slotsResponse.data.message);
          }
        } else {
          console.error(
            "Failed to fetch pincode: ",
            pincodeResponse.data.message
          );
        }
      } catch (error) {
        console.error("Error fetching data:", error);
        if (error.response && error.response.status === 401) {
          setSuccess(401);
        } else {
          setError(true);
          setMessage("Failed to fetch data");
          setTimeout(() => {
            setError(false);
          }, 3000);
        }
      }
    };
    fetchData();
  }, [token, userid]);

  const handleEditClick = (slot) => {
    setEditingSlotId(slot.slotid);
    setEditedTiming(slot.timing);
  };

  // const handleSaveClick = async () => {
  //   try {
  //     const response = await axios.put(
  //       `http://localhost:8071/updatetime/${editingSlotId}`,
  //       {
  //         newTiming: editedTiming,
  //       },
  //       {
  //         headers: {
  //           Authorization: `Bearer ${token}`,
  //         },
  //       }
  //     );
  //     setSlots(
  //       slots.map((slot) =>
  //         slot.slotid === editingSlotId
  //           ? { ...slot, timing: editedTiming }
  //           : slot
  //       )
  //     );  
  //   } catch (error) {
  //     console.error("Error updating slot:", error);
  //     if (error.response && error.response.status === 401) {
  //       setSuccess(401);
  //     }
  //   } finally {
  //     setEditingSlotId(null);
  //     setEditedTiming("");
  //   }

  // };



  const handleSaveClick = async () => {
    try {
      const result = await Swal.fire({
        title: 'Are you sure?',
        text: "Do you want to save these changes?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, save it!'
      });

      if (result.isConfirmed) {
        const response = await axios.put(
          `http://localhost:8071/updatetime/${editingSlotId}`,
          {
            newTiming: editedTiming,
          },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        setSlots(
          slots.map((slot) =>
            slot.slotid === editingSlotId
              ? { ...slot, timing: editedTiming }
              : slot
          )
        );

        Swal.fire('Saved!', 'The slot timing has been updated.', 'success');
      }
    } catch (error) {
      console.error("Error updating slot:", error);
      if (error.response && error.response.status === 401) {
        setSuccess(401);
      }
      Swal.fire('Error!', 'There was a problem updating the slot.', 'error');
    } finally {
      setEditingSlotId(null);
      setEditedTiming("");
    }
  };

  return (
    <div>
      <DashboardLayout>
        {success === 401 && (
          <div className="alert alert-danger mt-3 text-center">
            Please login again
          </div>
        )}
        <div className="viewslot-container">
          <h2 className="viewslot-h2">View Slots</h2>

          <table className="viewslot-table table table-hover">
            <thead>
              <tr>
                <th>ID</th>
                <th>Pincode</th>
                <th>Timing</th>
                <th className="viewslot-action">Actions</th>
              </tr>
            </thead>
            <tbody>
              {slots &&
                slots.map((slot) => (
                  <tr key={slot.slotid}>
                    <td>{slot.slotid}</td>
                    <td>{managerPinCode}</td>
                    <td>
                      {editingSlotId === slot.slotid ? (
                        <TextField
                          value={editedTiming}
                          onChange={(e) => setEditedTiming(e.target.value)}
                        />
                      ) : (
                        slot.timing
                      )}
                    </td>
                    <td className="viewslot-action editicon">
                      {editingSlotId === slot.slotid ? (
                        <Button
                          onClick={() => {
                            handleSaveClick(slot.timings);
                          }}
                          color="primary"
                        >
                          Save
                        </Button>
                      ) : (
                        <IconButton
                          className="viewslot-icon"
                          onClick={() => handleEditClick(slot)}
                        >
                          <FontAwesomeIcon icon={faEdit} />
                        </IconButton>
                      )}
                    </td>
                  </tr>
                ))}
            </tbody>
          </table>
        </div>
      </DashboardLayout>
    </div>
  );
}

export default ViewSlot;

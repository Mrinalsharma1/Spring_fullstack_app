import React, { useState } from "react";
import axios from "axios";
import "./AddBlog.css";
import DashboardLayout from "../Layout/DashboardLayout";
import log from "loglevel";
import { ADDBLOG_SUCCESS, ADDBLOG_FAILURE } from "../../GlobalData/Constant";
// import { Alert } from "@mui/material/Alert";
// import { Stack } from "@mui/material/Stack";
import { Stack, Alert } from "@mui/material";
import { useSelector } from "react-redux";

function AddBlogForm() {
  const categories = [
    "Mindfulness",
    "Health",
    "Travel",
    "Technology",
    "Food",
    "Lifestyle",
  ];

  // Single state object to hold all form data
  const [formData, setFormData] = useState({
    title: "",
    author: "",
    content: "",
    category: "",
    date: "",
    imageFile: null,
    profileFile: null,
  });

  const [success, setSuccess] = useState(false);
  const [error, setError] = useState(false);
  const user = useSelector((state) => state.user);

  // Update the form data state on change
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  // Handle file changes separately
  const handleFileChange = (e) => {
    const { name, files } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: files[0], // Save the file object
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Create a FormData object to append the form data
    const formDataToSend = new FormData();
    formDataToSend.append("title", formData.title);
    formDataToSend.append("author", formData.author);
    formDataToSend.append("content", formData.content);
    formDataToSend.append("category", formData.category);
    formDataToSend.append("date", formData.date);

    // Append the files if they exist
    if (formData.imageFile) {
      formDataToSend.append("imageFile", formData.imageFile);
    }
    if (formData.profileFile) {
      formDataToSend.append("profileFile", formData.profileFile);
    }

    // Example image URLs for demo purposes
    const imageUrl = formData.imageFile
      ? `utils/${formData.imageFile.name}`
      : "";
    const profileUrl = formData.profileFile
      ? `utils/${formData.profileFile.name}`
      : "";

    const blogData = {
      title: formData.title,
      author: formData.author,
      content: formData.content,
      date: formData.date,
      categories: [formData.category],
      email: formData.email,
      imageUrl,
      profileUrl,
    };

    try {
      // Send the blog data to the backend (json-server)
      await axios.post("http://localhost:5001/blogs", blogData);

      // Clear the form after successful submission
      setFormData({
        title: "",
        author: "",
        content: "",
        category: "",
        date: "",
        imageFile: null,
        profileFile: null,
      });
      //console.log(formData);

      // alert("Blog added successfully!");
      setSuccess(true);
      log.info("Blog added successfully by:" + formData.author);
      setTimeout(() => {
        setSuccess(false);
      }, 3000);
    } catch (error) {
      setError(true);
      log.info("Failed to add blog");
      setTimeout(() => {
        setError(false);
      }, 3000);
    }
  };

  return (
    <DashboardLayout>
      <div className="form-container">
        <h2>Add a New Blog</h2>
        <form className="blog-form" onSubmit={handleSubmit}>
          <div className="form-group">
            <div className="label-text">
              <label>Title: </label>
            </div>
            <input
              type="text"
              name="title"
              value={formData.title}
              onChange={handleInputChange}
              required
            />
          </div>
          <div className="form-group">
            <div className="label-text">
              <label>Author: </label>
            </div>
            <input
              type="text"
              name="author"
              value={formData.author}
              onChange={handleInputChange}
              required
            />
          </div>
          <div className="form-group">
            <div className="label-text">
              <label>Content: </label>
            </div>
            <textarea
              name="content"
              value={formData.content}
              onChange={handleInputChange}
              required
            />
          </div>
          <div className="form-group">
            <div className="label-text">
              <label>Upload Image: </label>
            </div>
            <input type="file" name="imageFile" onChange={handleFileChange} />
          </div>
          <div className="form-group">
            <div className="label-text">
              <label>Upload Profile Image: </label>
            </div>
            <input type="file" name="profileFile" onChange={handleFileChange} />
          </div>
          <div className="form-group">
            <div className="label-text">
              <label>Category:</label>
            </div>
            <select
              name="category"
              value={formData.category}
              onChange={handleInputChange}
              required
            >
              <option value="" disabled>
                Select a category
              </option>
              {categories.map((category, index) => (
                <option key={index} value={category}>
                  {category}
                </option>
              ))}
            </select>
          </div>
          <div className="form-group">
            <div className="label-text">
              <label>Date:</label>
            </div>
            <input
              type="date"
              name="date"
              value={formData.date}
              onChange={handleInputChange}
              required
            />
          </div>

          {success && (
            <Stack sx={{ width: "100%" }} spacing={2}>
              <Alert variant="filled" severity="success">
                {ADDBLOG_SUCCESS}
              </Alert>
            </Stack>
          )}
          {error && (
            <Stack sx={{ width: "100%" }} spacing={2}>
              <Alert variant="filled" severity="error">
                {ADDBLOG_FAILURE}
              </Alert>
            </Stack>
          )}

          <button type="submit" className="submit-btn">
            Add Blog
          </button>
        </form>
      </div>
    </DashboardLayout>
  );
}

export default AddBlogForm;

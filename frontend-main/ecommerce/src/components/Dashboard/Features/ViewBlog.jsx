import React, { useState, useEffect } from "react";
import axios from "axios";
import { FaEdit, FaTrashAlt } from "react-icons/fa";
import "./ViewBlog.css";
import {
  VIEWBLOG_FETCH,
  VIEWBLOG_DELETE,
  VIEWBLOG_SAVEBLOG,
} from "../../GlobalData/Constant";

import DashboardLayout from "../Layout/DashboardLayout";
import { Stack, Alert } from "@mui/material";

function ViewBlog() {
  const [blogs, setBlogs] = useState([]);
  const [editId, setEditId] = useState(null); // Track the blog being edited
  const [editableBlog, setEditableBlog] = useState({}); // Store the data being edited
  const [deleteBlog, setDeleteBlog] = useState(false);
  const [editBlogError, setEditBlogError] = useState(false);

  const [fetchBlog, setFetchBlog] = useState(false);

  useEffect(() => {
    // Fetch data from the JSON server
    axios
      .get("http://localhost:5001/blogs")
      .then((response) => {
        setBlogs(response.data);
      })
      .catch((error) => {
        setFetchBlog(true);
        setTimeout(() => {
          setFetchBlog(false);
        }, 3000);
      });
  }, []);

  // Handle edit click: Make the row editable
  const handleEditClick = (blog) => {
    setEditId(blog.id); // Set the edit mode for the clicked blog
    setEditableBlog(blog); // Store the editable blog data
  };

  const handleDeleteClick = (id) => {
    axios
      .delete(`http://localhost:5001/blogs/${id}`)
      .then((response) => {
        setBlogs(blogs.filter((blog) => blog.id !== id));
      })
      .catch((error) => {
        setDeleteBlog(true);
        setTimeout(() => {
          setDeleteBlog(false);
        }, 3000);
      });
  };

  // Handle input changes during editing
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setEditableBlog({
      ...editableBlog,
      [name]: value,
    });
  };

  // Handle saving the updated blog
  const handleSave = (id) => {
    axios
      .put(`http://localhost:5001/blogs/${id}`, editableBlog)
      .then((response) => {
        // Update the state with the updated blog
        setBlogs(blogs.map((blog) => (blog.id === id ? response.data : blog)));
        setEditId(null); // Exit edit mode
      })
      .catch((error) => {
        setEditBlogError(true);
        setTimeout(() => {
          setEditBlogError(false);
        }, 3000);
      });
  };

  return (
    <DashboardLayout>
      {fetchBlog && (
        <Stack sx={{ width: "100%" }} spacing={2}>
          <Alert variant="filled" severity="error">
            {VIEWBLOG_FETCH}
          </Alert>
        </Stack>
      )}
      {deleteBlog && (
        <Stack sx={{ width: "100%" }} spacing={2}>
          <Alert variant="filled" severity="error">
            {VIEWBLOG_DELETE}
          </Alert>
        </Stack>
      )}
      {editBlogError && (
        <Stack sx={{ width: "100%" }} spacing={2}>
          <Alert variant="filled" severity="error">
            {VIEWBLOG_SAVEBLOG}
          </Alert>
        </Stack>
      )}
      <div className="container">
        <div className="table-responsive">
          <table className="table table-striped">
            <thead>
              <tr>
                {/* <th scope="col">Sl No</th> */}
                <th scope="col">ID</th>
                <th scope="col">Blog Title</th>
                <th scope="col">Content</th>
                <th scope="col">Author Name</th>
                <th scope="col">Date</th>
                <th scope="col">Categories</th>
                <th scope="col">Image URL</th>
                <th scope="col">Profile URL</th>
                <th scope="col">Action</th>
              </tr>
            </thead>
            <tbody>
              {blogs.map((blog, index) => (
                <tr key={blog.id}>
                  {/* <th scope="row">{index + 1}</th> */}
                  {/* Check if this row is in edit mode */}
                  {editId === blog.id ? (
                    <>
                      <td>{blog.id}</td>
                      <td>
                        <input
                          type="text"
                          name="title"
                          value={editableBlog.title}
                          onChange={handleInputChange}
                        />
                      </td>
                      <td>
                        <textarea
                          name="content"
                          value={editableBlog.content}
                          onChange={handleInputChange}
                        />
                      </td>
                      <td>
                        <input
                          type="text"
                          name="author"
                          value={editableBlog.author}
                          onChange={handleInputChange}
                        />
                      </td>
                      <td>
                        <input
                          type="text"
                          name="date"
                          value={editableBlog.date}
                          onChange={handleInputChange}
                        />
                      </td>
                      <td>
                        <input
                          type="text"
                          name="categories"
                          value={editableBlog.categories}
                          onChange={handleInputChange}
                        />
                      </td>
                      <td>
                        <input
                          type="file"
                          name="imageUrl"
                          onChange={handleInputChange}
                        />
                      </td>
                      <td>
                        <input
                          type="file"
                          name="profileUrl"
                          onChange={handleInputChange}
                        />
                      </td>
                      <td>
                        <div className="view_save_button">
                          <div
                            className="save "
                            onClick={() => handleSave(blog.id)}
                          >
                            Save
                          </div>
                        </div>
                      </td>
                    </>
                  ) : (
                    <>
                      <td>{blog.id}</td>
                      <td>{blog.title.slice(0, 20)}</td>
                      <td>{blog.content.substring(0, 30)}...</td>{" "}
                      {/* Shortened content */}
                      <td>{blog.author}</td>
                      <td>{blog.date}</td>
                      <td>{blog.categories.join(", ")}</td>
                      <td>
                        <img
                          src={blog.imageUrl}
                          alt="Blog"
                          className="img-fluid img-thumbnail"
                          style={{ width: "50px", height: "50px" }}
                        />
                      </td>
                      <td>
                        <img
                          src={blog.profileUrl}
                          className="img-fluid img-thumbnail"
                          alt="Profile"
                          style={{ width: "50px", height: "50px" }}
                        />
                      </td>
                      <td>
                        <div className="icons">
                          <div
                            className="edit"
                            onClick={() => handleEditClick(blog)}
                          >
                            <FaEdit
                              style={{
                                color: "#0c0d4d",
                                fontSize: "30px",
                                paddingTop: "10px",
                              }}
                            />
                          </div>
                          <button
                            className="delete"
                            onClick={() => handleDeleteClick(blog.id)}
                          >
                            <FaTrashAlt className="text-danger" />
                          </button>
                        </div>
                      </td>
                    </>
                  )}
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </DashboardLayout>
  );
}

export default ViewBlog;

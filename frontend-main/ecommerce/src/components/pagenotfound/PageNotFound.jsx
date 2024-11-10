import React from "react";
import "./PageNotFound.css";
import { Link } from "react-router-dom";
function PageNotFound() {
  return (
    <div>
      <head>
        <link
          href="https://fonts.googleapis.com/css2?family=Nunito+Sans:wght@600;900&display=swap"
          rel="stylesheet"
        />
        <script
          src="https://kit.fontawesome.com/4b9ba14b0f.js"
          crossorigin="anonymous"
        ></script>
      </head>
      <body className="error_body">
        <div class="error_mainbox">
          <div class="err">4</div>
          <i class="far fa-question-circle fa-spin"></i>
          <div class="err2">4</div>
          <div class="error_msg">
            The page you are looking for does not exist.You can click the link
            below to go back to the homepage.
            <p className="error_paragraph">
              Let's go <Link to="/">home</Link> and try from there.
            </p>
          </div>
        </div>
      </body>
    </div>
  );
}

export default PageNotFound;

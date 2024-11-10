import React from "react";
import { FaRegClock } from "react-icons/fa";
import { IoIosMail, IoMdCall } from "react-icons/io";

import "./Header.css";

function Header() {
  return (
    <div className="outer-layer bg-light py-3">
      <div className="container-fluid">
        <div className="row header-logo">
          <div className="col-md-4 ">
            <div className="logo">
              <h2 className="fw-bold">SwiftQSolution</h2>
            </div>
          </div>
          <div className="col-md-8">
            <div className="row my-header-container">
              <div className=" col-md-4 d-flex align-items-center justify-content-center">
                <div className="header_icon_box">
                  <FaRegClock size={28} className="me-3 icon-custom-color " />
                  <div>
                    <div className="title fw-bold">Opening Time</div>
                    <div className="textp">Allday 9.00 - 18.00</div>
                  </div>
                </div>
              </div>
              <div className="col-md-4 d-flex align-items-center justify-content-center ">
                <div className="header_icon_box">
                  <IoIosMail size={28} className="me-3 icon-custom-color  " />
                  <div>
                    <div className="title fw-bold">Email us</div>
                    <div className="textp">
                      <a href="mailto:info@example.com" style={{ textDecoration: 'none', color: ' #60c5e4' }}>
                      swiftqsolutions@gmail.com
                      </a>
                    </div>
                  </div>
                </div>
              </div>
              <div className="col-md-4 d-flex align-items-center justify-content-center ">
                <div className="header_icon_box">
                  <IoMdCall size={28} className=" me-3 icon-custom-color " />
                  <div>
                    <div className="title fw-bold">Call us</div>
                    <div className="textp">
                      <a href="tel:+918678367823" style={{ textDecoration: 'none', color: ' #60c5e4' }}>+91-8678367823</a>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Header;

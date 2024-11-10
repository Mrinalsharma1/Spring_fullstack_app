import React from "react";
import "./Footer.css";
import { Link } from "react-router-dom";
import { FaArrowUpLong } from "react-icons/fa6";
import { useState, useEffect } from "react";
import axios from "axios";

function Footer() {
  const [email, setEmail] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Prepare the payload
    const payload = {
      email: email,
    };

    try {
      // Send POST request with Axios
      const response = await axios.post(
        "http://localhost:8072/newsletter/subscribe",
        payload,
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      //console.log(response.data);

      // Handle the response
      if (response.data.status === "success") {
        alert(response.data.message);
        setEmail("");
      }

      if (response.data.status === "fail") {
        alert("You are all ready subscribed");
      }
    } catch (error) {
      console.error(
        "Error:",
        error.response ? error.response.data : error.message
      );
      alert("You are all ready subscribed");
    }
  };
  const [showScrollButton, setShowScrollButton] = useState(false);

  const scrollToTop = () => {
    window.scrollTo({
      top: 0,
      behavior: "smooth",
    });
  };

  useEffect(() => {
    const handleScroll = () => {
      if (window.scrollY > 300) {
        setShowScrollButton(true);
      } else {
        setShowScrollButton(false);
      }
    };
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);
  return (
    <>
      <div className="footer-container ">
        {/* about start */}
        <div className="footer-about width-30 ">
          <div className="footer-about-heading">
            <div className="footer-icon-container">
              <i className="fa fa-mobile-alt mobile-icon"></i>
              <i
                className="fa-sharp fa-solid fa-wrench fa-rotate-by wrench-icon"
                style={{ "--fa-rotate-angle": "45deg" }}
              ></i>
            </div>
            <div className="footer-title-container">
              <p className="footer-about-title">SwiftQSolutions</p>
              <p className="footer-about-subtext">
                Repair for all your problems
              </p>
            </div>
          </div>
          <div className="footer-about-content sub-text">
            <p>
              Our comprehensive repair services cater to all your technological
              issues with utmost precision and expertise. We prioritize customer
              satisfaction, ensuring timely solutions that restore your devices
              to optimal performance. Trust our skilled technicians to address
              every challenge effectively and efficiently, delivering
              outstanding results.
            </p>
          </div>
          <div className="footer-contact-social">
            <div className="footer-contact-icon-container">
              <div className="icon">
                <i
                  className="fa-brands fa-twitter"
                  style={{ color: "#ffffff" }}
                ></i>
              </div>

              <div className="icon">
                <i
                  className="fa-brands fa-instagram"
                  style={{ color: "#ffffff" }}
                ></i>
              </div>

              <div className="icon">
                <i
                  className="fa-brands fa-facebook-f"
                  style={{ color: "#ffffff" }}
                ></i>
              </div>
            </div>
          </div>
        </div>
        {/* about end  */}

        {/* services start */}
        <div className="footer-services width-20">
          <div className="footer-service-title">
            <p>Our Services</p>
          </div>

          <Link
            to="/ourservices"
            style={{
              color: "#979797",
              fontSize: "15px",
              textDecoration: "none",
              marginBottom: "10px",
            }}
          >
            Smart Phone
          </Link>
          <Link
            to="/ourservices"
            style={{
              color: "#979797",
              fontSize: "15px",
              textDecoration: "none",
              marginBottom: "10px",
            }}
          >
            Laptop
          </Link>
          <Link
            to="/ourservices"
            style={{
              color: "#979797",
              fontSize: "15px",
              textDecoration: "none",
              marginBottom: "10px",
            }}
          >
            iPad
          </Link>
          <Link
            to="/ourservices"
            style={{
              color: "#979797",
              fontSize: "15px",
              textDecoration: "none",
              marginBottom: "10px",
            }}
          >
            Bluetooth Devices
          </Link>
          <Link
            to="/ourservices"
            style={{
              color: "#979797",
              fontSize: "15px",
              textDecoration: "none",
              marginBottom: "10px",
            }}
          >
            Tablets
          </Link>
          <Link
            to="/ourservices"
            style={{
              color: "#979797",
              fontSize: "15px",
              textDecoration: "none",
              marginBottom: "10px",
            }}
          >
            Repair Desktops
          </Link>
        </div>
        {/* services end  */}

        {/* contact us start  */}
        <div className="footer-contact width-20">
          <div className="footer-contact-title">
            <p>Contact Us</p>
          </div>
          <div className="footer-contact-text">
            <p>
              Have any questions or need support? Contact us any time and we’ll
              be happy to assist you!
            </p>
            <p>
              <a
                href="tel:+918678367823"
                style={{
                  cursor: "pointer",
                  textDecoration: "none",
                  color: "gray",
                }}
              >
                <i
                  className="fa fa-phone sub-text"
                  style={{ color: "#ffffff" }}
                ></i>{" "}
                +91-8678367823
              </a>
            </p>
            <p>
              <a
                href="mailto:info@example.com"
                style={{
                  cursor: "pointer",
                  textDecoration: "none",
                  color: "gray",
                }}
              >
                <i
                  className="fa fa-envelope sub-text"
                  style={{ color: "#ffffff" }}
                ></i>{" "}
                swiftqsolutions@gmail.com
              </a>
            </p>
            <p>You can also reach out to us on:</p>
          </div>
        </div>
        {/* contact us end */}

        {/* newletter start  */}
        <div className="footer-newsletter width-20">
          <div className="footer-newsletter-title">
            <p>News Letter</p>
          </div>
          <div className="footer-newsletter-text">
            <p>
              Stay informed with our curated insights and updates. Subscribe now
              to receive exclusive content and valuable information directly in
              your inbox!
            </p>
          </div>
          <form onSubmit={handleSubmit}>
            <input
              type="email"
              id="email"
              name="email"
              placeholder="Enter email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
            <br />
            <button className="subscribeButton">Subscribe</button>
          </form>
        </div>
        {/* newsletter end  */}
        <div className="contact-scrollup-button">
          <button
            onClick={scrollToTop}
            className={`scroll-to-top ${showScrollButton ? "show" : "hide"}`}
          >
            <FaArrowUpLong size={16} />
          </button>
        </div>
      </div>
      <div className="copyright">
        <span
          style={{
            fontSize: "25px",
            color: "#979797",
          }}
        >
          &copy;
        </span>
        <p
          style={{
            color: "#979797",
            fontSize: "15px",
          }}
        >
          {" "}
          <span style={{ color: " #60c5e4" }}>SwiftQSolution</span> 2024. All
          rights reserved.
        </p>
      </div>
    </>
  );
}

export default Footer;

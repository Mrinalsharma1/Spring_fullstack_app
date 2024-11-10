import React from "react";
import Header from "../../components/header/Header";
import Navbar from "../../components/header/Navbar";
import Footer from "../../components/footer/Footer";
import { FaAnglesRight } from "react-icons/fa6";
import { Link, useNavigate } from "react-router-dom";
import "./About.css";
import about1 from "../../assets/images/1.jpg";
import about2 from "../../assets/images/video-img.png";
import { FaRegPlayCircle } from "react-icons/fa";
import about3 from "../../assets/images/who1.jpg";
import about4 from "../../assets/images/who2.jpg";
import about5 from "../../assets/images/who3.jpg";
import { useState } from "react";

function About() {
  const [showVideo, setShowVideo] = useState(false);
  const navigate = useNavigate();
  const playVideo = () => {
    setShowVideo(true);
  };
  return (
    <>
      <Header />
      <Navbar />
      <div>
        <div className="about_bg">
          <div className="services_title">
            <h2>About us</h2>
          </div>
          <p className="text-center fs-6 home-link text-white">
            <Link to="/" style={{ textDecoration: "none" }}>
              Home
            </Link>
            <span className="px-2 contact-icon ">
              <FaAnglesRight />
            </span>
            <span>Service</span>
          </p>
        </div>
        <div className="container">
          <div className="row">
            <div className="col-md-6 mt-5 mb-5">
              <div className="about-content">
                <h2 className="about-msg">Welcome to Our </h2>
                <h1 className="aboutcompany-name">SwiftQSolution</h1>
                <p className="para-bold">
                  At SwiftQSolution, we pride ourselves on delivering
                  exceptional services that meet all your needs. Our dedicated
                  team is committed to excellence, ensuring your satisfaction
                  with every interaction. Trust us to provide innovative
                  solutions tailored just for you.
                </p>
                <p className="para-light">
                  Join us as we explore new horizons, fostering creativity and
                  collaboration to achieve outstanding results for our valued
                  clients and partners alike.
                </p>

                <div className="buttons">
                  <button
                    className="contact-button"
                    onClick={() => {
                      navigate("/contact");
                    }}
                  >
                    CONTACT US
                  </button>
                  <button
                    className="explore-button"
                    onClick={() => {
                      navigate("/ourservices");
                    }}
                  >
                    EXPLORE MORE
                  </button>
                </div>
              </div>
            </div>
            <div className="col-md-6  mt-5 mb-5">
              <div className="about_video">
                <div className="about-video-bg">
                  {!showVideo && (
                    <>
                      <img src={about1} alt="" />
                      {/* <button class="video-button" onclick="playVideo()">
                        <FaRegPlayCircle size={28} />
                      </button> */}
                    </>
                  )}

                  {showVideo && (
                    <iframe
                      id="video-iframe"
                      width="560"
                      height="315"
                      src="https://www.youtube.com/embed/su6apVhRovw?si=EQOAczHUpsENhMHS"
                      title="YouTube video player"
                      frameBorder="0"
                      allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
                      allowFullScreen
                    ></iframe>
                  )}
                </div>
                <div className="about-video-img">
                  <img src={about2} alt="" />
                </div>
              </div>
            </div>
          </div>
        </div>
        <div className="container">
          <div className="row mb-5">
            <div className="col-md-6">
              <div className="about-img">
                <img src={about3} alt="" className="img-thumbnail" />
              </div>
            </div>
            <div className="col-md-6">
              <div className="about-content">
                <h2 className="about-msg">Who We Are</h2>

                <p className="para-bold">
                  At SwiftQSolution, we are a dedicated team of professionals
                  committed to excellence in every aspect of our service. Our
                  passion for innovation drives us to provide tailored solutions
                  that exceed expectations and foster long-term relationships.
                  We strive to create an impactful difference for our clients
                  and community.
                </p>
                <p className="para-light">
                  Our mission is to empower individuals and organizations by
                  offering comprehensive support and resources that inspire
                  growth and success. We believe in a collaborative approach
                  that fosters creativity and drives results.
                </p>
                <p className="para-light">
                  With a focus on integrity and transparency, we are dedicated
                  to delivering the highest quality of service while maintaining
                  a positive impact on our environment and society. Join us on
                  this journey toward excellence and innovation.
                </p>
              </div>
            </div>
          </div>
          <div className="row mb-5">
            <div className="col-md-6">
              <div className="about-content">
                <h2 className="about-msg">Why Choose Us</h2>

                <p className="para-bold">
                  At SwiftQSolution, we distinguish ourselves through our
                  unwavering commitment to quality and customer satisfaction.
                  Our experienced team employs innovative strategies to deliver
                  solutions that not only meet but exceed your expectations. We
                  prioritize your needs in every aspect of our work.
                </p>
                <p className="para-light">
                  Our approach is built on transparency and collaboration,
                  ensuring that you are informed and engaged throughout the
                  process. We take pride in cultivating strong partnerships with
                  our clients, enabling us to tailor our services effectively
                  and efficiently.
                </p>
                <p className="para-light">
                  Choosing us means selecting a partner dedicated to your
                  success. We harness cutting-edge technology and industry
                  expertise to provide exceptional value and drive lasting
                  results for your organization and community.
                </p>
              </div>
            </div>
            <div className="col-md-6">
              <div className="about-img">
                <img src={about4} alt=" " className="img-thumbnail" />
              </div>
            </div>
          </div>
          <div className="row mb-5">
            <div className="col-md-6">
              <div className="about-img">
                <img src={about5} alt="" className="img-thumbnail" />
              </div>
            </div>
            <div className="col-md-6">
              <div className="about-content">
                <h2 className="about-msg">What we do</h2>
                {/* <h1 className="aboutcompany-name">SwiftQSolution</h1> */}
                <p className="para-bold">
                  At SwiftQSolution, we specialize in delivering comprehensive
                  solutions tailored to meet the diverse needs of our clients.
                  Our expert team utilizes innovative strategies and
                  cutting-edge technology to drive efficiency and enhance
                  productivity across various sectors. We are dedicated to
                  achieving exceptional results.
                </p>
                <p className="para-light">
                  We offer a wide range of services designed to empower
                  businesses and individuals alike, ensuring they have the tools
                  and support necessary to thrive in today's competitive
                  landscape. Our commitment to excellence is evident in every
                  project we undertake.
                </p>
                <p className="para-light">
                  By leveraging industry best practices and a client-centric
                  approach, we ensure that our solutions are not only effective
                  but also sustainable. Partner with us to transform your
                  challenges into opportunities for growth and success.
                </p>

                {/* <div className="buttons">
                  <button className="contact-button">CONTACT US</button>
                  <button className="explore-button">EXPLORE MORE</button>
                </div> */}
              </div>
            </div>
          </div>
        </div>
      </div>
      <Footer />
    </>
  );
}

export default About;

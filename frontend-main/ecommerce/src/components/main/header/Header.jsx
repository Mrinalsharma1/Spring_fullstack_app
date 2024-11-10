import React from "react";
import "./Header.css";

function Header() {
  return (
    <div>
      <header class="main-header">
        <div class="container">
          <div class="header-upper">
            <div class="row">
              <div class="col-md-6 col-sm-6 col-xs-12">
                <div class="top-left">
                  <div class="text">
                    Welcome to RepairPro - Making businesses better
                  </div>
                </div>
              </div>
              <div class="col-md-6 col-sm-6 col-xs-12">
                <div class="top-right clearfix">
                  <div class="text">
                    <a href="#">Login / Register</a>
                  </div>
                  <ul class="social-link">
                    <li>
                      <a href="#">
                        <i class="fa fa-facebook"></i>
                      </a>
                    </li>
                    <li>
                      <a href="#">
                        <i class="fa fa-twitter"></i>
                      </a>
                    </li>
                    <li>
                      <a href="#">
                        <i class="fa fa-rss"></i>
                      </a>
                    </li>
                    <li>
                      <a href="#">
                        <i class="fa fa-google-plus"></i>
                      </a>
                    </li>
                    <li>
                      <a href="#">
                        <i class="fa fa-vimeo"></i>
                      </a>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="outer-area">
          <div class="container">
            <div class="logo">
              <a href="index-2.html">
                <img src="images/home/logo.png" alt="" />
              </a>
            </div>
            <div class="single-info-box">
              <div class="single-info">
                <div class="icon-box">
                  <i class="flaticon-clock-1"></i>
                </div>
                <div class="title">Opening Time</div>
                <div class="text">Allday 9.00 - 18.00</div>
              </div>
              <div class="single-info">
                <div class="icon-box">
                  <i class="flaticon-envelope"></i>
                </div>
                <div class="title">Email Us</div>
                <div class="text">info@example.com</div>
              </div>
              <div class="single-info">
                <div class="icon-box">
                  <i class="fa flaticon-technology"></i>
                </div>
                <div class="title">Call Us Now</div>
                <div class="text-phone">+251-235-3256</div>
              </div>
            </div>
          </div>
        </div>

        <div class="header-lower">
          <div class="container">
            <div class="row">
              <div class="col-md-12 col-sm-12 col-xs-12">
                <div class="menu-bar">
                  <nav class="main-menu">
                    <div class="navbar-header">
                      <button
                        type="button"
                        class="navbar-toggle"
                        data-toggle="collapse"
                        data-target=".navbar-collapse"
                      >
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                      </button>
                    </div>
                    <div class="navbar-collapse collapse clearfix">
                      <ul class="navigation clearfix">
                        <li class="current">
                          <a href="index-2.html">Home</a>
                        </li>
                        <li class="dropdown">
                          <a href="#">Services</a>
                          <ul>
                            <li>
                              <a href="our-service.html">Our Service</a>
                            </li>
                            <li>
                              <a href="service-detail.html">Service Details</a>
                            </li>
                          </ul>
                        </li>
                        <li class="dropdown">
                          <a href="#">Page</a>
                          <ul>
                            <li>
                              <a href="about.html">About</a>
                            </li>
                            <li>
                              <a href="team.html">Our Team</a>
                            </li>
                            <li>
                              <a href="error-page.html">Error Page</a>
                            </li>
                          </ul>
                        </li>
                        <li class="dropdown">
                          <a href="#">Store</a>
                          <ul>
                            <li>
                              <a href="shop-page.html">Shop Page</a>
                            </li>
                            <li>
                              <a href="single-product.html">Single Prodct</a>
                            </li>
                            <li>
                              <a href="shoping-cart.html">Shoping Cart</a>
                            </li>
                            <li>
                              <a href="checkout.html">Checkout</a>
                            </li>
                          </ul>
                        </li>
                        <li class="dropdown">
                          <a href="blog.html">News</a>
                          <ul>
                            <li>
                              <a href="our-blog.html">Our Blog</a>
                            </li>
                            <li>
                              <a href="blog-single.html">Single Post</a>
                            </li>
                            <li>
                              <a href="faq.html">Faq</a>
                            </li>
                          </ul>
                        </li>
                        <li>
                          <a href="contact.html">contact</a>
                        </li>
                      </ul>
                    </div>
                  </nav>
                </div>
                <div class="more-option">
                  <div class="seach-toggle">
                    <i class="fa fa-search"></i>
                  </div>
                  <div class="search-box">
                    <form
                      method="post"
                      action="https://html.commonsupport.xyz/2017/RepairPro/index.html"
                    >
                      <div class="form-group">
                        <input
                          type="search"
                          name="search"
                          placeholder="Search Here"
                          required
                        />
                        <button type="submit">
                          <i class="fa fa-search"></i>
                        </button>
                      </div>
                    </form>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* <!--Sticky Header--> */}
        <div class="sticky-header">
          <div class="container clearfix">
            <div class="row">
              <div class="col-md-12 col-sm-12 col-xs-12">
                <div class="menu-bar">
                  <nav class="main-menu">
                    <div class="navbar-header">
                      <button
                        type="button"
                        class="navbar-toggle"
                        data-toggle="collapse"
                        data-target=".navbar-collapse"
                      >
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                      </button>
                    </div>
                    <div class="navbar-collapse collapse clearfix">
                      <ul class="navigation clearfix">
                        <li class="current">
                          <a href="index-2.html">Home</a>
                        </li>
                        <li class="dropdown">
                          <a href="#">Services</a>
                          <ul>
                            <li>
                              <a href="our-service.html">Our Service</a>
                            </li>
                            <li>
                              <a href="service-detail.html">Service Details</a>
                            </li>
                          </ul>
                        </li>
                        <li class="dropdown">
                          <a href="#">Page</a>
                          <ul>
                            <li>
                              <a href="about.html">About</a>
                            </li>
                            <li>
                              <a href="team.html">Our Team</a>
                            </li>
                            <li>
                              <a href="error-page.html">Error Page</a>
                            </li>
                          </ul>
                        </li>
                        <li class="dropdown">
                          <a href="#">Store</a>
                          <ul>
                            <li>
                              <a href="shop-page.html">Shop Page</a>
                            </li>
                            <li>
                              <a href="single-product.html">Single Prodct</a>
                            </li>
                            <li>
                              <a href="shoping-cart.html">Shoping Cart</a>
                            </li>
                            <li>
                              <a href="checkout.html">Checkout</a>
                            </li>
                          </ul>
                        </li>
                        <li class="dropdown">
                          <a href="blog.html">News</a>
                          <ul>
                            <li>
                              <a href="our-blog.html">Our Blog</a>
                            </li>
                            <li>
                              <a href="blog-single.html">Single Post</a>
                            </li>
                            <li>
                              <a href="faq.html">Faq</a>
                            </li>
                          </ul>
                        </li>
                        <li>
                          <a href="contact.html">contact</a>
                        </li>
                      </ul>
                    </div>
                  </nav>
                </div>
                <div class="more-option">
                  <div class="seach-toggle">
                    <i class="fa fa-search"></i>
                  </div>
                  <div class="search-box">
                    <form
                      method="post"
                      action="https://html.commonsupport.xyz/2017/RepairPro/index.html"
                    >
                      <div class="form-group">
                        <input
                          type="search"
                          name="search"
                          placeholder="Search Here"
                          required
                        />
                        <button type="submit">
                          <i class="fa fa-search"></i>
                        </button>
                      </div>
                    </form>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </header>
      {/* End Sticky Header */}
    </div>
  );
}

export default Header;

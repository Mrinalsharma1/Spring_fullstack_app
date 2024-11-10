import React, { useEffect, useState } from "react";
import "./SelectCenter.css";
import { Link } from "react-router-dom";

function SelectCenter(props) {
  const { states } = props;
  // //console.log(states.filter((ele) => (ele.pincode === 560010)));
  const [cityArray, setCityArray] = useState([]);
  const [displayAddress, setDisplayAddress] = useState(states);
  const [pincode, setPin] = useState("");

  function getUniqueStates(data) {
    const uniqueStates = [...new Set(data.map((item) => item.state))];
    return uniqueStates;
  }

  // useEffect(() => {
  //     setDisplayAddress(states.filter((ele) => ele.pincode === pincode));
  // }, [pincode]);

  const unique_states = getUniqueStates(states);

  return (
    <div className="displayCenter">
      <div className="service-center-container">
        {/* <div className="searchPin">
                    <input
                        type="text"
                        value={pincode}
                        onChange={
                            (e) => {
                                setPin(e.target.value);
                                setDisplayAddress(
                                    states.filter((ele) => ele.pincode === e.target.value)
                                );
                                // //console.log('pin changed' + pincode)
                                // //console.log(displayAddress);
                            }
                        }
                        placeholder="Enter Pin Code"

                    />

                </div> */}
        <div className="searchState">
          <h5>Search state</h5>
          <select
            className="form-control"
            onChange={(e) => {
              setCityArray(
                states.filter((ele) => ele.state === e.target.value)
              );

              setDisplayAddress(
                states.filter((ele) => ele.state === e.target.value)
              );
              setPin("");
            }}
          >
            <option className="dropdown-option">State</option>
            {unique_states.map((state, index) => (
              <option className="dropdown-option" key={index} value={state}>
                {state}
              </option>
            ))}
          </select>
        </div>
        <div className="searchCity">
          <h5>Search city</h5>
          <select
            className="form-control"
            onChange={(e) => {
              setPin("");
              setDisplayAddress(
                cityArray.filter((ele) => ele.city === e.target.value)
              );
            }}
          >
            <option className="dropdown-option">City</option>
            {cityArray.map((ct, index) => (
              <option className="dropdown-option" key={index} value={ct.city}>
                {ct.city}
              </option>
            ))}
          </select>
        </div>
      </div>

      <div className="address-container">
        <h2>Our Stores</h2>
        <div className="card-list">
          {displayAddress.map((item, index) => (
            <div key={index} className="address-card">
              <div className="address-box">
                <div className="address-container">
                  <h5>Address:</h5>
                  <p>
                    {item.address}, {item.city}, {item.pincode}, {item.state}
                  </p>
                  <Link
                    to={item.location}
                    className="nav-link active"
                    aria-current="page"
                  >
                    <p>
                      <i className="fa fa-location-dot"></i>See Map &gt;
                    </p>
                  </Link>
                </div>
                <div className="address-info">
                  <h5> Hours:</h5>
                  <p>
                    {" "}
                    {item.open_from} - {item.open_to}
                  </p>
                  <h5>Services:</h5>
                  <p>{item.service_type}</p>
                </div>
              </div>
              <div className="contact">
                <hr />
                <p>Phone: 9XXXXXXXX0</p>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default SelectCenter;

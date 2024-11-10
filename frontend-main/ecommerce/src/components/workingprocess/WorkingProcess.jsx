import React from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import "font-awesome/css/font-awesome.min.css";
import "./WorkingProcess.css";

const WorkingProcess = () => {
  const WORKING_PROCESS_STEPS = [
    { id: 1, title: "Damage Device", icon: "fa-laptop" },
    { id: 2, title: "Send it to Us", icon: "fa-paper-plane" },
    { id: 3, title: "Repair Device", icon: "fa fa-cogs" },
    { id: 4, title: "Quick Return", icon: "fa-arrow-right" },
  ];
  return (
    <div className="working-process">
      <div className="container-fluid">
        <div className="working_process_heading">
          <h1 className="text-center">Working Process</h1>

          <p className="text-center mb-7">
            <i className="fa fa-wrench fa-2x" style={{ color: "#4bb7e6" }}></i>{" "}
          </p>
        </div>
        <div className="my-product-container">
          {WORKING_PROCESS_STEPS.map((item) => (
            <div  key={item.id}>
              <div className="product_top_box">
                <div className="small_box">
                  <span className="one">{item.id}</span>
                </div>
                <div className="big_box">
                  <span>
                    <i className={`fa ${item.icon} fs-3 text-light`}></i>
                  </span>
                </div>
              </div>
              <div className="product_title_box">
                <p className="text-center text-light pb-1">{item.title}</p>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default WorkingProcess;

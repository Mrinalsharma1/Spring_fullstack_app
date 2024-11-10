import React from "react";
import StatisticCounter from "./StatisticCounter"; // Import StatisticCounter
import "./Statistic.css";

const statisticData = [
  { start: 0, end: 25, speed: 1500, text: "Years Experience" },
  { start: 0, end: 1250, speed: 1500, text: "Happy Customers" },
  { start: 0, end: 150, speed: 1500, text: "Expert Technicians" },
  { start: 0, end: 3550, speed: 1500, text: "Total Works Done" },
];

const StatisticCard = () => {
  return (
    <div className="counter-container">
      {" "}
      {/* Apply background to the whole container */}
      <div className="container">
        <div className="row">
          {statisticData.map((item, index) => (
            <div
              className="col-md-3 col-sm-6 col-xs-12 single-counter"
              key={index}
            >
              <article className="column wow fadeIn">
                <StatisticCounter
                  start={item.start}
                  end={item.end}
                  speed={item.speed}
                  text={item.text}
                />
              </article>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default StatisticCard;

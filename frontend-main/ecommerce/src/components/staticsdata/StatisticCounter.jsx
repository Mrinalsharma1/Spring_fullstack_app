import React, { useState, useEffect } from "react";

const StatisticCounter = ({ start, end, speed, text }) => {
  const [count, setCount] = useState(start);

  useEffect(() => {
    const incrementTime = Math.abs(Math.floor(speed / (end - start)));
    let currentCount = start;

    const counter = setInterval(() => {
      currentCount += 1;
      setCount(currentCount);
      if (currentCount === end) {
        clearInterval(counter);
      }
    }, incrementTime);

    return () => {
      clearInterval(counter);
    };
  }, [start, end, speed]);

  return (
    <div className="count-outer">
      <span className="count-text">{count} +</span>
      <div className="text">{text}</div>
    </div>
  );
};

export default StatisticCounter;

import React, { useState } from "react";
import Calendar from "react-calendar";

const Calender = ({ bookedSlots, handleDateChange }) => {
  const [selectedDate, setSelectedDate] = useState(null);

  //   const bookedSlots = [
  //     { date: "2024-10-28", count: 4 },
  //     { date: "2024-10-29", count: 4 },
  //     { date: "2024-10-30", count: 1 },
  //     { date: "2024-10-31", count: 1 },
  //   ];

  console.log("booking solts props", bookedSlots);
  const formatDate = (date) => {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}`;
  };

  const isFullyBooked = (date) => {
    const dateString = formatDate(date);
    const booking = bookedSlots?.find((slot) => slot.date === dateString);
    return booking && booking.count >= 4;
  };

  const isWithinNextFiveDays = (date) => {
    const today = new Date();
    const nextFiveDays = new Date();
    nextFiveDays.setDate(today.getDate() + 5);
    return date >= today && date <= nextFiveDays;
  };

  const tileClassName = ({ date }) => {
    if (isFullyBooked(date)) {
      return "fully-booked";
    } else if (isWithinNextFiveDays(date)) {
      return "available";
    }
    return null;
  };

  const tileDisabled = ({ date }) => {
    const today = new Date();
    const nextFiveDays = new Date();
    nextFiveDays.setDate(today.getDate() + 5);

    if (date < today || date > nextFiveDays) {
      return true;
    }

    if (date.getDay() === 0) {
      return true;
    }

    return isFullyBooked(date);
  };

  return (
    <Calendar
      //   onChange={(e) => {
      //     setSelectedDate(e);
      //     handleDateChange();
      //   }}
      onChange={(date) => {
        handleDateChange(date);
      }}
      value={selectedDate}
      tileClassName={tileClassName}
      tileDisabled={tileDisabled}
      minDate={new Date()}
    />
  );
};

export default Calender;

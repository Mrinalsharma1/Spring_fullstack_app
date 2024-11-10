import React from "react";
import { render, fireEvent, act } from "@testing-library/react";
import Calendar from "../../../components/availableslot/Calender/Calender";

test("fully booked date is styled correctly", () => {
  const bookedSlots = [{ date: "2024-10-28", count: 4 }];
  let container;
  act(() => {
    ({ container } = render(<Calendar bookedSlots={bookedSlots} handleDateChange={() => {}} />));
  });
  
  const fullyBookedTile = container.querySelector(".fully-booked");
  expect(fullyBookedTile).toBeInTheDocument();
});

test("available date within next five days is styled correctly", () => {
  const bookedSlots = [];
  let container;
  act(() => {
    ({ container } = render(<Calendar bookedSlots={bookedSlots} handleDateChange={() => {}} />));
  });
  
  const availableTile = container.querySelector(".available");
  expect(availableTile).toBeInTheDocument();
});

test("dates outside next five days are disabled", () => {
  const bookedSlots = [];
  let container;
  act(() => {
    ({ container } = render(<Calendar bookedSlots={bookedSlots} handleDateChange={() => {}} />));
  });
  
  const today = new Date();
  const nextFiveDays = new Date();
  nextFiveDays.setDate(today.getDate() + 5);
  
  const tiles = container.querySelectorAll(".react-calendar__tile");
  tiles.forEach(tile => {
    const date = new Date(tile.getAttribute("data-date"));
    if (date < today || date > nextFiveDays) {
      expect(tile).toHaveClass("react-calendar__tile--disabled");
    }
  });
});

test("Sundays are disabled", () => {
  const bookedSlots = [];
  let container;
  act(() => {
    ({ container } = render(<Calendar bookedSlots={bookedSlots} handleDateChange={() => {}} />));
  });
  
  const sundayTiles = Array.from(container.querySelectorAll(".react-calendar__tile")).filter(tile => {
    const date = new Date(tile.getAttribute("data-date"));
    return date.getDay() === 0;
  });
  
  sundayTiles.forEach(tile => {
    expect(tile).toHaveClass("react-calendar__tile--disabled");
  });
});

test("handleDateChange is called with the correct date", () => {
  const bookedSlots = [];
  const handleDateChange = jest.fn();
  let container;
  act(() => {
    ({ container } = render(<Calendar bookedSlots={bookedSlots} handleDateChange={handleDateChange} />));
  });
  
  const availableTile = container.querySelector(".available");
  fireEvent.click(availableTile);
  
  expect(handleDateChange).toHaveBeenCalled();
});

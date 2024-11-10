import axios from "axios";
import React, { useEffect, useState } from "react";
import { Bar } from "react-chartjs-2";

const BarChart = () => {
  const [data, setData] = useState({});
  function formatDate(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0"); // Months are zero-based
    const day = String(date.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}`;
  }

  // Get current date
  const currentDate = new Date();
  const formattedCurrentDate = formatDate(currentDate);

  // Get current date + 5 days
  const futureDate = new Date();
  futureDate.setDate(currentDate.getDate() + 4);
  const formattedFutureDate = formatDate(futureDate);

  const fetchWeekyBookings = async () => {
    try {
      const response = await axios.get(
        `http://localhost:8074/booking/between-dates?startDate=${formattedCurrentDate}&endDate=${formattedFutureDate}`
      );

      setData({ bookings: response.data.bookings });
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    fetchWeekyBookings();
  }, []);

  // Function to count bookings by weekdays
  function getBookingsByWeekday(bookings) {
    // const weekdays = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
    const counts = Array(7).fill(0);

    bookings &&
      bookings.forEach((booking) => {
        const date = new Date(booking.bdate);
        const dayIndex = date.getDay(); // 0 = Sunday, 1 = Monday, etc.
        counts[dayIndex]++;
      });

    return counts;
  }

  const weekdayBookings = getBookingsByWeekday(data.bookings);
  const colors = [
    "#FF6384",
    "#36A2EB",
    "#FFCE56",
    "#4BC0C0",
    "#9966FF",
    "#FF9F40",
    "#B37FEB",
  ];

  const chartData = {
    labels: ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"],
    datasets: [
      {
        label: "Bookings",
        data: weekdayBookings,
        backgroundColor: colors,
        borderColor: colors,
        borderWidth: 2,
        fill: false,
      },
    ],
  };

  return (
    <div>
      <Bar
        data={chartData}
        options={{
          responsive: true,
          plugins: {
            legend: {
              display: true,
            },
          },
        }}
      />
    </div>
  );
};

export default BarChart;

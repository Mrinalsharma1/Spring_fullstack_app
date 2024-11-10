import React from "react";
import { useSelector } from "react-redux";

function Temp() {
  const role = useSelector((state) => state.login);
  //console.log(role.name); // This should log the user's role
  return (
    <div>
      <p>User Role: {role.name}</p>
    </div>
  );
}

export default Temp;

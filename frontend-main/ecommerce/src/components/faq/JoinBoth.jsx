import React from "react";
import TrustedClients from "./TrustedClients";
import FAQ from "./FAQ";

function JoinBoth() {
  return (
    <div>
      <div className="container-fluid " style={{marginTop:"-105px"}}>
        <div className="row">
         
          <div className="col-md-6 col-sm-12 mb-4">
            <TrustedClients />
          </div>
          <div className="col-md-6 col-sm-12">
            <FAQ />
          </div>
          
        </div>
      </div>
    </div>
  );
}

export default JoinBoth;

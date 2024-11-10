import React from 'react';
import './ServiceCard.css';

function ServiceCard({ title, description, icon }) {
  return (
    <div className="service-card">

      <div className="card-content">

        <div className="icon-container">
          {icon}
        </div>
        <div className='card-subcontent'>
          <h3>{title}</h3>
          <p>{description}</p>
        </div>


      </div>
    </div>
  );
}

export default ServiceCard;
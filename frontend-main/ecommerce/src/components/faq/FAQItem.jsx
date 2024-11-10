import React, { useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import './FAQ.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlus, faMinus } from '@fortawesome/free-solid-svg-icons';


const FAQItem = ({ question, answer }) => {
  const [isOpen, setIsOpen] = useState(false);

  const toggleFAQ = () => {
    setIsOpen(!isOpen);
  };

  return (
    <div className="faq-item card mb-3">
      <div className="card-header d-flex justify-content-between align-items-center" onClick={toggleFAQ}>
        <h5 className="mb-0" style={{color:"black", fontSize:'15px' }} >{question} </h5>
        <FontAwesomeIcon icon={isOpen ? faMinus : faPlus} />
      </div>
      <div className={`collapse ${isOpen ? 'show' : ''}`}>
        <div className="card-body">
          {answer}
        </div>
      </div>
    </div>
  );
};

export default FAQItem;

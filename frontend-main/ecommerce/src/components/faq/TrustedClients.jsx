import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faWrench } from '@fortawesome/free-solid-svg-icons';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'font-awesome/css/font-awesome.min.css'; 
import './FAQ.css';
import { Carousel, Card } from 'react-bootstrap';
import client1 from '../../assets/images/client-1.jpg';
import client2 from '../../assets/images/client-2.jpg';
import client3 from '../../assets/images/client-3.jpg';
import { FaTools } from "react-icons/fa";


const TrustedClients = () => {

  const CLIENT_LOGOS = [
    {
      id: 1,
      src: client1,
      title: "Tamensica",
      description: "The service was exceptional! The team demonstrated great professionalism, providing quick and reliable solutions that exceeded our expectations.",
    },
    {
      id: 2,
      src: client2,
      title: "Rayan",
      description: "Absolutely fantastic experience! The technicians were knowledgeable and completed the repairs efficiently, making it a stress-free process for us.",
    },
    {
      id: 3,
      src: client3,
      title: "Emelli",
      description: "Highly recommend this service! The quality of work was outstanding, and the team was friendly and attentive to our needs throughout the process.",
    }
    
  ];

 
  
  return (
    <div className="feedback">
    <div className=" mt-5">
    <div className="left-section">
          <h2 className="trusted-clients-heading">Trusted Clients</h2>
          <div><FaTools size={30} style={{ color: "#60c5e4" }} /></div>
    </div>
    
    <Carousel interval={3000} controls={true} indicators={false}> 
      {CLIENT_LOGOS.map((card) => (
        <Carousel.Item style={{padding:"50px"}} key={card.id}>
          <Card className=" reviews-container">
            <Card.Img className='review-card-image' variant="top" src={card.src} alt={card.title} />
            <Card.Body>
              <Card.Text className='review-card-desc' >{card.description}</Card.Text>
              <Card.Title className='review-card-title' >{card.title}</Card.Title>
            </Card.Body>
          </Card>
        </Carousel.Item>
      ))}
    </Carousel>
  </div>
  </div>
 
  );
};

export default TrustedClients;

package com.happiest.bookingservice.service;

import com.happiest.bookingservice.dao.ServiceTypeInterface;
import com.happiest.bookingservice.dto.ServiceType;
import com.happiest.bookingservice.exception.ServiceAlreadyAddedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceTypeService {
    @Autowired
     private ServiceTypeInterface serviceTypeInterface;
    private static final Logger LOGGER = LogManager.getLogger(ServiceTypeService.class);


        public ServiceType addServiceType(ServiceType serviceType) {
            try {
                // Check if the service type already exists
                Optional<ServiceType> existingServiceType = serviceTypeInterface.findById(serviceType.getServiceid()); // Assuming ID check
                if (existingServiceType.isPresent()) {
                    LOGGER.error("Service type already exists: " + serviceType.getServicename());
                    throw new ServiceAlreadyAddedException("Service type with name" + serviceType.getServicename() + " already exists.");
                }

                LOGGER.info("Adding new service type: " + serviceType.getServicename());
                return serviceTypeInterface.save(serviceType);
            } catch (ServiceAlreadyAddedException e) {
                throw e; // Propagate the custom exception
            } catch (Exception e) {
                LOGGER.error("Error while adding service type: " + e.getMessage());
                throw new RuntimeException("Failed to add service type: " + e.getMessage());
            }
        }


    public List<ServiceType> getAllServiceTypes() {
        try {
            LOGGER.info("Fetching all service types.");
            return serviceTypeInterface.findAll();
        } catch (Exception e) {
            LOGGER.error("Error while fetching service types: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve service types.");
        }
    }

}

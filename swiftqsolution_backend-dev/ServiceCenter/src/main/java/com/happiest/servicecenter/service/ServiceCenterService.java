package com.happiest.servicecenter.service;

import com.happiest.servicecenter.constant.PredefinedConstants;
import com.happiest.servicecenter.dao.ServiceCenterInterface;

import com.happiest.servicecenter.dao.UserInterface;
import com.happiest.servicecenter.dto.ServiceCenterEntity;
import com.happiest.servicecenter.dto.UserEntity;
import com.happiest.servicecenter.exception.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceCenterService {

    @Autowired
    private ServiceCenterInterface serviceCenterInterface;

    @Autowired
    private UserInterface userInterface;

    private static final Logger LOGGER = LogManager.getLogger(ServiceCenterService.class);

    // Method to add a new service center
    public ServiceCenterEntity addServiceCenter(ServiceCenterEntity serviceCenter) throws Exception {
        LOGGER.info(PredefinedConstants.SERVICE_LOGGER_ADD_INFO1, serviceCenter.getPincode());
        try {
            Optional<ServiceCenterEntity> existingServiceCenter = serviceCenterInterface.findById(serviceCenter.getPincode());
            if (existingServiceCenter.isPresent()) {
                LOGGER.warn(PredefinedConstants.SERVICE_LOGGER_ADD_WARN, serviceCenter.getPincode());
                throw new ServiceCenterAlreadyRegisteredException(PredefinedConstants.SERVICECENTER_ALREADY_REGISTERED + serviceCenter.getPincode());
            }

            ServiceCenterEntity savedServiceCenter = serviceCenterInterface.save(serviceCenter);
            LOGGER.info(PredefinedConstants.SERVICE_LOGGER_ADD_SUCCESS, savedServiceCenter.getPincode());
            return savedServiceCenter;

        } catch (ServiceCenterAlreadyRegisteredException e) {
            LOGGER.error(PredefinedConstants.SERVICE_LOGGER_ADD_ERROR1, e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error(PredefinedConstants.SERVICE_LOGGER_ADD_ERROR2, e.getMessage(), e);
            throw new Exception(PredefinedConstants.SERVICECENTER_ADD_FAIL + e.getMessage(), e);
        }
    }

    public List<ServiceCenterEntity> getAllServiceCenters() throws Exception {
        LOGGER.info(PredefinedConstants.SERVICE_LOGGER_FETCH_INFO1);
        try {
            List<ServiceCenterEntity> serviceCenters = serviceCenterInterface.findAll();
            if (serviceCenters.isEmpty()) {
                LOGGER.warn(PredefinedConstants.SERVICE_LOGGER_FETCH_WARN);
                throw new NoServiceCenterRegisteredException(PredefinedConstants.NO_SERVICECENTER);
            }
            LOGGER.info(PredefinedConstants.SERVICE_LOGGER_FETCH_SUCCESS, serviceCenters.size());
            return serviceCenters;
        } catch (NoServiceCenterRegisteredException e) {
            LOGGER.error(PredefinedConstants.SERVICE_LOGGER_FETCH_FAIL, e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error(PredefinedConstants.SERVICE_LOGGER_FETCH_ERROR, e.getMessage(), e);
            throw new Exception(PredefinedConstants.SERVICECENTER_FETCH_FAIL + e.getMessage(), e);
        }
    }

    public List<Long> getAllPincodes() throws Exception {
        LOGGER.info(PredefinedConstants.SERVICE_LOGGER_PINCODES_INFO);
        try {
            List<Long> pincodes = serviceCenterInterface.findAllPincodes();
            if (pincodes.isEmpty()) {
                LOGGER.warn(PredefinedConstants.SERVICE_LOGGER_PINCODE_WARN);
                throw new NoServiceCenterRegisteredException();
            }
            LOGGER.info(PredefinedConstants.SERVICE_LOGGER_PINCODE_SUCCESS, pincodes.size());
            return pincodes;
        } catch (NoServiceCenterRegisteredException e) {
            LOGGER.error(PredefinedConstants.SERVICE_LOGGER_PINCODE_FAIL, e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error(PredefinedConstants.SERVICE_LOGGER_PINCODE_ERROR, e.getMessage(), e);
            throw new RuntimeException(PredefinedConstants.SERVICECENTER_PINCODE_FETCH_FAIL + e.getMessage(), e);
        }
    }

    public Long getPincodeByManagerId(long managerId) {
        LOGGER.info(PredefinedConstants.SERVICE_LOGGER_MANAGER_PINCODE_INFO, managerId);
        try {
            Optional<UserEntity> manager = userInterface.findById(managerId);
            if (manager.isEmpty()) {
                LOGGER.warn(PredefinedConstants.SERVICE_LOGGER_MANAGER_PINCODE_WARN, managerId);
                throw new ManagerNotFoundException(PredefinedConstants.MANAGER_NOT_FOUND + managerId);
            }

            Long pincode = serviceCenterInterface.findPincodeByManagerId(managerId);
            LOGGER.info(PredefinedConstants.SERVICE_LOGGER_MANAGER_PINCODE_SUCCESS, pincode, managerId);
            return pincode;
        }catch(ManagerNotAssignedException e){
            LOGGER.error(PredefinedConstants.SERVICE_LOGGER_MANAGER_PINCODE_FAIL, e.getMessage());
            throw e;

        } catch (Exception e) {
            LOGGER.error(PredefinedConstants.SERVICE_LOGGER_MANAGER_PINCODE_ERROR, e.getMessage(), e);
            throw new RuntimeException(PredefinedConstants.SERVICECENTER_PINCODE_FETCH_FAIL + e.getMessage(), e);
        }
    }

    public ServiceCenterEntity detachManagerFromServiceCenter(long pincode) throws ServiceCenterNotFoundException, NoManagerAssignedException {
        LOGGER.info(PredefinedConstants.SERVICE_LOGGER_REMOVE_MANAGER_INFO, pincode);
        try {
            Optional<ServiceCenterEntity> serviceCenterOpt = serviceCenterInterface.findById(pincode);
            if (serviceCenterOpt.isPresent()) {
                ServiceCenterEntity serviceCenter = serviceCenterOpt.get();
                if (serviceCenter.getManagerId() == null) {
                    LOGGER.warn(PredefinedConstants.SERVICE_LOGGER_REMOVE_MANAGER_WARN, pincode);
                    throw new NoManagerAssignedException(PredefinedConstants.NO_MANAGER_ASSIGNED);
                }

                serviceCenter.setManagerId(null);
                ServiceCenterEntity updatedServiceCenter = serviceCenterInterface.save(serviceCenter);
                LOGGER.info(PredefinedConstants.SERVICE_LOGGER_REMOVE_MANAGER_SUCCESS, pincode);
                return updatedServiceCenter;
            } else {
                LOGGER.error(PredefinedConstants.SERVICE_LOGGER_REMOVE_MANAGER_FAIL, pincode);
                throw new ServiceCenterNotFoundException(PredefinedConstants.NO_SERVICECENTER + pincode);
            }
        } catch (NoManagerAssignedException | ServiceCenterNotFoundException e) {
            LOGGER.error(PredefinedConstants.NO_MANAGER_ASSIGNED, e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error(PredefinedConstants.SERVICE_LOGGER_REMOVE_MANAGER_ERROR, e.getMessage(), e);
            throw new RuntimeException(PredefinedConstants.SERVICE_LOGGER_REMOVE_MANAGER_ERROR + e.getMessage(), e);
        }
    }

    public long countServiceCenters() {
        try {
            return serviceCenterInterface.count();
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while counting service centers: " + e.getMessage(), e);
        }
    }





}

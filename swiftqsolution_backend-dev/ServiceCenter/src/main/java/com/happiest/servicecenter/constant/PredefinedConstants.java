package com.happiest.servicecenter.constant;

public class PredefinedConstants {

    //--------------------------------Service center Exceptions-----------------------------------
    public static final String NO_SERVICECENTER="No service center has been registered";
    public static final String SERVICECENTER_ADD_FAIL ="Error occurred: error o while adding service center: ";
    public static final String  SERVICECENTER_ALREADY_REGISTERED ="Error occurred: Service center already registered with this pincode";
    public static final String  SERVICECENTER_FETCH_FAIL ="Error occurred : error while fetching service centers: ";
    public static final String  SERVICECENTER_PINCODE_FETCH_FAIL ="Error occurred: error while fetching pincodes: ";
    public static final String MAA="This manager already has been assigned to a service center";
    public static final String SCNF="No service center in this pincode";
    public static final String NUF="No user found  in this email";
    public static final String MANAGER_NOT_FOUND ="No Manager found with this email";
    public static final String MANAGER_NOT_ASSIGNED="This manager has not assigned to any of the service center";
    public static final String RCBC="Role cannot be changed for admin";
    public static final String NSCWM="There are no service centers without manager";
    public static final String SCAHM="This service center already has a manager assigned";
    public static final String UAE="Unauthorized access";
    public static final String NO_MANAGER_ASSIGNED="No manager has assigned to this service center";
    public static final String SCWM="service center has manager";



    //--------------------------------Service center Logger constants-----------------------------------
    //-----------------------------------Servicecenter controller--------------------------------------
    public static final String SERVICE_LOGGER_ADD_INFO1 ="Attempting to add new service center with pincode:";
    public static final String SERVICE_LOGGER_ADD_WARN = "Service center already registered with pincode:";
    public static final String SERVICE_LOGGER_ADD_SUCCESS ="Service center added successfully with pincode:";
    public static final String SERVICE_LOGGER_ADD_ERROR1 ="Service center already registered";
    public static final String SERVICE_LOGGER_ADD_ERROR2 ="Error while adding service center: {}";

    public static final String SERVICE_LOGGER_FETCH_INFO1 ="Fetching all service centers";
    public static final String SERVICE_LOGGER_FETCH_WARN ="No service centers found";
    public static final String SERVICE_LOGGER_FETCH_SUCCESS="Retrieved service centers";
    public static final String SERVICE_LOGGER_FETCH_FAIL="No service centers registered: ";
    public static final String SERVICE_LOGGER_FETCH_ERROR="Error while fetching service centers: {}";

    public static final String SERVICE_LOGGER_PINCODES_INFO="Fetching all pincodes of service centers";
    public static final String SERVICE_LOGGER_PINCODE_WARN="No service center pincodes found";
    public static final String SERVICE_LOGGER_PINCODE_SUCCESS="Retrieved {} pincodes successfully";
    public static final String SERVICE_LOGGER_PINCODE_FAIL="No service centers registered: {}";
    public static final String SERVICE_LOGGER_PINCODE_ERROR="Error while fetching pincodes: {}";


    public static final String SERVICE_LOGGER_MANAGER_PINCODE_INFO="Fetching pincode for manager with ID: {}";
    public static final String SERVICE_LOGGER_MANAGER_PINCODE_WARN="Manager not found with ID: {}";
    public static final String SERVICE_LOGGER_MANAGER_PINCODE_SUCCESS="Found pincode {} for manager ID: {}";
    public static final String SERVICE_LOGGER_MANAGER_PINCODE_FAIL="This manager has not assigned to any of the service center";
    public static final String SERVICE_LOGGER_MANAGER_PINCODE_ERROR="Error fetching the pincode";

    public static final String SERVICE_LOGGER_REMOVE_MANAGER_INFO="Removing manager from service center with pincode: {}";
    public static final String SERVICE_LOGGER_REMOVE_MANAGER_WARN="No manager assigned to service center with pincode: {}";
    public static final String SERVICE_LOGGER_REMOVE_MANAGER_SUCCESS="Manager removed successfully from service center with pincode: {}";
    public static final String SERVICE_LOGGER_REMOVE_MANAGER_FAIL="Manager removed successfully from service center with pincode: {}";
    public static final String SERVICE_LOGGER_REMOVE_MANAGER_ERROR="Error while detaching manager from service center: ";

    //----------------------------service center messages------------------------------------
    public static final String SERVICECENTER_ADD_SUCCESS="Service center added successfully";
    public static final String SERVICECENTER_FETCH_SUCCESS="Service centers fetched successfully";







}

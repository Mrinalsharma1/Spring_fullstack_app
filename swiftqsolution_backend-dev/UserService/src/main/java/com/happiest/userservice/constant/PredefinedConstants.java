package com.happiest.userservice.constant;

public class PredefinedConstants {
    //---------------------------------exception constants start here-----------------------
    public static final String NO_USER_REGISTERED="No user has been registered";
    public static final String USER_NOT_REGISTERED="User has not been registered";
    public static final String USER_ALREADY_EXISTS ="User with this email id is already registered";
    public static final String USER_NOT_ADDED ="An error occurred while fetching the user";
    public static final String USER_NOT_UPDATED ="Failed to update user profile";

    //--------------------logger constants starts here--------------------------------
    //-------------------- usercontroller----------------------------------
    //-------------------- register method----------------------------------
    public static final String LOGGER_REGISTER_INFO1="Registering user with Username: ";
    public static final String LOGGER_REGISTER_ERROR="User with Username already exists";
    public static final String LOGGER_REGISTER_INFO2 ="User with Username registered successfully. ";

    //-------------------- fetchByUserId method----------------------------------
    public static final String LOGGER_REGISTER_FETCH_INFO1 ="Fetching user details by id: ";
    public static final String LOGGER_REGISTER_FETCH_INFO2 ="User fetched successfully";
    public static final String LOGGER_REGISTER_FETCH_ERROR1 ="User not found with this id: ";
    public static final String LOGGER_REGISTER_FETCH_ERRO2="An error occurred while fetching the user: ";

    //-------------------- updateUserProfile method----------------------------------
    public static final String LOGGER_REGISTER_UPDATE_INFO1="Updating user profile for user with this id: ";
    public static final String LOGGER_REGISTER_UPDATE_INFO2="Profile name updated to: ";
    public static final String LOGGER_REGISTER_UPDATE_INFO3="Email updated to: ";
    public static final String LOGGER_REGISTER_UPDATE_WARN="No updates made for user ID: ";
    public static final String LOGGER_REGISTER_UPDATE_ERROR1="User not found with ID: ";
    public static final String LOGGER_REGISTER_UPDATE_ERROR2="An error occurred while updating user profile: ";

    //---------------------------logger constants end here-----------------------------------


    //---------------------------userservice-----------------------------------
    public static final String RESPONSE_MESSAGE_SUCCESS_REGISTER="User registered successfully";
    public static final String RESPONSE_MESSAGE_FAIL_REGISTER= "Error occurred while registering user";
    public static final String RESPONSE_MESSAGE_PASSWORD_LINK="Password reset link sent to your email";
    public static final String RESPONSE_MESSAGE_FAIL_FORGOT_PASSWORD="Error occurred while processing forgot password request";





}


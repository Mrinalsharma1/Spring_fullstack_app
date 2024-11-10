package com.happiest.servicecenter.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CenterInfo {
    private long id;
    private String profileName;
    private long pincode;
    private String address;
    private String city;
    private String state;

}

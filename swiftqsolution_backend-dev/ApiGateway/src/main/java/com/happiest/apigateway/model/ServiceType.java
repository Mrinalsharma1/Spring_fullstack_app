package com.happiest.apigateway.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class ServiceType {

    private long serviceId;
    private String serviceName;
    private String description;

}


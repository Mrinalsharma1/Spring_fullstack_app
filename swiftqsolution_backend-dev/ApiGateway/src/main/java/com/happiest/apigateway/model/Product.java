package com.happiest.apigateway.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Product {

    private long productId;
    private String productName;

}

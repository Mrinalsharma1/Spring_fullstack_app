package com.happiest.userservice.dto;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "newsletter")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsLetter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-generated ID
    @Column(name = "nid")
    private Long nid;

    @Column(name = "email", nullable = false, unique = true)
    private String email;


}



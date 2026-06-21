package com.example.matchingbab.restaurant.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long schoolId;
    @Enumerated(EnumType.STRING)
    private Category category;
    private String name;
    private String description;
    private String location;
    @Enumerated(EnumType.STRING)
    private Mood mood;
    private Integer maxPrice;
    private Boolean isPartner;
    private Integer averagePrice;
}
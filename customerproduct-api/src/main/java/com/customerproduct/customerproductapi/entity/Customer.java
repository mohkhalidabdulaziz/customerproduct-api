package com.customerproduct.customerproductapi.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "customer")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "office_email")
    private String officeEmail;

    @Column(name = "personal_email")
    private String personalEmail;

    @Column(name = "family_members")
    private int familyMembers;
}

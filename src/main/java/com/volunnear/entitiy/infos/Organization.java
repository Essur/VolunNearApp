package com.volunnear.entitiy.infos;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@Table(name = "organizations")
@AllArgsConstructor
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "organization_id", nullable = false)
    private Integer id;

    @Size(max = 45)
    @Column(name = "username", length = 45)
    private String username;

    @Size(max = 45)
    @Column(name = "email", length = 45)
    private String email;

    @Size(max = 45)
    @Column(name = "name", length = 45)
    private String name;

    @Size(max = 45)
    @Column(name = "address", length = 45)
    private String address;

    @Size(max = 45)
    @Column(name = "city", length = 45)
    private String city;

    @Size(max = 45)
    @Column(name = "country", length = 45)
    private String country;

}
package com.volunnear.entity.infos;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "organization_group_links")
public class OrganizationGroupLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_link_id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Size(max = 45)
    @Column(name = "social_network", length = 45)
    private String socialNetwork;

    @Size(max = 225)
    @Column(name = "link", length = 225)
    private String link;

}
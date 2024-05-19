package com.volunnear.entitiy.infos;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "organisation_group_links")
public class OrganisationGroupLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_link_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisation_id")
    private Organisation organisation;

    @Size(max = 45)
    @Column(name = "social_network", length = 45)
    private String socialNetwork;

    @Size(max = 225)
    @Column(name = "link", length = 225)
    private String link;

}
package com.volunnear.entitiy.infos;

import com.volunnear.entitiy.activities.Activity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "activity_chat_links")
public class ActivityChatLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_link_id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "activity_id")
    private Activity activity;

    @Size(max = 45)
    @NotNull
    @Column(name = "social_network", nullable = false, length = 45)
    private String socialNetwork;

    @Size(max = 225)
    @NotNull
    @Column(name = "link", nullable = false, length = 225)
    private String link;

}
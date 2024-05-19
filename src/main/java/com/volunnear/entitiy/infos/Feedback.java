package com.volunnear.entitiy.infos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "feedbacks")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "rate", nullable = false)
    private Integer rate;

    @Size(max = 225)
    @NotNull
    @Column(name = "description", nullable = false, length = 225)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "volunteer_feedback_author_id")
    private Volunteer volunteerFeedbackAuthor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_organisation_id")
    private Organisation targetOrganisation;

}
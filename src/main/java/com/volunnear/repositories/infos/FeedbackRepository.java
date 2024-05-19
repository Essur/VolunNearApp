package com.volunnear.repositories.infos;

import com.volunnear.entitiy.infos.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    List<Feedback> findAllByTargetOrganisation_Id(Integer id);
}
package com.volunnear.repositories.infos;

import com.volunnear.entity.infos.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    List<Feedback> findAllByTargetOrganization_Id(Integer id);
    Feedback findFeedbackById(Integer id);
    boolean existsFeedbackByVolunteerFeedbackAuthor_User_Username(String username);
}
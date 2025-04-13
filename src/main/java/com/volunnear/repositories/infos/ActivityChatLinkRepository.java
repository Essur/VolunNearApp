package com.volunnear.repositories.infos;

import com.volunnear.entity.infos.ActivityChatLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivityChatLinkRepository extends JpaRepository<ActivityChatLink, Integer> {
    boolean existsByActivity_Id(Integer id);

    Optional<ActivityChatLink> findActivityChatLinkByActivity_Id(Integer id);
}
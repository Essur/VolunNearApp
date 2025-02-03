package com.volunnear.repositories.infos;

import com.volunnear.entitiy.infos.ActivityChatLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivityChatLinkRepository extends JpaRepository<ActivityChatLink, Integer> {
    boolean existsByActivity_Id(Integer id);

    Optional<ActivityChatLink> findActivityChatLinkByActivity_Id(Integer id);
}
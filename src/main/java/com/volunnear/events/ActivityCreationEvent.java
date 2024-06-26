package com.volunnear.events;

import com.volunnear.dtos.ActivityNotificationDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ActivityCreationEvent extends ApplicationEvent {
    private final String status;
    private final ActivityNotificationDTO notificationDTO;

    public ActivityCreationEvent(Object source, ActivityNotificationDTO notificationDTO, String status) {
        super(source);
        this.notificationDTO = notificationDTO;
        this.status = status;
    }
}

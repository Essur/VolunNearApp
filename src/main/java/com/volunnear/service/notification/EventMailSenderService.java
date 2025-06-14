package com.volunnear.service.notification;

import com.volunnear.dto.ActivityNotificationDTO;
import com.volunnear.dto.response.ActivityDTO;
import com.volunnear.entity.infos.VolunteersSubscription;
import com.volunnear.event.ActivityCreationEvent;
import com.volunnear.repository.infos.VolunteersSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventMailSenderService {
    private final JavaMailSender javaMailSender;
    private final VolunteersSubscriptionRepository subscriptionRepository;

    @Async
    @EventListener(ActivityCreationEvent.class)
    public void sendNotificationForSubscribers(ActivityCreationEvent activityCreationEvent) {
        ActivityNotificationDTO notificationDTO = activityCreationEvent.getNotificationDTO();
        List<VolunteersSubscription> subscriptions = subscriptionRepository.findAllByOrganization_Id(notificationDTO.getOrganizationResponseDTO().getId());
        Map<String, String> usernameAndEmailMap = subscriptions.stream().collect(Collectors.toMap(
                volunteerNotificationSubscription -> volunteerNotificationSubscription.getVolunteer().getUser().getUsername(),
                volunteerNotificationSubscription -> volunteerNotificationSubscription.getVolunteer().getEmail(),
                (existingEmail, newEmail) -> existingEmail
        ));
        ActivityDTO activityDTO = notificationDTO.getActivityDTO();
        for (Map.Entry<String, String> usernameAndEmailMapEntry : usernameAndEmailMap.entrySet()) {
            String email = usernameAndEmailMapEntry.getValue();
            if (validateEmail(email)) {
                SimpleMailMessage mailMessage = getSimpleMailMessage(activityCreationEvent.getStatus(), notificationDTO, activityDTO);
                mailMessage.setTo(email);
                javaMailSender.send(mailMessage);
            }
        }
    }

    private boolean validateEmail(String email) {
        String regexPattern = "^(.+)@(\\S+)$";
        return Pattern.compile(regexPattern).matcher(email).matches();
    }

    private static SimpleMailMessage getSimpleMailMessage(String status, ActivityNotificationDTO notificationDTO, ActivityDTO activityDTO) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject(status + " activity from " + notificationDTO.getOrganizationResponseDTO().getOrganizationName() + " !");
        mailMessage.setText("There is info about activity: "
                + "\nTitle: " + activityDTO.getTitle()
                + "\nKind of activity: " + activityDTO.getKindOfActivity()
                + "\nDescription: " + activityDTO.getDescription()
                + "\nCountry and city: " + activityDTO.getCountry() + "/" + activityDTO.getCity()
                + "\nDate of place: " + activityDTO.getDateOfPlace());
        mailMessage.setFrom("volunNearAppTeam@gmail.com");
        return mailMessage;
    }
}

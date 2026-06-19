package ru.hits.just_4sport.service.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import ru.hits.just_4sport.repository.EventRepository;
import ru.hits.just_4sport.service.notification.event.EventCancelledEvent;

import java.util.HashSet;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final EventRepository eventRepository;
    private final EmailService emailService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEventCancelled(EventCancelledEvent event) {
        var eventEntity = eventRepository.findEventEntitiesById(event.eventId())
                .orElse(null);

        if (eventEntity == null) {
            log.warn("No event with id {} found", event.eventId());
            return;
        }

        var participants = new HashSet<String>();

        for (var team : eventEntity.getTeams()) {
            participants.add(team.getCaptain().getEmail());

            for (var member : team.getTeamMembers()) {
                participants.add(member.getEmail());
            }
        }

        participants.remove(eventEntity.getAuthor().getEmail());

        for (var email : participants) {
            try {
                emailService.sendEmail(
                        email,
                        "Мероприятие отменено",
                        buildEventCancelledText(eventEntity.getName())
                );
            } catch (Exception exception) {
                log.error("Failed to send event cancellation email to {}", email, exception);
            }
        }
    }

    private String buildEventCancelledText(String eventName) {
        return """
                Добрый день!
                
                К сожалению, вынуждены сообщить, что событие "%s" было отменено его организатором :(
                
                За всеми подробностями можете обратиться к автору мероприятия

                С уважением,
                команда JUST4SPORT
                """.formatted(eventName);
    }
}

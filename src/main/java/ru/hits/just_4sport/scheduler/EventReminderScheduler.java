package ru.hits.just_4sport.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.hits.just_4sport.repository.EventRepository;
import ru.hits.just_4sport.service.notification.EmailService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventReminderScheduler {

    private final EventRepository eventRepository;
    private final EmailService emailService;

    @Scheduled(cron = "0 0 17 30 * *", zone = "Asia/Novosibirsk")
    @Transactional(readOnly = true)
    public void sendTomorrowEventReminders() {
        var tomorrow = LocalDate.now().plusDays(1);

        var dayStart = LocalDateTime.of(tomorrow, LocalTime.MIN);
        var dayEnd = LocalDateTime.of(tomorrow, LocalTime.MAX);

        var events = eventRepository.findAllByDateStartBetween(
                dayStart,
                dayEnd
        );

        for (var event : events) {
            var participants = new HashSet<String>();

            for (var team : event.getTeams()) {
                participants.add(team.getCaptain().getEmail());

                for (var member : team.getTeamMembers()) {
                    participants.add(member.getEmail());
                }
            }

            for (var email : participants) {
                try {
                    emailService.sendEmail(
                            email,
                            "Напоминание о мероприятии",
                            buildReminderText(
                                    event.getName(),
                                    event.getDateStart(),
                                    event.getPlace()
                            )
                    );
                } catch (Exception exception) {
                    log.error("Failed to send event reminder email to {}", email, exception);
                }
            }
        }
    }

    private String buildReminderText(
            String eventName,
            LocalDateTime dateStart,
            String place
    ) {
        return """
                Добрый день!

                Напоминаем, что вы регистрировались на событие "%s", которое состоится уже завтра!

                Дата и время начала: %s
                Место проведения: %s

                С уважением,
                команда JUST4SPORT
                """.formatted(eventName, dateStart, place);
    }
}

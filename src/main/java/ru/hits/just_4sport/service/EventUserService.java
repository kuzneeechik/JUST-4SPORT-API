package ru.hits.just_4sport.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.hits.just_4sport.model.api.event.EventFilterModel;
import ru.hits.just_4sport.model.api.event.EventShortModel;
import ru.hits.just_4sport.model.enums.SortDirection;
import ru.hits.just_4sport.model.enums.SortField;
import ru.hits.just_4sport.model.mapper.EventMapper;
import ru.hits.just_4sport.repository.EventsRepository;
import ru.hits.just_4sport.utils.EventSpecificationUtility;

@Service
public class EventUserService {

    private final EventsRepository eventsRepository;
    private final EventMapper eventMapper;

    public EventUserService(EventsRepository eventsRepository, EventMapper eventMapper) {
        this.eventsRepository = eventsRepository;
        this.eventMapper = eventMapper;
    }

    public Page<EventShortModel> getFilteredEvents(
            EventFilterModel filter,
            SortField sortField,
            SortDirection sortDirection,
            int page,
            int size
    ) {
        var filtering = EventSpecificationUtility.getFilteredEvents(filter);

        var pageable = PageRequest.of(
                page,
                size,
                getSort(sortField, sortDirection));

        return eventsRepository.findAll(filtering, pageable)
                .map(eventMapper::toShortModel);
    }

    private Sort getSort(SortField sortField, SortDirection sortDirection) {
        var direction = SortDirection.DESC.equals(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;

        return switch (sortField) {
            case DATE -> Sort.by(direction, "startDate");
            case SortField.COST -> Sort.by(direction, "cost");
        };
    }
}

package ru.hits.just_4sport.utils;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.hits.just_4sport.model.api.event.EventFilterModel;
import ru.hits.just_4sport.model.domain.EventEntity;

import java.util.ArrayList;

@UtilityClass
public class EventSpecificationUtility {

    public Specification<EventEntity> getFilteredEvents(EventFilterModel filter) {
        var specificationPredicates = new ArrayList<Specification<EventEntity>>();

        if (filter == null) {
            return Specification.allOf();
        }

        if (filter.getEventStatus() != null) {
            specificationPredicates.add((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("eventStatus"), filter.getEventStatus()));
        }

        if (filter.getName() != null) {
            specificationPredicates.add((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("name")),
                            "%" + filter.getName().toLowerCase() + "%"
                    ));
        }

        if (filter.getDateStart() != null) {
            specificationPredicates.add((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("dateStart"), filter.getDateStart()));
        }

        if (filter.getDateEnd() != null) {
            specificationPredicates.add((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("dateEnd"), filter.getDateEnd()));
        }

        if (filter.getCostStart() != null) {
            specificationPredicates.add((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("cost"), filter.getCostStart()));
        }

        if (filter.getCostEnd() != null) {
            specificationPredicates.add((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("cost"), filter.getCostEnd()));
        }

        if (filter.getEventType() != null) {
            specificationPredicates.add((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("eventType"), filter.getEventType()));
        }

        if (filter.getSport() != null) {
            specificationPredicates.add((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("sport"), filter.getSport()));
        }

        if (filter.getSkillLevel() != null) {
            specificationPredicates.add((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("skillLevel"), filter.getSkillLevel()));
        }

        return Specification.allOf(specificationPredicates);
    }
}

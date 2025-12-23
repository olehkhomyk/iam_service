package com.post_hub.iam_service.repository.criteria;

import com.post_hub.iam_service.model.entities.User;
import com.post_hub.iam_service.model.request.user.UserSearchRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class UserSearchCriteria implements Specification<User> {
	private final UserSearchRequest request;

	@Override
	public Predicate toPredicate(@NotNull Root<User> root, CriteriaQuery<?> query, @NotNull CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = new ArrayList<>();

		if (Objects.nonNull(request.getUsername())) {
			Predicate predicate = criteriaBuilder.like(
					criteriaBuilder.lower(root.get(User.USERNAME_FIELD_NAME)),
					"%" + request.getUsername().toLowerCase() + "%"
			);
			predicates.add(predicate);
		}

		if (Objects.nonNull(request.getEmail())) {
			Predicate predicate = criteriaBuilder.like(
					criteriaBuilder.lower(root.get(User.EMAIL_FIELD_NAME)),
					"%" + request.getEmail().toLowerCase() + "%"
			);
			predicates.add(predicate);
		}

		if (Objects.nonNull(request.getDeleted())) {
			Predicate predicate = criteriaBuilder.equal(root.get(User.DELETED_FIELD_NAME), request.getDeleted());
			predicates.add(predicate);
		}

		if (Objects.nonNull(request.getKeyword())) {
			String keyword = request.getKeyword().toLowerCase();
			Predicate predicate = criteriaBuilder.or(
					criteriaBuilder.like(
							criteriaBuilder.lower(root.get(User.USERNAME_FIELD_NAME)),
							"%" + keyword + "%"
					),
					criteriaBuilder.like(
							criteriaBuilder.lower(root.get(User.EMAIL_FIELD_NAME)),
							"%" + keyword + "%"
					)
			);
			predicates.add(predicate);
		}

		sort(root, criteriaBuilder, query);

		return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	}

	private void sort(Root<User> root, CriteriaBuilder criteriaBuilder, CriteriaQuery<?> query) {
		if (Objects.nonNull(request.getSortField())) {
			switch (request.getSortField()) {
				case USERNAME -> query.orderBy(criteriaBuilder.asc(root.get(User.USERNAME_FIELD_NAME)));
				case EMAIL -> query.orderBy(criteriaBuilder.asc(root.get(User.EMAIL_FIELD_NAME)));
				case LAST_LOGIN -> query.orderBy(criteriaBuilder.desc(root.get(User.LAST_LOGIN_FIELD_NAME)));
				default -> query.orderBy(criteriaBuilder.desc(root.get(User.ID_FIELD_NAME)));
			}
		} else {
			query.orderBy(criteriaBuilder.desc(root.get(User.ID_FIELD_NAME)));
		}
	}
}

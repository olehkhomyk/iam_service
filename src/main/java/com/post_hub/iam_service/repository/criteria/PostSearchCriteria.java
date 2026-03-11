package com.post_hub.iam_service.repository.criteria;

import com.post_hub.iam_service.model.entity.Post;
import com.post_hub.iam_service.model.request.post.PostSearchRequest;
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
public class PostSearchCriteria implements Specification<Post> {
	private final PostSearchRequest request;

	@Override
	public Predicate toPredicate(
			@NotNull Root<Post> root,
			CriteriaQuery<?> query,
			@NotNull CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = new ArrayList<>();

		if (Objects.nonNull(request.getTitle())) {
			Predicate predicate = criteriaBuilder.like(
					criteriaBuilder.lower(root.get(Post.TITLE_FIELD_NAME)),
					"%" + request.getTitle().toLowerCase() + "%"
			);
			predicates.add(predicate);
		}
		if (Objects.nonNull(request.getContent())) {
			Predicate predicate = criteriaBuilder.like(
					criteriaBuilder.lower(root.get(Post.CONTENT_FIELD_NAME)),
					"%" + request.getContent().toLowerCase() + "%"
			);
			predicates.add(predicate);
		}

		if (Objects.nonNull(request.getDeleted())) {
			Predicate predicate = criteriaBuilder.equal(root.get(Post.DELETED_FIELD_NAME), request.getDeleted());
			predicates.add(predicate);
		}

		if (Objects.nonNull(request.getKeyword())) {
			String keyword = request.getKeyword().toLowerCase();
			Predicate predicate = criteriaBuilder.or(
					criteriaBuilder.like(
							criteriaBuilder.lower(root.get(Post.TITLE_FIELD_NAME)),
							"%" + keyword + "%"
					),
					criteriaBuilder.like(
							criteriaBuilder.lower(root.get(Post.CONTENT_FIELD_NAME)),
							"%" + keyword + "%"
					)
			);
			predicates.add(predicate);
		}

		sort(root, criteriaBuilder, query);

		return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	}

	private void sort(Root<Post> root, CriteriaBuilder criteriaBuilder, CriteriaQuery<?> query) {
		if (Objects.nonNull(request.getSortField())) {
			switch (request.getSortField()) {
				case TITLE -> query.orderBy(criteriaBuilder.asc(root.get(Post.TITLE_FIELD_NAME)));
				case CONTENT -> query.orderBy(criteriaBuilder.asc(root.get(Post.CONTENT_FIELD_NAME)));
				default -> query.orderBy(criteriaBuilder.desc(root.get(Post.ID_FIELD_NAME)));
			}
		} else {
			query.orderBy(criteriaBuilder.desc(root.get(Post.ID_FIELD_NAME)));
		}
	}
}

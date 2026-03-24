package com.post_hub.iam_service.model.dto.postLike;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostLikeDTO implements Serializable {
    private Long id;
    private Long userId;
    private LocalDateTime createdAt;
}

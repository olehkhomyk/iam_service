package com.post_hub.iam_service.model.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO implements Serializable {
    private Integer id;
    private String content;
    private String createdAt;
    private String createdBy;
}

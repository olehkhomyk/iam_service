package com.post_hub.iam_service.model.dto.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDTO implements Serializable {
	private String key;
	private String url;
}

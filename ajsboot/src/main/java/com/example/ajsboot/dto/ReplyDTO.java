package com.example.ajsboot.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDTO {

	//CustomRestAdvice 이후에 RestcontrollerAdvice를 이용하는 예시를 사용하기위해서
	// ReplyDTO에 검증과 관련된 어노테이션인 @notnull 과 @notempty를 넣어줍니다.
	
	private Long rno;
	
	@NotNull
	private Long bno;

	@NotEmpty
	private String replyText;
	
	@NotEmpty
	private String replyer;
	
	private LocalDateTime regDate, modDate;
}

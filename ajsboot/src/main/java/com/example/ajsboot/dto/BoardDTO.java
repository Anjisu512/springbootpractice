package com.example.ajsboot.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {

	private Long bno;
	
	@NotEmpty //javax꺼로 임포트
	@Size(min = 3, max=100) //javax
	private String title;
	
	@NotEmpty
	private String content;
	
	@NotEmpty
	private String writer;
	
	private LocalDateTime regDate;
	private LocalDateTime modDate;
	
}

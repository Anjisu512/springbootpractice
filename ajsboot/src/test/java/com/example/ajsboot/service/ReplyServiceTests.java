package com.example.ajsboot.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.ajsboot.dto.ReplyDTO;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class ReplyServiceTests {

	
	@Autowired
	private ReplyService replyService;
	
	//댓글등록 테스트
	@Test
	public void testRegister() {
		
		ReplyDTO replyDTO = ReplyDTO.builder()
				.replyText("댓글등록 테스트")
				.replyer("댓글등록테스터")
				.bno(100L)
				.build();
		log.info(replyService.register(replyDTO));
		
	}
	
}

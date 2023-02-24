package com.example.ajsboot.repository;

import static org.mockito.ArgumentMatchers.booleanThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.ajsboot.domain.Board;
import com.example.ajsboot.domain.Reply;
import com.example.ajsboot.dto.BoardListReplyCountDTO;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class ReplyRepositoryTests {
	
	@Autowired
	private ReplyRepository replyRepository;
	

	@Test	//댓글 작성 테스트
	public void testInsert() {
		//실제 DB에 있는bno
		Long bno = 100L;
		Board board = Board.builder().bno(bno).build();
		
		Reply reply = Reply.builder()
				.board(board)
				.replyText("댓글....")
				.replyer("replyer1")
				.build();
		
		replyRepository.save(reply);
		
	}

	//댓글 리스트처리 (페이징처리)
	@Test
	public void testBoardReplies() {
		Long bno = 100L;
		
		Pageable pageable = PageRequest.of(0, 10,Sort.by("rno").descending());
		
		Page<Reply> result = replyRepository.listOfBoard(bno, pageable);
		
		result.getContent().forEach(reply -> {
			log.info(reply);
		});
	}
	
	
	
}

package com.example.ajsboot.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.ajsboot.dto.BoardDTO;
import com.example.ajsboot.dto.PageRequestDTO;
import com.example.ajsboot.dto.PageResponseDTO;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class BoardServiceTests {

	@Autowired
	private BoardService boardService;
	
	@Test
	public void testRegister() {
		log.info(boardService.getClass().getName()); 
		
		BoardDTO boardDTO = BoardDTO.builder()
				.title("sample Title...")
				.content("sample content...")
				.writer("user00")
				.build();
		long bno = boardService.register(boardDTO);
		log.info("bno : " + bno);
	}
	
	@Test
	public void testmodify() {
		//변경에 필요한 데이터만
		BoardDTO boardDTO = BoardDTO.builder()
				.bno(99l)
				.title("update....")
				.content("update...")
				.build();
		
		boardService.modify(boardDTO);
	}
	
	@Test
	public void testList() {
		PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
				.type("tcw")
				.keyword("1")
				.page(1)
				.size(10)
				.build();
		
		PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);
		log.info(responseDTO);
	}
	
}

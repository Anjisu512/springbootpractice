package com.example.ajsboot.service;

import com.example.ajsboot.dto.BoardDTO;
import com.example.ajsboot.dto.BoardListReplyCountDTO;
import com.example.ajsboot.dto.PageRequestDTO;
import com.example.ajsboot.dto.PageResponseDTO;

public interface BoardService {
	
	
	//게시글 등록
	long register(BoardDTO boardDTO);
	
	//게시글 조회
	BoardDTO readOne(long bno);
	
	//게시글 수정
	void modify(BoardDTO boardDTO);
	
	//삭제
	void remove(long bno);
	
	//게시글 검색 & list처리
	PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);
	
	//댓글의 숫자까지 처리(게시글 옆에 댓글수 나타내는 메소드)
	PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);
	
	
}

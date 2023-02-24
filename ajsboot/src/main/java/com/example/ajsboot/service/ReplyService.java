package com.example.ajsboot.service;

import com.example.ajsboot.dto.PageRequestDTO;
import com.example.ajsboot.dto.PageResponseDTO;
import com.example.ajsboot.dto.ReplyDTO;

public interface ReplyService {
	
	//댓글 등록처리
	Long register(ReplyDTO replyDTO);
	
	// 댓글 조회(상세보기)
	ReplyDTO read(Long rno);
	
	// 댓글 수정
	void modify(ReplyDTO replyDTO);
	
	// 댓글 삭제
	void remove(Long rno);
	
	//특정게시물의 댓글 목록처리
	PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO);
	
}

package com.example.ajsboot.repository.search;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.ajsboot.domain.Board;
import com.example.ajsboot.dto.BoardListReplyCountDTO;

public interface BoardSearch {
		
	//페이징처리하기		springframework pageable 임포트 awt => X
	Page<Board> search1(Pageable pageable);
	
	//검색기능넣기
	Page<Board> searchAll(String[] types, String keyword, Pageable pageable);
	
	//댓글 목록처리하기
	Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types,
													String keyword,
													Pageable pageable);
	
}

package com.example.ajsboot.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.ajsboot.domain.Board;
import com.example.ajsboot.dto.BoardDTO;
import com.example.ajsboot.dto.BoardListReplyCountDTO;
import com.example.ajsboot.dto.PageRequestDTO;
import com.example.ajsboot.dto.PageResponseDTO;
import com.example.ajsboot.repository.BoardRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional //javax꺼로 임포트
public class BoardServiceImpl implements BoardService {
	
	private final ModelMapper modelMapper;
	private final BoardRepository boardRepository;
	
	//등록하기
	@Override
	public long register(BoardDTO boardDTO) {
		Board board = modelMapper.map(boardDTO, Board.class);
		long bno = boardRepository.save(board).getBno();

		return bno;
	}
	
	//조회하기
	@Override
	public BoardDTO readOne(long bno) {
		Optional<Board> result = boardRepository.findById(bno); //bno를 찾아서 result에 담기
		Board board =result.orElseThrow();//Otional에서 원하는 객체를 바로 얻거나 오류발생시 예외를 던지게하는 메소드
		BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);//boardDTO를 board에다가 매칭시켜줌
		 
		return boardDTO;
	}
	
	//수정하기
	@Override
	public void modify(BoardDTO boardDTO) {
		Optional<Board> result = boardRepository.findById(boardDTO.getBno()); //boardDTO정보와 bno갖고오기
		Board board = result.orElseThrow();//Otional에서 원하는 객체를 바로 얻거나 오류발생시 예외를 던지게하는 메소드
		board.change(boardDTO.getTitle(),boardDTO.getContent()); //change를 이용해 수정한 부분 get하기
		boardRepository.save(board); //수정된 정보 board에 저장하기
		
	}
	
	//삭제하기
	@Override
	public void remove(long bno) {
		boardRepository.deleteById(bno); //그냥 bno갖고와서 삭제
	}
	
	//게시글 검색 & list처리
	@Override
	public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {
		String[] types = pageRequestDTO.getTypes();
		String keyword = pageRequestDTO.getKeyword();
		Pageable pageable = pageRequestDTO.getPageable("bno");
		
		Page<Board> result = boardRepository.searchAll(types, keyword, pageable);
		
		List<BoardDTO> dtoList = result.getContent().stream()
				.map(board -> modelMapper.map(board, BoardDTO.class)).collect(Collectors.toList());
		
		return PageResponseDTO.<BoardDTO>withAll()
				.pageRequestDTO(pageRequestDTO)
				.dtoList(dtoList)
				.total((int)result.getTotalElements())
				.build();
	}

	//게시글 옆에 댓글 카운트 세주기
	@Override
	public PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO) {
		String[] types = pageRequestDTO.getTypes();
		String keyword = pageRequestDTO.getKeyword();
		Pageable pageable = pageRequestDTO.getPageable("bno");
		
		Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable);
		
		
		return PageResponseDTO.<BoardListReplyCountDTO>withAll()
				.pageRequestDTO(pageRequestDTO)
				.dtoList(result.getContent())
				.total((int)result.getTotalElements())
				.build();
				
				
	}
				
		
	


}

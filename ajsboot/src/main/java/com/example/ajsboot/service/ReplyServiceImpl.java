package com.example.ajsboot.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.ajsboot.domain.Reply;
import com.example.ajsboot.dto.PageRequestDTO;
import com.example.ajsboot.dto.PageResponseDTO;
import com.example.ajsboot.dto.ReplyDTO;
import com.example.ajsboot.repository.ReplyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReplyServiceImpl implements ReplyService{
	
	private final ReplyRepository replyRepository;
	private final ModelMapper modelMapper;
	
	//댓글 등록처리
	@Override
	public Long register(ReplyDTO replyDTO) {
		Reply reply = modelMapper.map(replyDTO, Reply.class); 
		//Reply(entity)를 replyDTO에다가 매칭시켜줌 -> entity에는 bno이 없으므로 
		//modelMapper를 설정한 config->RootConfig가서 
		//STRICT가 아닌 LOOSE로 변경해서 매핑해주어야함
		
		Long rno = replyRepository.save(reply).getRno();
		return rno;
	}
	
	//댓글 조회(상세보기)
	@Override
	public ReplyDTO read(Long rno) {
		Optional<Reply> replyOptional = replyRepository.findById(rno); //rno번호 갖고와서 리스트로저장
		
		Reply reply = replyOptional.orElseThrow(); //Otional에서 원하는 객체를 바로 얻거나 오류발생시 예외를 던지게하는 메소드
		
		return modelMapper.map(reply, ReplyDTO.class); //ReplyDTO를 reply(entity)에 매칭시켜줌	
	}
	
	//댓글 수정
	@Override
	public void modify(ReplyDTO replyDTO) {

		Optional<Reply> replOptional = replyRepository.findById(replyDTO.getRno());
		Reply reply = replOptional.orElseThrow(); //Otional에서 원하는 객체를 바로 얻거나 오류발생시 예외를 던지게하는 메소드
		
		reply.changeText(replyDTO.getReplyText()); // 댓글의 내용만 수정하기
		replyRepository.save(reply); //수정된 replyText 저장해주기
		
	}

	//댓글 삭제
	@Override
	public void remove(Long rno) {
		replyRepository.deleteById(rno);
	}
	
	//특정게시물 댓글 목록처리
	@Override
	public PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO) {
		//PageRequestDTO를 이용해서 페이지관련 정보를 처리하고
		//ReplyRespository 를 통해서 특정 게시물에 속하는 Page<Reply> 구현
		//실제 반환되어야하는 타입은 Reply가 아니라 ReplyDTO 타입이므로 ReplyServiceImpl에서는 이를 변환하는
		// 작업이 필요하다
		Pageable pageable = PageRequest.of(pageRequestDTO.getPage() <= 0 ? 0 : pageRequestDTO.getPage() -1,
										pageRequestDTO.getSize(),
										Sort.by("rno").ascending()); //정렬방식
		Page<Reply> result = replyRepository.listOfBoard(bno, pageable);
		List<ReplyDTO> dtoList = result.getContent().stream()
				.map(reply -> modelMapper.map(reply, ReplyDTO.class)) //위에말한 reply를 replyDTO로 변환하는부분
																	 //쉽게 생각해서 오른쪽변수를 왼쪽변수에 매칭시킴
				.collect(Collectors.toList());

		return PageResponseDTO.<ReplyDTO>withAll()
				.pageRequestDTO(pageRequestDTO)
				.dtoList(dtoList)
				.total((int)result.getTotalElements())
				.build();
	}
		

}

package com.example.ajsboot.controller;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ajsboot.dto.BoardDTO;
import com.example.ajsboot.dto.BoardListReplyCountDTO;
import com.example.ajsboot.dto.PageRequestDTO;
import com.example.ajsboot.dto.PageResponseDTO;
import com.example.ajsboot.service.BoardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

	private final BoardService boardService;
	
	//검색 및 list 메소드
	@GetMapping("/list")
	public void list (PageRequestDTO pageRequestDTO, Model model) {
	
		// 댓글 수 나타나기 전에 사용하던 리스트 메소드 
		//PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);
		
		//댓글 수 나타내기 작업후 사용할 메소드 작성후 list.html 이동
		PageResponseDTO<BoardListReplyCountDTO> responseDTO =
				boardService.listWithReplyCount(pageRequestDTO); 
		
		
		log.info(responseDTO);
		model.addAttribute("list",responseDTO);
		//pageRequestDTO와 pageResponseDTO객체가 view로 전달됨
	}
	
	//게시글 등록하기 get-post
	@GetMapping("/register")
	public void registerGET() {
		
	}
	
	@PostMapping("/register")
	public String registerPost(@Valid BoardDTO boardDTO, BindingResult bindingResult, 
			RedirectAttributes redirectAttributes) {
		log.info("board POST register==============");
		
		if(bindingResult.hasErrors()) { //오류 발생할경우
			log.info("has errors=================");
			redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
			//error가 발생할경우 error라는 이름으로 RedirectAtrributes에 추가해서 전송한다 라는 코드
			
			return "redirect:/board/register";
		}
		
		//정상적으로 생성될경우
		log.info(boardDTO);
		long bno = boardService.register(boardDTO);
		redirectAttributes.addFlashAttribute("result",bno);
		return "redirect:/board/list";
	}
	
	//게시글 상세보기(조회) + 수정
	@GetMapping({"/read","/modify"})
	public void read(long bno, PageRequestDTO pageRequestDTO, Model model) {
		
		//게시글 상세보기
		BoardDTO boardDTO = boardService.readOne(bno);
		log.info(boardDTO);
		model.addAttribute("dto",boardDTO);
	}
	
	//게시글 수정 post
	@PostMapping("/modify")
	public String modify(PageRequestDTO pageRequestDTO,
						@Valid BoardDTO boardDTO,
						BindingResult bindingResult,
						RedirectAttributes redirectAttributes) {
		log.info("board modify post================>" + boardDTO);
		
		//오류발생시
		if(bindingResult.hasErrors()) {
			log.info("has errors===============");
			String link = pageRequestDTO.getLink(); //url고정
			//에러발생시 모든 에러는 errors라는 이름으로 수정페이지로 이동시킴
			redirectAttributes.addFlashAttribute("errors",bindingResult.getAllErrors());
			redirectAttributes.addAttribute("bno", boardDTO.getBno());
			
			//link를 통해 기존의 모든 조건(url)을 붙여서 redirect
			return "redirect:/board/modify?"+link;
			
		} //errors if end
			
			
		//수정메소드
		boardService.modify(boardDTO);
		redirectAttributes.addFlashAttribute("result", "modified");
		redirectAttributes.addAttribute("bno",boardDTO.getBno());
		
		return "redirect:/board/read";
	}
	
	
	//삭제 처리
	@PostMapping("/remove")
	public String remove(long bno, RedirectAttributes redirectAttributes) {
		log.info("remove post================== " + bno);
		boardService.remove(bno);
		redirectAttributes.addFlashAttribute("result","removed");
		return "redirect:/board/list";
	}
	
	
}

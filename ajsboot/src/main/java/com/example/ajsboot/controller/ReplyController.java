package com.example.ajsboot.controller;


import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ajsboot.dto.PageRequestDTO;
import com.example.ajsboot.dto.PageResponseDTO;
import com.example.ajsboot.dto.ReplyDTO;
import com.example.ajsboot.service.ReplyService;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

@RestController
@RequestMapping("/replies")
@Log4j2
@RequiredArgsConstructor//의존성 주입(서비스받아야함)
public class ReplyController {
	
	private final ReplyService replyService;
	
	
	//swagger ui 에서 해당기능의 설명으로 출력되는 어노테이션
	@ApiOperation(value= "Replies POST", notes ="POST 방식으로 댓글 등록") 
	
							//consumes는 해당메소드를받아서 소비(consume)하는 데이터가 어떤 종류인지 명시할수 있음 
	//댓글 작성 컨트롤러		//이 메소드의 경우 JSON타입의 데이터를 처리하는 메소드임을 명시하는중임
	@PostMapping(value ="/", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Long> register(@Valid @RequestBody ReplyDTO replyDTO,//requetbody = springframework꺼로 import
																			//JSON문자열을 ReplyDTO로 변환하기위해 사용
									BindingResult bindingResult) throws BindException{ 
															
		log.info(replyDTO);
		
		if(bindingResult.hasErrors()) { // 오류발생시
			throw new BindException(bindingResult);
		}
		Map<String, Long> resultMap = new HashMap<>();
		
		Long rno = replyService.register(replyDTO);
		
		resultMap.put("rno", rno);
		return resultMap;
		/* 메소드 설명 = 1. ReplyDTO를 수집할때 @vaild 적용 
		 * 			2. Bindingresult를 파라미터로 추가하고 문제있을시 exception해서 throw함
		 * 			3. 메소드 선언부에 exception처리
		 * 			4. 메소드 리턴값에 문제가 있다면 @RestControllerAdvice가 처리할것이므로 정상적인 결과만 리턴하게됨
		 */

	}
	
	//특정 게시물의 댓글 목록
	// 특정 게시물의 댓글 목록 처리는 '/replies/list/{bno}을 이용하도록 구성 bno은 게시물번호
	//스프링은 @PathVariable이라는 어노테이션으로 호출하는 경로의 값을 직접 파라미터 변수로 처리할 수 있는 방법을 제공
	
	@ApiOperation(value = "Replies of Board", notes = "GET 방식으로 특정 게시물의 댓글 목록")
	@GetMapping("/list/{bno}")
	public PageResponseDTO<ReplyDTO> getList(@PathVariable("bno") Long bno,
											PageRequestDTO pageRequestDTO){
		PageResponseDTO<ReplyDTO> responseDTO = replyService.getListOfBoard(bno, pageRequestDTO);
		return responseDTO;
		//getList()에서 bno값은 경로에 있는 값을 취해서 사용할예정이기때문에 @PathVariable을 사용
		//페이지와 관련된 정보는 일반쿼리스트링을 이용(결과가 달라질 수 있는 부분은 일반쿼리스트링을쓰고
		//고정값은 URL로 고정하는 방식
	}
	
	//특정 댓글 조회
	//특정댓글조회는 Reply의 rno를 경로로 이용해 get으로 처리
	@ApiOperation(value = "Read Reply", notes = "GET 방식으로 특정 댓글 조회")
	@GetMapping("/{rno}")
	public ReplyDTO getReplyDTO( @PathVariable("rno") Long rno) {
		ReplyDTO replyDTO = replyService.read(rno);
		return replyDTO;
	}
	
	
	//특정 댓글 삭제
	//일반적으로 REST방식에서 삭제작업은 GET/POST가 아닌 DELETE방식을 사용
	@ApiOperation(value = "Delete Reply", notes="DELETE방식으로 특정 댓글 삭제")
	@DeleteMapping("/{rno}")
	public Map<String, Long> remove ( @PathVariable("rno") Long rno) {
		replyService.remove(rno);
		
		Map<String, Long> resultMap = new HashMap<>();
		
		resultMap.put("rno", rno);
		
		return resultMap;
	}
	
	//특정 댓글 수정
	//댓글 수정은 put방식으로 처리하도록해야함 마찬가지로 JSON문자열이 전송되므로 
	//이를 처리하도록 RequestBody를 적용해야함
	@ApiOperation(value = "Modify Reply", notes ="PUT 방식으로 특정 댓글 수정")
	@PutMapping(value = "/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE )
	public Map<String, Long> remove( @PathVariable("rno") Long rno, @RequestBody ReplyDTO replyDTO){
		
		replyDTO.setRno(rno); //번호를 일치시킨뒤
		replyService.modify(replyDTO); //수정
		Map<String, Long> resultMap = new HashMap<>();
		resultMap.put("rno", rno);
		return resultMap;
	}
		
		
	

}

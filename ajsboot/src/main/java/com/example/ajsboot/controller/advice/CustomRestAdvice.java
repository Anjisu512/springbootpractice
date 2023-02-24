package com.example.ajsboot.controller.advice;



import lombok.extern.log4j.Log4j2;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import groovyjarjarantlr4.v4.parse.ANTLRParser.exceptionGroup_return;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Log4j2
public class CustomRestAdvice {
	//REST방식의 컨트롤러는 대부분 Ajax와 같이 눈에 보이지않는 방식(비동기)으로 서버를 호출하기때문에 에러가 발생하면
	// 어디에 어떤 에러가 발생했는지 알기 힘듭니다. 이런이유로 @vaild 과정에서 문제가 발생하면 처리할 수 있도록
	//아래와 같이 설계를 해주는 것 입니다.
	
						//validation 으로 import
	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
	public ResponseEntity<Map<String, String>> handleBindException(BindException e){
		log.error(e);
		Map<String, String>errorMap = new HashMap<>();
		if(e.hasErrors()) {
			BindingResult bindingResult = e.getBindingResult();
			
			bindingResult.getFieldErrors().forEach(fieldError -> {
				errorMap.put(fieldError.getField(),fieldError.getCode());
			});
		}
		return ResponseEntity.badRequest().body(errorMap);
	}	
//RestControllerAdvice를 이용하면 컨트롤러에서 발생하는 예외에 대해 JSON과 같은 순수한 응답 메세지를 생성해서 보낼수있으며
//handleBindException은 컨트롤러에서 BindException이 던져지는 경우 이를 이용해서 JSON 메세지와
//404에러(Bad Request)를 전송하게 합니다.
	
	
	
	//=======댓글기능 설정후 추가된 rest오류 advice  < 존재하지않는 bno에 댓글을 작성할경우 >
	//만약 없는 bno를 swagger에 입력하고 execute를 시키면
	//(서버문제가 아니라 데이터전송문제이지만)상태코드는 500이 나타난다 
	//외부에서 ajax로 댓글 등록 기능을 호출했을때 500에러가 발생하면
	//호출한 측에서는 현재 서버의 문제라고 생각할것이고 전송하는 데이터 문제라고 생각하지 않을것이다.
	// 그렇기때문에 서버의 문제가아니라 데이터의 문제가 있다고 전송하기위해 아래의 advice를 만들어주는것이다.
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
	public ResponseEntity<Map<String, String>> handleFKException(Exception e){
		
		log.error(e);
		
		Map<String, String> errorMap = new HashMap<>();
		
		errorMap.put("time", ""+System.currentTimeMillis());
		errorMap.put("msg", "DataIntegrityViolationException 발생");
		
		return ResponseEntity.badRequest().body(errorMap);
	//추가한handleFKException() 메소드는 DataIntegrityViolationException이 발생하면
	//"constraint fails" 라는 메세지를 클라이언트로 전송하게 작성함
	}

	
	//=======존재하지않는 rno를 조회려고 할때 advice처리
	@ExceptionHandler({NoSuchElementException.class,
						EmptyResultDataAccessException.class }) // rno삭제 구현 후 추가된 행 
	@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
	public ResponseEntity<Map<String, String>> handleNoSuchElement(Exception e){
		log.error(e);
		
		Map<String, String> errorMap = new HashMap<>();
		
		errorMap.put("time", ""+System.currentTimeMillis());
		errorMap.put("msg", "No Such Element Exception 발생, 존재하지않는 rno 조회/삭제 시도임");
		return ResponseEntity.badRequest().body(errorMap);
	}
	
}


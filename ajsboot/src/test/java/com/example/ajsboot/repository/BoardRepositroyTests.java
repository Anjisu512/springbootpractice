package com.example.ajsboot.repository;


import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

import com.example.ajsboot.domain.Board;
import com.example.ajsboot.dto.BoardListReplyCountDTO;
import com.example.ajsboot.repository.BoardRepository;


@SpringBootTest
@Log4j2
public class BoardRepositroyTests {
	@Autowired
	private BoardRepository boardRepository;
	
	//insert기능 테스트
	@Test
	public void testinsert() {
		IntStream.rangeClosed(1, 100).forEach(i->{ //for문 생각  i값을 board에다가 1~100까지 넣을것 
			Board board = Board.builder()
					.title("title..."+i) // title에 i값 넣기
					.content("content..."+i) //content에 i값 넣기
					.writer("user..."+(i%10)) // writer에는 i%10한 user번호로 나타내기
					.build(); //생성하기
			Board result = boardRepository.save(board); //생성한 db를 save(board) board에다가 세이브하고 result에 넣기
			log.info("BNO : "+result.getBno()); //result에 담겨있는 bno갖고와서 출력
		});
	}

	
	//select 테스트
	@Test
	public void selectTest() {
		long bno = 100L; //select할 bno번호
		
		Optional<Board> result = boardRepository.findById(bno); //bno찾아오기
		
		Board board = result.orElseThrow(); //result값 던지기 board로
		log.info(board); //board 출력
	}
	
	
	//update 테스트
	@Test
	public void testUpdate() {
		
		long bno=99L;
		
		Optional<Board> result = boardRepository.findById(bno); //bno찾아오기
		
		Board board = result.orElseThrow(); //result값 board로 던지기
		board.change("update..title 99", "update..content 99"); //board에 작성된 update메소드 실행
		boardRepository.save(board); //board에 저장하기
		
	}
	
//	//delete test
//	@Test
//	public void testDelete() {
//		long bno = 1L;
//		boardRepository.deleteById(bno);
//	}
//	
	
	//paging 테스트
	@Test
	public void testPaging() {
		
		//1 page oreder by bno desc
		// import org.springframework.data.domain.Pageable;
		Pageable pageable = PageRequest.of(1, 10, Sort.by("bno").descending());
						      //페이지 번호는 0 (0부터시작) //사이즈는 10 //정렬조건은 sort ~~~		
		Page<Board> result = boardRepository.findAll(pageable);
		
		log.info("total count : "+result.getTotalElements());
		log.info("total pages: "+result.getTotalPages());
		log.info("page number : " + result.getNumber());
		log.info("page size : "+result.getSize());
		
		List<Board> todoList = result.getContent();
		todoList.forEach(board -> log.info(board));
	}
	
	
	//paging 테스트2
	@Test
	public void testsearch1() {
		//2 page oreder by bno desc
		Pageable pageable = PageRequest.of(1, 10, Sort.by("bno").descending());
		boardRepository.search1(pageable);
		
	}
	
	//검색기능 테스트
	@Test
	public void testSearchAll() {
		String[] types = {"t","c","w"};
		String keyword = "2";
		Pageable pageable = PageRequest.of(0,10,Sort.by("bno").descending());
		Page<Board> result = boardRepository.searchAll(types, keyword, pageable);
		
		//총 페이지
		log.info(result.getTotalPages());
		//페이지 사이즈
		log.info(result.getSize());
		//페이지 넘버
		log.info(result.getNumber());
		//prev next
		log.info(result.hasPrevious()+" : "+result.hasNext());
		
		result.getContent().forEach(board -> log.info(board));
	}
	
	//댓글 리스트처리 죄종테스트
		@Test
		public void testSearchReplyCount() {
			String[] types = {"t","c","w"};
			String keyword ="100";
			Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
			Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable);
			
			//총 페이지
			log.info(result.getTotalPages());
			//페이지 사이즈
			log.info(result.getSize());
			//페이지 넘버
			log.info(result.getNumber());
			//prev next
			log.info(result.hasPrevious()+" : "+result.hasNext());
			
			result.getContent().forEach(board -> log.info(board));

		}
		

	
}

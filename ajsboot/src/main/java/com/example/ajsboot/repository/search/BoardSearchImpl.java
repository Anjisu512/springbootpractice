package com.example.ajsboot.repository.search;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.example.ajsboot.domain.Board;
import com.example.ajsboot.domain.QBoard;
import com.example.ajsboot.domain.QReply;
import com.example.ajsboot.dto.BoardListReplyCountDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch{

	public BoardSearchImpl() {
		super(Board.class);
	}

	@Override //간단한 페이징처리 
	public Page<Board> search1(Pageable pageable) {

		QBoard board = QBoard.board; //Q도메인 객체
		JPQLQuery<Board> query = from(board); // select from... board
		
		//querydsl꺼로 임포트
		BooleanBuilder booleanBuilder = new BooleanBuilder(); // (
		booleanBuilder.or(board.title.contains("11")); // title like "11" 부분
		booleanBuilder.or(board.content.contains("11"));//content like "11" 
		query.where(booleanBuilder); // ) or이므로 ()로 묶어서 title과 content를 검색
		query.where(board.bno.gt(0L));
		
		//booleanbuilder 후 title like"1" where 검색
		
		query.where(board.title.contains("1")); //where title like ...
		
		//paging
		this.getQuerydsl().applyPagination(pageable, query);
		
		List<Board> list = query.fetch();
		long count = query.fetchCount();		
		return null;
	}

	//검색기능 넣기
	@Override
	public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {
		
		QBoard board = QBoard.board; //qboard 도메인 생성하기
		JPQLQuery<Board> query = from(board); // select from board 
		
		if( (types != null && types.length > 0) && keyword != null ) { //검색조건이나 키워드가 있다면 
			//검색 조건이 not null + tyeps.length가 0보다크다 = 검색조건이 있다
			//키워드가 not null = 키워드가 있다
			
			
			BooleanBuilder booleanBuilder = new BooleanBuilder(); // ( 쿼리문시작
			
			for(String type : types) {
				switch (type) {
				case "t": // 제목 
					booleanBuilder.or(board.title.contains(keyword));
					break;
				case "c": //내용
					booleanBuilder.or(board.content.contains(keyword));
					break;
				case "w": //작성자
					booleanBuilder.or(board.writer.contains(keyword));
					break;
				} //스위치end
			} //for end 
			query.where(booleanBuilder); //booleanbuilder안에서 찾겠다 선언 
		} // if end
		
		
		// booleanbuilder안에서 찾은것중에 bno가 0보다 큰거로 검색하겠다 
		query.where(board.bno.gt(0L)); // bno > 0 이라고 생각
		
		//paging 처리
		this.getQuerydsl().applyPagination(pageable, query); 
		List<Board> list = query.fetch(); 
		
		long count = query.fetchCount(); 
		return new PageImpl<>(list, pageable, count); 
		//spring data jpa 에서는 페이징처리의 최종 결과(타입반환)을 직접 처리해야하는 불편함(querydsl은 직접처리해야함)을 
		//줄여주기위해 pageimpl이라는 클래스를 제공해서 3개의 파라미터로 page<T>를 생성가능함
		
		//list = 실제 목록 데이터 
		//pageable = 페이지관련정보를 가진 객체 
		//count = 전체갯수
					
	
	}

	//댓글 목록처리
	//단방향 참조가 가지는 단점 = 필요한 정보가 하나의 엔티티를 통해서 접근할 수 없다는 점
	// 이 문제를 해결하기위해 가장 쉽게 사용할수있는 방법인 JPQL을 이용해서 left (out ~ er) join 이나 inner join같은
	// 조인을 이용하는것, 게시물과 댓글의 경우 한쪽에만 데이터가 존재하는 상황이 발생할수있음( 게시물은 있지만 댓글이없는경우)
	// 이럴땐 outer join을 통해 처리해야함
	@Override
	public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {
		QBoard board = QBoard.board;
		QReply reply = QReply.reply;
		
		JPQLQuery<Board> query = from(board);
		
		//JPQL의 leftJoin을 이용할때에는 on을 이용해서 조인조건을 지정하며 
		query.leftJoin(reply).on(reply.board.eq(board));
		
		//조인 후에는 게시물당 처리가 필요하므로 groupBy를 적용한다
		query.groupBy(board);
		
		//기본 JOIN(inner Join)은 교집합 
		//left join = 왼쪽테이블중심으로 오른쪽 테이블을 매치
		// ex) 왼쪽테이블이 4번 오른쪽테이블이 5번까지있으면 왼쪽1234에 해당하는번호에 맞는 오른쪽테이블1234 데이터 매치
		
		//right join = 오른쪽테이블중심으로 왼쪽 테이블을 매치
		// ex) 오른쪽테이블이 5번 왼쪽테이블이 4번까지있으면 오른쪽12345에 해당하는번호에 맞는 왼쪽테이블1234 데이터 매치
		// 왼쪽은 4번까지밖에없으니 5번테이블에 매치되는 오른쪽테이블내용은 null값이되는것
		
		// 구체적 설명은 블로그 참조(https://superman28.tistory.com/23)
	
		//======================(1번순서) JQPL join절 end 
		//======검색조건 설정(3번순서) 2번부터 하고오시오 
		if( ( types != null && types.length > 0) && keyword != null) {
			BooleanBuilder booleanBuilder = new BooleanBuilder(); //( 키워드/타입이 null이 아닐경우 조건실행
			
			for(String type : types) {
				switch (type) {
				case "t": // 제목 
					booleanBuilder.or(board.title.contains(keyword));
					break;
				case "c": //내용
					booleanBuilder.or(board.content.contains(keyword));
					break;
				case "w": //작성자
					booleanBuilder.or(board.writer.contains(keyword));
					break;
				}
			}//end for
			query.where(booleanBuilder);
		} // end if
		query.where(board.bno.gt(0L)); //bno > 0 이라는 JPQL의 문법
		//====(3번순서) END		
		//=========(2번순서) Projection.bean() 절 start
		//JPA에서는 프로젝션이라고해서 JPQL 결과를 바로 DTO처리하는 기능을 제공함(Qureydsl도 마찬가지로 제공함)
		// 목록화면에서 필요한 쿼리의 결과를 프로젝션.빈() 이라는 것을 이용해서 한번에DTO처리할 수 있는데 이를 이용하려면
		//JPQLQuery 객체의 select() 를 이용한다.
		
		JPQLQuery<BoardListReplyCountDTO> dtoQuery = 
				query.select(Projections.bean(BoardListReplyCountDTO.class, 
						board.bno,
						board.title,
						board.writer,
						board.regDate,
						reply.count().as("replyCount")
						));
		
		//==================projection.bean절 end

		this.getQuerydsl().applyPagination(pageable, dtoQuery);
		
		List<BoardListReplyCountDTO> dtoList = dtoQuery.fetch();
		
		long count = dtoQuery.fetchCount();
		
		return new PageImpl<>(dtoList, pageable, count);
		
	}
		
		
	

	
	
}

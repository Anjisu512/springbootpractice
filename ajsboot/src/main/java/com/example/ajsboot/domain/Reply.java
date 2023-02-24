package com.example.ajsboot.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
//댓글이 사용되는 방식 -> 주로 게시물번호로 사용되는경우 多 쿼리조건으로 자주사용되는 컬럼에는 인덱스를 생성해두는것이
// 좋기때문에 @Table을 이용해 인덱스를 지정할 수 있다
@Table(name = "Reply", indexes = {
		@Index(name = "idx_reply_board_bno",columnList = "board_bno")
})


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude="board")
public class Reply extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//mysql & mariaDB는 auto_increment key 사용을 데이터베이스에 위임하려면 IDENTITY
	// SEQUENCE = ORACLE (@SequenceGenerator필요)
	// TABLE 키생성용 테이블 사용, 모든DB에서사용 @TableGenerator 필요
	// AUTO 방언에 따라 자동지정, 기본값
	private long rno;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Board board;
	// Reply클래스에는 Board타입의 객체참조를 board라는 변수를 이용해서 참조하는데
	// 이때 @ManyToOne을 이용해서 '다대일' 관계로 구성됨을 뜻함
	//lazy는 지연로딩이라고 표현되며 기본적으로 필요한 순간까지 데이터베이스와 연결하지 않는 방식으로 동작됨
	// 반대는 EAGER이며 즉시로딩이라는 단어로 표현, 해당 엔티티를 로딩할때 같이 로딩하는 방식이지만
	// 성능에 영향을 줄 수 있으므로 우선은 LAZY를 사용하고 필요에따라 EAGER를 고려하면된다
	
	private String replyText;
	private String replyer;
	
	
	//======댓글 등록기능까지 하고  조회/수정/삭제/목록을 위해 만들어짐 그전엔 안쓰임
	public void changeText(String text) {
		this.replyText = text;
	}
	
	
	
}	

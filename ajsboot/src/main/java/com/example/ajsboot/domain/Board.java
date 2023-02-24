package com.example.ajsboot.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Board extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//mysql & mariaDB는 auto_increment key 사용을 데이터베이스에 위임하려면 IDENTITY
	// SEQUENCE = ORACLE (@SequenceGenerator필요)
	// TABLE 키생성용 테이블 사용, 모든DB에서사용 @TableGenerator 필요
	// AUTO 방언에 따라 자동지정, 기본값
	private Long bno;

	@Column(length = 500, nullable = false) //컬럼의 길이와null허용여부
	private String title;
	
	@Column(length = 2000, nullable = false)
	private String content;
	
	@Column(length = 50, nullable = false)
	private String writer;
	
	//update를 위한 메소드
	public void change(String title, String content) {
		this.title = title;
		this.content = content;
	}
	
	
	
	
	
}

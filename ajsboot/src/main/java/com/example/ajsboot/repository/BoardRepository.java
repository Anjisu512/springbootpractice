package com.example.ajsboot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.ajsboot.domain.Board;
import com.example.ajsboot.repository.search.BoardSearch;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch{ 
	// 인터페이스 상속시 엔티티타입과@id 타입지정해주어야 하는 점을 제외하면 아무 코드없이 개발가능
	
	@Query(value= "select now()", nativeQuery = true)
	String getTime();

}

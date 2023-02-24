package com.example.ajsboot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ajsboot.domain.Reply;



public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Query("select r from Reply r where r.board.bno = :bno")
    Page<Reply> listOfBoard(@Param("bno") Long bno, @Param("pageable") Pageable pageable);
    //책에는 param 안써있음 param으로 안받아주면 junit에서 계속 parameter받아야한다고 오류나옴
}
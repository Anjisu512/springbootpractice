package com.example.ajsboot.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PageResponseDTO<E> {
   private int page;
   private int size;
   private int total;
   
   // 시작, 끝 페이지 번호
   private int start;
   private int end;
   // 이전, 다음 페이지의 존재 여부
   private boolean prev;
   private boolean next;
   
   private List<E> dtoList;
   
   @Builder(builderMethodName = "withAll")
   public PageResponseDTO(PageRequestDTO pageRequestDTO, List<E> dtoList, int total) {
      if(total <= 0) {
         return;
      }
      
      this.page = pageRequestDTO.getPage();
      this.size = pageRequestDTO.getSize();
      this.total = total;
      this.dtoList = dtoList;
      this.end = (int)(Math.ceil(this.page/10.0))*10; //현재화면에서 end번호구하기
      this.start = this.end - 9; //현재화면에서의 start번호 구하기
      int last = (int)(Math.ceil((total/(double)size))); //데이터 갯수를 계산한 마지막 페이지번호
      this.end = end > last ? last : end;
      this.prev = this.start > 1;
      this.next = total > this.end * this.size;
   }
   
}
				
		

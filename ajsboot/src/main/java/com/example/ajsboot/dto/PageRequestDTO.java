package com.example.ajsboot.dto;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {
	
	@Builder.Default
	private int page= 1; //page의 default 기본값은 1이다
	
	@Builder.Default
	private int size= 10; //page size는 10이 기본값이다
	
	private String type; // 검색 종류는 t / c / w / tc / tw / twc
	private String keyword;
	
	//현재 검색 조건들을 BoardRepository에서 String[] 처리하기 때문에 type 이라는 문자열을 배열로 반환해주는 기능이필요
	// 페이징 처리를 위해서 사용하는 Pageable타입을 반환하는 기능도 있으면 편리하므로 메소드를 미리 구현
	
	public String[] getTypes() {
		if(type == null || type.isEmpty()) { //type이 null 또는 비어있다면 return null 하기
			return null;
		}
		return type.split(""); // ""기준으로 type나누기
	}
	
	//pagealbe임포트할땐 반드시 프레임워크꺼해야함 awt하면 안됨
	public Pageable getPageable(String...props) { 
		//props는 부모컴포넌트에서 자식컴포넌트로 데이터를 전달해주는 객체이다(properties의 줄임말)
		return PageRequest.of(this.page -1,this.size, Sort.by(props).descending());
	}
	
	//검색조건과 페이징조건 등을 문자열로 구성하는 getLink를 추가
	private String link;
	
	//스프링에서 url고정했던것 생각하면 이해 쉬움
	public String getLink() {
		if(link==null) {
			StringBuilder builder = new StringBuilder();
			builder.append("page="+this.page);
			builder.append("&size="+this.size);
			
			if(type !=null && type.length()>0) {
				builder.append("&type="+type);
			} //type if end
			
			if(keyword != null) {
				try {
					builder.append("&keyword=" + URLEncoder.encode(keyword,"UTF-8"));
				}catch (UnsupportedEncodingException e) {
				}
			} //keyword if end
			link = builder.toString();
			
		}//부모if end
		return link;
	}
	
	
	
}

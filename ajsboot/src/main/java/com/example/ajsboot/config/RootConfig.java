package com.example.ajsboot.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RootConfig {

	//ModelMapper란?
	//어떤 Object에 있는 필드값들을 자동으로 원하는 Object로 Mapping시켜주는것
	// ex = board( name / title) , BoardDTO (name/title/content/writer...)
	// board 와 boardDTO가 매핑이된다면 board에 있는 name과 title만 매핑전략에따라 매핑해주는것
	// 필요한것만 끌어쓰기때문에 매우 간편함 매핑작업이안된다면 해당메소드에 따라 getter와 setter를 해주는 갯수가 다르기때문에
	// 가독성이 심히떨어지고 지저분해보일 수 있기 때문
	
	
	@Bean
	public ModelMapper getMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration()
				.setFieldMatchingEnabled(true)
				.setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
				.setMatchingStrategy(MatchingStrategies.LOOSE); 
		// 매핑전략에는 3가지가있다
		// STANDARD, STRICT, LOOSE
		// 지능적으로매핑 / 정확하게 매핑 / 느슨하게 매핑
		// MAPPER 관련 내용 https://devwithpug.github.io/java/java-modelmapper/
		return modelMapper;
		
	}
	
}

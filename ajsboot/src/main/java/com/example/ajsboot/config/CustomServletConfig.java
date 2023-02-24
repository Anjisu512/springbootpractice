package com.example.ajsboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration
@EnableWebMvc
public class CustomServletConfig implements WebMvcConfigurer{

	//swaggerConfig를 적용하기위해 여기까지하면 /swagger-ui/index.html까지들어가지지만 기존에만들어둔
	// 게시판의 css가 제대로 적용이안되는것을 볼 수 있습니다. 이는 정적 파일 경로문제로써 파일의 
	// 경로를 재 설정해주어야합니다.
	
	//=====여기서부터 경로재설정부분 (implements 부터시작임)
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/js/**")
				.addResourceLocations("classpath:/static/js/");
		registry.addResourceHandler("/fonts/**")
				.addResourceLocations("classpath:/static/fonts/");
		registry.addResourceHandler("/css/**")
				.addResourceLocations("classpath:/static/css/");
		registry.addResourceHandler("/assets/**")
				.addResourceLocations("classpath:/static/assets/");
	}
	
	
}

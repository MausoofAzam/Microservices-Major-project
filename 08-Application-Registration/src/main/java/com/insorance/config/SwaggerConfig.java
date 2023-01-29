package com.insorance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@Slf4j
@EnableWebMvc
@EnableSwagger2
public class SwaggerConfig{
//http://localhost:5050/swagger-ui/index.html#/
	//Describe your application info
	ApiInfo getApiInfo() {
		return new ApiInfoBuilder()
				.title("Registration Application for Microservices")
				.description("This Api is Build to Satisfy the Requirement of Insurance Application")
				.version("Version - 1.0")
				.license("Lisence - Snort-Lisence")
				.contact(new Contact("Mausoof Azam", "www.snort.in", "azam@gmail.com")).build();
	}
	//Docker Bean

	@Bean
	public Docket getDocket() {
		log.info("Api Build Successfully..Docket Bean is Created");
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(getApiInfo()).forCodeGeneration(true)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build();
	}

}
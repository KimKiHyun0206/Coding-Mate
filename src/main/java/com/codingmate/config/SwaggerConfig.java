package com.codingmate.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * Swagger를 사용하여 문서화를 하기 위한 Config.
 * <p><a href="http://localhost:8080/swagger-ui/index.html">테스트 페이지</a></p>
 * @author duskafka
 * */
@Configuration
public class SwaggerConfig {

   @Bean
   public OpenAPI openAPI(){
      Info info = new Info()
              .title("Spring Boot 를 이용한 API 확인 Swagger")
              .version("0.1")
              .description("API 문서입니다. \n Demo 버전으로 들고다니는 용도입니다.");
      return new OpenAPI()
              .components(new Components())
              .info(info);
   }
}
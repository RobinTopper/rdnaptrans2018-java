package io.github.rdnaptrans2018.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.zalando.problem.jackson.ProblemModule;

@Configuration
public class WebFluxConfig implements WebFluxConfigurer {

  @Bean
  public ProblemModule problemModule() {
    return new ProblemModule();
  }

}

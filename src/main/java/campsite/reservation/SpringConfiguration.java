package campsite.reservation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@Configuration
@EnableScheduling
public class SpringConfiguration {

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
}

package org.difin.volcanic_getaways.reservation.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
@EnableTransactionManagement
@EnableSwagger2
public class SpringConfiguration {

    @Value("${spring.datasource.hikari.maximum-pool-size}")
    private int connectionPoolSize;

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor validationPostProcessor = new MethodValidationPostProcessor();
        validationPostProcessor.setValidatorFactory(getValidator());
        return validationPostProcessor;
    }

    @Bean
    public Scheduler scheduler(){
        return Schedulers.fromExecutor(Executors.newFixedThreadPool(connectionPoolSize));
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("volcanic_getaways.reservation"))
                .paths(PathSelectors.any())
                .build();
    }
}

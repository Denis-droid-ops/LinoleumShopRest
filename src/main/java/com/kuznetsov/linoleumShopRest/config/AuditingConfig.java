package com.kuznetsov.linoleumShopRest.config;


import com.kuznetsov.linoleumShopRest.LinoleumShopRestApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.envers.repository.config.EnableEnversRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
@EnableEnversRepositories(basePackageClasses = LinoleumShopRestApplication.class)
public class AuditingConfig {

    @Bean
    public AuditorAware<String> auditorAware(){
        return ()-> Optional.of("myemail@mail.ru");
    }
}

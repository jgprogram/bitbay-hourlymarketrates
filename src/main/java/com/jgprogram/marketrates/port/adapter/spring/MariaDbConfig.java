package com.jgprogram.marketrates.port.adapter.spring;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;

@Configuration
public class MariaDbConfig {

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSourceProperties mariadbDSProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean("mariadbDS")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return mariadbDSProperties().initializeDataSourceBuilder().build();
    }

    @Primary
    @Bean(name = "mariadbEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean mariadbEntityManagerFactory(
            EntityManagerFactoryBuilder emFactoryBuilder,
            @Qualifier("mariadbDS") DataSource dataSource
    ) {
        return emFactoryBuilder.dataSource(dataSource)
                .packages("com.jgprogram.marketrates.port.adapter.jpa")
                .persistenceUnit("mariadbPU")
                .build();
    }
}

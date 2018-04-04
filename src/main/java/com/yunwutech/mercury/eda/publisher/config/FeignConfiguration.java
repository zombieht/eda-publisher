package com.yunwutech.mercury.eda.publisher.config;

import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.yunwutech.mercury.eda.publisher")
public class FeignConfiguration {

}

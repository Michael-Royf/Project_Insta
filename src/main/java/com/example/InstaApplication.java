package com.example;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@SpringBootApplication
public class InstaApplication {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
        //post not work
    }
//    @Bean
//    public HttpFirewall allowUrlSemicolonHttpFirewall() {
//        StrictHttpFirewall firewall = new StrictHttpFirewall();
//        firewall.setAllowSemicolon(true);
//        return firewall;
//    }
    public static void main(String[] args) {
        SpringApplication.run(InstaApplication.class, args);
    }


}

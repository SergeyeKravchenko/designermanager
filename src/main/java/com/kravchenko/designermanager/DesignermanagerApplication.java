package com.kravchenko.designermanager;

import com.kravchenko.designermanager.processor.OrderedItemProcessor;
import com.kravchenko.designermanager.security.UrlConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DesignermanagerApplication {

    @Autowired
    private OrderedItemProcessor manager;

    @Autowired
    private UrlConnector connector;

    public static void main(String[] args) {
        SpringApplication.run(
                DesignermanagerApplication.class, args);
    }

    @Bean
    public CommandLineRunner init() {
        return (String... args) -> {
//            manager.processDocument("default");
//            List<String> orderedItemsInfo = connector.findOrderedItemsInfo(Arrays.asList("649", "650"));
//            System.out.println("---------------");
//            orderedItemsInfo.stream().forEach(System.out::println);
        };
    }
}

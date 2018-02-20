package com.kravchenko.designermanager;

import com.kravchenko.designermanager.processor.OrderedItemProcessor;
import com.kravchenko.designermanager.security.UrlConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import java.io.File;

@SpringBootApplication
public class DesignermanagerApplication {

    @Autowired
    private OrderedItemProcessor manager;

    @Autowired
    private UrlConnector connector;

    public static void main(String[] args) {
        String prop = System.getProperty("app.conf");
        SpringApplicationBuilder builder = new SpringApplicationBuilder(DesignermanagerApplication.class);

        if (prop != null && !prop.equals("") && new File(prop).exists()) {
            builder.properties("spring.config.location=" + prop);
        }
        builder.run(args);
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

package com.kravchenko.designermanager.processor.url;

import com.kravchenko.designermanager.model.OrderInfo;
import com.kravchenko.designermanager.processor.OrderedItemHtmlParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UrlHtmlParserTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(UrlHtmlParser.class);

    private String source;
    private OrderInfo test;

    @Autowired
    private OrderedItemHtmlParser parser;

    @Before
    public void before() throws Exception {
        File file = new ClassPathResource("661.html").getFile();
        StringBuilder builder = new StringBuilder();
        Files.lines(Paths.get(file.toString())).forEach(builder::append);
        source = builder.toString();
        test = new OrderInfo();
        test.setOrderNumber("661");
        test.setDescription("Дата:14.02.2018 Способ оплаты:Банківський переказ Замовник: Олена Ходикіна ТОВ \"Аерохендлінг\" e.khodykina@aeh.aero 0957668840");
    }

    @Test
    public void shouldReturnMapWithRequestedData() {
        LOGGER.debug("File source :" + source);
        Map<OrderInfo, List<String>> listMap = parser.parse(source);
        LOGGER.debug("Map in test :" + listMap.toString());
        assertThat(listMap).isNotEmpty().hasSize(1);
        assertThat(listMap).containsKey(test);
        assertThat(listMap.get(test).get(0)).containsSequence("Варіанти знаків Куріння заборонено");
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void shouldReturnExeptionIfNullSource() {
        parser.parse(null);
    }

    @Test()
    public void shouldReturnExeptionIfOrderNumberDoesNotExist() {
        Map<OrderInfo, List<String>> listMap = parser.parse("");
        assertThat(listMap).isEmpty();
    }
}
package com.kravchenko.designermanager.processor.url;

import com.kravchenko.designermanager.model.OrderInfo;
import com.kravchenko.designermanager.model.OrderedItem;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.*;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UrlHtmlGeneratorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UrlHtmlGeneratorTest.class);
    @Autowired
    private UrlHtmlGenerator generator;

    @Autowired
    private Environment environment;

    private static List<OrderedItem> items;
    private static OrderedItem item1;
    private static OrderedItem item2;
    private static OrderedItem item3;
    private static OrderInfo info;
    private static Map<OrderInfo, List<OrderedItem>> listMap;

    @BeforeClass
    public static void init() {
        info = new OrderInfo();
        info.setOrderNumber("661");
        info.setDescription("Test OrderInfo");
        item1 = new OrderedItem();
        item1.setCount(5);
        item1.setName("Знак Прохід заборонено Звалювання лісу");
        item1.setMaterial("Пластик ПВХ товщиною 4 мм");
        item1.setRange("10 х 10 см");
        item1.setAmount("15 грн.");
        item2 = new OrderedItem();
        item2.setCount(1);
        item2.setName("Бензомоторна пила. Безпека робіт на лісосіці");
        item2.setMaterial("Пластик ПВХ товщиною 2 мм");
        item2.setRange("42 х 60 см");
        item2.setAmount("240 грн.");
        item3 = new OrderedItem();
        item3.setCount(1);
        item3.setName("Пиляння деревини");
        item3.setMaterial("Пластик ПВХ товщиною 2 мм");
        item3.setRange("42 х 60 см");
        item3.setAmount("240 грн.");
        items = new ArrayList(Arrays.asList(item1, item2, item3));
        listMap = new HashMap<>();
        listMap.put(info, items);
    }

    @Test
    public void generateFilesWithRequestedNumbers() {
        generator.generate(listMap);
        LOGGER.debug("Generating file with name :" + info.getOrderNumber() + ".docx");
        assertThat(new File(environment.getRequiredProperty("generate.doc.path") + info.getOrderNumber() + ".docx")).exists();
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void shouldReturnExeptionIfEmptySource() {
        generator.generate(Collections.emptyMap());
    }
}
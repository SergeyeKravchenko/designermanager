package com.kravchenko.designermanager.processor.url;

import com.kravchenko.designermanager.model.OrderedItem;
import com.kravchenko.designermanager.processor.OrderedItemConverter;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UrlHtmlConverterIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UrlHtmlConverterIntegrationTest.class);

    @Autowired
    private OrderedItemConverter converter;

    private static List<String> rows;
    private static OrderedItem item1;
    private static OrderedItem item2;
    private static OrderedItem item3;

    @BeforeClass
    public static void init() {
        rows = new ArrayList();
        rows.add("Знак Прохід заборонено Звалювання лісу - Розмір знака: 10 х 10 см в квадраті - Вибір матеріалу: Пластик ПВХ товщиною 4 ммXXX5XXX15 грн.");
        rows.add("Бензомоторна пила. Безпека робіт на лісосіці - Розмір плакату: 42 х 60 см - Вибір матеріалу: Пластик ПВХ товщиною 2 ммXXX1XXX240 грн.");
        rows.add("Пиляння деревини - Розмір плакату: 42 х 60 см - Вибір матеріалу: Пластик ПВХ товщиною 2 ммXXX1XXX240 грн.");
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
    }

    @Test
    public void convertSourceWithRequestData() {
        LOGGER.debug("Rows :" + rows);
        List<OrderedItem> items = converter.convert(rows);
        assertThat(items).isNotEmpty().hasSize(3);
        assertThat(items).containsExactlyInAnyOrder(item1, item2, item3);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void shouldReturnExeptionIfEmptySource() {
        converter.convert(Collections.emptyList());
    }
}
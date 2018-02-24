package com.kravchenko.designermanager.processor;

import com.kravchenko.designermanager.model.OrderInfo;
import com.kravchenko.designermanager.model.OrderedItem;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class ProcessorManagerTest {

    private static String source;
    private static List<OrderedItem> items;
    private static OrderedItem item1;
    private static OrderedItem item2;
    private static OrderedItem item3;
    private static OrderInfo test;
    private static Map<OrderInfo, List<String>> listMap;
    private static Map<OrderInfo, List<OrderedItem>> generatorMap;

    @InjectMocks
    private ProcessorManager manager;

    @Mock
    private OrderedItemDocumentGenerator generator;

    @Mock
    private OrderedItemHtmlParser parser;

    @Mock
    private OrderedItemConverter converter;

    @BeforeClass
    public static void init() throws IOException {
        File file = new ClassPathResource("661.html").getFile();
        StringBuilder builder = new StringBuilder();
        Files.lines(Paths.get(file.toString())).forEach(builder::append);
        source = builder.toString();

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
        test = new OrderInfo();
        test.setOrderNumber("661");
        test.setDescription("Дата:14.02.2018 Способ оплаты:Банківський переказ Замовник: Олена Ходикіна ТОВ \"Аерохендлінг\" e.khodykina@aeh.aero 0957668840");
        listMap=new HashMap<>();
        listMap.put(test, Collections.singletonList(source));
        generatorMap = new HashMap<>();
        generatorMap.put(test,items);
    }

    @Test
    public void processDocumentWithMocks() {
        when(parser.parse(source)).thenReturn(listMap);
        when(converter.convert(Collections.singletonList(source))).thenReturn(items);
        manager.processDocument(Collections.singletonList(source));
        verify(parser).parse(source);
        verify(converter).convert(Arrays.asList(source));
        verify(generator).generate(generatorMap);
    }
    @Test
    public void emptySourceShouldReturnEmptyList() {
        List<OrderInfo> returned = manager.processDocument(Collections.EMPTY_LIST);
        assertThat(returned).isEmpty();
        verify(parser,never()).parse("");
    }
}
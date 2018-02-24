package com.kravchenko.designermanager.processor;

import com.kravchenko.designermanager.model.OrderInfo;
import com.kravchenko.designermanager.model.OrderedItem;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//@RunWith(SpringRunner.class)
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class ProcessorManagerTest {

    private static String source;
    private static List<OrderedItem> items;
    private static OrderedItem item1;
    private static OrderedItem item2;
    private static OrderedItem item3;
    private static OrderInfo info;
    private static OrderInfo test;
    private static Map<OrderInfo, List<String>> listMap;
    private static Map<OrderInfo, List<OrderedItem>> generatorMap;

    @BeforeClass
    public static void init() throws IOException {
        File file = new ClassPathResource("661.html").getFile();
        StringBuilder builder = new StringBuilder();
        Files.lines(Paths.get(file.toString())).forEach(builder::append);
        source = builder.toString();

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
        test = new OrderInfo();
        test.setOrderNumber("661");
        test.setDescription("Дата:14.02.2018 Способ оплаты:Банківський переказ Замовник: Олена Ходикіна ТОВ \"Аерохендлінг\" e.khodykina@aeh.aero 0957668840");
        listMap=new HashMap<>();
        listMap.put(test,Arrays.asList(source));
        generatorMap = new HashMap<>();
        generatorMap.put(test,items);
    }
//    @Rule
//    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    OrderedItemDocumentGenerator generator;

    @Mock
    OrderedItemHtmlParser parser;

    @Mock
    OrderedItemConverter converter;


    @Test
    public void processDocumentWithMocks() {
        ProcessorManager manager = new ProcessorManager(parser, converter, generator);
        when(parser.parse(source)).thenReturn(listMap);
        when(converter.convert(Arrays.asList(source))).thenReturn(items);
        manager.processDocument(Arrays.asList(source));
        verify(parser).parse(source);
        verify(converter).convert(Arrays.asList(source));
        verify(generator).generate(generatorMap);
    }
}
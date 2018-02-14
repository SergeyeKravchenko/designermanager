package com.kravchenko.designermanager.processor.url;

import com.kravchenko.designermanager.model.OrderInfo;
import com.kravchenko.designermanager.processor.OrderedItemHtmlParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class UrlHtmlParser implements OrderedItemHtmlParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(UrlHtmlParser.class);

    @Override
    public Map<OrderInfo, List<String>> parse(String source) {
        Map<OrderInfo, List<String>> listMap = new LinkedHashMap<>();
        OrderInfo info = new OrderInfo();
        List<String> list = new ArrayList<>();
        LOGGER.debug("------Inside UrlHtmlParser-------");
        Document doc = Jsoup.parse(source, "utf-8");
        tableInfo(doc);
        Element table = doc.getElementsByTag("table").get(1);
        Elements rows = table.select("tr");
        Element number = rows.get(1).select("td").get(1);
        info.setOrderNumber(number.text());
        String description = "Дата:" + rows.get(0).select("td").get(1).text() + " Способ оплаты:"
                + rows.get(2).select("td").get(1).text();
        table = doc.getElementsByTag("table").get(2);
        rows = table.select("tr");
        description = description + " Замовник: " + rows.get(1).select("td").get(0).text();
        info.setDescription(description);
        LOGGER.debug("Object OrderInfo :" + info);
        table = doc.getElementsByTag("table").get(3);
        rows = table.select("tr");
        for (int i = 1; i < rows.size() - 3; i++) {
            String result = rows.get(i).select("td").get(0).text() + "XXX"
                    + rows.get(i).select("td").get(2).text() + "XXX" + rows.get(i).select("td").get(3).text();
            list.add(result);
        }
        listMap.put(info, list);
//            for (int i = 1; i < rows.size() - 3; i++) {
//                Element row = rows.get(i);
//                LOGGER.info("Parsing Rows :" + i);
//                Elements cols = row.select("td");
//                for (int j = 0; j < cols.size(); j++) {
//                    Element col = cols.get(j);
//                    LOGGER.info("Parsing Cols :" + j + " " + col);
//                }
//            }
        return listMap;
    }

    private void tableInfo(Document doc) {
        Element table = doc.getElementsByTag("table").get(0);
        LOGGER.debug("------Table 0-------");
        LOGGER.debug(table.text());
        Element table1 = doc.getElementsByTag("table").get(1);
        LOGGER.debug("------Table 1-------");
        LOGGER.debug(table1.text());
        Element table2 = doc.getElementsByTag("table").get(2);
        LOGGER.debug("------Table 2-------");
        LOGGER.debug(table2.text());
        Element table3 = doc.getElementsByTag("table").get(3);
        LOGGER.debug("------Table 3-------");
        LOGGER.debug(table3.text());
        Element table4 = doc.getElementsByTag("table").get(4);
        LOGGER.debug("------Table 4-------");
        LOGGER.debug(table4.text());
    }
}

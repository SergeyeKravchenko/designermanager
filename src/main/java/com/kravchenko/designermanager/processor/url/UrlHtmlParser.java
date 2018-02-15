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
import org.springframework.util.Assert;

import java.util.*;

@Component
public class UrlHtmlParser implements OrderedItemHtmlParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(UrlHtmlParser.class);

    @Override
    public Map<OrderInfo, List<String>> parse(String source) {
        Assert.notNull(source, "Source must not be null");
        Map<OrderInfo, List<String>> listMap = new LinkedHashMap<>();
        OrderInfo info = new OrderInfo();
        List<String> list = new ArrayList<>();
        LOGGER.debug("------Inside UrlHtmlParser-------");
        Document doc = Jsoup.parse(source, "utf-8");
        LOGGER.debug("Doc :" + doc);
        tableInfo(doc);
        Elements elementsByTag = doc.getElementsByTag("table");
        LOGGER.debug("elementsByTag.text() :" + elementsByTag.text());
        if (!elementsByTag.text().equals("")) {
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
            return listMap;
        } else return Collections.emptyMap();
    }

    private void tableInfo(Document doc) {
        Elements tableList = doc.getElementsByTag("table");
        for (int i = 0; i < tableList.size(); i++) {
            Element table = doc.getElementsByTag("table").get(i);
            LOGGER.debug("------Table " + i + "-------");
            LOGGER.debug(table.text());
        }

    }
}

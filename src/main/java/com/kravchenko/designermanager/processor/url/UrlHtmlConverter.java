package com.kravchenko.designermanager.processor.url;

import com.kravchenko.designermanager.model.OrderedItem;
import com.kravchenko.designermanager.processor.OrderedItemConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UrlHtmlConverter implements OrderedItemConverter {

    private List<String> itemProperties;
    List<OrderedItem> items;
    private static final Logger LOGGER = LoggerFactory.getLogger(UrlHtmlConverter.class);
    public static final String SIZE = "[\\s]\\Р[а]?[о]?зм[і]?[е]?[р]";
    public static final String RANGE = "\\d+[\\s]?[с]?[c]?[м]?[\\s]x?х?[\\s]\\d+[\\s][с]?[c]?[м]";
    public static final String CHOICE = "В[ы]?[и]?б[о]?[і]?р матер[и]?[і]?ал[а]?[у]?:";
    private String regexp;

    @Override
    public List<OrderedItem> convert(List<String> rows) {
        LOGGER.debug("=====Inside Converter==== " + rows);
        items = new ArrayList<>();

        for (String row : rows) {
            LOGGER.debug("Row :" + row);
            itemProperties = getItemProperties(row);
            LOGGER.debug("List: " + itemProperties.size() + itemProperties.toString());
            OrderedItem item = new OrderedItem();
            item.setCount(getItemCount());
            LOGGER.debug("Count :" + item.getCount());
            item.setName(getItemName());
            LOGGER.debug("Name :" + item.getName());
            item.setRange(getItemRange());
            LOGGER.debug("Range :" + item.getRange());
            item.setMaterial(getItemMaterial());
            LOGGER.debug("Material :" + item.getMaterial());
            item.setAmount(getItemAmount());
            LOGGER.debug("Amount :" + item.getAmount());
            items.add(item);
        }
        LOGGER.debug("Finish Convertor :" + items);
        return items;
    }

    private int getItemCount() {
        return Integer.valueOf(itemProperties.get(1));
    }


    private String getItemName() {
        String current = itemProperties.get(0);
        LOGGER.debug("Current :" + current);
        regexp = SIZE;
        String index = getStringWithRegexp(current, regexp);
        LOGGER.debug("String index :" + index);
        regexp = CHOICE;
        String indexNext = getStringWithRegexp(current, regexp);
        LOGGER.debug("String indexNext :" + indexNext);
        if (current.indexOf(index) < current.indexOf(indexNext)) {
            LOGGER.debug("Index of index :"+current.indexOf(index));
            LOGGER.debug("Index of indexNext :"+current.indexOf(indexNext));
            return current.substring(0, current.indexOf(index) - 2);
        } else {
            LOGGER.debug("Index of index :" + current.indexOf(index));
            LOGGER.debug("Index of indexNext :" + current.indexOf(indexNext));
            return current.substring(0, current.indexOf(indexNext)-2);
        }
    }

    private String getItemRange() {
        String current = itemProperties.get(0);
        regexp = RANGE;
        return getStringWithRegexp(current, regexp);
    }

    private String getStringWithRegexp(String current, String regexp) {
        Pattern pat = Pattern.compile(regexp);
        Matcher mat = pat.matcher(current);
        if (mat.find()) {
            return current.substring(mat.start(), mat.end());
        }
        return null;
    }

    private String getItemMaterial() {
        String current = itemProperties.get(0);
        regexp = CHOICE;
        String index = getStringWithRegexp(current, regexp);
        return current.substring(current.indexOf(index) + 17);
    }


    public String getItemAmount() {
        return itemProperties.get(2);
    }

    private ArrayList<String> getItemProperties(String value) {
        return new ArrayList<>(Arrays.asList(value.split("XXX")));
    }

}

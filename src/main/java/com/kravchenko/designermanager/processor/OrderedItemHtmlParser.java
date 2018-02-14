package com.kravchenko.designermanager.processor;

import com.kravchenko.designermanager.model.OrderInfo;

import java.util.List;
import java.util.Map;

public interface OrderedItemHtmlParser {
    Map<OrderInfo,List<String>> parse(String source);
}

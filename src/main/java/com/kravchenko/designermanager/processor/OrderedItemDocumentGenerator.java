package com.kravchenko.designermanager.processor;

import com.kravchenko.designermanager.model.OrderInfo;
import com.kravchenko.designermanager.model.OrderedItem;

import java.util.List;
import java.util.Map;

public interface OrderedItemDocumentGenerator {
    void generate(Map<OrderInfo,List<OrderedItem>> infoListMap);
}

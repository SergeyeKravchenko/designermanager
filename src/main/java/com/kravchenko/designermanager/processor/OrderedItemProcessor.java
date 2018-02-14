package com.kravchenko.designermanager.processor;

import com.kravchenko.designermanager.model.OrderInfo;

import java.util.List;

public interface OrderedItemProcessor {
    List<OrderInfo> processDocument(List<String> urls);
}

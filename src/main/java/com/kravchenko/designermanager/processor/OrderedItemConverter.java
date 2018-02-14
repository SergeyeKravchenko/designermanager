package com.kravchenko.designermanager.processor;

import com.kravchenko.designermanager.model.OrderedItem;

import java.util.List;

public interface OrderedItemConverter {

   List<OrderedItem> convert(List<String> rows);

}

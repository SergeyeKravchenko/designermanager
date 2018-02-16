package com.kravchenko.designermanager.processor;

import com.kravchenko.designermanager.model.OrderInfo;
import com.kravchenko.designermanager.model.OrderedItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProcessorManager implements OrderedItemProcessor {

    @Autowired
    OrderedItemHtmlParser parserHtml;

    @Autowired
    OrderedItemConverter converter;

    @Autowired
    OrderedItemDocumentGenerator generator;

    private List<OrderInfo> infos = new ArrayList<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessorManager.class);

    @Override
    public List<OrderInfo> processDocument(List<String> sources) {

        LOGGER.debug("========Inside Manager=======");

        for (String source : sources) {
            LOGGER.debug("The source value :" + source);
            Map<OrderInfo, List<String>> listRowsPerNumber = parserHtml.parse(source);
            if (!listRowsPerNumber.isEmpty()) {
                for (OrderInfo info : listRowsPerNumber.keySet()) {
                    LOGGER.debug("Current order :" + info.toString());
                    infos.add(info);
                    List<OrderedItem> orderedItems = converter.convert(listRowsPerNumber.get(info));
                    LOGGER.debug(orderedItems.size() + orderedItems.toString());
                    Map<OrderInfo, List<OrderedItem>> orderInfoListMap = new HashMap<>();
                    orderInfoListMap.put(info, orderedItems);
                    generator.generate(orderInfoListMap);
                }
            } else{
                LOGGER.info("Order number does not exist");
            }
        }
        return infos;
    }
}
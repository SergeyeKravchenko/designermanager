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

    private OrderedItemHtmlParser parserHtml;

    private OrderedItemConverter converter;

    private OrderedItemDocumentGenerator generator;

    @Autowired
    public ProcessorManager(OrderedItemHtmlParser parserHtml,
                            OrderedItemConverter converter,
                            OrderedItemDocumentGenerator generator) {
        this.parserHtml = parserHtml;
        this.converter = converter;
        this.generator = generator;
    }

    private List<OrderInfo> infos;
    private Map<OrderInfo, List<OrderedItem>> orderInfoListMap;
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessorManager.class);

    @Override
    public List<OrderInfo> processDocument(List<String> sources) {

        LOGGER.debug("========Inside Manager=======");
        infos = new ArrayList<>();
        for (String source : sources) {
            LOGGER.debug("The source value :" + source);
            Map<OrderInfo, List<String>> listRowsPerNumber = parserHtml.parse(source);
            if (!listRowsPerNumber.isEmpty()) {
                for (OrderInfo info : listRowsPerNumber.keySet()) {
                    LOGGER.debug("Current order :" + info.toString());
                    infos.add(info);
                    List<OrderedItem> orderedItems = converter.convert(listRowsPerNumber.get(info));
                    LOGGER.debug(orderedItems.size() + orderedItems.toString());
                    orderInfoListMap = new HashMap();
                    orderInfoListMap.put(info, orderedItems);
                    generator.generate(orderInfoListMap);
                }
            } else {
                LOGGER.info("Order number does not exist");
            }
        }
        return infos;
    }
}
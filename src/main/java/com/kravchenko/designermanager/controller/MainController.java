package com.kravchenko.designermanager.controller;

import com.kravchenko.designermanager.model.OrderInfo;
import com.kravchenko.designermanager.model.OrderNumberHolder;
import com.kravchenko.designermanager.processor.OrderedItemProcessor;
import com.kravchenko.designermanager.security.UrlConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private UrlConnector connector;

    @Autowired
    private OrderedItemProcessor manager;

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @GetMapping("/home")
    public String home(@ModelAttribute OrderNumberHolder holder) {
        LOGGER.info("In /home method Get");
        return "home";
    }

    @PostMapping("/process")
    public String process(@Valid OrderNumberHolder holder, BindingResult result, Model model) throws Exception {
        LOGGER.info("In /process method Post");
        if (result.hasErrors()) {
            return "home";
        } else {
            List<String> orderNumbers = Arrays.asList(holder.getOrderNumber().split(","));
            List<String> itemsInfo = connector.findOrderedItemsInfo(orderNumbers);
            List<OrderInfo> infoList = manager.processDocument(itemsInfo);
                model.addAttribute("infoList", infoList);
            if (infoList.isEmpty()) {
                model.addAttribute("message", "Order number does not exist");
            }else {
                model.addAttribute("message", "Documents generated successfully");
            }
            return "success";
        }
    }
}

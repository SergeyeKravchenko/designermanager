package com.kravchenko.designermanager.model;

import lombok.Data;

@Data
public class OrderedItem {
    private int count;
    private String name;
    private String material;
    private String range;
    private String amount;
}

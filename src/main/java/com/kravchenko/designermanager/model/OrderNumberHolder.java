package com.kravchenko.designermanager.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

@Data
public class OrderNumberHolder {
    @NotEmpty(message = "Petia, please insert order number")
    @Pattern(regexp = "[\\d+][,\\d]*", message = "Only digits permitted")
    private String orderNumber;
}

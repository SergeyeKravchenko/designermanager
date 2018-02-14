package com.kravchenko.designermanager.security;

import java.util.List;

public interface UrlConnector {
    List<String> findOrderedItemsInfo(List<String> orders) throws Exception;
}

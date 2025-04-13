package com.exchange.domain.discount.impl;

import com.exchange.domain.model.entity.Item;

import java.math.BigDecimal;
import java.util.List;

public interface DiscountStrategy {

    BigDecimal applyDiscount(List<Item> items, BigDecimal originalAmount);
    boolean isApplicable(List<Item> items, String userId, int tenure, String userType);
}

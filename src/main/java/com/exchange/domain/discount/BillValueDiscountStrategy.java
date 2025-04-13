package com.exchange.domain.discount;

import com.exchange.domain.model.entity.Item;
import com.exchange.domain.discount.impl.DiscountStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class BillValueDiscountStrategy implements DiscountStrategy {

    private static final BigDecimal HUNDRED = new BigDecimal("100");
    private static final BigDecimal DISCOUNT_PER_HUNDRED = new BigDecimal("5");

    @Override
    public BigDecimal applyDiscount(List<Item> items, BigDecimal originalAmount) {
        BigDecimal totalHundreds = originalAmount.divide(HUNDRED, 0, RoundingMode.FLOOR);
        BigDecimal discount = totalHundreds.multiply(DISCOUNT_PER_HUNDRED);

        return originalAmount.subtract(discount);
    }

    @Override
    public boolean isApplicable(List<Item> items, String userId, int tenure, String userType) {
        return true; // This discount is always applicable
    }
}

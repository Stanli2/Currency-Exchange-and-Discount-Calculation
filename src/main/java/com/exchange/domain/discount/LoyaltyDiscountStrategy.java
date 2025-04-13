package com.exchange.domain.discount;

import com.exchange.domain.model.entity.Item;
import com.exchange.domain.model.enums.ItemCategory;
import com.exchange.domain.discount.impl.DiscountStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class LoyaltyDiscountStrategy implements DiscountStrategy {

    private static final BigDecimal DISCOUNT_RATE = new BigDecimal("0.05");
    private static final int MINIMUM_TENURE_YEARS = 2;

    @Override
    public BigDecimal applyDiscount(List<Item> items, BigDecimal originalAmount) {
        BigDecimal eligibleAmount = getEligibleAmount(items);
        BigDecimal discount = eligibleAmount.multiply(DISCOUNT_RATE)
                .setScale(2, RoundingMode.HALF_UP);

        return originalAmount.subtract(discount);
    }

    @Override
    public boolean isApplicable(List<Item> items, String userId, int tenure, String userType) {
        return tenure > MINIMUM_TENURE_YEARS;
    }

    private BigDecimal getEligibleAmount(List<Item> items) {
        return items.stream()
                .filter(item -> item.getCategory() != ItemCategory.GROCERY)
                .map(Item::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

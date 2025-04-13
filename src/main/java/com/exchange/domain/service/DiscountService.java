package com.exchange.domain.service;

import com.exchange.domain.discount.AffiliateDiscountStrategy;
import com.exchange.domain.discount.BillValueDiscountStrategy;
import com.exchange.domain.discount.EmployeeDiscountStrategy;
import com.exchange.domain.discount.LoyaltyDiscountStrategy;
import com.exchange.domain.discount.impl.DiscountStrategy;
import com.exchange.domain.model.entity.Item;
import com.exchange.domain.model.entity.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class DiscountService {

    private final List<DiscountStrategy> percentageDiscountStrategies;
    private final BillValueDiscountStrategy billValueDiscountStrategy;

    public DiscountService(
            EmployeeDiscountStrategy employeeDiscountStrategy,
            AffiliateDiscountStrategy affiliateDiscountStrategy,
            LoyaltyDiscountStrategy loyaltyDiscountStrategy,
            BillValueDiscountStrategy billValueDiscountStrategy) {

        this.percentageDiscountStrategies = Arrays.asList(
                employeeDiscountStrategy,
                affiliateDiscountStrategy,
                loyaltyDiscountStrategy
        );

        this.billValueDiscountStrategy = billValueDiscountStrategy;
    }

    public BigDecimal applyDiscounts(List<Item> items, User user, BigDecimal originalAmount) {
        // First, apply one of the percentage-based discounts (take the best one)
        BigDecimal amountAfterPercentageDiscount = applyBestPercentageDiscount(items, user, originalAmount);

        // Then, apply the fixed amount discount
        return billValueDiscountStrategy.applyDiscount(items, amountAfterPercentageDiscount);
    }

    private BigDecimal applyBestPercentageDiscount(List<Item> items, User user, BigDecimal originalAmount) {
        // Find the applicable percentage discount strategies
        List<DiscountStrategy> applicableStrategies = percentageDiscountStrategies.stream()
                .filter(strategy -> strategy.isApplicable(
                        items,
                        user.getId(),
                        user.getTenureYears(),
                        user.getType().name()))
                .toList();

        // If no applicable strategy, return the original amount
        if (applicableStrategies.isEmpty()) {
            return originalAmount;
        }

        // Find the strategy that gives the best discount
        Optional<BigDecimal> bestDiscountedAmount = applicableStrategies.stream()
                .map(strategy -> strategy.applyDiscount(items, originalAmount))
                .min(Comparator.naturalOrder());

        return bestDiscountedAmount.orElse(originalAmount);
    }
}

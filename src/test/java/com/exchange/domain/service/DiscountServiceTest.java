package com.exchange.domain.service;

import com.exchange.domain.discount.AffiliateDiscountStrategy;
import com.exchange.domain.discount.BillValueDiscountStrategy;
import com.exchange.domain.discount.EmployeeDiscountStrategy;
import com.exchange.domain.discount.LoyaltyDiscountStrategy;
import com.exchange.domain.model.entity.Item;
import com.exchange.domain.model.entity.User;
import com.exchange.domain.model.enums.ItemCategory;
import com.exchange.domain.model.enums.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiscountServiceTest {

    private DiscountService discountService;

    @BeforeEach
    public void setup() {
        EmployeeDiscountStrategy employeeDiscountStrategy = new EmployeeDiscountStrategy();
        AffiliateDiscountStrategy affiliateDiscountStrategy = new AffiliateDiscountStrategy();
        LoyaltyDiscountStrategy loyaltyDiscountStrategy = new LoyaltyDiscountStrategy();
        BillValueDiscountStrategy billValueDiscountStrategy = new BillValueDiscountStrategy();

        discountService = new DiscountService(
                employeeDiscountStrategy,
                affiliateDiscountStrategy,
                loyaltyDiscountStrategy,
                billValueDiscountStrategy
        );
    }

    @Test
    public void testApplyDiscounts_Employee() {
        // Given
        List<Item> items = Arrays.asList(
                new Item("Electronics", new BigDecimal("100"), ItemCategory.ELECTRONICS),
                new Item("Clothing", new BigDecimal("200"), ItemCategory.CLOTHING)
        );
        User user = new User("1", "John", UserType.EMPLOYEE, 1);
        BigDecimal originalAmount = new BigDecimal("300");

        // When
        BigDecimal result = discountService.applyDiscounts(items, user, originalAmount);

        // Then
        // Employee discount: 30% of $300 = $90, so $210 after percentage discount
        // Bill value discount: $210 / 100 = 2 discounts of $5 each = $10, so $200 final amount
        assertEquals(new BigDecimal("200.00"), result.setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void testApplyDiscounts_Affiliate() {
        // Given
        List<Item> items = Arrays.asList(
                new Item("Electronics", new BigDecimal("100"), ItemCategory.ELECTRONICS),
                new Item("Clothing", new BigDecimal("200"), ItemCategory.CLOTHING)
        );
        User user = new User("1", "John", UserType.AFFILIATE, 1);
        BigDecimal originalAmount = new BigDecimal("300");

        // When
        BigDecimal result = discountService.applyDiscounts(items, user, originalAmount);

        // Then
        // Affiliate discount: 10% of $300 = $30, so $270 after percentage discount
        // Bill value discount: $270 / 100 = 2 discounts of $5 each = $10, so $260 final amount
        assertEquals(new BigDecimal("260.00"), result.setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void testApplyDiscounts_LoyalCustomer() {
        // Given
        List<Item> items = Arrays.asList(
                new Item("Electronics", new BigDecimal("100"), ItemCategory.ELECTRONICS),
                new Item("Clothing", new BigDecimal("200"), ItemCategory.CLOTHING)
        );
        User user = new User("1", "John", UserType.REGULAR, 3);
        BigDecimal originalAmount = new BigDecimal("300");

        // When
        BigDecimal result = discountService.applyDiscounts(items, user, originalAmount);

        // Then
        // Loyalty discount: 5% of $300 = $15, so $285 after percentage discount
        // Bill value discount: $285 / 100 = 2 discounts of $5 each = $10, so $275 final amount
        assertEquals(new BigDecimal("275.00"), result.setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void testApplyDiscounts_RegularCustomer() {
        // Given
        List<Item> items = Arrays.asList(
                new Item("Electronics", new BigDecimal("100"), ItemCategory.ELECTRONICS),
                new Item("Clothing", new BigDecimal("200"), ItemCategory.CLOTHING)
        );
        User user = new User("1", "John", UserType.REGULAR, 1);
        BigDecimal originalAmount = new BigDecimal("300");

        // When
        BigDecimal result = discountService.applyDiscounts(items, user, originalAmount);

        // Then
        // No percentage discount applies
        // Bill value discount: $300 / 100 = 3 discounts of $5 each = $15, so $285 final amount
        assertEquals(new BigDecimal("285.00"), result.setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void testApplyDiscounts_WithGroceries() {
        // Given
        List<Item> items = Arrays.asList(
                new Item("Electronics", new BigDecimal("100"), ItemCategory.ELECTRONICS),
                new Item("Groceries", new BigDecimal("200"), ItemCategory.GROCERY)
        );
        User user = new User("1", "John", UserType.EMPLOYEE, 1);
        BigDecimal originalAmount = new BigDecimal("300");

        // When
        BigDecimal result = discountService.applyDiscounts(items, user, originalAmount);

        // Then
        // Employee discount: 30% of $100 (non-grocery) = $30, so $270 after percentage discount
        // Bill value discount: $270 / 100 = 2 discounts of $5 each = $10, so $260 final amount
        assertEquals(new BigDecimal("260.00"), result.setScale(2, RoundingMode.HALF_UP));
    }
}

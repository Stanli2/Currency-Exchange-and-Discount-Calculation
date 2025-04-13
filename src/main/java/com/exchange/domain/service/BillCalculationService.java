package com.exchange.domain.service;

import com.exchange.domain.model.dto.BillRequest;
import com.exchange.domain.model.dto.BillResponse;
import com.exchange.domain.model.entity.Item;
import com.exchange.domain.model.entity.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BillCalculationService {

    private final DiscountService discountService;
    private final CurrencyConversionService conversionService;

    public BillCalculationService(DiscountService discountService, CurrencyConversionService conversionService) {
        this.discountService = discountService;
        this.conversionService = conversionService;
    }

    public BillResponse calculateBill(BillRequest request) {
        List<Item> items = request.getItems();
        User user = request.getUser();
        String baseCurrency = request.getBaseCurrency();
        String targetCurrency = request.getTargetCurrency();

        // Calculate original total amount
        BigDecimal originalAmount = calculateTotalAmount(items);

        // Apply applicable discounts
        BigDecimal discountedAmount = discountService.applyDiscounts(items, user, originalAmount);

        // Get exchange rate
        BigDecimal exchangeRate = conversionService.getExchangeRate(baseCurrency, targetCurrency);

        // Convert to target currency
        BigDecimal convertedAmount = conversionService.convertCurrency(
                discountedAmount, baseCurrency, targetCurrency);

        return BillResponse.builder()
                .originalAmount(originalAmount)
                .baseCurrency(baseCurrency)
                .discountedAmount(discountedAmount)
                .convertedAmount(convertedAmount)
                .targetCurrency(targetCurrency)
                .exchangeRate(exchangeRate)
                .build();
    }

    private BigDecimal calculateTotalAmount(List<Item> items) {
        return items.stream()
                .map(Item::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

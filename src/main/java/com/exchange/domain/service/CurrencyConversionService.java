package com.exchange.domain.service;

import com.exchange.domain.discount.CurrencyExchangeClient;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CurrencyConversionService {

    private final CurrencyExchangeClient exchangeClient;

    public CurrencyConversionService(CurrencyExchangeClient exchangeClient) {
        this.exchangeClient = exchangeClient;
    }

    public BigDecimal convertCurrency(BigDecimal amount, String fromCurrency, String toCurrency) {
        if (fromCurrency.equals(toCurrency)) {
            return amount;
        }

        BigDecimal exchangeRate = exchangeClient.getExchangeRate(fromCurrency, toCurrency);
        return amount.multiply(exchangeRate).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        return exchangeClient.getExchangeRate(fromCurrency, toCurrency);
    }
}

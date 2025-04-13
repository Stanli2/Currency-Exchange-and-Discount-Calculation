package com.exchange.domain.service;

import com.exchange.domain.integration.CurrencyExchangeClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CurrencyConversionServiceTest {

    @Mock
    private CurrencyExchangeClient exchangeClient;

    private CurrencyConversionService conversionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        conversionService = new CurrencyConversionService(exchangeClient);
    }

    @Test
    public void testConvertCurrency_SameCurrency() {
        // Given
        BigDecimal amount = new BigDecimal("100.00");
        String fromCurrency = "USD";
        String toCurrency = "USD";

        // When
        BigDecimal result = conversionService.convertCurrency(amount, fromCurrency, toCurrency);

        // Then
        assertEquals(amount, result);
    }

    @Test
    public void testConvertCurrency_DifferentCurrency() {
        // Given
        BigDecimal amount = new BigDecimal("100.00");
        String fromCurrency = "USD";
        String toCurrency = "EUR";
        BigDecimal exchangeRate = new BigDecimal("0.85");

        when(exchangeClient.getExchangeRate(fromCurrency, toCurrency)).thenReturn(exchangeRate);

        // When
        BigDecimal result = conversionService.convertCurrency(amount, fromCurrency, toCurrency);

        // Then
        assertEquals(new BigDecimal("85.00"), result);
    }
}

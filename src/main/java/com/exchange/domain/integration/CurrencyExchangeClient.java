package com.exchange.domain.integration;


import com.exchange.domain.model.dto.ExchangeRateResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class CurrencyExchangeClient {

    private final RestTemplate restTemplate;
    private final String apiUrl;
    private final String apiKey;


    public CurrencyExchangeClient(
            @Value("${exchange.api.url}") String apiUrl,
            @Value("${exchange.api.key}") String apiKey) {
        this.restTemplate = new RestTemplate();
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
    }

    @Cacheable("exchangeRates")
    public ExchangeRateResponse getExchangeRates(String baseCurrency) {
        String url = apiUrl.replace("{base_currency}", baseCurrency) + "?apikey=" + apiKey;
        return restTemplate.getForObject(url, ExchangeRateResponse.class);
    }

    public BigDecimal getExchangeRate(String baseCurrency, String targetCurrency) {
        ExchangeRateResponse response = getExchangeRates(baseCurrency);
        return response.getRates().get(targetCurrency);
    }
}

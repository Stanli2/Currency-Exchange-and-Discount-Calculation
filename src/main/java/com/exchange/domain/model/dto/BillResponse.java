package com.exchange.domain.model.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillResponse {

    private BigDecimal originalAmount;
    private String baseCurrency;
    private BigDecimal discountedAmount;
    private BigDecimal convertedAmount;
    private String targetCurrency;
    private BigDecimal exchangeRate;
}

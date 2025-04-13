package com.exchange.domain.model.dto;

import com.exchange.domain.model.entity.Item;
import com.exchange.domain.model.entity.User;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillRequest {

    private List<Item> items;
    private User user;
    private String baseCurrency;
    private String targetCurrency;

}

package com.exchange.domain.model.entity;

import com.exchange.domain.model.enums.ItemCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private String name;
    private BigDecimal price;
    private ItemCategory category;

}


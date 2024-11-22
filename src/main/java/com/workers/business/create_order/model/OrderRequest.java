package com.workers.business.create_order.model;

import com.workers.business.create_order.model.enums.OrderCategory;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderRequest {
    private String customerId;
    private Long chatId;
    private OrderCategory category;
    private String shortDescription;
    private String detailedDescription;
    private BigDecimal amount;
    private String address;
}

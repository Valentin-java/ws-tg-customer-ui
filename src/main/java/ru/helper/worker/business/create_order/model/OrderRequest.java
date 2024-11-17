package ru.helper.worker.business.create_order.model;

import lombok.Data;
import ru.helper.worker.business.create_order.model.enums.OrderCategory;

import java.math.BigDecimal;

@Data
public class OrderRequest {
    private String customerId;
    private OrderCategory category;
    private String shortDescription;
    private String detailedDescription;
    private BigDecimal amount;
    private String address;
}

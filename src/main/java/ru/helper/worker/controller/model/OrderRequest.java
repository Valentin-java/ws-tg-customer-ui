package ru.helper.worker.controller.model;

import lombok.Data;
import ru.helper.worker.controller.model.enums.OrderCategory;

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

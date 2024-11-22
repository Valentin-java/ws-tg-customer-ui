package com.workers.persistence.enums;

public enum OrderStatus {
    DRAFT,      // Не отправленный заказ
    SENT,       // Заказ отправлен
    STOPPED,  // Заказ приостановлен
    CANCELLED   // Заказ отменен
}

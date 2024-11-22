package com.workers.business.received_bid.model.enums;

public enum BidReceivePayloadEnum {
    ACCEPT_BID,             // Заказчик принял отклик мастера
    REJECT_BID,             // Заказчик отказался от работы мастера - начать поиск заново - удалить контекст - перевести статус заказа в NEW
    SEND_MESSAGE_BID,       // Заказчик хочет задать вопрос мастеру без обмена контактами
    INFO_BID,               // Заказчик хочет увидеть карточку мастера
    FEEDBACK_BID,           // Заказчик хочет оставить отзыв мастеру
    REJECT_FEEDBACK_BID,    // Заказчик отказался оставлять отзыв мастеру
    SUCCESS_BID             // Мастер успешно завершил заказ
}

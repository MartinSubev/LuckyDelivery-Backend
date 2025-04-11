package LuckyDelivery.Model.Enums;

public enum OrderStatus {
    PENDING,          // Поръчката е в процес на обработка
    ACCEPTED,         // Поръчката е приета
    IN_TRANSIT,       // Поръчката е на път
    DELIVERED,        // Поръчката е доставена
    CANCELED          // Поръчката е отменена
}

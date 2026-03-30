package com.ym.orderservice.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Запрос на создание заказа")
public class OrderRequest {

    @Schema(description = "Информация о клиенте")
    private CustomerDto customer;

    @Schema(description = "Адрес доставки")
    private AddressDto shippingAddress;

    @Schema(description = "Список товаров в заказе")
    private List<OrderItemRequest> items;

    @Schema(description = "Дополнительные заметки к заказу",
            example = "Позвонить перед доставкой",
            maxLength = 500)
    private String notes;

    @Data
    @Schema(description = "Информация о клиенте")
    public static class CustomerDto {

        @Schema(description = "ID клиента в системе",
                example = "12345",
                required = true)
        private Long id;

        @Schema(description = "Полное имя клиента",
                example = "Иван Иванов",
                required = true,
                minLength = 2,
                maxLength = 100)
        private String fullName;

        @Schema(description = "Email адрес клиента",
                example = "ivan.ivanov@example.com",
                required = true,
                pattern = "^[A-Za-z0-9+_.-]+@(.+)$")
        private String email;

        @Schema(description = "Номер телефона",
                example = "+7 (999) 123-45-67",
                required = true,
                pattern = "^\\+?[0-9\\s\\-\\(\\)]{10,20}$")
        private String phone;

        @Schema(description = "Адрес клиента")
        private AddressDto address;
    }

    @Data
    @Schema(description = "Адресная информация")
    public static class AddressDto {

        @Schema(description = "Улица и номер дома",
                example = "ул. Тверская, д. 15",
                required = true,
                maxLength = 200)
        private String street;

        @Schema(description = "Город",
                example = "Москва",
                required = true,
                maxLength = 100)
        private String city;

        @Schema(description = "Регион/Область",
                example = "Московская область",
                maxLength = 100)
        private String state;

        @Schema(description = "Почтовый индекс",
                example = "123456",
                pattern = "^[0-9]{5,6}$",
                maxLength = 10)
        private String zipCode;

        @Schema(description = "Страна",
                example = "Россия",
                required = true,
                maxLength = 100)
        private String country;
    }
}
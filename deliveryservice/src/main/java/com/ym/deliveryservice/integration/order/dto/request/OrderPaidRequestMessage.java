package com.ym.deliveryservice.integration.order.dto.request;

import lombok.*;

import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPaidRequestMessage {
   private UUID orderId;
}

package src.main.java.com.ym.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "shop-records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopRecord {
    @Id
    private String id;
    private String orderStatus;
    private Long customerId;
    private String trackingNumber;
    private BigDecimal paymentAmount;
}

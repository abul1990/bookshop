package com.tw.bootcamp.bookshop.purchase;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseResponse {
    private Long orderId;
    private TransactionStatus transactionStatus;
}

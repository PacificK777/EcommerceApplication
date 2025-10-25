package com.example.paymentservice.DTOS;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class initiatePaymentRequestDTO {
    private String orderID;
    private Long amount;
    private String name;
    private String email;
    private String contactNo;
}

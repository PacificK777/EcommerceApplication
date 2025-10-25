package com.example.paymentservice.PaymentGateway;

import com.razorpay.RazorpayException;

public interface PaymentGateway {
    String generatePaymentLink(String orderid,
                               Long amount,
                               String name,
                               String email,
                               String contactNo) throws RazorpayException;
}

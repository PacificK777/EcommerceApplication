package com.example.paymentservice.PaymentGateway;

public class StripePaymentGateway implements PaymentGateway{
    @Override
    public String generatePaymentLink(String orderid,
                                      Long amount,
                                      String name,
                                      String email,
                                      String contactNo) {
        return "";
    }
}

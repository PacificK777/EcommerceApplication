package com.example.paymentservice.Service;

import com.example.paymentservice.PaymentGateway.PaymentGateway;
import com.razorpay.RazorpayException;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private PaymentGateway paymentGateway;

    public PaymentService(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public String initiatePayment(String orderID,
                                  Long amount,
                                  String name,
                                  String email,
                                  String contactNo) throws RazorpayException {

        //HERE PAYMENT SERVICE WILL INTERACT WITH ORDER_SERVICE TO FETCH THE DETAILS FOR PRODUCTS

        //        Order order = orderService.getOrderDetails(orderId);
        //        int amount = order.getAmount;
        //        String productName = order.getProductName();
        //Long amount = 198767L; // 1987.67
        //Long amount = 10000L; // 100.00

        //Generate the payment link.

        return paymentGateway.generatePaymentLink(orderID, amount, name, email, contactNo);
    }
}

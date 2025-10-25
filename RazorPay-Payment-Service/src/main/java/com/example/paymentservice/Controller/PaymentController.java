package com.example.paymentservice.Controller;

import com.example.paymentservice.DTOS.initiatePaymentRequestDTO;
import com.example.paymentservice.PaymentGateway.PaymentGateway;
import com.example.paymentservice.Service.PaymentService;
import com.razorpay.RazorpayException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/")
    public String initiatePayment(@RequestBody initiatePaymentRequestDTO requestDTO) throws RazorpayException {
        return paymentService.initiatePayment(requestDTO.getOrderID(),
                requestDTO.getAmount(),
                requestDTO.getName(),
                requestDTO.getEmail(),
                requestDTO.getContactNo());
    }
}

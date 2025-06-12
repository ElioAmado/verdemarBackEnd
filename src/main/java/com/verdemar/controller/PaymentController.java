package com.verdemar.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.verdemar.domain.dto.CheckoutRequest;
import com.verdemar.service.StripeService;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Autowired
    private StripeService stripeService;

    /**
     * Endpoint to create a Stripe Checkout session.
     *
     * @param request the checkout request
     * @return a response with the session URL or error
     */
    @PostMapping("/create-checkout-session")
    public ResponseEntity<Map<String, String>> createCheckoutSession(@RequestBody CheckoutRequest request) {
        try {
            Session session = stripeService.createCheckoutSession(request);
            return ResponseEntity.ok(Collections.singletonMap("url", session.getUrl()));
        } catch (StripeException e) {
            e.printStackTrace(); // You can log properly with a logger
            return ResponseEntity.status(500)
                    .body(Collections.singletonMap("error", "Stripe session creation failed."));
        }
    }
}

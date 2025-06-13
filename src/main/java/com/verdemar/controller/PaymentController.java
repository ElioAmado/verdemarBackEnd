package com.verdemar.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.verdemar.domain.dto.CheckoutRequest;

@RestController
public class PaymentController {

    @Value("${stripe.secret.key}")
    String stripeSecretKey;

    public PaymentController() {
        // Inicializar la API key de Stripe (en producción, usa una variable de entorno)
        Stripe.apiKey = stripeSecretKey;
    }

    @PostMapping("/api/create-checkout-session")
    public ResponseEntity<Map<String, String>> createCheckoutSession(@RequestBody CheckoutRequest request) {
        try {
            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(
                    SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("eur") // o "usd", según tu sistema
                        .setUnitAmount((long) (request.getAmount() * 100)) // Monto en centavos
                        .setProductData(
                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName("Reserva apartamento")
                                .build()
                        )
                        .build()
                )
                .build();

            SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/confirmation")
                .setCancelUrl("http://localhost:3000/cancel")
                .addLineItem(lineItem)
                .build();

            Session session = Session.create(params);
            return ResponseEntity.ok(Collections.singletonMap("url", session.getUrl()));
        } catch (StripeException e) {
            e.printStackTrace(); // Puedes loguear o lanzar una excepción personalizada
            return ResponseEntity.status(500).body(Collections.singletonMap("error", e.getMessage()));
        }
    }
}

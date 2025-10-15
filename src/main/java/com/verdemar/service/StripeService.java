package com.verdemar.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.verdemar.domain.dto.CheckoutRequest;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

  @Value("${stripe.secret.key}")
  private String stripeSecretKey;

  @PostConstruct
  public void init() {
    System.out.println("Initializing Stripe with secret key: " + stripeSecretKey);
    Stripe.apiKey = stripeSecretKey;
  }

  /**
   * Creates a Stripe Checkout session based on the provided request.
   *
   * @param request the checkout request
   * @return the created Stripe session
   * @throws StripeException if Stripe API fails
   */
  public Session createCheckoutSession(CheckoutRequest request) throws StripeException {
    List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();

    // TODO: Customize based on request
    lineItems.add(
        SessionCreateParams.LineItem.builder()
            .setPrice("price_12345") // Replace with dynamic price ID
            .setQuantity(1L)
            .build());

    SessionCreateParams params =
        SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl("http://localhost:3000/success")
            .setCancelUrl("http://localhost:3000/cancel")
            .addAllLineItem(lineItems)
            .build();

    return Session.create(params);
  }
}

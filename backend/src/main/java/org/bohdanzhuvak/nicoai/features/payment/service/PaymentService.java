package org.bohdanzhuvak.nicoai.features.payment.service;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.features.payment.model.Payment;
import org.bohdanzhuvak.nicoai.features.payment.repository.PaymentRepository;
import org.bohdanzhuvak.nicoai.shared.config.StripeProperties;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
  private final PaymentRepository paymentRepository;
  private final StripeProperties stripeProperties;

  @PostConstruct
  private void init() {
    Stripe.apiKey = stripeProperties.getSecret_key();
  }

  public Session createCheckoutSession(String currency, long amount, String successUrl, String cancelUrl) throws Exception {
    SessionCreateParams params = SessionCreateParams.builder()
        .setMode(SessionCreateParams.Mode.PAYMENT)
        .setSuccessUrl(successUrl)
        .setCancelUrl(cancelUrl)
        .addLineItem(SessionCreateParams.LineItem.builder()
            .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency(currency)
                .setUnitAmount(amount)
                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                    .setName("Top up NicoAI Account")
                    .build())
                .build())
            .setQuantity(1L)
            .build())
        .build();
    return Session.create(params);
  }

  public void processPayment(Session session) {
    String sessionId = session.getId();
    Payment payment = paymentRepository.getPaymentBySessionId(sessionId);
    String customerEmail = session.getCustomerDetails().getEmail();
    payment.setCustomerEmail(customerEmail);
    payment.setStatus("SUCCESS");

    paymentRepository.save(payment);
  }

  public void addPayment(Session session) {
    String sessionId = session.getId();
    long amountTotal = session.getAmountTotal();
    Payment payment = new Payment();
    payment.setSessionId(sessionId);
    payment.setAmount(amountTotal);
    payment.setStatus("PENDING");

    paymentRepository.save(payment);
  }
}

package org.bohdanzhuvak.nicoai.features.payment.controller;

import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.features.payment.service.PaymentService;
import org.bohdanzhuvak.nicoai.shared.config.StripeProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StripeWebhookController {
  private final PaymentService paymentService;
  private final StripeProperties stripeProperties;

  @PostMapping("/stripe/webhook")
  public ResponseEntity<String> handleStripeWebhook(
      @RequestBody String payload,
      @RequestHeader("Stripe-Signature") String sigHeader) {
    try {
      Event event = Webhook.constructEvent(payload, sigHeader, stripeProperties.getWebhook_key());

      if ("checkout.session.completed".equals(event.getType())) {
        Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
        if (session != null) {
          paymentService.processPayment(session);
        }
      }

      return ResponseEntity.ok("Webhook received");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body("Invalid webhook signature");
    }
  }
}

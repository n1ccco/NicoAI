package org.bohdanzhuvak.nicoai.features.payment.controller;

import com.stripe.model.checkout.Session;
import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.features.payment.dto.StripeDto;
import org.bohdanzhuvak.nicoai.features.payment.service.PaymentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
  private final PaymentService paymentService;

  @PostMapping("/top-up")
  public StripeDto createCheckoutSession() throws Exception {
    Session session = paymentService.createCheckoutSession("EUR", 5 * 100, "http://localhost:3000/payment/success", "http://localhost:3000/payment/error");
    String stripeUrl = session.getUrl();
    paymentService.addPayment(session);
    return StripeDto.builder().paymentUrl(stripeUrl).build();
  }
}

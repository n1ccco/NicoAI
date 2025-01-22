package org.bohdanzhuvak.nicoai.features.payment.repository;

import org.bohdanzhuvak.nicoai.features.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
  Payment getPaymentBySessionId(String sessionId);
}

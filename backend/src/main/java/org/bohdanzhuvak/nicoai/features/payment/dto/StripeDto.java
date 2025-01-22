package org.bohdanzhuvak.nicoai.features.payment.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StripeDto {
  String paymentUrl;
}

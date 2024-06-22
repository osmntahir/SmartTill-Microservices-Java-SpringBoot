package com.toyota.saleservice.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PaymentTypeTest {

    @Test
    public void testPaymentTypeValues() {
        PaymentType[] expectedValues = { PaymentType.CASH, PaymentType.CREDIT_CARD, PaymentType.DEBIT_CARD };
        PaymentType[] actualValues = PaymentType.values();

        assertThat(actualValues).containsExactly(expectedValues);
    }

    @Test
    public void testPaymentTypeValueOf() {
        assertThat(PaymentType.valueOf("CASH")).isEqualTo(PaymentType.CASH);
        assertThat(PaymentType.valueOf("CREDIT_CARD")).isEqualTo(PaymentType.CREDIT_CARD);
        assertThat(PaymentType.valueOf("DEBIT_CARD")).isEqualTo(PaymentType.DEBIT_CARD);
    }
}

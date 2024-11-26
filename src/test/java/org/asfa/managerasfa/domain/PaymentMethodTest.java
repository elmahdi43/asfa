package org.asfa.managerasfa.domain;

import static org.asfa.managerasfa.domain.PaymentMethodTestSamples.*;
import static org.asfa.managerasfa.domain.PaymentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.asfa.managerasfa.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentMethodTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentMethod.class);
        PaymentMethod paymentMethod1 = getPaymentMethodSample1();
        PaymentMethod paymentMethod2 = new PaymentMethod();
        assertThat(paymentMethod1).isNotEqualTo(paymentMethod2);

        paymentMethod2.setId(paymentMethod1.getId());
        assertThat(paymentMethod1).isEqualTo(paymentMethod2);

        paymentMethod2 = getPaymentMethodSample2();
        assertThat(paymentMethod1).isNotEqualTo(paymentMethod2);
    }

    @Test
    void paymentTest() {
        PaymentMethod paymentMethod = getPaymentMethodRandomSampleGenerator();
        Payment paymentBack = getPaymentRandomSampleGenerator();

        paymentMethod.setPayment(paymentBack);
        assertThat(paymentMethod.getPayment()).isEqualTo(paymentBack);

        paymentMethod.payment(null);
        assertThat(paymentMethod.getPayment()).isNull();
    }
}

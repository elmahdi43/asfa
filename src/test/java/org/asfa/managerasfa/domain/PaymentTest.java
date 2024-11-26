package org.asfa.managerasfa.domain;

import static org.asfa.managerasfa.domain.MemberSubscriptionTestSamples.*;
import static org.asfa.managerasfa.domain.PaymentMethodTestSamples.*;
import static org.asfa.managerasfa.domain.PaymentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import org.asfa.managerasfa.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Payment.class);
        Payment payment1 = getPaymentSample1();
        Payment payment2 = new Payment();
        assertThat(payment1).isNotEqualTo(payment2);

        payment2.setId(payment1.getId());
        assertThat(payment1).isEqualTo(payment2);

        payment2 = getPaymentSample2();
        assertThat(payment1).isNotEqualTo(payment2);
    }

    @Test
    void memberSubscriptionTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        MemberSubscription memberSubscriptionBack = getMemberSubscriptionRandomSampleGenerator();

        payment.addMemberSubscription(memberSubscriptionBack);
        assertThat(payment.getMemberSubscriptions()).containsOnly(memberSubscriptionBack);
        assertThat(memberSubscriptionBack.getPayment()).isEqualTo(payment);

        payment.removeMemberSubscription(memberSubscriptionBack);
        assertThat(payment.getMemberSubscriptions()).doesNotContain(memberSubscriptionBack);
        assertThat(memberSubscriptionBack.getPayment()).isNull();

        payment.memberSubscriptions(new HashSet<>(Set.of(memberSubscriptionBack)));
        assertThat(payment.getMemberSubscriptions()).containsOnly(memberSubscriptionBack);
        assertThat(memberSubscriptionBack.getPayment()).isEqualTo(payment);

        payment.setMemberSubscriptions(new HashSet<>());
        assertThat(payment.getMemberSubscriptions()).doesNotContain(memberSubscriptionBack);
        assertThat(memberSubscriptionBack.getPayment()).isNull();
    }

    @Test
    void methodsTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        PaymentMethod paymentMethodBack = getPaymentMethodRandomSampleGenerator();

        payment.addMethods(paymentMethodBack);
        assertThat(payment.getMethods()).containsOnly(paymentMethodBack);
        assertThat(paymentMethodBack.getPayment()).isEqualTo(payment);

        payment.removeMethods(paymentMethodBack);
        assertThat(payment.getMethods()).doesNotContain(paymentMethodBack);
        assertThat(paymentMethodBack.getPayment()).isNull();

        payment.methods(new HashSet<>(Set.of(paymentMethodBack)));
        assertThat(payment.getMethods()).containsOnly(paymentMethodBack);
        assertThat(paymentMethodBack.getPayment()).isEqualTo(payment);

        payment.setMethods(new HashSet<>());
        assertThat(payment.getMethods()).doesNotContain(paymentMethodBack);
        assertThat(paymentMethodBack.getPayment()).isNull();
    }
}

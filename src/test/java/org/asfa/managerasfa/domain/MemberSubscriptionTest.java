package org.asfa.managerasfa.domain;

import static org.asfa.managerasfa.domain.MemberSubscriptionTestSamples.*;
import static org.asfa.managerasfa.domain.MemberTestSamples.*;
import static org.asfa.managerasfa.domain.PaymentTestSamples.*;
import static org.asfa.managerasfa.domain.ProductTestSamples.*;
import static org.asfa.managerasfa.domain.SubscriptionTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import org.asfa.managerasfa.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MemberSubscriptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MemberSubscription.class);
        MemberSubscription memberSubscription1 = getMemberSubscriptionSample1();
        MemberSubscription memberSubscription2 = new MemberSubscription();
        assertThat(memberSubscription1).isNotEqualTo(memberSubscription2);

        memberSubscription2.setId(memberSubscription1.getId());
        assertThat(memberSubscription1).isEqualTo(memberSubscription2);

        memberSubscription2 = getMemberSubscriptionSample2();
        assertThat(memberSubscription1).isNotEqualTo(memberSubscription2);
    }

    @Test
    void membersTest() {
        MemberSubscription memberSubscription = getMemberSubscriptionRandomSampleGenerator();
        Member memberBack = getMemberRandomSampleGenerator();

        memberSubscription.addMembers(memberBack);
        assertThat(memberSubscription.getMembers()).containsOnly(memberBack);
        assertThat(memberBack.getSubscription()).isEqualTo(memberSubscription);

        memberSubscription.removeMembers(memberBack);
        assertThat(memberSubscription.getMembers()).doesNotContain(memberBack);
        assertThat(memberBack.getSubscription()).isNull();

        memberSubscription.members(new HashSet<>(Set.of(memberBack)));
        assertThat(memberSubscription.getMembers()).containsOnly(memberBack);
        assertThat(memberBack.getSubscription()).isEqualTo(memberSubscription);

        memberSubscription.setMembers(new HashSet<>());
        assertThat(memberSubscription.getMembers()).doesNotContain(memberBack);
        assertThat(memberBack.getSubscription()).isNull();
    }

    @Test
    void typesTest() {
        MemberSubscription memberSubscription = getMemberSubscriptionRandomSampleGenerator();
        SubscriptionType subscriptionTypeBack = getSubscriptionTypeRandomSampleGenerator();

        memberSubscription.addTypes(subscriptionTypeBack);
        assertThat(memberSubscription.getTypes()).containsOnly(subscriptionTypeBack);
        assertThat(subscriptionTypeBack.getSubscription()).isEqualTo(memberSubscription);

        memberSubscription.removeTypes(subscriptionTypeBack);
        assertThat(memberSubscription.getTypes()).doesNotContain(subscriptionTypeBack);
        assertThat(subscriptionTypeBack.getSubscription()).isNull();

        memberSubscription.types(new HashSet<>(Set.of(subscriptionTypeBack)));
        assertThat(memberSubscription.getTypes()).containsOnly(subscriptionTypeBack);
        assertThat(subscriptionTypeBack.getSubscription()).isEqualTo(memberSubscription);

        memberSubscription.setTypes(new HashSet<>());
        assertThat(memberSubscription.getTypes()).doesNotContain(subscriptionTypeBack);
        assertThat(subscriptionTypeBack.getSubscription()).isNull();
    }

    @Test
    void productsTest() {
        MemberSubscription memberSubscription = getMemberSubscriptionRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        memberSubscription.addProducts(productBack);
        assertThat(memberSubscription.getProducts()).containsOnly(productBack);
        assertThat(productBack.getSubscription()).isEqualTo(memberSubscription);

        memberSubscription.removeProducts(productBack);
        assertThat(memberSubscription.getProducts()).doesNotContain(productBack);
        assertThat(productBack.getSubscription()).isNull();

        memberSubscription.products(new HashSet<>(Set.of(productBack)));
        assertThat(memberSubscription.getProducts()).containsOnly(productBack);
        assertThat(productBack.getSubscription()).isEqualTo(memberSubscription);

        memberSubscription.setProducts(new HashSet<>());
        assertThat(memberSubscription.getProducts()).doesNotContain(productBack);
        assertThat(productBack.getSubscription()).isNull();
    }

    @Test
    void paymentTest() {
        MemberSubscription memberSubscription = getMemberSubscriptionRandomSampleGenerator();
        Payment paymentBack = getPaymentRandomSampleGenerator();

        memberSubscription.setPayment(paymentBack);
        assertThat(memberSubscription.getPayment()).isEqualTo(paymentBack);

        memberSubscription.payment(null);
        assertThat(memberSubscription.getPayment()).isNull();
    }
}

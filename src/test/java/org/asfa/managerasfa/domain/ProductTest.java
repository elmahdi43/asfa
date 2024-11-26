package org.asfa.managerasfa.domain;

import static org.asfa.managerasfa.domain.CategoryTestSamples.*;
import static org.asfa.managerasfa.domain.MemberSubscriptionTestSamples.*;
import static org.asfa.managerasfa.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import org.asfa.managerasfa.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = getProductSample1();
        Product product2 = new Product();
        assertThat(product1).isNotEqualTo(product2);

        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);

        product2 = getProductSample2();
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void categoriesTest() {
        Product product = getProductRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        product.addCategories(categoryBack);
        assertThat(product.getCategories()).containsOnly(categoryBack);
        assertThat(categoryBack.getProduct()).isEqualTo(product);

        product.removeCategories(categoryBack);
        assertThat(product.getCategories()).doesNotContain(categoryBack);
        assertThat(categoryBack.getProduct()).isNull();

        product.categories(new HashSet<>(Set.of(categoryBack)));
        assertThat(product.getCategories()).containsOnly(categoryBack);
        assertThat(categoryBack.getProduct()).isEqualTo(product);

        product.setCategories(new HashSet<>());
        assertThat(product.getCategories()).doesNotContain(categoryBack);
        assertThat(categoryBack.getProduct()).isNull();
    }

    @Test
    void subscriptionTest() {
        Product product = getProductRandomSampleGenerator();
        MemberSubscription memberSubscriptionBack = getMemberSubscriptionRandomSampleGenerator();

        product.setSubscription(memberSubscriptionBack);
        assertThat(product.getSubscription()).isEqualTo(memberSubscriptionBack);

        product.subscription(null);
        assertThat(product.getSubscription()).isNull();
    }
}

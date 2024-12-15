package org.asfa.managerasfa.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class EventSubscriptionAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEventSubscriptionAllPropertiesEquals(EventSubscription expected, EventSubscription actual) {
        assertEventSubscriptionAutoGeneratedPropertiesEquals(expected, actual);
        assertEventSubscriptionAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEventSubscriptionAllUpdatablePropertiesEquals(EventSubscription expected, EventSubscription actual) {
        assertEventSubscriptionUpdatableFieldsEquals(expected, actual);
        assertEventSubscriptionUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEventSubscriptionAutoGeneratedPropertiesEquals(EventSubscription expected, EventSubscription actual) {
        assertThat(expected)
            .as("Verify EventSubscription auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEventSubscriptionUpdatableFieldsEquals(EventSubscription expected, EventSubscription actual) {
        assertThat(expected)
            .as("Verify EventSubscription relevant properties")
            .satisfies(e -> assertThat(e.getSubscriptionDate()).as("check subscriptionDate").isEqualTo(actual.getSubscriptionDate()))
            .satisfies(e -> assertThat(e.getIsActive()).as("check isActive").isEqualTo(actual.getIsActive()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEventSubscriptionUpdatableRelationshipsEquals(EventSubscription expected, EventSubscription actual) {
        assertThat(expected)
            .as("Verify EventSubscription relationships")
            .satisfies(e -> assertThat(e.getTypes()).as("check types").isEqualTo(actual.getTypes()))
            .satisfies(e -> assertThat(e.getPayment()).as("check payment").isEqualTo(actual.getPayment()))
            .satisfies(e -> assertThat(e.getMembers()).as("check members").isEqualTo(actual.getMembers()))
            .satisfies(e -> assertThat(e.getProducts()).as("check products").isEqualTo(actual.getProducts()));
    }
}
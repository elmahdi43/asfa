package org.asfa.managerasfa.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class MemberSubscriptionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MemberSubscription getMemberSubscriptionSample1() {
        return new MemberSubscription().id(1L);
    }

    public static MemberSubscription getMemberSubscriptionSample2() {
        return new MemberSubscription().id(2L);
    }

    public static MemberSubscription getMemberSubscriptionRandomSampleGenerator() {
        return new MemberSubscription().id(longCount.incrementAndGet());
    }
}

package org.asfa.managerasfa.repository;

import org.asfa.managerasfa.domain.MemberSubscription;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MemberSubscription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MemberSubscriptionRepository extends JpaRepository<MemberSubscription, Long> {}

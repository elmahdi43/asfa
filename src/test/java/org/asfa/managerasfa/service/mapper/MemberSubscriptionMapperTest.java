package org.asfa.managerasfa.service.mapper;

import static org.asfa.managerasfa.domain.MemberSubscriptionAsserts.*;
import static org.asfa.managerasfa.domain.MemberSubscriptionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MemberSubscriptionMapperTest {

    private MemberSubscriptionMapper memberSubscriptionMapper;

    @BeforeEach
    void setUp() {
        memberSubscriptionMapper = new MemberSubscriptionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMemberSubscriptionSample1();
        var actual = memberSubscriptionMapper.toEntity(memberSubscriptionMapper.toDto(expected));
        assertMemberSubscriptionAllPropertiesEquals(expected, actual);
    }
}

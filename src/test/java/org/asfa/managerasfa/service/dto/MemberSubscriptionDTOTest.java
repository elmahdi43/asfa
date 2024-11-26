package org.asfa.managerasfa.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.asfa.managerasfa.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MemberSubscriptionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MemberSubscriptionDTO.class);
        MemberSubscriptionDTO memberSubscriptionDTO1 = new MemberSubscriptionDTO();
        memberSubscriptionDTO1.setId(1L);
        MemberSubscriptionDTO memberSubscriptionDTO2 = new MemberSubscriptionDTO();
        assertThat(memberSubscriptionDTO1).isNotEqualTo(memberSubscriptionDTO2);
        memberSubscriptionDTO2.setId(memberSubscriptionDTO1.getId());
        assertThat(memberSubscriptionDTO1).isEqualTo(memberSubscriptionDTO2);
        memberSubscriptionDTO2.setId(2L);
        assertThat(memberSubscriptionDTO1).isNotEqualTo(memberSubscriptionDTO2);
        memberSubscriptionDTO1.setId(null);
        assertThat(memberSubscriptionDTO1).isNotEqualTo(memberSubscriptionDTO2);
    }
}

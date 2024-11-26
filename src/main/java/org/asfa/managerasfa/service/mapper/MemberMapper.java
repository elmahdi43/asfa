package org.asfa.managerasfa.service.mapper;

import org.asfa.managerasfa.domain.Member;
import org.asfa.managerasfa.domain.MemberSubscription;
import org.asfa.managerasfa.service.dto.MemberDTO;
import org.asfa.managerasfa.service.dto.MemberSubscriptionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Member} and its DTO {@link MemberDTO}.
 */
@Mapper(componentModel = "spring")
public interface MemberMapper extends EntityMapper<MemberDTO, Member> {
    @Mapping(target = "subscription", source = "subscription", qualifiedByName = "memberSubscriptionId")
    @Mapping(target = "member", source = "member", qualifiedByName = "memberId")
    MemberDTO toDto(Member s);

    @Named("memberId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MemberDTO toDtoMemberId(Member member);

    @Named("memberSubscriptionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MemberSubscriptionDTO toDtoMemberSubscriptionId(MemberSubscription memberSubscription);
}

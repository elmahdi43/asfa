package org.asfa.managerasfa.service;

import java.util.Optional;
import org.asfa.managerasfa.domain.MemberSubscription;
import org.asfa.managerasfa.repository.MemberSubscriptionRepository;
import org.asfa.managerasfa.service.dto.MemberSubscriptionDTO;
import org.asfa.managerasfa.service.mapper.MemberSubscriptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.asfa.managerasfa.domain.MemberSubscription}.
 */
@Service
@Transactional
public class MemberSubscriptionService {

    private static final Logger LOG = LoggerFactory.getLogger(MemberSubscriptionService.class);

    private final MemberSubscriptionRepository memberSubscriptionRepository;

    private final MemberSubscriptionMapper memberSubscriptionMapper;

    public MemberSubscriptionService(
        MemberSubscriptionRepository memberSubscriptionRepository,
        MemberSubscriptionMapper memberSubscriptionMapper
    ) {
        this.memberSubscriptionRepository = memberSubscriptionRepository;
        this.memberSubscriptionMapper = memberSubscriptionMapper;
    }

    /**
     * Save a memberSubscription.
     *
     * @param memberSubscriptionDTO the entity to save.
     * @return the persisted entity.
     */
    public MemberSubscriptionDTO save(MemberSubscriptionDTO memberSubscriptionDTO) {
        LOG.debug("Request to save MemberSubscription : {}", memberSubscriptionDTO);
        MemberSubscription memberSubscription = memberSubscriptionMapper.toEntity(memberSubscriptionDTO);
        memberSubscription = memberSubscriptionRepository.save(memberSubscription);
        return memberSubscriptionMapper.toDto(memberSubscription);
    }

    /**
     * Update a memberSubscription.
     *
     * @param memberSubscriptionDTO the entity to save.
     * @return the persisted entity.
     */
    public MemberSubscriptionDTO update(MemberSubscriptionDTO memberSubscriptionDTO) {
        LOG.debug("Request to update MemberSubscription : {}", memberSubscriptionDTO);
        MemberSubscription memberSubscription = memberSubscriptionMapper.toEntity(memberSubscriptionDTO);
        memberSubscription = memberSubscriptionRepository.save(memberSubscription);
        return memberSubscriptionMapper.toDto(memberSubscription);
    }

    /**
     * Partially update a memberSubscription.
     *
     * @param memberSubscriptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MemberSubscriptionDTO> partialUpdate(MemberSubscriptionDTO memberSubscriptionDTO) {
        LOG.debug("Request to partially update MemberSubscription : {}", memberSubscriptionDTO);

        return memberSubscriptionRepository
            .findById(memberSubscriptionDTO.getId())
            .map(existingMemberSubscription -> {
                memberSubscriptionMapper.partialUpdate(existingMemberSubscription, memberSubscriptionDTO);

                return existingMemberSubscription;
            })
            .map(memberSubscriptionRepository::save)
            .map(memberSubscriptionMapper::toDto);
    }

    /**
     * Get all the memberSubscriptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MemberSubscriptionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all MemberSubscriptions");
        return memberSubscriptionRepository.findAll(pageable).map(memberSubscriptionMapper::toDto);
    }

    /**
     * Get one memberSubscription by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MemberSubscriptionDTO> findOne(Long id) {
        LOG.debug("Request to get MemberSubscription : {}", id);
        return memberSubscriptionRepository.findById(id).map(memberSubscriptionMapper::toDto);
    }

    /**
     * Delete the memberSubscription by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MemberSubscription : {}", id);
        memberSubscriptionRepository.deleteById(id);
    }
}

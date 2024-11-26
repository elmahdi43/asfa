package org.asfa.managerasfa.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.asfa.managerasfa.repository.MemberSubscriptionRepository;
import org.asfa.managerasfa.service.MemberSubscriptionService;
import org.asfa.managerasfa.service.dto.MemberSubscriptionDTO;
import org.asfa.managerasfa.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.asfa.managerasfa.domain.MemberSubscription}.
 */
@RestController
@RequestMapping("/api/member-subscriptions")
public class MemberSubscriptionResource {

    private static final Logger LOG = LoggerFactory.getLogger(MemberSubscriptionResource.class);

    private static final String ENTITY_NAME = "memberSubscription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MemberSubscriptionService memberSubscriptionService;

    private final MemberSubscriptionRepository memberSubscriptionRepository;

    public MemberSubscriptionResource(
        MemberSubscriptionService memberSubscriptionService,
        MemberSubscriptionRepository memberSubscriptionRepository
    ) {
        this.memberSubscriptionService = memberSubscriptionService;
        this.memberSubscriptionRepository = memberSubscriptionRepository;
    }

    /**
     * {@code POST  /member-subscriptions} : Create a new memberSubscription.
     *
     * @param memberSubscriptionDTO the memberSubscriptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new memberSubscriptionDTO, or with status {@code 400 (Bad Request)} if the memberSubscription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MemberSubscriptionDTO> createMemberSubscription(@Valid @RequestBody MemberSubscriptionDTO memberSubscriptionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save MemberSubscription : {}", memberSubscriptionDTO);
        if (memberSubscriptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new memberSubscription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        memberSubscriptionDTO = memberSubscriptionService.save(memberSubscriptionDTO);
        return ResponseEntity.created(new URI("/api/member-subscriptions/" + memberSubscriptionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, memberSubscriptionDTO.getId().toString()))
            .body(memberSubscriptionDTO);
    }

    /**
     * {@code PUT  /member-subscriptions/:id} : Updates an existing memberSubscription.
     *
     * @param id the id of the memberSubscriptionDTO to save.
     * @param memberSubscriptionDTO the memberSubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated memberSubscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the memberSubscriptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the memberSubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MemberSubscriptionDTO> updateMemberSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MemberSubscriptionDTO memberSubscriptionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MemberSubscription : {}, {}", id, memberSubscriptionDTO);
        if (memberSubscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, memberSubscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!memberSubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        memberSubscriptionDTO = memberSubscriptionService.update(memberSubscriptionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, memberSubscriptionDTO.getId().toString()))
            .body(memberSubscriptionDTO);
    }

    /**
     * {@code PATCH  /member-subscriptions/:id} : Partial updates given fields of an existing memberSubscription, field will ignore if it is null
     *
     * @param id the id of the memberSubscriptionDTO to save.
     * @param memberSubscriptionDTO the memberSubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated memberSubscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the memberSubscriptionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the memberSubscriptionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the memberSubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MemberSubscriptionDTO> partialUpdateMemberSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MemberSubscriptionDTO memberSubscriptionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MemberSubscription partially : {}, {}", id, memberSubscriptionDTO);
        if (memberSubscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, memberSubscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!memberSubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MemberSubscriptionDTO> result = memberSubscriptionService.partialUpdate(memberSubscriptionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, memberSubscriptionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /member-subscriptions} : get all the memberSubscriptions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of memberSubscriptions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MemberSubscriptionDTO>> getAllMemberSubscriptions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of MemberSubscriptions");
        Page<MemberSubscriptionDTO> page = memberSubscriptionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /member-subscriptions/:id} : get the "id" memberSubscription.
     *
     * @param id the id of the memberSubscriptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the memberSubscriptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MemberSubscriptionDTO> getMemberSubscription(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MemberSubscription : {}", id);
        Optional<MemberSubscriptionDTO> memberSubscriptionDTO = memberSubscriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(memberSubscriptionDTO);
    }

    /**
     * {@code DELETE  /member-subscriptions/:id} : delete the "id" memberSubscription.
     *
     * @param id the id of the memberSubscriptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMemberSubscription(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MemberSubscription : {}", id);
        memberSubscriptionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

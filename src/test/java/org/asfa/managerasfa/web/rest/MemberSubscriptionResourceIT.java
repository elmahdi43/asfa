package org.asfa.managerasfa.web.rest;

import static org.asfa.managerasfa.domain.MemberSubscriptionAsserts.*;
import static org.asfa.managerasfa.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.asfa.managerasfa.IntegrationTest;
import org.asfa.managerasfa.domain.MemberSubscription;
import org.asfa.managerasfa.repository.MemberSubscriptionRepository;
import org.asfa.managerasfa.service.dto.MemberSubscriptionDTO;
import org.asfa.managerasfa.service.mapper.MemberSubscriptionMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MemberSubscriptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MemberSubscriptionResourceIT {

    private static final LocalDate DEFAULT_SUBSCRIPTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SUBSCRIPTION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/member-subscriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MemberSubscriptionRepository memberSubscriptionRepository;

    @Autowired
    private MemberSubscriptionMapper memberSubscriptionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMemberSubscriptionMockMvc;

    private MemberSubscription memberSubscription;

    private MemberSubscription insertedMemberSubscription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MemberSubscription createEntity() {
        return new MemberSubscription().subscriptionDate(DEFAULT_SUBSCRIPTION_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MemberSubscription createUpdatedEntity() {
        return new MemberSubscription().subscriptionDate(UPDATED_SUBSCRIPTION_DATE);
    }

    @BeforeEach
    public void initTest() {
        memberSubscription = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMemberSubscription != null) {
            memberSubscriptionRepository.delete(insertedMemberSubscription);
            insertedMemberSubscription = null;
        }
    }

    @Test
    @Transactional
    void createMemberSubscription() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MemberSubscription
        MemberSubscriptionDTO memberSubscriptionDTO = memberSubscriptionMapper.toDto(memberSubscription);
        var returnedMemberSubscriptionDTO = om.readValue(
            restMemberSubscriptionMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(memberSubscriptionDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MemberSubscriptionDTO.class
        );

        // Validate the MemberSubscription in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMemberSubscription = memberSubscriptionMapper.toEntity(returnedMemberSubscriptionDTO);
        assertMemberSubscriptionUpdatableFieldsEquals(
            returnedMemberSubscription,
            getPersistedMemberSubscription(returnedMemberSubscription)
        );

        insertedMemberSubscription = returnedMemberSubscription;
    }

    @Test
    @Transactional
    void createMemberSubscriptionWithExistingId() throws Exception {
        // Create the MemberSubscription with an existing ID
        memberSubscription.setId(1L);
        MemberSubscriptionDTO memberSubscriptionDTO = memberSubscriptionMapper.toDto(memberSubscription);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMemberSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(memberSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MemberSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSubscriptionDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        memberSubscription.setSubscriptionDate(null);

        // Create the MemberSubscription, which fails.
        MemberSubscriptionDTO memberSubscriptionDTO = memberSubscriptionMapper.toDto(memberSubscription);

        restMemberSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(memberSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMemberSubscriptions() throws Exception {
        // Initialize the database
        insertedMemberSubscription = memberSubscriptionRepository.saveAndFlush(memberSubscription);

        // Get all the memberSubscriptionList
        restMemberSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(memberSubscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].subscriptionDate").value(hasItem(DEFAULT_SUBSCRIPTION_DATE.toString())));
    }

    @Test
    @Transactional
    void getMemberSubscription() throws Exception {
        // Initialize the database
        insertedMemberSubscription = memberSubscriptionRepository.saveAndFlush(memberSubscription);

        // Get the memberSubscription
        restMemberSubscriptionMockMvc
            .perform(get(ENTITY_API_URL_ID, memberSubscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(memberSubscription.getId().intValue()))
            .andExpect(jsonPath("$.subscriptionDate").value(DEFAULT_SUBSCRIPTION_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMemberSubscription() throws Exception {
        // Get the memberSubscription
        restMemberSubscriptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMemberSubscription() throws Exception {
        // Initialize the database
        insertedMemberSubscription = memberSubscriptionRepository.saveAndFlush(memberSubscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the memberSubscription
        MemberSubscription updatedMemberSubscription = memberSubscriptionRepository.findById(memberSubscription.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMemberSubscription are not directly saved in db
        em.detach(updatedMemberSubscription);
        updatedMemberSubscription.subscriptionDate(UPDATED_SUBSCRIPTION_DATE);
        MemberSubscriptionDTO memberSubscriptionDTO = memberSubscriptionMapper.toDto(updatedMemberSubscription);

        restMemberSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, memberSubscriptionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(memberSubscriptionDTO))
            )
            .andExpect(status().isOk());

        // Validate the MemberSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMemberSubscriptionToMatchAllProperties(updatedMemberSubscription);
    }

    @Test
    @Transactional
    void putNonExistingMemberSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        memberSubscription.setId(longCount.incrementAndGet());

        // Create the MemberSubscription
        MemberSubscriptionDTO memberSubscriptionDTO = memberSubscriptionMapper.toDto(memberSubscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemberSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, memberSubscriptionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(memberSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MemberSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMemberSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        memberSubscription.setId(longCount.incrementAndGet());

        // Create the MemberSubscription
        MemberSubscriptionDTO memberSubscriptionDTO = memberSubscriptionMapper.toDto(memberSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(memberSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MemberSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMemberSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        memberSubscription.setId(longCount.incrementAndGet());

        // Create the MemberSubscription
        MemberSubscriptionDTO memberSubscriptionDTO = memberSubscriptionMapper.toDto(memberSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(memberSubscriptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MemberSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMemberSubscriptionWithPatch() throws Exception {
        // Initialize the database
        insertedMemberSubscription = memberSubscriptionRepository.saveAndFlush(memberSubscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the memberSubscription using partial update
        MemberSubscription partialUpdatedMemberSubscription = new MemberSubscription();
        partialUpdatedMemberSubscription.setId(memberSubscription.getId());

        partialUpdatedMemberSubscription.subscriptionDate(UPDATED_SUBSCRIPTION_DATE);

        restMemberSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMemberSubscription.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMemberSubscription))
            )
            .andExpect(status().isOk());

        // Validate the MemberSubscription in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMemberSubscriptionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMemberSubscription, memberSubscription),
            getPersistedMemberSubscription(memberSubscription)
        );
    }

    @Test
    @Transactional
    void fullUpdateMemberSubscriptionWithPatch() throws Exception {
        // Initialize the database
        insertedMemberSubscription = memberSubscriptionRepository.saveAndFlush(memberSubscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the memberSubscription using partial update
        MemberSubscription partialUpdatedMemberSubscription = new MemberSubscription();
        partialUpdatedMemberSubscription.setId(memberSubscription.getId());

        partialUpdatedMemberSubscription.subscriptionDate(UPDATED_SUBSCRIPTION_DATE);

        restMemberSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMemberSubscription.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMemberSubscription))
            )
            .andExpect(status().isOk());

        // Validate the MemberSubscription in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMemberSubscriptionUpdatableFieldsEquals(
            partialUpdatedMemberSubscription,
            getPersistedMemberSubscription(partialUpdatedMemberSubscription)
        );
    }

    @Test
    @Transactional
    void patchNonExistingMemberSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        memberSubscription.setId(longCount.incrementAndGet());

        // Create the MemberSubscription
        MemberSubscriptionDTO memberSubscriptionDTO = memberSubscriptionMapper.toDto(memberSubscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemberSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, memberSubscriptionDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(memberSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MemberSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMemberSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        memberSubscription.setId(longCount.incrementAndGet());

        // Create the MemberSubscription
        MemberSubscriptionDTO memberSubscriptionDTO = memberSubscriptionMapper.toDto(memberSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(memberSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MemberSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMemberSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        memberSubscription.setId(longCount.incrementAndGet());

        // Create the MemberSubscription
        MemberSubscriptionDTO memberSubscriptionDTO = memberSubscriptionMapper.toDto(memberSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(memberSubscriptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MemberSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMemberSubscription() throws Exception {
        // Initialize the database
        insertedMemberSubscription = memberSubscriptionRepository.saveAndFlush(memberSubscription);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the memberSubscription
        restMemberSubscriptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, memberSubscription.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return memberSubscriptionRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected MemberSubscription getPersistedMemberSubscription(MemberSubscription memberSubscription) {
        return memberSubscriptionRepository.findById(memberSubscription.getId()).orElseThrow();
    }

    protected void assertPersistedMemberSubscriptionToMatchAllProperties(MemberSubscription expectedMemberSubscription) {
        assertMemberSubscriptionAllPropertiesEquals(expectedMemberSubscription, getPersistedMemberSubscription(expectedMemberSubscription));
    }

    protected void assertPersistedMemberSubscriptionToMatchUpdatableProperties(MemberSubscription expectedMemberSubscription) {
        assertMemberSubscriptionAllUpdatablePropertiesEquals(
            expectedMemberSubscription,
            getPersistedMemberSubscription(expectedMemberSubscription)
        );
    }
}

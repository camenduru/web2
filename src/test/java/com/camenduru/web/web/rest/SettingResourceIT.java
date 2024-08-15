package com.camenduru.web.web.rest;

import static com.camenduru.web.domain.SettingAsserts.*;
import static com.camenduru.web.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.camenduru.web.IntegrationTest;
import com.camenduru.web.domain.Setting;
import com.camenduru.web.domain.enumeration.Membership;
import com.camenduru.web.repository.SettingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link SettingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SettingResourceIT {

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_TOTAL = "AAAAAAAAAA";
    private static final String UPDATED_TOTAL = "BBBBBBBBBB";

    private static final Membership DEFAULT_MEMBERSHIP = Membership.FREE;
    private static final Membership UPDATED_MEMBERSHIP = Membership.PAID;

    private static final String DEFAULT_NOTIFY_URI = "AAAAAAAAAA";
    private static final String UPDATED_NOTIFY_URI = "BBBBBBBBBB";

    private static final String DEFAULT_NOTIFY_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_NOTIFY_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_DISCORD_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_DISCORD_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_DISCORD_ID = "AAAAAAAAAA";
    private static final String UPDATED_DISCORD_ID = "BBBBBBBBBB";

    private static final String DEFAULT_DISCORD_CHANNEL = "AAAAAAAAAA";
    private static final String UPDATED_DISCORD_CHANNEL = "BBBBBBBBBB";

    private static final String DEFAULT_DISCORD_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_DISCORD_TOKEN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/settings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SettingRepository settingRepository;

    @Autowired
    private MockMvc restSettingMockMvc;

    private Setting setting;

    private Setting insertedSetting;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Setting createEntity() {
        Setting setting = new Setting()
            .login(DEFAULT_LOGIN)
            .total(DEFAULT_TOTAL)
            .membership(DEFAULT_MEMBERSHIP)
            .notifyUri(DEFAULT_NOTIFY_URI)
            .notifyToken(DEFAULT_NOTIFY_TOKEN)
            .discordUsername(DEFAULT_DISCORD_USERNAME)
            .discordId(DEFAULT_DISCORD_ID)
            .discordChannel(DEFAULT_DISCORD_CHANNEL)
            .discordToken(DEFAULT_DISCORD_TOKEN);
        return setting;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Setting createUpdatedEntity() {
        Setting setting = new Setting()
            .login(UPDATED_LOGIN)
            .total(UPDATED_TOTAL)
            .membership(UPDATED_MEMBERSHIP)
            .notifyUri(UPDATED_NOTIFY_URI)
            .notifyToken(UPDATED_NOTIFY_TOKEN)
            .discordUsername(UPDATED_DISCORD_USERNAME)
            .discordId(UPDATED_DISCORD_ID)
            .discordChannel(UPDATED_DISCORD_CHANNEL)
            .discordToken(UPDATED_DISCORD_TOKEN);
        return setting;
    }

    @BeforeEach
    public void initTest() {
        setting = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSetting != null) {
            settingRepository.delete(insertedSetting);
            insertedSetting = null;
        }
    }

    @Test
    void createSetting() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Setting
        var returnedSetting = om.readValue(
            restSettingMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(setting)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Setting.class
        );

        // Validate the Setting in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSettingUpdatableFieldsEquals(returnedSetting, getPersistedSetting(returnedSetting));

        insertedSetting = returnedSetting;
    }

    @Test
    void createSettingWithExistingId() throws Exception {
        // Create the Setting with an existing ID
        setting.setId("existing_id");

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(setting)))
            .andExpect(status().isBadRequest());

        // Validate the Setting in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkLoginIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        setting.setLogin(null);

        // Create the Setting, which fails.

        restSettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(setting)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkTotalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        setting.setTotal(null);

        // Create the Setting, which fails.

        restSettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(setting)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkMembershipIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        setting.setMembership(null);

        // Create the Setting, which fails.

        restSettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(setting)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkNotifyUriIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        setting.setNotifyUri(null);

        // Create the Setting, which fails.

        restSettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(setting)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkNotifyTokenIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        setting.setNotifyToken(null);

        // Create the Setting, which fails.

        restSettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(setting)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkDiscordUsernameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        setting.setDiscordUsername(null);

        // Create the Setting, which fails.

        restSettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(setting)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkDiscordIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        setting.setDiscordId(null);

        // Create the Setting, which fails.

        restSettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(setting)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkDiscordChannelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        setting.setDiscordChannel(null);

        // Create the Setting, which fails.

        restSettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(setting)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkDiscordTokenIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        setting.setDiscordToken(null);

        // Create the Setting, which fails.

        restSettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(setting)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllSettings() throws Exception {
        // Initialize the database
        insertedSetting = settingRepository.save(setting);

        // Get all the settingList
        restSettingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(setting.getId())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL)))
            .andExpect(jsonPath("$.[*].membership").value(hasItem(DEFAULT_MEMBERSHIP.toString())))
            .andExpect(jsonPath("$.[*].notifyUri").value(hasItem(DEFAULT_NOTIFY_URI)))
            .andExpect(jsonPath("$.[*].notifyToken").value(hasItem(DEFAULT_NOTIFY_TOKEN)))
            .andExpect(jsonPath("$.[*].discordUsername").value(hasItem(DEFAULT_DISCORD_USERNAME)))
            .andExpect(jsonPath("$.[*].discordId").value(hasItem(DEFAULT_DISCORD_ID)))
            .andExpect(jsonPath("$.[*].discordChannel").value(hasItem(DEFAULT_DISCORD_CHANNEL)))
            .andExpect(jsonPath("$.[*].discordToken").value(hasItem(DEFAULT_DISCORD_TOKEN)));
    }

    @Test
    void getSetting() throws Exception {
        // Initialize the database
        insertedSetting = settingRepository.save(setting);

        // Get the setting
        restSettingMockMvc
            .perform(get(ENTITY_API_URL_ID, setting.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(setting.getId()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL))
            .andExpect(jsonPath("$.membership").value(DEFAULT_MEMBERSHIP.toString()))
            .andExpect(jsonPath("$.notifyUri").value(DEFAULT_NOTIFY_URI))
            .andExpect(jsonPath("$.notifyToken").value(DEFAULT_NOTIFY_TOKEN))
            .andExpect(jsonPath("$.discordUsername").value(DEFAULT_DISCORD_USERNAME))
            .andExpect(jsonPath("$.discordId").value(DEFAULT_DISCORD_ID))
            .andExpect(jsonPath("$.discordChannel").value(DEFAULT_DISCORD_CHANNEL))
            .andExpect(jsonPath("$.discordToken").value(DEFAULT_DISCORD_TOKEN));
    }

    @Test
    void getNonExistingSetting() throws Exception {
        // Get the setting
        restSettingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingSetting() throws Exception {
        // Initialize the database
        insertedSetting = settingRepository.save(setting);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the setting
        Setting updatedSetting = settingRepository.findById(setting.getId()).orElseThrow();
        updatedSetting
            .login(UPDATED_LOGIN)
            .total(UPDATED_TOTAL)
            .membership(UPDATED_MEMBERSHIP)
            .notifyUri(UPDATED_NOTIFY_URI)
            .notifyToken(UPDATED_NOTIFY_TOKEN)
            .discordUsername(UPDATED_DISCORD_USERNAME)
            .discordId(UPDATED_DISCORD_ID)
            .discordChannel(UPDATED_DISCORD_CHANNEL)
            .discordToken(UPDATED_DISCORD_TOKEN);

        restSettingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSetting.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSetting))
            )
            .andExpect(status().isOk());

        // Validate the Setting in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSettingToMatchAllProperties(updatedSetting);
    }

    @Test
    void putNonExistingSetting() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        setting.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSettingMockMvc
            .perform(put(ENTITY_API_URL_ID, setting.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(setting)))
            .andExpect(status().isBadRequest());

        // Validate the Setting in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSetting() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        setting.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSettingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(setting))
            )
            .andExpect(status().isBadRequest());

        // Validate the Setting in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSetting() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        setting.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSettingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(setting)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Setting in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSettingWithPatch() throws Exception {
        // Initialize the database
        insertedSetting = settingRepository.save(setting);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the setting using partial update
        Setting partialUpdatedSetting = new Setting();
        partialUpdatedSetting.setId(setting.getId());

        partialUpdatedSetting
            .login(UPDATED_LOGIN)
            .notifyUri(UPDATED_NOTIFY_URI)
            .discordId(UPDATED_DISCORD_ID)
            .discordChannel(UPDATED_DISCORD_CHANNEL)
            .discordToken(UPDATED_DISCORD_TOKEN);

        restSettingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSetting.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSetting))
            )
            .andExpect(status().isOk());

        // Validate the Setting in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSettingUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSetting, setting), getPersistedSetting(setting));
    }

    @Test
    void fullUpdateSettingWithPatch() throws Exception {
        // Initialize the database
        insertedSetting = settingRepository.save(setting);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the setting using partial update
        Setting partialUpdatedSetting = new Setting();
        partialUpdatedSetting.setId(setting.getId());

        partialUpdatedSetting
            .login(UPDATED_LOGIN)
            .total(UPDATED_TOTAL)
            .membership(UPDATED_MEMBERSHIP)
            .notifyUri(UPDATED_NOTIFY_URI)
            .notifyToken(UPDATED_NOTIFY_TOKEN)
            .discordUsername(UPDATED_DISCORD_USERNAME)
            .discordId(UPDATED_DISCORD_ID)
            .discordChannel(UPDATED_DISCORD_CHANNEL)
            .discordToken(UPDATED_DISCORD_TOKEN);

        restSettingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSetting.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSetting))
            )
            .andExpect(status().isOk());

        // Validate the Setting in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSettingUpdatableFieldsEquals(partialUpdatedSetting, getPersistedSetting(partialUpdatedSetting));
    }

    @Test
    void patchNonExistingSetting() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        setting.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSettingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, setting.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(setting))
            )
            .andExpect(status().isBadRequest());

        // Validate the Setting in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSetting() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        setting.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSettingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(setting))
            )
            .andExpect(status().isBadRequest());

        // Validate the Setting in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSetting() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        setting.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSettingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(setting)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Setting in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSetting() throws Exception {
        // Initialize the database
        insertedSetting = settingRepository.save(setting);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the setting
        restSettingMockMvc
            .perform(delete(ENTITY_API_URL_ID, setting.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return settingRepository.count();
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

    protected Setting getPersistedSetting(Setting setting) {
        return settingRepository.findById(setting.getId()).orElseThrow();
    }

    protected void assertPersistedSettingToMatchAllProperties(Setting expectedSetting) {
        assertSettingAllPropertiesEquals(expectedSetting, getPersistedSetting(expectedSetting));
    }

    protected void assertPersistedSettingToMatchUpdatableProperties(Setting expectedSetting) {
        assertSettingAllUpdatablePropertiesEquals(expectedSetting, getPersistedSetting(expectedSetting));
    }
}

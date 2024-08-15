package com.camenduru.web.web.rest;

import static com.camenduru.web.domain.AppAsserts.*;
import static com.camenduru.web.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.camenduru.web.IntegrationTest;
import com.camenduru.web.domain.App;
import com.camenduru.web.repository.AppRepository;
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
 * Integration tests for the {@link AppResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AppResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_AMOUNT = "AAAAAAAAAA";
    private static final String UPDATED_AMOUNT = "BBBBBBBBBB";

    private static final String DEFAULT_SCHEMA = "AAAAAAAAAA";
    private static final String UPDATED_SCHEMA = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_MODEL = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DEFAULT = false;
    private static final Boolean UPDATED_IS_DEFAULT = true;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Boolean DEFAULT_IS_FREE = false;
    private static final Boolean UPDATED_IS_FREE = true;

    private static final String ENTITY_API_URL = "/api/apps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private MockMvc restAppMockMvc;

    private App app;

    private App insertedApp;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static App createEntity() {
        App app = new App()
            .type(DEFAULT_TYPE)
            .amount(DEFAULT_AMOUNT)
            .schema(DEFAULT_SCHEMA)
            .model(DEFAULT_MODEL)
            .title(DEFAULT_TITLE)
            .isDefault(DEFAULT_IS_DEFAULT)
            .isActive(DEFAULT_IS_ACTIVE)
            .isFree(DEFAULT_IS_FREE);
        return app;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static App createUpdatedEntity() {
        App app = new App()
            .type(UPDATED_TYPE)
            .amount(UPDATED_AMOUNT)
            .schema(UPDATED_SCHEMA)
            .model(UPDATED_MODEL)
            .title(UPDATED_TITLE)
            .isDefault(UPDATED_IS_DEFAULT)
            .isActive(UPDATED_IS_ACTIVE)
            .isFree(UPDATED_IS_FREE);
        return app;
    }

    @BeforeEach
    public void initTest() {
        app = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedApp != null) {
            appRepository.delete(insertedApp);
            insertedApp = null;
        }
    }

    @Test
    void createApp() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the App
        var returnedApp = om.readValue(
            restAppMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(app)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            App.class
        );

        // Validate the App in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAppUpdatableFieldsEquals(returnedApp, getPersistedApp(returnedApp));

        insertedApp = returnedApp;
    }

    @Test
    void createAppWithExistingId() throws Exception {
        // Create the App with an existing ID
        app.setId("existing_id");

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(app)))
            .andExpect(status().isBadRequest());

        // Validate the App in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        app.setType(null);

        // Create the App, which fails.

        restAppMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(app)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        app.setAmount(null);

        // Create the App, which fails.

        restAppMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(app)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkSchemaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        app.setSchema(null);

        // Create the App, which fails.

        restAppMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(app)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkModelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        app.setModel(null);

        // Create the App, which fails.

        restAppMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(app)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        app.setTitle(null);

        // Create the App, which fails.

        restAppMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(app)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkIsDefaultIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        app.setIsDefault(null);

        // Create the App, which fails.

        restAppMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(app)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        app.setIsActive(null);

        // Create the App, which fails.

        restAppMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(app)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkIsFreeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        app.setIsFree(null);

        // Create the App, which fails.

        restAppMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(app)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllApps() throws Exception {
        // Initialize the database
        insertedApp = appRepository.save(app);

        // Get all the appList
        restAppMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(app.getId())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].schema").value(hasItem(DEFAULT_SCHEMA)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT.booleanValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isFree").value(hasItem(DEFAULT_IS_FREE.booleanValue())));
    }

    @Test
    void getApp() throws Exception {
        // Initialize the database
        insertedApp = appRepository.save(app);

        // Get the app
        restAppMockMvc
            .perform(get(ENTITY_API_URL_ID, app.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(app.getId()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT))
            .andExpect(jsonPath("$.schema").value(DEFAULT_SCHEMA))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.isDefault").value(DEFAULT_IS_DEFAULT.booleanValue()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.isFree").value(DEFAULT_IS_FREE.booleanValue()));
    }

    @Test
    void getNonExistingApp() throws Exception {
        // Get the app
        restAppMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingApp() throws Exception {
        // Initialize the database
        insertedApp = appRepository.save(app);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the app
        App updatedApp = appRepository.findById(app.getId()).orElseThrow();
        updatedApp
            .type(UPDATED_TYPE)
            .amount(UPDATED_AMOUNT)
            .schema(UPDATED_SCHEMA)
            .model(UPDATED_MODEL)
            .title(UPDATED_TITLE)
            .isDefault(UPDATED_IS_DEFAULT)
            .isActive(UPDATED_IS_ACTIVE)
            .isFree(UPDATED_IS_FREE);

        restAppMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApp.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(updatedApp))
            )
            .andExpect(status().isOk());

        // Validate the App in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAppToMatchAllProperties(updatedApp);
    }

    @Test
    void putNonExistingApp() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        app.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppMockMvc
            .perform(put(ENTITY_API_URL_ID, app.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(app)))
            .andExpect(status().isBadRequest());

        // Validate the App in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchApp() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        app.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(app))
            )
            .andExpect(status().isBadRequest());

        // Validate the App in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamApp() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        app.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(app)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the App in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAppWithPatch() throws Exception {
        // Initialize the database
        insertedApp = appRepository.save(app);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the app using partial update
        App partialUpdatedApp = new App();
        partialUpdatedApp.setId(app.getId());

        partialUpdatedApp
            .type(UPDATED_TYPE)
            .schema(UPDATED_SCHEMA)
            .title(UPDATED_TITLE)
            .isActive(UPDATED_IS_ACTIVE)
            .isFree(UPDATED_IS_FREE);

        restAppMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApp.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedApp))
            )
            .andExpect(status().isOk());

        // Validate the App in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedApp, app), getPersistedApp(app));
    }

    @Test
    void fullUpdateAppWithPatch() throws Exception {
        // Initialize the database
        insertedApp = appRepository.save(app);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the app using partial update
        App partialUpdatedApp = new App();
        partialUpdatedApp.setId(app.getId());

        partialUpdatedApp
            .type(UPDATED_TYPE)
            .amount(UPDATED_AMOUNT)
            .schema(UPDATED_SCHEMA)
            .model(UPDATED_MODEL)
            .title(UPDATED_TITLE)
            .isDefault(UPDATED_IS_DEFAULT)
            .isActive(UPDATED_IS_ACTIVE)
            .isFree(UPDATED_IS_FREE);

        restAppMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApp.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedApp))
            )
            .andExpect(status().isOk());

        // Validate the App in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppUpdatableFieldsEquals(partialUpdatedApp, getPersistedApp(partialUpdatedApp));
    }

    @Test
    void patchNonExistingApp() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        app.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppMockMvc
            .perform(patch(ENTITY_API_URL_ID, app.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(app)))
            .andExpect(status().isBadRequest());

        // Validate the App in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchApp() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        app.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(app))
            )
            .andExpect(status().isBadRequest());

        // Validate the App in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamApp() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        app.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(app)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the App in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteApp() throws Exception {
        // Initialize the database
        insertedApp = appRepository.save(app);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the app
        restAppMockMvc.perform(delete(ENTITY_API_URL_ID, app.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return appRepository.count();
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

    protected App getPersistedApp(App app) {
        return appRepository.findById(app.getId()).orElseThrow();
    }

    protected void assertPersistedAppToMatchAllProperties(App expectedApp) {
        assertAppAllPropertiesEquals(expectedApp, getPersistedApp(expectedApp));
    }

    protected void assertPersistedAppToMatchUpdatableProperties(App expectedApp) {
        assertAppAllUpdatablePropertiesEquals(expectedApp, getPersistedApp(expectedApp));
    }
}

package com.camenduru.web.web.rest;

import com.camenduru.web.domain.Setting;
import com.camenduru.web.repository.SettingRepository;
import com.camenduru.web.security.AuthoritiesConstants;
import com.camenduru.web.security.SecurityUtils;
import com.camenduru.web.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.camenduru.web.domain.Setting}.
 */
@RestController
@RequestMapping("/api/settings")
public class SettingResource {

    private static final Logger log = LoggerFactory.getLogger(SettingResource.class);

    private static final String ENTITY_NAME = "setting";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SettingRepository settingRepository;

    public SettingResource(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    /**
     * {@code POST  /settings} : Create a new setting.
     *
     * @param setting the setting to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new setting, or with status {@code 400 (Bad Request)} if the setting has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Setting> createSetting(@Valid @RequestBody Setting setting) throws URISyntaxException {
        log.debug("REST request to save Setting : {}", setting);
        if (setting.getId() != null) {
            throw new BadRequestAlertException("A new setting cannot already have an ID", ENTITY_NAME, "idexists");
        }
        setting = settingRepository.save(setting);
        return ResponseEntity.created(new URI("/api/settings/" + setting.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, setting.getId()))
            .body(setting);
    }

    /**
     * {@code PUT  /settings/:id} : Updates an existing setting.
     *
     * @param id the id of the setting to save.
     * @param setting the setting to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated setting,
     * or with status {@code 400 (Bad Request)} if the setting is not valid,
     * or with status {@code 500 (Internal Server Error)} if the setting couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_PAID', 'ROLE_ADMIN')")
    public ResponseEntity<Setting> updateSetting(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody Setting setting
    ) throws URISyntaxException {
        log.debug("REST request to update Setting : {}, {}", id, setting);
        if (setting.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, setting.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!settingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            setting = settingRepository.save(setting);
        } else if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.PAID)) {
            Setting currentSetting = settingRepository
                .findOneByUserIsCurrentUser(id, SecurityUtils.getCurrentUserLogin().orElseThrow())
                .orElseThrow();
            setting.login(currentSetting.getLogin());
            setting.total(currentSetting.getTotal());
            setting.membership(currentSetting.getMembership());
            setting = settingRepository.save(setting);
        } else {
            Setting currentSetting = settingRepository
                .findOneByUserIsCurrentUser(id, SecurityUtils.getCurrentUserLogin().orElseThrow())
                .orElseThrow();
            setting.login(currentSetting.getLogin());
            setting.total(currentSetting.getTotal());
            setting.membership(currentSetting.getMembership());
            setting.notifyUri(currentSetting.getNotifyUri());
            setting.notifyToken(currentSetting.getNotifyToken());
            setting.discordToken(currentSetting.getDiscordToken());
            setting.apiKey(currentSetting.getApiKey());
            setting = settingRepository.save(setting);
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, setting.getId()))
            .body(setting);
    }

    /**
     * {@code PATCH  /settings/:id} : Partial updates given fields of an existing setting, field will ignore if it is null
     *
     * @param id the id of the setting to save.
     * @param setting the setting to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated setting,
     * or with status {@code 400 (Bad Request)} if the setting is not valid,
     * or with status {@code 404 (Not Found)} if the setting is not found,
     * or with status {@code 500 (Internal Server Error)} if the setting couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_PAID', 'ROLE_ADMIN')")
    public ResponseEntity<Setting> partialUpdateSetting(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody Setting setting
    ) throws URISyntaxException {
        log.debug("REST request to partial update Setting partially : {}, {}", id, setting);
        if (setting.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, setting.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!settingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<Setting> result;
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            result = settingRepository
                .findById(setting.getId())
                .map(existingSetting -> {
                    if (setting.getLogin() != null) {
                        existingSetting.setLogin(setting.getLogin());
                    }
                    if (setting.getTotal() != null) {
                        existingSetting.setTotal(setting.getTotal());
                    }
                    if (setting.getMembership() != null) {
                        existingSetting.setMembership(setting.getMembership());
                    }
                    if (setting.getNotifyUri() != null) {
                        existingSetting.setNotifyUri(setting.getNotifyUri());
                    }
                    if (setting.getNotifyToken() != null) {
                        existingSetting.setNotifyToken(setting.getNotifyToken());
                    }
                    if (setting.getDiscordUsername() != null) {
                        existingSetting.setDiscordUsername(setting.getDiscordUsername());
                    }
                    if (setting.getDiscordId() != null) {
                        existingSetting.setDiscordId(setting.getDiscordId());
                    }
                    if (setting.getDiscordChannel() != null) {
                        existingSetting.setDiscordChannel(setting.getDiscordChannel());
                    }
                    if (setting.getDiscordToken() != null) {
                        existingSetting.setDiscordToken(setting.getDiscordToken());
                    }
                    if (setting.getApiKey() != null) {
                        existingSetting.setApiKey(setting.getApiKey());
                    }

                    return existingSetting;
                })
                .map(settingRepository::save);
        } else if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.PAID)) {
            Setting currentSetting = settingRepository
                .findOneByUserIsCurrentUser(id, SecurityUtils.getCurrentUserLogin().orElseThrow())
                .orElseThrow();
            setting.login(currentSetting.getLogin());
            setting.total(currentSetting.getTotal());
            setting.membership(currentSetting.getMembership());
            result = settingRepository
                .findById(setting.getId())
                .map(existingSetting -> {
                    if (setting.getNotifyUri() != null) {
                        existingSetting.setNotifyUri(setting.getNotifyUri());
                    }
                    if (setting.getNotifyToken() != null) {
                        existingSetting.setNotifyToken(setting.getNotifyToken());
                    }
                    if (setting.getDiscordUsername() != null) {
                        existingSetting.setDiscordUsername(setting.getDiscordUsername());
                    }
                    if (setting.getDiscordId() != null) {
                        existingSetting.setDiscordId(setting.getDiscordId());
                    }
                    if (setting.getDiscordChannel() != null) {
                        existingSetting.setDiscordChannel(setting.getDiscordChannel());
                    }
                    if (setting.getDiscordToken() != null) {
                        existingSetting.setDiscordToken(setting.getDiscordToken());
                    }
                    if (setting.getApiKey() != null) {
                        existingSetting.setApiKey(setting.getApiKey());
                    }

                    return existingSetting;
                })
                .map(settingRepository::save);
        } else {
            Setting currentSetting = settingRepository
                .findOneByUserIsCurrentUser(id, SecurityUtils.getCurrentUserLogin().orElseThrow())
                .orElseThrow();
            setting.login(currentSetting.getLogin());
            setting.total(currentSetting.getTotal());
            setting.membership(currentSetting.getMembership());
            setting.notifyUri(currentSetting.getNotifyUri());
            setting.notifyToken(currentSetting.getNotifyToken());
            setting.discordToken(currentSetting.getDiscordToken());
            setting.apiKey(currentSetting.getApiKey());
            result = settingRepository
                .findById(setting.getId())
                .map(existingSetting -> {
                    if (setting.getDiscordUsername() != null) {
                        existingSetting.setDiscordUsername(setting.getDiscordUsername());
                    }
                    if (setting.getDiscordId() != null) {
                        existingSetting.setDiscordId(setting.getDiscordId());
                    }
                    if (setting.getDiscordChannel() != null) {
                        existingSetting.setDiscordChannel(setting.getDiscordChannel());
                    }

                    return existingSetting;
                })
                .map(settingRepository::save);
        }
        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, setting.getId()));
    }

    /**
     * {@code GET  /settings} : get all the settings.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of settings in body.
     */
    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<Setting>> getAllSettings(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Settings");
        Page<Setting> page;
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            page = settingRepository.findAll(pageable);
        } else {
            page = settingRepository.findAllByUserIsCurrentUser(pageable, SecurityUtils.getCurrentUserLogin().orElseThrow());
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /settings/:id} : get the "id" setting.
     *
     * @param id the id of the setting to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the setting, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Setting> getSetting(@PathVariable("id") String id) {
        log.debug("REST request to get Setting : {}", id);
        Optional<Setting> setting;
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            setting = settingRepository.findById(id);
        } else {
            setting = settingRepository.findOneByUserIsCurrentUser(id, SecurityUtils.getCurrentUserLogin().orElseThrow());
        }
        return ResponseUtil.wrapOrNotFound(setting);
    }

    /**
     * {@code DELETE  /settings/:id} : delete the "id" setting.
     *
     * @param id the id of the setting to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteSetting(@PathVariable("id") String id) {
        log.debug("REST request to delete Setting : {}", id);
        settingRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}

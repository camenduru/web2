package com.camenduru.web.web.rest;

import com.camenduru.web.domain.App;
import com.camenduru.web.repository.AppRepository;
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
 * REST controller for managing {@link com.camenduru.web.domain.App}.
 */
@RestController
@RequestMapping("/api/apps")
public class AppResource {

    private static final Logger log = LoggerFactory.getLogger(AppResource.class);

    private static final String ENTITY_NAME = "app";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppRepository appRepository;

    public AppResource(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    /**
     * {@code POST  /apps} : Create a new app.
     *
     * @param app the app to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new app, or with status {@code 400 (Bad Request)} if the app has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<App> createApp(@Valid @RequestBody App app) throws URISyntaxException {
        log.debug("REST request to save App : {}", app);
        if (app.getId() != null) {
            throw new BadRequestAlertException("A new app cannot already have an ID", ENTITY_NAME, "idexists");
        }
        app = appRepository.save(app);
        return ResponseEntity.created(new URI("/api/apps/" + app.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, app.getId()))
            .body(app);
    }

    /**
     * {@code PUT  /apps/:id} : Updates an existing app.
     *
     * @param id the id of the app to save.
     * @param app the app to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated app,
     * or with status {@code 400 (Bad Request)} if the app is not valid,
     * or with status {@code 500 (Internal Server Error)} if the app couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<App> updateApp(@PathVariable(value = "id", required = false) final String id, @Valid @RequestBody App app)
        throws URISyntaxException {
        log.debug("REST request to update App : {}, {}", id, app);
        if (app.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, app.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        app = appRepository.save(app);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, app.getId())).body(app);
    }

    /**
     * {@code PATCH  /apps/:id} : Partial updates given fields of an existing app, field will ignore if it is null
     *
     * @param id the id of the app to save.
     * @param app the app to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated app,
     * or with status {@code 400 (Bad Request)} if the app is not valid,
     * or with status {@code 404 (Not Found)} if the app is not found,
     * or with status {@code 500 (Internal Server Error)} if the app couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<App> partialUpdateApp(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody App app
    ) throws URISyntaxException {
        log.debug("REST request to partial update App partially : {}, {}", id, app);
        if (app.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, app.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<App> result = appRepository
            .findById(app.getId())
            .map(existingApp -> {
                if (app.getType() != null) {
                    existingApp.setType(app.getType());
                }
                if (app.getAmount() != null) {
                    existingApp.setAmount(app.getAmount());
                }
                if (app.getSchema() != null) {
                    existingApp.setSchema(app.getSchema());
                }
                if (app.getModel() != null) {
                    existingApp.setModel(app.getModel());
                }
                if (app.getTitle() != null) {
                    existingApp.setTitle(app.getTitle());
                }
                if (app.getIsDefault() != null) {
                    existingApp.setIsDefault(app.getIsDefault());
                }
                if (app.getIsActive() != null) {
                    existingApp.setIsActive(app.getIsActive());
                }
                if (app.getIsFree() != null) {
                    existingApp.setIsFree(app.getIsFree());
                }
                if (app.getCooldown() != null) {
                    existingApp.setCooldown(app.getCooldown());
                }

                return existingApp;
            })
            .map(appRepository::save);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, app.getId()));
    }

    /**
     * {@code GET  /apps} : get all the apps.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of apps in body.
     */
    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<App>> getAllApps(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Apps");
        Page<App> page = appRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /apps/:id} : get the "id" app.
     *
     * @param id the id of the app to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the app, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<App> getApp(@PathVariable("id") String id) {
        log.debug("REST request to get App : {}", id);
        Optional<App> app = appRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(app);
    }

    /**
     * {@code DELETE  /apps/:id} : delete the "id" app.
     *
     * @param id the id of the app to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteApp(@PathVariable("id") String id) {
        log.debug("REST request to delete App : {}", id);
        appRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}

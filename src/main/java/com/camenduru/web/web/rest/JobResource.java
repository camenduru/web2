package com.camenduru.web.web.rest;

import com.camenduru.web.domain.App;
import com.camenduru.web.domain.Authority;
import com.camenduru.web.domain.Job;
import com.camenduru.web.domain.Setting;
import com.camenduru.web.domain.User;
import com.camenduru.web.domain.enumeration.JobSource;
import com.camenduru.web.domain.enumeration.JobStatus;
import com.camenduru.web.repository.AppRepository;
import com.camenduru.web.repository.JobRepository;
import com.camenduru.web.repository.SettingRepository;
import com.camenduru.web.repository.UserRepository;
import com.camenduru.web.security.AuthoritiesConstants;
import com.camenduru.web.security.SecurityUtils;
import com.camenduru.web.web.rest.errors.BadRequestAlertException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.camenduru.web.domain.Job}.
 */
@RestController
@RequestMapping("/api/jobs")
public class JobResource {

    private static final Logger log = LoggerFactory.getLogger(JobResource.class);

    private static final String ENTITY_NAME = "job";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Value("${camenduru.web2.default.result}")
    private String camenduruWebResult;

    @Value("${camenduru.web2.default.result.suffix}")
    private String camenduruWebResultSuffix;

    @Value("${camenduru.web2.default.free.total}")
    private String camenduruWebFreeTotal;

    @Value("${camenduru.web2.default.paid.total}")
    private String camenduruWebPaidTotal;

    @Value("${camenduru.web2.default.min.total}")
    private String camenduruWebMinTotal;

    @Value("${camenduru.web2.s3.region}")
    private String camenduruWebS3Region;

    @Value("${camenduru.web2.s3.access}")
    private String camenduruWebS3Access;

    @Value("${camenduru.web2.s3.secret}")
    private String camenduruWebS3Secret;

    @Value("${camenduru.web2.s3.bucket}")
    private String camenduruWebS3Bucket;

    @Value("${camenduru.web2.s3.preview}")
    private String camenduruWebS3Preview;

    private final JobRepository jobRepository;
    private final SettingRepository settingRepository;
    private final UserRepository userRepository;
    private final AppRepository appRepository;
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private S3Client s3Client;

    public JobResource(
        JobRepository jobRepository,
        SettingRepository settingRepository,
        UserRepository userRepository,
        AppRepository appRepository,
        SimpMessageSendingOperations simpMessageSendingOperations
    ) {
        this.jobRepository = jobRepository;
        this.settingRepository = settingRepository;
        this.userRepository = userRepository;
        this.appRepository = appRepository;
        this.simpMessageSendingOperations = simpMessageSendingOperations;
    }

    @PostConstruct
    public void initializeS3Client() {
        this.s3Client = S3Client.builder()
            .region(Region.of(camenduruWebS3Region))
            .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(camenduruWebS3Access, camenduruWebS3Secret)))
            .build();
    }

    /**
     * {@code POST  /jobs} : Create a new job.
     *
     * @param job the job to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new job, or with status {@code 400 (Bad Request)} if the job has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Job> createJob(@Valid @RequestBody Job job) throws URISyntaxException {
        Setting setting = settingRepository.findAllByUserIsCurrentUser(SecurityUtils.getCurrentUserLogin().orElseThrow()).orElseThrow();
        int total = Integer.parseInt(setting.getTotal());
        App app = appRepository.findOneByType(job.getType()).orElseThrow();
        int amount = Integer.parseInt(app.getAmount());
        String destination = String.format("/notify/%s", setting.getLogin());
        int cooldown = Integer.parseInt(app.getCooldown());
        Date date = new Date(System.currentTimeMillis() - (cooldown * 1000));

        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            log.debug("REST request to save Job : {}", job);
            if (job.getId() != null) {
                throw new BadRequestAlertException("A new job cannot already have an ID", ENTITY_NAME, "idexists");
            }
            job = jobRepository.save(job);
            return ResponseEntity.created(new URI("/api/jobs/" + job.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, job.getId()))
                .body(job);
        } else if (total >= amount) {
            if (
                jobRepository.findAllByUserNonExpiredJobsNewerThanTheDate(SecurityUtils.getCurrentUserLogin().orElseThrow(), date).size() >
                0
            ) {
                String result = String.format("Oops! Cooldown is %s seconds.", cooldown);
                String payload = String.format("%s", result);
                simpMessageSendingOperations.convertAndSend(destination, payload);
                // throw new BadRequestAlertException("User in cooldown state.", ENTITY_NAME, "Cooldown State");
                return ResponseEntity.ok().body(null);
            } else {
                log.debug("REST request to save Job : {}", job);
                if (job.getId() != null) {
                    throw new BadRequestAlertException("A new job cannot already have an ID", ENTITY_NAME, "idexists");
                }
                User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().orElseThrow()).orElseThrow();
                if (
                    (!user.getAuthorities().contains(new Authority().name("ROLE_PAID")) && app.getIsFree()) ||
                    user.getAuthorities().contains(new Authority().name("ROLE_PAID"))
                ) {
                    int width = 512;
                    int height = 512;
                    String jsonString = job.getCommand();
                    try {
                        JsonElement jsonElement = JsonParser.parseString(jsonString);
                        if (jsonElement.isJsonObject()) {
                            JsonObject jsonObject = jsonElement.getAsJsonObject();
                            if (jsonObject.has("input_image_files")) {
                                JsonArray input_images = jsonObject.getAsJsonArray("input_image_files");
                                if (input_images.size() == 0) {
                                    simpMessageSendingOperations.convertAndSend(destination, "Oops! No input image files were found.");
                                    return ResponseEntity.ok().body(null);
                                }
                                for (JsonElement input_element : input_images) {
                                    JsonObject image_object = input_element.getAsJsonObject();
                                    JsonObject input_image = image_object.getAsJsonObject("url");
                                    String base64Data = input_image.get("data").getAsString();
                                    byte[] imageBytes = Base64.getDecoder().decode(base64Data);
                                    String filename = input_image.get("filename").getAsString();
                                    String fileType = filename.substring(filename.lastIndexOf('.') + 1);
                                    String uniqueFilename = UUID.randomUUID().toString() + "." + fileType;
                                    try (ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes)) {
                                        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                            .bucket(camenduruWebS3Bucket)
                                            .key(uniqueFilename)
                                            .build();
                                        String url = String.format("%s/%s/%s", camenduruWebS3Preview, camenduruWebS3Bucket, uniqueFilename);
                                        s3Client.putObject(
                                            putObjectRequest,
                                            software.amazon.awssdk.core.sync.RequestBody.fromBytes(imageBytes)
                                        );
                                        image_object.addProperty("url", url);
                                    } catch (IOException e) {
                                        input_image.addProperty("url", "null");
                                        e.printStackTrace();
                                    }
                                }
                                jsonObject.add("input_image_files", input_images);
                                job.setCommand(jsonObject.toString());
                            }
                            if (jsonObject.has("input_image_file")) {
                                JsonObject input_image = jsonObject.get("input_image_file").getAsJsonObject();
                                if (
                                    !input_image.has("data") ||
                                    !input_image.has("filename") ||
                                    input_image.get("data").getAsString().isEmpty()
                                ) {
                                    simpMessageSendingOperations.convertAndSend(destination, "Oops! No input image file was found.");
                                    return ResponseEntity.ok().body(null);
                                }
                                String base64Data = input_image.get("data").getAsString();
                                byte[] imageBytes = Base64.getDecoder().decode(base64Data);
                                String filename = input_image.get("filename").getAsString();
                                String fileType = filename.substring(filename.lastIndexOf('.') + 1);
                                String uniqueFilename = UUID.randomUUID().toString() + "." + fileType;
                                try (ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes)) {
                                    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                        .bucket(camenduruWebS3Bucket)
                                        .key(uniqueFilename)
                                        .build();
                                    String url = String.format("%s/%s/%s", camenduruWebS3Preview, camenduruWebS3Bucket, uniqueFilename);
                                    s3Client.putObject(
                                        putObjectRequest,
                                        software.amazon.awssdk.core.sync.RequestBody.fromBytes(imageBytes)
                                    );
                                    jsonObject.addProperty("input_image_file", url);
                                } catch (IOException e) {
                                    jsonObject.addProperty("input_image_file", "null");
                                    e.printStackTrace();
                                }
                                job.setCommand(jsonObject.toString());
                            }
                            if (jsonObject.has("input_image_check")) {
                                if (jsonObject.has("width") && jsonObject.has("height")) {
                                    width = jsonObject.get("width").getAsInt();
                                    height = jsonObject.get("height").getAsInt();
                                } else {
                                    String input_image = jsonObject.get("input_image_check").getAsString();
                                    URL image_url;
                                    BufferedImage image;
                                    try {
                                        image_url = new URL(input_image);
                                        image = ImageIO.read(image_url);
                                        width = image.getWidth();
                                        height = image.getHeight();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                for (String key : jsonObject.keySet()) {
                                    if (key.startsWith("width")) {
                                        width = jsonObject.get(key).getAsInt();
                                    }
                                    if (key.startsWith("height")) {
                                        height = jsonObject.get(key).getAsInt();
                                    }
                                }
                            }
                        }
                    } catch (JsonSyntaxException e) {
                        System.err.println("Invalid JSON syntax: " + e.getMessage());
                    }
                    job.setResult(camenduruWebResult + width + "x" + height + camenduruWebResultSuffix);
                    job.setType(app.getType());
                    job.setAmount(app.getAmount());
                    job.setNotifyUri(setting.getNotifyUri());
                    job.setNotifyToken(setting.getNotifyToken());
                    job.setDiscordUsername(setting.getDiscordUsername());
                    job.setDiscordId(setting.getDiscordId());
                    job.setDiscordChannel(setting.getDiscordChannel());
                    job.setDiscordToken(setting.getDiscordToken());
                    job.setDate(Instant.now());
                    job.setStatus(JobStatus.WAITING);
                    job.setLogin(SecurityUtils.getCurrentUserLogin().orElseThrow());
                    job.setSource(JobSource.WEB);
                    job.setTotal(setting.getTotal());
                    job = jobRepository.save(job);
                    return ResponseEntity.created(new URI("/api/jobs/" + job.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, job.getId()))
                        .body(job);
                } else {
                    throw new BadRequestAlertException("User authority and job authority mismatch.", ENTITY_NAME, "Invalid Authority");
                }
            }
        } else {
            String result = String.format(
                """
                    Oops! Your balance is insufficient. If you want a daily wallet balance of
                    <span class='text-info' style='font-weight: bold;'>%s</span> ($%s/month), please subscribe via
                    <a class='text-info' style='font-weight: bold;' href='https://github.com/sponsors/camenduru'>GitHub Sponsors</a>,
                    <a class='text-info' style='font-weight: bold;' href='https://www.patreon.com/camenduru'>Patreon</a>, or purchase a
                    <a class='text-info' style='font-weight: bold;' href='https://buymeacoffee.com/camenduru/extras'>Tost Wallet Code</a>.
                """,
                camenduruWebPaidTotal,
                camenduruWebMinTotal
            );
            String payload = String.format("%s", result);
            simpMessageSendingOperations.convertAndSend(destination, payload);
            // throw new BadRequestAlertException("User balance is insufficient.", ENTITY_NAME, "Insufficient Balance");
            return ResponseEntity.ok().body(null);
        }
    }

    /**
     * {@code PUT  /jobs/:id} : Updates an existing job.
     *
     * @param id the id of the job to save.
     * @param job the job to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated job,
     * or with status {@code 400 (Bad Request)} if the job is not valid,
     * or with status {@code 500 (Internal Server Error)} if the job couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Job> updateJob(@PathVariable(value = "id", required = false) final String id, @Valid @RequestBody Job job)
        throws URISyntaxException {
        log.debug("REST request to update Job : {}, {}", id, job);
        if (job.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, job.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        job = jobRepository.save(job);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, job.getId())).body(job);
    }

    /**
     * {@code PATCH  /jobs/:id} : Partial updates given fields of an existing job, field will ignore if it is null
     *
     * @param id the id of the job to save.
     * @param job the job to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated job,
     * or with status {@code 400 (Bad Request)} if the job is not valid,
     * or with status {@code 404 (Not Found)} if the job is not found,
     * or with status {@code 500 (Internal Server Error)} if the job couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Job> partialUpdateJob(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody Job job
    ) throws URISyntaxException {
        log.debug("REST request to partial update Job partially : {}, {}", id, job);
        if (job.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, job.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Job> result = jobRepository
            .findById(job.getId())
            .map(existingJob -> {
                if (job.getLogin() != null) {
                    existingJob.setLogin(job.getLogin());
                }
                if (job.getDate() != null) {
                    existingJob.setDate(job.getDate());
                }
                if (job.getStatus() != null) {
                    existingJob.setStatus(job.getStatus());
                }
                if (job.getType() != null) {
                    existingJob.setType(job.getType());
                }
                if (job.getCommand() != null) {
                    existingJob.setCommand(job.getCommand());
                }
                if (job.getAmount() != null) {
                    existingJob.setAmount(job.getAmount());
                }
                if (job.getNotifyUri() != null) {
                    existingJob.setNotifyUri(job.getNotifyUri());
                }
                if (job.getNotifyToken() != null) {
                    existingJob.setNotifyToken(job.getNotifyToken());
                }
                if (job.getDiscordUsername() != null) {
                    existingJob.setDiscordUsername(job.getDiscordUsername());
                }
                if (job.getDiscordId() != null) {
                    existingJob.setDiscordId(job.getDiscordId());
                }
                if (job.getDiscordChannel() != null) {
                    existingJob.setDiscordChannel(job.getDiscordChannel());
                }
                if (job.getDiscordToken() != null) {
                    existingJob.setDiscordToken(job.getDiscordToken());
                }
                if (job.getSource() != null) {
                    existingJob.setSource(job.getSource());
                }
                if (job.getTotal() != null) {
                    existingJob.setTotal(job.getTotal());
                }
                if (job.getResult() != null) {
                    existingJob.setResult(job.getResult());
                }

                return existingJob;
            })
            .map(jobRepository::save);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, job.getId()));
    }

    /**
     * {@code GET  /jobs} : get all the jobs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jobs in body.
     */
    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<Job>> getAllJobs(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Jobs");
        Page<Job> page;
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            page = jobRepository.findAll(pageable);
        } else {
            page = jobRepository.findAllByUserIsCurrentUser(pageable, SecurityUtils.getCurrentUserLogin().orElseThrow());
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /jobs/type} : get all the jobs by type.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jobs in body.
     */
    @GetMapping("/type/{type}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<Job>> getAllJobsByType(
        @PathVariable("type") String type,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of Jobs");
        Page<Job> page;
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            page = jobRepository.findAllByType(pageable, type);
        } else {
            page = jobRepository.findAllByTypeByUserIsCurrentUser(pageable, SecurityUtils.getCurrentUserLogin().orElseThrow(), type);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /jobs/:id} : get the "id" job.
     *
     * @param id the id of the job to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the job, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Job> getJob(@PathVariable("id") String id) {
        log.debug("REST request to get Job : {}", id);
        Optional<Job> job = jobRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(job);
    }

    /**
     * {@code DELETE  /jobs/:id} : delete the "id" job.
     *
     * @param id the id of the job to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteJob(@PathVariable("id") String id) {
        log.debug("REST request to delete Job : {}", id);
        jobRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}

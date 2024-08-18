package com.camenduru.web.web.rest;

import com.camenduru.web.domain.App;
import com.camenduru.web.domain.Job;
import com.camenduru.web.domain.Redeem;
import com.camenduru.web.domain.Setting;
import com.camenduru.web.domain.User;
import com.camenduru.web.domain.enumeration.JobSource;
import com.camenduru.web.domain.enumeration.JobStatus;
import com.camenduru.web.domain.enumeration.RedeemStatus;
import com.camenduru.web.repository.AppRepository;
import com.camenduru.web.repository.JobRepository;
import com.camenduru.web.repository.RedeemRepository;
import com.camenduru.web.repository.SettingRepository;
import com.camenduru.web.repository.UserRepository;
import com.camenduru.web.security.SecurityUtils;
import com.camenduru.web.service.EmailServiceNotAllowedException;
import com.camenduru.web.service.MailService;
import com.camenduru.web.service.UserService;
import com.camenduru.web.service.chat.ChatRequestBody;
import com.camenduru.web.service.dto.AdminUserDTO;
import com.camenduru.web.service.dto.PasswordChangeDTO;
import com.camenduru.web.service.notify.NotifyRequestBody;
import com.camenduru.web.web.rest.errors.*;
import com.camenduru.web.web.rest.vm.KeyAndPasswordVM;
import com.camenduru.web.web.rest.vm.ManagedUserVM;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private static class AccountResourceException extends RuntimeException {

        private AccountResourceException(String message) {
            super(message);
        }
    }

    private static final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final UserRepository userRepository;

    private final UserService userService;

    private final MailService mailService;

    private final SettingRepository settingRepository;
    private final JobRepository jobRepository;
    private final RedeemRepository redeemRepository;
    private final AppRepository appRepository;
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @Value("${camenduru.web2.token}")
    private String webToken;

    @Value("${camenduru.web2.default.result}")
    private String camenduruWebResult;

    @Value("${camenduru.web2.default.result.suffix}")
    private String camenduruWebResultSuffix;

    public AccountResource(
        UserRepository userRepository,
        UserService userService,
        MailService mailService,
        SettingRepository settingRepository,
        JobRepository jobRepository,
        RedeemRepository redeemRepository,
        AppRepository appRepository,
        SimpMessageSendingOperations simpMessageSendingOperations
    ) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
        this.settingRepository = settingRepository;
        this.jobRepository = jobRepository;
        this.redeemRepository = redeemRepository;
        this.appRepository = appRepository;
        this.simpMessageSendingOperations = simpMessageSendingOperations;
    }

    /**
     * {@code POST  /register} : register the user.
     *
     * @param managedUserVM the managed user View Model.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already used.
     * @throws EmailServiceNotAllowedException {@code 400 (Bad Request)} if the email service is not allowed.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
        if (isPasswordLengthInvalid(managedUserVM.getPassword())) {
            throw new InvalidPasswordException();
        }
        User user = userService.registerUser(managedUserVM, managedUserVM.getPassword());
        mailService.sendActivationEmail(user);
    }

    /**
     * {@code GET  /activate} : activate the registered user.
     *
     * @param key the activation key.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be activated.
     */
    @GetMapping("/activate")
    public void activateAccount(@RequestParam(value = "key") String key) {
        Optional<User> user = userService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this activation key");
        }
    }

    /**
     * {@code POST  /notify} : notify the registered user.
     *
     * @param key the activation key.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be activated.
     */
    @PostMapping("/notify")
    public ResponseEntity<String> notifyAccount(
        @RequestBody NotifyRequestBody notify,
        @RequestHeader(value = "Authorization", required = true) String token
    ) {
        if (token.equals(webToken)) {
            String result = notify.getResult();
            String jobId = notify.getJobId();
            String status = notify.getStatus();
            Job job = jobRepository.findById(jobId).orElseThrow();
            String login = job.getLogin();
            if (status.equals("DONE")) {
                job.setStatus(JobStatus.DONE);
                job.setResult(result);
                Setting setting = settingRepository.findAllByUserIsCurrentUser(login).orElseThrow();
                int total = Integer.parseInt(setting.getTotal()) - Integer.parseInt(job.getAmount());
                job.setTotal(Integer.toString(total));
                setting.setTotal(Integer.toString(total));
                jobRepository.save(job);
                settingRepository.save(setting);
                if (job.getType().startsWith("chat")) {
                    String destination = String.format("/chat/%s", login);
                    StringBuilder content = new StringBuilder();
                    try {
                        URL url = new URL(result);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            content.append(line);
                            content.append(System.lineSeparator());
                        }
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String payload = String.format("%s", content.toString());
                    simpMessageSendingOperations.convertAndSend(destination, payload);
                } else {
                    String destination = String.format("/notify/%s", login);
                    String payload = String.format("%s", "DONE");
                    simpMessageSendingOperations.convertAndSend(destination, payload);
                }
            } else {
                job.setStatus(JobStatus.FAILED);
                jobRepository.save(job);
                String destination = String.format("/notify/%s", login);
                String payload = String.format("%s", "FAILED");
                simpMessageSendingOperations.convertAndSend(destination, payload);
            }
            return new ResponseEntity<String>("✔ Valid", HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("❌ Invalid", HttpStatus.OK);
        }
    }

    /**
     * {@code GET  /home} : get all the home user's jobs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jobs in body.
     */
    @GetMapping("/home")
    public ResponseEntity<List<Job>> geHomeJobs(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Jobs");
        Page<Job> page = jobRepository.findAllByUserIsCurrentUser(pageable, "home");
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code POST  /chat} : chat the registered user.
     *
     * @param key the activation key.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be activated.
     */
    @PostMapping(value = "/chat")
    public ResponseEntity<String> chatAccount(@RequestBody ChatRequestBody chat) {
        String login = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AccountResourceException("Current user login not found"));
        Setting setting = settingRepository.findAllByUserIsCurrentUser(SecurityUtils.getCurrentUserLogin().orElseThrow()).orElseThrow();
        JsonObject jsonModel = JsonParser.parseString(chat.getModel()).getAsJsonObject();
        App app = appRepository.findOneByType(jsonModel.get("model").getAsString()).orElseThrow();

        JsonObject combinedJsonChat = new JsonObject();
        try {
            JsonObject jsonChat = JsonParser.parseString(new ObjectMapper().writeValueAsString(chat)).getAsJsonObject();
            JsonArray jsonChatMessages = jsonChat.get("messages").getAsJsonArray();
            JsonObject jsonChatModel = JsonParser.parseString(jsonChat.get("model").getAsString()).getAsJsonObject();
            combinedJsonChat.add("messages", jsonChatMessages);
            combinedJsonChat.add("model", jsonChatModel);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Job job = new Job();
        job.setDate(Instant.now());
        job.setStatus(JobStatus.WAITING);
        job.setLogin(login);
        job.setSource(JobSource.WEB);
        job.setNotifyUri(setting.getNotifyUri());
        job.setNotifyToken(setting.getNotifyToken());
        job.setDiscordUsername(setting.getDiscordUsername());
        job.setDiscordId(setting.getDiscordId());
        job.setDiscordChannel(setting.getDiscordChannel());
        job.setDiscordToken(setting.getDiscordToken());
        job.setTotal(setting.getTotal());
        job.setType(app.getType());
        job.setAmount(app.getAmount());
        // job.setResult(combinedJsonChat.get("messages").toString());
        job.setResult(camenduruWebResult + "512x512" + camenduruWebResultSuffix);
        job.setCommand(combinedJsonChat.toString());
        job = jobRepository.save(job);

        String destination = String.format("/notify/%s", login);
        String payload = String.format("%s", "DONE");
        simpMessageSendingOperations.convertAndSend(destination, payload);

        return new ResponseEntity<String>("✔ Valid", HttpStatus.OK);
    }

    /**
     * {@code GET  /code} : get the "redeem" and "login" for redeem.
     *
     * @param code the code of the redeem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the redeem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/code")
    public ResponseEntity<String> useRedeem(@RequestParam(value = "redeem") String code) {
        log.debug("REST request to get Redeem : {}", code);
        Redeem redeem = redeemRepository.findOneByCode(code);
        String login = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AccountResourceException("Current user login not found"));
        User user = userRepository.findOneByLogin(login).orElseThrow();
        if (redeem != null && user != null && redeem.getStatus() == RedeemStatus.WAITING) {
            Setting setting = settingRepository.findAllByUserIsCurrentUser(login).orElseThrow();
            int total = Integer.parseInt(setting.getTotal()) + Integer.parseInt(redeem.getAmount());
            setting.setTotal(Integer.toString(total));
            settingRepository.save(setting);
            redeem.setLogin(user.getLogin());
            redeem.setStatus(RedeemStatus.USED);
            redeemRepository.save(redeem);
            return new ResponseEntity<String>("✔ Code Redeemed", HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("❌ Code Invalid", HttpStatus.OK);
        }
    }

    /**
     * {@code POST  /v1} : API v1.
     *
     * @param key the activation key.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be activated.
     */
    @PostMapping("/v1")
    public ResponseEntity<String> apiV1(
        @RequestBody String api_v1,
        @RequestHeader(value = "Authorization", required = true) String api_key
    ) {
        Setting setting = settingRepository.findOneByApiKey(api_key).orElseThrow();
        JsonObject jsonObject = JsonParser.parseString(api_v1).getAsJsonObject();
        App app = appRepository.findOneByType(jsonObject.get("app").getAsString()).orElseThrow();
        JsonObject command = jsonObject.getAsJsonObject("command");
        int cooldown = Integer.parseInt(app.getCooldown());
        int amount = Integer.parseInt(app.getAmount());
        int total = Integer.parseInt(setting.getTotal());
        Date date = new Date(System.currentTimeMillis() - (cooldown * 1000));

        if (total >= amount) {
            if (jobRepository.findAllByUserNonExpiredJobsNewerThanTheDate(setting.getLogin(), date).size() > 0) {
                String message = String.format("Oops! Cooldown is %s seconds.", cooldown);
                JsonObject jsonResponse = new JsonObject();
                jsonResponse.addProperty("message", message);
                jsonResponse.addProperty("status", "COOLDOWN");
                return new ResponseEntity<String>(jsonResponse.toString(), HttpStatus.OK);
            } else {
                Job job = new Job();
                job.setDate(Instant.now());
                job.setStatus(JobStatus.WAITING);
                job.setLogin(setting.getLogin());
                job.setSource(JobSource.API);
                job.setNotifyUri(setting.getNotifyUri());
                job.setNotifyToken(setting.getNotifyToken());
                job.setDiscordUsername(setting.getDiscordUsername());
                job.setDiscordId(setting.getDiscordId());
                job.setDiscordChannel(setting.getDiscordChannel());
                job.setDiscordToken(setting.getDiscordToken());
                job.setTotal(setting.getTotal());
                job.setType(app.getType());
                job.setAmount(app.getAmount());
                job.setResult(camenduruWebResult + "512x512" + camenduruWebResultSuffix);
                job.setCommand(command.toString());
                job = jobRepository.save(job);

                JsonObject jsonResponse = new JsonObject();
                jsonResponse.addProperty("jobId", job.getId());
                jsonResponse.addProperty("status", "QUEUED");
                return new ResponseEntity<String>(jsonResponse.toString(), HttpStatus.OK);
            }
        } else {
            String message = "Oops! Your balance is insufficient.";
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("message", message);
            jsonResponse.addProperty("status", "BALANCE");
            return new ResponseEntity<String>(jsonResponse.toString(), HttpStatus.OK);
        }
    }

    /**
     * {@code GET  /account} : get the current user.
     *
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @GetMapping("/account")
    public AdminUserDTO getAccount() {
        return userService
            .getUserWithAuthorities()
            .map(AdminUserDTO::new)
            .orElseThrow(() -> new AccountResourceException("User could not be found"));
    }

    /**
     * {@code POST  /account} : update the current user information.
     *
     * @param userDTO the current user information.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user login wasn't found.
     */
    @PostMapping("/account")
    public void saveAccount(@Valid @RequestBody AdminUserDTO userDTO) {
        String userLogin = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new AccountResourceException("Current user login not found"));
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.orElseThrow().getLogin().equalsIgnoreCase(userLogin))) {
            throw new EmailAlreadyUsedException();
        }
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        if (!user.isPresent()) {
            throw new AccountResourceException("User could not be found");
        }
        userService.updateUser(
            userDTO.getFirstName(),
            userDTO.getLastName(),
            userDTO.getEmail(),
            userDTO.getLangKey(),
            userDTO.getImageUrl()
        );
    }

    /**
     * {@code POST  /account/change-password} : changes the current user's password.
     *
     * @param passwordChangeDto current and new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the new password is incorrect.
     */
    @PostMapping(path = "/account/change-password")
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        if (isPasswordLengthInvalid(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
    }

    /**
     * {@code POST   /account/reset-password/init} : Send an email to reset the password of the user.
     *
     * @param mail the mail of the user.
     */
    @PostMapping(path = "/account/reset-password/init")
    public void requestPasswordReset(@RequestBody String mail) {
        Optional<User> user = userService.requestPasswordReset(mail);
        if (user.isPresent()) {
            mailService.sendPasswordResetMail(user.orElseThrow());
        } else {
            // Pretend the request has been successful to prevent checking which emails really exist
            // but log that an invalid attempt has been made
            log.warn("Password reset requested for non existing mail");
        }
    }

    /**
     * {@code POST   /account/reset-password/finish} : Finish to reset the password of the user.
     *
     * @param keyAndPassword the generated key and the new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the password could not be reset.
     */
    @PostMapping(path = "/account/reset-password/finish")
    public void finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
        if (isPasswordLengthInvalid(keyAndPassword.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        Optional<User> user = userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this reset key");
        }
    }

    private static boolean isPasswordLengthInvalid(String password) {
        return (
            StringUtils.isEmpty(password) ||
            password.length() < ManagedUserVM.PASSWORD_MIN_LENGTH ||
            password.length() > ManagedUserVM.PASSWORD_MAX_LENGTH
        );
    }
}

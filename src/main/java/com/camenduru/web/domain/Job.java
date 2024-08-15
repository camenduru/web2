package com.camenduru.web.domain;

import com.camenduru.web.domain.enumeration.JobSource;
import com.camenduru.web.domain.enumeration.JobStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Job.
 */
@Document(collection = "job")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Job implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("login")
    private String login;

    @NotNull
    @Field("date")
    private Instant date;

    @NotNull
    @Field("status")
    private JobStatus status;

    @NotNull
    @Field("type")
    private String type;

    @NotNull
    @Field("command")
    private String command;

    @NotNull
    @Field("amount")
    private String amount;

    @NotNull
    @Field("notify_uri")
    private String notifyUri;

    @NotNull
    @Field("notify_token")
    private String notifyToken;

    @NotNull
    @Field("discord_username")
    private String discordUsername;

    @NotNull
    @Field("discord_id")
    private String discordId;

    @NotNull
    @Field("discord_channel")
    private String discordChannel;

    @NotNull
    @Field("discord_token")
    private String discordToken;

    @NotNull
    @Field("source")
    private JobSource source;

    @NotNull
    @Field("result")
    private String result;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Job id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return this.login;
    }

    public Job login(String login) {
        this.setLogin(login);
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Instant getDate() {
        return this.date;
    }

    public Job date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public JobStatus getStatus() {
        return this.status;
    }

    public Job status(JobStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public String getType() {
        return this.type;
    }

    public Job type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCommand() {
        return this.command;
    }

    public Job command(String command) {
        this.setCommand(command);
        return this;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getAmount() {
        return this.amount;
    }

    public Job amount(String amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNotifyUri() {
        return this.notifyUri;
    }

    public Job notifyUri(String notifyUri) {
        this.setNotifyUri(notifyUri);
        return this;
    }

    public void setNotifyUri(String notifyUri) {
        this.notifyUri = notifyUri;
    }

    public String getNotifyToken() {
        return this.notifyToken;
    }

    public Job notifyToken(String notifyToken) {
        this.setNotifyToken(notifyToken);
        return this;
    }

    public void setNotifyToken(String notifyToken) {
        this.notifyToken = notifyToken;
    }

    public String getDiscordUsername() {
        return this.discordUsername;
    }

    public Job discordUsername(String discordUsername) {
        this.setDiscordUsername(discordUsername);
        return this;
    }

    public void setDiscordUsername(String discordUsername) {
        this.discordUsername = discordUsername;
    }

    public String getDiscordId() {
        return this.discordId;
    }

    public Job discordId(String discordId) {
        this.setDiscordId(discordId);
        return this;
    }

    public void setDiscordId(String discordId) {
        this.discordId = discordId;
    }

    public String getDiscordChannel() {
        return this.discordChannel;
    }

    public Job discordChannel(String discordChannel) {
        this.setDiscordChannel(discordChannel);
        return this;
    }

    public void setDiscordChannel(String discordChannel) {
        this.discordChannel = discordChannel;
    }

    public String getDiscordToken() {
        return this.discordToken;
    }

    public Job discordToken(String discordToken) {
        this.setDiscordToken(discordToken);
        return this;
    }

    public void setDiscordToken(String discordToken) {
        this.discordToken = discordToken;
    }

    public JobSource getSource() {
        return this.source;
    }

    public Job source(JobSource source) {
        this.setSource(source);
        return this;
    }

    public void setSource(JobSource source) {
        this.source = source;
    }

    public String getResult() {
        return this.result;
    }

    public Job result(String result) {
        this.setResult(result);
        return this;
    }

    public void setResult(String result) {
        this.result = result;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Job)) {
            return false;
        }
        return getId() != null && getId().equals(((Job) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Job{" +
            "id=" + getId() +
            ", login='" + getLogin() + "'" +
            ", date='" + getDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", type='" + getType() + "'" +
            ", command='" + getCommand() + "'" +
            ", amount='" + getAmount() + "'" +
            ", notifyUri='" + getNotifyUri() + "'" +
            ", notifyToken='" + getNotifyToken() + "'" +
            ", discordUsername='" + getDiscordUsername() + "'" +
            ", discordId='" + getDiscordId() + "'" +
            ", discordChannel='" + getDiscordChannel() + "'" +
            ", discordToken='" + getDiscordToken() + "'" +
            ", source='" + getSource() + "'" +
            ", result='" + getResult() + "'" +
            "}";
    }
}

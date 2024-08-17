package com.camenduru.web.domain;

import com.camenduru.web.domain.enumeration.Membership;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Setting.
 */
@Document(collection = "setting")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Setting implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("login")
    private String login;

    @NotNull
    @Field("total")
    private String total;

    @NotNull
    @Field("membership")
    private Membership membership;

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
    @Field("api_key")
    private String apiKey;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Setting id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return this.login;
    }

    public Setting login(String login) {
        this.setLogin(login);
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getTotal() {
        return this.total;
    }

    public Setting total(String total) {
        this.setTotal(total);
        return this;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Membership getMembership() {
        return this.membership;
    }

    public Setting membership(Membership membership) {
        this.setMembership(membership);
        return this;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }

    public String getNotifyUri() {
        return this.notifyUri;
    }

    public Setting notifyUri(String notifyUri) {
        this.setNotifyUri(notifyUri);
        return this;
    }

    public void setNotifyUri(String notifyUri) {
        this.notifyUri = notifyUri;
    }

    public String getNotifyToken() {
        return this.notifyToken;
    }

    public Setting notifyToken(String notifyToken) {
        this.setNotifyToken(notifyToken);
        return this;
    }

    public void setNotifyToken(String notifyToken) {
        this.notifyToken = notifyToken;
    }

    public String getDiscordUsername() {
        return this.discordUsername;
    }

    public Setting discordUsername(String discordUsername) {
        this.setDiscordUsername(discordUsername);
        return this;
    }

    public void setDiscordUsername(String discordUsername) {
        this.discordUsername = discordUsername;
    }

    public String getDiscordId() {
        return this.discordId;
    }

    public Setting discordId(String discordId) {
        this.setDiscordId(discordId);
        return this;
    }

    public void setDiscordId(String discordId) {
        this.discordId = discordId;
    }

    public String getDiscordChannel() {
        return this.discordChannel;
    }

    public Setting discordChannel(String discordChannel) {
        this.setDiscordChannel(discordChannel);
        return this;
    }

    public void setDiscordChannel(String discordChannel) {
        this.discordChannel = discordChannel;
    }

    public String getDiscordToken() {
        return this.discordToken;
    }

    public Setting discordToken(String discordToken) {
        this.setDiscordToken(discordToken);
        return this;
    }

    public void setDiscordToken(String discordToken) {
        this.discordToken = discordToken;
    }

    public String getApiKey() {
        return this.apiKey;
    }

    public Setting apiKey(String apiKey) {
        this.setApiKey(apiKey);
        return this;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Setting)) {
            return false;
        }
        return getId() != null && getId().equals(((Setting) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Setting{" +
            "id=" + getId() +
            ", login='" + getLogin() + "'" +
            ", total='" + getTotal() + "'" +
            ", membership='" + getMembership() + "'" +
            ", notifyUri='" + getNotifyUri() + "'" +
            ", notifyToken='" + getNotifyToken() + "'" +
            ", discordUsername='" + getDiscordUsername() + "'" +
            ", discordId='" + getDiscordId() + "'" +
            ", discordChannel='" + getDiscordChannel() + "'" +
            ", discordToken='" + getDiscordToken() + "'" +
            ", apiKey='" + getApiKey() + "'" +
            "}";
    }
}

package com.camenduru.web.domain;

import java.util.UUID;

public class SettingTestSamples {

    public static Setting getSettingSample1() {
        return new Setting()
            .id("id1")
            .login("login1")
            .total("total1")
            .notifyUri("notifyUri1")
            .notifyToken("notifyToken1")
            .discordUsername("discordUsername1")
            .discordId("discordId1")
            .discordChannel("discordChannel1")
            .discordToken("discordToken1");
    }

    public static Setting getSettingSample2() {
        return new Setting()
            .id("id2")
            .login("login2")
            .total("total2")
            .notifyUri("notifyUri2")
            .notifyToken("notifyToken2")
            .discordUsername("discordUsername2")
            .discordId("discordId2")
            .discordChannel("discordChannel2")
            .discordToken("discordToken2");
    }

    public static Setting getSettingRandomSampleGenerator() {
        return new Setting()
            .id(UUID.randomUUID().toString())
            .login(UUID.randomUUID().toString())
            .total(UUID.randomUUID().toString())
            .notifyUri(UUID.randomUUID().toString())
            .notifyToken(UUID.randomUUID().toString())
            .discordUsername(UUID.randomUUID().toString())
            .discordId(UUID.randomUUID().toString())
            .discordChannel(UUID.randomUUID().toString())
            .discordToken(UUID.randomUUID().toString());
    }
}

package com.camenduru.web.domain;

import java.util.UUID;

public class JobTestSamples {

    public static Job getJobSample1() {
        return new Job()
            .id("id1")
            .login("login1")
            .type("type1")
            .command("command1")
            .amount("amount1")
            .notifyUri("notifyUri1")
            .notifyToken("notifyToken1")
            .discordUsername("discordUsername1")
            .discordId("discordId1")
            .discordChannel("discordChannel1")
            .discordToken("discordToken1")
            .total("total1")
            .result("result1");
    }

    public static Job getJobSample2() {
        return new Job()
            .id("id2")
            .login("login2")
            .type("type2")
            .command("command2")
            .amount("amount2")
            .notifyUri("notifyUri2")
            .notifyToken("notifyToken2")
            .discordUsername("discordUsername2")
            .discordId("discordId2")
            .discordChannel("discordChannel2")
            .discordToken("discordToken2")
            .total("total2")
            .result("result2");
    }

    public static Job getJobRandomSampleGenerator() {
        return new Job()
            .id(UUID.randomUUID().toString())
            .login(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .command(UUID.randomUUID().toString())
            .amount(UUID.randomUUID().toString())
            .notifyUri(UUID.randomUUID().toString())
            .notifyToken(UUID.randomUUID().toString())
            .discordUsername(UUID.randomUUID().toString())
            .discordId(UUID.randomUUID().toString())
            .discordChannel(UUID.randomUUID().toString())
            .discordToken(UUID.randomUUID().toString())
            .total(UUID.randomUUID().toString())
            .result(UUID.randomUUID().toString());
    }
}

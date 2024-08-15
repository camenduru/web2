package com.camenduru.web.domain;

import java.util.UUID;

public class AppTestSamples {

    public static App getAppSample1() {
        return new App().id("id1").type("type1").amount("amount1").schema("schema1").model("model1").title("title1");
    }

    public static App getAppSample2() {
        return new App().id("id2").type("type2").amount("amount2").schema("schema2").model("model2").title("title2");
    }

    public static App getAppRandomSampleGenerator() {
        return new App()
            .id(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .amount(UUID.randomUUID().toString())
            .schema(UUID.randomUUID().toString())
            .model(UUID.randomUUID().toString())
            .title(UUID.randomUUID().toString());
    }
}

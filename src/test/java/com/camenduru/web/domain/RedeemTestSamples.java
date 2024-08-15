package com.camenduru.web.domain;

import java.util.UUID;

public class RedeemTestSamples {

    public static Redeem getRedeemSample1() {
        return new Redeem().id("id1").login("login1").type("type1").author("author1").amount("amount1").code("code1");
    }

    public static Redeem getRedeemSample2() {
        return new Redeem().id("id2").login("login2").type("type2").author("author2").amount("amount2").code("code2");
    }

    public static Redeem getRedeemRandomSampleGenerator() {
        return new Redeem()
            .id(UUID.randomUUID().toString())
            .login(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .author(UUID.randomUUID().toString())
            .amount(UUID.randomUUID().toString())
            .code(UUID.randomUUID().toString());
    }
}

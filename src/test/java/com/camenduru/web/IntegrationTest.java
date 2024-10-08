package com.camenduru.web;

import com.camenduru.web.config.AsyncSyncConfiguration;
import com.camenduru.web.config.EmbeddedMongo;
import com.camenduru.web.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { Web2App.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedMongo
public @interface IntegrationTest {
}

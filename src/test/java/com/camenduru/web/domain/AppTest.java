package com.camenduru.web.domain;

import static com.camenduru.web.domain.AppTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.camenduru.web.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(App.class);
        App app1 = getAppSample1();
        App app2 = new App();
        assertThat(app1).isNotEqualTo(app2);

        app2.setId(app1.getId());
        assertThat(app1).isEqualTo(app2);

        app2 = getAppSample2();
        assertThat(app1).isNotEqualTo(app2);
    }
}

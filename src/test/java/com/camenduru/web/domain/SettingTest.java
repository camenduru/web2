package com.camenduru.web.domain;

import static com.camenduru.web.domain.SettingTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.camenduru.web.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SettingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Setting.class);
        Setting setting1 = getSettingSample1();
        Setting setting2 = new Setting();
        assertThat(setting1).isNotEqualTo(setting2);

        setting2.setId(setting1.getId());
        assertThat(setting1).isEqualTo(setting2);

        setting2 = getSettingSample2();
        assertThat(setting1).isNotEqualTo(setting2);
    }
}

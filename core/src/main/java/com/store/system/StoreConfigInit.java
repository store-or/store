package com.store.system;

import com.core.system.InitialException;
import com.core.system.Initialize;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 *
 */
@Component
public class StoreConfigInit implements Initialize {
    @Value("${version}")
    private String version;

    @Override
    public void init() throws InitialException {
        StoreConfig.gVersion = StringUtils.substringBefore(version, "-");
    }
}


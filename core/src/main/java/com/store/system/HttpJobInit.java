package com.store.system;

import com.core.http.HttpJob;
import com.core.system.InitialException;
import com.core.system.Initialize;
import org.springframework.stereotype.Component;

/**
 * Created by laizy on 2016/5/5.
 */
@Component
public class HttpJobInit implements Initialize {
    @Override
    public void init() throws InitialException {
        HttpJob.setDefaultMaxPerRoute(30);
        HttpJob.setMaxTotal(100);
    }
}

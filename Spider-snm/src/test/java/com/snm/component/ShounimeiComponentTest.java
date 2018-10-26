package com.snm.component;

import com.snm.BaseJunit4Test;
import org.junit.Test;

import javax.annotation.Resource;

public class ShounimeiComponentTest extends BaseJunit4Test {

    @Resource
    ShounimeiComponent shounimeiComponent;

    @Test
    public void testLogin() throws Exception {
        shounimeiComponent.login();
    }

    @Test
    public void testVerifyCaptcha() throws Exception {
        shounimeiComponent.verifyCaptcha("http://pt.aipt123.org/image.php?action=regimage&imagehash=4cc96647641e39193e77a3f7254f25db");
    }

    @Test
    public void testDownloadTorrent() throws Exception {
        if (shounimeiComponent.login())
            shounimeiComponent.downloadTorrent("148066");
    }
}
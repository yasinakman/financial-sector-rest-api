package com.akman.springbootdemo.rest_api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@PrepareForTest(SpringBootDemoApplication.class)
public class SpringBootDemoApplicationTests {
    @LocalServerPort
    int randomServerPort;

    @Test
    public void testPackageMarker() {
        new PackageMarker();
        new com.akman.springbootdemo.PackageMarker();
    }
    @InjectMocks
    private SpringBootDemoApplication springBootDemoApplication;

    private SpringApplicationBuilder application = new SpringApplicationBuilder();

    @Test
    public void testApplication() {
        springBootDemoApplication.configure(application);
    }
}
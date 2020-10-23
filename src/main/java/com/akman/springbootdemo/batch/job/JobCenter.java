package com.akman.springbootdemo.batch.job;

import com.akman.springbootdemo.batch.step.StepUpdateRatings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@EnableAutoConfiguration
@Slf4j
public class JobCenter {

    private final StepUpdateRatings stepUpdateRatings;

    @Bean
    public Job callUpdateRatingsJob(JobBuilderFactory jobBuilderFactory) {
        log.info("rating update job has started.");
        return jobBuilderFactory.get("callUpdateRatingsJob").start(stepUpdateRatings).build();
    }
}
package com.akman.springbootdemo.batch.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@RequiredArgsConstructor
@EnableScheduling
@Component
@Slf4j
public class BatchScheduler {

    @Value("${batch.updateRatingsJob.isEnabled}")
    private Boolean isUpdateRatingsJobEnabled;

    private final JobLauncher jobLauncher;

    private final Job callUpdateRatingsJob;

    @Scheduled(initialDelay = 60 * 1000, fixedRate = 1000 * 60 /* * 60*/) //optional// cron = "${batch.updateRatingsJob.cronEveryDayAtTenAm}")
    public void runUpdateRatingsJob() throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
            JobRestartException, JobInstanceAlreadyCompleteException {
        if (isUpdateRatingsJobEnabled) {
            log.info("rating update job scheduled");jobLauncher.run(callUpdateRatingsJob, new JobParametersBuilder()
                    .addDate("date", new Date()).toJobParameters());
        }
    }
}
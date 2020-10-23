package com.akman.springbootdemo.batch.step;

import com.akman.springbootdemo.service.creditservice.CreditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@EnableAutoConfiguration
@Slf4j
public class StepUpdateRatings implements Step {

    private final CreditService creditService;

    @Override
    public String getName() {
        return "stepUpdateRatings";
    }

    @Override
    public boolean isAllowStartIfComplete() {
        return false;
    }

    @Override
    public int getStartLimit() {
        return 1;
    }

    @Override
    public void execute(StepExecution stepExecution) {
        log.info("rating update step has started");
        creditService.updateCreditsAndCustomerRatingsPerJob();
    }
}
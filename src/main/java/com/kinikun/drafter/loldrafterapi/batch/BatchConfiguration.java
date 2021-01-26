package com.kinikun.drafter.loldrafterapi.batch;

import com.kinikun.drafter.loldrafterapi.batch.listener.JobCompletionListener;
import com.kinikun.drafter.loldrafterapi.batch.processor.MatchProcessor;
import com.kinikun.drafter.loldrafterapi.batch.reader.MatchReader;
import com.kinikun.drafter.loldrafterapi.batch.writer.MatchWriter;
import com.kinikun.drafter.loldrafterapi.dto.MatchDto;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job processJob;

    @Bean
    public Job processJob() {
        return jobBuilderFactory.get("processJob").incrementer(new RunIdIncrementer()).listener(listener())
                .flow(orderStep1()).end().build();
    }

    @Bean
    public Step orderStep1() {
        return stepBuilderFactory.get("orderStep1").<MatchDto, MatchDto>chunk(1).reader(new MatchReader())
                .processor(new MatchProcessor()).writer(new MatchWriter()).build();
    }

    @Bean
    public JobExecutionListener listener() {
        return new JobCompletionListener();
    }

    @Scheduled(cron = "0 0 0 ? * *")
    public void launchMatchBatch() throws JobExecutionAlreadyRunningException, JobRestartException,
            JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(processJob, jobParameters);
    }
}

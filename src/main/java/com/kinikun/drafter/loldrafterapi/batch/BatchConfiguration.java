package com.kinikun.drafter.loldrafterapi.batch;

import java.util.List;

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
import org.springframework.batch.item.ItemReader;
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
        return stepBuilderFactory.get("orderStep1").<List<String>, MatchDto>chunk(1).reader(itemReader())
                .processor(new MatchProcessor()).writer(new MatchWriter()).build();
    }

    @Bean
    public JobExecutionListener listener() {
        return new JobCompletionListener();
    }

    @Bean
    public ItemReader<List<String>> itemReader() {
        return new MatchReader();
    }

    @Scheduled(cron = "0 0 0 ? * *")
    public void launchMatchBatch() throws JobExecutionAlreadyRunningException, JobRestartException,
            JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(processJob, jobParameters);
    }
}

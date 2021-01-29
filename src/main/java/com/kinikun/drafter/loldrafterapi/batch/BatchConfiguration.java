package com.kinikun.drafter.loldrafterapi.batch;

import java.util.HashMap;
import java.util.Map;

import com.kinikun.drafter.loldrafterapi.batch.listener.JobCompletionListener;
import com.kinikun.drafter.loldrafterapi.batch.processor.MatchProcessor;
import com.kinikun.drafter.loldrafterapi.batch.tasklet.MatchPurgeTasklet;
import com.kinikun.drafter.loldrafterapi.batch.writer.MatchWriter;
import com.kinikun.drafter.loldrafterapi.dto.MatchDto;
import com.kinikun.drafter.loldrafterapi.entity.PlayerEntity;
import com.kinikun.drafter.loldrafterapi.repository.PlayerRepository;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableBatchProcessing
@EnableScheduling
public class BatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job processJob;

    @Bean
    public Job processJob(PlayerRepository playerRepository) {
        return jobBuilderFactory.get("processJob").incrementer(new RunIdIncrementer()).listener(listener())
                .flow(purgeMatchesStep()).next(getMatchStep(playerRepository)).end().build();
    }

    @Bean
    public Step purgeMatchesStep() {
        return stepBuilderFactory.get("myStep").tasklet(new MatchPurgeTasklet()).build();
    }

    @Bean
    public Step getMatchStep(PlayerRepository playerRepository) {
        return stepBuilderFactory.get("getMatchStep").<PlayerEntity, MatchDto>chunk(1)
                .reader(itemReader(playerRepository)).processor(new MatchProcessor(playerRepository)).writer(new MatchWriter()).build();
    }

    @Bean
    public JobExecutionListener listener() {
        return new JobCompletionListener();
    }

    @Bean
    public ItemReader<PlayerEntity> itemReader(PlayerRepository playerRepository) {
        Map<String, Direction> sorts = new HashMap<>();
        sorts.put("id", Direction.ASC);

        RepositoryItemReader<PlayerEntity> reader = new RepositoryItemReader<>();
        reader.setRepository(playerRepository);
        reader.setMethodName("findAll");
        reader.setSort(sorts);

        return reader;
    }

    @Scheduled(cron = "0 0 0 1/1 * ?")
    public void launchMatchBatch() throws JobExecutionAlreadyRunningException, JobRestartException,
            JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(processJob, jobParameters);
    }
}

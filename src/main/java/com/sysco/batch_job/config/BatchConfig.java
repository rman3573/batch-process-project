package com.sysco.batch_job.config;

import com.sysco.batch_job.dto.PersonDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("importData", jobRepository)
                .<PersonDTO, String>chunk(10, transactionManager)
                .reader(itemReader())
                .processor(processor())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public Step step2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("importData2", jobRepository)
                .<PersonDTO, String>chunk(10, transactionManager)
                .reader(itemReader())
                .processor(processor2())
                .writer(itemWriter2())
                .build();
    }

    @Bean
    public Job job(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("importDataJob", jobRepository)
                .start(step(jobRepository, transactionManager))
                .next(step2(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public FlatFileItemReader<PersonDTO> itemReader() {
        FlatFileItemReader<PersonDTO> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource("src/main/resources/datasource.csv"));
        reader.setName("itemReader");
        reader.setLinesToSkip(1);
        reader.setLineMapper(lineMapper());
        return reader;
    }

    @Bean
    public PersonProcessor processor() {
        return new PersonProcessor();
    }

    @Bean
    public PersonProcessor2 processor2() {
        return new PersonProcessor2();
    }

    @Bean
    public FlatFileItemWriter<String> itemWriter() {
        FlatFileItemWriter<String> itemWriter = new FlatFileItemWriter<>();

        itemWriter.setResource(new FileSystemResource("src/main/resources/datasource.txt"));
        itemWriter.setName("itemWriter");
        itemWriter.setLineAggregator(getAggregator());
        return itemWriter;
    }

    @Bean
    public FlatFileItemWriter<String> itemWriter2() {
        FlatFileItemWriter<String> itemWriter = new FlatFileItemWriter<>();

        itemWriter.setResource(new FileSystemResource("src/main/resources/datasource1.txt"));
        itemWriter.setName("itemWriter1");
        itemWriter.setLineAggregator(getAggregator());
        return itemWriter;
    }

    private LineAggregator<String> getAggregator() {
        return new PersonAggregator();
    }

    private LineMapper<PersonDTO> lineMapper() {
        DefaultLineMapper<PersonDTO> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("name", "age");

        BeanWrapperFieldSetMapper<PersonDTO> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(PersonDTO.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }
}

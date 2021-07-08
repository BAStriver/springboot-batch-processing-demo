package com.bas.processing.config;

import com.bas.processing.entity.Person;
import com.bas.processing.processor.JobCompletionNotificationListener;
import com.bas.processing.processor.PersonProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    PersonProcessor processor;

    @Bean
    public FlatFileItemReader<Person> reader() {
        return new FlatFileItemReaderBuilder<Person>().name("personItemReader")
                .resource(new ClassPathResource("sample-data.csv")).delimited()
                .names(new String[]{"firstName", "lastName"})
                .recordSeparatorPolicy(new RecordSeparatorPolicy()) // 实现跳过空行
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {
                    {
                        setTargetType(Person.class);
                    }
                }).build();
    }

    @Bean
    public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Person>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)").dataSource(dataSource)
                .build();
    }

    @Bean
    public FlatFileItemWriter<Person> writer2() {
        //Create writer instance
        FlatFileItemWriter<Person> writer = new FlatFileItemWriter<>();
        //Set output file location
        writer.setResource(new FileSystemResource("output/outputData.csv"));
        //All job repetitions should "append" to same output file
        writer.setAppendAllowed(true);

        //Name field values sequence based on object properties
        writer.setLineAggregator(new DelimitedLineAggregator<Person>() {
            {
                setDelimiter(",");
                setFieldExtractor(new BeanWrapperFieldExtractor<Person>() {
                    {
                        setNames(new String[]{"firstName", "lastName"});
                    }
                });
            }
        });
        return writer;
    }

    @Bean
    public Step step1(FlatFileItemWriter<Person> writer2) {
        return stepBuilderFactory.get("step1")
                // 定义一次要写入多少数据,这里每个批次允许处理10条数据
                .<Person, Person>chunk(10)
                // 读取CSV
                .reader(reader())
                // 处理业务逻辑
                .processor(processor)
                // 写入数据库
                .writer(writer2).build();
    }

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
        JobBuilder builder = jobBuilderFactory.get("importUserJob");
        Job job = builder.incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(step1)
                .build();

        return job;
    }

}

package com.bas.processing.processor;

import com.bas.processing.entity.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class PersonProcessor implements ItemProcessor<Person, Person> {

    private static final Logger log = LoggerFactory.getLogger(PersonProcessor.class);

    JobParameters jobParameters;

    @BeforeStep
    public void beforeStep(final StepExecution stepExecution) {
        jobParameters = stepExecution.getJobParameters();
        log.info("jobParameters: {}", jobParameters);
    }

    @AfterStep
    public void afterStep() {
        log.info("afterStep finished.");
    }

    @Override
    public Person process(Person sourcePerson) throws Exception {

        final String firstName = sourcePerson.getFirstName().toUpperCase();
        final String lastName = sourcePerson.getLastName().toUpperCase();

        final Person transformedPerson = new Person(firstName, lastName);

        log.info("Converting (" + sourcePerson + ") into (" + transformedPerson + ")");

        return transformedPerson;
    }

}
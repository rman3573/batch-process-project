package com.sysco.batch_job.config;

import com.sysco.batch_job.dto.PersonDTO;
import org.springframework.batch.item.ItemProcessor;

public class PersonProcessor implements ItemProcessor<PersonDTO, String> {
    @Override
    public String process(PersonDTO personDTO) throws Exception {
        return "Person name is " + personDTO.getName() + " and age is " +
                personDTO.getAge() + " - " + System.currentTimeMillis();
    }
}

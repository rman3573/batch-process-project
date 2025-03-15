package com.sysco.batch_job.config;

import com.sysco.batch_job.dto.PersonDTO;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class PersonProcessor2 implements ItemProcessor<PersonDTO, String> {
    @Override
    public String process(PersonDTO personDTO) throws Exception {
        return "Hello " + personDTO.getName() + " your age is " +
                personDTO.getAge() + " - " + System.currentTimeMillis();
    }
}

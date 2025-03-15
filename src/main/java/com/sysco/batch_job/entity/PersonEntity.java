package com.sysco.batch_job.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "person")
@AllArgsConstructor
@NoArgsConstructor
public class PersonEntity {
    @Id
    private long id;

    private String name;
}

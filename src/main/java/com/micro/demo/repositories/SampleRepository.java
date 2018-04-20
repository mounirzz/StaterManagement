package com.micro.demo.repositories;

import org.springframework.data.repository.CrudRepository;

import com.micro.demo.models.Sample;

public interface SampleRepository extends CrudRepository<Sample, Long>{

}

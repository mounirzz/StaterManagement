package com.micro.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.micro.demo.models.Sample;
import com.micro.demo.repositories.SampleRepository;

@Service
public class SampleService extends CrudService<Sample, SampleRepository> {

	@Autowired
	@Override
	public void setRepo(SampleRepository repo) {
		this.repo = repo;
		
	}

	@Override
	public Sample copy(Sample from, Sample to) {
		to =from ;
		return to ;
	}

}

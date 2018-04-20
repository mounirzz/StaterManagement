package com.micro.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.micro.demo.models.Sample;
import com.micro.demo.services.SampleService;
@RestController
@RequestMapping("/sample")
public class SampleController extends CrudController<Sample, SampleService> {

	@Autowired
	@Override
	public void setService(SampleService service) {
		// TODO Auto-generated method stub
		this.service = service;
	}

	@Override
	public boolean isAuthorized(Long entityId, SampleService service) {
		return true;
	}

}

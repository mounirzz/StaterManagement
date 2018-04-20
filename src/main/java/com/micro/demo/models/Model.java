package com.micro.demo.models;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Version;

@MappedSuperclass
public abstract class Model {
	@GeneratedValue
	@Id
	private Long id;

	private Date dateCreated;

	@Version
	private Timestamp dateModified;

	@PrePersist
	void createdAt() {
		this.setDateCreated(new Date());
	}

	public Long getId() {
		return id;
	}

	public Date gerDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Timestamp getDateModified() {
		return dateModified;
	}

	public void setDateModified(Timestamp dateModified) {
		this.dateModified = dateModified;
	}

}

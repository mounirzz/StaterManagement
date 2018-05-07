package com.micro.demo.services;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.micro.demo.models.Model;
public abstract class CrudService<M extends Model, R extends CrudRepository<M, Long>> {
	R repo;

	public abstract void setRepo(R repo);

	/**
	 * Define the parameters that you want to save to the DB when calling the update() method
	 * @param model source object
	 * @param updated DB object that gets saves "return to" in this method
	 * @return
	 */
	public abstract M copy(M model, M updated);

	public Iterable<M> getAll() {
		return this.repo.findAll();
	}

	/**
	 * Mainly used to create a new entity
	 * however, can also be used to save something without using the update() method.
	 * @param model
	 * @return saved entity model
	 */
	public M save(M model) {
		return this.repo.save(model);
	}

	public M get(Long id) {
		return this.repo.findOne(id);
	}

	public M update(M model,Long id) {
		M updated = this.repo.findOne(id);
		updated = copy(model, updated);
		return this.repo.save(updated);
	}

	
	public boolean delete(Long id) {
		this.repo.delete(id);
		return true;
	}

}

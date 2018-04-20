package com.micro.demo.services;

import org.springframework.data.repository.CrudRepository;

import com.micro.demo.models.Model;

public abstract class CrudService<M extends Model, R extends CrudRepository<M, Long>> {
	R repo;

	public abstract void setRepo(R repo);

	/**
	 * 
	 */
	public abstract M copy(M from, M to);

	public Iterable<M> getAll() {
		return this.repo.findAll();
	}

	/**
	 * 
	 */
	public M save(M model) {
		return this.repo.save(model);
	}

	public M get(Long id) {
		return this.repo.findOne(id);
	}

	public M update(M model) {
		M updated = this.repo.findOne(model.getId());
		updated = copy(model, updated);
		return this.repo.save(updated);
	}

	public boolean delete(Long id) {
		this.repo.delete(id);
		return true;
	}

}

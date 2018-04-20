package com.micro.demo.controllers;

import org.springframework.data.repository.CrudRepository;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.micro.demo.models.Model;
import com.micro.demo.services.CrudService;

import net.bytebuddy.asm.Advice.Return;

@RestController
public abstract class CrudController<M extends Model, S extends CrudService<M, ? extends CrudRepository<M,Long>>>  {
		S service ;
		public abstract void setService(S service);
		public abstract boolean isAuthorized(Long entityId, S service);
		
		@RequestMapping(value="/create" , method = RequestMethod.GET)// For testing 
		public M create(M object) {
			if(isAuthorized(object.getId(), service)) {
					return service.save(object);
		}
        logUnauthorizedAccess();
		return null ;
}
		@RequestMapping(value="/update", method = RequestMethod.POST)
		public M update(M object) {
			if(isAuthorized(object.getId(),service)) {
				return service.update(object);
			}
			logUnauthorizedAccess();
			return null ;
		}
	    @RequestMapping(value="/delete", method = RequestMethod.POST)
	    public Boolean delete(Long id) {
	        if(isAuthorized(id, service)) {
	            return service.delete(id);
	        }
	        logUnauthorizedAccess();
	        return null;
	    }
	    
	    @RequestMapping(value="/get", method = RequestMethod.GET)
	    public @ResponseBody M get(Long id) {
	        if(isAuthorized(id, service)) {
	            return service.get(id);
	        }
	        logUnauthorizedAccess();
	        return null;
	    }
	    
	    
	    @RequestMapping(value="/json", method = RequestMethod.GET)
	    public @ResponseBody Iterable<M> json(ModelMap map) {
	        return service.getAll();
	    }
	    
	    private void logUnauthorizedAccess() {
	        System.out.println("!!UN-AUTHORIZED ACCESS DETECTED!!");
	    }
	}

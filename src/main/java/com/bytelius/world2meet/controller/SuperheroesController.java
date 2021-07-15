package com.bytelius.world2meet.controller;

import com.bytelius.world2meet.common.GenericResponse;
import com.bytelius.world2meet.common.URLConstant;
import com.bytelius.world2meet.data.model.input.SuperheroeDAO;
import com.bytelius.world2meet.service.interfaces.SuperheroeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.core.Response;

@RestController
@RequestMapping(value = "${spring.data.rest.base-path}" + URLConstant.SUPERHEROE)
@CrossOrigin(origins = "*")
public class SuperheroesController {

	@Autowired
	private SuperheroeService service;

	@GetMapping(path = URLConstant.GET + URLConstant.ALL, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody GenericResponse getAllSuperheroes() { return service.getAllSuperheroes(); }

	@GetMapping(path = URLConstant.GET + URLConstant.ONE + URLConstant.ID_VARIABLE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody GenericResponse getOneSuperheroeByID(@Valid @PathVariable Integer id) {
		if(id == null || id < 0) {
			return new GenericResponse(Response.Status.BAD_REQUEST.getStatusCode(), Response.Status.BAD_REQUEST.getReasonPhrase());
		}
		return service.getSuperheroByID(id);
	}

	@GetMapping(path = URLConstant.GET + URLConstant.ONE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody GenericResponse getSuperheroesByNameValue(@Valid @RequestParam String param) {
		if(param == null || param.isEmpty()) {
			return new GenericResponse(Response.Status.BAD_REQUEST.getStatusCode(), Response.Status.BAD_REQUEST.getReasonPhrase());
		}
		return service.getSuperheroesByNameValue(param);
	}

	@PutMapping(path = URLConstant.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody GenericResponse putExample(@Valid @RequestBody SuperheroeDAO data) {
		if(data == null || StringUtils.isAllEmpty(data.getName()) || StringUtils.isAllEmpty(data.getPower())) {
			return new GenericResponse(Response.Status.BAD_REQUEST.getStatusCode(), Response.Status.BAD_REQUEST.getReasonPhrase() + " - missing information.");
		}
		return service.updateSuperhero(data);
	}
}

package com.bytelius.world2meet.service;

import com.bytelius.world2meet.common.GenericResponse;
import com.bytelius.world2meet.data.entity.SuperheroeEntity;
import com.bytelius.world2meet.data.mapper.SuperheroeMapper;
import com.bytelius.world2meet.data.model.input.SuperheroeDAO;
import com.bytelius.world2meet.data.model.output.SuperheroeDTO;
import com.bytelius.world2meet.repository.SuperheroesRepository;
import com.bytelius.world2meet.service.interfaces.SuperheroeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response.Status;
import java.util.ArrayList;

@Service
public class SuperheroeServiceImpl implements SuperheroeService {

	@Autowired
	private SuperheroesRepository repository;

	@Autowired
	private SuperheroeMapper mapper;

	@Override
	public GenericResponse getAllSuperheroes() {
		GenericResponse response;

		ArrayList<SuperheroeEntity> entities = repository.findAll();

		if(entities == null || entities.size() == 0) {
			return new GenericResponse(Status.NOT_FOUND.getStatusCode(), Status.NOT_FOUND.getReasonPhrase() + " - There is no superheros in database");
		}

		ArrayList<SuperheroeDTO> results = mapper.arrayListSuperheroeEntityToArrayListSuperheroDTO(entities);

		int status = Status.OK.getStatusCode();
		String message = Status.OK.getReasonPhrase() + " - Superheroes found it.";

		response = new GenericResponse(status, message, results);

		return response;
	}

	@Override
	public GenericResponse getSuperheroByID(Integer id) {
		GenericResponse response;

		SuperheroeEntity entity = repository.findSuperheroeEntityById(id);

		if(entity == null) {
			return new GenericResponse(Status.NOT_FOUND.getStatusCode(), Status.NOT_FOUND.getReasonPhrase() + " - Incorrect id. Superhero not found.");
		}

		SuperheroeDTO result = mapper.superheroeEntityToSuperheroDTO(entity);

		int status = Status.OK.getStatusCode();
		String message = Status.OK.getReasonPhrase() + " - Superhero found.";
		response = new GenericResponse(status, message, result);

		return response;
	}

	@Override
	public GenericResponse getSuperheroesByNameValue(String param) {
		GenericResponse response;

		ArrayList<SuperheroeEntity> entities = repository.findSuperheroeEntitiesByNameContainingIgnoreCase(param);

		if(entities == null || entities.size() == 0) {
			return new GenericResponse(Status.NOT_FOUND.getStatusCode(), Status.NOT_FOUND.getReasonPhrase() + " - There is no superheros with this parameters.");
		}

		ArrayList<SuperheroeDTO> results = mapper.arrayListSuperheroeEntityToArrayListSuperheroDTO(entities);

		int status = Status.OK.getStatusCode();
		String message = Status.OK.getReasonPhrase() + " - Superheroes found it.";

		response = new GenericResponse(status, message, results);

		return response;
	}

	@Override
	public GenericResponse updateSuperhero(SuperheroeDAO data) {
		GenericResponse response;

		SuperheroeEntity entity = repository.findSuperheroeEntityById(data.getId());

		//Check if data is correct
		if (entity == null) {
			return new GenericResponse(Status.NOT_FOUND.getStatusCode(), Status.NOT_FOUND.getReasonPhrase() + " - Incorrect id. Superhero not found.");
		}
		entity.setName(data.getName());
		entity.setPower(data.getPower());

		repository.saveAndFlush(entity);

		SuperheroeDTO result = mapper.superheroeEntityToSuperheroDTO(entity);

		int status = Status.CREATED.getStatusCode();
		String message = Status.CREATED.getReasonPhrase() + " - Example user updated.";
		response = new GenericResponse(status, message, result);

		return response;
	}

	@Override
	public GenericResponse deleteSuperhero(Integer id) {
		GenericResponse response;

		SuperheroeEntity entity = repository.findSuperheroeEntityById(id);

		if(entity == null) {
			return new GenericResponse(Status.NOT_FOUND.getStatusCode(), Status.NOT_FOUND.getReasonPhrase() + " - Incorrect id. Superhero not found.");
		}

		repository.deleteById(id);

		int status = Status.OK.getStatusCode();
		String message = Status.OK.getReasonPhrase() + " - Superhero deleted.";
		response = new GenericResponse(status, message, true);

		return response;
	}
}

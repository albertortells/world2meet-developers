package com.bytelius.world2meet.service;

import com.bytelius.world2meet.common.GenericResponse;
import com.bytelius.world2meet.data.entity.SuperheroeEntity;
import com.bytelius.world2meet.data.mapper.SuperheroeMapper;
import com.bytelius.world2meet.data.model.output.SuperheroeDTO;
import com.bytelius.world2meet.repository.SuperheroesRepository;
import com.bytelius.world2meet.service.interfaces.SuperheroeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response.Status;
import java.util.ArrayList;
import java.util.Optional;

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

		if(entities == null) {
			return new GenericResponse(Status.NOT_FOUND.getStatusCode(), Status.NOT_FOUND.getReasonPhrase() + " - There is no superheros in database");
		}

		ArrayList<SuperheroeDTO> results = mapper.arrayListSuperheroeEntityToArrayListSuperheroDTO(entities);

		int status = Status.OK.getStatusCode();
		String message = Status.OK.getReasonPhrase() + " - Superheroes found it.";

		response = new GenericResponse(status, message, results);

		return response;
	}

	@Override
	public GenericResponse getSuperheroeByID(Integer id) {
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

		if(entities == null) {
			return new GenericResponse(Status.NOT_FOUND.getStatusCode(), Status.NOT_FOUND.getReasonPhrase() + " - There is no superheros with this parameters.");
		}

		ArrayList<SuperheroeDTO> results = mapper.arrayListSuperheroeEntityToArrayListSuperheroDTO(entities);

		int status = Status.OK.getStatusCode();
		String message = Status.OK.getReasonPhrase() + " - Superheroes found it.";

		response = new GenericResponse(status, message, results);

		return response;
	}
}

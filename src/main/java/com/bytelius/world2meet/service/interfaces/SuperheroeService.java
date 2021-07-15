package com.bytelius.world2meet.service.interfaces;

import com.bytelius.world2meet.common.GenericResponse;
import com.bytelius.world2meet.data.model.input.SuperheroeDAO;

public interface SuperheroeService {

	GenericResponse getAllSuperheroes();

	GenericResponse getSuperheroByID(Integer id);

	GenericResponse getSuperheroesByNameValue(String param);

	GenericResponse updateSuperhero(SuperheroeDAO data);

	GenericResponse deleteSuperhero(Integer id);
}

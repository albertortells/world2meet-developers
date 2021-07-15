package com.bytelius.world2meet.service.interfaces;

import com.bytelius.world2meet.common.GenericResponse;

public interface SuperheroeService {

	GenericResponse getAllSuperheroes();

	GenericResponse getSuperheroeByID(Integer id);

	GenericResponse getSuperheroesByNameValue(String param);
}

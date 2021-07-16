package com.bytelius.world2meet.service;

import com.bytelius.world2meet.common.GenericResponse;
import com.bytelius.world2meet.data.entity.SuperheroeEntity;
import com.bytelius.world2meet.data.model.input.SuperheroeDAO;
import com.bytelius.world2meet.data.model.output.SuperheroeDTO;
import com.bytelius.world2meet.repository.SuperheroesRepository;
import com.bytelius.world2meet.service.interfaces.SuperheroeService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
class SuperheroeServiceImplTest {

	@Autowired
	private SuperheroeService service;

	@Mock
	private SuperheroesRepository repository;

	@Test
	void getAllSuperheroes_isOK_thenReturnStatus200() {
		//Given

		//When
		doReturn(new ArrayList<SuperheroeEntity>()).when(repository).findAll();
		GenericResponse response = service.getAllSuperheroes();

		//Then
		assertEquals(200, response.getStatus());
	}

	/**
	 * Este test se debe probar comentando las lines de insercion de datos a la bbdd
	 * dado que sino, como va a buscar los datos en la H2 en memoria, siempre encuentra
	 * datos y por tanto, no da nunca la opción a una bbdd vacía.
	 */
	@Test
	@Disabled
	void getAllSuperheroes_isNOK_thenReturnStatus404() {
		//Given

		//When
		doReturn(new ArrayList<SuperheroeEntity>()).when(repository).findAll();
		GenericResponse response = service.getAllSuperheroes();

		//Then
		assertEquals(404, response.getStatus());
	}

	@Test
	void getSuperheroByID_isOK_thenReturnStatus200() {
		//Given
		int id = 2;

		//When
		doReturn(new SuperheroeEntity()).when(repository).findSuperheroeEntityById(id);
		GenericResponse response = service.getSuperheroByID(id);

		//Then
		SuperheroeDTO superheroeDTO = (SuperheroeDTO) response.getData();

		assertEquals(200, response.getStatus());
		assertEquals("Batman", superheroeDTO.getName());
	}

	@Test
	void getSuperheroByID_isNOK_thenReturnStatus404() {
		//Given
		int id = 123;

		//When
		doReturn(new SuperheroeEntity()).when(repository).findSuperheroeEntityById(id);
		GenericResponse response = service.getSuperheroByID(id);

		//Then
		assertEquals(404, response.getStatus());
	}

	@Test
	void getSuperheroesByNameValue_isOK_thenReturnStatus200() {
		//Given
		String str = "man";

		//When
		doReturn(new ArrayList<SuperheroeEntity>()).when(repository).findSuperheroeEntitiesByNameContainingIgnoreCase(str);
		GenericResponse response = service.getSuperheroesByNameValue(str);

		//Then
		assertEquals(200, response.getStatus());
	}

	@Test
	void getSuperheroesByNameValue_isNOK_thenReturnStatus404() {
		//Given
		String str = "dnf";

		//When
		doReturn(new ArrayList<SuperheroeEntity>()).when(repository).findSuperheroeEntitiesByNameContainingIgnoreCase(str);
		GenericResponse response = service.getSuperheroesByNameValue(str);

		//Then
		assertEquals(404, response.getStatus());
	}

	@Test
	void updateSuperhero_isOK_thenReturnStatus201() {
		//Given
		SuperheroeDAO dao = new SuperheroeDAO();
		dao.setId(1);
		dao.setName("Goku");
		dao.setPower("God");

		//When
		doReturn(new SuperheroeEntity()).when(repository).findSuperheroeEntityById(dao.getId());
		doReturn(null).when(repository).save(any());
		GenericResponse response = service.updateSuperhero(dao);

		//Then
		assertEquals(201, response.getStatus());
	}

	@Test
	void updateSuperhero_isNOK_thenReturnStatus404() {
		//Given
		SuperheroeDAO dao = new SuperheroeDAO();
		dao.setId(11);
		dao.setName("Goku");
		dao.setPower("God");

		//When
		doReturn(new SuperheroeEntity()).when(repository).findSuperheroeEntityById(dao.getId());
		doReturn(null).when(repository).save(any());
		GenericResponse response = service.updateSuperhero(dao);

		//Then
		assertEquals(404, response.getStatus());
	}

	@Test
	void deleteSuperhero_isOK_thenReturnStatus200() {
		//Given

		//When
		doNothing().when(repository).deleteById(any());
		GenericResponse response = service.deleteSuperhero(4);

		//Then
		assertEquals(200, response.getStatus());
	}

	@Test
	void deleteSuperhero_isNOK_thenReturnStatus404() {
		//Given

		//When
		doNothing().when(repository).deleteById(any());
		GenericResponse response = service.deleteSuperhero(11);

		//Then
		assertEquals(404, response.getStatus());
	}
}
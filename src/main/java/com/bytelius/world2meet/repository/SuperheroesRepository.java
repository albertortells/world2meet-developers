package com.bytelius.world2meet.repository;

import com.bytelius.world2meet.data.entity.SuperheroeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface SuperheroesRepository extends JpaRepository<SuperheroeEntity, Integer> {

	ArrayList<SuperheroeEntity> findAll();

	SuperheroeEntity findSuperheroeEntityById(Integer id);

	ArrayList<SuperheroeEntity> findSuperheroeEntitiesByNameContainingIgnoreCase(String param);
}

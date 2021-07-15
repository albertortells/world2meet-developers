package com.bytelius.world2meet.data.mapper;

import com.bytelius.world2meet.data.entity.SuperheroeEntity;
import com.bytelius.world2meet.data.model.output.SuperheroeDTO;
import org.mapstruct.Mapper;

import java.util.ArrayList;

@Mapper(componentModel = "spring")
public interface SuperheroeMapper {

	SuperheroeDTO superheroeEntityToSuperheroDTO(SuperheroeEntity entity);

	ArrayList<SuperheroeDTO> arrayListSuperheroeEntityToArrayListSuperheroDTO(ArrayList<SuperheroeEntity> entity);
}

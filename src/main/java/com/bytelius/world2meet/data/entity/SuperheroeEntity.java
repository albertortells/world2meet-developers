package com.bytelius.world2meet.data.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "SUPERHEROES")
public class SuperheroeEntity implements Serializable {

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "name", nullable = false, length = 25)
	private String name;

	@Column(name = "power", nullable = false, length = 50)
	private String power;

	public SuperheroeEntity() {}

	public Integer getId() { return id; }

	public void setId(Integer id) { this.id = id; }

	public String getName() { return name; }

	public void setName(String name) { this.name = name; }

	public String getPower() { return power; }

	public void setPower(String power) { this.power = power; }
}

# world2meet-developers

Prueba técnica para World2Meet — API REST de mantenimiento de súper héroes, desarrollada con Spring Boot 2 y Java 11.

> Realizada en julio de 2021 como prueba técnica de acceso.

## Enunciado

### Prueba técnica Spring Boot

Desarrollar, utilizando Spring Boot 2 y Java 11, una API que permita hacer un mantenimiento de súper héroes.

Este mantenimiento debe permitir:

- Consultar todos los súper héroes.
- Consultar un único súper héroe por id.
- Consultar todos los súper héroes que contienen, en su nombre, el valor de un parámetro enviado en la petición. Por ejemplo, si enviamos "man" devolverá "Spiderman", "Superman", "Manolito el fuerte", etc.
- Modificar un súper héroe.
- Eliminar un súper héroe.
- Test unitarios de algún servicio.

**Puntos a tener en cuenta:**

- Los súper héroes se deben guardar en una base de datos H2 en memoria.
- La prueba se debe presentar en un repositorio de Git. No hace falta que esté publicado. Se puede pasar comprimido en un único archivo.

**Puntos opcionales de mejora:**

- Utilizar alguna librería que facilite el mantenimiento de los scripts DDL de base de datos.
- Implementar una anotación personalizada que sirva para medir cuánto tarda en ejecutarse una petición. Se podría anotar alguno o todos los métodos de la API con esa anotación. Funcionaría de forma similar al `@Timed` de Spring, pero imprimiendo la duración en un log.
- Gestión centralizada de excepciones.
- Test de integración.
- Presentar la aplicación dockerizada.
- Poder cachear peticiones.
- Documentación de la API.
- Seguridad del API.

**Se valorará positivamente:**

- El uso de TDD. En caso de subir la práctica a un repositorio, se pueden utilizar los *commits* para ver el proceso.
- Aplicación de los principios SOLID.

## Cómo se resolvió

El relato de cómo se abordó la prueba, las decisiones tomadas y los puntos opcionales cubiertos, junto con las mejoras pendientes para dejarla "perfecta", están publicados en [tech-test.albertortells.cat/world2meet](https://tech-test.albertortells.cat/world2meet/) (versión en markdown de las mejoras: [`IMPROVEMENTS.md`](./IMPROVEMENTS.md)).

## Stack técnico

- Java 11
- Spring Boot 2.5.2 (Web, Data JPA, Data REST, Data JDBC)
- Liquibase (mantenimiento de scripts DDL)
- Base de datos H2 en memoria
- MapStruct (mapeo Entity ↔ DTO/DAO)
- JUnit 5 + Mockito (tests unitarios)

## Cómo ejecutar el proyecto

```bash
./mvnw spring-boot:run
```

La API queda disponible en `http://localhost:8090/world2meet/superheroes`. La consola de H2 está habilitada en `/h2-console`.

## Endpoints principales

| Método | Ruta                                          | Descripción                                    |
|--------|-----------------------------------------------|-------------------------------------------------|
| GET    | `/world2meet/superheroes/get/all`             | Lista todos los súper héroes                     |
| GET    | `/world2meet/superheroes/get/one/{id}`        | Consulta un súper héroe por id                   |
| GET    | `/world2meet/superheroes/get/one?param=man`   | Busca súper héroes cuyo nombre contiene `param`  |
| PUT    | `/world2meet/superheroes/put`                 | Modifica un súper héroe                          |
| DELETE | `/world2meet/superheroes/delete/{id}`         | Elimina un súper héroe                           |

## Tests

```bash
./mvnw test
```

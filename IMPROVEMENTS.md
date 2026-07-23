# Mejoras pendientes

Análisis del estado actual del código y lista de cambios para dejar esta prueba técnica
completa respecto al enunciado (ver [`README.md`](./README.md)) y alineada con buenas
prácticas actuales. Contexto de por qué se llegó hasta aquí en
[`storytelling.html`](./storytelling.html).

## 1. Bug encontrado: el test no está probando lo que parece

`SuperheroeServiceImplTest` (`src/test/java/.../SuperheroeServiceImplTest.java`) declara:

```java
@Autowired
private SuperheroeService service;

@Mock
private SuperheroesRepository repository;
```

El `@Mock` nunca se inyecta en `service` (no hay `@InjectMocks`, `@MockBean` ni
`MockitoAnnotations.openMocks(this)`/`MockitoExtension`). El `service` autowireado es el bean
real, conectado al repositorio real contra la H2 en memoria sembrada por Liquibase con los 10
héroes de `superheroes.sql`. Los `doReturn(...).when(repository)...` no tienen ningún efecto:
son un mock huérfano.

Esto explica por qué el test `getSuperheroByID_isOK_thenReturnStatus200` "funciona" y espera
`"Batman"` para `id = 2`: no es porque el mock devuelva ese valor, es que coincide por
casualidad con el dato semilla real. Es un test de integración disfrazado de test unitario, y
frágil ante cualquier cambio en `superheroes.sql`.

**Cómo arreglarlo:** añadir `@ExtendWith(MockitoExtension.class)` (o `MockitoAnnotations.openMocks(this)`
en un `@BeforeEach`) e inyectar el mock con `@InjectMocks SuperheroeServiceImpl service` en lugar
de `@Autowired` + `@SpringBootTest`, para que sea un test unitario real y rápido, sin levantar
contexto de Spring ni base de datos.

## 2. Inconsistencia Java 11 vs bytecode 1.8

`pom.xml` declara `<java.version>11</java.version>` pero el `maven-compiler-plugin` está
configurado con `<source>1.8</source><target>1.8</target>`. El enunciado pedía explícitamente
Java 11. Cambiar el plugin a `<source>11</source><target>11</target>` (o mejor, usar
`<maven.compiler.release>11</maven.compiler.release>` y eliminar la propiedad duplicada).

## 3. Puntos opcionales del enunciado sin implementar

- **Anotación personalizada de tiempos de ejecución.** Crear `@LogExecutionTime` +
  `@Aspect` con Spring AOP (`spring-boot-starter-aop`) que envuelva el método anotado, mida
  con `System.nanoTime()` y escriba el resultado con un logger (ver punto 6). Aplicarla sobre
  los métodos de `SuperheroeServiceImpl`.
- **Gestión centralizada de excepciones.** Añadir un `@RestControllerAdvice` con
  `@ExceptionHandler` para `MethodArgumentNotValidException`, `ConstraintViolationException` y
  una excepción de negocio propia (p. ej. `SuperheroeNotFoundException`), devolviendo siempre un
  `GenericResponse` coherente en vez de las validaciones manuales repetidas en el controlador
  (`if (id == null || id < 0) ...` en cada método de `SuperheroesController`).
- **Test de integración.** Añadir un `@SpringBootTest` con `@AutoConfigureMockMvc` que golpee
  los endpoints reales (`MockMvc` o `WebTestClient`) contra la H2 en memoria, complementando el
  test unitario del servicio.
- **Dockerización.** Añadir un `Dockerfile` multi-stage (build con `mvnw` + imagen final
  `eclipse-temurin:11-jre`) y opcionalmente un `docker-compose.yml`.
- **Caché de peticiones.** Añadir `spring-boot-starter-cache` + `@EnableCaching`, y anotar
  `getAllSuperheroes()` / `getSuperheroByID()` con `@Cacheable`, invalidando la caché en
  `updateSuperhero()` y `deleteSuperhero()` con `@CacheEvict`.
- **Documentación de la API.** Ya está la dependencia `spring-restdocs-mockmvc` en el `pom.xml`
  pero no se llegó a generar documentación con ella. Más simple y mantenible hoy: añadir
  `springdoc-openapi-ui` para Swagger UI autogenerado, o completar la configuración de REST Docs
  ya presente.
- **Seguridad del API.** No hay ninguna dependencia de seguridad. Como mínimo, añadir
  `spring-boot-starter-security` con autenticación básica o un API key para los endpoints de
  escritura (`PUT`/`DELETE`), y sustituir `@CrossOrigin(origins = "*")` (abierto a cualquier
  origen) por una lista blanca de orígenes concreta.

## 4. Diseño de la API

- **No es RESTful.** Las rutas usan el verbo en la propia URL (`GET /get/all`,
  `GET /get/one/{id}`, `PUT /put`, `DELETE /delete/{id}`) en vez de apoyarse en el método HTTP:
  debería ser `GET /superheroes`, `GET /superheroes/{id}`, `GET /superheroes?name=man`,
  `PUT /superheroes/{id}`, `DELETE /superheroes/{id}`.
- **El `PUT` no lleva el id en la URL** (`SuperheroesController.putExample`, línea 42-48): recibe
  el id dentro del body (`SuperheroeDAO.id`). Debería ser `PUT /superheroes/{id}` con el id como
  `@PathVariable`, y el body solo con los campos a modificar.
- **Nombres de método heredados de la plantilla.** `putExample` y `deleteExample` en
  `SuperheroesController` son nombres de ejemplo de Spring Initializr que no se renombraron;
  deberían llamarse `updateSuperheroe` / `deleteSuperheroe`.
- **`URLConstant.POST` está declarado y no se usa** (no hay endpoint de creación, ni lo pedía el
  enunciado). Si no se va a añadir un `POST`, eliminar la constante muerta; si se añade, usarla.
- **Falta paginación** en `getAllSuperheroes()`. Con `JpaRepository` ya disponible, cambiar a
  `Page<SuperheroeEntity> findAll(Pageable pageable)` es casi gratis.

## 5. Validación

`javax.validation` está en el `pom.xml` y `@Valid` se usa en el controlador, pero
`SuperheroeDAO` no tiene ninguna anotación de validación (`@NotBlank`, `@Size`, etc.), así que
`@Valid` no hace nada ahí. Los controles actuales son manuales y repetidos:

```java
if(data == null || StringUtils.isAllEmpty(data.getName()) || StringUtils.isAllEmpty(data.getPower())) { ... }
```

Mover estas reglas a anotaciones en `SuperheroeDAO` (`@NotBlank` en `name`/`power`,
`@Size(max = 25)` / `@Size(max = 50)` acorde a las columnas de `superheroes.sql`) y dejar que el
`@RestControllerAdvice` del punto 3 traduzca los errores de validación a `GenericResponse`.

## 6. Logging

No hay ningún logger en el proyecto (`grep` de `Logger`/`log.` en `src/main` no devuelve nada).
Añadir SLF4J (`LoggerFactory.getLogger(...)`) al menos en el servicio y en el aspecto de tiempos
de ejecución del punto 3; ahora mismo no queda ningún rastro de lo que hace la aplicación en
producción.

## 7. Acoplamiento a tipos concretos

- `SuperheroesRepository` declara `ArrayList<SuperheroeEntity> findAll()` y
  `ArrayList<SuperheroeEntity> findSuperheroeEntitiesByNameContainingIgnoreCase(String param)`.
  Un repositorio Spring Data debería exponer `List<T>`, no `ArrayList<T>`: acoplarse a la
  implementación concreta no aporta nada y sobreescribir `findAll()` solo para cambiar el tipo de
  retorno es innecesario.
- Mismo patrón en `SuperheroeMapper.arrayListSuperheroeEntityToArrayListSuperheroDTO`: usar
  `List<SuperheroeDTO>` en la firma.

## 8. Mezcla de JAX-RS y Spring

El proyecto usa Spring Boot de principio a fin, pero para los códigos de estado tira de
`javax.ws.rs.core.Response.Status` (JAX-RS) en vez de `org.springframework.http.HttpStatus`, y
mete la dependencia `javax.ws.rs-api` solo para eso. Sustituir por `HttpStatus` de Spring y
devolver `ResponseEntity<GenericResponse>` en los controladores en vez de fijar el status solo
dentro del `GenericResponse` (así el código HTTP real de la respuesta coincide con el que se
informa en el body).

## 9. Configuración

- La contraseña de la datasource (`spring.datasource.password=password`) está hardcodeada en
  `application.properties`. Para una H2 en memoria de prueba el riesgo es mínimo, pero es un mal
  hábito a evitar incluso en pruebas técnicas.
- No hay perfiles (`application-dev.properties`, `application-prod.properties`) ni forma de
  parametrizar el puerto/entorno sin editar el fichero fuente.

## 10. Documentación y naming

- Mezcla de español ("Superheroe", "Prueba técnica") e inglés (nombres de clases, mensajes de
  respuesta) — no bloqueante, pero conviene decidir un idioma para el código y mantenerlo
  consistente.
- No hay Javadoc en las interfaces públicas (`SuperheroeService`, `SuperheroesRepository`).

## Resumen priorizado

1. Arreglar el test que no testea nada real (punto 1) — es lo más grave, da falsa confianza.
2. Alinear Java 11 en el compilador (punto 2) — incumple un requisito explícito del enunciado.
3. Gestión centralizada de excepciones + validación con anotaciones (puntos 3 y 5) — mejora
   visible con poco esfuerzo.
4. Rediseñar rutas a estilo REST (punto 4) — impacto en el contrato de la API, hacerlo antes de
   sumar más clientes.
5. Resto de opcionales (AOP de tiempos, tests de integración, Docker, caché, seguridad,
   documentación OpenAPI) según tiempo disponible.

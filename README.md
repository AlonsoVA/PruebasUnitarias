# Gestión de productos

API REST en Spring Boot 3 para registrar y consultar productos. La base de datos en memoria es H2. Las pruebas unitarias usan JUnit 5, Mockito y AssertJ, y aíslan el servicio del repositorio.

## Requisitos

- JDK 17
- Maven 3.9 o superior

## Cómo ejecutar

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
export PATH="$JAVA_HOME/bin:$PATH"
mvn test
mvn clean verify
```

Si `java_home` ya reconoce el JDK 17, también sirve `export JAVA_HOME=$(/usr/libexec/java_home -v 17)`.

`mvn verify` además comprueba, con JaCoCo, que la cobertura de líneas sea al menos 90 %. La clase principal queda fuera de esa medición.

Para levantar la API:

```bash
mvn spring-boot:run
```

- `GET http://localhost:8080/api/productos`
- `GET http://localhost:8080/api/productos/{id}`
- `POST http://localhost:8080/api/productos`
- Consola H2: `http://localhost:8080/h2-console`  
  JDBC URL: `jdbc:h2:mem:productosdb`  
  Usuario: `sa`  
  Contraseña: vacía

## Estructura

- `entity/Producto`: id, nombre, precio y stock
- `repository/ProductoRepository`: `JpaRepository`
- `service/ProductoService`: `findAll`, `findById` y `create` (con validación)
- `controller/ProductoController`: `/api/productos`
- `src/test/java/.../service/ProductoServiceTest.java`: pruebas unitarias con `@Mock` e `@InjectMocks`

## Lista de cotejo

| Criterio | Cumple |
| --- | --- |
| 1. Dependencia `spring-boot-starter-test` (JUnit 5, Mockito y AssertJ) | Sí |
| 2. Prueba de `findAll`: repositorio simulado y lista validada | Sí |
| 3. Prueba de `create`: persistencia simulada y atributos verificados | Sí |
| 4. `@Mock`, `@InjectMocks` y aserciones AssertJ | Sí |
| 5. `mvn test` termina sin errores | Sí |

La evidencia de la ejecución está en `evidencias/`:

- `mvn-test.txt`: salida completa de `mvn test`
- `01-mvn-test.png`: resultado de las 21 pruebas y `BUILD SUCCESS`
- `02-cobertura-jacoco.png`: cobertura de líneas al 100 %
- `03-api-h2.png`: respuesta real de `GET /api/productos` con los datos de H2

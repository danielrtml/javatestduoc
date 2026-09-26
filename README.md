# RutaLimpia · solicitudes-service

Microservicio de **solicitudes de retiro** del caso RutaLimpia (JVY0101, Duoc UC). Es el servicio que usa el vecino: registra la solicitud y devuelve el folio de inmediato, sin esperar a que haya un camión disponible. La asignación del camión ocurre después de forma asíncrona (SQS + Lambda), así que por ahora ese paso queda solo registrado en el log.

## Tecnologías

Java 17, Spring Boot 3.5, Maven, Spring Data JPA, H2 (perfil dev), PostgreSQL (perfil prod), Lombok y JUnit 5.

## Dependencias del pom.xml

| Dependencia | Para qué se usa |
|---|---|
| spring-boot-starter-web | Exponer la API REST |
| spring-boot-starter-data-jpa | Guardar y consultar solicitudes con JPA/Hibernate |
| spring-boot-starter-validation | Validar el body del POST |
| spring-boot-starter-actuator | `/actuator/health` y `/actuator/info` (muestra la versión del jar) |
| h2 | Base en memoria para desarrollo |
| postgresql | Driver JDBC para producción |
| lombok | Getters, setters, constructores y logger |
| spring-boot-starter-test | Pruebas con JUnit 5 y MockMvc |

No se incluyó `spring-boot-starter-security` porque en nuestro diseño la autenticación la hace **auth-service** y el token lo valida el **API Gateway** antes de llegar a este servicio.

## Compilar y generar el artefacto

El proyecto trae Maven Wrapper, así que no hace falta tener Maven instalado (solo JDK 17 o superior):

```bash
# Windows (PowerShell)
.\mvnw.cmd clean package

# Linux / Mac
./mvnw clean package
```

Esto compila, corre los tests y deja el jar en `target/solicitudes-service-1.0.0.jar`. Si tienes Maven instalado, `mvn clean package` hace lo mismo.

## Ejecutar

```bash
java -jar target/solicitudes-service-1.0.0.jar
```

El servicio queda en `http://localhost:8081`. Para usar PostgreSQL:

```bash
java -jar target/solicitudes-service-1.0.0.jar --spring.profiles.active=prod
```

(con las variables `DB_URL`, `DB_USER` y `DB_PASSWORD` definidas).

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/solicitudes` | Crea la solicitud y devuelve el folio (201) |
| GET | `/solicitudes/{folio}` | Consulta el estado de una solicitud |
| GET | `/solicitudes` | Lista todas las solicitudes |
| GET | `/actuator/health` | Estado del servicio |
| GET | `/actuator/info` | Versión del artefacto |

Ejemplo del body para el POST:

```json
{
  "direccion": "Pasaje Los Aromos 123",
  "tipoResiduo": "PLASTICO",
  "horarioPreferido": "09:00-12:00"
}
```

Tipos de residuo válidos: `PLASTICO`, `VIDRIO`, `PAPEL_CARTON`, `LATAS`, `ELECTRONICOS`.

Prueba rápida desde PowerShell:

```powershell
$body = @{ direccion = "Pasaje Los Aromos 123"; tipoResiduo = "PLASTICO"; horarioPreferido = "09:00-12:00" } | ConvertTo-Json
Invoke-RestMethod -Uri http://localhost:8081/solicitudes -Method Post -ContentType "application/json" -Body $body
```

## Estructura

```
src/main/java/cl/duoc/rutalimpia/solicitudes
├── controller/   endpoints REST
├── dto/          datos de entrada
├── model/        entidad Solicitud y enums
├── repository/   acceso a datos (JPA)
└── service/      lógica: folio y estado
src/main/resources
├── application.properties
├── application-dev.properties
└── application-prod.properties
src/test/java/...  pruebas del controlador
```

## Versionado

La versión del artefacto se define en el `pom.xml` (`<version>1.0.0</version>`) y se marca en Git con un tag `v1.0.0`. La carpeta `/target/` no se sube al repositorio (está en el `.gitignore`).

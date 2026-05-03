# Requerimientos para ejecutar el servicio

Este documento describe los requisitos y los pasos para ejecutar el servicio `nlp-service-java` localmente, sin usar Docker.

## Requisitos mínimos

- Java 17
- Maven 3.x
- Ollama instalado y corriendo en el host local
- Modelo disponible en Ollama, por defecto `deepseek-r1`

## Configuración de Ollama

1. Asegúrate de que Ollama esté en ejecución y accesible en:
   - `http://localhost:11434`

2. Verifica que el endpoint de tags responde:

```bash
curl http://localhost:11434/api/tags
```

3. Verifica que el modelo `deepseek-r1` esté disponible en la lista de modelos.

## Configuración del servicio

El servicio usa `src/main/resources/application.properties` con estos valores por defecto:

```properties
server.port=8192
ollama.api.url=http://localhost:11434/api/generate
ollama.model=deepseek-r1
```

Si necesitas cambiar la URL de Ollama o el modelo, puedes hacerlo en `application.properties` o con variables de entorno:

- `OLLAMA_API_URL`
- `OLLAMA_MODEL`
- `SERVER_PORT`

## Construir el proyecto

Desde la carpeta del repositorio:

```bash
cd /home/fhernandezm/Documents/Repositorios/nlp-service-java
mvn clean package
```

## Ejecutar el servicio

```bash
mvn spring-boot:run
```

O ejecutar el JAR generado:

```bash
java -jar target/nlp-service-java-1.0-SNAPSHOT.jar
```

## Endpoints disponibles

- Health: `GET http://localhost:8192/inference/health`
- Inference: `POST http://localhost:8192/inference`

## Comandos de prueba

Health:

```bash
curl -X GET http://localhost:8192/inference/health
```

Inference:

```bash
curl -X POST http://localhost:8192/inference \
  -H "Content-Type: application/json" \
  -d '{
    "query": "latest social media trends in football",
    "keywordsCount": 5,
    "expandedQueriesCount": 3
  }'
```

## Nota

Si el service devuelve error en `health` o `inference`, es muy probable que el problema sea la conexión con Ollama. Revisa que Ollama esté escuchando en `localhost:11434` y que el servicio pueda acceder a ese endpoint.

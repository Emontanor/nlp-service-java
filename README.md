# NLP Service Java

## Overview

`nlp-service-java` is a Spring Boot microservice for RacconAnalityc's social media scraping pipeline. Its primary objective is to connect a Large Language Model (LLM) to the RacconAnalityc social media scraper, enabling the service to infer keywords, hashtags, and alternative query phrases from user search input.

This service receives a search query, builds a prompt for an LLM, sends that prompt to an Ollama-compatible model endpoint, and returns structured inference output useful for social media analysis.

## Key Features

- Spring Boot REST service
- LLM integration via Ollama API
- Request validation and inference prompt generation
- Health check for Ollama connectivity
- Outputs structured JSON for keywords, expanded queries, and language detection

## Architecture

- `com.racconanalytics.nlpservicejava.NlpServiceApplication` - Spring Boot application entry point
- `com.racconanalytics.nlpservicejava.controller.InferenceController` - REST controller exposing inference and health endpoints
- `com.racconanalytics.nlpservicejava.service.OllamaService` - HTTP client for calling Ollama and parsing model responses
- `com.racconanalytics.nlpservicejava.service.PromptBuilder` - Builds the LLM prompt used for query inference
- `com.racconanalytics.nlpservicejava.dto.InferenceRequest` - Input payload schema
- `com.racconanalytics.nlpservicejava.dto.InferenceResponse` - Output payload schema

## API Endpoints

### Health Check

`GET /inference/health`

Returns service health and verifies Ollama connectivity.

Example response:

```json
{ "status": "healthy", "ollama": "connected" }
```

### Inference

`POST /inference`

Accepts an inference request and returns structured data from the LLM.

Request body:

```json
{
  "query": "latest social media trends in football",
  "keywordsCount": 5,
  "expandedQueriesCount": 3
}
```

Response body:

```json
{
  "originalQuery": "latest social media trends in football",
  "keywords": [
    "football trends",
    "sports discussion",
    "fan reactions",
    "match highlights",
    "world cup buzz"
  ],
  "expandedQueries": [
    "social media trends about football",
    "what football topics are popular online",
    "latest fan conversation about soccer"
  ],
  "language": "en"
}
```

## Configuration

Default configuration values are loaded from `application.properties` or environment variables.

The service uses the following properties:

- `ollama.api.url` - URL for the Ollama generate endpoint (default: `http://localhost:11434/api/generate`)
- `ollama.model` - Ollama model name to use (default: `deepseek-r1`)

The health check currently targets `http://localhost:11434/api/tags` to verify Ollama availability.

## Build and Run

Build with Maven:

```bash
mvn clean package
```

Run the service:

```bash
mvn spring-boot:run
```

Or run the packaged JAR:

```bash
java -jar target/nlp-service-java-1.0-SNAPSHOT.jar
```

## LLM Integration for RacconAnalityc Social Media Scraper

This service is designed as the LLM bridge for RacconAnalityc's social media scraper. It takes raw social query input and transforms it into:

- `keywords` for trend detection and hashtag generation
- `expanded_queries` for related search intents
- `language` to preserve the query language for multilingual analysis

The prompt builder ensures the model returns only valid JSON with the exact fields required by downstream scraper and analytics components.

## Notes

- Ensure Ollama is running and reachable before using the inference endpoint.
- The service expects the Ollama model response to include a JSON object inside the `response` field.
- If the LLM returns invalid JSON, the service will raise an error while parsing the response.

## Docker support

### Dockerfile

A multi-stage Dockerfile is provided to build the service and package a minimal runtime image.

### docker-compose.yml

The compose file builds the Java service and configures it to connect to Ollama on the Docker host via `host.docker.internal`.

### Run with Docker Compose

```bash
docker compose up --build
```

This starts the service at `http://localhost:8192` on the host.

### Override Ollama URL if needed

If your Ollama instance runs on a different host or port, set the environment variable before starting:

```bash
export OLLAMA_API_URL=http://<ollama-host>:11434/api/generate
docker compose up --build
```

### Container service routes

- Health: `GET http://localhost:8192/inference/health`
- Inference: `POST http://localhost:8192/inference`

### Example curl commands

Health check:

```bash
curl -X GET http://localhost:8192/inference/health
```

Inference request:

```bash
curl -X POST http://localhost:8192/inference \
  -H "Content-Type: application/json" \
  -d '{
    "query": "latest social media trends in football",
    "keywordsCount": 5,
    "expandedQueriesCount": 3
  }'
```

### Notes for Linux Docker hosts

The compose file uses `extra_hosts` with `host-gateway` so the container can reach `host.docker.internal`. This allows the service to talk to an Ollama instance running on the host machine at `http://localhost:11434`.

## License

This repository is part of the RacconAnalityc platform and may follow the licensing terms defined by the organization.

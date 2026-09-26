# Novo Jisho API

A modern Scala 3 REST API for searching japanese definitions on Jisho.

A small project to test some of the VirtusLab Scala Stack. Since this
is a well scoped project, I chose sbt over scala-cli because of familiarity. 

Built following the **VirtusLab Scala Stack (VSS)** guidelines with some personal modifications:
- **Direct-Style Scala 3**: Clean, imperative-feeling, synchronous-looking code without monadic effect wrappers (`Future`, `IO`, `Task`).
- **Ox**: For concurrency. Although I'm not using it much, I kept it to remind myself of its syntax in the server edge.
- **Java 21 Virtual Threads**: High-throughput, non-blocking runtime execution powered by Project Loom.
- **Tapir (`tapir-netty-server-sync`)**: Declarative, type-safe API endpoints exposed via Netty Sync Server.
- **Jsoniter-Scala**: Fast compile-time macro-based JSON serialization.
- **Swagger UI**: Interactive OpenAPI documentation automatically generated from endpoint contracts.
- **sbt**: Standard Scala build tool configured with Java 21 target options.

---

## Getting Started

### Prerequisites
- JDK 21+
- sbt 1.10+ 

### Build and Test
```shell
# Compile sources
sbt compile

# Run tests
sbt test
```

### Run the Application
```shell
sbt run
```

### Endpoint usage example

```bash
/jisho?term=dog
```
### Response
```json
[
  {
    "japanese": [
      {
        "word": "犬の顔",
        "reading": "いぬのかお"
      }
    ],
    "english": {
      "definitions": [
        "dog",
        "dog's face",
        "dog's head",
        "sake bottle shape",
        "pattern of three stones resembling a dog's face"
      ]
    },
    "speech": {
      "parts": [
        "Expressions (phrases, clauses, etc.)",
        "Noun"
      ]
    }
  },
  {
    "japanese": [
      {
        "word": "トックリ形",
        "reading": "とっくりけい"
      },
      {
        "word": "トックリ型",
        "reading": "とっくりけい"
      }
    ],
    "english": {
      "definitions": [
        "dog",
        "dog's face",
        "dog's head",
        "sake bottle shape",
        "pattern of three stones resembling a dog's face"
      ]
    },
    "speech": {
      "parts": [
        "Expressions (phrases, clauses, etc.)",
        "Noun"
      ]
    }
  }
  ...
]
```
### Environment variables
```bash
JISHO_HTTP_PORT # 8080 by default
JISHO_API_HOST # localhost by default 
JISHO_RESULTS_LIMIT # number of results to display at once
```

By default, the server starts on port `8080` (configurable via the `JISHO_HTTP_PORT` environment variable).
- **Swagger UI**: [http://localhost:8080/docs](http://localhost:8080/docs)
- **API Spec (YAML)**: [http://localhost:8080/docs/docs.yaml](http://localhost:8080/docs/docs.yaml)

---

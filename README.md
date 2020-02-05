# Стартер-бработчик ошибок

###Default response
```json5
  {
    "errorId": "8f29fa7d-9f50-495c-b291-75e2f2d3667a",
    "errorCode":"INTERNAL_ERROR",
    "message":"Internal server error",
    "applicationName":"test-application"
  }
```
| Field | Meaning |
| ----- | ------- |
| errorId  | Random error id (UUID) |
| errorCode  | Error code  |
| message  | Text message to display |
| applicationName  | Service name that throws message|

###Configuration

```yaml
errors:
  error-codes:
    '[error.null.pointer]':             <-- error code
      http-code: 500                    <-- http code for this error
      client-code: NULL_POINTER         <-- client code for this error
      messages:
        ru: "Ошибка на стороне сервера" <-- message for 'ru' locale
        en: "Server error"              <-- message for 'en' locale
```


# Deploy

### To build artifacts
```shell script
./gradlew clean build
```

### To add into local maven repository
```shell script
./gradlew publishToMavenLocal
```

### To deploy snapshot
```shell script
./gradlew clean build snapshot -Prelease.scope=(major|minor|patch) publish
```

### To deploy release
```shell script
./gradlew clean build final -Prelease.scope=(major|minor|patch) publish
```
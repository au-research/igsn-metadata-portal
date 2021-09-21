# IGSN Portal
## Requirements

* JDK 8+
* Apache Maven 3.2.1+ 
* IGSN Metadata Registry 1.0+
* nodejs >8 with `npm` and `yarn`

For detailed requirements and installation instructions, please refer to the [Installation Guide](docs/Install.md) and [Keycloak Guide](docs/Keycloak.md)

## Quick Start
```
cp /src/main/resources/application.properties.sample /src/main/resources/application.properties

yarn build-prod
mvn clean package -Dmaven.test.skip=true
java -jar target/igsn-portal.jar
```

## Configuration
Configuration can be done via `application.properties` the most notable properties are
* `app.registry_url` should point to the live instance of IGSN Metadata Registry, eg. [https://identifiers.ardc.edu.au/igsn-registry/](https://identifiers.ardc.edu.au/igsn-registry/)
* `app.portal_url` should point to the URL of the IGSN Metadata Portal eg. [https://identifiers.ardc.edu.au/igsn-portal/](https://identifiers.ardc.edu.au/igsn-portal/)
* `app.editor_url` should point to the URL of the IGSN Metadata Editor eg.  [https://identifiers.ardc.edu.au/igsn-editor/](https://identifiers.ardc.edu.au/igsn-editor/)

## License
IGSN Metadata Registry is licensed under the Apache license, Version 2.0 See [LICENSE](LICENSE) for the full license text
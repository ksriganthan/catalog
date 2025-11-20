package org.example.catalog;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;


/*
Sinn und Zweck:
Testcontainer sollten eine echte DB (in diesem Fall PostgreSql) für das Testen verwenden,
ohne dabei die richtige DB zu verwenden.
 */

// Diese Klasse erbt von PostgresContainerTest -> Sobald die Klasse erstellt wird, wird Container gestartet
@Testcontainers
public abstract class PostgresContainerTest {

    // Container deklarieren
    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:16") // PostgreSQL 16 Image von Docker Hub
                    .withDatabaseName("catalog")
                    .withUsername("catalog")
                    .withPassword("catalog");

}

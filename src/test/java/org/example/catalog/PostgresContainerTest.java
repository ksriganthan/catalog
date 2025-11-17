package org.example.catalog;


import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

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
    static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:16") // PostgreSQL 16 Image von Docker Hub
                    .withDatabaseName("catalog")
                    .withUsername("catalog")
                    .withPassword("catalog");

    /* Diese Methode überschreibt die Spring Properties zur Testlaufzeit
       Damit sich Spring während Tests nicht zur echten DB, sondern zur DB im Container verbindet
     */
    @DynamicPropertySource
    public static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl); // Port ist zufällig
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);

        registry.add("spring.datasource.driver-class-name",
                () -> "org.postgresql.Driver");

        registry.add("spring.jpa.hibernate.ddl-auto",
                () -> "create-drop");

        registry.add("spring.jpa.database-platform",
                () -> "org.hibernate.dialect.PostgreSQLDialect");
    }
}

package org.example.catalog;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class CatalogApplicationTests extends PostgresContainerTest {

	@Test
	void contextLoads() {
	}

}

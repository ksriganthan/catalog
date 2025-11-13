package org.example.catalog.gatlingLastTest;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

// Dieser Test kann nicht in diesem Projekt ausgeführt werden - abgesprochen mit Herrn Martinelli
// 13.11.2025 ks

public class BookSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080")
            .acceptHeader("application/json");

    ScenarioBuilder scn = scenario("Book API Load Test")
            .repeat(1).on(
                    exec(http("GetBooks")
                            .get("/books/search?keyword=")
                            .check(status().is(200))
                    )
            );

    {
        setUp(
                scn.injectOpen(atOnceUsers(100)) // 1 User x 100 Anfragen
        ).protocols(httpProtocol);
    }
}

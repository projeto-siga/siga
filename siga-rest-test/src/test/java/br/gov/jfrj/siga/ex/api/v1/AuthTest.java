package br.gov.jfrj.siga.ex.api.v1;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.BeforeClass;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

public class AuthTest {

    private static RequestSpecification requestSpecZZ99999;
    private static RequestSpecification requestSpecZZ99998;
    private static RequestSpecification requestSpecZZ99997;

    public static enum User {
        ZZ99999, ZZ99998, ZZ99997
    }

    public static enum Lotacao {
        ZZLTEST, ZZLTEST2, ZZLTEST3
    }

    @BeforeClass
    public static void AuthSetup() {

        // Utilizei uma lógica de singleton para não ser necessário autenticar a cada
        // teste.
        //
        RequestSpecification localInstance = requestSpecZZ99999;
        if (localInstance == null) {
            synchronized (AuthTest.class) {
                localInstance = requestSpecZZ99999;
                if (localInstance == null) {
                    RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
                    requestSpecZZ99997 = buildSpec(auth("ZZ99997", "Password1"));
                    requestSpecZZ99998 = buildSpec(auth("ZZ99998", "Password1"));
                    requestSpecZZ99999 = buildSpec(auth("ZZ99999", "Password1"));
                }
            }
        }
    }

    private static RequestSpecification buildSpec(String Authorization) {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setBaseUri("http://localhost:8080");
        builder.addHeader("Authorization", Authorization);

        return builder.build();
    }

    private static String auth(String username, String password) {
        String token = given().log().everything().auth().preemptive().basic(username, password)

                .when()
                .post("http://localhost:8080/siga/api/v1/autenticar")

                .then()
                .statusCode(200)
                .body("token", notNullValue())

                .extract()
                .path("token");

        String Authorization = "bearer " + token;
        return Authorization;
    }

    public static RequestSpecification givenZZ99999() {
        return given().spec(requestSpecZZ99999);
    }

    public static RequestSpecification givenZZ99998() {
        return given().spec(requestSpecZZ99998);
    }

    public static RequestSpecification givenZZ99997() {
        return given().spec(requestSpecZZ99997);
    }

    public static RequestSpecification givenFor(User user) {
        switch (user) {
            case ZZ99999:
                return givenZZ99999();
            case ZZ99998:
                return givenZZ99998();
            case ZZ99997:
                return givenZZ99997();
        }
        return null;
    }

}

package br.gov.jfrj.siga.api.v1;

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
    private static RequestSpecification requestSpecZZ99996;
    private static RequestSpecification requestSpecZZ99995;
    private static RequestSpecification requestSpecZZ99994;

    public static enum Pessoa {
        ZZ99999(1), ZZ99998(2), ZZ99997(3), ZZ99996(9), ZZ99995(8), ZZ99994(7);

        int id;

        Pessoa(int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }
    }

    public static enum Lotacao {
        ZZLTEST(1), ZZLTEST2(2), ZZLTEST3(3);

        int id;

        Lotacao(int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }
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
                    requestSpecZZ99994 = buildSpec(auth("ZZ99994", "Password1"));
                    requestSpecZZ99995 = buildSpec(auth("ZZ99995", "Password1"));
                    requestSpecZZ99996 = buildSpec(auth("ZZ99996", "Password1"));
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

    public static RequestSpecification givenZZ99996() {
        return given().spec(requestSpecZZ99996);
    }

    public static RequestSpecification givenZZ99995() {
        return given().spec(requestSpecZZ99995);
    }

    public static RequestSpecification givenZZ99994() {
        return given().spec(requestSpecZZ99994);
    }

    public static RequestSpecification givenFor(Pessoa pessoa) {
        switch (pessoa) {
            case ZZ99999:
                return givenZZ99999();
            case ZZ99998:
                return givenZZ99998();
            case ZZ99997:
                return givenZZ99997();
            case ZZ99996:
                return givenZZ99996();
            case ZZ99995:
                return givenZZ99995();
            case ZZ99994:
                return givenZZ99994();
        }
        return null;
    }

}

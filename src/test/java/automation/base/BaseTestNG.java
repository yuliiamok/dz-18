package automation.base;

import automation.Config;
import automation.app.BookingId;
import io.qameta.allure.Attachment;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static io.restassured.RestAssured.given;
import static automation.Config.*;
import static io.restassured.mapper.ObjectMapperType.GSON;
import static org.hamcrest.Matchers.notNullValue;

public class BaseTestNG {
    final protected Logger logger = LogManager.getLogger(this.getClass());

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method, Object[] testArgs) {
        logger.debug("---------------------------------------------------------------------------");
        logger.debug("-- Run test: " + method.getAnnotation(Test.class).testName());
        logger.debug("---------------------------------------------------------------------------");
        int i = 1;
        for (Object obj : testArgs) {
            logger.debug("Argument " + i ++ + ": " + obj);
        }

    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod(Method method) {
        logger.debug("---------------------------------------------------------------------------");
        logger.debug("-- End test: " + method.getAnnotation(Test.class).testName());
        logger.debug("---------------------------------------------------------------------------");
        this.attachResourceFile("env/" + Config.getEnvironmentName() + ".properties");
    }

    @Attachment("Resource file: {resourceFilePath}")
    private byte[] attachResourceFile(String resourceFilePath) {
        try {
            URI uri      = ClassLoader.getSystemResource(resourceFilePath).toURI();
            Path path     = Paths.get(uri);
            String content  = Files.readString(path);
            //System.out.println(content);
            return content.getBytes();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

    }

    private String token;
    public String getToken() {
        if (token == null) {
            token = login();
        }
        return token;
    }

    private String login() {

        JSONObject authData = new JSONObject();

        authData.put("username", SITE_USER_USERNAME.value);
        authData.put("password", SITE_USER_PASSWORD.value);

        ValidatableResponse response = given()
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .body(authData.toString())
                .when()
                .post(getBaseURL().resolve("/auth"))
                .then()
                .assertThat()
                .statusCode(200)
                .log()
                .all();

        return response.extract().path("token");
    }

    @BeforeMethod
    public void setFilters() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        RestAssured.config = RestAssuredConfig.config().objectMapperConfig(new ObjectMapperConfig(GSON));
    }
    protected static final RequestSpecification REQ_SPEC =
            new RequestSpecBuilder()
                    .setContentType(ContentType.JSON)
                    .setAccept("application/json")
                    .build();

    protected static final ResponseSpecification RESP_SPEC =
            new ResponseSpecBuilder()
                    .expectStatusCode(200)
                    .build();
}

package mc;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;

@Slf4j
public class BaseTestCase {

    @BeforeAll
    public static void setup() {
        baseUriVersioned();
    }

    private static final Config CONFIG = ConfigFactory.load();

    private static void baseUriVersioned() {
        log.info("Building base URI with versioned path");

        RestAssured.baseURI = CONFIG.getString("base.uri");
        RestAssured.basePath = CONFIG.getString("base.version");
    }

    protected RequestSpecification buildRequestSpecification(Object body) {
        return new RequestSpecBuilder()
                .log(LogDetail.ALL)
                .setContentType(ContentType.JSON)
                .setBody(body)
                .addFilter(new AllureRestAssured())
                .addFilter(new ResponseLoggingFilter())
                .build();
    }

    public static <T> T extractObjectAsDto(ValidatableResponse validatableResponse, Class<T> dtoClass) {
        return validatableResponse.extract().as(dtoClass, ObjectMapperType.JACKSON_2);
    }
}
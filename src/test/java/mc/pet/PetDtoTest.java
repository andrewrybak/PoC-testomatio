package mc.pet;

import com.github.javafaker.Faker;
import com.mc.models.PetDto;
import io.restassured.response.ValidatableResponse;
import mc.BaseTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

class PetDtoTest extends BaseTestCase {

    private final Faker faker = new Faker();

    @Test
    @Tags(value = {@Tag("pets"), @Tag("regression")})
    @DisplayName("Verify ability to create the pet")
    void verifyCreatePet() {
        var pet = PetDto.builder()
                .id(faker.number().randomNumber())
                .name(faker.name().name())
                .photoUrls(Collections.singletonList(faker.witcher().quote()))
                .status("available")
                .build();

        ValidatableResponse response = given()
                .spec(buildRequestSpecification(pet))
                .when()
                .post("/pet")
                .then();

        assertThat(response.extract().body().jsonPath().getLong("id")).isEqualTo(pet.getId());
        assertThat(response.extract().body().jsonPath().getString("name")).isEqualTo(pet.getName());
    }

    @Test
    @Tags(value = {@Tag("pets"), @Tag("regression")})
    @DisplayName("Verify ability to get the pet by id")
    void verifyGetPetById() {
        var pet = PetDto.builder()
                .id(faker.number().randomNumber())
                .name(faker.name().name())
                .photoUrls(Collections.singletonList(faker.witcher().quote()))
                .status("available")
                .build();

        ValidatableResponse createdPet = given()
                .spec(buildRequestSpecification(pet))
                .when()
                .post("/pet")
                .then();

        var petId = createdPet.extract().body().jsonPath().getLong("id");

        var retrievedPet = given()
                .spec(buildRequestSpecification(pet))
                .when()
                .get("/pet/{petId}", petId)
                .then();

        assertThat(retrievedPet.extract().body().jsonPath().getLong("id"))
                .isEqualTo(createdPet.extract().body().jsonPath().getLong("id"));

        assertThat(retrievedPet.extract().body().jsonPath().getString("name"))
                .isEqualTo(createdPet.extract().body().jsonPath().getString("name"));
    }

    @Test
    @Tags(value = {@Tag("pets"), @Tag("regression")})
    @DisplayName("Verify ability to create the pet with unavailable status")
    void verifyCreatePetWithStatusUnavailable() {
        var pet = PetDto.builder()
                .id(faker.number().randomNumber())
                .name(faker.name().name())
                .photoUrls(Collections.singletonList(faker.witcher().quote()))
                .status("unavailable")
                .build();

        ValidatableResponse response = given()
                .spec(buildRequestSpecification(pet))
                .when()
                .post("/pet")
                .then();

        assertThat(response.extract().body().jsonPath().getString("status")).isEqualTo("unavailable");

    }

//    @Test
//    @Tags(value = {@Tag("pets"), @Tag("regression")})
//    @DisplayName("Verify ability to create the pet with available status")
//    void verifyCreatePetWithStatusAvailable() {
//        var pet = PetDto.builder()
//                .id(faker.number().randomNumber())
//                .name(faker.name().name())
//                .photoUrls(Collections.singletonList(faker.witcher().quote()))
//                .status("available")
//                .build();
//
//        ValidatableResponse response = given()
//                .spec(buildRequestSpecification(pet))
//                .when()
//                .post("/pet")
//                .then();
//
//        assertThat(response.extract().body().jsonPath().getString("status")).isEqualTo("unavailable");
//    }
}
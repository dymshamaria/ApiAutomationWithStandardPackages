package rest_assured;

import org.json.JSONObject;
import org.junit.jupiter.api.*;
import sun.nio.ch.WindowsAsynchronousFileChannelImpl;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Pet {

    String requestBody = null;
    Integer petId = 32; // id питомца
    String petName = "Hazel"; // имя питомца

    {
        //тело запроса
        String initialBody = null;
        try {
            initialBody = WindowsAsynchronousFileChannelImpl.readFile("src/test/resources/addNewPet.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //изменяем тело запроса
        JSONObject jsonObject = new JSONObject(initialBody);
        jsonObject.put("id", petId);
        jsonObject.put("name", petName);
        requestBody = jsonObject.toString();
    }

    @Test
    @Order(1)
    @DisplayName("Поиск питомца по статусу")
    public void test1() {

        Response response = given()
                .baseUri("https://petstore.swagger.io/v2/")
                .basePath("pet/findByStatus")
                .queryParam("status", "available")
                .header("Accept", "application/json")
                .header("Content-Type", "application/json;;charset=UTF-8")
                .header("Accept-Encoding", "gzip, deflate, br")
                .when()
                .get()
                .then()
                .extract().response();

        assertEquals(200, response.getStatusCode(), "Статус не равен ожидаемого. Он равен " + response.getStatusCode());

        System.out.println(response.statusCode());
        System.out.println(response.getBody().asString);
    }

    @Test
    @Order(2)
    @DisplayName("Добавление нового питомца")
    public void test2() {

        Response response = given()
                .baseUri("https://petstore.swagger.io/v2/")
                .basePath("pet")
                .header("Accept", "application/json")
                .header("Content-Type", "application/json;;charset=UTF-8")
                .header("Accept-Encoding", "gzip, deflate, br")
                .body(requestBody)
                .when()
                .post()
                .then()
                .extract().response();

        assertEquals(200, response.getStatusCode(), "Статус не равен ожидаемого. Он равен " + response.getStatusCode());

        System.out.println(response.statusCode());
        System.out.println(response.getBody().asString);
    }

    @Test
    @Order(3)
    @DisplayName("Поиск питомца по его id")
    public void test3() {

        Response response = given()
                .baseUri("https://petstore.swagger.io/v2/")
                .basePath("pet/" + petId)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json;;charset=UTF-8")
                .header("Accept-Encoding", "gzip, deflate, br")
                .when()
                .get()
                .then()
                .extract().response();

        assertEquals(200, response.getStatusCode(), "Статус не равен ожидаемого. Он равен " + response.getStatusCode());

        System.out.println(response.statusCode());
        System.out.println(response.getBody().asString);
    }

    @Test
    @Order(4)
    @DisplayName("Обновление информации по существующему питомцу")
    public void test4() {

        //изменение тело запроса
        JSONObject jsonObject = new JSONObject(requestBody);
        jsonObject.put("name", "Lucky");

        Response response = given()
                .baseUri("https://petstore.swagger.io/v2/")
                .basePath("pet")
                .header("Accept", "application/json")
                .header("Content-Type", "application/json;;charset=UTF-8")
                .header("Accept-Encoding", "gzip, deflate, br")
                .body(jsonObject.toString())
                .when()
                .put()
                .then()
                .extract().response();

        assertEquals(200, response.getStatusCode(), "Статус не равен ожидаемого. Он равен " + response.getStatusCode());

        System.out.println(response.statusCode());
        System.out.println(response.getBody().asString);
    }


    @Test
    @Order(5)
    @DisplayName("Обновление информации по существующему питомцу через форму")
    public void test5() {

        //изменение тело запроса
        JSONObject jsonObject = new JSONObject(requestBody);
        jsonObject.put("name", "Coco");

        Response response = given()
                .baseUri("https://petstore.swagger.io/v2/")
                .basePath("pet/" + petId)
                .header("Accept", "application/json")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept-Encoding", "gzip, deflate, br")
                .formParam("name", "Coco")
                .when()
                .post()
                .then()
                .extract().response();

        assertEquals(200, response.getStatusCode(), "Статус не равен ожидаемого. Он равен " + response.getStatusCode());

        System.out.println(response.statusCode());
        System.out.println(response.getBody().asString);
    }

    @Test
    @Order(6)
    @DisplayName("Удаление информации о питомце по его id")
    public void test6() {

        Response response = given()
                .baseUri("https://petstore.swagger.io/v2/")
                .basePath("pet/" + petId)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json;;charset=UTF-8")
                .header("Accept-Encoding", "gzip, deflate, br")
                .when()
                .delete()
                .then()
                .extract().response();

        assertEquals(200, response.getStatusCode(), "Статус не равен ожидаемого. Он равен " + response.getStatusCode());

        System.out.println(response.statusCode());
        System.out.println(response.getBody().asString);
    }
}


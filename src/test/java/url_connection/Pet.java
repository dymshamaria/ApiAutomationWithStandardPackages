package url_connection;

import org.json.JSONObject;
import org.junit.jupiter.api.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sun.nio.ch.WindowsAsynchronousFileChannelImpl.readFile;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Pet {

    String requestBody = null;
    Integer petId = 12; // id питомца
    String petName = "Hazel"; // имя питомца

    {
        //тело запроса
        String initialBody = readFile("src/test/resources/addNewPet.json");

        //изменяем тело запроса
        JSONObject jsonObject = new JSONObject(initialBody);
        jsonObject.put("id", petId);
        jsonObject.put("name", petName);
        requestBody = jsonObject.toString();
    }

    public Pet() throws IOException {
    }

    @Test
    @Order(1)
    @DisplayName("Поиск питомца по статусу")
    public void test1() throws IOException {

        //формируем запрос
        URL baseUrl = new URL("https://petstore.swagger.io/v2/pet/findByStatus?status=available");
        HttpURLConnection connection = (HttpURLConnection) baseUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");

        //получаем ответ
        StringBuilder content = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        Map<String, List<String>> headersMap = connection.getHeaderFields();
        for (Map.Entry item : headersMap.entrySet()) {
            System.out.println(item.getKey() + "  " + item.getValue());
        }
        System.out.println(content);

        Assertions.assertTrue(headersMap.keySet().contains("Connection"));
    }

    @Test
    @Order(2)
    @DisplayName("Добавление нового питомца")
    public void test2() throws IOException {

        //формируем запрос
        URL baseUrl = new URL("https://petstore.swagger.io/v2/pet");
        HttpURLConnection connection = (HttpURLConnection) baseUrl.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
        connection.setDoOutput(true);
        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(requestBody.getBytes(StandardCharsets.UTF_8));
        }

        //получаем ответ
        StringBuilder content = new StringBuilder();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        Map<String, List<String>> headersMap = connection.getHeaderFields();
        for (Map.Entry item : headersMap.entrySet()) {
            System.out.println(item.getKey() + "  " + item.getValue());
        }
        Assertions.assertTrue(content.toString().contains("12"));
        System.out.println(content);
    }

    @Test
    @Order(3)
    @DisplayName("Поиск питомца по его id")
    public void test3() throws IOException {

        //формируем запрос
        URL baseUrl = new URL("https://petstore.swagger.io/v2/pet/" + petId);
        HttpURLConnection connection = (HttpURLConnection) baseUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");


        //получаем ответ
        StringBuilder content = new StringBuilder();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        Map<String, List<String>> headersMap = connection.getHeaderFields();
        for (Map.Entry item : headersMap.entrySet()) {
            System.out.println(item.getKey() + "  " + item.getValue());
        }
        Assertions.assertTrue(content.toString().contains("12"));
        Assertions.assertTrue(content.toString().contains("Hazel"));
        System.out.println(content);
    }

    @Test
    @Order(4)
    @DisplayName("Обновление информации по существующему питомцу")
    public void test4() throws IOException {

        //изменяем тело запроса
        JSONObject jsonObject = new JSONObject(requestBody);
        jsonObject.put("name", "Lucky");

        //формируем запрос
        URL baseUrl = new URL("https://petstore.swagger.io/v2/pet/");
        HttpURLConnection connection = (HttpURLConnection) baseUrl.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
        connection.setDoOutput(true);
        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
        }

        //получаем ответ
        StringBuilder content = new StringBuilder();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        Map<String, List<String>> headersMap = connection.getHeaderFields();
        for (Map.Entry item : headersMap.entrySet()) {
            System.out.println(item.getKey() + "  " + item.getValue());
        }
        Assertions.assertTrue(content.toString().contains("Lucky"));
        System.out.println(content);
    }

    @Test
    @Order(5)
    @DisplayName("Обновление информации по существующему питомцу через форму")
    public void test5() throws IOException {

        //изменяем тело запроса
        Map<String, String> formData = new HashMap<>();
        formData.put("name", "Coco");

        //формируем запрос
        URL baseUrl = new URL("https://petstore.swagger.io/v2/pet/" + petId);
        HttpURLConnection connection = (HttpURLConnection) baseUrl.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
        connection.setDoOutput(true);
        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(getFormDataAsString(formData).getBytes(StandardCharsets.UTF_8));
        }

        //получаем ответ
        StringBuilder content = new StringBuilder();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        Map<String, List<String>> headersMap = connection.getHeaderFields();
        for (Map.Entry item : headersMap.entrySet()) {
            System.out.println(item.getKey() + "  " + item.getValue());
        }
        Assertions.assertTrue(content.toString().contains("Coco"));
        System.out.println(content);
    }

    @Test
    @Order(6)
    @DisplayName("Удаление информации о питомце по его id")
    public void test6() throws IOException {

        //формируем запрос
        URL baseUrl = new URL("https://petstore.swagger.io/v2/pet/" + petId);
        HttpURLConnection connection = (HttpURLConnection) baseUrl.openConnection();
        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");

        //получаем ответ
        StringBuilder content = new StringBuilder();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        Map<String, List<String>> headersMap = connection.getHeaderFields();
        for (Map.Entry item : headersMap.entrySet()) {
            System.out.println(item.getKey() + "  " + item.getValue());
        }
        System.out.println(content);
    }

    private String getFormDataAsString(Map<String, String> formData) {
        StringBuilder formBodyBuilder = new StringBuilder();
        for (Map.Entry<String, String> singleEntry : formData.entrySet()) {
            if (formBodyBuilder.length() > 0) {
                formBodyBuilder.append("&");
            }
            formBodyBuilder.append(URLEncoder.encode(singleEntry.getKey(), StandardCharsets.UTF_8));
            formBodyBuilder.append("=");
            formBodyBuilder.append(URLEncoder.encode(singleEntry.getValue(), StandardCharsets.UTF_8));
        }
        return formBodyBuilder.toString();
    }
}

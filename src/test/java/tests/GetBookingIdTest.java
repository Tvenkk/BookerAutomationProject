package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.BookingDates;
import core.models.CreatedBooking;
import core.models.NewBooking;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GetBookingIdTest {

    private APIClient apiClient;
    private ObjectMapper objectMapper;
    private CreatedBooking createdBooking; // Храним созданное бронирование
    private NewBooking newBooking;

    @Test
    public void testGetBookingId() throws JsonProcessingException {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();

        //Создаем объект Booking с необходимыми данными
        newBooking = new NewBooking();
        newBooking.setFirstname("Roman");
        newBooking.setLastname("Tsapko");
        newBooking.setTotalprice(170);
        newBooking.setDepositpaid(true);
        newBooking.setBookingdates(new BookingDates("2024-12-07", "2024-12-12"));
        newBooking.setAdditionalneeds("BreakFast");

        // Выполняем запрос к эндпоинту /booking через APIClient
        String requestBody = objectMapper.writeValueAsString(newBooking);
        Response response = apiClient.createBooking(requestBody);

        // Проверяем что статус-код ответа равен 200
        assertThat(response.getStatusCode()).isEqualTo(200);

        // Десерилизуем тело ответа в список объектов Booking
        String responseBody = response.getBody().asString();
        CreatedBooking createdBooking = objectMapper.readValue(response.asString(), CreatedBooking.class);

        // Получаем id созданного бронирования
        int bookingid = createdBooking.getBookingid();
        // Проверка передачи id
        System.out.println(bookingid);

        // Выполняем запрос на получение информации по созданном клиенте отправляя id
        Response response1 = apiClient.getBookingById(bookingid);

        // Проверяем, что статус-код ответа равен 200
        Assertions.assertThat(response1.getStatusCode()).isEqualTo(200);

        // Удаляем созданное бронирование
        apiClient.createToken("admin", "password123");
        apiClient.deleteBooking(createdBooking.getBookingid());

        // Проверяем, что бронирование успешно удалено
        assertThat(apiClient.getBookingById(createdBooking.getBookingid()).getStatusCode()).isEqualTo(404);
    }
}
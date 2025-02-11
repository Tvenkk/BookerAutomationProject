package tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.Booking;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeleteBookingTest {
    private APIClient apiClient;
    private ObjectMapper objectMapper;

    // Инициализация API клиента перед каждым тестом
    @BeforeEach
    public void setup() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
        apiClient.createToken("admin", "password123");
    }

    @Test
    public void testGetBooking() throws Exception {
        // Выполняем запрос к эндпоинту /booking через APIClient
        Response response = apiClient.getBooking();

        // Десерилизуем тело ответа в список объектов Booking
        String responseBody = response.getBody().asString();
        List<Booking> bookings = objectMapper.readValue(responseBody, new TypeReference<List<Booking>>() {});
        int bookingid = bookings.get(0).getBookingid();
        apiClient.deleteBooking(bookingid);
        Response deleteResponse = apiClient.getBookingById(bookingid);
        assertEquals(404, deleteResponse.getStatusCode());
    }
}

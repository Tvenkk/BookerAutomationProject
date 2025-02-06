package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.BookingById;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetBookingByIdTest {
    private APIClient apiClient;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        apiClient = new APIClient();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetBookingById() throws Exception {
        // Выполняем запрос к эндпоинту /bookingbyid через APIClient
        Response response = apiClient.getBookingById();

        // Проверяем, что статус-код ответа равен 200
        assertThat(response.getStatusCode()).isEqualTo(200);

        BookingById bookingbyid = objectMapper.readValue(response.asString(), BookingById.class);

        assertEquals("Mark", bookingbyid.getFirstname(), "Неверное Имя");
        assertEquals("Smith", bookingbyid.getLastname(), "Неверная Фамилия");
        assertEquals(405, bookingbyid.getTotalprice(), "Неверная цена");
    }
}
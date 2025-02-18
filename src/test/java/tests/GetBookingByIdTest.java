package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.BookingById;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        Response response = apiClient.getBookingById(779);

        // Проверяем, что статус-код ответа равен 200
        assertThat(response.getStatusCode()).isEqualTo(200);

        BookingById bookingbyid = objectMapper.readValue(response.asString(), BookingById.class);

        assertEquals("John", bookingbyid.getFirstname(), "Неверное Имя");
        assertEquals("Smith", bookingbyid.getLastname(), "Неверная Фамилия");
        assertEquals(111, bookingbyid.getTotalprice(), "Неверная цена");
        assertTrue(bookingbyid.isDepositpaid());
        assertEquals("Breakfast", bookingbyid.getAdditionalneeds(), "Неверные данные о дополнительных потребностях");
    }
}
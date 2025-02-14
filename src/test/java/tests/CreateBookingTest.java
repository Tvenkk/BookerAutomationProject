package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.APIClient;
import core.models.BookingDates;
import core.models.CreatedBooking;
import core.models.NewBooking;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateBookingTest {

    private APIClient apiClient;
    private ObjectMapper objectMapper;
    private CreatedBooking createdBooking; // Храним созданное бронирование
    private NewBooking newBooking; // Новый объект для создания бронирования

    // Инициализация API Клиента и создание объекта Booking перед каждым тестом
    @BeforeEach
    public void setup() {
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
    }

    @Test
    public void createBooking() throws JsonProcessingException {
        // Выполняем запрос к эндпоинту /booking через APIClient
        String requestBody = objectMapper.writeValueAsString(newBooking);
        Response response = apiClient.createBooking(requestBody);

        // Проверяем что статус-код ответа равен 200
        assertThat(response.getStatusCode()).isEqualTo(200);

        // Десериализуем тело ответа в объект Booking
        String responseBody = response.asString();
        createdBooking = objectMapper.readValue(responseBody, CreatedBooking.class);

        // Проверяем, что тело ответа содержит объект нового бронирования
        assertThat(createdBooking).isNotNull();
        assertEquals(newBooking.getFirstname(), createdBooking.getBooking().getFirstname());
        assertEquals(newBooking.getLastname(), createdBooking.getBooking().getLastname());
        assertEquals(newBooking.getTotalprice(), createdBooking.getBooking().getTotalprice());
        assertEquals(newBooking.isDepositpaid(), createdBooking.getBooking().isDepositpaid());
        assertEquals(newBooking.getBookingdates().getCheckin(), createdBooking.getBooking().getBookingdates().getCheckin());
        assertEquals(newBooking.getBookingdates().getCheckout(), createdBooking.getBooking().getBookingdates().getCheckout());
        assertEquals(newBooking.getAdditionalneeds(), createdBooking.getBooking().getAdditionalneeds());
    }

    @AfterEach
    public void tearDown() {
        // Удаляем созданное бронирование
        apiClient.createToken("admin", "password123");
        apiClient.deleteBooking(createdBooking.getBookingid());

        // Проверяем, что бронирование успешно удалено
        assertThat(apiClient.getBookingById(createdBooking.getBookingid()).getStatusCode()).isEqualTo(404);
    }
}

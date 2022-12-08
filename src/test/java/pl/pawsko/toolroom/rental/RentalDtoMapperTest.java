package pl.pawsko.toolroom.rental;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.pawsko.toolroom.category.Category;
import pl.pawsko.toolroom.location.Location;
import pl.pawsko.toolroom.manufacturer.Manufacturer;
import pl.pawsko.toolroom.powertype.PowerType;
import pl.pawsko.toolroom.status.Status;
import pl.pawsko.toolroom.tool.Tool;
import pl.pawsko.toolroom.tool.ToolRepository;
import pl.pawsko.toolroom.user.User;
import pl.pawsko.toolroom.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RentalDtoMapperTest {
    static Manufacturer manufacturer = new Manufacturer();
    static Category category = new Category();
    static PowerType powerType = new PowerType();
    static Status status = new Status();
    static Location location = new Location();
    static User user;
    static Tool tool;

    @Mock
    public UserRepository userRepository;
    @Mock
    public ToolRepository toolRepository;
    @InjectMocks
    public RentalDtoMapper rentalDtoMapper;

    @BeforeAll
    static void setup() {
        manufacturer.setId(1L);
        manufacturer.setManufacturerName("Bosch");

        category.setId(20L);
        category.setCategoryName("Power tools");

        powerType.setId(300L);
        powerType.setPowerTypeName("230V");

        status.setId(4000L);
        status.setStatusName("Available");

        location.setId(50000L);
        location.setLocationName("Basement");

        user = new User(1L, "Pawel", "Skora",
                "123456789","pawel@email.org", 10,
                LocalDateTime.of(2010, 10, 10, 12, 25),
                null);

        tool = new Tool(1L, "Drill", "Dr100", manufacturer, category,
                powerType, status, 10, location, user,
                LocalDateTime.of(2020, 5, 15, 12, 12),
                null);
    }

    @Test
    void testMapFromRentalToRentalDtoResponse() {
        Rental rental = new Rental(1L,
                LocalDateTime.of(2020, 1, 10, 10, 10),
                LocalDateTime.of(2020, 2, 10, 15, 39),
                "OK", user, tool);
        RentalDtoResponse mapped = rentalDtoMapper.map(rental);

        assertEquals(mapped.getId(), rental.getId());
        assertEquals(mapped.getRented(), rental.getRented());
        assertEquals(mapped.getReturned(), rental.getReturned());
        assertEquals(mapped.getNotices(), rental.getNotices());
        assertEquals(mapped.getUser(), rental.getUser());
        assertEquals(mapped.getTool(), rental.getTool());

        assertNotEquals(mapped.getNotices(), "anyNotice");
        assertNotEquals(mapped.getId(), 4321L);
    }

    @Test
    void testMapFromRentalDtoRequestToRental() {
        RentalDtoRequest rentalDtoRequest = new RentalDtoRequest(1L, 1L,
                LocalDateTime.of(2020, 11, 15, 12, 34),
                LocalDateTime.of(2020, 12, 15, 17, 4),
                "OK");

        given(userRepository.findById(rentalDtoRequest.getUserId()))
                .willReturn(Optional.ofNullable(user));
        given(toolRepository.findById(rentalDtoRequest.getToolId()))
                .willReturn(Optional.ofNullable(tool));

        Rental mapped = rentalDtoMapper.map(rentalDtoRequest);
        assertEquals(mapped.getRented(), rentalDtoRequest.getRented());
        assertEquals(mapped.getReturned(), rentalDtoRequest.getReturned());
        assertEquals(mapped.getNotices(), rentalDtoRequest.getNotices());
        assertEquals(mapped.getUser().getId(), rentalDtoRequest.getUserId());
        assertEquals(mapped.getTool().getId(), rentalDtoRequest.getToolId());

        assertNotEquals(mapped.getNotices(), "anyNotice");
    }
}
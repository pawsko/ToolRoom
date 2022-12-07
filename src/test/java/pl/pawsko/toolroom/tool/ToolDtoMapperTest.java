package pl.pawsko.toolroom.tool;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.pawsko.toolroom.category.Category;
import pl.pawsko.toolroom.category.CategoryRepository;
import pl.pawsko.toolroom.location.Location;
import pl.pawsko.toolroom.location.LocationRepository;
import pl.pawsko.toolroom.manufacturer.Manufacturer;
import pl.pawsko.toolroom.manufacturer.ManufacturerRepository;
import pl.pawsko.toolroom.powertype.PowerType;
import pl.pawsko.toolroom.powertype.PowerTypeRepository;
import pl.pawsko.toolroom.status.Status;
import pl.pawsko.toolroom.status.StatusRepository;
import pl.pawsko.toolroom.user.User;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ToolDtoMapperTest {

    static Manufacturer manufacturer = new Manufacturer();
    static Category category = new Category();
    static PowerType powerType = new PowerType();
    static Status status = new Status();
    static Location location = new Location();

    static User user;

    @Mock
    ManufacturerRepository manufacturerRepository;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    PowerTypeRepository powerTypeRepository;

    @Mock
    StatusRepository statusRepository;

    @Mock
    LocationRepository locationRepository;

    @InjectMocks
    ToolDtoMapper toolDtoMapper;

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

        user = new User(1L, "Pawel", "Skora", "123456789",
                "pawel@email.org", 10, LocalDateTime.now(), null);
    }

    @Test
    void testMapFromToolToToolDtoResponse() {
        Tool tool = new Tool(1L, "Drill", "Dr100", manufacturer, category,
                powerType, status, 10, location, user, LocalDateTime.now(), null);
        ToolDtoResponse mapped = toolDtoMapper.map(tool);

        assertEquals(mapped.getId(), tool.getId());
        assertEquals(mapped.getName(), tool.getName());
        assertEquals(mapped.getModel(), tool.getModel());
        assertEquals(mapped.getManufacturer().getManufacturerName(), tool.getManufacturer().getManufacturerName());
        assertEquals(mapped.getCategory().getCategoryName(), tool.getCategory().getCategoryName());
        assertEquals(mapped.getPowerType().getPowerTypeName(), tool.getPowerType().getPowerTypeName());
        assertEquals(mapped.getStatus().getStatusName(), tool.getStatus().getStatusName());
        assertEquals(mapped.getLocation().getLocationName(), tool.getLocation().getLocationName());
        assertNotEquals(mapped.getName(), "anyTool");
        assertNotEquals(mapped.getId(), 4321L);
    }

    @Test
    void testMapFromToolDtoRequestToTool() {
        ToolDtoRequest toolDtoRequest = new ToolDtoRequest("Cutter", "Makita", 1L,
                20L, 300L, 4000L, 10, 50000L);
        given(manufacturerRepository.findById(toolDtoRequest.getManufacturerId()))
                .willReturn(Optional.ofNullable(manufacturer));
        given(categoryRepository.findById(toolDtoRequest.getCategoryId()))
                .willReturn(Optional.ofNullable(category));
        given(powerTypeRepository.findById(toolDtoRequest.getPowerTypeId()))
                .willReturn(Optional.ofNullable(powerType));
        given(statusRepository.findById(toolDtoRequest.getStatusId()))
                .willReturn(Optional.ofNullable(status));
        given(locationRepository.findById(toolDtoRequest.getLocationId()))
                .willReturn(Optional.ofNullable(location));

        Tool mapped = toolDtoMapper.map(toolDtoRequest);
        assertEquals(mapped.getName(), toolDtoRequest.getName());
        assertEquals(mapped.getModel(), toolDtoRequest.getModel());
        assertEquals(mapped.getManufacturer().getId(), toolDtoRequest.getManufacturerId());
        assertEquals(mapped.getCategory().getId(), toolDtoRequest.getCategoryId());
        assertEquals(mapped.getPowerType().getId(), toolDtoRequest.getPowerTypeId());
        assertEquals(mapped.getStatus().getId(), toolDtoRequest.getStatusId());
        assertEquals(mapped.getRating(), toolDtoRequest.getRating());
        assertEquals(mapped.getLocation().getId(), toolDtoRequest.getLocationId());
        assertNotEquals(mapped.getName(), "anyTool");
    }
}
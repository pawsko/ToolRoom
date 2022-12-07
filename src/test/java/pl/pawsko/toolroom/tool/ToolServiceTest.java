package pl.pawsko.toolroom.tool;

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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ToolServiceTest {

    private static Tool tool1;
    private static Tool tool2;
    private static ToolDtoResponse toolRes1;
    private static ToolDtoResponse toolRes2;
    private static ToolDtoRequest toolReq1;

    @Mock
    private ToolRepository toolRepository;

    @Mock
    private ToolDtoMapper toolDtoMapper;

    @InjectMocks
    private ToolService toolService;

    @BeforeAll
    public static void setup() {
        Manufacturer manufacturer1 = new Manufacturer();
        manufacturer1.setId(1L);
        manufacturer1.setManufacturerName("Makita");
        Category category1 = new Category();
        category1.setId(2L);
        category1.setCategoryName("Power tools");
        PowerType powerType1 = new PowerType();
        powerType1.setId(3L);
        powerType1.setPowerTypeName("230V");
        Status status1 = new Status();
        status1.setId(4L);
        status1.setStatusName("Rented");
        Location location1 =new Location();
        location1.setId(5L);
        location1.setLocationName("Garage");

        Manufacturer manufacturer2 = new Manufacturer();
        manufacturer2.setId(2L);
        manufacturer2.setManufacturerName("Makita");
        Category category2 = new Category();
        category2.setId(3L);
        category2.setCategoryName("Power tools");
        PowerType powerType2 = new PowerType();
        powerType2.setId(4L);
        powerType2.setPowerTypeName("230V");
        Status status2 = new Status();
        status2.setId(5L);
        status2.setStatusName("Rented");
        Location location2 =new Location();
        location2.setId(6L);
        location2.setLocationName("Garage");

        tool1 = new Tool(1L,"Drill", "DR123", manufacturer1, category1, powerType1,
                status1, 10, location1, null, LocalDateTime.now(), null);
        tool2 = new Tool(2L, "Cutter", "CT456", manufacturer2, category2, powerType2,
                status2, 5, location2, null, LocalDateTime.now(), null);
        toolRes1 = new ToolDtoResponse(1L,"Drill", "DR123", manufacturer1, category1, powerType1,
                status1, location1);
        toolRes2 = new ToolDtoResponse(2L, "Cutter", "CT456", manufacturer2, category2, powerType2,
                status2, location2);
        toolReq1 = new ToolDtoRequest("Drill", "DR123", 1L, 2L, 3L, 4L, 10, 5L);
    }

    @Test
    void shouldReturnTwoTools() {
        List<Tool> categories = List.of(tool1, tool2);
        List<ToolDtoResponse> categoriesDtoRes = List.of(toolRes1, toolRes2);

        given(toolRepository.findAll()).willReturn(categories);
        given(toolDtoMapper.map(tool1)).willReturn(toolRes1);
        given(toolDtoMapper.map(tool2)).willReturn(toolRes2);
        List<ToolDtoResponse> allToolsActual = toolService.getAllTools();
        assertNotEquals(categoriesDtoRes.get(0), allToolsActual.get(1));
        assertIterableEquals(categoriesDtoRes, allToolsActual);
        verify(toolRepository).findAll();
        verify(toolDtoMapper).map(tool1);
        verify(toolDtoMapper).map(tool2);
    }

    @Test
    void shouldReturnOneTool() {
        given(toolRepository.findById(1L)).willReturn(Optional.of(tool1));
        given(toolDtoMapper.map(tool1)).willReturn(toolRes1);
        Optional<ToolDtoResponse> toolByIdActual = toolService.getToolById(1L);
        assertEquals(Optional.of(toolRes1), toolByIdActual);
        assertNotEquals(Optional.of(toolRes2), toolByIdActual);
        verify(toolRepository).findById(1L);
        verify(toolDtoMapper).map(tool1);
    }

    @Test
    void shouldSaveTool() {
        given(toolDtoMapper.map(toolReq1)).willReturn(tool1);
        given(toolRepository.save(tool1)).willReturn(tool1);
        given((toolDtoMapper.map(tool1))).willReturn(toolRes1);
        ToolDtoResponse toolDtoResponseActual = toolService.saveTool(toolReq1);
        assertEquals(toolRes1, toolDtoResponseActual);
        assertNotEquals(toolRes2, toolDtoResponseActual);
        verify(toolRepository).save(tool1);
        verify(toolDtoMapper).map(tool1);
        verify(toolDtoMapper).map(toolReq1);
    }

    @Test
    void shouldReplaceToolWhenIdExists() {
        given(toolDtoMapper.map(toolReq1)).willReturn(tool1);
        given(toolRepository.save(tool1)).willReturn(tool1);
        given(toolDtoMapper.map(tool1)).willReturn(toolRes1);
        given(toolRepository.existsById(1L)).willReturn(true);
        Optional<ToolDtoResponse> toolDtoResponseActual = toolService.replaceTool(1L, toolReq1);
        assertEquals(Optional.of(toolRes1), toolDtoResponseActual);

        verify(toolRepository).save(tool1);
        verify(toolDtoMapper).map(tool1);
        verify(toolDtoMapper).map(toolReq1);
    }

    @Test
    void shouldReplaceEmptyOptionalWhenIdDoesNotExists() {
        given(toolRepository.existsById(3L)).willReturn(false);
        Optional<ToolDtoResponse> toolDtoResponseActual = toolService.replaceTool(3L, toolReq1);
        assertEquals(Optional.empty(), toolDtoResponseActual);
    }
}
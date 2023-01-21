package pl.pawsko.toolroom.tool;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(value = ToolController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc
class ToolControllerTest {
    private static Tool tool1;
    private static Tool tool2;
    private static ToolDtoResponse toolRes1;
    private static ToolDtoResponse toolRes2;
    private static ToolDtoRequest toolReq1;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ToolService toolService;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private LocationRepository locationRepository;

    @MockBean
    private ManufacturerRepository manufacturerRepository;

    @MockBean
    private PowerTypeRepository powerTypeRepository;

    @MockBean
    private StatusRepository statusRepository;

    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
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
        Location location1 = new Location();
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
        Location location2 = new Location();
        location2.setId(6L);
        location2.setLocationName("Garage");

        tool1 = new Tool(1L, "Drill", "DR123", manufacturer1, category1, powerType1,
                status1, 10, location1, null, LocalDateTime.now(), null);
        tool2 = new Tool(2L, "Cutter", "CT456", manufacturer2, category2, powerType2,
                status2, 5, location2, null, LocalDateTime.now(), null);
        toolRes1 = new ToolDtoResponse(1L, "Drill", "DR123", manufacturer1, category1, powerType1,
                status1, location1);
        toolRes2 = new ToolDtoResponse(2L, "Cutter", "CT456", manufacturer2, category2, powerType2,
                status2, location2);
        toolReq1 = new ToolDtoRequest("Drill", "DR123", 1L, 2L, 3L, 4L,
                10, 5L);
    }

    @Test
    void whenGetAllTools_thenReturnJsonArray() throws Exception {
        List<ToolDtoResponse> allTools = Arrays.asList(toolRes1, toolRes2);
        given(toolService.getAllTools()).willReturn(allTools);

        mockMvc.perform(get("/api/tool")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(tool1.getName())))
                .andExpect(jsonPath("$[1].name", is(tool2.getName())));

        verify(toolService).getAllTools();
    }

    @Test
    void whenGetToolById_thenReturnValidRequest() throws Exception {
        given(toolService.getToolById(anyLong())).willReturn(Optional.of(toolRes1));

        mockMvc.perform(get("/api/tool/{id}", anyLong())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(toolRes1.getName())));

        verify(toolService).getToolById(anyLong());
    }

    @Test
    void whenGetByIdButBadType_thenReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/tool/{id}", "stringInsteadLong")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        verifyNoInteractions(toolService);
    }

    @Test
    void whenGetToolByIdButNotExisted_thenReturnNotFound() throws Exception {
        given(toolService.getToolById(anyLong())).willReturn(Optional.empty());

        mockMvc.perform(get("/api/tool/{id}", anyLong())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(toolService).getToolById(anyLong());
    }

    @Test
    void whenSaveTool_thenReturnJson() {
        given(toolService.saveTool(eq(toolReq1))).willReturn(toolRes1);
        given(categoryRepository.existsById(anyLong())).willReturn(true);
        given(locationRepository.existsById(anyLong())).willReturn(true);
        given(manufacturerRepository.existsById(anyLong())).willReturn(true);
        given(powerTypeRepository.existsById(anyLong())).willReturn(true);
        given(statusRepository.existsById(anyLong())).willReturn(true);

        try {
            mockMvc.perform(post("/api/tool")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(toolReq1)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/api/tool/1"))
                    .andExpect(jsonPath("$.name", is(toolReq1.getName())));
        } catch (Exception e) {
            assertThat(e).isInstanceOf(MethodArgumentNotValidException.class);
        }

        verify(toolService).saveTool(toolReq1);
    }

    @Test
    void whenReplaceTool_thenStatusOk() throws Exception {
        given(toolService.replaceTool(anyLong(), eq(toolReq1))).willReturn(Optional.of(toolRes1));
        given(categoryRepository.existsById(anyLong())).willReturn(true);
        given(locationRepository.existsById(anyLong())).willReturn(true);
        given(manufacturerRepository.existsById(anyLong())).willReturn(true);
        given(powerTypeRepository.existsById(anyLong())).willReturn(true);
        given(statusRepository.existsById(anyLong())).willReturn(true);

        mockMvc.perform(put("/api/tool/{id}", 100L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(toolReq1)))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(toolService).replaceTool(anyLong(), eq(toolReq1));
    }

    @Test
    void whenReplaceToolNonExistingId_thenReturnNotFound() throws Exception {
        given(toolService.replaceTool(anyLong(), eq(toolReq1))).willReturn(Optional.empty());
        given(categoryRepository.existsById(anyLong())).willReturn(true);
        given(locationRepository.existsById(anyLong())).willReturn(true);
        given(manufacturerRepository.existsById(anyLong())).willReturn(true);
        given(powerTypeRepository.existsById(anyLong())).willReturn(true);
        given(statusRepository.existsById(anyLong())).willReturn(true);

        mockMvc.perform(put("/api/tool/{id}", 100L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(toolReq1)))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(toolService).replaceTool(anyLong(), eq(toolReq1));
    }

    @Test
    void whenReplaceTooShortToolName_thenReturnBadRequest() throws Exception {
        toolReq1.setName("P");
        given(toolService.replaceTool(anyLong(), eq(toolReq1))).willReturn(Optional.empty());
        given(categoryRepository.existsById(anyLong())).willReturn(true);
        given(locationRepository.existsById(anyLong())).willReturn(true);
        given(manufacturerRepository.existsById(anyLong())).willReturn(true);
        given(powerTypeRepository.existsById(anyLong())).willReturn(true);
        given(statusRepository.existsById(anyLong())).willReturn(true);

        mockMvc.perform(put("/api/tool/{id}", 100L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(toolReq1)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        verifyNoInteractions(toolService);
    }

    @Test
    void whenReplaceToolNull_thenReturnBadRequest() throws Exception {
        ToolDtoRequest toolReq = new ToolDtoRequest();

        mockMvc.perform(put("/api/tool/{id}", 100L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(toolReq)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        verifyNoInteractions(toolService);
    }
}
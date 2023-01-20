package pl.pawsko.toolroom.rental;

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
import pl.pawsko.toolroom.location.Location;
import pl.pawsko.toolroom.manufacturer.Manufacturer;
import pl.pawsko.toolroom.powertype.PowerType;
import pl.pawsko.toolroom.status.Status;
import pl.pawsko.toolroom.tool.Tool;
import pl.pawsko.toolroom.tool.ToolRepository;
import pl.pawsko.toolroom.user.User;
import pl.pawsko.toolroom.user.UserRepository;

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
@WebMvcTest(value = RentalController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc
class RentalControllerTest {
    private static Rental rental1;
    private static Rental rental2;
    private static RentalDtoResponse rentalRes1;
    private static RentalDtoResponse rentalRes2;
    private static RentalDtoRequest rentalReq1;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RentalService rentalService;
    @MockBean
    private RentalRepository rentalRepository;
    @MockBean
    private ToolRepository toolRepository;
    @MockBean
    private UserRepository userRepository;
    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setId(1L);
        manufacturer.setManufacturerName("Bosch");

        Category category = new Category();
        category.setId(20L);
        category.setCategoryName("Power rentals");

        PowerType powerType = new PowerType();
        powerType.setId(300L);
        powerType.setPowerTypeName("230V");

        Status status = new Status();
        status.setId(4000L);
        status.setStatusName("Available");

        Location location = new Location();
        location.setId(50000L);
        location.setLocationName("Basement");

        User user = new User(1L, "Pawel", "Skora",
                "123456789", "pawel@email.org", 10,
                LocalDateTime.of(2010, 10, 10, 12, 25),
                null);

        Tool tool = new Tool(1L, "Drill", "Dr100", manufacturer, category,
                powerType, status, 10, location, user,
                LocalDateTime.of(2020, 5, 15, 12, 12, 11),
                null);
        rental1 = new Rental(1L,
                LocalDateTime.of(2020, 1, 10, 10, 10, 12),
                LocalDateTime.of(2020, 2, 10, 15, 39, 13),
                "Not OK", user, tool);
        rental2 = new Rental(2L,
                LocalDateTime.of(2021, 3, 10, 10, 10, 14),
                LocalDateTime.of(2021, 4, 10, 15, 39, 15),
                "OK", user, tool);
        rentalRes1 = new RentalDtoResponse(1L, tool, user,
                LocalDateTime.of(2020, 1, 10, 10, 10, 12),
                LocalDateTime.of(2020, 2, 10, 15, 39, 13),
                "Not OK");
        rentalRes2 = new RentalDtoResponse(2L, tool, user,
                LocalDateTime.of(2021, 3, 10, 10, 10, 14),
                LocalDateTime.of(2021, 4, 10, 15, 39, 15),
                "OK");
        rentalReq1 = new RentalDtoRequest(1L, 1L,
                LocalDateTime.of(2020, 1, 10, 10, 10, 12),
                LocalDateTime.of(2020, 2, 10, 15, 39, 13),
                "Not OK");

        mapper.findAndRegisterModules();
    }

    @Test
    void whenGetAllRentals_thenReturnJsonArray() throws Exception {
        List<RentalDtoResponse> allRentals = Arrays.asList(rentalRes1, rentalRes2);
        given(rentalService.getAllRentals()).willReturn(allRentals);
        mockMvc.perform(get("/api/rental")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].rented", is(rental1.getRented().toString())))
                .andExpect(jsonPath("$[1].rented", is(rental2.getRented().toString())));

        verify(rentalService).getAllRentals();
    }

    @Test
    void whenGetRentalById_thenReturnValidRequest() throws Exception {
        given(rentalService.getRentalById(anyLong())).willReturn(Optional.of(rentalRes1));

        mockMvc.perform(get("/api/rental/{id}", anyLong())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.returned", is(rentalRes1.getReturned().toString())));

        verify(rentalService).getRentalById(anyLong());
    }

    @Test
    void whenGetByIdButBadType_thenReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/rental/{id}", "stringInsteadLong")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        verifyNoInteractions(rentalService);
    }

    @Test
    void whenGetRentalByIdButNotExisted_thenReturnNotFound() throws Exception {
        given(rentalService.getRentalById(anyLong())).willReturn(Optional.empty());

        mockMvc.perform(get("/api/rental/{id}", anyLong())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(rentalService).getRentalById(anyLong());
    }

    @Test
    void whenSaveRental_thenReturnJson() {
        given(rentalService.saveRental(eq(rentalReq1))).willReturn(rentalRes1);
        given(rentalRepository.existsById(anyLong())).willReturn(true);
        given(toolRepository.existsById(anyLong())).willReturn(true);
        given(userRepository.existsById(anyLong())).willReturn(true);

        try {
            mockMvc.perform(post("/api/rental")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(rentalReq1)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/api/rental/1"))
                    .andExpect(jsonPath("$.rented", is(rentalReq1.getRented().toString())));
        } catch (Exception e) {
            assertThat(e).isInstanceOf(MethodArgumentNotValidException.class);
        }

        verify(rentalService).saveRental(rentalReq1);
    }

    @Test
    void whenReplaceRental_thenStatusOk() throws Exception {
        given(rentalService.replaceRental(anyLong(), eq(rentalReq1))).willReturn(Optional.of(rentalRes1));
        given(rentalRepository.existsById(anyLong())).willReturn(true);
        given(toolRepository.existsById(anyLong())).willReturn(true);
        given(userRepository.existsById(anyLong())).willReturn(true);

        mockMvc.perform(put("/api/rental/{id}", 100L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(rentalReq1)))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(rentalService).replaceRental(anyLong(), eq(rentalReq1));
    }

    @Test
    void whenReplaceRentalNonExistingId_thenReturnNotFound() throws Exception {
        given(rentalService.replaceRental(anyLong(), eq(rentalReq1))).willReturn(Optional.empty());
        given(rentalRepository.existsById(anyLong())).willReturn(true);
        given(toolRepository.existsById(anyLong())).willReturn(true);
        given(userRepository.existsById(anyLong())).willReturn(true);

        mockMvc.perform(put("/api/rental/{id}", 100L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(rentalReq1)))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(rentalService).replaceRental(anyLong(), eq(rentalReq1));
    }

    @Test
    void whenReplaceRentalWithNoTExistedTool_thenReturnBadRequest() throws Exception {
        given(rentalService.replaceRental(anyLong(), eq(rentalReq1))).willReturn(Optional.empty());
        given(rentalRepository.existsById(anyLong())).willReturn(true);
        given(toolRepository.existsById(anyLong())).willReturn(false);
        given(userRepository.existsById(anyLong())).willReturn(true);

        mockMvc.perform(put("/api/rental/{id}", 100L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(rentalReq1)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        verifyNoInteractions(rentalService);
    }

    @Test
    void whenReplaceRentalWithNoTExistedUser_thenReturnBadRequest() throws Exception {
        given(rentalService.replaceRental(anyLong(), eq(rentalReq1))).willReturn(Optional.empty());
        given(rentalRepository.existsById(anyLong())).willReturn(true);
        given(toolRepository.existsById(anyLong())).willReturn(true);
        given(userRepository.existsById(anyLong())).willReturn(false);

        mockMvc.perform(put("/api/rental/{id}", 100L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(rentalReq1)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        verifyNoInteractions(rentalService);
    }

    @Test
    void whenReplaceRentalNull_thenReturnBadRequest() throws Exception {
        RentalDtoRequest rentalReq = new RentalDtoRequest();

        mockMvc.perform(put("/api/rental/{id}", 100L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(rentalReq)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        verifyNoInteractions(rentalService);
    }
}
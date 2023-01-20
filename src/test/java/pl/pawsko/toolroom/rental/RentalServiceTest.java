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
import pl.pawsko.toolroom.user.User;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    private static Rental rental1;
    private static Rental rental2;
    private static RentalDtoResponse rentalRes1;
    private static RentalDtoResponse rentalRes2;
    private static RentalDtoRequest rentalReq1;

    @Mock RentalRepository rentalRepository;
    @Mock
    private RentalDtoMapper rentalDtoMapper;
    @InjectMocks
    private RentalService rentalService;

    @BeforeAll
    public static void setup() {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setId(1L);
        manufacturer.setManufacturerName("Bosch");
        Category category = new Category();
        category.setId(20L);
        category.setCategoryName("Power tools");

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
                LocalDateTime.of(2020, 5, 15, 12, 12),
                null);
        rental1 = new Rental(1L,
                LocalDateTime.of(2020, 1, 10, 10, 10),
                LocalDateTime.of(2020, 2, 10, 15, 39),
                "OK", user, tool);
        rental2 = new Rental(2L,
                LocalDateTime.of(2021, 3, 10, 10, 10),
                LocalDateTime.of(2021, 4, 10, 15, 39),
                "OK", user, tool);
        rentalRes1 = new RentalDtoResponse(1L, tool, user,
                LocalDateTime.of(2020, 1, 10, 10, 10),
                LocalDateTime.of(2020, 2, 10, 15, 39),
                "OK");
        rentalRes2 = new RentalDtoResponse(2L, tool, user, LocalDateTime.of(2021, 3, 10, 10, 10),
                LocalDateTime.of(2021, 4, 10, 15, 39),
                "OK");


        rentalReq1 = new RentalDtoRequest(1L, 1L,
                LocalDateTime.of(2020, 11, 15, 12, 34),
                LocalDateTime.of(2020, 12, 15, 17, 4),
                "OK");
    }

    @Test
    void shouldReturnTwoRentals() {
        List<Rental> categories = List.of(rental1, rental2);
        List<RentalDtoResponse> categoriesDtoRes = List.of(rentalRes1, rentalRes2);

        given(rentalRepository.findAll()).willReturn(categories);
        given(rentalDtoMapper.map(rental1)).willReturn(rentalRes1);
        given(rentalDtoMapper.map(rental2)).willReturn(rentalRes2);

        List<RentalDtoResponse> allRentalsActual = rentalService.getAllRentals();

        assertNotEquals(categoriesDtoRes.get(0), allRentalsActual.get(1));
        assertIterableEquals(categoriesDtoRes, allRentalsActual);
        verify(rentalRepository).findAll();
        verify(rentalDtoMapper).map(rental1);
        verify(rentalDtoMapper).map(rental2);
    }

    @Test
    void shouldReturnOneRental() {
        given(rentalRepository.findById(1L)).willReturn(Optional.of(rental1));
        given(rentalDtoMapper.map(rental1)).willReturn(rentalRes1);

        Optional<RentalDtoResponse> rentalByIdActual = rentalService.getRentalById(1L);

        assertEquals(Optional.of(rentalRes1), rentalByIdActual);
        assertNotEquals(Optional.of(rentalRes2), rentalByIdActual);
        verify(rentalRepository).findById(1L);
        verify(rentalDtoMapper).map(rental1);
    }

    @Test
    void shouldSaveRental() {
        given(rentalDtoMapper.map(rentalReq1)).willReturn(rental1);
        given(rentalRepository.save(rental1)).willReturn(rental1);
        given((rentalDtoMapper.map(rental1))).willReturn(rentalRes1);

        RentalDtoResponse rentalDtoResponseActual = rentalService.saveRental(rentalReq1);

        assertEquals(rentalRes1, rentalDtoResponseActual);
        assertNotEquals(rentalRes2, rentalDtoResponseActual);
        verify(rentalRepository).save(rental1);
        verify(rentalDtoMapper).map(rental1);
        verify(rentalDtoMapper).map(rentalReq1);
    }

    @Test
    void shouldReplaceRentalWhenIdExists() {
        given(rentalDtoMapper.map(rentalReq1)).willReturn(rental1);
        given(rentalRepository.save(rental1)).willReturn(rental1);
        given(rentalDtoMapper.map(rental1)).willReturn(rentalRes1);
        given(rentalRepository.existsById(1L)).willReturn(true);

        Optional<RentalDtoResponse> rentalDtoResponseActual = rentalService.replaceRental(1L, rentalReq1);

        assertEquals(Optional.of(rentalRes1), rentalDtoResponseActual);
        verify(rentalRepository).save(rental1);
        verify(rentalDtoMapper).map(rental1);
        verify(rentalDtoMapper).map(rentalReq1);
    }

    @Test
    void shouldReplaceEmptyOptionalWhenIdDoesNotExists() {
        given(rentalRepository.existsById(3L)).willReturn(false);

        Optional<RentalDtoResponse> rentalDtoResponseActual = rentalService.replaceRental(3L, rentalReq1);

        assertEquals(Optional.empty(), rentalDtoResponseActual);
    }
}
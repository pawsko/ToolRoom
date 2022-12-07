package pl.pawsko.toolroom.user;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static User user1;
    private static User user2;
    private static UserDtoResponse userRes1;
    private static UserDtoResponse userRes2;
    private static UserDtoRequest userReq1;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDtoMapper userDtoMapper;

    @InjectMocks
    private UserService userService;

    @BeforeAll
    public static void setup() {
        user1 = new User(1L,"Pawel", "Skora", "123456789",
                "pawel@email.org", 10, null, null);
        user2 = new User(2L, "Adam", "Mickiewicz", "123123123",
                "am@email.com", 1, null, null);
        userRes1 = new UserDtoResponse(1L,"Pawel", "Skora", "123456789",
                "pawel@email.org");
        userRes2 = new UserDtoResponse(2L, "Adam", "Mickiewicz", "123123123",
                "am@email.com");
        userReq1 = new UserDtoRequest("Pawel", "Skora", "123456789",
                "pawel@email.org", 10);
    }

    @Test
    void shouldReturnTwoUsers() {
        List<User> categories = List.of(user1, user2);
        List<UserDtoResponse> categoriesDtoRes = List.of(userRes1, userRes2);

        given(userRepository.findAll()).willReturn(categories);
        given(userDtoMapper.map(user1)).willReturn(userRes1);
        given(userDtoMapper.map(user2)).willReturn(userRes2);
        List<UserDtoResponse> allUsersActual = userService.getAllUsers();
        assertNotEquals(categoriesDtoRes.get(0), allUsersActual.get(1));
        assertIterableEquals(categoriesDtoRes, allUsersActual);
        verify(userRepository).findAll();
        verify(userDtoMapper).map(user1);
        verify(userDtoMapper).map(user2);
    }

    @Test
    void shouldReturnOneUser() {
        given(userRepository.findById(1L)).willReturn(Optional.of(user1));
        given(userDtoMapper.map(user1)).willReturn(userRes1);
        Optional<UserDtoResponse> userByIdActual = userService.getUserById(1L);
        assertEquals(Optional.of(userRes1), userByIdActual);
        assertNotEquals(Optional.of(userRes2), userByIdActual);
        verify(userRepository).findById(1L);
        verify(userDtoMapper).map(user1);
    }

    @Test
    void shouldSaveUser() {
        given(userDtoMapper.map(userReq1)).willReturn(user1);
        given(userRepository.save(user1)).willReturn(user1);
        given((userDtoMapper.map(user1))).willReturn(userRes1);
        UserDtoResponse userDtoResponseActual = userService.saveUser(userReq1);
        assertEquals(userRes1, userDtoResponseActual);
        assertNotEquals(userRes2, userDtoResponseActual);
        verify(userRepository).save(user1);
        verify(userDtoMapper).map(user1);
        verify(userDtoMapper).map(userReq1);
    }

    @Test
    void shouldReplaceUserWhenIdExists() {
        given(userDtoMapper.map(userReq1)).willReturn(user1);
        given(userRepository.save(user1)).willReturn(user1);
        given(userDtoMapper.map(user1)).willReturn(userRes1);
        given(userRepository.existsById(1L)).willReturn(true);
        Optional<UserDtoResponse> userDtoResponseActual = userService.replaceUser(1L, userReq1);
        assertEquals(Optional.of(userRes1), userDtoResponseActual);

        verify(userRepository).save(user1);
        verify(userDtoMapper).map(user1);
        verify(userDtoMapper).map(userReq1);
    }

    @Test
    void shouldReplaceEmptyOptionalWhenIdDoesNotExists() {
        given(userRepository.existsById(3L)).willReturn(false);
        Optional<UserDtoResponse> userDtoResponseActual = userService.replaceUser(3L, userReq1);
        assertEquals(Optional.empty(), userDtoResponseActual);
    }
}
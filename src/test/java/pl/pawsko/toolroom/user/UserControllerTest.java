package pl.pawsko.toolroom.user;

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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(value = UserController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc
class UserControllerTest {

    private static User user1;
    private static User user2;
    private static UserDtoResponse userRes1;
    private static UserDtoResponse userRes2;
    private static UserDtoRequest userReq1;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        user1 = new User(1L, "Pawel", "Skora", "123456789",
                "pawel@email.org", 10, null, null);
        user2 = new User(2L, "Adam", "Mickiewicz", "123123123",
                "am@email.com", 1, null, null);
        userRes1 = new UserDtoResponse(1L, "Pawel", "Skora", "123456789",
                "pawel@email.org");
        userRes2 = new UserDtoResponse(2L, "Adam", "Mickiewicz", "123123123",
                "am@email.com");
        userReq1 = new UserDtoRequest("Pawel", "Skora", "123456789",
                "pawel@email.org", 10);
    }
    @Test
    void whenGetAllUsers_thenReturnJsonArray() throws Exception {

        System.out.println("test1");
        List<UserDtoResponse> allUsers = Arrays.asList(userRes1, userRes2);

        given(userService.getAllUsers()).willReturn(allUsers);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is(user1.getFirstName())))
                .andExpect(jsonPath("$[1].firstName", is(user2.getFirstName())));

        verify(userService).getAllUsers();
    }

    @Test
    void whenGetUserById_thenReturnValidRequest() throws Exception {

        given(userService.getUserById(anyLong())).willReturn(Optional.of(userRes1));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", anyLong())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(userRes1.getFirstName())));

        verify(userService).getUserById(anyLong());
    }

    @Test
    void whenGetByIdButBadType_thenReturnBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", "stringInsteadLong")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        verifyNoInteractions(userService);
    }

    @Test
    void whenGetUserByIdButNotExisted_thenReturnNotFound() throws Exception {
        given(userService.getUserById(anyLong())).willReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", anyLong())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(userService).getUserById(anyLong());
    }

    @Test
    void whenSaveUser_thenReturnJson() {

        given(userService.saveUser(userReq1)).willReturn(userRes1);

        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(userReq1)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/api/user/1"))
                    .andExpect(jsonPath("$.firstName", is(userReq1.getFirstName())));
        } catch (Exception e) {
            assertThat(e).isInstanceOf(MethodArgumentNotValidException.class);
        }

        verify(userService).saveUser(userReq1);
    }

    @Test
    void whenReplaceUser_thenStatusOk() throws Exception {

        given(userService.replaceUser(anyLong(), eq(userReq1))).willReturn(Optional.of(userRes1));

        mockMvc.perform(put("/api/user/{id}", 100L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userReq1)))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(userService).replaceUser(anyLong(), eq(userReq1));
    }

    @Test
    void whenReplaceUserNonExistingId_thenReturnNotFound() throws Exception {

        given(userService.replaceUser(anyLong(), eq(userReq1))).willReturn(Optional.empty());

        mockMvc.perform(put("/api/user/{id}", 100L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userReq1)))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(userService).replaceUser(anyLong(), eq(userReq1));
    }

    @Test
    void whenReplaceTooShortUserName_thenReturnBadRequest() throws Exception {

        userReq1.setFirstName("P");

        mockMvc.perform(put("/api/user/{id}", 100L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userReq1)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        verifyNoInteractions(userService);
    }

    @Test
    void whenReplaceUserNull_thenReturnBadRequest() throws Exception {
        UserDtoRequest userReq = new UserDtoRequest();

        mockMvc.perform(put("/api/user/{id}", 100L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userReq)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        verifyNoInteractions(userService);
    }
}
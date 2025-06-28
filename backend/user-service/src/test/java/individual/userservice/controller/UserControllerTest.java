package individual.userservice.controller;

import individual.userservice.business.user.*;
import individual.userservice.business.user.exception.EmailAlreadyInUseException;
import individual.userservice.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import individual.userservice.business.user.exception.UserNotFoundException;
import individual.userservice.domain.enums.UserRole;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateUserUseCase createUserUseCase;

    @MockitoBean
    private GetUserUseCase getUserUseCase;

    @MockitoBean
    private GetUsersUseCase getUsersUseCase;

    @MockitoBean
    private UpdateUserUseCase updateUserUseCase;

    @MockitoBean
    private DeactivateUserUseCase deactivateUserUseCase;

    @InjectMocks
    private UserController userController;


    @Test
    void getUser_shouldReturn200WithUser_whenUserExists() throws Exception {
        User user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .profilePicture(null)
                .joinDate(LocalDate.now())
                .role(UserRole.USER)
                .build();

        when(getUserUseCase.getUser(1L)).thenReturn(user);

        mockMvc.perform(get("/users/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    void getUser_shouldReturn404_whenUserNotFound() throws Exception {
        when(getUserUseCase.getUser(1L)).thenThrow(new UserNotFoundException());

        mockMvc.perform(get("/users/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getUser_shouldReturn500_whenUnexpectedErrorOccurs() throws Exception {
        when(getUserUseCase.getUser(1L)).thenThrow(new RuntimeException("Something went wrong"));

        mockMvc.perform(get("/users/1"))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getUsers_shouldReturn200WithUserList_whenAdminRoleProvided() throws Exception {
        List<User> users = List.of(
                User.builder()
                        .id(1L)
                        .name("Alice")
                        .email("alice@example.com")
                        .profilePicture(null)
                        .joinDate(LocalDate.now())
                        .role(UserRole.USER)
                        .isDeleted(false)
                        .deletionDate(null)
                        .build(),
                User.builder()
                        .id(2L)
                        .name("Bob")
                        .email("bob@example.com")
                        .profilePicture(null)
                        .joinDate(LocalDate.now())
                        .role(UserRole.USER)
                        .isDeleted(false)
                        .deletionDate(null)
                        .build()
        );

        when(getUsersUseCase.getUsers()).thenReturn(users);

        mockMvc.perform(get("/users")
                        .header("X-Role", "ADMIN"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.users").isArray())
                .andExpect(jsonPath("$.users.length()").value(2))
                .andExpect(jsonPath("$.users[0].name").value("Alice"))
                .andExpect(jsonPath("$.users[1].email").value("bob@example.com"));
    }

    @Test
    void getUsers_shouldReturn403_whenRoleIsNotAdmin() throws Exception {
        mockMvc.perform(get("/users")
                        .header("X-Role", "USER"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void getUsers_shouldReturn500_whenExceptionOccurs() throws Exception {
        when(getUsersUseCase.getUsers()).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/users")
                        .header("X-Role", "ADMIN"))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createUser_shouldReturn201WithUserId_whenUserCreatedSuccessfully() throws Exception {
        when(createUserUseCase.createUser(any(User.class), any(String.class))).thenReturn(1L);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "name": "test",
                            "email": "test@example.com",
                            "password": "password123"
                        }
                    """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.message").doesNotExist());
    }


    @Test
    void createUser_shouldReturn400WithMessage_whenEmailAlreadyInUse() throws Exception {
        when(createUserUseCase.createUser(any(User.class), anyString()))
                .thenThrow(EmailAlreadyInUseException.class);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "name": "test",
                            "email": "test@example.com",
                            "password": "password123"
                        }
                    """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.message").value("The email address is already in use."));
    }

    @Test
    void createUser_shouldReturn500_whenUnexpectedErrorOccurs() throws Exception {
        when(createUserUseCase.createUser(any(User.class), anyString()))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "name": "test",
                            "email": "test@example.com",
                            "password": "password123"
                        }
                    """))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    void updateUser_shouldReturn200_whenUpdateIsSuccessful() throws Exception {
        doNothing().when(updateUserUseCase).updateUser(eq(1L), any(User.class));

        mockMvc.perform(put("/users/1")
                        .header("X-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "name": "Updated User",
                                "email": "updated@example.com",
                                "profilePicture": null
                            }
                            """))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void updateUser_shouldReturn401_whenUserIdMismatch() throws Exception {
        mockMvc.perform(put("/users/1")
                        .header("X-User-Id", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "name": "Updated User",
                                "email": "updated@example.com",
                                "profilePicture": "dXBkYXRlZA=="
                            }
                            """))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateUser_shouldReturn404_whenUserNotFound() throws Exception {
        doThrow(new UserNotFoundException()).when(updateUserUseCase).updateUser(eq(1L), any(User.class));

        mockMvc.perform(put("/users/1")
                        .header("X-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "name": "Updated User",
                                "email": "updated@example.com",
                                "profilePicture": "dXBkYXRlZA=="
                            }
                            """))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUser_shouldReturn500_whenUnexpectedErrorOccurs() throws Exception {
        doThrow(new RuntimeException("Unexpected")).when(updateUserUseCase).updateUser(eq(1L), any(User.class));

        mockMvc.perform(put("/users/1")
                        .header("X-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "name": "Updated User",
                                "email": "updated@example.com",
                                "profilePicture": "dXBkYXRlZA=="
                            }
                            """))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deactivateUser_shouldReturn200_whenDeactivationSuccessful() throws Exception {
        doNothing().when(deactivateUserUseCase).deactivateUser(1L);

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .delete("/users/1")
                                .header("X-User-Id", "1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deactivateUser_shouldReturn401_whenUserIdMismatch() throws Exception {
        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .delete("/users/1")
                                .header("X-User-Id", "99"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deactivateUser_shouldReturn404_whenUserNotFound() throws Exception {
        doThrow(new UserNotFoundException()).when(deactivateUserUseCase).deactivateUser(1L);

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .delete("/users/1")
                                .header("X-User-Id", "1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deactivateUser_shouldReturn500_whenUnexpectedErrorOccurs() throws Exception {
        doThrow(new RuntimeException("Unexpected error")).when(deactivateUserUseCase).deactivateUser(1L);

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .delete("/users/1")
                                .header("X-User-Id", "1"))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }
}

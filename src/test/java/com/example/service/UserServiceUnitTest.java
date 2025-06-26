package com.example.service;

import com.example.dto.request.LoginDTO;
import com.example.dto.request.UserRegistrationDTO;
import com.example.dto.response.AuthResponseDTO;
import com.example.dto.response.UserProfileDTO;
import com.example.exception.BadRequestException;
import com.example.mapper.UserMapper;
import com.example.model.*;
import com.example.repository.*;
import com.example.security.TokenProvider;
import com.example.security.UserPrincipal;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceUnitTest {

    @Mock private UserRepository userRepository;
    @Mock private StudentRepository studentRepository;
    @Mock private TeacherRepository teacherRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private UserMapper userMapper;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private TokenProvider tokenProvider;

    @InjectMocks private UserServiceImpl userService;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    /*User Story 01*/
    @Test
    @DisplayName("CP01 – Rechazar registro si el email ya existe")
    void registerStudent_duplicateEmail_throwsException() {
        UserRegistrationDTO request = new UserRegistrationDTO();
        request.setEmail("tATEmcraeisthebest@gmail.com");
        when(userRepository.existsByEmail("tATEmcraeisthebest@gmail.com")).thenReturn(true);
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> userService.registerStudent(request)
        );
        assertEquals("El email ya esta registrado", exception.getMessage());
    }

    @Test
    @DisplayName("CP02 – Registrar con un usuario duplicado")
    void registerStudent_fullDuplicate_throwsBadRequest() {
        UserRegistrationDTO request = new UserRegistrationDTO();
        request.setEmail("lkdsjfaskld@gmail.com");
        request.setName("Eldu");
        request.setLastName("Plicado");

        when(userRepository.existsByEmail("lkdsjfaskld@gmail.com")).thenReturn(true);
        when(studentRepository.existsByNameAndLastName("Eldu", "Plicado")).thenReturn(true);

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> userService.registerStudent(request)
        );
        assertEquals("Ya existe un usuario con el mismo nombre, apellido y email", exception.getMessage());
    }

    @Test
    @DisplayName("CP03 – Devolver AccountResponse con id, email y role")
    void registerStudent_success_returnsProfile() {
        UserRegistrationDTO request = new UserRegistrationDTO();
        request.setEmail("estudiante@gmail.com");
        request.setPassword("1234");
        request.setName("Checo");
        request.setLastName("Perez");

        Role studentRole = new Role();
        studentRole.setId(1);
        studentRole.setName(ERole.STUDENT);
        User entity = new User();
        entity.setEmail(request.getEmail());
        entity.setRole(studentRole);
        entity.setId(8);

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(studentRepository.existsByNameAndLastName(request.getName(), request.getLastName())).thenReturn(false);
        when(teacherRepository.existsByNameAndLastName(request.getName(), request.getLastName())).thenReturn(false);
        when(roleRepository.findByName(ERole.STUDENT)).thenReturn(Optional.of(studentRole));
        when(passwordEncoder.encode("1234")).thenReturn("12423412453462345");
        when(userMapper.toUserEntity(request)).thenReturn(entity);
        when(userRepository.save(entity)).thenReturn(entity);
        when(userMapper.toUserProfileDTO(entity)).thenReturn(
                new UserProfileDTO(){{
                    setId(8); setEmail(request.getEmail()); setRole(ERole.STUDENT);
                }}
        );

        UserProfileDTO profile = userService.registerStudent(request);
        assertNotNull(profile.getId());
        assertEquals("estudiante@gmail.com", profile.getEmail());
        assertEquals(ERole.STUDENT, profile.getRole());
    }

    @Test
    @DisplayName("CP04 – Nombre y apellido duplicados")
    void registerStudent_duplicateNameLastName_throwsBadRequest() {
        UserRegistrationDTO request = new UserRegistrationDTO();
        request.setEmail("tate@gmail.com");    // correo no repetido
        request.setPassword("1234");
        request.setName("Tate");
        request.setLastName("McRae");

        when(userRepository.existsByEmail("tate@gmail.com")).thenReturn(false);
        when(studentRepository.existsByNameAndLastName("Tate", "McRae")).thenReturn(true);

        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> userService.registerStudent(request)
        );
        assertEquals("Ya existe un usuario con el mismo nombre y apellido", ex.getMessage());
    }

    /*User story 2*/
    @Test
    @DisplayName("CP05 – Validar email y password correctos")
    void login_validCredentials_returnsToken() {
        LoginDTO request = new LoginDTO();
        request.setEmail("charlesleclerc@gmail.com");
        request.setPassword("ferrari");
        Role role = new Role();
        role.setName(ERole.STUDENT);
        User user = new User();
        user.setId(10);
        user.setEmail(request.getEmail());
        user.setRole(role);
        UserPrincipal principal = mock(UserPrincipal.class);
        when(principal.getUser()).thenReturn(user);

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(principal);
        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(tokenProvider.createAccessToken(auth)).thenReturn("ferrarimightbethebestsometime");
        when(userMapper.toAuthResponseDTO(user, "ferrarimightbethebestsometime")).thenReturn(
                new AuthResponseDTO(){{
                    setToken("ferrarimightbethebestsometime"); setName("Charles"); setLastName("Leclerc"); setRole("STUDENT");
                }}
        );

        AuthResponseDTO response = userService.login(request);
        assertEquals("ferrarimightbethebestsometime", response.getToken());
        assertEquals("STUDENT", response.getRole());
    }

    @Test
    @DisplayName("CP06 – Error si las credenciales son incorrectas")
    void login_invalidCredentials_throwsException() {
        LoginDTO request = new LoginDTO();
        request.setEmail("dududududumaxverstappen@gmail.com");
        request.setPassword("dududumaxverstappen");
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));
        assertThrows(BadCredentialsException.class, () -> userService.login(request));
    }


}

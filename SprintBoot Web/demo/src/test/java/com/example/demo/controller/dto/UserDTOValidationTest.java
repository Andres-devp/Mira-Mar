package com.example.demo.controller.dto;

import com.example.demo.enums.UserRole;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;

public class UserDTOValidationTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // --- UserRegisterDTO Tests ---

    @Test
    public void UserRegisterDTO_ValidRegistration_Success() {
        UserRegisterDTO dto = UserRegisterDTO.builder()
                .nombre("Juan Pérez")
                .apellido("López")
                .usuario("juanperez")
                .email("juan@example.com")
                .contrasena("password123")
                .contrasenaConfirm("password123")
                .telefono("5551234567")
                .cedula("1234567890")
                .rol(UserRole.CLIENT)
                .build();

        Set<ConstraintViolation<UserRegisterDTO>> violations = validator.validate(dto);
        Assertions.assertThat(violations).isEmpty();
    }

    @Test
    public void UserRegisterDTO_MissingNombre_ValidationError() {
        UserRegisterDTO dto = UserRegisterDTO.builder()
                .usuario("juanperez")
                .email("juan@example.com")
                .contrasena("password123")
                .contrasenaConfirm("password123")
                .rol(UserRole.CLIENT)
                .build();

        Set<ConstraintViolation<UserRegisterDTO>> violations = validator.validate(dto);
        Assertions.assertThat(violations).isNotEmpty();
        Assertions.assertThat(violations)
                .extracting(v -> v.getPropertyPath().toString())
                .contains("nombre");
    }

    @Test
    public void UserRegisterDTO_InvalidEmail_ValidationError() {
        UserRegisterDTO dto = UserRegisterDTO.builder()
                .nombre("Juan")
                .usuario("juanperez")
                .email("invalidemail")
                .contrasena("password123")
                .contrasenaConfirm("password123")
                .rol(UserRole.CLIENT)
                .build();

        Set<ConstraintViolation<UserRegisterDTO>> violations = validator.validate(dto);
        Assertions.assertThat(violations).isNotEmpty();
    }

    @Test
    public void UserRegisterDTO_PasswordTooShort_ValidationError() {
        UserRegisterDTO dto = UserRegisterDTO.builder()
                .nombre("Juan")
                .usuario("juanperez")
                .email("juan@example.com")
                .contrasena("123")
                .contrasenaConfirm("123")
                .rol(UserRole.CLIENT)
                .build();

        Set<ConstraintViolation<UserRegisterDTO>> violations = validator.validate(dto);
        Assertions.assertThat(violations).isNotEmpty();
    }

    @Test
    public void UserRegisterDTO_UsuarioTooShort_ValidationError() {
        UserRegisterDTO dto = UserRegisterDTO.builder()
                .nombre("Juan")
                .usuario("ab")
                .email("juan@example.com")
                .contrasena("password123")
                .contrasenaConfirm("password123")
                .rol(UserRole.CLIENT)
                .build();

        Set<ConstraintViolation<UserRegisterDTO>> violations = validator.validate(dto);
        Assertions.assertThat(violations).isNotEmpty();
    }

    @Test
    public void UserRegisterDTO_MissingRole_ValidationError() {
        UserRegisterDTO dto = UserRegisterDTO.builder()
                .nombre("Juan")
                .usuario("juanperez")
                .email("juan@example.com")
                .contrasena("password123")
                .contrasenaConfirm("password123")
                .build();

        Set<ConstraintViolation<UserRegisterDTO>> violations = validator.validate(dto);
        Assertions.assertThat(violations).isNotEmpty();
    }

    // --- UserLoginDTO Tests ---

    @Test
    public void UserLoginDTO_ValidLogin_Success() {
        UserLoginDTO dto = UserLoginDTO.builder()
                .username("juanperez")
                .password("password123")
                .build();

        Set<ConstraintViolation<UserLoginDTO>> violations = validator.validate(dto);
        Assertions.assertThat(violations).isEmpty();
    }

    @Test
    public void UserLoginDTO_LoginWithEmail_Success() {
        UserLoginDTO dto = UserLoginDTO.builder()
                .username("juan@example.com")
                .password("password123")
                .build();

        Set<ConstraintViolation<UserLoginDTO>> violations = validator.validate(dto);
        Assertions.assertThat(violations).isEmpty();
    }

    @Test
    public void UserLoginDTO_LoginWithCedula_Success() {
        UserLoginDTO dto = UserLoginDTO.builder()
                .username("1234567890")
                .password("password123")
                .build();

        Set<ConstraintViolation<UserLoginDTO>> violations = validator.validate(dto);
        Assertions.assertThat(violations).isEmpty();
    }

    @Test
    public void UserLoginDTO_MissingUsername_ValidationError() {
        UserLoginDTO dto = UserLoginDTO.builder()
                .password("password123")
                .build();

        Set<ConstraintViolation<UserLoginDTO>> violations = validator.validate(dto);
        Assertions.assertThat(violations).isNotEmpty();
    }

    @Test
    public void UserLoginDTO_MissingPassword_ValidationError() {
        UserLoginDTO dto = UserLoginDTO.builder()
                .username("juanperez")
                .build();

        Set<ConstraintViolation<UserLoginDTO>> violations = validator.validate(dto);
        Assertions.assertThat(violations).isNotEmpty();
    }

    // --- UserUpdateDTO Tests ---

    @Test
    public void UserUpdateDTO_PartialUpdate_Success() {
        UserUpdateDTO dto = UserUpdateDTO.builder()
                .nombre("Nuevo Nombre")
                .email("nuevo@example.com")
                .build();

        Set<ConstraintViolation<UserUpdateDTO>> violations = validator.validate(dto);
        Assertions.assertThat(violations).isEmpty();
    }

    @Test
    public void UserUpdateDTO_InvalidEmail_ValidationError() {
        UserUpdateDTO dto = UserUpdateDTO.builder()
                .email("notanemail")
                .build();

        Set<ConstraintViolation<UserUpdateDTO>> violations = validator.validate(dto);
        Assertions.assertThat(violations).isNotEmpty();
    }

    @Test
    public void UserUpdateDTO_PasswordTooShort_ValidationError() {
        UserUpdateDTO dto = UserUpdateDTO.builder()
                .contrasena("123")
                .build();

        Set<ConstraintViolation<UserUpdateDTO>> violations = validator.validate(dto);
        Assertions.assertThat(violations).isNotEmpty();
    }

    // --- UserProfileDTO Tests ---

    @Test
    public void UserProfileDTO_CreateProfile_Success() {
        UserProfileDTO dto = UserProfileDTO.builder()
                .id(1L)
                .usuario("juanperez")
                .nombre("Juan")
                .email("juan@example.com")
                .rol(UserRole.CLIENT)
                .activo(true)
                .build();

        Assertions.assertThat(dto).isNotNull();
        Assertions.assertThat(dto.getId()).isEqualTo(1L);
        Assertions.assertThat(dto.getRol()).isEqualTo(UserRole.CLIENT);
    }

    // --- UserResponseDTO Tests ---

    @Test
    public void UserResponseDTO_CreateResponse_NoPassword() {
        UserResponseDTO dto = UserResponseDTO.builder()
                .id(1L)
                .usuario("juanperez")
                .nombre("Juan")
                .email("juan@example.com")
                .rol(UserRole.OPERATOR)
                .activo(true)
                .build();

        Assertions.assertThat(dto).isNotNull();
        Assertions.assertThat(dto.getId()).isEqualTo(1L);
        Assertions.assertThat(dto.getRol()).isEqualTo(UserRole.OPERATOR);
    }
}

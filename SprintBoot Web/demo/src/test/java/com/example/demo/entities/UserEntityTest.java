package com.example.demo.entities;

import com.example.demo.enums.UserRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDateTime;

public class UserEntityTest {

    private UserEntity userEntity;

    @BeforeEach
    public void setUp() {
        userEntity = UserEntity.builder()
                .usuario("testuser")
                .contrasena("password123")
                .nombre("Juan")
                .apellido("Pérez")
                .email("juan@example.com")
                .cedula("1234567890")
                .telefono("5551234567")
                .fotoPerfil("http://example.com/photo.jpg")
                .rol(UserRole.CLIENT)
                .activo(true)
                .build();
    }

    @Test
    public void UserEntity_CreateClientUser_Success() {
        Assertions.assertThat(userEntity).isNotNull();
        Assertions.assertThat(userEntity.getUsuario()).isEqualTo("testuser");
        Assertions.assertThat(userEntity.getNombre()).isEqualTo("Juan");
        Assertions.assertThat(userEntity.getRol()).isEqualTo(UserRole.CLIENT);
        Assertions.assertThat(userEntity.getActivo()).isTrue();
    }

    @Test
    public void UserEntity_CreateOperatorUser_Success() {
        UserEntity operator = UserEntity.builder()
                .usuario("operador1")
                .contrasena("password123")
                .nombre("Carlos")
                .apellido("García")
                .email("carlos@hotel.com")
                .cedula("9876543210")
                .rol(UserRole.OPERATOR)
                .activo(true)
                .build();

        Assertions.assertThat(operator).isNotNull();
        Assertions.assertThat(operator.getRol()).isEqualTo(UserRole.OPERATOR);
        Assertions.assertThat(operator.getNombre()).isEqualTo("Carlos");
    }

    @Test
    public void UserEntity_CreateAdminUser_Success() {
        UserEntity admin = UserEntity.builder()
                .usuario("admin1")
                .contrasena("adminpass123")
                .nombre("Administrador")
                .email("admin@hotel.com")
                .rol(UserRole.ADMIN)
                .activo(true)
                .build();

        Assertions.assertThat(admin).isNotNull();
        Assertions.assertThat(admin.getRol()).isEqualTo(UserRole.ADMIN);
    }

    @Test
    public void UserEntity_SetFields_Success() {
        userEntity.setNombre("Roberto");
        userEntity.setActivo(false);

        Assertions.assertThat(userEntity.getNombre()).isEqualTo("Roberto");
        Assertions.assertThat(userEntity.getActivo()).isFalse();
    }

    @Test
    public void UserEntity_ValidateEmail_Success() {
        Assertions.assertThat(userEntity.getEmail())
                .isNotNull()
                .contains("@")
                .endsWith(".com");
    }

    @Test
    public void UserEntity_CedulaOptionalForClient_Success() {
        UserEntity clientNoCedula = UserEntity.builder()
                .usuario("client2")
                .contrasena("pass123")
                .nombre("Pedro")
                .email("pedro@example.com")
                .rol(UserRole.CLIENT)
                .build();

        Assertions.assertThat(clientNoCedula.getCedula()).isNull();
    }

    @Test
    public void UserEntity_AllRolesEnum_Success() {
        Assertions.assertThat(UserRole.values()).hasSize(3);
        Assertions.assertThat(UserRole.ADMIN.getDisplayName()).isEqualTo("Administrador");
        Assertions.assertThat(UserRole.OPERATOR.getDisplayName()).isEqualTo("Operador");
        Assertions.assertThat(UserRole.CLIENT.getDisplayName()).isEqualTo("Cliente");
    }
}

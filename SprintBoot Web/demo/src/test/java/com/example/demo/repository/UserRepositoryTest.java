package com.example.demo.repository;

import com.example.demo.entities.UserEntity;
import com.example.demo.enums.UserRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        // Crear usuario tipo CLIENT
        userRepository.save(UserEntity.builder()
                .usuario("cliente1")
                .contrasena("pass123")
                .nombre("Pedro")
                .apellido("González")
                .email("pedro@example.com")
                .telefono("5551111111")
                .rol(UserRole.CLIENT)
                .activo(true)
                .build());

        // Crear usuario tipo OPERATOR
        userRepository.save(UserEntity.builder()
                .usuario("operador1")
                .contrasena("pass123")
                .nombre("Carlos")
                .apellido("García")
                .email("carlos@hotel.com")
                .cedula("1234567890")
                .telefono("5552222222")
                .rol(UserRole.OPERATOR)
                .activo(true)
                .build());

        // Crear usuario tipo ADMIN
        userRepository.save(UserEntity.builder()
                .usuario("admin1")
                .contrasena("adminpass123")
                .nombre("Administrador")
                .email("admin@hotel.com")
                .rol(UserRole.ADMIN)
                .activo(true)
                .build());

        // Crear otro CLIENT inactivo
        userRepository.save(UserEntity.builder()
                .usuario("cliente2")
                .contrasena("pass123")
                .nombre("Laura")
                .email("laura@example.com")
                .rol(UserRole.CLIENT)
                .activo(false)
                .build());
    }

    @Test
    public void UserRepository_FindByUsuario_Success() {
        Optional<UserEntity> user = userRepository.findByUsuario("cliente1");

        Assertions.assertThat(user).isPresent();
        Assertions.assertThat(user.get().getNombre()).isEqualTo("Pedro");
        Assertions.assertThat(user.get().getRol()).isEqualTo(UserRole.CLIENT);
    }

    @Test
    public void UserRepository_FindByUsuario_NotFound() {
        Optional<UserEntity> user = userRepository.findByUsuario("usuarioInexistente");

        Assertions.assertThat(user).isEmpty();
    }

    @Test
    public void UserRepository_FindByEmail_Success() {
        Optional<UserEntity> user = userRepository.findByEmail("carlos@hotel.com");

        Assertions.assertThat(user).isPresent();
        Assertions.assertThat(user.get().getUsuario()).isEqualTo("operador1");
        Assertions.assertThat(user.get().getRol()).isEqualTo(UserRole.OPERATOR);
    }

    @Test
    public void UserRepository_FindByCedula_Success() {
        Optional<UserEntity> user = userRepository.findByCedula("1234567890");

        Assertions.assertThat(user).isPresent();
        Assertions.assertThat(user.get().getUsuario()).isEqualTo("operador1");
    }

    @Test
    public void UserRepository_FindByCedula_NotFound() {
        Optional<UserEntity> user = userRepository.findByCedula("9999999999");

        Assertions.assertThat(user).isEmpty();
    }

    @Test
    public void UserRepository_FindByRol_AllClients() {
        List<UserEntity> clients = userRepository.findByRol(UserRole.CLIENT);

        Assertions.assertThat(clients).hasSize(2);
        Assertions.assertThat(clients).allMatch(u -> u.getRol() == UserRole.CLIENT);
    }

    @Test
    public void UserRepository_FindByRol_AllOperators() {
        List<UserEntity> operators = userRepository.findByRol(UserRole.OPERATOR);

        Assertions.assertThat(operators).hasSize(1);
        Assertions.assertThat(operators.get(0).getUsuario()).isEqualTo("operador1");
    }

    @Test
    public void UserRepository_FindByRol_AllAdmins() {
        List<UserEntity> admins = userRepository.findByRol(UserRole.ADMIN);

        Assertions.assertThat(admins).hasSize(1);
        Assertions.assertThat(admins.get(0).getUsuario()).isEqualTo("admin1");
    }

    @Test
    public void UserRepository_FindByActivo_AllActive() {
        List<UserEntity> activeUsers = userRepository.findByActivo(true);

        Assertions.assertThat(activeUsers).hasSize(3);
        Assertions.assertThat(activeUsers).allMatch(u -> u.getActivo() == true);
    }

    @Test
    public void UserRepository_FindByActivo_AllInactive() {
        List<UserEntity> inactiveUsers = userRepository.findByActivo(false);

        Assertions.assertThat(inactiveUsers).hasSize(1);
        Assertions.assertThat(inactiveUsers.get(0).getUsuario()).isEqualTo("cliente2");
    }

    @Test
    public void UserRepository_FindByRolAndActivo_ActiveClients() {
        List<UserEntity> activeClients = userRepository.findByRolAndActivo(UserRole.CLIENT, true);

        Assertions.assertThat(activeClients).hasSize(1);
        Assertions.assertThat(activeClients.get(0).getUsuario()).isEqualTo("cliente1");
    }

    @Test
    public void UserRepository_FindByRolAndActivo_InactiveClients() {
        List<UserEntity> inactiveClients = userRepository.findByRolAndActivo(UserRole.CLIENT, false);

        Assertions.assertThat(inactiveClients).hasSize(1);
        Assertions.assertThat(inactiveClients.get(0).getUsuario()).isEqualTo("cliente2");
    }

    @Test
    public void UserRepository_ExistsByUsuario_True() {
        boolean exists = userRepository.existsByUsuario("admin1");

        Assertions.assertThat(exists).isTrue();
    }

    @Test
    public void UserRepository_ExistsByUsuario_False() {
        boolean exists = userRepository.existsByUsuario("usuarioNoExiste");

        Assertions.assertThat(exists).isFalse();
    }

    @Test
    public void UserRepository_ExistsByEmail_True() {
        boolean exists = userRepository.existsByEmail("pedro@example.com");

        Assertions.assertThat(exists).isTrue();
    }

    @Test
    public void UserRepository_ExistsByCedula_True() {
        boolean exists = userRepository.existsByCedula("1234567890");

        Assertions.assertThat(exists).isTrue();
    }

    @Test
    public void UserRepository_ExistsByCedula_False() {
        boolean exists = userRepository.existsByCedula("0000000000");

        Assertions.assertThat(exists).isFalse();
    }

    @Test
    public void UserRepository_SaveNewUser_Success() {
        UserEntity newUser = UserEntity.builder()
                .usuario("nuevousuario")
                .contrasena("pass123")
                .nombre("Nuevo Usuario")
                .email("nuevo@example.com")
                .rol(UserRole.CLIENT)
                .activo(true)
                .build();

        UserEntity saved = userRepository.save(newUser);

        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getId()).isNotNull();
        Assertions.assertThat(saved.getUsuario()).isEqualTo("nuevousuario");
    }

    @Test
    public void UserRepository_UpdateUser_Success() {
        Optional<UserEntity> user = userRepository.findByUsuario("cliente1");
        Assertions.assertThat(user).isPresent();

        UserEntity userToUpdate = user.get();
        userToUpdate.setNombre("Pedro Actualizado");
        userToUpdate.setActivo(false);

        UserEntity updated = userRepository.save(userToUpdate);

        Assertions.assertThat(updated.getNombre()).isEqualTo("Pedro Actualizado");
        Assertions.assertThat(updated.getActivo()).isFalse();
    }

    @Test
    public void UserRepository_DeleteUser_Success() {
        Optional<UserEntity> user = userRepository.findByUsuario("cliente2");
        Assertions.assertThat(user).isPresent();

        userRepository.delete(user.get());
        Optional<UserEntity> deleted = userRepository.findByUsuario("cliente2");

        Assertions.assertThat(deleted).isEmpty();
    }

    @Test
    public void UserRepository_AllThreeRolesExist_Success() {
        long clientCount = userRepository.findByRol(UserRole.CLIENT).size();
        long operatorCount = userRepository.findByRol(UserRole.OPERATOR).size();
        long adminCount = userRepository.findByRol(UserRole.ADMIN).size();

        Assertions.assertThat(clientCount).isGreaterThan(0);
        Assertions.assertThat(operatorCount).isGreaterThan(0);
        Assertions.assertThat(adminCount).isGreaterThan(0);
    }
}

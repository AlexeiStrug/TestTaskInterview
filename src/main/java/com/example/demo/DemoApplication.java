package com.example.demo;

import com.example.demo.config.SecurityConfig;
import com.example.demo.domain.Permission;
import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import com.example.demo.domain.enums.PermissionEnum;
import com.example.demo.repository.PermissionRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}

@Component
class DemoCommandLineRunner implements CommandLineRunner {

    private PermissionRepository permissionRepository;
    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private SecurityConfig securityConfig;

    @Autowired
    public DemoCommandLineRunner(PermissionRepository permissionRepository, RoleRepository roleRepository, UserRepository userRepository, SecurityConfig securityConfig) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.securityConfig = securityConfig;
    }


    @Override
    public void run(String... args) throws Exception {
        List<Permission> permissions = new ArrayList<>();
        permissions.add(Permission.builder().id(1L).name(PermissionEnum.CREATE.getName()).build());
        permissions.add(Permission.builder().id(2L).name(PermissionEnum.UPDATE.getName()).build());
        permissions.add(Permission.builder().id(3L).name(PermissionEnum.DELETE.getName()).build());
        permissions.add(Permission.builder().id(4L).name(PermissionEnum.LIST_ALL.getName()).build());
        initPermissionTable(permissions);

        Role role = Role.builder().id(1L).roleName("Admin").permissions(new HashSet<>(permissions)).build();
        initAdminRole(role);

        User user = User.builder().id(1L).username("Admin").password(securityConfig.passwordEncoder("admin")).role(role).build();
        initAdminUser(user);

    }

    private void initPermissionTable(List<Permission> permissions) {
        permissionRepository.saveAll(permissions);
    }


    public void initAdminRole(Role role) {
        roleRepository.save(role);
    }

    public void initAdminUser(User user) {
        userRepository.save(user);
    }
}


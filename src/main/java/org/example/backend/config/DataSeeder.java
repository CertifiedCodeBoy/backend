package org.example.backend.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.entity.OpenPurchaseOrder;
import org.example.backend.entity.User;
import org.example.backend.enums.Role;
import org.example.backend.enums.TaskStatus;
import org.example.backend.repository.OpenPurchaseOrderRepository;
import org.example.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final OpenPurchaseOrderRepository openPurchaseOrderRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            log.info("No users found — seeding default users...");

            User admin = User.builder()
                    .username("admin")
                    .email("admin@mobai.com")
                    .password(passwordEncoder.encode("admin123"))
                    .firstName("System")
                    .lastName("Administrator")
                    .role(Role.ADMIN)
                    .active(true)
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .build();

            User supervisor = User.builder()
                    .username("supervisor1")
                    .email("supervisor@mobai.com")
                    .password(passwordEncoder.encode("supervisor123"))
                    .firstName("John")
                    .lastName("Supervisor")
                    .role(Role.SUPERVISOR)
                    .active(true)
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .build();

            User employee = User.builder()
                    .username("employee1")
                    .email("employee@mobai.com")
                    .password(passwordEncoder.encode("employee123"))
                    .firstName("Jane")
                    .lastName("Employee")
                    .role(Role.EMPLOYEE)
                    .active(true)
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .build();

            userRepository.save(admin);
            userRepository.save(supervisor);
            userRepository.save(employee);

            log.info("Default users seeded successfully (admin, supervisor1, employee1)");
        } else {
            log.info("Users already exist — resetting default passwords...");
            resetDefaultUserPassword("admin", "admin123");
            resetDefaultUserPassword("supervisor1", "supervisor123");
            resetDefaultUserPassword("employee1", "employee123");
        }

        seedOrdersIfNeeded();
    }

    private void resetDefaultUserPassword(String username, String rawPassword) {
        userRepository.findByUsername(username).ifPresent(user -> {
            if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(rawPassword));
                user.setActive(true);
                userRepository.save(user);
                log.info("Password reset for user: {}", username);
            }
        });
    }

    private void seedOrdersIfNeeded() {
        if (openPurchaseOrderRepository.count() > 0) {
            log.info("Orders already exist — skipping order seed");
            return;
        }

        log.info("No orders found — seeding dummy orders...");

        LocalDateTime now = LocalDateTime.now();

        OpenPurchaseOrder order1 = OpenPurchaseOrder.builder()
                .packageId("PKG-1001")
                .weightKg(4.5)
                .fragile(true)
                .fromLocation("RECV-A1")
                .toLocation("STG-B3")
                .transferPath(List.of("RECV-A1", "AISLE-A", "STG-B3"))
                .assignedWorkerId("employee1")
                .status(TaskStatus.CREATED)
                .scanStep(0)
                .createdAt(now.minusHours(8))
                .build();

        OpenPurchaseOrder order2 = OpenPurchaseOrder.builder()
                .packageId("PKG-1002")
                .weightKg(12.3)
                .fragile(false)
                .fromLocation("RECV-A2")
                .toLocation("RACK-C1")
                .transferPath(List.of("RECV-A2", "QC-ZONE", "RACK-C1"))
                .assignedWorkerId("employee1")
                .status(TaskStatus.PICKED)
                .scanStep(2)
                .createdAt(now.minusHours(12))
                .pickedAt(now.minusHours(10))
                .build();

        OpenPurchaseOrder order3 = OpenPurchaseOrder.builder()
                .packageId("PKG-1003")
                .weightKg(7.8)
                .fragile(false)
                .fromLocation("RECV-B1")
                .toLocation("RACK-D4")
                .transferPath(List.of("RECV-B1", "AISLE-D", "RACK-D4"))
                .assignedWorkerId("employee2")
                .status(TaskStatus.COMPLETED)
                .scanStep(3)
                .createdAt(now.minusDays(1))
                .pickedAt(now.minusDays(1).plusHours(1))
                .completedAt(now.minusDays(1).plusHours(3))
                .build();

        OpenPurchaseOrder order4 = OpenPurchaseOrder.builder()
                .packageId("PKG-1004")
                .weightKg(2.1)
                .fragile(true)
                .fromLocation("RECV-C2")
                .toLocation("BIN-E2")
                .transferPath(List.of("RECV-C2", "CROSSDOCK", "BIN-E2"))
                .assignedWorkerId("employee3")
                .status(TaskStatus.CREATED)
                .scanStep(0)
                .createdAt(now.minusHours(2))
                .build();

        OpenPurchaseOrder order5 = OpenPurchaseOrder.builder()
                .packageId("PKG-1005")
                .weightKg(18.0)
                .fragile(false)
                .fromLocation("RECV-D1")
                .toLocation("RACK-F5")
                .transferPath(List.of("RECV-D1", "AISLE-F", "RACK-F5"))
                .assignedWorkerId("employee1")
                .status(TaskStatus.COMPLETED)
                .scanStep(4)
                .createdAt(now.minusDays(2))
                .pickedAt(now.minusDays(2).plusHours(2))
                .completedAt(now.minusDays(2).plusHours(6))
                .build();

        openPurchaseOrderRepository.saveAll(List.of(order1, order2, order3, order4, order5));
        log.info("Dummy orders seeded successfully (5 records)");
    }
}

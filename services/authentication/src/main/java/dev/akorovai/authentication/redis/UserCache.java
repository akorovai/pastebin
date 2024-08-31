package dev.akorovai.authentication.redis;

import dev.akorovai.authentication.role.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCache {
    private UUID id;
    private String nickname;
    private String password;
    private List<Role> roles;
    private LocalDateTime createdDate;
}
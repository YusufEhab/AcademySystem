package com.iacademy.AcademySystem.Document;

import com.iacademy.AcademySystem.Enum.Role;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "password")
public class User implements UserDetails {

    @Id
    private String id;

    private String username;

    private String email;

    private String password;

    private Role role = Role.USER;

    private boolean enabled = true;

    private LocalDate passwordExpiryDate;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + (role != null ? role.name() : "USER")));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return passwordExpiryDate == null || !passwordExpiryDate.isBefore(LocalDate.now());
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}

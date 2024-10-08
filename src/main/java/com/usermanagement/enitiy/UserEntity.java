package com.usermanagement.enitiy;


import com.usermanagement.Enum.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table (name = "userDb")
@Entity(name = "userDb")
public class UserEntity implements UserDetails {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @Column(nullable = false)
        private String name;

        @Column(unique = true,nullable = false)
        private String email;

        @Column(nullable = false)
        private String password;

        @Column(name = "about_me")
        private String aboutMe;

        @Enumerated(EnumType.STRING) //stores enum as string in DB
        @Column(nullable = false, name = "role")
        private Role role = Role.USER;


        // New fields for password reset
        @Column(name = "reset_password_token")
        private String resetPasswordToken;

        @Column(name = "reset_password_token_expiry")
        private LocalDateTime resetPasswordTokenExpiry;


        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name="category_id",referencedColumnName = "id")
        private CategoryEntity category;



        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
        }

        // Add logic to check if the reset token is expired
        public boolean isResetTokenExpired() {
                return resetPasswordTokenExpiry == null || LocalDateTime.now().isAfter(resetPasswordTokenExpiry);
        }
        @Override
        public String getUsername() {
                return this.email;
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
                return true;
        }

        @Override
        public boolean isEnabled() {
                return false;
        }


}

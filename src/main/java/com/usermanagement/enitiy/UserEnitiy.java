package com.usermanagement.enitiy;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table (name = "userDb")
public class UserEnitiy implements UserDetails {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private long id;
        @Column(nullable = false)
        private String name;

        @Column(unique = true,nullable = false)
        private String email;
        @Column(nullable = false)
        private String password;
        private String aboutMe;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name="category_id",referencedColumnName = "id")
        private CategoryEntity category;


        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
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
                return true;
        }
}

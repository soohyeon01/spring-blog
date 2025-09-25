package springboot_developer.spring_blog.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    // 리소스 서버에서 제공해주는 이름을 받을 필드
    @Column(name = "nickname", unique = true)
    private String nickname;

    @Builder
    public User(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
    
    /* 사용자 이름 변경 */
    /* 사용자 정보가 있다면 리소스 서버에서 제공해주는 이름을 업데이트하고, 없다면 새 사용자를 생성해 DB에 저장하는 서비스 구현 */
    public User update(String nickname) {
        this.nickname = nickname;

        return this;
    }

    /* 권한 반환 */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }
    
    /* 사용자의 id 반환(고유한 값) */
    @Override
    public String getUsername() {
        return email;
    }

    /* 사용자의 패스워드 반환 */
    @Override
    public String getPassword() {
        return password;
    }


    /* 계정 만료 여부 반환 */
    @Override
    public boolean isAccountNonExpired() {
        // 만료되었는지 확인하는 로직
        return true;    // true -> 만료되지 않음
    }

    /* 계정 잠금 여부 반환 */
    @Override
    public boolean isAccountNonLocked() {
        // 계정 잠금되었는지 확인하는 로직
        return true;
    }

    /* 패스워드의 만료 여부 반환 */
    @Override
    public boolean isCredentialsNonExpired() {
        // 패스워드가 만료되었는지 확인하는 로직
        return true;
    }

    /* 계정 사용 가능 여부 반환 */
    @Override
    public boolean isEnabled() {
        // 계정이 사용 가능한지 확인하는 로직
        return true;
    }
}

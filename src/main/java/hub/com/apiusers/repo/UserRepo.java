package hub.com.apiusers.repo;

import hub.com.apiusers.entity.User;
import hub.com.apiusers.projection.user.UserView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepo extends JpaRepository<User,Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // projection
    List<UserView> findByActiveTrue();
}

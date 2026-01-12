package hub.com.apiusers.repo;

import hub.com.apiusers.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Long> {

}

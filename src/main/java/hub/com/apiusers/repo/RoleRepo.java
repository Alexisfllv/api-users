package hub.com.apiusers.repo;

import hub.com.apiusers.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role,Long> {
    boolean existsByName(String name);
}

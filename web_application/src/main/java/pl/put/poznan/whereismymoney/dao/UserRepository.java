package pl.put.poznan.whereismymoney.dao;

import org.springframework.data.repository.CrudRepository;
import pl.put.poznan.whereismymoney.model.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findByUsername(String username);
    List<User> findByEmail(String email);
}

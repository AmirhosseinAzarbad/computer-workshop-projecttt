package ir.speedy.computerworkshopproject.Repositories;

import ir.speedy.computerworkshopproject.models.AdminRegPass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRegPassRepository extends JpaRepository<AdminRegPass, Long> {
    Optional<AdminRegPass> findFirstByOrderByIdAsc();

}

package repositories;

import models.Application;
import models.CompositionOfApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompositionRepo extends JpaRepository<CompositionOfApplication, Application> {
}

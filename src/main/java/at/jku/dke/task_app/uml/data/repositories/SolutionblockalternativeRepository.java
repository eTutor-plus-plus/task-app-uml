package at.jku.dke.task_app.uml.data.repositories;

import at.jku.dke.task_app.uml.data.entities.UmlBlockAlt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolutionblockalternativeRepository extends JpaRepository<UmlBlockAlt, Long> {
}

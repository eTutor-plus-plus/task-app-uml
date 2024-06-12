package at.jku.dke.task_app.uml.data.repositories;

import at.jku.dke.task_app.uml.data.entities.UmlSolution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UmlSolutionRepository extends JpaRepository<UmlSolution, Long> {
}

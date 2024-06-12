package at.jku.dke.task_app.uml.data.repositories;

import at.jku.dke.task_app.uml.data.entities.UmlBlock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolutionblockRepository extends JpaRepository<UmlBlock, Long> {
}

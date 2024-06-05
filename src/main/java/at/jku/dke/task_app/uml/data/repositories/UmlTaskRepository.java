package at.jku.dke.task_app.uml.data.repositories;

import at.jku.dke.task_app.uml.data.entities.UmlTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UmlTaskRepository extends JpaRepository<UmlTask, Long> {
}

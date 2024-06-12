package at.jku.dke.task_app.uml.data.repositories;

import at.jku.dke.etutor.task_app.data.repositories.TaskRepository;
import at.jku.dke.task_app.uml.data.entities.UmlTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UmlTaskRepository extends TaskRepository<UmlTask> {
}

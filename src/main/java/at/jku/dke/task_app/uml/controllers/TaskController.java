package at.jku.dke.task_app.uml.controllers;

import at.jku.dke.etutor.task_app.controllers.BaseTaskController;
import at.jku.dke.task_app.uml.data.entities.UmlTask;
import at.jku.dke.task_app.uml.dto.UmlTaskDto;
import at.jku.dke.task_app.uml.dto.ModifyUmlTaskDto;
import at.jku.dke.task_app.uml.services.UmlTaskService;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing {@link UmlTask}s.
 */
@RestController
public class TaskController extends BaseTaskController<UmlTask, UmlTaskDto, ModifyUmlTaskDto> {

    /**
     * Creates a new instance of class {@link TaskController}.
     *
     * @param taskService The task service.
     */
    public TaskController(UmlTaskService taskService) {
        super(taskService);
    }

    @Override
    protected UmlTaskDto mapToDto(UmlTask task) {
        return new UmlTaskDto(1);
    }

}

package at.jku.dke.task_app.uml.controllers;

import at.jku.dke.etutor.task_app.controllers.BaseTaskController;
import at.jku.dke.task_app.uml.data.entities.UmlTask;
import at.jku.dke.task_app.uml.dto.BinarySearchTaskDto;
import at.jku.dke.task_app.uml.dto.ModifyBinarySearchTaskDto;
import at.jku.dke.task_app.uml.services.BinarySearchTaskService;
import at.jku.dke.task_app.uml.services.UmlTaskService;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing {@link UmlTask}s.
 */
@RestController
public class TaskController extends BaseTaskController<UmlTask, BinarySearchTaskDto, ModifyBinarySearchTaskDto> {

    /**
     * Creates a new instance of class {@link TaskController}.
     *
     * @param taskService The task service.
     */
    public TaskController(UmlTaskService taskService) {
        super(taskService);
    }

    @Override
    protected BinarySearchTaskDto mapToDto(UmlTask task) {
        return new BinarySearchTaskDto(task.getSolutions());
    }

}

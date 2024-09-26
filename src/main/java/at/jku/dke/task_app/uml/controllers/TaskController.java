package at.jku.dke.task_app.uml.controllers;

import at.jku.dke.etutor.task_app.controllers.BaseTaskController;
import at.jku.dke.task_app.uml.data.entities.UmlBlock;
import at.jku.dke.task_app.uml.data.entities.UmlBlockAlt;
import at.jku.dke.task_app.uml.data.entities.UmlTask;
import at.jku.dke.task_app.uml.data.repositories.UmlBlockAltRepository;
import at.jku.dke.task_app.uml.data.repositories.UmlBlockRepository;
import at.jku.dke.task_app.uml.dto.UmlBlockAltDto;
import at.jku.dke.task_app.uml.dto.UmlBlockDto;
import at.jku.dke.task_app.uml.dto.UmlTaskDto;
import at.jku.dke.task_app.uml.dto.ModifyUmlTaskDto;
import at.jku.dke.task_app.uml.services.UmlTaskService;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
/**
 * Controller for managing {@link UmlTask}s.
 */
@RestController
public class TaskController extends BaseTaskController<UmlTask, UmlTaskDto, ModifyUmlTaskDto> {

    private final UmlBlockRepository umlBlockRepository;
    private final UmlBlockAltRepository umlBlockAltRepository;
    /**
     * Creates a new instance of class {@link TaskController}.
     *
     * @param taskService The task service.
     */
    public TaskController(UmlTaskService taskService, UmlBlockRepository umlBlockRepository, UmlBlockAltRepository umlBlockAltRepository) {
        super(taskService);
        this.umlBlockRepository = umlBlockRepository;
        this.umlBlockAltRepository = umlBlockAltRepository;

    }

    @Override
    protected UmlTaskDto mapToDto(UmlTask task) {
        List<UmlBlockDto> umlBlockDtos = new ArrayList<>();
        for (UmlBlock umlBlock : umlBlockRepository.findByTask(task)) {
            List<UmlBlockAltDto> umlBlockAltDtos = new ArrayList<>();
            for (UmlBlockAlt umlBlockAlt : umlBlockAltRepository.findByUmlBlock(umlBlock)) {
                umlBlockAltDtos.add(new UmlBlockAltDto(umlBlockAlt.getUmlBlockAlternative()));
            }
            UmlBlockDto umlBlockDto = new UmlBlockDto(umlBlockAltDtos);

            umlBlockDtos.add(umlBlockDto);
        }
        return new UmlTaskDto(task.getCompleteComparison(), umlBlockDtos, task.getClassPoints().doubleValue(), task.getRelationshipPoints().doubleValue(), task.getAssociationPoints().doubleValue(), task.getConstraintPoints().doubleValue());
    }
}



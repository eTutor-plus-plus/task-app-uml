package at.jku.dke.task_app.uml.services;

import at.jku.dke.etutor.task_app.data.repositories.TaskRepository;
import at.jku.dke.etutor.task_app.dto.ModifyTaskDto;
import at.jku.dke.etutor.task_app.dto.TaskModificationResponseDto;
import at.jku.dke.etutor.task_app.services.BaseTaskService;

import at.jku.dke.task_app.uml.data.entities.UmlBlock;
import at.jku.dke.task_app.uml.data.entities.UmlTask;
import at.jku.dke.task_app.uml.dto.ModifyUmlTaskDto;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * This class provides methods for managing {@link UmlTask}s.
 */
@Service
public class UmlTaskService extends BaseTaskService<UmlTask, ModifyUmlTaskDto>{

    private final MessageSource messageSource;

    /**
     * Creates a new instance of class {@link BaseTaskService}.
     *
     * @param repository The task repository.
     */
    protected UmlTaskService(TaskRepository<UmlTask> repository, MessageSource messageSource) {
        super(repository);
        this.messageSource = messageSource;
    }


    @Override
    protected UmlTask createTask(long id, ModifyTaskDto<ModifyUmlTaskDto> modifyTaskDto) {
        UmlTask task = new UmlTask();
        if (!modifyTaskDto.taskType().equals("uml"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid task type.");

        List<UmlBlock> umlBlocks = new ArrayList<>();
//        for (List<Solutionblockalternative> solution : modifyTaskDto.additionalData().solution().) {
//            List<Solutionblockalternative> solutionblockalternatives = new ArrayList<>();
//
//            for (String s : solution) {
//                Solutionblockalternative solutionblockalternative = new Solutionblockalternative();
//                solutionblockalternative.setSolutionBlockAlternative(s);
//                solutionblockalternatives.add(solutionblockalternative);
//            }
//            Solutionblock solutionblock = new Solutionblock();
//            solutionblock.setSolutionblockalternatives(solutionblockalternatives);
//            solutionblocks.add(solutionblock);
//        }
//
//        task.setSolutionblocks(solutionblocks);
        return task;
    }

    @Override
    protected void afterCreate(UmlTask task, ModifyTaskDto<ModifyUmlTaskDto> dto) {
        super.afterCreate(task, dto);
        //get all identifiers from the task;
    }

    @Override
    protected void updateTask(UmlTask task, ModifyTaskDto<ModifyUmlTaskDto> dto) {

    }



    @Override
    protected TaskModificationResponseDto mapToReturnData(UmlTask task, boolean create) {
        return new TaskModificationResponseDto(
            this.messageSource.getMessage("defaultTaskDescription", null, Locale.GERMAN),
            this.messageSource.getMessage("defaultTaskDescription", null, Locale.ENGLISH)
        );
    }
}

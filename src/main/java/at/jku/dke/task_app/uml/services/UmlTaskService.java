package at.jku.dke.task_app.uml.services;

import at.jku.dke.etutor.task_app.data.repositories.TaskRepository;
import at.jku.dke.etutor.task_app.dto.ModifyTaskDto;
import at.jku.dke.etutor.task_app.dto.TaskModificationResponseDto;
import at.jku.dke.etutor.task_app.services.BaseTaskService;

import at.jku.dke.task_app.uml.data.entities.UmlTask;
import at.jku.dke.task_app.uml.dto.ModifyUmlTaskDto;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        if (!modifyTaskDto.taskType().equals("binary-search"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid task type.");
        return new UmlTask();
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

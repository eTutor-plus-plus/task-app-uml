package at.jku.dke.task_app.uml.services;

import at.jku.dke.etutor.task_app.data.repositories.TaskRepository;
import at.jku.dke.etutor.task_app.dto.ModifyTaskDto;
import at.jku.dke.etutor.task_app.dto.TaskModificationResponseDto;
import at.jku.dke.etutor.task_app.services.BaseTaskService;

import at.jku.dke.task_app.uml.data.entities.UmlBlock;
import at.jku.dke.task_app.uml.data.entities.UmlBlockAlt;
import at.jku.dke.task_app.uml.data.entities.UmlTask;
import at.jku.dke.task_app.uml.data.repositories.UmlBlockAltRepository;
import at.jku.dke.task_app.uml.data.repositories.UmlBlockRepository;
import at.jku.dke.task_app.uml.data.repositories.UmlTaskRepository;
import at.jku.dke.task_app.uml.dto.ModifyUmlTaskDto;
import at.jku.dke.task_app.uml.dto.UmlBlockAltDto;
import at.jku.dke.task_app.uml.dto.UmlBlockDto;
import at.jku.dke.task_app.uml.evaluation.MyPlantUML_ATGListener;
import at.jku.dke.task_app.uml.evaluation.atg.gen.PlantUML_ATGLexer;
import at.jku.dke.task_app.uml.evaluation.atg.gen.PlantUML_ATGParser;
import at.jku.dke.task_app.uml.evaluation.atg.objects.UMLClass;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

/**
 * This class provides methods for managing {@link UmlTask}s.
 */
@Service
public class UmlTaskService extends BaseTaskService<UmlTask, ModifyUmlTaskDto>{

    private final MessageSource messageSource;
    private final TaskRepository<UmlTask> repository;
    private final UmlBlockRepository umlBlockRepository;
    private final UmlBlockAltRepository umlBlockAltRepository;
    private final UmlTaskRepository umlTaskRepository;

    /**
     * Creates a new instance of class {@link BaseTaskService}.
     *
     * @param repository The task repository.
     */
    protected UmlTaskService(TaskRepository<UmlTask> repository, MessageSource messageSource, TaskRepository<UmlTask> repository1, UmlBlockRepository umlBlockRepository, UmlBlockAltRepository umlBlockAltRepository, UmlTaskRepository umlTaskRepository) {
        super(repository);
        this.messageSource = messageSource;
        this.repository = repository1;

        this.umlBlockRepository = umlBlockRepository;
        this.umlBlockAltRepository = umlBlockAltRepository;
        this.umlTaskRepository = umlTaskRepository;
    }


    @Override
    protected UmlTask createTask(long id, ModifyTaskDto<ModifyUmlTaskDto> modifyTaskDto) {

        if (!modifyTaskDto.taskType().equals("uml"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid task type.");


        UmlTask task = new UmlTask();
        task.setCompleteComparison(modifyTaskDto.additionalData().completeComparison());
        task.setId(id);
        return task;
    }

    @Override
    protected void afterCreate(UmlTask task, ModifyTaskDto<ModifyUmlTaskDto> dto) {
        super.afterCreate(task, dto);
        for (UmlBlockDto umlBlockDto : dto.additionalData().umlSolution()) {
            UmlBlock umlBlock = new UmlBlock();
            umlBlock.setTask(task);
            umlBlockRepository.save(umlBlock);
            for (UmlBlockAltDto umlBlockAltDto : umlBlockDto.getUmlBlockAlt()) {
                UmlBlockAlt umlBlockAlt = new UmlBlockAlt();
                umlBlockAlt.setUmlBlock(umlBlock);
                umlBlockAlt.setUmlBlockAlternative(umlBlockAltDto.getSolutionBlockAlternative());
                umlBlockAltRepository.save(umlBlockAlt);
            }

        }
        generateObjectsFromSolution(task, dto);
        //get all identifiers from the task;
    }

    @Override
    protected void updateTask(UmlTask task, ModifyTaskDto<ModifyUmlTaskDto> dto) {
        deleteBlocks(task);

        task.setCompleteComparison(dto.additionalData().completeComparison());
        umlTaskRepository.save(task);
    }

    @Override
    protected void afterUpdate(UmlTask task, ModifyTaskDto<ModifyUmlTaskDto> dto) {
        super.afterUpdate(task, dto);
        for (UmlBlockDto umlBlockDto : dto.additionalData().umlSolution()) {
            UmlBlock umlBlock = new UmlBlock();
            umlBlock.setTask(task);
            umlBlockRepository.save(umlBlock);
            for (UmlBlockAltDto umlBlockAltDto : umlBlockDto.getUmlBlockAlt()) {
                UmlBlockAlt umlBlockAlt = new UmlBlockAlt();
                umlBlockAlt.setUmlBlock(umlBlock);
                umlBlockAlt.setUmlBlockAlternative(umlBlockAltDto.getSolutionBlockAlternative());
                umlBlockAltRepository.save(umlBlockAlt);
            }

        }
        generateObjectsFromSolution(task, dto);
        //get all identifiers from the task;
    }

    @Override
    public void delete(long id) {
        umlTaskRepository.findById(id).ifPresent(task -> {
            if (deleteBlocks(task)) {
                super.delete(id);
            }
        });
    }

    private boolean deleteBlocks(UmlTask task){
        List<UmlBlock> umlBlocks = umlBlockRepository.findByTask(task);
        for (UmlBlock umlBlock : umlBlocks) {
            List<UmlBlockAlt> umlBlockAlts = umlBlockAltRepository.findByUmlBlock(umlBlock);
            for (UmlBlockAlt umlBlockAlt : umlBlockAlts) {
                umlBlockAltRepository.delete(umlBlockAlt);
            }
            umlBlockRepository.delete(umlBlock);
        }
        return true;
    }

    @Override
    protected TaskModificationResponseDto mapToReturnData(UmlTask task, boolean create) {
        return new TaskModificationResponseDto(
            this.messageSource.getMessage("defaultTaskDescription", null, Locale.GERMAN),
            this.messageSource.getMessage("defaultTaskDescription", null, Locale.ENGLISH)
        );
    }

    protected void generateObjectsFromSolution(UmlTask task, ModifyTaskDto<ModifyUmlTaskDto> dto) {
        for (UmlBlockDto umlBlockDto : dto.additionalData().umlSolution()) {
            UmlBlock umlBlock = new UmlBlock();
            umlBlock.setTask(task);
            umlBlockRepository.save(umlBlock);
            for (UmlBlockAltDto umlBlockAltDto : umlBlockDto.getUmlBlockAlt()) {
                String solution = umlBlockAltDto.getSolutionBlockAlternative();
                CharStream input = CharStreams.fromString(solution);
                PlantUML_ATGLexer lexer = new PlantUML_ATGLexer(input);
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                PlantUML_ATGParser parser = new PlantUML_ATGParser(tokens);
                ParseTree tree = parser.start();
                ParseTreeWalker walker = new ParseTreeWalker();
                MyPlantUML_ATGListener listener = new MyPlantUML_ATGListener();
                walker.walk(listener, tree);
                List<UMLClass> umlClasses = listener.getUmlClasses();
                System.out.println(umlClasses);
            }
        }
    }
}

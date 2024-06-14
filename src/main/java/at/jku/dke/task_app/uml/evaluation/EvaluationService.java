package at.jku.dke.task_app.uml.evaluation;

import at.jku.dke.etutor.task_app.dto.CriterionDto;
import at.jku.dke.etutor.task_app.dto.GradingDto;
import at.jku.dke.etutor.task_app.dto.SubmitSubmissionDto;
import at.jku.dke.task_app.uml.data.repositories.UmlTaskRepository;
import at.jku.dke.task_app.uml.dto.UmlSubmissionDto;
import at.jku.dke.task_app.uml.evaluation.atg.objects.UMLResult;
import at.jku.dke.task_app.uml.services.UmlGenerationService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Service that evaluates submissions.
 */
@Service
public class EvaluationService {
    private static final Logger LOG = LoggerFactory.getLogger(EvaluationService.class);

    private final UmlTaskRepository taskRepository;
    private final MessageSource messageSource;
    private final UmlGenerationService umlGenerationService;

    /**
     * Creates a new instance of class {@link EvaluationService}.
     *
     * @param taskRepository The task repository.
     * @param messageSource  The message source.
     */
    public EvaluationService(UmlTaskRepository taskRepository, MessageSource messageSource, UmlGenerationService umlGenerationService) {
        this.taskRepository = taskRepository;
        this.messageSource = messageSource;
        this.umlGenerationService = umlGenerationService;
    }

    /**
     * Evaluates a input.
     *
     * @param submission The input to evaluate.
     * @return The evaluation result.
     */
    @Transactional
    public GradingDto evaluate(SubmitSubmissionDto<UmlSubmissionDto> submission) {
        // find task
        var task = this.taskRepository.findById(submission.taskId()).orElseThrow(() -> new EntityNotFoundException("Task " + submission.taskId() + " does not exist."));

        // evaluate
        var points = BigDecimal.ZERO;
        var criteria = new ArrayList<CriterionDto>();
        UMLResult umlResultSubmission = umlGenerationService.generateResultsFromSubmission(submission.submission().input());
        System.out.println(umlResultSubmission);


        // evaluate



        return new GradingDto(task.getMaxPoints(), points, "", criteria);
    }
}

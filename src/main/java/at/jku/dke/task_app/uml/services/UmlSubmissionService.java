package at.jku.dke.task_app.uml.services;

import at.jku.dke.etutor.task_app.dto.GradingDto;
import at.jku.dke.etutor.task_app.dto.SubmitSubmissionDto;
import at.jku.dke.etutor.task_app.services.BaseSubmissionService;
import at.jku.dke.task_app.uml.data.entities.UmlSubmission;
import at.jku.dke.task_app.uml.data.entities.UmlTask;
import at.jku.dke.task_app.uml.data.repositories.UmlSubmissionRepository;
import at.jku.dke.task_app.uml.data.repositories.UmlTaskRepository;
import at.jku.dke.task_app.uml.dto.UmlSubmissionDto;
import at.jku.dke.task_app.uml.evaluation.EvaluationService;
import org.springframework.stereotype.Service;

/**
 * This class provides methods for managing {@link UmlSubmission}s.
 */
@Service
public class UmlSubmissionService extends BaseSubmissionService<UmlTask, UmlSubmission, UmlSubmissionDto> {

    private final EvaluationService evaluationService;

    /**
     * Creates a new instance of class {@link UmlSubmissionService}.
     *
     * @param submissionRepository The input repository.
     * @param taskRepository       The task repository.
     * @param evaluationService    The evaluation service.
     */
    public UmlSubmissionService(UmlSubmissionRepository submissionRepository, UmlTaskRepository taskRepository, EvaluationService evaluationService) {
        super(submissionRepository, taskRepository);
        this.evaluationService = evaluationService;
    }

    @Override
    protected UmlSubmission createSubmissionEntity(SubmitSubmissionDto<UmlSubmissionDto> submitSubmissionDto) {
        return new UmlSubmission();
    }

    @Override
    protected GradingDto evaluate(SubmitSubmissionDto<UmlSubmissionDto> submitSubmissionDto) {
        return this.evaluationService.evaluate(submitSubmissionDto);
    }

    @Override
    protected UmlSubmissionDto mapSubmissionToSubmissionData(UmlSubmission submission) {
        return new UmlSubmissionDto(submission.getSubmission());
    }

}

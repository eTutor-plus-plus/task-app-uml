package at.jku.dke.task_app.uml.controllers;

import at.jku.dke.etutor.task_app.controllers.BaseSubmissionController;
import at.jku.dke.task_app.uml.data.entities.BinarySearchSubmission;
import at.jku.dke.task_app.uml.dto.BinarySearchSubmissionDto;
import at.jku.dke.task_app.uml.services.BinarySearchSubmissionService;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing {@link BinarySearchSubmission}s.
 */
@RestController
public class SubmissionController extends BaseSubmissionController<BinarySearchSubmissionDto> {
    /**
     * Creates a new instance of class {@link SubmissionController}.
     *
     * @param submissionService The input service.
     */
    public SubmissionController(BinarySearchSubmissionService submissionService) {
        super(submissionService);
    }
}

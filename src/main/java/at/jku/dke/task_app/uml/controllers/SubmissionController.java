package at.jku.dke.task_app.uml.controllers;

import at.jku.dke.etutor.task_app.controllers.BaseSubmissionController;
import at.jku.dke.task_app.uml.dto.UmlSubmissionDto;
import at.jku.dke.task_app.uml.services.UmlSubmissionService;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Submission controller.
 */
@RestController
public class SubmissionController extends BaseSubmissionController<UmlSubmissionDto> {
    /**
     * Creates a new instance of class {@link SubmissionController}.
     *
     * @param submissionService The input service.
     */
    public SubmissionController(UmlSubmissionService submissionService) {
        super(submissionService);
    }
}

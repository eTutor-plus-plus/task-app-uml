package at.jku.dke.task_app.uml.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * This class represents a data transfer object for submitting a solution.
 *
 * @param input The user input.
 */
public record UmlSubmissionDto(@NotNull String input) {
}

package at.jku.dke.task_app.uml.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link at.jku.dke.task_app.uml.data.entities.UmlTask}
 *
 * @param umlSolution The solution.
 */
public record UmlTaskDto(@NotNull boolean completeComparison, @NotNull List<UmlBlockDto> umlSolution) implements Serializable {
}

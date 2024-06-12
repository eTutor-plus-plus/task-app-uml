package at.jku.dke.task_app.uml.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;

/**
 * This class represents a data transfer object for modifying a binary search task.
 *
 * @param umlSolution The solution.
 */
public record ModifyUmlTaskDto(@NotNull List<UmlBlockDto> umlSolution) implements Serializable {
}

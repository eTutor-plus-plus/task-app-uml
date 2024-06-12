package at.jku.dke.task_app.uml.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * This class represents a data transfer object for modifying a binary search task.
 *
 * @param solution The solution.
 */
public record ModifyUmlTaskDto(@NotNull Integer solution) implements Serializable {
}

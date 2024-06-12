package at.jku.dke.task_app.uml.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link at.jku.dke.task_app.uml.data.entities.UmlTask}
 *
 * @param solution The solution.
 */
public record UmlTaskDto(@NotNull Integer solution) implements Serializable {
}

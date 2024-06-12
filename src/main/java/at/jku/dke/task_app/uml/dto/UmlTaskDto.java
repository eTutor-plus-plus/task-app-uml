package at.jku.dke.task_app.uml.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link at.jku.dke.task_app.uml.data.entities.UmlTask}
 *
 * @param umlBlocks The solution.
 */
public record UmlTaskDto(@NotNull List<UmlBlockDto> umlBlocks) implements Serializable {
}

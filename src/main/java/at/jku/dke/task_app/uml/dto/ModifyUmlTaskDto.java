package at.jku.dke.task_app.uml.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;

/**
 * This class represents a data transfer object for modifying a binary search task.
 *
 * @param umlSolution The solution.
 */
public record ModifyUmlTaskDto(@NotNull boolean completeComparison, @NotNull List<UmlBlockDto> umlSolution, @NotNull double classPoints, @NotNull double attributePoints,
                               @NotNull double relationshipPoints, @NotNull double associationPoints, @NotNull double constraintPoints) implements Serializable {

}

package at.jku.dke.task_app.uml.data.repositories;

import at.jku.dke.task_app.uml.data.entities.UmlBlock;
import at.jku.dke.task_app.uml.data.entities.UmlBlockAlt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UmlBlockAltRepository extends JpaRepository<UmlBlockAlt, UUID> {
    List<UmlBlockAlt> findByUmlBlock_Id(Integer umlBlock_id);

    List<UmlBlockAlt> findByUmlBlock(UmlBlock umlBlock);

    @Override
    Optional<UmlBlockAlt> findById(UUID uuid);
}

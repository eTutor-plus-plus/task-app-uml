package at.jku.dke.task_app.uml.services;

import at.jku.dke.etutor.task_app.data.repositories.TaskRepository;
import at.jku.dke.task_app.uml.data.entities.UmlBlock;
import at.jku.dke.task_app.uml.data.entities.UmlBlockAlt;
import at.jku.dke.task_app.uml.data.entities.UmlTask;
import at.jku.dke.task_app.uml.data.repositories.UmlBlockAltRepository;
import at.jku.dke.task_app.uml.data.repositories.UmlBlockRepository;
import at.jku.dke.task_app.uml.data.repositories.UmlTaskRepository;
import at.jku.dke.task_app.uml.evaluation.atg.MyPlantUML_ATGListener;
import at.jku.dke.task_app.uml.evaluation.atg.gen.PlantUML_ATGLexer;
import at.jku.dke.task_app.uml.evaluation.atg.gen.PlantUML_ATGParser;
import at.jku.dke.task_app.uml.evaluation.atg.objects.*;
import jakarta.persistence.EntityNotFoundException;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UmlGenerationService {

    private final MessageSource messageSource;
    private final UmlBlockRepository umlBlockRepository;
    private final UmlBlockAltRepository umlBlockAltRepository;
    private final UmlTaskRepository umlTaskRepository;
    private final TaskRepository taskRepository;

    public UmlGenerationService(MessageSource messageSource, TaskRepository<UmlTask> repository, UmlBlockRepository umlBlockRepository, UmlBlockAltRepository umlBlockAltRepository, UmlTaskRepository umlTaskRepository, TaskRepository taskRepository) {
        this.messageSource = messageSource;
        this.umlBlockRepository = umlBlockRepository;
        this.umlBlockAltRepository = umlBlockAltRepository;
        this.umlTaskRepository = umlTaskRepository;
        this.taskRepository = taskRepository;
    }


    protected List<String> generateIdentifiersFromSolution(long id) {
        UmlTask task = umlTaskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task " + id + " does not exist."));
        List<UmlBlock> umlBlocks = umlBlockRepository.findByTask(task);
        List<String> identifiers = new ArrayList<>();
        List<UMLClass> umlClasses = new ArrayList<>();
        List<UMLAssociation> associations = new ArrayList<>();
        List<UMLRelationship> relationships = new ArrayList<>();
        List<UMLConstraints> constraints = new ArrayList<>();
        for (UmlBlock umlBlock : umlBlocks) {

            List<UmlBlockAlt> umlBlockAlts = umlBlockAltRepository.findByUmlBlock(umlBlock);

            for (UmlBlockAlt umlBlockAlt : umlBlockAlts) {
                String solution = umlBlockAlt.getUmlBlockAlternative();
                CharStream input = CharStreams.fromString(solution);
                PlantUML_ATGLexer lexer = new PlantUML_ATGLexer(input);
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                PlantUML_ATGParser parser = new PlantUML_ATGParser(tokens);
                ParseTree tree = parser.start();
                ParseTreeWalker walker = new ParseTreeWalker();
                MyPlantUML_ATGListener listener = new MyPlantUML_ATGListener();
                walker.walk(listener, tree);
                umlClasses.addAll(listener.getUmlClasses());
                relationships.addAll(listener.getRelationships());
                associations.addAll(listener.getAssociations());
                constraints.addAll(listener.getConstraints());
            }
            for (UMLClass umlClass : umlClasses) {
                if(!identifiers.stream().anyMatch(i -> i.equals(umlClass.getName()))) {
                    identifiers.add(umlClass.getName());
                }
                if (umlClass.getAttributes() != null) {
                    for (UMLAttribute attribute : umlClass.getAttributes()) {
                        if(!identifiers.stream().anyMatch(i -> i.equals(attribute.getName()))) {
                            identifiers.add(attribute.getName());
                        }
                    }
                }
            }
            for (UMLRelationship relationship : relationships) {
                for (UMLRelationshipEntity entity : relationship.getEntities()) {
                    if(!identifiers.stream().anyMatch(i -> i.equals(entity.getClassname()))) {
                        identifiers.add(entity.getClassname());
                    }
                }
            }
            for (UMLAssociation association : associations) {
                if(!identifiers.stream().anyMatch(i -> i.equals(association.getAssoClass()))) {
                    identifiers.add(association.getAssoClass());
                }
            }
            for (UMLConstraints constraint : constraints) {
               //prob not possible
            }


        }
        task.setIdentifiers(identifiers);
        return identifiers;
    }
    protected UMLResult generateResultsFromBlockAlt(long id) {
        UmlBlockAlt umlBlockAlt = umlBlockAltRepository.findById(UUID.fromString(String.valueOf(id))).orElseThrow(() -> new EntityNotFoundException("BlockAlt " + id + " does not exist."));
        String solution = umlBlockAlt.getUmlBlockAlternative();
        CharStream input = CharStreams.fromString(solution);
        PlantUML_ATGLexer lexer = new PlantUML_ATGLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PlantUML_ATGParser parser = new PlantUML_ATGParser(tokens);
        ParseTree tree = parser.start();
        ParseTreeWalker walker = new ParseTreeWalker();
        MyPlantUML_ATGListener listener = new MyPlantUML_ATGListener();
        walker.walk(listener, tree);
        UMLResult result = new UMLResult(listener.getUmlClasses(), listener.getRelationships(), listener.getAssociations(), listener.getConstraints());
        return result;
    }

    public UMLResult generateResultsFromSubmission(String submission) {
        CharStream input = CharStreams.fromString(submission);
        PlantUML_ATGLexer lexer = new PlantUML_ATGLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PlantUML_ATGParser parser = new PlantUML_ATGParser(tokens);
        ParseTree tree = parser.start();
        ParseTreeWalker walker = new ParseTreeWalker();
        MyPlantUML_ATGListener listener = new MyPlantUML_ATGListener();
        walker.walk(listener, tree);
        UMLResult result = new UMLResult(listener.getUmlClasses(), listener.getRelationships(), listener.getAssociations(), listener.getConstraints());
        return result;
    }

}

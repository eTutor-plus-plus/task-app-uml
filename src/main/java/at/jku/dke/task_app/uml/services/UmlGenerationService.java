package at.jku.dke.task_app.uml.services;

import at.jku.dke.etutor.task_app.data.repositories.TaskRepository;
import at.jku.dke.task_app.uml.data.entities.UmlBlock;
import at.jku.dke.task_app.uml.data.entities.UmlBlockAlt;
import at.jku.dke.task_app.uml.data.entities.UmlTask;
import at.jku.dke.task_app.uml.data.repositories.UmlBlockAltRepository;
import at.jku.dke.task_app.uml.data.repositories.UmlBlockRepository;
import at.jku.dke.task_app.uml.data.repositories.UmlTaskRepository;
import at.jku.dke.task_app.uml.evaluation.atg.MyPlantUML_ATGListener;
import at.jku.dke.task_app.uml.evaluation.atg.PlantUML_ATGErrorListener;
import at.jku.dke.task_app.uml.evaluation.atg.PlantUML_ATGException;
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

    protected int getMaxPossiblePoints(long task){
        UmlTask umlTask = umlTaskRepository.findById(task).orElseThrow(() -> new EntityNotFoundException("Task " + task + " does not exist."));
        List<List<String>> blocksText = new ArrayList<>();
        for (UmlBlock umlBlock : umlBlockRepository.findByTask(umlTask)) {
            List<String> block = new ArrayList<>();
            String altText = "";
            for (UmlBlockAlt umlBlockAlt : umlBlockAltRepository.findByUmlBlock(umlBlock)) {
                altText = umlBlockAlt.getUmlBlockAlternative();
                altText = altText.replaceAll("@startuml", "");
                altText = altText.replaceAll("@enduml", "");
                block.add(altText);
            }
            blocksText.add(block);
        }
        List<String> allCombinations = generateCombinations(blocksText);
        for (int i = 0; i < allCombinations.size(); i++) {
            String combination = allCombinations.get(i);
            combination = "@startuml\n" + combination + "\n@enduml";
            allCombinations.set(i, combination);
        }

        double maxPoints = 0;
        for (String combination : allCombinations) {
            double currentPoints = 0;
            UMLResult result = generateResultsFromText(combination);

            for(UMLClass umlClass : result.getUmlClasses()){
                if(umlClass.getPoints()!=0){
                currentPoints += umlClass.getPoints();
                }
                else{
                    currentPoints += umlTask.getClassPoints().doubleValue();
                }
                for(UMLAttribute attribute : umlClass.getAttributes()){
                    if(attribute.getPoints()!=0){
                        currentPoints += attribute.getPoints();
                    }
                    else{
                        currentPoints += umlTask.getAttributePoints().doubleValue();
                    }
                }
            }
            for(UMLRelationship relationship : result.getRelationships()){
                if(relationship.getPoints()!=0){
                    currentPoints += relationship.getPoints();
                }
                else{
                    currentPoints += umlTask.getRelationshipPoints().doubleValue();
                }
            }
            for(UMLAssociation association : result.getAssociations()){
                if(association.getPoints()!=0){
                    currentPoints += association.getPoints();
                }
                else{
                    currentPoints += umlTask.getAssociationPoints().doubleValue();
                }
            }
            for(UMLConstraints constraint : result.getConstraints()){
                if(constraint.getPoints()!=0){
                    currentPoints += constraint.getPoints();
                }
                else{
                    currentPoints += umlTask.getConstraintPoints().doubleValue();
                }
            }
            if(maxPoints<currentPoints){
                maxPoints = currentPoints;
            }
        }
        return (int) maxPoints;
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
                if(!identifiers.stream().anyMatch(i -> i.equals(association.getClass1()))) {
                    identifiers.add(association.getClass1());
                }
                if(!identifiers.stream().anyMatch(i -> i.equals(association.getClass2()))) {
                    identifiers.add(association.getClass2());
                }
            }
            for (UMLConstraints constraint : constraints) {
               if(!identifiers.stream().anyMatch(i -> i.equals(constraint.getRel1C1()))) {
                        identifiers.add(constraint.getRel1C1());
                    }
                    if(!identifiers.stream().anyMatch(i -> i.equals(constraint.getRel1C2()))) {
                        identifiers.add(constraint.getRel1C2());
                    }
                    if(!identifiers.stream().anyMatch(i -> i.equals(constraint.getRel2C1()))) {
                        identifiers.add(constraint.getRel2C1());
                    }
                    if(!identifiers.stream().anyMatch(i -> i.equals(constraint.getRel2C2()))) {
                        identifiers.add(constraint.getRel2C2());
                    }
            }


        }
        task.setIdentifiers(identifiers);
        return identifiers;
    }
    public UMLResult generateResultsFromBlockAlt(long id) {
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
        UMLResult result = new UMLResult(listener.getUmlClasses(), listener.getRelationships(), listener.getAssociations(), listener.getConstraints(), listener.getMultiRelationships(),listener.getNotes(),listener.getNoteConnections());
        return result;
    }

    public UMLResult generateResultsFromSubmission(String submission) {
        try {
            CharStream input = CharStreams.fromString(submission);
            PlantUML_ATGLexer lexer = new PlantUML_ATGLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            PlantUML_ATGParser parser = new PlantUML_ATGParser(tokens);
            parser.addErrorListener(new PlantUML_ATGErrorListener());
            ParseTree tree = parser.start();
            ParseTreeWalker walker = new ParseTreeWalker();
            MyPlantUML_ATGListener listener = new MyPlantUML_ATGListener();
            walker.walk(listener, tree);
            UMLResult result = new UMLResult(listener.getUmlClasses(), listener.getRelationships(), listener.getAssociations(), listener.getConstraints(), listener.getMultiRelationships(),listener.getNotes(),listener.getNoteConnections());
            return result;
        } catch (Exception e) {
            throw e;
        }
    }

    public UMLResult generateResultsFromText(String solution) {

        CharStream input = CharStreams.fromString(solution);
        PlantUML_ATGLexer lexer = new PlantUML_ATGLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PlantUML_ATGParser parser = new PlantUML_ATGParser(tokens);
        ParseTree tree = parser.start();
        ParseTreeWalker walker = new ParseTreeWalker();
        MyPlantUML_ATGListener listener = new MyPlantUML_ATGListener();
        walker.walk(listener, tree);
        UMLResult result = new UMLResult(listener.getUmlClasses(), listener.getRelationships(), listener.getAssociations(), listener.getConstraints(), listener.getMultiRelationships(),listener.getNotes(),listener.getNoteConnections());
        return result;
    }
    public  List<String> generateCombinations(List<List<String>> blocks) {
        List<String> result = new ArrayList<>();
        if (blocks == null || blocks.isEmpty()) {
            return result;
        }
        // Start the combination process
        generateCombinationsRecursive(blocks, result, "", 0);
        return result;
    }

    private void generateCombinationsRecursive(List<List<String>> blocks, List<String> result, String current, int depth) {
        // Base case: if the current depth equals the number of blocks, add the combination to the result list
        if (depth == blocks.size()) {
            result.add(current.trim());
            return;
        }
        // Iterate through each alternative in the current block
        for (String alternative : blocks.get(depth)) {
            generateCombinationsRecursive(blocks, result, current + " " + alternative, depth + 1);
        }
    }

}

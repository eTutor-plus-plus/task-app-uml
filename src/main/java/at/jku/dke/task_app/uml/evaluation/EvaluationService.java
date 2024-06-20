package at.jku.dke.task_app.uml.evaluation;

import at.jku.dke.etutor.task_app.dto.CriterionDto;
import at.jku.dke.etutor.task_app.dto.GradingDto;
import at.jku.dke.etutor.task_app.dto.SubmissionMode;
import at.jku.dke.etutor.task_app.dto.SubmitSubmissionDto;
import at.jku.dke.task_app.uml.data.entities.UmlBlock;
import at.jku.dke.task_app.uml.data.entities.UmlBlockAlt;
import at.jku.dke.task_app.uml.data.entities.UmlTask;
import at.jku.dke.task_app.uml.data.repositories.UmlBlockAltRepository;
import at.jku.dke.task_app.uml.data.repositories.UmlBlockRepository;
import at.jku.dke.task_app.uml.data.repositories.UmlTaskRepository;
import at.jku.dke.task_app.uml.dto.UmlSubmissionDto;
import at.jku.dke.task_app.uml.evaluation.atg.EvaluationResult;
import at.jku.dke.task_app.uml.evaluation.atg.objects.*;
import at.jku.dke.task_app.uml.services.UmlGenerationService;
import jakarta.persistence.EntityNotFoundException;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Service that evaluates submissions.
 */
@Service
public class EvaluationService {
    private static final Logger LOG = LoggerFactory.getLogger(EvaluationService.class);

    private final UmlTaskRepository taskRepository;
    private final MessageSource messageSource;
    private final UmlGenerationService umlGenerationService;
    private final UmlBlockRepository umlBlockRepository;
    private final UmlBlockAltRepository umlBlockAltRepository;

    /**
     * Creates a new instance of class {@link EvaluationService}.
     *
     * @param taskRepository The task repository.
     * @param messageSource  The message source.
     */
    public EvaluationService(UmlTaskRepository taskRepository, MessageSource messageSource, UmlGenerationService umlGenerationService, UmlBlockRepository umlBlockRepository, UmlBlockAltRepository umlBlockAltRepository) {
        this.taskRepository = taskRepository;
        this.messageSource = messageSource;
        this.umlGenerationService = umlGenerationService;
        this.umlBlockRepository = umlBlockRepository;
        this.umlBlockAltRepository = umlBlockAltRepository;
    }

    /**
     * Evaluates a input.
     *
     * @param submission The input to evaluate.
     * @return The evaluation result.
     */
    @Transactional
    public GradingDto evaluate(SubmitSubmissionDto<UmlSubmissionDto> submission) {
        // find task
        var task = this.taskRepository.findById(submission.taskId()).orElseThrow(() -> new EntityNotFoundException("Task " + submission.taskId() + " does not exist."));

        // evaluate
        var points = BigDecimal.ZERO;
        var criteria = new ArrayList<CriterionDto>();
        UMLResult umlResultSubmission = null;
        try {
            umlResultSubmission = umlGenerationService.generateResultsFromSubmission(submission.submission().input());
            criteria.add(new CriterionDto("Syntax", BigDecimal.ZERO, true, "Valid Syntax"));
            criteria.add(new CriterionDto("Image", BigDecimal.ZERO, true, generateImage(submission.submission().input())));

        } catch (Exception e) {
            LOG.error("Error while evaluating submission", e);
            criteria.add(new CriterionDto("Syntax", BigDecimal.ZERO, false, "Syntax Error" + e.getMessage()));
            return new GradingDto(task.getMaxPoints(), points, "", criteria);
        }
        System.out.println(umlResultSubmission);


        EvaluationResult evaluationResult = null;
        // evaluate
        if (task.getCompleteComparison()) {
            evaluationResult = fullyCompare(task, umlResultSubmission);
        } else {
            evaluationResult = fullyCompare(task, umlResultSubmission);
        }

        GradingDto gradingDto = generateFeedback(task, evaluationResult, submission, umlResultSubmission);
        return gradingDto;
    }

    private GradingDto generateFeedback(UmlTask task, EvaluationResult evaluationResult, SubmitSubmissionDto<UmlSubmissionDto> submission, UMLResult umlResultSubmission) {
        CriterionDto syntax = new CriterionDto("Syntax", BigDecimal.ZERO, true, "Valid Syntax");
        List<CriterionDto> criteria = new ArrayList<>();
        criteria.add(syntax);
        if (!areNamesInIdentifiers(umlResultSubmission, task.getIdentifiers())) {
            criteria.add(new CriterionDto("Identifiers", BigDecimal.ZERO, false, "Not all Identifiers are correct"));
        } else {

            criteria.add(new CriterionDto("Identifiers", BigDecimal.ZERO, true, "All Identifiers are correct"));
        }
        if (submission.mode().equals(SubmissionMode.RUN)) {
            criteria.add(new CriterionDto("Image", BigDecimal.ZERO, true, generateImage(submission.submission().input())));
            return new GradingDto(task.getMaxPoints(), BigDecimal.ZERO, "", criteria);
        }
        if(evaluationResult.getPoints() != task.getMaxPoints().intValue()){
            if(evaluationResult.getWrongClasses().isEmpty()&&evaluationResult.getWrongAttributes().isEmpty()&&evaluationResult.getWrongRelationships().isEmpty()&&evaluationResult.getWrongAssociations().isEmpty()&&evaluationResult.getWrongConstraints().isEmpty()) {
                criteria.add(new CriterionDto("Correctness", BigDecimal.ZERO, true, "Not the best Solution found!"));
            }
        }


        if (submission.feedbackLevel().equals(1)) {
            if (evaluationResult.getMissingClasses().size() > 0 || !evaluationResult.getWrongClasses().isEmpty()) {
                criteria.add(new CriterionDto("Classes", BigDecimal.ZERO, false, "Missing Classes: " + (evaluationResult.getMissingClasses().size() + evaluationResult.getWrongClasses().size())));
            }
            if (evaluationResult.getMissingAttributes().size() > 0 || !evaluationResult.getWrongAttributes().isEmpty()) {
                criteria.add(new CriterionDto("Attributes", BigDecimal.ZERO, false, "Missing Attributes: " + (evaluationResult.getMissingAttributes().size() + evaluationResult.getWrongAttributes().size())));
            }
            if (evaluationResult.getMissingAbstractClasses().size() > 0) {
                criteria.add(new CriterionDto("Abstract Classes", BigDecimal.ZERO, false, "Missing Abstract Classes: " + (evaluationResult.getMissingAbstractClasses().size())));
            }
            if (evaluationResult.getMissingRelationships().size() > 0 || !evaluationResult.getWrongRelationships().isEmpty()) {
                criteria.add(new CriterionDto("Relationships", BigDecimal.ZERO, false, "Missing Relationships: " + (evaluationResult.getMissingRelationships().size() + evaluationResult.getWrongRelationships().size())));
            }
            if (evaluationResult.getMissingAssociations().size() > 0 || !evaluationResult.getWrongAssociations().isEmpty()) {
                criteria.add(new CriterionDto("Associations", BigDecimal.ZERO, false, "Missing Associations: " + (evaluationResult.getMissingAssociations().size() + evaluationResult.getWrongAssociations().size())));
            }
            if (evaluationResult.getMissingConstraints().size() > 0 || !evaluationResult.getWrongConstraints().isEmpty()) {
                criteria.add(new CriterionDto("Constraints", BigDecimal.ZERO, false, "Missing Constraints: " + (evaluationResult.getMissingConstraints().size() + evaluationResult.getWrongConstraints().size())));
            }
        }
        if (submission.feedbackLevel().equals(2)) {
            if (evaluationResult.getMissingClasses().size() > 0 || !evaluationResult.getWrongClasses().isEmpty()) {
                criteria.add(new CriterionDto("Classes", BigDecimal.ZERO, false, "Missing Classes: " + evaluationResult.getMissingClasses().size() + "<br>association Wrong Classes: " + evaluationResult.getWrongClasses().size()));
            }
            if (evaluationResult.getMissingAttributes().size() > 0 || !evaluationResult.getWrongAttributes().isEmpty()) {
                criteria.add(new CriterionDto("Attributes", BigDecimal.ZERO, false, "Missing Attributes: " + evaluationResult.getMissingAttributes().size() + "<br> Wrong Attributes: " + evaluationResult.getWrongAttributes().size()));
            }
            if (evaluationResult.getMissingAbstractClasses().size() > 0) {
                criteria.add(new CriterionDto("Abstract Classes", BigDecimal.ZERO, false, "Missing Abstract Classes: " + evaluationResult.getMissingAbstractClasses().size()));
            }
            if (evaluationResult.getMissingRelationships().size() > 0 || !evaluationResult.getWrongRelationships().isEmpty()) {
                criteria.add(new CriterionDto("Relationships", BigDecimal.ZERO, false, "Missing Relationships: " + evaluationResult.getMissingRelationships().size() + "<br> Wrong Relationships: " + evaluationResult.getWrongRelationships().size()));
            }
            if (evaluationResult.getMissingAssociations().size() > 0 || !evaluationResult.getWrongAssociations().isEmpty()) {
                criteria.add(new CriterionDto("Associations", BigDecimal.ZERO, false, "Missing Associations: " + evaluationResult.getMissingAssociations().size() + "<br> Wrong Associations: " + evaluationResult.getWrongAssociations().size()));
            }
            if (evaluationResult.getMissingConstraints().size() > 0 || !evaluationResult.getWrongConstraints().isEmpty()) {
                criteria.add(new CriterionDto("Constraints", BigDecimal.ZERO, false, "Missing Constraints: " + evaluationResult.getMissingConstraints().size() + "<br>Wrong Constraints: " + evaluationResult.getWrongConstraints().size()));
            }
        }
        if (submission.feedbackLevel().equals(3)) {
            if (evaluationResult.getMissingClasses().size() > 0 || !evaluationResult.getWrongClasses().isEmpty()) {
                String s = "Missing Classes: ";
                for (UMLClass umlClass : evaluationResult.getMissingClasses()) {
                    s += umlClass.getName() + ", ";
                }
                s += "<br> Wrong Classes: ";
                for (UMLClass umlClass : evaluationResult.getWrongClasses()) {
                    s += umlClass.getName() + ", ";
                }
                criteria.add(new CriterionDto("Classes", BigDecimal.ZERO, false, s));
            }
            if (evaluationResult.getMissingAttributes().size() > 0 || !evaluationResult.getWrongAttributes().isEmpty()) {
                String s = "Missing Attributes: ";
                for (UMLAttribute umlAttribute : evaluationResult.getMissingAttributes()) {
                    s += umlAttribute.getName() + ", ";
                }
                s += "<br> Wrong Attributes: ";
                for (UMLAttribute umlAttribute : evaluationResult.getWrongAttributes()) {
                    s += umlAttribute.getName() + ", ";

                }
                criteria.add(new CriterionDto("Attributes", BigDecimal.ZERO, false, s));
            }
            if (evaluationResult.getMissingAbstractClasses().size() > 0) {
                String s = "Missing Abstract Classes: ";
                for (UMLClass umlClass : evaluationResult.getMissingAbstractClasses()) {
                    s += umlClass.getName() + ", ";
                }


                criteria.add(new CriterionDto("Abstract Classes", BigDecimal.ZERO, false, s));
            }
            if (evaluationResult.getMissingRelationships().size() > 0 || !evaluationResult.getWrongRelationships().isEmpty()) {
                String s = "Missing Relationships: ";
                for (UMLRelationship umlRelationship : evaluationResult.getMissingRelationships()) {
                    s += umlRelationship.getEntities().getFirst().getClassname() + "--" + umlRelationship.getEntities().getLast().getClassname() + ", ";
                }
                s += "<br> Wrong Relationships: ";
                for (UMLRelationship umlRelationship : evaluationResult.getWrongRelationships()) {
                    s += umlRelationship.getEntities().getFirst().getClassname() + umlRelationship.getType() + umlRelationship.getEntities().getLast().getClassname() + ", ";
                }
                criteria.add(new CriterionDto("Relationships", BigDecimal.ZERO, false, s));
            }
            if (evaluationResult.getMissingAssociations().size() > 0 || !evaluationResult.getWrongAssociations().isEmpty()) {
                String s = "Missing Associations: ";
                for (UMLAssociation umlAssociation : evaluationResult.getMissingAssociations()) {
                    s += umlAssociation.getAssoClass() + "--" + umlAssociation.getClass1() + "--" + umlAssociation.getClass2() + ", ";
                }
                s += "<br> Wrong Associations: ";
                for (UMLAssociation umlAssociation : evaluationResult.getWrongAssociations()) {
                    s += umlAssociation.getAssoClass() + "--" + umlAssociation.getClass1() + "--" + umlAssociation.getClass2() + ", ";
                }
                criteria.add(new CriterionDto("Associations", BigDecimal.ZERO, false, s));
            }
            if (evaluationResult.getMissingConstraints().size() > 0 || !evaluationResult.getWrongConstraints().isEmpty()) {
                String s = "Missing Constraints: ";
                for (UMLConstraints umlConstraints : evaluationResult.getMissingConstraints()) {
                    s += umlConstraints.getRel1C1() + "--" + umlConstraints.getRel1C2() + "--" + umlConstraints.getRel2C1() + "--" + umlConstraints.getRel2C2() + ", ";
                }
                s += "<br> Wrong Constraints: ";
                for (UMLConstraints umlConstraints : evaluationResult.getWrongConstraints()) {
                    s += umlConstraints.getRel1C1() + "--" + umlConstraints.getRel1C2() + "--" + umlConstraints.getRel2C1() + "--" + umlConstraints.getRel2C2() + ", ";
                }
                criteria.add(new CriterionDto("Constraints", BigDecimal.ZERO, false, s));
            }

            if(evaluationResult.getWrongMultiRelationships().size() > 0){
                String s = "Wrong ternaer Relationship: ";
                for (UMLMultiRelationship multiRelationship : evaluationResult.getWrongMultiRelationships()){
                    s +=  multiRelationship.getName() +", ";
                }
                criteria.add(new CriterionDto("MultiRelationships", BigDecimal.ZERO, false, s));
            }


        }
        criteria.add(new CriterionDto("Image", BigDecimal.ZERO, true, generateImage(submission.submission().input())));


        return new GradingDto(task.getMaxPoints(), BigDecimal.valueOf(evaluationResult.getPoints()), "", criteria);


    }

    private GradingDto partiallyCompare(UmlTask task, UMLResult umlResultSubmission) {
        int allpoints = 0;
        List<UMLClass> allMissingClasses = new ArrayList<>();
        List<UMLAttribute> allMissingAttributes = new ArrayList<>();
        List<UMLClass> allMissingAbstractClasses = new ArrayList<>();
        List<UMLRelationship> allMissingRelationships = new ArrayList<>();
        List<UMLAssociation> allMissingAssociations = new ArrayList<>();
        List<UMLConstraints> allMissingConstraints = new ArrayList<>();

        for (UmlBlock umlBlock : umlBlockRepository.findByTask(task)) {
            boolean correct = true;
            int points = 0;
            List<UMLClass> missingClasses = new ArrayList<>();
            List<UMLAttribute> missingAttributes = new ArrayList<>();
            List<UMLClass> missingAbstractClasses = new ArrayList<>();
            List<UMLRelationship> missingRelationships = new ArrayList<>();
            List<UMLAssociation> missingAssociations = new ArrayList<>();
            List<UMLConstraints> missingConstraints = new ArrayList<>();
            for (UmlBlockAlt umlBlockAlt : umlBlockAltRepository.findByUmlBlock(umlBlock)) {
                int currentPoints = 0;
                List<UMLClass> currentMissingClasses = new ArrayList<>();
                List<UMLAttribute> currentMissingAttributes = new ArrayList<>();
                List<UMLClass> currentMissingAbstractClasses = new ArrayList<>();
                List<UMLRelationship> currentMissingRelationships = new ArrayList<>();
                List<UMLAssociation> currentMissingAssociations = new ArrayList<>();
                List<UMLConstraints> currentMissingConstraints = new ArrayList<>();
                UMLResult umlResultSolution = umlGenerationService.generateResultsFromBlockAlt(umlBlockAlt.getId());
                //vergleich
                for (UMLClass umlClassSubmission : umlResultSubmission.getUmlClasses()) {
                    for (UMLClass umlClassSolution : umlResultSolution.getUmlClasses()) {
                        if (umlClassSubmission.getName().equals(umlClassSolution.getName())) {
                            if (umlClassSubmission.isAbstract() == umlClassSolution.isAbstract()) {
                                currentPoints += umlClassSolution.getPoints();

                                for (UMLAttribute attributeSubmission : umlClassSubmission.getAttributes()) {
                                    for (UMLAttribute attributeSolution : umlClassSolution.getAttributes()) {
                                        if (attributeSubmission.getName().equals(attributeSolution.getName())) {
                                            if (attributeSubmission.getType().equals(attributeSolution.getType())) {
                                                currentPoints += attributeSolution.getPoints();
                                                break;
                                            }
                                        }
                                    }
                                    currentMissingAttributes.add(attributeSubmission);
                                    correct = false;
                                }
                                break;
                            }
                            currentMissingAbstractClasses.add(umlClassSubmission);
                            correct = false;
                        }

                    }
                    correct = false;
                    currentMissingClasses.add(umlClassSubmission);
                }
                for (UMLRelationship relationshipSubmission : umlResultSubmission.getRelationships()) {
                    for (UMLRelationship relationshipSolution : umlResultSolution.getRelationships()) {
                        if (relationshipSubmission.getType().equals(relationshipSolution.getType())) {
                            boolean allEntitiesCorrect = true;
                            for (UMLRelationshipEntity entitySubmission : relationshipSubmission.getEntities()) {
                                for (UMLRelationshipEntity entitySolution : relationshipSolution.getEntities()) {
                                    if (entitySubmission.getClassname().equals(entitySolution.getClassname())) {
                                        if (entitySubmission.getMultiplicity().equals(entitySolution.getMultiplicity())) {


                                            break;
                                        }
                                    }
                                }
                                correct = false;
                                allEntitiesCorrect = false;

                            }
                            if (allEntitiesCorrect) {
                                currentPoints += relationshipSolution.getPoints();
                                break;
                            }
                            break;
                        }
                    }
                    correct = false;
                    currentMissingRelationships.add(relationshipSubmission);
                }
                for (UMLAssociation associationSubmission : umlResultSubmission.getAssociations()) {
                    for (UMLAssociation associationSolution : umlResultSolution.getAssociations()) {
                        if (associationSubmission.getAssoClass().equals(associationSolution.getAssoClass())) {
                            if (associationSubmission.getClass1().equals(associationSolution.getClass1())) {
                                if (associationSubmission.getClass2().equals(associationSolution.getClass2())) {
                                    currentPoints += associationSolution.getPoints();
                                    break;
                                }
                            }
                        }
                    }
                    correct = false;
                    currentMissingAssociations.add(associationSubmission);
                }

                for (UMLConstraints constraintSubmission : umlResultSubmission.getConstraints()) {
                    for (UMLConstraints constraintSolution : umlResultSolution.getConstraints()) {
                        if (constraintSubmission.getRel1C1().equals(constraintSolution.getRel1C1())) {
                            if (constraintSubmission.getRel1C2().equals(constraintSolution.getRel1C2())) {
                                if (constraintSubmission.getRel2C1().equals(constraintSolution.getRel2C1())) {
                                    if (constraintSubmission.getRel2C2().equals(constraintSolution.getRel2C2())) {
                                        currentPoints += constraintSolution.getPoints();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    correct = false;
                    currentMissingConstraints.add(constraintSubmission);
                }


                if (currentPoints > points) {
                    points = currentPoints;
                    missingClasses = currentMissingClasses;
                    missingAttributes = currentMissingAttributes;
                    missingAbstractClasses = currentMissingAbstractClasses;
                }
                if (correct) {
                    break;
                }
            }
            allpoints += points;
            allMissingClasses.addAll(missingClasses);
            allMissingAttributes.addAll(missingAttributes);
            allMissingAbstractClasses.addAll(missingAbstractClasses);
            allMissingRelationships.addAll(missingRelationships);
            allMissingAssociations.addAll(missingAssociations);
            allMissingConstraints.addAll(missingConstraints);
        }

        return new GradingDto(task.getMaxPoints(), BigDecimal.valueOf(allpoints), "", new ArrayList<>());
    }

    private String generateImage(String submission) {
        try {
            SourceStringReader reader = new SourceStringReader(submission);
            try (var baos = new ByteArrayOutputStream()) {
                reader.outputImage(baos, new FileFormatOption(FileFormat.PNG));
                String base64EncodedImage = Base64.getEncoder().encodeToString(baos.toByteArray());
                return "<img src=\"data:image/png;base64," + base64EncodedImage + "\" alt=\"UML-Diagramm\">";
            }
        } catch (Exception ex) {
            return "<p>" + ex.getMessage() + "</p>";
        }
    }

    private EvaluationResult fullyCompare(UmlTask task, UMLResult umlResultSubmission) {
        List<List<String>> blocksText = new ArrayList<>();
        for (UmlBlock umlBlock : umlBlockRepository.findByTask(task)) {
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
        List<String> allCombinations = umlGenerationService.generateCombinations(blocksText);
        for (int i = 0; i < allCombinations.size(); i++) {
            String combination = allCombinations.get(i);
            combination = "@startuml\n" + combination + "\n@enduml";
            allCombinations.set(i, combination);
        }
        double bestPoints = 0;
        UMLResult currentBestResult = null;
        EvaluationResult bestevaluationResult = new EvaluationResult();

        for (String combination : allCombinations) {
            double points = 0;
            EvaluationResult evaluationResult = new EvaluationResult();
            UMLResult umlResultSolution = umlGenerationService.generateResultsFromText(combination);


            for (UMLClass umlClassSubmission : umlResultSubmission.getUmlClasses()) {
                boolean isClassCorrect = false;
                for (UMLClass umlClassSolution : umlResultSolution.getUmlClasses()) {
                    if (umlClassSubmission.getName().equals(umlClassSolution.getName())) {
                        if (umlClassSubmission.isAbstract() == umlClassSolution.isAbstract()) {
                            if (umlClassSubmission.getParentClass() != null) {
                                if (umlClassSolution.getParentClass() != null) {
                                    if (umlClassSubmission.getParentClass().getName().equals(umlClassSolution.getParentClass().getName())) {
                                        if (umlClassSolution.getPoints() == 0) {
                                            points += task.getClassPoints().doubleValue();
                                        } else {
                                            points += umlClassSolution.getPoints();
                                        }
                                        isClassCorrect = true;
                                        for (UMLAttribute attributeSubmission : umlClassSubmission.getAttributes()) {
                                            boolean isAttributecorrect = false;
                                            for (UMLAttribute attributeSolution : umlClassSolution.getAttributes()) {
                                                if (attributeSubmission.getName().equals(attributeSolution.getName())) {
                                                    if (attributeSubmission.getType().equals(attributeSolution.getType())) {
                                                        if (attributeSolution.getPoints() == 0) {
                                                            points += task.getAttributePoints().doubleValue();
                                                        } else {
                                                            points += attributeSolution.getPoints();
                                                        }
                                                        isAttributecorrect = true;
                                                        break;
                                                    }
                                                }
                                            }
                                            if (!isAttributecorrect) {
                                                evaluationResult.getWrongAttributes().add(attributeSubmission);
                                            }
                                        }
                                        for (UMLAttribute attributeSolution : umlClassSolution.getAttributes()) {
                                            boolean isAttributecorrect = false;
                                            for (UMLAttribute attributeSubmission : umlClassSubmission.getAttributes()) {
                                                if (attributeSubmission.getName().equals(attributeSolution.getName())) {
                                                    if (attributeSubmission.getType().equals(attributeSolution.getType())) {
                                                        isAttributecorrect = true;
                                                        break;
                                                    }
                                                }
                                            }
                                            if (!isAttributecorrect) {
                                                evaluationResult.getMissingAttributes().add(attributeSolution);
                                            }
                                        }
                                        break;
                                    }
                                }
                                evaluationResult.getWrongClasses().add(umlClassSubmission);
                            } else if (umlClassSolution.getParentClass() == null) {
                                if (umlClassSolution.getPoints() == 0) {
                                    points += task.getClassPoints().doubleValue();
                                } else {
                                    points += umlClassSolution.getPoints();
                                }
                                isClassCorrect = true;
                                for (UMLAttribute attributeSubmission : umlClassSubmission.getAttributes()) {
                                    boolean isAttributecorrect = false;
                                    for (UMLAttribute attributeSolution : umlClassSolution.getAttributes()) {
                                        if (attributeSubmission.getName().equals(attributeSolution.getName())) {
                                            if (attributeSubmission.getType().equals(attributeSolution.getType())) {
                                                if (attributeSolution.getPoints() == 0) {
                                                    points += task.getAttributePoints().doubleValue();
                                                } else {
                                                    points += attributeSolution.getPoints();
                                                }
                                                isAttributecorrect = true;
                                                break;
                                            }
                                        }
                                    }
                                    if (!isAttributecorrect) {
                                        evaluationResult.getWrongAttributes().add(attributeSubmission);
                                    }
                                }
                                for (UMLAttribute attributeSolution : umlClassSolution.getAttributes()) {
                                    boolean isAttributecorrect = false;
                                    for (UMLAttribute attributeSubmission : umlClassSubmission.getAttributes()) {
                                        if (attributeSubmission.getName().equals(attributeSolution.getName())) {
                                            if (attributeSubmission.getType().equals(attributeSolution.getType())) {
                                                isAttributecorrect = true;
                                                break;
                                            }
                                        }
                                    }
                                    if (!isAttributecorrect) {
                                        evaluationResult.getMissingAttributes().add(attributeSolution);
                                    }
                                }
                                break;

                            }
                            evaluationResult.getWrongClasses().add(umlClassSubmission);
                        }
                        evaluationResult.getMissingAbstractClasses().add(umlClassSubmission);
                    }
                }
                if (!isClassCorrect) {
                    evaluationResult.getWrongClasses().add(umlClassSubmission);
                }
            }
            for (UMLClass umlClassSolution : umlResultSolution.getUmlClasses()) {
                boolean isClassMissing = false;
                for (UMLClass umlClassSubmission : umlResultSubmission.getUmlClasses()) {
                    if (umlClassSolution.getName().equals(umlClassSubmission.getName())) {
                        isClassMissing = true;
                        break;
                    }
                }
                if (!isClassMissing) {
                    evaluationResult.getMissingClasses().add(umlClassSolution);
                }
            }


            for (UMLRelationship relationshipSubmission : umlResultSubmission.getRelationships()) {
                boolean isCorrectRelationship = false;
                for (UMLRelationship relationshipSolution : umlResultSolution.getRelationships()) {
                    if (relationshipSolution.getEntities().stream().anyMatch(e -> e.getClassname().equals(relationshipSubmission.getEntities().getFirst().getClassname()))) {
                        if (relationshipSolution.getEntities().stream().anyMatch(e -> e.getClassname().equals(relationshipSubmission.getEntities().getLast().getClassname()))) {
                            //compare multiplicity of the two entities with matching name
                            if (relationshipSolution.getEntities().stream().filter(e -> e.getClassname().equals(relationshipSubmission.getEntities().getFirst().getClassname())).findFirst().get().getMultiplicity().equals(relationshipSubmission.getEntities().getFirst().getMultiplicity())) {
                                if (relationshipSolution.getEntities().stream().filter(e -> e.getClassname().equals(relationshipSubmission.getEntities().getLast().getClassname())).findFirst().get().getMultiplicity().equals(relationshipSubmission.getEntities().getLast().getMultiplicity())) {
                                    if (relationshipSubmission.getType().equals(relationshipSolution.getType())) {
                                        if (relationshipSubmission.getName().equals(relationshipSolution.getName())) {
                                            if (relationshipSolution.getPoints() == 0) {
                                                points += task.getRelationshipPoints().doubleValue();
                                            } else {
                                                points += relationshipSolution.getPoints();
                                            }

                                            isCorrectRelationship = true;
                                            break;
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
                if (!isCorrectRelationship) {
                    evaluationResult.getWrongRelationships().add(relationshipSubmission);
                }
            }
            for (UMLRelationship relationshipSolution : umlResultSolution.getRelationships()) {
                boolean isCorrectRelationship = false;
                for (UMLRelationship relationshipSubmission : umlResultSubmission.getRelationships()) {

                    if (relationshipSolution.getEntities().stream().anyMatch(e -> e.getClassname().equals(relationshipSubmission.getEntities().getFirst().getClassname()))) {
                        if (relationshipSolution.getEntities().stream().anyMatch(e -> e.getClassname().equals(relationshipSubmission.getEntities().getLast().getClassname()))) {
                            //compare multiplicity of the two entities with matching name
                            isCorrectRelationship = true;
                            break;

                        }
                    }
                }
                if (!isCorrectRelationship) {
                    evaluationResult.getMissingRelationships().add(relationshipSolution);
                }
            }


            for (UMLAssociation associationSubmission : umlResultSubmission.getAssociations()) {
                boolean isCorrectAssociation = false;
                for (UMLAssociation associationSolution : umlResultSolution.getAssociations()) {
                    if (associationSubmission.getAssoClass().equals(associationSolution.getAssoClass())) {
                        if (associationSubmission.getClass1().equals(associationSolution.getClass1())) {
                            if (associationSubmission.getClass2().equals(associationSolution.getClass2())) {
                                if (associationSolution.getPoints() == 0) {
                                    points += task.getAssociationPoints().doubleValue();
                                } else {
                                    points += associationSolution.getPoints();
                                }
                                isCorrectAssociation = true;
                                break;
                            }
                        } else if (associationSubmission.getClass1().equals(associationSolution.getClass2())) {
                            if (associationSubmission.getClass2().equals(associationSolution.getClass1())) {
                                if (associationSolution.getPoints() == 0) {
                                    points += task.getAssociationPoints().doubleValue();
                                } else {
                                    points += associationSolution.getPoints();
                                }
                                isCorrectAssociation = true;
                            }
                        }
                    }
                }
                if (!isCorrectAssociation) {
                    evaluationResult.getWrongAssociations().add(associationSubmission);
                }
            }
            for (UMLAssociation associationSolution : umlResultSolution.getAssociations()) {
                boolean isCorrectAssociation = false;
                for (UMLAssociation associationSubmission : umlResultSubmission.getAssociations()) {
                    if (associationSubmission.getAssoClass().equals(associationSolution.getAssoClass())) {
                        if (associationSubmission.getClass1().equals(associationSolution.getClass1())) {
                            if (associationSubmission.getClass2().equals(associationSolution.getClass2())) {
                                isCorrectAssociation = true;
                                break;
                            }
                        } else if (associationSubmission.getClass1().equals(associationSolution.getClass2())) {
                            if (associationSubmission.getClass2().equals(associationSolution.getClass1())) {
                                isCorrectAssociation = true;
                                break;
                            }

                        }
                    }

                    evaluationResult.getMissingAssociations().add(associationSolution);
                }
            }


            for (UMLConstraints constraintSubmission : umlResultSubmission.getConstraints()) {
                boolean isCorrectConstraint = false;
                for (UMLConstraints constraintSolution : umlResultSolution.getConstraints()) {
                    if (constraintSubmission.getRel1C1().equals(constraintSolution.getRel1C1())) {
                        if (constraintSubmission.getRel1C2().equals(constraintSolution.getRel1C2())) {
                            if (constraintSubmission.getRel2C1().equals(constraintSolution.getRel2C1())) {
                                if (constraintSubmission.getRel2C2().equals(constraintSolution.getRel2C2())) {
                                    if (constraintSolution.getType().equals(constraintSubmission.getType())) {
                                        if (constraintSolution.getPoints() == 0) {
                                            points += task.getConstraintPoints().doubleValue();
                                        } else {
                                            points += constraintSolution.getPoints();
                                        }
                                        isCorrectConstraint = true;
                                        break;
                                    }
                                }
                            } else if (constraintSubmission.getRel2C1().equals(constraintSolution.getRel2C2())) {
                                if (constraintSubmission.getRel2C2().equals(constraintSolution.getRel2C1())) {
                                    if (constraintSolution.getType().equals(constraintSubmission.getType())) {
                                        if (constraintSolution.getPoints() == 0) {
                                            points += task.getConstraintPoints().doubleValue();
                                        } else {
                                            points += constraintSolution.getPoints();
                                        }
                                        isCorrectConstraint = true;
                                        break;
                                    }
                                }

                            }
                        }
                    } else if (constraintSubmission.getRel1C1().equals(constraintSolution.getRel1C2())) {
                        if (constraintSubmission.getRel1C2().equals(constraintSolution.getRel1C1())) {
                            if (constraintSubmission.getRel2C1().equals(constraintSolution.getRel2C1())) {
                                if (constraintSubmission.getRel2C2().equals(constraintSolution.getRel2C2())) {
                                    if (constraintSolution.getType().equals(constraintSubmission.getType())) {
                                        if (constraintSolution.getPoints() == 0) {
                                            points += task.getConstraintPoints().doubleValue();
                                        } else {
                                            points += constraintSolution.getPoints();
                                        }
                                        isCorrectConstraint = true;
                                        break;
                                    }
                                }
                            } else if (constraintSubmission.getRel2C1().equals(constraintSolution.getRel2C2())) {
                                if (constraintSubmission.getRel2C2().equals(constraintSolution.getRel2C1())) {
                                    if (constraintSolution.getType().equals(constraintSubmission.getType())) {
                                        if (constraintSolution.getPoints() == 0) {
                                            points += task.getConstraintPoints().doubleValue();
                                        } else {
                                            points += constraintSolution.getPoints();
                                        }
                                        isCorrectConstraint = true;
                                        break;
                                    }
                                }

                            }
                        }
                    }

                }
                if (!isCorrectConstraint) {
                    evaluationResult.getWrongConstraints().add(constraintSubmission);
                }
            }
            for (UMLConstraints constraintSolution : umlResultSolution.getConstraints()) {
                boolean isCorrectConstraint = false;
                for (UMLConstraints constraintSubmission : umlResultSubmission.getConstraints()) {
                    if (constraintSubmission.getRel1C1().equals(constraintSolution.getRel1C1())) {
                        if (constraintSubmission.getRel1C2().equals(constraintSolution.getRel1C2())) {
                            if (constraintSubmission.getRel2C1().equals(constraintSolution.getRel2C1())) {
                                if (constraintSubmission.getRel2C2().equals(constraintSolution.getRel2C2())) {
                                    isCorrectConstraint = true;
                                    break;
                                }
                            } else if (constraintSubmission.getRel2C1().equals(constraintSolution.getRel2C2())) {
                                if (constraintSubmission.getRel2C2().equals(constraintSolution.getRel2C1())) {
                                    isCorrectConstraint = true;
                                    break;
                                }

                            }
                        }
                    } else if (constraintSubmission.getRel1C1().equals(constraintSolution.getRel1C2())) {
                        if (constraintSubmission.getRel1C2().equals(constraintSolution.getRel1C1())) {
                            if (constraintSubmission.getRel2C1().equals(constraintSolution.getRel2C1())) {
                                if (constraintSubmission.getRel2C2().equals(constraintSolution.getRel2C2())) {
                                    isCorrectConstraint = true;
                                    break;
                                }
                            } else if (constraintSubmission.getRel2C1().equals(constraintSolution.getRel2C2())) {
                                if (constraintSubmission.getRel2C2().equals(constraintSolution.getRel2C1())) {
                                    isCorrectConstraint = true;
                                    break;
                                }
                            }
                        }
                    }

                }
                if (!isCorrectConstraint) {
                    evaluationResult.getMissingConstraints().add(constraintSolution);
                }
            }

            for(UMLMultiRelationship multiRelationship : umlResultSubmission.getMultiRelationships())
            {
                boolean isCorrectMultiRelationship = false;
                for(UMLMultiRelationship multiRelationshipSolution : umlResultSolution.getMultiRelationships())
                {
                    if(multiRelationship.getName().equals(multiRelationshipSolution.getName()))
                    {
                        String connectedNoteSolution = umlResultSolution.getNoteConnections().stream().filter(noteConnection -> noteConnection.getClassName().equals(multiRelationshipSolution.getName())).findFirst().get().getNoteName();
                        if(umlResultSubmission.getNoteConnections().stream().anyMatch(e -> e.getClassName().equals(multiRelationship.getName())&& e.getNoteName().equals(connectedNoteSolution)))
                        {
                            isCorrectMultiRelationship = true;
                            break;
                        }
                    }

                }
                if(!isCorrectMultiRelationship)
                {
                    evaluationResult.getWrongMultiRelationships().add(multiRelationship);
                }
            }

            points = points - evaluationResult.getMissingAttributes().size() - evaluationResult.getMissingAssociations().size() - evaluationResult.getMissingClasses().size() - evaluationResult.getMissingConstraints().size() - evaluationResult.getMissingRelationships().size();
            if (points < 0) {
                points = 0;
            }
            if (points >= bestPoints) {
                bestPoints = points;
                currentBestResult = umlResultSolution;
                bestevaluationResult = evaluationResult;
            }

        }
        bestevaluationResult.setPoints((int)bestPoints);
        return bestevaluationResult;
    }




    public boolean areNamesInIdentifiers(UMLResult umlResult, List<String> identifiers) {
        // Check classes
        for (UMLClass umlClass : umlResult.getUmlClasses()) {
            if (!identifiers.contains(umlClass.getName())) {
                return false;
            }
            for (UMLAttribute attribute : umlClass.getAttributes()) {
                if (!identifiers.contains(attribute.getName())) {
                    return false;
                }
            }
        }

        // Check relationships
        for (UMLRelationship relationship : umlResult.getRelationships()) {
            for (UMLRelationshipEntity entity : relationship.getEntities()) {
                if (!identifiers.contains(entity.getClassname())) {
                    return false;
                }
            }
        }

        // Check associations
        for (UMLAssociation association : umlResult.getAssociations()) {
            if (!identifiers.contains(association.getAssoClass())) {
                return false;
            }
            if (!identifiers.contains(association.getClass1())) {
                return false;
            }
            if (!identifiers.contains(association.getClass2())) {
                return false;
            }
        }

        // Check constraints
        for (UMLConstraints constraint : umlResult.getConstraints()) {
            if (!identifiers.contains(constraint.getRel1C1())) {
                return false;
            }
            if (!identifiers.contains(constraint.getRel1C2())) {
                return false;
            }
            if (!identifiers.contains(constraint.getRel2C1())) {
                return false;
            }
            if (!identifiers.contains(constraint.getRel2C2())) {
                return false;
            }
        }

        // If all checks pass, all names are in the identifiers list
        return true;
    }
}

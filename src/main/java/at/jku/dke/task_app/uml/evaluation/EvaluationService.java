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
            criteria.add(new CriterionDto("Syntax", null, true, "Valid Syntax"));
            criteria.add(new CriterionDto("Image", null, true, generateImage(submission.submission().input())));

        } catch (Exception e) {
            LOG.error("Error while evaluating submission", e);
            criteria.add(new CriterionDto("Syntax", null, false, "Syntax Error" + e.getMessage()));
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
        CriterionDto syntax = new CriterionDto("Syntax", null, true, "Valid Syntax");
        List<CriterionDto> criteria = new ArrayList<>();
        criteria.add(syntax);
        List<String> wrongIdentifier = wrongIdentifiers(umlResultSubmission, task.getIdentifiers());
        if (wrongIdentifier.size() > 0 || !wrongIdentifier.isEmpty()) {
            criteria.add(new CriterionDto("Identifiers", null, false, "Wrong Identifiers: " + wrongIdentifier.toString()));
        } else {

            criteria.add(new CriterionDto("Identifiers", null, true, "All Identifiers are correct"));
        }
        if (submission.mode().equals(SubmissionMode.RUN)) {
            criteria.add(new CriterionDto("Image", null, true, generateImage(submission.submission().input())));
            return new GradingDto(task.getMaxPoints(), BigDecimal.ZERO, "", criteria);
        }
        if (evaluationResult.getPoints() != task.getMaxPoints().intValue()) {
            if (evaluationResult.getWrongClasses().isEmpty() && evaluationResult.getWrongAttributes().isEmpty() && evaluationResult.getWrongRelationships().isEmpty() && evaluationResult.getWrongAssociations().isEmpty() && evaluationResult.getWrongConstraints().isEmpty()) {
                criteria.add(new CriterionDto("Correctness", null, true, "Not the best Solution found!"));
            }
        }


        if (submission.feedbackLevel().equals(1)) {
            if (evaluationResult.getMissingClasses().size() > 0 || !evaluationResult.getWrongClasses().isEmpty()) {
                criteria.add(new CriterionDto("Classes", null, false, "Missing Classes: " + (evaluationResult.getMissingClasses().size() + evaluationResult.getWrongClasses().size())));
            }
            if (evaluationResult.getMissingAttributes().size() > 0 || !evaluationResult.getWrongAttributes().isEmpty()) {
                criteria.add(new CriterionDto("Attributes", null, false, "Missing Attributes: " + (evaluationResult.getMissingAttributes().size() + evaluationResult.getWrongAttributes().size())));
            }
            if (evaluationResult.getMissingAbstractClasses().size() > 0) {
                criteria.add(new CriterionDto("Abstract Classes", null, false, "Missing Abstract Classes: " + (evaluationResult.getMissingAbstractClasses().size())));
            }
            if (evaluationResult.getMissingRelationships().size() > 0 || !evaluationResult.getWrongRelationships().isEmpty()) {
                criteria.add(new CriterionDto("Relationships", null, false, "Missing Relationships: " + (evaluationResult.getMissingRelationships().size() + evaluationResult.getWrongRelationships().size())));
            }
            if (evaluationResult.getMissingAssociations().size() > 0 || !evaluationResult.getWrongAssociations().isEmpty()) {
                criteria.add(new CriterionDto("Associations", null, false, "Missing Associations: " + (evaluationResult.getMissingAssociations().size() + evaluationResult.getWrongAssociations().size())));
            }
            if (evaluationResult.getMissingConstraints().size() > 0 || !evaluationResult.getWrongConstraints().isEmpty()) {
                criteria.add(new CriterionDto("Constraints", null, false, "Missing Constraints: " + (evaluationResult.getMissingConstraints().size() + evaluationResult.getWrongConstraints().size())));
            }
        }
        if (submission.feedbackLevel().equals(2)) {
            if (evaluationResult.getMissingClasses().size() > 0 || !evaluationResult.getWrongClasses().isEmpty()) {
                criteria.add(new CriterionDto("Classes", null, false, "Missing Classes: " + evaluationResult.getMissingClasses().size() + "<br>association Wrong Classes: " + evaluationResult.getWrongClasses().size()));
            }
            if (evaluationResult.getMissingAttributes().size() > 0 || !evaluationResult.getWrongAttributes().isEmpty()) {
                criteria.add(new CriterionDto("Attributes", null, false, "Missing Attributes: " + evaluationResult.getMissingAttributes().size() + "<br> Wrong Attributes: " + evaluationResult.getWrongAttributes().size()));
            }
            if (evaluationResult.getMissingAbstractClasses().size() > 0) {
                criteria.add(new CriterionDto("Abstract Classes", null, false, "Missing Abstract Classes: " + evaluationResult.getMissingAbstractClasses().size()));
            }
            if (evaluationResult.getMissingRelationships().size() > 0 || !evaluationResult.getWrongRelationships().isEmpty()) {
                criteria.add(new CriterionDto("Relationships", null, false, "Missing Relationships: " + evaluationResult.getMissingRelationships().size() + "<br> Wrong Relationships: " + evaluationResult.getWrongRelationships().size()));
            }
            if (evaluationResult.getMissingAssociations().size() > 0 || !evaluationResult.getWrongAssociations().isEmpty()) {
                criteria.add(new CriterionDto("Associations", null, false, "Missing Associations: " + evaluationResult.getMissingAssociations().size() + "<br> Wrong Associations: " + evaluationResult.getWrongAssociations().size()));
            }
            if (evaluationResult.getMissingConstraints().size() > 0 || !evaluationResult.getWrongConstraints().isEmpty()) {
                criteria.add(new CriterionDto("Constraints", null, false, "Missing Constraints: " + evaluationResult.getMissingConstraints().size() + "<br>Wrong Constraints: " + evaluationResult.getWrongConstraints().size()));
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
                criteria.add(new CriterionDto("Classes", null, false, s));
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
                criteria.add(new CriterionDto("Attributes", null, false, s));
            }
            if (evaluationResult.getMissingAbstractClasses().size() > 0) {
                String s = "Missing Abstract Classes: ";
                for (UMLClass umlClass : evaluationResult.getMissingAbstractClasses()) {
                    s += umlClass.getName() + ", ";
                }


                criteria.add(new CriterionDto("Abstract Classes", null, false, s));
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
                criteria.add(new CriterionDto("Relationships", null, false, s));
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
                criteria.add(new CriterionDto("Associations", null, false, s));
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
                criteria.add(new CriterionDto("Constraints", null, false, s));
            }

            if (evaluationResult.getWrongMultiRelationships().size() > 0) {
                String s = "Wrong ternaer Relationship: ";
                for (UMLMultiRelationship multiRelationship : evaluationResult.getWrongMultiRelationships()) {
                    s += multiRelationship.getName() + ", ";
                }
                criteria.add(new CriterionDto("MultiRelationships", null, false, s));
            }


        }
        criteria.add(new CriterionDto("Image", null, true, generateImage(submission.submission().input())));


        return new GradingDto(task.getMaxPoints(), BigDecimal.valueOf(evaluationResult.getPoints()), "", criteria);


    }

    //NOT WORKING - NOT FINISHED
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


            points += compareClass(evaluationResult, umlResultSolution, umlResultSubmission, task);

            points += compareRelationship(evaluationResult, umlResultSolution, umlResultSubmission, task);

            points += compareAssociation(evaluationResult, umlResultSolution, umlResultSubmission, task);

            points += compareConstraints(evaluationResult, umlResultSolution, umlResultSubmission, task);

            points += compareMultiRelationships(evaluationResult, umlResultSolution, umlResultSubmission, task);

            points = points - evaluationResult.getMissingAssociations().size() - evaluationResult.getWrongClasses().size() - evaluationResult.getMissingConstraints().size() - evaluationResult.getWrongRelationships().size();
            if (points < 0) {
                points = 0;
            }
            if (points >= bestPoints) {
                bestPoints = points;
                currentBestResult = umlResultSolution;
                bestevaluationResult = evaluationResult;
            }

        }
        bestevaluationResult.setPoints((int) bestPoints);
        return bestevaluationResult;
    }

    private double compareMultiRelationships(EvaluationResult evaluationResult, UMLResult umlResultSolution, UMLResult umlResultSubmission, UmlTask task) {
        double points = 0;
        for (UMLMultiRelationship multiRelationship : umlResultSubmission.getMultiRelationships()) {
            boolean isCorrectMultiRelationship = false;
            for (UMLMultiRelationship multiRelationshipSolution : umlResultSolution.getMultiRelationships()) {
                if (multiRelationship.getName().equals(multiRelationshipSolution.getName())) {
                    String connectedNoteSolution = umlResultSolution.getNoteConnections().stream().filter(noteConnection -> noteConnection.getClassName().equals(multiRelationshipSolution.getName())).findFirst().get().getNoteName();
                    if (umlResultSubmission.getNoteConnections().stream().anyMatch(e -> e.getClassName().equals(multiRelationship.getName()) && e.getNoteName().equals(connectedNoteSolution))) {
                        isCorrectMultiRelationship = true;
                        points += task.getRelationshipPoints().doubleValue();
                        break;
                    }
                }

            }
            if (!isCorrectMultiRelationship) {
                evaluationResult.getWrongMultiRelationships().add(multiRelationship);
            }
        }
        LOG.info("Points multi: " + points);
        return points;
    }

    private double compareConstraints(EvaluationResult evaluationResult, UMLResult umlResultSolution, UMLResult umlResultSubmission, UmlTask task) {
        double points = 0;
        boolean isFlipped = false;
        for (UMLConstraints constraintSubmission : umlResultSubmission.getConstraints()) {
            boolean isCorrectConstraint = false;
            for (UMLConstraints constraintSolution : umlResultSolution.getConstraints()) {
                //same order
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
                            //second relation flipped
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
                    //dirst relation flipped/second relation in same order
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
                            //second relation flipped and first relation flipped
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
                //first and second relation flipped
                if(!isCorrectConstraint){
                    String r1c1 = constraintSubmission.getRel1C1();
                    String r1c2 = constraintSubmission.getRel1C2();
                    String r2c1 = constraintSubmission.getRel2C1();
                    String r2c2 = constraintSubmission.getRel2C2();

                    constraintSubmission.setRel1C1(r2c1);
                    constraintSubmission.setRel1C2(r2c2);
                    constraintSubmission.setRel2C1(r1c1);
                    constraintSubmission.setRel2C2(r1c2);
                    isFlipped = true;
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
                                //second relation flipped
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
                        //dirst relation flipped/second relation in same order
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
                                //second relation flipped and first relation flipped
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
                    constraintSolution.setRel1C1(r1c1);
                    constraintSolution.setRel1C2(r1c2);
                    constraintSolution.setRel2C1(r2c1);
                    constraintSolution.setRel2C2(r2c2);
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
        LOG.info("Points con: " + points);
        return points;
    }

    private double compareAssociation(EvaluationResult evaluationResult, UMLResult umlResultSolution, UMLResult umlResultSubmission, UmlTask task) {
        double points = 0;
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
        LOG.info("Points asso: " + points);

        return points;
    }

    private double compareRelationship(EvaluationResult evaluationResult, UMLResult umlResultSolution, UMLResult umlResultSubmission, UmlTask task) {
        double points = 0;
        for (UMLRelationship relationshipSubmission : umlResultSubmission.getRelationships()) {
            boolean isCorrectRelationship = false;
            for (UMLRelationship relationshipSolution : umlResultSolution.getRelationships()) {

                if (relationshipSolution.getEntities().stream().anyMatch(e -> e.getClassname().equals(relationshipSubmission.getEntities().getFirst().getClassname()))) {
                    if (relationshipSolution.getEntities().stream().anyMatch(e -> e.getClassname().equals(relationshipSubmission.getEntities().getLast().getClassname()))) {
                        //compare multiplicity of the two entities with matching name
                        if (relationshipSolution.getEntities().stream().filter(e -> e.getClassname().equals(relationshipSubmission.getEntities().getFirst().getClassname())).findFirst().get().getMultiplicity().equals(relationshipSubmission.getEntities().getFirst().getMultiplicity())) {
                            if (relationshipSolution.getEntities().stream().filter(e -> e.getClassname().equals(relationshipSubmission.getEntities().getLast().getClassname())).findFirst().get().getMultiplicity().equals(relationshipSubmission.getEntities().getLast().getMultiplicity())) {

                                if(relationshipSubmission.getEntities().getFirst().getClassname().equals(relationshipSubmission.getEntities().getLast().getClassname())){
                                    if(!relationshipSolution.getEntities().getFirst().getClassname().equals(relationshipSolution.getEntities().getLast().getClassname())){
                                        break;
                                    }
                                }

                                //opposite order
                                if(relationshipSolution.getEntities().getFirst().getClassname().equals(relationshipSubmission.getEntities().getLast().getClassname()))
                                {

                                    String reversedType = reverseType(relationshipSolution.getType());
                                    String reversedDirection = reverseType(relationshipSolution.getDirection());

                                    if (relationshipSubmission.getType().equals(reversedType)) {
                                        if(relationshipSubmission.getDirection().equals(reversedDirection)) {

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
                                }else //same order
                                {
                                    if (relationshipSubmission.getType().equals(relationshipSolution.getType())) {
                                        if (relationshipSubmission.getName().equals(relationshipSolution.getName())) {
                                            if(relationshipSubmission.getDirection().equals(relationshipSolution.getDirection())) {
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
        LOG.info("Points rel: " + points);

        return points;
    }


    public List<String> wrongIdentifiers(UMLResult umlResult, List<String> identifiers) {
        // Check classes
        List<String> wrongIdentifiers = new ArrayList<>();
        for (UMLClass umlClass : umlResult.getUmlClasses()) {
            if (!identifiers.contains(umlClass.getName())) {
                wrongIdentifiers.add(umlClass.getName());
            }
            for (UMLAttribute attribute : umlClass.getAttributes()) {
                if (!identifiers.contains(attribute.getName())) {
                    wrongIdentifiers.add(attribute.getName());
                }
            }
        }

        // Check relationships
        for (UMLRelationship relationship : umlResult.getRelationships()) {
            for (UMLRelationshipEntity entity : relationship.getEntities()) {
                if (!identifiers.contains(entity.getClassname())) {
                    wrongIdentifiers.add(entity.getClassname());
                }
            }
        }

        // Check associations
        for (UMLAssociation association : umlResult.getAssociations()) {
            if (!identifiers.contains(association.getAssoClass())) {
                wrongIdentifiers.add(association.getAssoClass());
            }
            if (!identifiers.contains(association.getClass1())) {
                wrongIdentifiers.add(association.getClass1());
            }
            if (!identifiers.contains(association.getClass2())) {
                wrongIdentifiers.add(association.getClass2());
            }
        }

        // Check constraints
        for (UMLConstraints constraint : umlResult.getConstraints()) {
            if (!identifiers.contains(constraint.getRel1C1())) {
                wrongIdentifiers.add(constraint.getRel1C1());
            }
            if (!identifiers.contains(constraint.getRel1C2())) {
                wrongIdentifiers.add(constraint.getRel1C2());
            }
            if (!identifiers.contains(constraint.getRel2C1())) {
                wrongIdentifiers.add(constraint.getRel2C1());
            }
            if (!identifiers.contains(constraint.getRel2C2())) {
                wrongIdentifiers.add(constraint.getRel2C2());
            }
        }

        // If all checks pass, all names are in the identifiers list
        return wrongIdentifiers;
    }


    public double compareClass(EvaluationResult evaluationResult, UMLResult umlResultSolution, UMLResult umlResultSubmission, UmlTask task) {
        //compare if all classes in submission are part of the Solution
        double points = 0;
        for (UMLClass umlClassSubmission : umlResultSubmission.getUmlClasses()) {
            boolean isClassCorrect = false;
            for (UMLClass umlClassSolution : umlResultSolution.getUmlClasses()) {
                if (umlClassSubmission.getName().equals(umlClassSolution.getName())) {
                    if (umlClassSubmission.isAbstract() == umlClassSolution.isAbstract()) {
                        if (umlClassSubmission.getParentClass() != null) {
                            if (umlClassSolution.getParentClass() != null) {
                                if (umlClassSubmission.getParentClass().getName().equals(umlClassSolution.getParentClass().getName())) {
//                                    if (umlClassSolution.getPoints() == 0) {
//                                        points += task.getClassPoints().doubleValue();
//                                    } else {
//                                        points += umlClassSolution.getPoints();
//                                    }
                                    //the class is correct for now, if there is an attribute without a points specification it can still negate the points
                                    isClassCorrect = true;
                                    for (UMLAttribute attributeSubmission : umlClassSubmission.getAttributes()) {
                                        boolean isAttributecorrect = false;
                                        for (UMLAttribute attributeSolution : umlClassSolution.getAttributes()) {
                                            if (attributeSubmission.getName().equals(attributeSolution.getName())) {
                                                if (attributeSubmission.getType().equals(attributeSolution.getType())) {
                                                    //if points are specified it will add the points
                                                    if (attributeSolution.getPoints() != 0) {
                                                        points += attributeSolution.getPoints();
                                                    }
                                                    isAttributecorrect = true;
                                                    break;
                                                }
                                            }
                                        }
                                        //wrong attribute
                                        if (!isAttributecorrect) {
                                            evaluationResult.getWrongAttributes().add(attributeSubmission);
                                            //always wrong class because an element not in the solution can not be specified as a points giving element
                                            isClassCorrect = false;
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
                                            //if there are no points specified for an attribute in the solution that it is not in the submission it negate the class
                                            if (attributeSolution.getPoints() == 0) {
                                                isClassCorrect = false;
                                            }
                                        }
                                    }
                                    if(isClassCorrect)
                                    {
                                        if (umlClassSolution.getPoints() == 0) {
                                            points += task.getClassPoints().doubleValue();
                                        } else {
                                            points += umlClassSolution.getPoints();
                                        }
                                    }
                                    break;
                                }
                            }
                            //submission is abstract solution is not
                            break;
                        } else if (umlClassSolution.getParentClass() == null) {
//                            if (umlClassSolution.getPoints() == 0) {
//                                points += task.getClassPoints().doubleValue();
//                            } else {
//                                points += umlClassSolution.getPoints();
//                            }
                            isClassCorrect = true;
                            for (UMLAttribute attributeSubmission : umlClassSubmission.getAttributes()) {
                                boolean isAttributecorrect = false;
                                for (UMLAttribute attributeSolution : umlClassSolution.getAttributes()) {
                                    if (attributeSubmission.getName().equals(attributeSolution.getName())) {
                                        if (attributeSubmission.getType().equals(attributeSolution.getType())) {
                                            if (attributeSolution.getPoints() != 0) {
                                                points += attributeSolution.getPoints();
                                            }
                                            isAttributecorrect = true;
                                            break;
                                        }
                                    }
                                }
                                if (!isAttributecorrect) {
                                    evaluationResult.getWrongAttributes().add(attributeSubmission);
                                    isClassCorrect = false;
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
                                    //if there are no points specified for an attribute in the solution that it is not in the submission it negate the class
                                    if (attributeSolution.getPoints() == 0) {
                                        isClassCorrect = false;
                                    }
                                }
                            }
                            if(isClassCorrect)
                            {
                                if (umlClassSolution.getPoints() == 0) {
                                    points += task.getClassPoints().doubleValue();
                                } else {
                                    points += umlClassSolution.getPoints();
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

        //filter for Elements in the Solution which are not present in the Submission
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

        LOG.info("Points Class: " + points);
        return points;
    }

    public static String reverseType(String input) {
        switch (input) {
            case "--*":
                return "*--";
            case "*--":
                return "--*";
            case "<--":
                return "-->";
            case "-->":
                return "<--";
            case "--|>":
                return "<|--";
            case "<|--":
                return "--|>";
            case "<":
                return ">";
            case ">":
                return "<";
            case "--":
                return "--";
            default:
                return input; // Return the input unchanged if it doesn't match any pattern
        }
    }



    }


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
import java.util.Optional;

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

    //method to run through all classes and add them together if the name is matching
    private void getUnionClassesWithSameName(List<UMLClass> classes) {
        for (int i = 0; i < classes.size(); i++) {
            UMLClass class1 = classes.get(i);
            for (int j = i + 1; j < classes.size(); j++) {
                UMLClass class2 = classes.get(j);
                if (class1.getName().equals(class2.getName())) {
                    //add attributes if not present
                    for (UMLAttribute attribute : class2.getAttributes()) {
                        if (!class1.getAttributes().contains(attribute)) {
                            class1.getAttributes().add(attribute);
                        }
                    }
                    if(class2.isAbstract())
                    {
                        class1.setAbstract(true);
                    }
                    if(class2.getParentClasses() != null && !class2.getParentClasses().isEmpty()){
                        for(UMLClass parentClass : class2.getParentClasses()){
                            if (class1.getParentClasses() == null){
                                class1.setParentClasses(new ArrayList<>());
                            }

                            if( !class1.getParentClasses().contains(parentClass)){
                                class1.getParentClasses().add(parentClass);
                            }
                        }
                    }
                    //add associations if not present
                    for (UMLAssociation association : class2.getAssociations()) {
                        if (!class1.getAssociations().contains(association)) {
                            class1.getAssociations().add(association);
                        }
                    }
                    //sum points
                    class1.setPoints(class1.getPoints() + class2.getPoints());
                    classes.remove(j);
                }
            }
        }
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
                criteria.add(new CriterionDto("Associations", null, false, "Missing Associations: " + (evaluationResult.getMissingRelationships().size() + evaluationResult.getWrongRelationships().size())));
            }
            if (evaluationResult.getMissingAssociations().size() > 0 || !evaluationResult.getWrongAssociations().isEmpty()) {
                criteria.add(new CriterionDto("Association Classes", null, false, "Missing Association Classes: " + (evaluationResult.getMissingAssociations().size() + evaluationResult.getWrongAssociations().size())));
            }
            if (evaluationResult.getMissingConstraints().size() > 0 || !evaluationResult.getWrongConstraints().isEmpty()) {
                criteria.add(new CriterionDto("Constraints", null, false, "Missing Constraints: " + (evaluationResult.getMissingConstraints().size() + evaluationResult.getWrongConstraints().size())));
            }
        }
        if (submission.feedbackLevel().equals(2)) {
            if (evaluationResult.getMissingClasses().size() > 0 || !evaluationResult.getWrongClasses().isEmpty()) {
                criteria.add(new CriterionDto("Classes", null, false, "Missing Classes: " + evaluationResult.getMissingClasses().size() + "<br>Wrong Classes: " + evaluationResult.getWrongClasses().size()));
            }
            if (evaluationResult.getMissingAttributes().size() > 0 || !evaluationResult.getWrongAttributes().isEmpty()) {
                criteria.add(new CriterionDto("Attributes", null, false, "Missing Attributes: " + evaluationResult.getMissingAttributes().size() + "<br> Wrong Attributes: " + evaluationResult.getWrongAttributes().size()));
            }
            if (evaluationResult.getMissingAbstractClasses().size() > 0) {
                criteria.add(new CriterionDto("Abstract Classes", null, false, "Missing Abstract Classes: " + evaluationResult.getMissingAbstractClasses().size()));
            }
            if (evaluationResult.getMissingRelationships().size() > 0 || !evaluationResult.getWrongRelationships().isEmpty()) {
                criteria.add(new CriterionDto("Associations", null, false, "Missing Associations: " + evaluationResult.getMissingRelationships().size() + "<br> Wrong Associations: " + evaluationResult.getWrongRelationships().size()));
            }
            if (evaluationResult.getMissingAssociations().size() > 0 || !evaluationResult.getWrongAssociations().isEmpty()) {
                criteria.add(new CriterionDto("Association Classes", null, false, "Missing Association Classes: " + evaluationResult.getMissingAssociations().size() + "<br> Wrong Association Classes: " + evaluationResult.getWrongAssociations().size()));
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
                String s = "Missing Associations: ";
                for (UMLRelationship umlRelationship : evaluationResult.getMissingRelationships()) {
                    s += umlRelationship.getEntity1().getClassname() + "--" + umlRelationship.getEntity2().getClassname() + ", ";
                }
                s += "<br> Wrong Associations: ";
                for (UMLRelationship umlRelationship : evaluationResult.getWrongRelationships()) {
                    s += umlRelationship.getEntity1().getClassname() + umlRelationship.getType() + umlRelationship.getEntity2().getClassname() + ", ";
                }
                criteria.add(new CriterionDto("Association", null, false, s));
            }
            if (evaluationResult.getMissingAssociations().size() > 0 || !evaluationResult.getWrongAssociations().isEmpty()) {
                String s = "Missing Association Classes: ";
                for (UMLAssociation umlAssociation : evaluationResult.getMissingAssociations()) {
                    s += umlAssociation.getAssoClass() + "--" + umlAssociation.getClass1() + "--" + umlAssociation.getClass2() + ", ";
                }
                s += "<br> Wrong Association Classes: ";
                for (UMLAssociation umlAssociation : evaluationResult.getWrongAssociations()) {
                    s += umlAssociation.getAssoClass() + "--" + umlAssociation.getClass1() + "--" + umlAssociation.getClass2() + ", ";
                }
                criteria.add(new CriterionDto("Association Classes", null, false, s));
            }
            if (evaluationResult.getMissingConstraints().size() > 0 || !evaluationResult.getWrongConstraints().isEmpty()) {
                String s = "Missing Constraints: ";
                for (UMLConstraints umlConstraints : evaluationResult.getMissingConstraints()) {
                    if (umlConstraints.getType().equals("class")) {
                        s += "Class: " + umlConstraints.getRel1C1() + " on Attribute: " + umlConstraints.getRel1C2() + ", ";
                    } else {
                        s += umlConstraints.getRel1C1() + "--" + umlConstraints.getRel1C2() + "--" + umlConstraints.getRel2C1() + "--" + umlConstraints.getRel2C2() + ", ";
                    }
                }

                    s += "<br> Wrong Constraints: ";
                for (UMLConstraints umlConstraints : evaluationResult.getWrongConstraints()) {
                    if (umlConstraints.getType().equals("class")) {
                        s += "Class: " + umlConstraints.getRel1C1() + " on Attribute: " + umlConstraints.getRel1C2() + ", ";
                    } else {
                        s += umlConstraints.getRel1C1() + "--" + umlConstraints.getRel1C2() + "--" + umlConstraints.getRel2C1() + "--" + umlConstraints.getRel2C2() + ", ";
                    }
                }
                criteria.add(new CriterionDto("Constraints", null, false, s));
            }

            if (evaluationResult.getWrongMultiRelationships().size() > 0) {
                String s = "Wrong ternaer Relationship: ";
                for (UMLMultiRelationship multiRelationship : evaluationResult.getWrongMultiRelationships()) {
                    s += multiRelationship.getName() + ", ";
                }
                criteria.add(new CriterionDto("Multi-Relationships", null, false, s));
            }


        }
        criteria.add(new CriterionDto("Image", null, true, generateImage(submission.submission().input())));


        return new GradingDto(task.getMaxPoints(), BigDecimal.valueOf(evaluationResult.getPoints()), "", criteria);


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

            getUnionClassesWithSameName(umlResultSolution.getUmlClasses());
            getUnionClassesWithSameName(umlResultSubmission.getUmlClasses());


            points += compareClass(evaluationResult, umlResultSolution, umlResultSubmission, task);

            points += compareRelationship(evaluationResult, umlResultSolution, umlResultSubmission, task);

            points += compareAssociation(evaluationResult, umlResultSolution, umlResultSubmission, task);

            points += compareConstraints(evaluationResult, umlResultSolution, umlResultSubmission, task);

            points += compareMultiRelationships(evaluationResult, umlResultSolution, umlResultSubmission, task);

            points = points - evaluationResult.getWrongAssociations().size() - evaluationResult.getWrongClasses().size() - evaluationResult.getWrongConstraints().size() - evaluationResult.getWrongRelationships().size();
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
                        if (umlResultSolution.getNotes().stream().anyMatch(e -> e.getNoteName().equals(connectedNoteSolution)) && umlResultSubmission.getNotes().stream().anyMatch(e -> e.getNoteName().equals(connectedNoteSolution))) {
                            //search in submission and solution after nodename to find note and compare
                            String noteSubmission = umlResultSubmission.getNotes().stream()
                                .filter(note -> note.getNoteName().equals(connectedNoteSolution))
                                .findFirst()
                                .get()
                                .getNote();
                            String noteSolution = umlResultSolution.getNotes().stream()
                                .filter(note -> note.getNoteName().equals(connectedNoteSolution))
                                .findFirst()
                                .get()
                                .getNote();
                            if (noteSolution.equals(noteSubmission)) {
                                isCorrectMultiRelationship = true;
                                points += task.getRelationshipPoints().doubleValue();
                                break;
                            }
                        }
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

        for (UMLConstraints constraintSubmission : umlResultSubmission.getConstraints()) {

            boolean isCorrectConstraint = false;
            for (UMLConstraints constraintSolution : umlResultSolution.getConstraints()) {
                boolean isFlipped = false;
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

                //first relation flipped/second relation in same order
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
                if (!isCorrectConstraint) {
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
                        //first relation flipped/second relation in same order
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
                    constraintSubmission.setRel1C1(r1c1);
                    constraintSubmission.setRel1C2(r1c2);
                    constraintSubmission.setRel2C1(r2c1);
                    constraintSubmission.setRel2C2(r2c2);
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



        // Check class constraints from submission if present in solution
        for (UMLClass umlClass : umlResultSubmission.getUmlClasses()) {
            for (UMLAttribute umlAttribute : umlClass.getAttributes()) {
                if (umlAttribute.getType().equals("special")) {
                    Optional<UMLClass> optionalUmlClass = umlResultSolution.getUmlClasses().stream()
                        .filter(e -> e.getName().equals(umlClass.getName()))
                        .findFirst();

                    if (optionalUmlClass.isPresent()) {
                        UMLClass umlClassFound = optionalUmlClass.get();

                        Optional<UMLAttribute> optionalUmlAttribute = umlClassFound.getAttributes().stream()
                            .filter(e -> e.getName().equals(umlAttribute.getName()))
                            .findFirst();

                        if (optionalUmlAttribute.isPresent()) {
                            UMLAttribute umlAttributeFound = optionalUmlAttribute.get();
                            if (umlAttributeFound.getName().equals(umlAttribute.getName())) {
                                if(umlAttributeFound.getPoints()!=0)
                                {
                                    points += umlAttributeFound.getPoints();
                                }
                                else
                                {
                                    points += task.getConstraintPoints().doubleValue();
                                }
                            }
                        } else {
                            // If attribute is not found, add wrong constraints
                            evaluationResult.getWrongConstraints().add(new UMLConstraints(
                                umlClass.getName(),
                                umlAttribute.getName(),
                                null,
                                null,
                                "class"
                            ));
                        }
                    } else {
                        // If class is not found, handle the same way as when the attribute is not found
                        evaluationResult.getWrongConstraints().add(new UMLConstraints(
                            umlClass.getName(),
                            umlAttribute.getName(),
                            null,
                            null,
                            "class"
                        ));
                    }
                }

                if (!umlAttribute.getType().equals("normal") && !umlAttribute.getType().equals("special")) {
                    Optional<UMLClass> optionalUmlClassForTypeCheck = umlResultSolution.getUmlClasses().stream()
                        .filter(e -> e.getName().equals(umlClass.getName()))
                        .findFirst();

                    if (optionalUmlClassForTypeCheck.isPresent()) {
                        UMLClass umlClassFoundForTypeCheck = optionalUmlClassForTypeCheck.get();

                        Optional<UMLAttribute> optionalUmlAttributeForTypeCheck = umlClassFoundForTypeCheck.getAttributes().stream()
                            .filter(e -> e.getName().equals(umlAttribute.getName()))
                            .findFirst();

                        if (optionalUmlAttributeForTypeCheck.isPresent()) {
                            UMLAttribute umlAttributeFoundForTypeCheck = optionalUmlAttributeForTypeCheck.get();
                            if (umlAttributeFoundForTypeCheck.getType().equals(umlAttribute.getType())) {
                                points += task.getConstraintPoints().doubleValue();
                            } else {
                                evaluationResult.getWrongConstraints().add(new UMLConstraints(
                                    umlClass.getName(),
                                    umlAttribute.getName(),
                                    null,
                                    null,
                                    "class"
                                ));
                            }
                        } else {
                            evaluationResult.getWrongConstraints().add(new UMLConstraints(
                                umlClass.getName(),
                                umlAttribute.getName(),
                                null,
                                null,
                                "class"
                            ));
                        }
                    } else {
                        evaluationResult.getWrongConstraints().add(new UMLConstraints(
                            umlClass.getName(),
                            umlAttribute.getName(),
                            null,
                            null,
                            "class"
                        ));
                    }
                }
            }
        }

// Check class constraints from solution if present in submission
        for (UMLClass umlClass : umlResultSolution.getUmlClasses()) {
            for (UMLAttribute umlAttribute : umlClass.getAttributes()) {
                if (umlAttribute.getType().equals("special")) {
                    Optional<UMLClass> optionalUmlClassSubmission = umlResultSubmission.getUmlClasses().stream()
                        .filter(e -> e.getName().equals(umlClass.getName()))
                        .findFirst();

                    if (optionalUmlClassSubmission.isPresent()) {
                        UMLClass umlClassFoundInSubmission = optionalUmlClassSubmission.get();

                        Optional<UMLAttribute> optionalUmlAttributeSubmission = umlClassFoundInSubmission.getAttributes().stream()
                            .filter(e -> e.getName().equals(umlAttribute.getName()))
                            .findFirst();

                        if (optionalUmlAttributeSubmission.isPresent()) {
                            UMLAttribute umlAttributeFoundInSubmission = optionalUmlAttributeSubmission.get();
                            if (umlAttributeFoundInSubmission.getName().equals(umlAttribute.getName())) {

                            } else {
                                evaluationResult.getMissingConstraints().add(new UMLConstraints(
                                    umlClass.getName(),
                                    umlAttribute.getName(),
                                    null,
                                    null,
                                    "class"
                                ));
                            }
                        } else {
                            evaluationResult.getMissingConstraints().add(new UMLConstraints(
                                umlClass.getName(),
                                umlAttribute.getName(),
                                null,
                                null,
                                "class"
                            ));
                        }
                    } else {
                        evaluationResult.getMissingConstraints().add(new UMLConstraints(
                            umlClass.getName(),
                            umlAttribute.getName(),
                            null,
                            null,
                            "class"
                        ));
                    }
                }

                if (!umlAttribute.getType().equals("normal") && !umlAttribute.getType().equals("special")) {
                    Optional<UMLClass> optionalUmlClassForTypeCheckSubmission = umlResultSubmission.getUmlClasses().stream()
                        .filter(e -> e.getName().equals(umlClass.getName()))
                        .findFirst();

                    if (optionalUmlClassForTypeCheckSubmission.isPresent()) {
                        UMLClass umlClassFoundForTypeCheckSubmission = optionalUmlClassForTypeCheckSubmission.get();

                        Optional<UMLAttribute> optionalUmlAttributeForTypeCheckSubmission = umlClassFoundForTypeCheckSubmission.getAttributes().stream()
                            .filter(e -> e.getName().equals(umlAttribute.getName()))
                            .findFirst();

                        if (optionalUmlAttributeForTypeCheckSubmission.isPresent()) {
                            UMLAttribute umlAttributeFoundForTypeCheckSubmission = optionalUmlAttributeForTypeCheckSubmission.get();
                            if (umlAttributeFoundForTypeCheckSubmission.getType().equals(umlAttribute.getType())) {
                            } else {
                                evaluationResult.getMissingConstraints().add(new UMLConstraints(
                                    umlClass.getName(),
                                    umlAttribute.getName(),
                                    null,
                                    null,
                                    "class"
                                ));
                            }
                        } else {
                            evaluationResult.getMissingConstraints().add(new UMLConstraints(
                                umlClass.getName(),
                                umlAttribute.getName(),
                                null,
                                null,
                                "class"
                            ));
                        }
                    } else {
                        evaluationResult.getMissingConstraints().add(new UMLConstraints(
                            umlClass.getName(),
                            umlAttribute.getName(),
                            null,
                            null,
                            "class"
                        ));
                    }
                }
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

            //search for the same relationship in the solution in same order
            for (UMLRelationship relationshipSolution : umlResultSolution.getRelationships()) {

                    if(relationshipSubmission.getEntity1().getClassname().equals(relationshipSolution.getEntity1().getClassname()))
                    {
                        if(relationshipSubmission.getEntity2().getClassname().equals(relationshipSolution.getEntity2().getClassname()))
                        {
                            if(relationshipSubmission.getEntity1().getMultiplicity().equals(relationshipSolution.getEntity1().getMultiplicity()))
                            {
                                if(relationshipSubmission.getEntity2().getMultiplicity().equals(relationshipSolution.getEntity2().getMultiplicity()))
                                {

                                    if(relationshipSubmission.getType().equals(relationshipSolution.getType()))
                                    {
                                        if(relationshipSubmission.getName().equals(relationshipSolution.getName()))
                                        {
                                            if(relationshipSubmission.getDirection().equals(relationshipSolution.getDirection()))
                                            {
                                                if(relationshipSolution.getPoints() == 0)
                                                {
                                                    points += task.getRelationshipPoints().doubleValue();
                                                }
                                                else
                                                {
                                                    points += relationshipSolution.getPoints();
                                                }
                                                isCorrectRelationship = true;
                                                break;
                                            }
                                            else
                                            {
                                                if(relationshipSolution.getPoints() == 0)
                                                {
                                                    points += task.getRelationshipPoints().doubleValue();
                                                }
                                                else
                                                {
                                                    points += relationshipSolution.getPoints();
                                                }
                                                isCorrectRelationship = false;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }



            }
            //search for the same relationship in the solution in flipped order
            if(!isCorrectRelationship)
            {
                for (UMLRelationship relationshipSolution : umlResultSolution.getRelationships()) {

                    if(relationshipSubmission.getEntity1().getClassname().equals(relationshipSolution.getEntity2().getClassname()))
                    {
                        if(relationshipSubmission.getEntity2().getClassname().equals(relationshipSolution.getEntity1().getClassname()))
                        {
                            if(relationshipSubmission.getEntity1().getMultiplicity().equals(relationshipSolution.getEntity2().getMultiplicity()))
                            {
                                if(relationshipSubmission.getEntity2().getMultiplicity().equals(relationshipSolution.getEntity1().getMultiplicity()))
                                {

                                    if(relationshipSubmission.getType().equals(reverseType(relationshipSolution.getType())))
                                    {
                                        if(relationshipSubmission.getName().equals(relationshipSolution.getName()))
                                        {
                                            if(relationshipSubmission.getDirection().equals(reverseType(relationshipSolution.getDirection())))
                                            {
                                                if(relationshipSolution.getPoints() == 0)
                                                {
                                                    points += task.getRelationshipPoints().doubleValue();
                                                }
                                                else
                                                {
                                                    points += relationshipSolution.getPoints();
                                                }
                                                isCorrectRelationship = true;
                                                break;
                                            }
                                            else {
                                                if(relationshipSolution.getPoints() == 0)
                                                {
                                                    points += task.getRelationshipPoints().doubleValue();
                                                }
                                                else
                                                {
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



            if(!isCorrectRelationship)
            {
                evaluationResult.getWrongRelationships().add(relationshipSubmission);
            }
        }
        //find missing relationships
        for (UMLRelationship relationshipSolution : umlResultSolution.getRelationships()) {
            boolean isCorrectRelationship = false;
            for (UMLRelationship relationshipSubmission : umlResultSubmission.getRelationships()) {
                if(relationshipSubmission.getEntity1().getClassname().equals(relationshipSolution.getEntity1().getClassname()))
                {
                    if(relationshipSubmission.getEntity2().getClassname().equals(relationshipSolution.getEntity2().getClassname()))
                    {
                        if(relationshipSubmission.getEntity1().getMultiplicity().equals(relationshipSolution.getEntity1().getMultiplicity()))
                        {
                            if(relationshipSubmission.getEntity2().getMultiplicity().equals(relationshipSolution.getEntity2().getMultiplicity()))
                            {

                                if(relationshipSubmission.getType().equals(relationshipSolution.getType()))
                                {
                                    if(relationshipSubmission.getName().equals(relationshipSolution.getName()))
                                    {
                                            isCorrectRelationship = true;
                                            break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            //search for the same relationship in the solution in flipped order
            if(!isCorrectRelationship)
            {
                for (UMLRelationship relationshipSubmission : umlResultSubmission.getRelationships()) {

                    if(relationshipSubmission.getEntity1().getClassname().equals(relationshipSolution.getEntity2().getClassname()))
                    {
                        if(relationshipSubmission.getEntity2().getClassname().equals(relationshipSolution.getEntity1().getClassname()))
                        {
                            if(relationshipSubmission.getEntity1().getMultiplicity().equals(relationshipSolution.getEntity2().getMultiplicity()))
                            {
                                if(relationshipSubmission.getEntity2().getMultiplicity().equals(relationshipSolution.getEntity1().getMultiplicity()))
                                {

                                    if(relationshipSubmission.getType().equals(reverseType(relationshipSolution.getType())))
                                    {
                                        if(relationshipSubmission.getName().equals(relationshipSolution.getName()))
                                        {
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
            if(!isCorrectRelationship)
            {
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

                if (!identifiers.contains(relationship.getEntity1().getClassname())) {
                    wrongIdentifiers.add(relationship.getEntity1().getClassname());
                }
                if (!identifiers.contains(relationship.getEntity2().getClassname())) {
                    wrongIdentifiers.add(relationship.getEntity2().getClassname());
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
                        if (umlClassSubmission.getParentClasses() != null) {
                            if (umlClassSolution.getParentClasses() != null) {
                                if (compareUmlClassNames(umlClassSolution.getParentClasses(), umlClassSubmission.getParentClasses())) {
//                                    if (umlClassSolution.getPoints() == 0) {
//                                        points += task.getClassPoints().doubleValue();
//                                    } else {
//                                        points += umlClassSolution.getPoints();
//                                    }
                                    //the class is correct for now, if there is an attribute without a points specification it can still negate the points
                                    isClassCorrect = true;
                                    for (UMLAttribute attributeSubmission : umlClassSubmission.getAttributes()) {
                                        if (attributeSubmission.getType().equals("special")) {
                                            //special attribute not checked here
                                            continue;
                                        }
                                        boolean isAttributecorrect = false;
                                        for (UMLAttribute attributeSolution : umlClassSolution.getAttributes()) {
                                            if (attributeSubmission.getName().equals(attributeSolution.getName())) {
                                                if (!attributeSubmission.getType().equals("special")) {
                                                    //if points are specified it will add the points
                                                    if (attributeSolution.getPoints() != 0) {
                                                        points += attributeSolution.getPoints();
                                                    }
                                                    isAttributecorrect = true;
                                                    break;
                                                } else {
                                                    //special attribute not checked here
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
                                        if (attributeSolution.getType().equals("special")) {
                                            //special attribute not checked here
                                            continue;
                                        }
                                        boolean isAttributecorrect = false;
                                        for (UMLAttribute attributeSubmission : umlClassSubmission.getAttributes()) {
                                            if (attributeSubmission.getName().equals(attributeSolution.getName())) {

                                                isAttributecorrect = true;
                                                break;
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
                                    if (isClassCorrect) {
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
                        } else if (umlClassSolution.getParentClasses() == null) {
//                            if (umlClassSolution.getPoints() == 0) {
//                                points += task.getClassPoints().doubleValue();
//                            } else {
//                                points += umlClassSolution.getPoints();
//                            }
                            isClassCorrect = true;
                            for (UMLAttribute attributeSubmission : umlClassSubmission.getAttributes()) {
                                if (attributeSubmission.getType().equals("special")) {
                                    //special attribute not checked here
                                    continue;
                                }
                                boolean isAttributecorrect = false;
                                for (UMLAttribute attributeSolution : umlClassSolution.getAttributes()) {
                                    if (attributeSubmission.getName().equals(attributeSolution.getName())) {
                                        if (!attributeSubmission.getType().equals("special")) {
                                            //if points are specified it will add the points
                                            if (attributeSolution.getPoints() != 0) {
                                                points += attributeSolution.getPoints();
                                            }
                                            isAttributecorrect = true;
                                            break;
                                        } else {
                                            //special attribute not checked here
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
                                if (attributeSolution.getType().equals("special")) {
                                    //special attribute not checked here
                                    continue;
                                }
                                boolean isAttributecorrect = false;
                                for (UMLAttribute attributeSubmission : umlClassSubmission.getAttributes()) {
                                    if (attributeSubmission.getName().equals(attributeSolution.getName())) {

                                        isAttributecorrect = true;
                                        break;

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
                            if (isClassCorrect) {
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

    private boolean compareUmlClassNames(List<UMLClass> parentClasses1, List<UMLClass> parentClasses2) {
        if (parentClasses1.size() != parentClasses2.size()) {
            return false;
        }
        for (int i = 0; i < parentClasses1.size(); i++) {
            if (!parentClasses1.get(i).getName().equals(parentClasses2.get(i).getName())) {
                return false;
            }
        }
        return true;
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


package at.jku.dke.task_app.uml.evaluation.atg;

import at.jku.dke.task_app.uml.evaluation.atg.gen.PlantUML_ATGBaseListener;
import at.jku.dke.task_app.uml.evaluation.atg.gen.PlantUML_ATGParser;
import at.jku.dke.task_app.uml.evaluation.atg.objects.*;

import java.util.ArrayList;
import java.util.List;

public class MyPlantUML_ATGListener extends PlantUML_ATGBaseListener {
    private List<UMLClass> umlClasses = new ArrayList<>();
    private UMLClass currentClass;
    private List<UMLAttribute> currentAttributes;
    private List<UMLAssociation> currentAssociations;
    private List<UMLRelationship> relationships = new ArrayList<>();
    private List<UMLAssociation> associations = new ArrayList<>();
    private List<UMLConstraints> constraints = new ArrayList<>();
    private List<UMLMultiRelationship> multiRelationships = new ArrayList<>();
    private List<UMLNote> notes = new ArrayList<>();
    private List<UMLNoteConnection> noteConnections = new ArrayList<>();

    @Override
    public void enterClassDefinition(PlantUML_ATGParser.ClassDefinitionContext ctx) {
        currentClass = new UMLClass();
        if (umlClasses.stream().anyMatch(c -> c.getName().equals(ctx.className.getText()))) {

            currentClass = umlClasses.stream().filter(c -> c.getName().equals(ctx.className.getText())).findFirst().get();
        }
        currentClass.setName(ctx.className().getText());
        if(ctx.score()!=null) {
            currentClass.setPoints(Integer.parseInt(ctx.score().points().getText()));
        }
        if(ctx.parentClassName()!=null) {
            currentClass.setParentClass(new UMLClass(ctx.parentClassName().getText()));
        }
        currentAttributes = new ArrayList<>();
        currentAssociations = new ArrayList<>();
        currentClass.setAbstract(ctx.abstractModifier() != null);
    }


    @Override
    public void enterAttribute(PlantUML_ATGParser.AttributeContext ctx) {
        UMLAttribute attribute = new UMLAttribute();
        attribute.setName(ctx.attributeName().getText());
        if(ctx.attributeModifier()!=null) {
            attribute.setType(ctx.attributeModifier.getText().toLowerCase());
        }
        else{
            attribute.setType("normal");
        }
        if (ctx.score() != null) {
            attribute.setPoints(Integer.parseInt(ctx.score().points().getText()));
        }
        currentAttributes.add(attribute);
    }

    @Override
    public void enterSpecialAttribute(PlantUML_ATGParser.SpecialAttributeContext ctx) {
        UMLAttribute attribute = new UMLAttribute();
        attribute.setName(ctx.speciallabel().getText().toLowerCase());

        if (ctx.score() != null) {
            attribute.setPoints(Integer.parseInt(ctx.score().points().getText()));
        }
        attribute.setType("special");
        currentAttributes.add(attribute);
    }



    @Override
    public void exitClassDefinition(PlantUML_ATGParser.ClassDefinitionContext ctx) {
        currentClass.setAttributes(currentAttributes);
        currentClass.setAssociations(currentAssociations);
        if(ctx.score()!=null) {
            currentClass.setPoints(Integer.parseInt(ctx.score().points().getText()));
        }
        umlClasses.add(currentClass);
        currentClass = null;

    }

    public List<UMLClass> getUmlClasses() {
        return umlClasses;
    }

    @Override
    public void enterRelationship(PlantUML_ATGParser.RelationshipContext ctx) {
        UMLRelationship relationship = new UMLRelationship();
        UMLRelationshipEntity entity1 = new UMLRelationshipEntity();
        UMLRelationshipEntity entity2 = new UMLRelationshipEntity();
        entity1.setClassname(ctx.participant1.className.getText());
        entity2.setClassname(ctx.participant2.className.getText());
        if(ctx.participant1.multiplicity!=null) {
            entity1.setMultiplicity(ctx.participant1.multiplicity);
        }
        if(ctx.participant2.multiplicity!=null) {
            entity2.setMultiplicity(ctx.participant2.multiplicity);
        }
        relationship.addEntity(entity1);
        relationship.addEntity(entity2);
        relationship.setType(ctx.relationTyp.getText());
        if(ctx.labelMultiplicity()!=null) {
            relationship.setDirection(ctx.labelMultiplicity().multiplicity);
        }
        if(ctx.score()!=null) {
            relationship.setPoints(Integer.parseInt(ctx.score().points().getText()));
        }
        if(ctx.label()!=null) {
            relationship.setName(ctx.label().getText());
        }
        else{
            relationship.setName("");
        }
        relationships.add(relationship);


        String className = ctx.participant1.className.getText();
        String className2 = ctx.participant2.className.getText();
        System.out.println(className + " " + className2);
        if (className!=null&&!umlClasses.stream().anyMatch(c -> c.getName().equals(className))&&multiRelationships.stream().noneMatch(c -> c.getName().equals(className))) {
            UMLClass umlClass = new UMLClass(className);
            umlClass.setAttributes(new ArrayList<>());
            umlClass.setAssociations(new ArrayList<>());
            umlClasses.add(umlClass);

        }
        if (className2!=null&&!umlClasses.stream().anyMatch(c -> c.getName().equals(className2))&&multiRelationships.stream().noneMatch(c -> c.getName().equals(className2))) {
            UMLClass umlClass = new UMLClass(className2);
            umlClass.setAttributes(new ArrayList<>());
            umlClass.setAssociations(new ArrayList<>());
            umlClasses.add(umlClass);
        }
    }

    @Override
    public void exitRelationship(PlantUML_ATGParser.RelationshipContext ctx) {
        if(currentClass!=null) {
            umlClasses.add(currentClass);
        }
        currentClass = null;
    }

    public List<UMLRelationship> getRelationships() {
        return relationships;
    }

    @Override
    public void enterAssociation(PlantUML_ATGParser.AssociationContext ctx) {
        UMLAssociation association = new UMLAssociation();
        association.setClass1(ctx.className1.getText());
        association.setClass2(ctx.className2.getText());
        association.setAssoClass(ctx.className3.getText());
        if(ctx.score()!=null) {
            association.setPoints(Integer.parseInt(ctx.score().points().getText()));
        }
        currentAssociations.add(association);
        associations.add(association);
    }

    public List<UMLAssociation> getAssociations() {
        return associations;
    }

    @Override
    public void enterConstraints(PlantUML_ATGParser.ConstraintsContext ctx) {
        UMLConstraints constraint = new UMLConstraints();

        constraint.setRel1C1(ctx.constraintmember(0).className1 != null ? ctx.constraintmember(0).className1.getText() : null);
        constraint.setRel1C2(ctx.constraintmember(0).className2 != null ? ctx.constraintmember(0).className2.getText() : null);
        constraint.setRel2C1(ctx.constraintmember(1).className1 != null ? ctx.constraintmember(1).className1.getText() : null);
        constraint.setRel2C2(ctx.constraintmember(1).className2 != null ? ctx.constraintmember(1).className2.getText() : null);
        constraint.setType(ctx.constrainttype().getText());
        if(ctx.score()!=null) {
            constraint.setPoints(Integer.parseInt(ctx.score().points().getText()));
        }
        constraints.add(constraint);
    }

    public List<UMLConstraints> getConstraints() {
        return constraints;
    }

    @Override
    public void enterMultiRelationship(PlantUML_ATGParser.MultiRelationshipContext ctx) {
        UMLMultiRelationship multiRelationship = new UMLMultiRelationship();
        multiRelationship.setName(ctx.multiRelationshipName().Identifier().getText());
        multiRelationships.add(multiRelationship);

    }

    public List<UMLMultiRelationship> getMultiRelationships() {
        return multiRelationships;
    }

    @Override
    public void enterNoteConnection(PlantUML_ATGParser.NoteConnectionContext ctx) {
        UMLNoteConnection noteConnection = new UMLNoteConnection();
        noteConnection.setNoteName(ctx.noteName().getText());
        noteConnection.setClassName(ctx.multiRelationshipName().Identifier().getText());
        noteConnections.add(noteConnection);
    }

    public List<UMLNoteConnection> getNoteConnections() {
        return noteConnections;
    }

    @Override
    public void enterNote(PlantUML_ATGParser.NoteContext ctx) {
        UMLNote note = new UMLNote();
        note.setNote(ctx.noteText().getText());
        note.setNoteName(ctx.noteName().Identifier().getText());
        notes.add(note);
    }

    public List<UMLNote> getNotes() {
        return notes;
    }
}

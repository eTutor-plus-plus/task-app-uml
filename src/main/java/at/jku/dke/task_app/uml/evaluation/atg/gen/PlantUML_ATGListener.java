// Generated from PlantUML_ATG.g4 by ANTLR 4.13.1
package at.jku.dke.task_app.uml.evaluation.atg.gen;
import at.jku.dke.task_app.uml.evaluation.atg.objects.UMLAssociation;
import at.jku.dke.task_app.uml.evaluation.atg.objects.UMLClass;
import at.jku.dke.task_app.uml.evaluation.atg.objects.UMLAttribute;
import at.jku.dke.task_app.uml.evaluation.atg.objects.UMLRelationship;
import at.jku.dke.task_app.uml.evaluation.atg.objects.UMLRelationshipEntity;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PlantUML_ATGParser}.
 */
public interface PlantUML_ATGListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link PlantUML_ATGParser#classDiagram}.
	 * @param ctx the parse tree
	 */
	void enterClassDiagram(PlantUML_ATGParser.ClassDiagramContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUML_ATGParser#classDiagram}.
	 * @param ctx the parse tree
	 */
	void exitClassDiagram(PlantUML_ATGParser.ClassDiagramContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUML_ATGParser#association}.
	 * @param ctx the parse tree
	 */
	void enterAssociation(PlantUML_ATGParser.AssociationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUML_ATGParser#association}.
	 * @param ctx the parse tree
	 */
	void exitAssociation(PlantUML_ATGParser.AssociationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUML_ATGParser#noteConnection}.
	 * @param ctx the parse tree
	 */
	void enterNoteConnection(PlantUML_ATGParser.NoteConnectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUML_ATGParser#noteConnection}.
	 * @param ctx the parse tree
	 */
	void exitNoteConnection(PlantUML_ATGParser.NoteConnectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUML_ATGParser#constraints}.
	 * @param ctx the parse tree
	 */
	void enterConstraints(PlantUML_ATGParser.ConstraintsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUML_ATGParser#constraints}.
	 * @param ctx the parse tree
	 */
	void exitConstraints(PlantUML_ATGParser.ConstraintsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUML_ATGParser#constraintmember}.
	 * @param ctx the parse tree
	 */
	void enterConstraintmember(PlantUML_ATGParser.ConstraintmemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUML_ATGParser#constraintmember}.
	 * @param ctx the parse tree
	 */
	void exitConstraintmember(PlantUML_ATGParser.ConstraintmemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUML_ATGParser#constrainttype}.
	 * @param ctx the parse tree
	 */
	void enterConstrainttype(PlantUML_ATGParser.ConstrainttypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUML_ATGParser#constrainttype}.
	 * @param ctx the parse tree
	 */
	void exitConstrainttype(PlantUML_ATGParser.ConstrainttypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUML_ATGParser#note}.
	 * @param ctx the parse tree
	 */
	void enterNote(PlantUML_ATGParser.NoteContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUML_ATGParser#note}.
	 * @param ctx the parse tree
	 */
	void exitNote(PlantUML_ATGParser.NoteContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUML_ATGParser#noteName}.
	 * @param ctx the parse tree
	 */
	void enterNoteName(PlantUML_ATGParser.NoteNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUML_ATGParser#noteName}.
	 * @param ctx the parse tree
	 */
	void exitNoteName(PlantUML_ATGParser.NoteNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUML_ATGParser#noteText}.
	 * @param ctx the parse tree
	 */
	void enterNoteText(PlantUML_ATGParser.NoteTextContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUML_ATGParser#noteText}.
	 * @param ctx the parse tree
	 */
	void exitNoteText(PlantUML_ATGParser.NoteTextContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUML_ATGParser#classDefinition}.
	 * @param ctx the parse tree
	 */
	void enterClassDefinition(PlantUML_ATGParser.ClassDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUML_ATGParser#classDefinition}.
	 * @param ctx the parse tree
	 */
	void exitClassDefinition(PlantUML_ATGParser.ClassDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUML_ATGParser#multiRelationship}.
	 * @param ctx the parse tree
	 */
	void enterMultiRelationship(PlantUML_ATGParser.MultiRelationshipContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUML_ATGParser#multiRelationship}.
	 * @param ctx the parse tree
	 */
	void exitMultiRelationship(PlantUML_ATGParser.MultiRelationshipContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUML_ATGParser#specialAttribute}.
	 * @param ctx the parse tree
	 */
	void enterSpecialAttribute(PlantUML_ATGParser.SpecialAttributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUML_ATGParser#specialAttribute}.
	 * @param ctx the parse tree
	 */
	void exitSpecialAttribute(PlantUML_ATGParser.SpecialAttributeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUML_ATGParser#attribute}.
	 * @param ctx the parse tree
	 */
	void enterAttribute(PlantUML_ATGParser.AttributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUML_ATGParser#attribute}.
	 * @param ctx the parse tree
	 */
	void exitAttribute(PlantUML_ATGParser.AttributeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUML_ATGParser#attributeModifier}.
	 * @param ctx the parse tree
	 */
	void enterAttributeModifier(PlantUML_ATGParser.AttributeModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUML_ATGParser#attributeModifier}.
	 * @param ctx the parse tree
	 */
	void exitAttributeModifier(PlantUML_ATGParser.AttributeModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUML_ATGParser#score}.
	 * @param ctx the parse tree
	 */
	void enterScore(PlantUML_ATGParser.ScoreContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUML_ATGParser#score}.
	 * @param ctx the parse tree
	 */
	void exitScore(PlantUML_ATGParser.ScoreContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUML_ATGParser#relationship}.
	 * @param ctx the parse tree
	 */
	void enterRelationship(PlantUML_ATGParser.RelationshipContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUML_ATGParser#relationship}.
	 * @param ctx the parse tree
	 */
	void exitRelationship(PlantUML_ATGParser.RelationshipContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUML_ATGParser#participant}.
	 * @param ctx the parse tree
	 */
	void enterParticipant(PlantUML_ATGParser.ParticipantContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUML_ATGParser#participant}.
	 * @param ctx the parse tree
	 */
	void exitParticipant(PlantUML_ATGParser.ParticipantContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUML_ATGParser#participantMultiplicity}.
	 * @param ctx the parse tree
	 */
	void enterParticipantMultiplicity(PlantUML_ATGParser.ParticipantMultiplicityContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUML_ATGParser#participantMultiplicity}.
	 * @param ctx the parse tree
	 */
	void exitParticipantMultiplicity(PlantUML_ATGParser.ParticipantMultiplicityContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUML_ATGParser#visibility}.
	 * @param ctx the parse tree
	 */
	void enterVisibility(PlantUML_ATGParser.VisibilityContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUML_ATGParser#visibility}.
	 * @param ctx the parse tree
	 */
	void exitVisibility(PlantUML_ATGParser.VisibilityContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUML_ATGParser#abstractModifier}.
	 * @param ctx the parse tree
	 */
	void enterAbstractModifier(PlantUML_ATGParser.AbstractModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUML_ATGParser#abstractModifier}.
	 * @param ctx the parse tree
	 */
	void exitAbstractModifier(PlantUML_ATGParser.AbstractModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUML_ATGParser#points}.
	 * @param ctx the parse tree
	 */
	void enterPoints(PlantUML_ATGParser.PointsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUML_ATGParser#points}.
	 * @param ctx the parse tree
	 */
	void exitPoints(PlantUML_ATGParser.PointsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUML_ATGParser#className}.
	 * @param ctx the parse tree
	 */
	void enterClassName(PlantUML_ATGParser.ClassNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUML_ATGParser#className}.
	 * @param ctx the parse tree
	 */
	void exitClassName(PlantUML_ATGParser.ClassNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUML_ATGParser#parentClassName}.
	 * @param ctx the parse tree
	 */
	void enterParentClassName(PlantUML_ATGParser.ParentClassNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUML_ATGParser#parentClassName}.
	 * @param ctx the parse tree
	 */
	void exitParentClassName(PlantUML_ATGParser.ParentClassNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUML_ATGParser#speciallabel}.
	 * @param ctx the parse tree
	 */
	void enterSpeciallabel(PlantUML_ATGParser.SpeciallabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUML_ATGParser#speciallabel}.
	 * @param ctx the parse tree
	 */
	void exitSpeciallabel(PlantUML_ATGParser.SpeciallabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUML_ATGParser#attributeName}.
	 * @param ctx the parse tree
	 */
	void enterAttributeName(PlantUML_ATGParser.AttributeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUML_ATGParser#attributeName}.
	 * @param ctx the parse tree
	 */
	void exitAttributeName(PlantUML_ATGParser.AttributeNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUML_ATGParser#label}.
	 * @param ctx the parse tree
	 */
	void enterLabel(PlantUML_ATGParser.LabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUML_ATGParser#label}.
	 * @param ctx the parse tree
	 */
	void exitLabel(PlantUML_ATGParser.LabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUML_ATGParser#multiRelationshipName}.
	 * @param ctx the parse tree
	 */
	void enterMultiRelationshipName(PlantUML_ATGParser.MultiRelationshipNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUML_ATGParser#multiRelationshipName}.
	 * @param ctx the parse tree
	 */
	void exitMultiRelationshipName(PlantUML_ATGParser.MultiRelationshipNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUML_ATGParser#labelMultiplicity}.
	 * @param ctx the parse tree
	 */
	void enterLabelMultiplicity(PlantUML_ATGParser.LabelMultiplicityContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUML_ATGParser#labelMultiplicity}.
	 * @param ctx the parse tree
	 */
	void exitLabelMultiplicity(PlantUML_ATGParser.LabelMultiplicityContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUML_ATGParser#cardinality}.
	 * @param ctx the parse tree
	 */
	void enterCardinality(PlantUML_ATGParser.CardinalityContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUML_ATGParser#cardinality}.
	 * @param ctx the parse tree
	 */
	void exitCardinality(PlantUML_ATGParser.CardinalityContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUML_ATGParser#relationTyp}.
	 * @param ctx the parse tree
	 */
	void enterRelationTyp(PlantUML_ATGParser.RelationTypContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUML_ATGParser#relationTyp}.
	 * @param ctx the parse tree
	 */
	void exitRelationTyp(PlantUML_ATGParser.RelationTypContext ctx);
	/**
	 * Enter a parse tree produced by {@link PlantUML_ATGParser#start}.
	 * @param ctx the parse tree
	 */
	void enterStart(PlantUML_ATGParser.StartContext ctx);
	/**
	 * Exit a parse tree produced by {@link PlantUML_ATGParser#start}.
	 * @param ctx the parse tree
	 */
	void exitStart(PlantUML_ATGParser.StartContext ctx);
}

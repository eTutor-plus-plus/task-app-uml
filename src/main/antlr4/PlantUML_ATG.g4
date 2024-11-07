grammar PlantUML_ATG;

@header {
import at.jku.dke.task_app.uml.evaluation.atg.objects.UMLAssociation;
import at.jku.dke.task_app.uml.evaluation.atg.objects.UMLClass;
import at.jku.dke.task_app.uml.evaluation.atg.objects.UMLAttribute;
import at.jku.dke.task_app.uml.evaluation.atg.objects.UMLRelationship;
import at.jku.dke.task_app.uml.evaluation.atg.objects.UMLRelationshipEntity;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
}

@members {
List<UMLClass> umlClasses = new ArrayList<>();
Map<String, List<UMLAttribute>> attributesMap = new HashMap<>();
List<UMLRelationship> relationships = new ArrayList<>();
Map<String, UMLClass> classMap = new HashMap<>();
String currentClassName; // To track current class in context for attributes
List<UMLAssociation> associations = new ArrayList<>();
}

// Parser Rules
classDiagram: '@startuml' ( classDefinition | relationship | multiRelationship | constraints | note | noteConnection | association)* '@enduml'
                {
                    System.out.println("All Classes: " + umlClasses);
                    System.out.println("Attributes Map: " + attributesMap);
                    System.out.println("Relationships: " + relationships);
                };

association: LPAREN className1=className ',' className2=className RPAREN '..' className3=className score?
                {
                    UMLAssociation association = new UMLAssociation();
                    association.setClass1($className1.text);
                    association.setClass2($className2.text);
                    association.setAssoClass($className3.text);
                    associations.add(association);
                };

noteConnection: noteName '..' multiRelationshipName;

constraints: constraintmember1 = constraintmember '..' constraintmember2 = constraintmember ':' '{' constrainttype '}' score?;
constraintmember: (LPAREN className1=className COMMA className2=className RPAREN)|className|multiRelationshipName;
constrainttype: ('disjoint'|'overlapping'|('Teilmenge' labelMultiplicity)|('Ungleich' labelMultiplicity?));

note: 'note'  noteText 'as' noteName;

noteName: Identifier;
noteText: StringLiteral ;

classDefinition: visibility? abstractModifier? 'class' className ('extends' parentClassName )? score?
                {
                    UMLClass clazz = new UMLClass($className.text);
                    clazz.setAbstract($abstractModifier.text != null);
                    if ($parentClassName.text != null) {
                        clazz.addParentClass(classMap.get($parentClassName.text));
                    }
                    umlClasses.add(clazz);
                    classMap.put($className.text, clazz);
                    attributesMap.put($className.text, new ArrayList<>());
                }
                '{' ((attribute|specialAttribute)*) '}';

multiRelationship: DIAMOND multiRelationshipName;

specialAttribute: '{' speciallabel '}' score?;

attribute: attributeName attributeModifier? score?
                {
                    UMLAttribute attr = new UMLAttribute($attributeName.text);
                    if($attributeModifier.text != null){
                    attr.setType($attributeModifier.text);
                    }
                    attributesMap.get(currentClassName).add(attr);
                };

attributeModifier: '{ID}'|'{id}';
score: '[' points ']';
relationship: participant1=participant relationTyp participant2=participant (COLON label)? labelMultiplicity? score?
                {
                    UMLRelationshipEntity entity1 = new UMLRelationshipEntity();
                    entity1.setClassname($participant1.text);
                    entity1.setMultiplicity($participant1.multiplicity);

                    UMLRelationshipEntity entity2 = new UMLRelationshipEntity();
                    entity2.setClassname($participant2.text);
                    entity2.setMultiplicity($participant2.multiplicity);



                    UMLRelationship relation = new UMLRelationship();
                    relation.setEntities(entities);
                    relation.setName($label.text);
                    relation.setType($relationTyp.text);
                    relation.setEntity1(entity1);
                    relation.setEntity2(entity2);
                    relationships.add(relation);
                };

participant returns [String multiplicity]
    : participantMultiplicity1=participantMultiplicity? className participantMultiplicity2=participantMultiplicity?
        {
            currentClassName = $className.text;
            if($participantMultiplicity1.text != null)
            {
                $multiplicity = $participantMultiplicity1.text;
            }
            else if($participantMultiplicity2.text != null)
            {
                $multiplicity = $participantMultiplicity2.text;
            }
            else
            {
                $multiplicity = "";
            }
        };

participantMultiplicity returns [String multiplicity]
     : StringLiteral
         {
             $multiplicity = $text;
         };

// Lexer Rules
visibility: ('+' | '-' | '#' | '~');
abstractModifier: 'abstract';
points: Integer;
className: Identifier
                    {
                        currentClassName = $Identifier.text;
                    };
parentClassName: Identifier;
speciallabel: (Identifier|'('|')'|','|Integer|labelMultiplicity)+;
attributeName: Identifier;
label: Identifier+;
multiRelationshipName: Identifier;
labelMultiplicity returns [String multiplicity]
    : (STAR |QUESTION| PLUS|GT|LT)
        {
            $multiplicity = $text;
        };
cardinality: Integer;
relationTyp : RELATION_TYPE;



RELATION_TYPE
    : '*--' | '--' | '<--' | '-->' | '--*'
    ;

// Define StringLiteral to match strings in double quotes
StringLiteral
    : '"' ('\\' . | ~["\\\r\n])* '"'
    ;

// Define tokens for operators and symbols
COLON: ':';
SEMICOLON: ';';
COMMA: ',';
LPAREN: '(';
RPAREN: ')';
LBRACE: '{';
RBRACE: '}';
LT: '<';
GT: '>';
STAR: '*';
PLUS: '+';
QUESTION: '?';
DIAMOND: '<>'|'diamond';


// Skip lines starting with 'skinparam'
SKINPARAM_LINE : [ \t]* 'skinparam' ~[\r\n]* ('\r'? '\n')? -> skip;
// Skip white spaces and newlines
WS: [ \t\r\n]+ -> skip;

Identifier: [a-zA-Z_][a-zA-Z0-9_]*;
Integer : [0-9]+;
// Parser entry point
start: classDiagram;

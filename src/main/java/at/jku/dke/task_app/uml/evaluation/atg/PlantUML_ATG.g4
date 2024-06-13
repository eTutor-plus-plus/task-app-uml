grammar PlantUML_ATG;

// Parser Rules
classDiagram: '@startuml' (classDefinition | relationship | multiRelationship | constraints | note | noteConnection | association)* '@enduml'
                {
                    System.out.println("All Classes: " + umlClasses);
                    System.out.println("Attributes Map: " + attributesMap);
                    System.out.println("Relationships: " + relationships);
                };

association: '(' className1=className ',' className2=className ')' '..' className3=className
                {
                    UMLAssociation assoc = new UMLAssociation(classMap.get($className1.text), classMap.get($className2.text), classMap.get($className3.text));
                    relationships.add(assoc);
                };

noteConnection: noteName '..' multiRelationshipName;

constraints: '(' className1=className ',' className2=className ')' '..' '(' className3=className ',' className4=className ')' ':' '{' constrainttype '}';

constrainttype: ('disjoint'|'overlapping'|('Teilmenge' labelMultiplicity)|('Ungleich' labelMultiplicity?));

note: 'note' '"' noteText '"' 'as' noteName;

noteName: Identifier;
noteText: Identifier;

classDefinition: visibility? abstractModifier? 'class' className ('extends' parentClassName)?
                {
                    UMLClass clazz = new UMLClass($className.text);
                    clazz.setAbstract($abstractModifier.text != null);
                    if ($parentClassName.text != null && $parentClassName.text != null) {
                        clazz.setParentClass(classMap.get($parentClassName.text));
                    }
                    umlClasses.add(clazz);
                    classMap.put($className.text, clazz);
                    attributesMap.put($className.text, new ArrayList<>());
                }
                '{' (attribute*) '}';

multiRelationship: 'diamond' multiRelationshipName;

attribute: attributeName attributeModifier?
                {
                    UMLAttribute attr = new UMLAttribute($attributeName.text);
                    attr.setModifier($attributeModifier.text != null);
                    attributesMap.get(currentClassName).add(attr);
                };

attributeModifier: '{ID}';

relationship: participant1=participant relationTyp participant2=participant (':' label)? labelMultiplicity?
                {
                    UMLRelationship relationship = new UMLRelationship(classMap.get($participant1.text), classMap.get($participant2.text), $relationTyp.text);
                    relationships.add(relationship);
                };

relationTyp: ('*--'|'--' | '<--' | '---|>' | '<|--');

participant: participantMultiplicity? className participantMultiplicity?
                {
                    currentClassName = $className.text;
                };

participantMultiplicity: '"'('*'|cardinality|(cardinality'..'cardinality))'"';

// Lexer Rules
visibility: ('+' | '-' | '#' | '~');
abstractModifier: 'abstract';
className: Identifier
                    {
                        currentClassName = $Identifier.text;
                    };
parentClassName: Identifier;
attributeName: Identifier;
label: Identifier+;
multiRelationshipName: Identifier;
labelMultiplicity: ('*' | '?' | '+'|'>'|'<')?;
cardinality: Integer;
// Skip white spaces and newlines
WS: [ \t\r\n]+ -> skip;
Identifier: [a-zA-Z_][a-zA-Z0-9_]*;
Integer : [0-9]+;
// Parser entry point
start: classDiagram;

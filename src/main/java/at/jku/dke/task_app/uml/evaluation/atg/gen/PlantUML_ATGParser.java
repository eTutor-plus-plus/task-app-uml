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

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class PlantUML_ATGParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9,
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17,
		T__17=18, RELATION_TYPE=19, StringLiteral=20, COLON=21, SEMICOLON=22,
		COMMA=23, LPAREN=24, RPAREN=25, LBRACE=26, RBRACE=27, LT=28, GT=29, STAR=30,
		PLUS=31, QUESTION=32, DIAMOND=33, SKINPARAM_LINE=34, WS=35, Identifier=36,
		Integer=37;
	public static final int
		RULE_classDiagram = 0, RULE_association = 1, RULE_noteConnection = 2,
		RULE_constraints = 3, RULE_constraintmember = 4, RULE_constrainttype = 5,
		RULE_note = 6, RULE_noteName = 7, RULE_noteText = 8, RULE_classDefinition = 9,
		RULE_multiRelationship = 10, RULE_specialAttribute = 11, RULE_attribute = 12,
		RULE_attributeModifier = 13, RULE_score = 14, RULE_relationship = 15,
		RULE_participant = 16, RULE_participantMultiplicity = 17, RULE_visibility = 18,
		RULE_abstractModifier = 19, RULE_points = 20, RULE_className = 21, RULE_parentClassName = 22,
		RULE_speciallabel = 23, RULE_attributeName = 24, RULE_label = 25, RULE_multiRelationshipName = 26,
		RULE_labelMultiplicity = 27, RULE_cardinality = 28, RULE_relationTyp = 29,
		RULE_start = 30;
	private static String[] makeRuleNames() {
		return new String[] {
			"classDiagram", "association", "noteConnection", "constraints", "constraintmember",
			"constrainttype", "note", "noteName", "noteText", "classDefinition",
			"multiRelationship", "specialAttribute", "attribute", "attributeModifier",
			"score", "relationship", "participant", "participantMultiplicity", "visibility",
			"abstractModifier", "points", "className", "parentClassName", "speciallabel",
			"attributeName", "label", "multiRelationshipName", "labelMultiplicity",
			"cardinality", "relationTyp", "start"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'@startuml'", "'@enduml'", "'..'", "'disjoint'", "'overlapping'",
			"'Teilmenge'", "'Ungleich'", "'note'", "'as'", "'class'", "'extends'",
			"'{ID}'", "'['", "']'", "'-'", "'#'", "'~'", "'abstract'", null, null,
			"':'", "';'", "','", "'('", "')'", "'{'", "'}'", "'<'", "'>'", "'*'",
			"'+'", "'?'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null,
			null, null, null, null, null, null, null, "RELATION_TYPE", "StringLiteral",
			"COLON", "SEMICOLON", "COMMA", "LPAREN", "RPAREN", "LBRACE", "RBRACE",
			"LT", "GT", "STAR", "PLUS", "QUESTION", "DIAMOND", "SKINPARAM_LINE",
			"WS", "Identifier", "Integer"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "PlantUML_ATG.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }


	List<UMLClass> umlClasses = new ArrayList<>();
	Map<String, List<UMLAttribute>> attributesMap = new HashMap<>();
	List<UMLRelationship> relationships = new ArrayList<>();
	Map<String, UMLClass> classMap = new HashMap<>();
	String currentClassName; // To track current class in context for attributes
	List<UMLAssociation> associations = new ArrayList<>();

	public PlantUML_ATGParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ClassDiagramContext extends ParserRuleContext {
		public List<ClassDefinitionContext> classDefinition() {
			return getRuleContexts(ClassDefinitionContext.class);
		}
		public ClassDefinitionContext classDefinition(int i) {
			return getRuleContext(ClassDefinitionContext.class,i);
		}
		public List<RelationshipContext> relationship() {
			return getRuleContexts(RelationshipContext.class);
		}
		public RelationshipContext relationship(int i) {
			return getRuleContext(RelationshipContext.class,i);
		}
		public List<MultiRelationshipContext> multiRelationship() {
			return getRuleContexts(MultiRelationshipContext.class);
		}
		public MultiRelationshipContext multiRelationship(int i) {
			return getRuleContext(MultiRelationshipContext.class,i);
		}
		public List<ConstraintsContext> constraints() {
			return getRuleContexts(ConstraintsContext.class);
		}
		public ConstraintsContext constraints(int i) {
			return getRuleContext(ConstraintsContext.class,i);
		}
		public List<NoteContext> note() {
			return getRuleContexts(NoteContext.class);
		}
		public NoteContext note(int i) {
			return getRuleContext(NoteContext.class,i);
		}
		public List<NoteConnectionContext> noteConnection() {
			return getRuleContexts(NoteConnectionContext.class);
		}
		public NoteConnectionContext noteConnection(int i) {
			return getRuleContext(NoteConnectionContext.class,i);
		}
		public List<AssociationContext> association() {
			return getRuleContexts(AssociationContext.class);
		}
		public AssociationContext association(int i) {
			return getRuleContext(AssociationContext.class,i);
		}
		public ClassDiagramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classDiagram; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).enterClassDiagram(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).exitClassDiagram(this);
		}
	}

	public final ClassDiagramContext classDiagram() throws RecognitionException {
		ClassDiagramContext _localctx = new ClassDiagramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_classDiagram);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(62);
			match(T__0);
			setState(72);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 79475213568L) != 0)) {
				{
				setState(70);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
				case 1:
					{
					setState(63);
					classDefinition();
					}
					break;
				case 2:
					{
					setState(64);
					relationship();
					}
					break;
				case 3:
					{
					setState(65);
					multiRelationship();
					}
					break;
				case 4:
					{
					setState(66);
					constraints();
					}
					break;
				case 5:
					{
					setState(67);
					note();
					}
					break;
				case 6:
					{
					setState(68);
					noteConnection();
					}
					break;
				case 7:
					{
					setState(69);
					association();
					}
					break;
				}
				}
				setState(74);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(75);
			match(T__1);

			                    System.out.println("All Classes: " + umlClasses);
			                    System.out.println("Attributes Map: " + attributesMap);
			                    System.out.println("Relationships: " + relationships);

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AssociationContext extends ParserRuleContext {
		public ClassNameContext className1;
		public ClassNameContext className2;
		public ClassNameContext className3;
		public TerminalNode LPAREN() { return getToken(PlantUML_ATGParser.LPAREN, 0); }
		public TerminalNode COMMA() { return getToken(PlantUML_ATGParser.COMMA, 0); }
		public TerminalNode RPAREN() { return getToken(PlantUML_ATGParser.RPAREN, 0); }
		public List<ClassNameContext> className() {
			return getRuleContexts(ClassNameContext.class);
		}
		public ClassNameContext className(int i) {
			return getRuleContext(ClassNameContext.class,i);
		}
		public ScoreContext score() {
			return getRuleContext(ScoreContext.class,0);
		}
		public AssociationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_association; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).enterAssociation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).exitAssociation(this);
		}
	}

	public final AssociationContext association() throws RecognitionException {
		AssociationContext _localctx = new AssociationContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_association);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(78);
			match(LPAREN);
			setState(79);
			((AssociationContext)_localctx).className1 = className();
			setState(80);
			match(COMMA);
			setState(81);
			((AssociationContext)_localctx).className2 = className();
			setState(82);
			match(RPAREN);
			setState(83);
			match(T__2);
			setState(84);
			((AssociationContext)_localctx).className3 = className();
			setState(86);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__12) {
				{
				setState(85);
				score();
				}
			}


			                    UMLAssociation association = new UMLAssociation();
			                    association.setClass1((((AssociationContext)_localctx).className1!=null?_input.getText(((AssociationContext)_localctx).className1.start,((AssociationContext)_localctx).className1.stop):null));
			                    association.setClass2((((AssociationContext)_localctx).className2!=null?_input.getText(((AssociationContext)_localctx).className2.start,((AssociationContext)_localctx).className2.stop):null));
			                    association.setAssoClass((((AssociationContext)_localctx).className3!=null?_input.getText(((AssociationContext)_localctx).className3.start,((AssociationContext)_localctx).className3.stop):null));
			                    associations.add(association);

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NoteConnectionContext extends ParserRuleContext {
		public NoteNameContext noteName() {
			return getRuleContext(NoteNameContext.class,0);
		}
		public MultiRelationshipNameContext multiRelationshipName() {
			return getRuleContext(MultiRelationshipNameContext.class,0);
		}
		public NoteConnectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_noteConnection; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).enterNoteConnection(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).exitNoteConnection(this);
		}
	}

	public final NoteConnectionContext noteConnection() throws RecognitionException {
		NoteConnectionContext _localctx = new NoteConnectionContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_noteConnection);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(90);
			noteName();
			setState(91);
			match(T__2);
			setState(92);
			multiRelationshipName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConstraintsContext extends ParserRuleContext {
		public List<ConstraintmemberContext> constraintmember() {
			return getRuleContexts(ConstraintmemberContext.class);
		}
		public ConstraintmemberContext constraintmember(int i) {
			return getRuleContext(ConstraintmemberContext.class,i);
		}
		public TerminalNode COLON() { return getToken(PlantUML_ATGParser.COLON, 0); }
		public TerminalNode LBRACE() { return getToken(PlantUML_ATGParser.LBRACE, 0); }
		public ConstrainttypeContext constrainttype() {
			return getRuleContext(ConstrainttypeContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(PlantUML_ATGParser.RBRACE, 0); }
		public ScoreContext score() {
			return getRuleContext(ScoreContext.class,0);
		}
		public ConstraintsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constraints; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).enterConstraints(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).exitConstraints(this);
		}
	}

	public final ConstraintsContext constraints() throws RecognitionException {
		ConstraintsContext _localctx = new ConstraintsContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_constraints);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(94);
			constraintmember();
			setState(95);
			match(T__2);
			setState(96);
			constraintmember();
			setState(97);
			match(COLON);
			setState(98);
			match(LBRACE);
			setState(99);
			constrainttype();
			setState(100);
			match(RBRACE);
			setState(102);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__12) {
				{
				setState(101);
				score();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConstraintmemberContext extends ParserRuleContext {
		public ClassNameContext className1;
		public ClassNameContext className2;
		public TerminalNode LPAREN() { return getToken(PlantUML_ATGParser.LPAREN, 0); }
		public TerminalNode COMMA() { return getToken(PlantUML_ATGParser.COMMA, 0); }
		public TerminalNode RPAREN() { return getToken(PlantUML_ATGParser.RPAREN, 0); }
		public List<ClassNameContext> className() {
			return getRuleContexts(ClassNameContext.class);
		}
		public ClassNameContext className(int i) {
			return getRuleContext(ClassNameContext.class,i);
		}
		public MultiRelationshipNameContext multiRelationshipName() {
			return getRuleContext(MultiRelationshipNameContext.class,0);
		}
		public ConstraintmemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constraintmember; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).enterConstraintmember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).exitConstraintmember(this);
		}
	}

	public final ConstraintmemberContext constraintmember() throws RecognitionException {
		ConstraintmemberContext _localctx = new ConstraintmemberContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_constraintmember);
		try {
			setState(112);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(104);
				match(LPAREN);
				setState(105);
				((ConstraintmemberContext)_localctx).className1 = className();
				setState(106);
				match(COMMA);
				setState(107);
				((ConstraintmemberContext)_localctx).className2 = className();
				setState(108);
				match(RPAREN);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(110);
				className();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(111);
				multiRelationshipName();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConstrainttypeContext extends ParserRuleContext {
		public LabelMultiplicityContext labelMultiplicity() {
			return getRuleContext(LabelMultiplicityContext.class,0);
		}
		public ConstrainttypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constrainttype; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).enterConstrainttype(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).exitConstrainttype(this);
		}
	}

	public final ConstrainttypeContext constrainttype() throws RecognitionException {
		ConstrainttypeContext _localctx = new ConstrainttypeContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_constrainttype);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(122);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__3:
				{
				setState(114);
				match(T__3);
				}
				break;
			case T__4:
				{
				setState(115);
				match(T__4);
				}
				break;
			case T__5:
				{
				{
				setState(116);
				match(T__5);
				setState(117);
				labelMultiplicity();
				}
				}
				break;
			case T__6:
				{
				{
				setState(118);
				match(T__6);
				setState(120);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 8321499136L) != 0)) {
					{
					setState(119);
					labelMultiplicity();
					}
				}

				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NoteContext extends ParserRuleContext {
		public NoteTextContext noteText() {
			return getRuleContext(NoteTextContext.class,0);
		}
		public NoteNameContext noteName() {
			return getRuleContext(NoteNameContext.class,0);
		}
		public NoteContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_note; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).enterNote(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).exitNote(this);
		}
	}

	public final NoteContext note() throws RecognitionException {
		NoteContext _localctx = new NoteContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_note);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(124);
			match(T__7);
			setState(125);
			noteText();
			setState(126);
			match(T__8);
			setState(127);
			noteName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NoteNameContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(PlantUML_ATGParser.Identifier, 0); }
		public NoteNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_noteName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).enterNoteName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).exitNoteName(this);
		}
	}

	public final NoteNameContext noteName() throws RecognitionException {
		NoteNameContext _localctx = new NoteNameContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_noteName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(129);
			match(Identifier);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NoteTextContext extends ParserRuleContext {
		public TerminalNode StringLiteral() { return getToken(PlantUML_ATGParser.StringLiteral, 0); }
		public NoteTextContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_noteText; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).enterNoteText(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).exitNoteText(this);
		}
	}

	public final NoteTextContext noteText() throws RecognitionException {
		NoteTextContext _localctx = new NoteTextContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_noteText);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(131);
			match(StringLiteral);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ClassDefinitionContext extends ParserRuleContext {
		public AbstractModifierContext abstractModifier;
		public ClassNameContext className;
		public ParentClassNameContext parentClassName;
		public ClassNameContext className() {
			return getRuleContext(ClassNameContext.class,0);
		}
		public TerminalNode LBRACE() { return getToken(PlantUML_ATGParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(PlantUML_ATGParser.RBRACE, 0); }
		public VisibilityContext visibility() {
			return getRuleContext(VisibilityContext.class,0);
		}
		public AbstractModifierContext abstractModifier() {
			return getRuleContext(AbstractModifierContext.class,0);
		}
		public ParentClassNameContext parentClassName() {
			return getRuleContext(ParentClassNameContext.class,0);
		}
		public ScoreContext score() {
			return getRuleContext(ScoreContext.class,0);
		}
		public List<AttributeContext> attribute() {
			return getRuleContexts(AttributeContext.class);
		}
		public AttributeContext attribute(int i) {
			return getRuleContext(AttributeContext.class,i);
		}
		public List<SpecialAttributeContext> specialAttribute() {
			return getRuleContexts(SpecialAttributeContext.class);
		}
		public SpecialAttributeContext specialAttribute(int i) {
			return getRuleContext(SpecialAttributeContext.class,i);
		}
		public ClassDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).enterClassDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).exitClassDefinition(this);
		}
	}

	public final ClassDefinitionContext classDefinition() throws RecognitionException {
		ClassDefinitionContext _localctx = new ClassDefinitionContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_classDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(134);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 2147713024L) != 0)) {
				{
				setState(133);
				visibility();
				}
			}

			setState(137);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__17) {
				{
				setState(136);
				((ClassDefinitionContext)_localctx).abstractModifier = abstractModifier();
				}
			}

			setState(139);
			match(T__9);
			setState(140);
			((ClassDefinitionContext)_localctx).className = className();
			setState(143);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__10) {
				{
				setState(141);
				match(T__10);
				setState(142);
				((ClassDefinitionContext)_localctx).parentClassName = parentClassName();
				}
			}

			setState(146);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__12) {
				{
				setState(145);
				score();
				}
			}


			                    UMLClass clazz = new UMLClass((((ClassDefinitionContext)_localctx).className!=null?_input.getText(((ClassDefinitionContext)_localctx).className.start,((ClassDefinitionContext)_localctx).className.stop):null));
			                    clazz.setAbstract((((ClassDefinitionContext)_localctx).abstractModifier!=null?_input.getText(((ClassDefinitionContext)_localctx).abstractModifier.start,((ClassDefinitionContext)_localctx).abstractModifier.stop):null) != null);
			                    if ((((ClassDefinitionContext)_localctx).parentClassName!=null?_input.getText(((ClassDefinitionContext)_localctx).parentClassName.start,((ClassDefinitionContext)_localctx).parentClassName.stop):null) != null) {
			                        clazz.setParentClass(classMap.get((((ClassDefinitionContext)_localctx).parentClassName!=null?_input.getText(((ClassDefinitionContext)_localctx).parentClassName.start,((ClassDefinitionContext)_localctx).parentClassName.stop):null)));
			                    }
			                    umlClasses.add(clazz);
			                    classMap.put((((ClassDefinitionContext)_localctx).className!=null?_input.getText(((ClassDefinitionContext)_localctx).className.start,((ClassDefinitionContext)_localctx).className.stop):null), clazz);
			                    attributesMap.put((((ClassDefinitionContext)_localctx).className!=null?_input.getText(((ClassDefinitionContext)_localctx).className.start,((ClassDefinitionContext)_localctx).className.stop):null), new ArrayList<>());

			setState(149);
			match(LBRACE);
			{
			setState(154);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LBRACE || _la==Identifier) {
				{
				setState(152);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Identifier:
					{
					setState(150);
					attribute();
					}
					break;
				case LBRACE:
					{
					setState(151);
					specialAttribute();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(156);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
			setState(157);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MultiRelationshipContext extends ParserRuleContext {
		public TerminalNode DIAMOND() { return getToken(PlantUML_ATGParser.DIAMOND, 0); }
		public MultiRelationshipNameContext multiRelationshipName() {
			return getRuleContext(MultiRelationshipNameContext.class,0);
		}
		public MultiRelationshipContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multiRelationship; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).enterMultiRelationship(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).exitMultiRelationship(this);
		}
	}

	public final MultiRelationshipContext multiRelationship() throws RecognitionException {
		MultiRelationshipContext _localctx = new MultiRelationshipContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_multiRelationship);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(159);
			match(DIAMOND);
			setState(160);
			multiRelationshipName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SpecialAttributeContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(PlantUML_ATGParser.LBRACE, 0); }
		public SpeciallabelContext speciallabel() {
			return getRuleContext(SpeciallabelContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(PlantUML_ATGParser.RBRACE, 0); }
		public ScoreContext score() {
			return getRuleContext(ScoreContext.class,0);
		}
		public SpecialAttributeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_specialAttribute; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).enterSpecialAttribute(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).exitSpecialAttribute(this);
		}
	}

	public final SpecialAttributeContext specialAttribute() throws RecognitionException {
		SpecialAttributeContext _localctx = new SpecialAttributeContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_specialAttribute);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(162);
			match(LBRACE);
			setState(163);
			speciallabel();
			setState(164);
			match(RBRACE);
			setState(166);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__12) {
				{
				setState(165);
				score();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AttributeContext extends ParserRuleContext {
		public AttributeNameContext attributeName;
		public AttributeModifierContext attributeModifier;
		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}
		public AttributeModifierContext attributeModifier() {
			return getRuleContext(AttributeModifierContext.class,0);
		}
		public ScoreContext score() {
			return getRuleContext(ScoreContext.class,0);
		}
		public AttributeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attribute; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).enterAttribute(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).exitAttribute(this);
		}
	}

	public final AttributeContext attribute() throws RecognitionException {
		AttributeContext _localctx = new AttributeContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_attribute);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(168);
			((AttributeContext)_localctx).attributeName = attributeName();
			setState(170);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__11) {
				{
				setState(169);
				((AttributeContext)_localctx).attributeModifier = attributeModifier();
				}
			}

			setState(173);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__12) {
				{
				setState(172);
				score();
				}
			}


			                    UMLAttribute attr = new UMLAttribute((((AttributeContext)_localctx).attributeName!=null?_input.getText(((AttributeContext)_localctx).attributeName.start,((AttributeContext)_localctx).attributeName.stop):null));
			                    if((((AttributeContext)_localctx).attributeModifier!=null?_input.getText(((AttributeContext)_localctx).attributeModifier.start,((AttributeContext)_localctx).attributeModifier.stop):null) != null){
			                    attr.setType((((AttributeContext)_localctx).attributeModifier!=null?_input.getText(((AttributeContext)_localctx).attributeModifier.start,((AttributeContext)_localctx).attributeModifier.stop):null));
			                    }
			                    attributesMap.get(currentClassName).add(attr);

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AttributeModifierContext extends ParserRuleContext {
		public AttributeModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeModifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).enterAttributeModifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).exitAttributeModifier(this);
		}
	}

	public final AttributeModifierContext attributeModifier() throws RecognitionException {
		AttributeModifierContext _localctx = new AttributeModifierContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_attributeModifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(177);
			match(T__11);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ScoreContext extends ParserRuleContext {
		public PointsContext points() {
			return getRuleContext(PointsContext.class,0);
		}
		public ScoreContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_score; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).enterScore(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).exitScore(this);
		}
	}

	public final ScoreContext score() throws RecognitionException {
		ScoreContext _localctx = new ScoreContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_score);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(179);
			match(T__12);
			setState(180);
			points();
			setState(181);
			match(T__13);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RelationshipContext extends ParserRuleContext {
		public ParticipantContext participant1;
		public RelationTypContext relationTyp;
		public ParticipantContext participant2;
		public LabelContext label;
		public RelationTypContext relationTyp() {
			return getRuleContext(RelationTypContext.class,0);
		}
		public List<ParticipantContext> participant() {
			return getRuleContexts(ParticipantContext.class);
		}
		public ParticipantContext participant(int i) {
			return getRuleContext(ParticipantContext.class,i);
		}
		public TerminalNode COLON() { return getToken(PlantUML_ATGParser.COLON, 0); }
		public LabelContext label() {
			return getRuleContext(LabelContext.class,0);
		}
		public LabelMultiplicityContext labelMultiplicity() {
			return getRuleContext(LabelMultiplicityContext.class,0);
		}
		public ScoreContext score() {
			return getRuleContext(ScoreContext.class,0);
		}
		public RelationshipContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relationship; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).enterRelationship(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).exitRelationship(this);
		}
	}

	public final RelationshipContext relationship() throws RecognitionException {
		RelationshipContext _localctx = new RelationshipContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_relationship);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(183);
			((RelationshipContext)_localctx).participant1 = participant();
			setState(184);
			((RelationshipContext)_localctx).relationTyp = relationTyp();
			setState(185);
			((RelationshipContext)_localctx).participant2 = participant();
			setState(188);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==COLON) {
				{
				setState(186);
				match(COLON);
				setState(187);
				((RelationshipContext)_localctx).label = label();
				}
			}

			setState(191);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				{
				setState(190);
				labelMultiplicity();
				}
				break;
			}
			setState(194);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__12) {
				{
				setState(193);
				score();
				}
			}


			                    UMLRelationshipEntity entity1 = new UMLRelationshipEntity();
			                    entity1.setClassname((((RelationshipContext)_localctx).participant1!=null?_input.getText(((RelationshipContext)_localctx).participant1.start,((RelationshipContext)_localctx).participant1.stop):null));
			                    entity1.setMultiplicity(((RelationshipContext)_localctx).participant1.multiplicity);

			                    UMLRelationshipEntity entity2 = new UMLRelationshipEntity();
			                    entity2.setClassname((((RelationshipContext)_localctx).participant2!=null?_input.getText(((RelationshipContext)_localctx).participant2.start,((RelationshipContext)_localctx).participant2.stop):null));
			                    entity2.setMultiplicity(((RelationshipContext)_localctx).participant2.multiplicity);

			                    List<UMLRelationshipEntity> entities = new ArrayList<>();
			                    entities.add(entity1);
			                    entities.add(entity2);

			                    UMLRelationship relation = new UMLRelationship();
			                    relation.setEntities(entities);
			                    relation.setName((((RelationshipContext)_localctx).label!=null?_input.getText(((RelationshipContext)_localctx).label.start,((RelationshipContext)_localctx).label.stop):null));
			                    relation.setType((((RelationshipContext)_localctx).relationTyp!=null?_input.getText(((RelationshipContext)_localctx).relationTyp.start,((RelationshipContext)_localctx).relationTyp.stop):null));

			                    relationships.add(relation);

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParticipantContext extends ParserRuleContext {
		public String multiplicity;
		public ParticipantMultiplicityContext participantMultiplicity1;
		public ClassNameContext className;
		public ParticipantMultiplicityContext participantMultiplicity2;
		public ClassNameContext className() {
			return getRuleContext(ClassNameContext.class,0);
		}
		public List<ParticipantMultiplicityContext> participantMultiplicity() {
			return getRuleContexts(ParticipantMultiplicityContext.class);
		}
		public ParticipantMultiplicityContext participantMultiplicity(int i) {
			return getRuleContext(ParticipantMultiplicityContext.class,i);
		}
		public ParticipantContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_participant; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).enterParticipant(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).exitParticipant(this);
		}
	}

	public final ParticipantContext participant() throws RecognitionException {
		ParticipantContext _localctx = new ParticipantContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_participant);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(199);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==StringLiteral) {
				{
				setState(198);
				((ParticipantContext)_localctx).participantMultiplicity1 = participantMultiplicity();
				}
			}

			setState(201);
			((ParticipantContext)_localctx).className = className();
			setState(203);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				{
				setState(202);
				((ParticipantContext)_localctx).participantMultiplicity2 = participantMultiplicity();
				}
				break;
			}

			            currentClassName = (((ParticipantContext)_localctx).className!=null?_input.getText(((ParticipantContext)_localctx).className.start,((ParticipantContext)_localctx).className.stop):null);
			            if((((ParticipantContext)_localctx).participantMultiplicity1!=null?_input.getText(((ParticipantContext)_localctx).participantMultiplicity1.start,((ParticipantContext)_localctx).participantMultiplicity1.stop):null) != null)
			            {
			                ((ParticipantContext)_localctx).multiplicity =  (((ParticipantContext)_localctx).participantMultiplicity1!=null?_input.getText(((ParticipantContext)_localctx).participantMultiplicity1.start,((ParticipantContext)_localctx).participantMultiplicity1.stop):null);
			            }
			            else if((((ParticipantContext)_localctx).participantMultiplicity2!=null?_input.getText(((ParticipantContext)_localctx).participantMultiplicity2.start,((ParticipantContext)_localctx).participantMultiplicity2.stop):null) != null)
			            {
			                ((ParticipantContext)_localctx).multiplicity =  (((ParticipantContext)_localctx).participantMultiplicity2!=null?_input.getText(((ParticipantContext)_localctx).participantMultiplicity2.start,((ParticipantContext)_localctx).participantMultiplicity2.stop):null);
			            }
			            else
			            {
			                ((ParticipantContext)_localctx).multiplicity =  "";
			            }

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParticipantMultiplicityContext extends ParserRuleContext {
		public String multiplicity;
		public TerminalNode StringLiteral() { return getToken(PlantUML_ATGParser.StringLiteral, 0); }
		public ParticipantMultiplicityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_participantMultiplicity; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).enterParticipantMultiplicity(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).exitParticipantMultiplicity(this);
		}
	}

	public final ParticipantMultiplicityContext participantMultiplicity() throws RecognitionException {
		ParticipantMultiplicityContext _localctx = new ParticipantMultiplicityContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_participantMultiplicity);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(207);
			match(StringLiteral);

			             ((ParticipantMultiplicityContext)_localctx).multiplicity =  _input.getText(_localctx.start, _input.LT(-1));

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VisibilityContext extends ParserRuleContext {
		public TerminalNode PLUS() { return getToken(PlantUML_ATGParser.PLUS, 0); }
		public VisibilityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_visibility; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).enterVisibility(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).exitVisibility(this);
		}
	}

	public final VisibilityContext visibility() throws RecognitionException {
		VisibilityContext _localctx = new VisibilityContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_visibility);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(210);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 2147713024L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AbstractModifierContext extends ParserRuleContext {
		public AbstractModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_abstractModifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).enterAbstractModifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).exitAbstractModifier(this);
		}
	}

	public final AbstractModifierContext abstractModifier() throws RecognitionException {
		AbstractModifierContext _localctx = new AbstractModifierContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_abstractModifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(212);
			match(T__17);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PointsContext extends ParserRuleContext {
		public TerminalNode Integer() { return getToken(PlantUML_ATGParser.Integer, 0); }
		public PointsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_points; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).enterPoints(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).exitPoints(this);
		}
	}

	public final PointsContext points() throws RecognitionException {
		PointsContext _localctx = new PointsContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_points);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(214);
			match(Integer);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ClassNameContext extends ParserRuleContext {
		public Token Identifier;
		public TerminalNode Identifier() { return getToken(PlantUML_ATGParser.Identifier, 0); }
		public ClassNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_className; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).enterClassName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).exitClassName(this);
		}
	}

	public final ClassNameContext className() throws RecognitionException {
		ClassNameContext _localctx = new ClassNameContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_className);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(216);
			((ClassNameContext)_localctx).Identifier = match(Identifier);

			                        currentClassName = (((ClassNameContext)_localctx).Identifier!=null?((ClassNameContext)_localctx).Identifier.getText():null);

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParentClassNameContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(PlantUML_ATGParser.Identifier, 0); }
		public ParentClassNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parentClassName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).enterParentClassName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).exitParentClassName(this);
		}
	}

	public final ParentClassNameContext parentClassName() throws RecognitionException {
		ParentClassNameContext _localctx = new ParentClassNameContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_parentClassName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(219);
			match(Identifier);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SpeciallabelContext extends ParserRuleContext {
		public List<TerminalNode> Identifier() { return getTokens(PlantUML_ATGParser.Identifier); }
		public TerminalNode Identifier(int i) {
			return getToken(PlantUML_ATGParser.Identifier, i);
		}
		public List<TerminalNode> LPAREN() { return getTokens(PlantUML_ATGParser.LPAREN); }
		public TerminalNode LPAREN(int i) {
			return getToken(PlantUML_ATGParser.LPAREN, i);
		}
		public List<TerminalNode> RPAREN() { return getTokens(PlantUML_ATGParser.RPAREN); }
		public TerminalNode RPAREN(int i) {
			return getToken(PlantUML_ATGParser.RPAREN, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(PlantUML_ATGParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(PlantUML_ATGParser.COMMA, i);
		}
		public List<TerminalNode> Integer() { return getTokens(PlantUML_ATGParser.Integer); }
		public TerminalNode Integer(int i) {
			return getToken(PlantUML_ATGParser.Integer, i);
		}
		public List<LabelMultiplicityContext> labelMultiplicity() {
			return getRuleContexts(LabelMultiplicityContext.class);
		}
		public LabelMultiplicityContext labelMultiplicity(int i) {
			return getRuleContext(LabelMultiplicityContext.class,i);
		}
		public SpeciallabelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_speciallabel; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).enterSpeciallabel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).exitSpeciallabel(this);
		}
	}

	public final SpeciallabelContext speciallabel() throws RecognitionException {
		SpeciallabelContext _localctx = new SpeciallabelContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_speciallabel);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(227);
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				setState(227);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Identifier:
					{
					setState(221);
					match(Identifier);
					}
					break;
				case LPAREN:
					{
					setState(222);
					match(LPAREN);
					}
					break;
				case RPAREN:
					{
					setState(223);
					match(RPAREN);
					}
					break;
				case COMMA:
					{
					setState(224);
					match(COMMA);
					}
					break;
				case Integer:
					{
					setState(225);
					match(Integer);
					}
					break;
				case LT:
				case GT:
				case STAR:
				case PLUS:
				case QUESTION:
					{
					setState(226);
					labelMultiplicity();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(229);
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 214538649600L) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AttributeNameContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(PlantUML_ATGParser.Identifier, 0); }
		public AttributeNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).enterAttributeName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).exitAttributeName(this);
		}
	}

	public final AttributeNameContext attributeName() throws RecognitionException {
		AttributeNameContext _localctx = new AttributeNameContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_attributeName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(231);
			match(Identifier);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LabelContext extends ParserRuleContext {
		public List<TerminalNode> Identifier() { return getTokens(PlantUML_ATGParser.Identifier); }
		public TerminalNode Identifier(int i) {
			return getToken(PlantUML_ATGParser.Identifier, i);
		}
		public LabelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_label; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).enterLabel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).exitLabel(this);
		}
	}

	public final LabelContext label() throws RecognitionException {
		LabelContext _localctx = new LabelContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_label);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(234);
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(233);
					match(Identifier);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(236);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,23,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MultiRelationshipNameContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(PlantUML_ATGParser.Identifier, 0); }
		public MultiRelationshipNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multiRelationshipName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).enterMultiRelationshipName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).exitMultiRelationshipName(this);
		}
	}

	public final MultiRelationshipNameContext multiRelationshipName() throws RecognitionException {
		MultiRelationshipNameContext _localctx = new MultiRelationshipNameContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_multiRelationshipName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(238);
			match(Identifier);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LabelMultiplicityContext extends ParserRuleContext {
		public String multiplicity;
		public TerminalNode STAR() { return getToken(PlantUML_ATGParser.STAR, 0); }
		public TerminalNode QUESTION() { return getToken(PlantUML_ATGParser.QUESTION, 0); }
		public TerminalNode PLUS() { return getToken(PlantUML_ATGParser.PLUS, 0); }
		public TerminalNode GT() { return getToken(PlantUML_ATGParser.GT, 0); }
		public TerminalNode LT() { return getToken(PlantUML_ATGParser.LT, 0); }
		public LabelMultiplicityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_labelMultiplicity; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).enterLabelMultiplicity(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).exitLabelMultiplicity(this);
		}
	}

	public final LabelMultiplicityContext labelMultiplicity() throws RecognitionException {
		LabelMultiplicityContext _localctx = new LabelMultiplicityContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_labelMultiplicity);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(240);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 8321499136L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}

			            ((LabelMultiplicityContext)_localctx).multiplicity =  _input.getText(_localctx.start, _input.LT(-1));

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CardinalityContext extends ParserRuleContext {
		public TerminalNode Integer() { return getToken(PlantUML_ATGParser.Integer, 0); }
		public CardinalityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cardinality; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).enterCardinality(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).exitCardinality(this);
		}
	}

	public final CardinalityContext cardinality() throws RecognitionException {
		CardinalityContext _localctx = new CardinalityContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_cardinality);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(243);
			match(Integer);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RelationTypContext extends ParserRuleContext {
		public TerminalNode RELATION_TYPE() { return getToken(PlantUML_ATGParser.RELATION_TYPE, 0); }
		public RelationTypContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relationTyp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).enterRelationTyp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).exitRelationTyp(this);
		}
	}

	public final RelationTypContext relationTyp() throws RecognitionException {
		RelationTypContext _localctx = new RelationTypContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_relationTyp);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(245);
			match(RELATION_TYPE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StartContext extends ParserRuleContext {
		public ClassDiagramContext classDiagram() {
			return getRuleContext(ClassDiagramContext.class,0);
		}
		public StartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_start; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).enterStart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PlantUML_ATGListener ) ((PlantUML_ATGListener)listener).exitStart(this);
		}
	}

	public final StartContext start() throws RecognitionException {
		StartContext _localctx = new StartContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_start);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(247);
			classDiagram();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001%\u00fa\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007\u001b"+
		"\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007\u001e"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0005\u0000G\b\u0000\n\u0000\f\u0000J\t\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u0001"+
		"W\b\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0003\u0003g\b\u0003\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0003\u0004q\b\u0004\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0003\u0005y\b\u0005\u0003\u0005"+
		"{\b\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\t\u0003\t\u0087\b\t\u0001"+
		"\t\u0003\t\u008a\b\t\u0001\t\u0001\t\u0001\t\u0001\t\u0003\t\u0090\b\t"+
		"\u0001\t\u0003\t\u0093\b\t\u0001\t\u0001\t\u0001\t\u0001\t\u0005\t\u0099"+
		"\b\t\n\t\f\t\u009c\t\t\u0001\t\u0001\t\u0001\n\u0001\n\u0001\n\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0001\u000b\u0003\u000b\u00a7\b\u000b\u0001\f"+
		"\u0001\f\u0003\f\u00ab\b\f\u0001\f\u0003\f\u00ae\b\f\u0001\f\u0001\f\u0001"+
		"\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0003\u000f\u00bd\b\u000f"+
		"\u0001\u000f\u0003\u000f\u00c0\b\u000f\u0001\u000f\u0003\u000f\u00c3\b"+
		"\u000f\u0001\u000f\u0001\u000f\u0001\u0010\u0003\u0010\u00c8\b\u0010\u0001"+
		"\u0010\u0001\u0010\u0003\u0010\u00cc\b\u0010\u0001\u0010\u0001\u0010\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0013\u0001"+
		"\u0013\u0001\u0014\u0001\u0014\u0001\u0015\u0001\u0015\u0001\u0015\u0001"+
		"\u0016\u0001\u0016\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001"+
		"\u0017\u0001\u0017\u0004\u0017\u00e4\b\u0017\u000b\u0017\f\u0017\u00e5"+
		"\u0001\u0018\u0001\u0018\u0001\u0019\u0004\u0019\u00eb\b\u0019\u000b\u0019"+
		"\f\u0019\u00ec\u0001\u001a\u0001\u001a\u0001\u001b\u0001\u001b\u0001\u001b"+
		"\u0001\u001c\u0001\u001c\u0001\u001d\u0001\u001d\u0001\u001e\u0001\u001e"+
		"\u0001\u001e\u0000\u0000\u001f\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010"+
		"\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.02468:<\u0000\u0002"+
		"\u0002\u0000\u000f\u0011\u001f\u001f\u0001\u0000\u001c \u00fe\u0000>\u0001"+
		"\u0000\u0000\u0000\u0002N\u0001\u0000\u0000\u0000\u0004Z\u0001\u0000\u0000"+
		"\u0000\u0006^\u0001\u0000\u0000\u0000\bp\u0001\u0000\u0000\u0000\nz\u0001"+
		"\u0000\u0000\u0000\f|\u0001\u0000\u0000\u0000\u000e\u0081\u0001\u0000"+
		"\u0000\u0000\u0010\u0083\u0001\u0000\u0000\u0000\u0012\u0086\u0001\u0000"+
		"\u0000\u0000\u0014\u009f\u0001\u0000\u0000\u0000\u0016\u00a2\u0001\u0000"+
		"\u0000\u0000\u0018\u00a8\u0001\u0000\u0000\u0000\u001a\u00b1\u0001\u0000"+
		"\u0000\u0000\u001c\u00b3\u0001\u0000\u0000\u0000\u001e\u00b7\u0001\u0000"+
		"\u0000\u0000 \u00c7\u0001\u0000\u0000\u0000\"\u00cf\u0001\u0000\u0000"+
		"\u0000$\u00d2\u0001\u0000\u0000\u0000&\u00d4\u0001\u0000\u0000\u0000("+
		"\u00d6\u0001\u0000\u0000\u0000*\u00d8\u0001\u0000\u0000\u0000,\u00db\u0001"+
		"\u0000\u0000\u0000.\u00e3\u0001\u0000\u0000\u00000\u00e7\u0001\u0000\u0000"+
		"\u00002\u00ea\u0001\u0000\u0000\u00004\u00ee\u0001\u0000\u0000\u00006"+
		"\u00f0\u0001\u0000\u0000\u00008\u00f3\u0001\u0000\u0000\u0000:\u00f5\u0001"+
		"\u0000\u0000\u0000<\u00f7\u0001\u0000\u0000\u0000>H\u0005\u0001\u0000"+
		"\u0000?G\u0003\u0012\t\u0000@G\u0003\u001e\u000f\u0000AG\u0003\u0014\n"+
		"\u0000BG\u0003\u0006\u0003\u0000CG\u0003\f\u0006\u0000DG\u0003\u0004\u0002"+
		"\u0000EG\u0003\u0002\u0001\u0000F?\u0001\u0000\u0000\u0000F@\u0001\u0000"+
		"\u0000\u0000FA\u0001\u0000\u0000\u0000FB\u0001\u0000\u0000\u0000FC\u0001"+
		"\u0000\u0000\u0000FD\u0001\u0000\u0000\u0000FE\u0001\u0000\u0000\u0000"+
		"GJ\u0001\u0000\u0000\u0000HF\u0001\u0000\u0000\u0000HI\u0001\u0000\u0000"+
		"\u0000IK\u0001\u0000\u0000\u0000JH\u0001\u0000\u0000\u0000KL\u0005\u0002"+
		"\u0000\u0000LM\u0006\u0000\uffff\uffff\u0000M\u0001\u0001\u0000\u0000"+
		"\u0000NO\u0005\u0018\u0000\u0000OP\u0003*\u0015\u0000PQ\u0005\u0017\u0000"+
		"\u0000QR\u0003*\u0015\u0000RS\u0005\u0019\u0000\u0000ST\u0005\u0003\u0000"+
		"\u0000TV\u0003*\u0015\u0000UW\u0003\u001c\u000e\u0000VU\u0001\u0000\u0000"+
		"\u0000VW\u0001\u0000\u0000\u0000WX\u0001\u0000\u0000\u0000XY\u0006\u0001"+
		"\uffff\uffff\u0000Y\u0003\u0001\u0000\u0000\u0000Z[\u0003\u000e\u0007"+
		"\u0000[\\\u0005\u0003\u0000\u0000\\]\u00034\u001a\u0000]\u0005\u0001\u0000"+
		"\u0000\u0000^_\u0003\b\u0004\u0000_`\u0005\u0003\u0000\u0000`a\u0003\b"+
		"\u0004\u0000ab\u0005\u0015\u0000\u0000bc\u0005\u001a\u0000\u0000cd\u0003"+
		"\n\u0005\u0000df\u0005\u001b\u0000\u0000eg\u0003\u001c\u000e\u0000fe\u0001"+
		"\u0000\u0000\u0000fg\u0001\u0000\u0000\u0000g\u0007\u0001\u0000\u0000"+
		"\u0000hi\u0005\u0018\u0000\u0000ij\u0003*\u0015\u0000jk\u0005\u0017\u0000"+
		"\u0000kl\u0003*\u0015\u0000lm\u0005\u0019\u0000\u0000mq\u0001\u0000\u0000"+
		"\u0000nq\u0003*\u0015\u0000oq\u00034\u001a\u0000ph\u0001\u0000\u0000\u0000"+
		"pn\u0001\u0000\u0000\u0000po\u0001\u0000\u0000\u0000q\t\u0001\u0000\u0000"+
		"\u0000r{\u0005\u0004\u0000\u0000s{\u0005\u0005\u0000\u0000tu\u0005\u0006"+
		"\u0000\u0000u{\u00036\u001b\u0000vx\u0005\u0007\u0000\u0000wy\u00036\u001b"+
		"\u0000xw\u0001\u0000\u0000\u0000xy\u0001\u0000\u0000\u0000y{\u0001\u0000"+
		"\u0000\u0000zr\u0001\u0000\u0000\u0000zs\u0001\u0000\u0000\u0000zt\u0001"+
		"\u0000\u0000\u0000zv\u0001\u0000\u0000\u0000{\u000b\u0001\u0000\u0000"+
		"\u0000|}\u0005\b\u0000\u0000}~\u0003\u0010\b\u0000~\u007f\u0005\t\u0000"+
		"\u0000\u007f\u0080\u0003\u000e\u0007\u0000\u0080\r\u0001\u0000\u0000\u0000"+
		"\u0081\u0082\u0005$\u0000\u0000\u0082\u000f\u0001\u0000\u0000\u0000\u0083"+
		"\u0084\u0005\u0014\u0000\u0000\u0084\u0011\u0001\u0000\u0000\u0000\u0085"+
		"\u0087\u0003$\u0012\u0000\u0086\u0085\u0001\u0000\u0000\u0000\u0086\u0087"+
		"\u0001\u0000\u0000\u0000\u0087\u0089\u0001\u0000\u0000\u0000\u0088\u008a"+
		"\u0003&\u0013\u0000\u0089\u0088\u0001\u0000\u0000\u0000\u0089\u008a\u0001"+
		"\u0000\u0000\u0000\u008a\u008b\u0001\u0000\u0000\u0000\u008b\u008c\u0005"+
		"\n\u0000\u0000\u008c\u008f\u0003*\u0015\u0000\u008d\u008e\u0005\u000b"+
		"\u0000\u0000\u008e\u0090\u0003,\u0016\u0000\u008f\u008d\u0001\u0000\u0000"+
		"\u0000\u008f\u0090\u0001\u0000\u0000\u0000\u0090\u0092\u0001\u0000\u0000"+
		"\u0000\u0091\u0093\u0003\u001c\u000e\u0000\u0092\u0091\u0001\u0000\u0000"+
		"\u0000\u0092\u0093\u0001\u0000\u0000\u0000\u0093\u0094\u0001\u0000\u0000"+
		"\u0000\u0094\u0095\u0006\t\uffff\uffff\u0000\u0095\u009a\u0005\u001a\u0000"+
		"\u0000\u0096\u0099\u0003\u0018\f\u0000\u0097\u0099\u0003\u0016\u000b\u0000"+
		"\u0098\u0096\u0001\u0000\u0000\u0000\u0098\u0097\u0001\u0000\u0000\u0000"+
		"\u0099\u009c\u0001\u0000\u0000\u0000\u009a\u0098\u0001\u0000\u0000\u0000"+
		"\u009a\u009b\u0001\u0000\u0000\u0000\u009b\u009d\u0001\u0000\u0000\u0000"+
		"\u009c\u009a\u0001\u0000\u0000\u0000\u009d\u009e\u0005\u001b\u0000\u0000"+
		"\u009e\u0013\u0001\u0000\u0000\u0000\u009f\u00a0\u0005!\u0000\u0000\u00a0"+
		"\u00a1\u00034\u001a\u0000\u00a1\u0015\u0001\u0000\u0000\u0000\u00a2\u00a3"+
		"\u0005\u001a\u0000\u0000\u00a3\u00a4\u0003.\u0017\u0000\u00a4\u00a6\u0005"+
		"\u001b\u0000\u0000\u00a5\u00a7\u0003\u001c\u000e\u0000\u00a6\u00a5\u0001"+
		"\u0000\u0000\u0000\u00a6\u00a7\u0001\u0000\u0000\u0000\u00a7\u0017\u0001"+
		"\u0000\u0000\u0000\u00a8\u00aa\u00030\u0018\u0000\u00a9\u00ab\u0003\u001a"+
		"\r\u0000\u00aa\u00a9\u0001\u0000\u0000\u0000\u00aa\u00ab\u0001\u0000\u0000"+
		"\u0000\u00ab\u00ad\u0001\u0000\u0000\u0000\u00ac\u00ae\u0003\u001c\u000e"+
		"\u0000\u00ad\u00ac\u0001\u0000\u0000\u0000\u00ad\u00ae\u0001\u0000\u0000"+
		"\u0000\u00ae\u00af\u0001\u0000\u0000\u0000\u00af\u00b0\u0006\f\uffff\uffff"+
		"\u0000\u00b0\u0019\u0001\u0000\u0000\u0000\u00b1\u00b2\u0005\f\u0000\u0000"+
		"\u00b2\u001b\u0001\u0000\u0000\u0000\u00b3\u00b4\u0005\r\u0000\u0000\u00b4"+
		"\u00b5\u0003(\u0014\u0000\u00b5\u00b6\u0005\u000e\u0000\u0000\u00b6\u001d"+
		"\u0001\u0000\u0000\u0000\u00b7\u00b8\u0003 \u0010\u0000\u00b8\u00b9\u0003"+
		":\u001d\u0000\u00b9\u00bc\u0003 \u0010\u0000\u00ba\u00bb\u0005\u0015\u0000"+
		"\u0000\u00bb\u00bd\u00032\u0019\u0000\u00bc\u00ba\u0001\u0000\u0000\u0000"+
		"\u00bc\u00bd\u0001\u0000\u0000\u0000\u00bd\u00bf\u0001\u0000\u0000\u0000"+
		"\u00be\u00c0\u00036\u001b\u0000\u00bf\u00be\u0001\u0000\u0000\u0000\u00bf"+
		"\u00c0\u0001\u0000\u0000\u0000\u00c0\u00c2\u0001\u0000\u0000\u0000\u00c1"+
		"\u00c3\u0003\u001c\u000e\u0000\u00c2\u00c1\u0001\u0000\u0000\u0000\u00c2"+
		"\u00c3\u0001\u0000\u0000\u0000\u00c3\u00c4\u0001\u0000\u0000\u0000\u00c4"+
		"\u00c5\u0006\u000f\uffff\uffff\u0000\u00c5\u001f\u0001\u0000\u0000\u0000"+
		"\u00c6\u00c8\u0003\"\u0011\u0000\u00c7\u00c6\u0001\u0000\u0000\u0000\u00c7"+
		"\u00c8\u0001\u0000\u0000\u0000\u00c8\u00c9\u0001\u0000\u0000\u0000\u00c9"+
		"\u00cb\u0003*\u0015\u0000\u00ca\u00cc\u0003\"\u0011\u0000\u00cb\u00ca"+
		"\u0001\u0000\u0000\u0000\u00cb\u00cc\u0001\u0000\u0000\u0000\u00cc\u00cd"+
		"\u0001\u0000\u0000\u0000\u00cd\u00ce\u0006\u0010\uffff\uffff\u0000\u00ce"+
		"!\u0001\u0000\u0000\u0000\u00cf\u00d0\u0005\u0014\u0000\u0000\u00d0\u00d1"+
		"\u0006\u0011\uffff\uffff\u0000\u00d1#\u0001\u0000\u0000\u0000\u00d2\u00d3"+
		"\u0007\u0000\u0000\u0000\u00d3%\u0001\u0000\u0000\u0000\u00d4\u00d5\u0005"+
		"\u0012\u0000\u0000\u00d5\'\u0001\u0000\u0000\u0000\u00d6\u00d7\u0005%"+
		"\u0000\u0000\u00d7)\u0001\u0000\u0000\u0000\u00d8\u00d9\u0005$\u0000\u0000"+
		"\u00d9\u00da\u0006\u0015\uffff\uffff\u0000\u00da+\u0001\u0000\u0000\u0000"+
		"\u00db\u00dc\u0005$\u0000\u0000\u00dc-\u0001\u0000\u0000\u0000\u00dd\u00e4"+
		"\u0005$\u0000\u0000\u00de\u00e4\u0005\u0018\u0000\u0000\u00df\u00e4\u0005"+
		"\u0019\u0000\u0000\u00e0\u00e4\u0005\u0017\u0000\u0000\u00e1\u00e4\u0005"+
		"%\u0000\u0000\u00e2\u00e4\u00036\u001b\u0000\u00e3\u00dd\u0001\u0000\u0000"+
		"\u0000\u00e3\u00de\u0001\u0000\u0000\u0000\u00e3\u00df\u0001\u0000\u0000"+
		"\u0000\u00e3\u00e0\u0001\u0000\u0000\u0000\u00e3\u00e1\u0001\u0000\u0000"+
		"\u0000\u00e3\u00e2\u0001\u0000\u0000\u0000\u00e4\u00e5\u0001\u0000\u0000"+
		"\u0000\u00e5\u00e3\u0001\u0000\u0000\u0000\u00e5\u00e6\u0001\u0000\u0000"+
		"\u0000\u00e6/\u0001\u0000\u0000\u0000\u00e7\u00e8\u0005$\u0000\u0000\u00e8"+
		"1\u0001\u0000\u0000\u0000\u00e9\u00eb\u0005$\u0000\u0000\u00ea\u00e9\u0001"+
		"\u0000\u0000\u0000\u00eb\u00ec\u0001\u0000\u0000\u0000\u00ec\u00ea\u0001"+
		"\u0000\u0000\u0000\u00ec\u00ed\u0001\u0000\u0000\u0000\u00ed3\u0001\u0000"+
		"\u0000\u0000\u00ee\u00ef\u0005$\u0000\u0000\u00ef5\u0001\u0000\u0000\u0000"+
		"\u00f0\u00f1\u0007\u0001\u0000\u0000\u00f1\u00f2\u0006\u001b\uffff\uffff"+
		"\u0000\u00f27\u0001\u0000\u0000\u0000\u00f3\u00f4\u0005%\u0000\u0000\u00f4"+
		"9\u0001\u0000\u0000\u0000\u00f5\u00f6\u0005\u0013\u0000\u0000\u00f6;\u0001"+
		"\u0000\u0000\u0000\u00f7\u00f8\u0003\u0000\u0000\u0000\u00f8=\u0001\u0000"+
		"\u0000\u0000\u0018FHVfpxz\u0086\u0089\u008f\u0092\u0098\u009a\u00a6\u00aa"+
		"\u00ad\u00bc\u00bf\u00c2\u00c7\u00cb\u00e3\u00e5\u00ec";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}

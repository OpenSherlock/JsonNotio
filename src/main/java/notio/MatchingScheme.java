package notio;

    /**
     * A class used to specify how matching of graph elements should be performed.
     * When matching graphs or graph components, a MatchingScheme instance is used to
     * describe exactly how matching should be performed.  Not all matching schemes need be
     * implemented by a given implementation and many schemes would not make sense.  The
     * exact behaviour of an implementation under these circumstances is undefined but it
     * is recommended that the implementation throw notio.UnimplementedFeatureException
     * either when an invalid scheme is constructed or when it is used in a matching method.
     * MatchingScheme instances may be reused as they are not altered by the matching process.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.33 $, $Date: 1999/05/18 03:03:10 $
		 * @legal Copyright (c) Finnegan Southey, 1996-1999
		 *	This program is free software; you can redistribute it and/or modify it 
		 *	under the terms of the GNU Library General Public License as published 
		 *	by the Free Software Foundation; either version 2 of the License, or 
		 *	(at your option) any later version.  This program is distributed in the 
		 *	hope that it will be useful, but WITHOUT ANY WARRANTY; without even the 
		 *	implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
		 *	See the GNU Library General Public License for more details.  You should 
		 *	have received a copy of the GNU Library General Public License along 
		 *	with this program; if not, write to the Free Software Foundation, Inc., 
		 *	675 Mass Ave, Cambridge, MA 02139, USA.
     * @see notio.UnimplementedFeatureException
     *
     * @idea RN_MATCH_QUANTIFIER?
     * @idea Unordered arc matching?
     * @bug Coreference matching should be better structured.  Currently automatch means that two
     * concepts will automatically match if they are coreferent.  Coref agreement mean
     * that when matching two concepts, all of their coreferent concepts must match as well.
     * Should also be usable
     * to test that two graphs possess identical coreference structure.  Perhaps
     * the current meaning should be a set of CN_ flags or a new set of REFERENT flags
     * should be added.  Then the COREF_ flag could take a meaning similar to the ARC_
     * flag (i.e a requirement for identical structure).
     * @bug ARC flags and CONN flags may conflict in some ways.
     * @idea Possibly should have RN_/CN_MATCH_OFF in order to specify that those nodes
     * should not be considered during matching.  Only makes sense with CONN_MATCH_OFF.
     * @bug Instance matching is too implementation specific.  It is based on the assumption
     * that two calls to Graph.getConcepts(), etc. will result in the same set of objects.
     * This need not be the case.  We should probably override the .equals() method in 
     * various classes to detect whether two different objects in fact represent the same
     * node within a given graph (compare graphs and place in graph, if no graph, use 
     * Object.equals()).
     * @idea QF_MATCH_QUANTITY - referents must refer to the same quantity (e.g. 1 == 1, 
     * 3 == 3, every == every).
     * @idea QF_MATCH_SUBSET - first referent must match some subset of the second referent.
     * Thus, if the first quantifier is larger than the second, they will never be matched
     * by this flag.  Unclear how this should be dealt with two universal quantifiers.
     */
public class MatchingScheme
  {
  /** Graph matching control flag: arguments must be the same Graph instance.  **/
  public final static int GR_MATCH_INSTANCE = 0;
  /** Graph matching control flag: arguments completely match.  **/
  public final static int GR_MATCH_COMPLETE = 1;
  /** Graph matching control flag: first argument must be a subgraph of the second.  **/
  public final static int GR_MATCH_SUBGRAPH = 2;
  /** Graph matching control flag: first argument must be a subgraph of the second.  **/
  public final static int GR_MATCH_PROPER_SUBGRAPH = 3;
  /** Graph matching control flag: one argument must be a subgraph of the other.  **/
  public final static int GR_MATCH_EITHER_SUBGRAPH = 4;
  /** Graph matching control flag: one argument must be a proper subgraph of the other.  **/
  public final static int GR_MATCH_EITHER_PROPER_SUBGRAPH = 5;
  /** Graph matching control flag: graphs must share one or more common subgraphs.  **/
  public final static int GR_MATCH_COMMON_SUBGRAPH = 6;
  /** Graph matching control flag: arguments will always match.  **/
  public final static int GR_MATCH_ANYTHING = 7;

  /** Concept matching control flag: arguments must be the same Concept instance.  **/
  public final static int CN_MATCH_INSTANCE = 10;
  /** Concept matching control flag: arguments will be matched according to the concept type match flag.  **/
  public final static int CN_MATCH_TYPES = 11;
  /** Concept matching control flag: arguments will be matched according to the coreference, designator and quantifier match flags.  **/
  public final static int CN_MATCH_REFERENTS = 12;
  /** Concept matching control flag: arguments will be matched using coreference flag only.  **/
  public final static int CN_MATCH_COREFERENTS = 13;
  /** Concept matching control flag: arguments will be matched using coreference, quantifier, designator, and type flags.  **/
  public final static int CN_MATCH_ALL = 14;
  /** Concept matching control flag: arguments will always match.  **/
  public final static int CN_MATCH_ANYTHING = 15;

  /** Relation matching control flag: arguments must be the same Relation instance.  **/
  public final static int RN_MATCH_INSTANCE = 20;
  /** Relation matching control flag: arguments will be matched according to the relation type match flag.  **/
  public final static int RN_MATCH_TYPES = 21;
  /** Relation matching control flag: arguments will be matched according to the arc match flag.  **/
  public final static int RN_MATCH_ARCS = 22;
  /** Relation matching control flag: arguments will be matched according to all other match flags.  **/
  public final static int RN_MATCH_ALL = 23;
  /** Relation matching control flag: arguments will always match.  **/
  public final static int RN_MATCH_ANYTHING = 24;

  /** ConceptType matching control flag: arguments must be the same ConceptType instance.  **/
  public final static int CT_MATCH_INSTANCE = 30;
  /** ConceptType matching control flag: arguments must have the exact same type label.  **/
  public final static int CT_MATCH_LABEL = 31;
  /** ConceptType matching control flag: first argument must be a subtype of the second.  **/
  public final static int CT_MATCH_SUBTYPE = 32;
  /** ConceptType matching control flag: first argument must be a supertype of the second.  **/
  public final static int CT_MATCH_SUPERTYPE = 33;
  /** ConceptType matching control flag: arguments must have a sub/supertype relationship.  **/
  public final static int CT_MATCH_EQUIVALENT = 34;
  /** ConceptType matching control flag: arguments will always match.  **/
  public final static int CT_MATCH_ANYTHING = 35;

  /** RelationType matching control flag: arguments must be the same RelationType instance.  **/
  public final static int RT_MATCH_INSTANCE = 40;
  /** RelationType matching control flag: arguments must have the exact same type label.  **/
  public final static int RT_MATCH_LABEL = 41;
  /** RelationType matching control flag: first argument must be a subtype of the second.  **/
  public final static int RT_MATCH_SUBTYPE = 42;
  /** RelationType matching control flag: first argument must be a supertype of the second.  **/
  public final static int RT_MATCH_SUPERTYPE = 43;
  /** RelationType matching control flag: arguments must have a sub/supertype relationship.  **/
  public final static int RT_MATCH_EQUIVALENT = 44;
  /** RelationType matching control flag: arguments will always match.  **/
  public final static int RT_MATCH_ANYTHING = 45;

  /** Quantifier matching control flag: arguments will always match.  **/
  public final static int QF_MATCH_ANYTHING = 50;

  /** Designator matching control flag: arguments must be the same Designator instance.  **/
  public final static int DG_MATCH_INSTANCE = 60;
  /** Designator matching control flag: arguments refer to the same individual.  **/
  public final static int DG_MATCH_INDIVIDUAL = 61;
  /** Designator matching control flag: arguments may have a generic/specific relationship.  **/
  public final static int DG_MATCH_EQUIVALENT = 62;
  /** Designator matching control flag: the first argument may be a restriction of the second.  **/
  public final static int DG_MATCH_RESTRICTION = 63;
  /** Designator matching control flag: the first argument may be a generalization of the second.  **/
  public final static int DG_MATCH_GENERALIZATION = 64;
  /** Designator matching control flag: the first argument must be a restriction of the second.  **/
  public final static int DG_MATCH_PROPER_RESTRICTION = 65;
  /** Designator matching control flag: the first argument must be a generalization of the second.  **/
  public final static int DG_MATCH_PROPER_GENERALIZATION = 66;
  /** Designator matching control flag: arguments will always match.  **/
  public final static int DG_MATCH_ANYTHING = 67;

  /** Arc matching control flag: arguments must be same Concept instances.  **/
  public final static int ARC_MATCH_INSTANCE = 70;
  /** Arc matching control flag: arguments will be matched according to scheme's concept flag.  **/
  public final static int ARC_MATCH_CONCEPT = 71;
  /** Arc matching control flag: arguments must have the same number of arcs.  **/
  public final static int ARC_MATCH_VALENCE = 72;
  /** Arc matching control flag: arguments will always match.  **/
  public final static int ARC_MATCH_ANYTHING = 73;

  /** Coreference matching control flag: coreferent concepts will be treated as normal concepts.  **/
  public final static int COREF_AUTOMATCH_OFF = 80;
  /** Coreference matching control flag: coreferent concepts will match regardless of types and referents.  **/
  public final static int COREF_AUTOMATCH_ON = 81;

  /** Coreference matching control flag: when matching a concept, all coreferent concepts must match as well.  **/
  public final static int COREF_AGREE_OFF = 90;
  /** Coreference matching control flag: when matching a concept, coreferent concepts will not be considered.  **/
  public final static int COREF_AGREE_ON = 91;

  /** Folding matching control flag: folds will not be matched.  **/
  public final static int FOLD_MATCH_OFF = 100;
  /** Folding matching control flag: folds will be matched.  **/
  public final static int FOLD_MATCH_ON = 101;

  /** Connected graph matching control flag: disconnected matches are allowed.  **/
  public final static int CONN_MATCH_OFF = 110;
  /** Connected graph matching control flag: disconnected matches are not allowed.  **/
  public final static int CONN_MATCH_ON = 111;
  
  /** Marker matching control flag: markers match only if their ID's are the same. **/
  public final static int MARKER_MATCH_ID = 120;
  /** Marker matching control flag: markers are matched using the marker comparator 
      specified in this scheme. **/
  public final static int MARKER_MATCH_COMPARATOR = 121;
  /** Marker matching control flag: markers always match. **/
  public final static int MARKER_MATCH_ANYTHING = 122;

  /** The flag for matching graphs. **/
  private int graphFlag;
  /** The flag for matching concepts. **/
  private int conceptFlag;
  /** The flag for matching relations. **/
  private int relationFlag;
  /** The flag for matching concept types. **/
  private int conceptTypeFlag;
  /** The flag for matching relation types. **/
  private int relationTypeFlag;
  /** The flag for matching quantifiers. **/
  private int quantifierFlag;
  /** The flag for matching designators. **/
  private int designatorFlag;
  /** The flag for matching markers. **/
  private int markerFlag;
  /** The flag for matching arcs. **/
  private int arcFlag;
  /** The flag for automatching matching coreference links. **/
  private int corefAutoMatchFlag;
  /** The flag for forcing agreement with coreferent concepts. **/
  private int corefAgreementFlag;
  /** The flag for matching with folding. **/
  private int foldingFlag;
  /** The flag for matching with unconnected graphs. **/
  private int connectedFlag;
  /** The number of maximum number of graph matches to generated in the current context. **/
  private int maxMatches;
  /** The nested matching scheme. **/
  private MatchingScheme nestedScheme;
  /** The optional MarkerComparator. **/
  private MarkerComparator markerComparator;

    /**
     * Constructs a matching scheme with the specified control flags.
     *
     * @param newGraphFlag  Matching flag for graphs.
     * @param newConceptFlag  Matching flag for concepts.
     * @param newRelationFlag  Matching flag for relations.
     * @param newConceptTypeFlag  Matching flag for concept types.
     * @param newRelationTypeFlag  Matching flag for relation types.
     * @param newQuantifierFlag  Matching flag for quantifiers.
     * @param newDesignatorFlag  Matching flag for designators.
     * @param newMarkerFlag  Matching flag for markers.
     * @param newArcFlag  Matching flag for arcs.
     * @param newCorefAutoMatchFlag  Flag for automatching coreference concepts.
     * @param newCorefAgreementFlag  Flag for forcing agreement with corefernt concepts.
     * @param newFoldingFlag  Matching flag for folding.
     * @param newConnectedFlag  Matching flag for connected graphs.
     * @param newMaxMatches  The maximum number of graph matches to generate in this context.
     * A zero indicates that all possible matches should be generated.
     * @param newMarkerComparator a MarkerComparator to be used for matching Markers 
     * (must be null if the marker matching flag does not call for a comparator).
     * @param newNestedScheme  A nested matching scheme to be used
     * for matching nested graphs (null means use present scheme).
     */
  public MatchingScheme(int newGraphFlag, 
  	int newConceptFlag, int newRelationFlag,
  	int newConceptTypeFlag, int newRelationTypeFlag,
  	int newQuantifierFlag, int newDesignatorFlag,
  	int newMarkerFlag,
  	int newArcFlag, int newCorefAutoMatchFlag, 
  	int newCorefAgreementFlag, int newFoldingFlag,
  	int newConnectedFlag, 
  	int newMaxMatches, 
  	MarkerComparator newMarkerComparator,
  	MatchingScheme newNestedScheme)
    {
    if ((newGraphFlag < GR_MATCH_INSTANCE) || (newGraphFlag > GR_MATCH_ANYTHING))
    	throw new IllegalArgumentException("Invalid graph matching flag: " + newGraphFlag);

    if ((newConceptFlag < CN_MATCH_INSTANCE) || (newConceptFlag > CN_MATCH_ANYTHING))
    	throw new IllegalArgumentException("Invalid concept matching flag: " + newConceptFlag);

    if ((newRelationFlag < RN_MATCH_INSTANCE) || (newRelationFlag > RN_MATCH_ANYTHING))
    	throw new IllegalArgumentException("Invalid relation matching flag: " + newRelationFlag);

    if ((newConceptTypeFlag < CT_MATCH_INSTANCE) || (newConceptTypeFlag > CT_MATCH_ANYTHING))
    	throw new IllegalArgumentException("Invalid concept type matching flag: " + newConceptTypeFlag);

    if ((newRelationTypeFlag < RT_MATCH_INSTANCE) || (newRelationTypeFlag > RT_MATCH_ANYTHING))
    	throw new IllegalArgumentException("Invalid relation type matching flag: " + newRelationTypeFlag);

    if ((newQuantifierFlag < QF_MATCH_ANYTHING) || (newQuantifierFlag > QF_MATCH_ANYTHING))
    	throw new IllegalArgumentException("Invalid quantifier matching flag: " + newQuantifierFlag);

    if ((newDesignatorFlag < DG_MATCH_INSTANCE) || (newDesignatorFlag > DG_MATCH_ANYTHING))
    	throw new IllegalArgumentException("Invalid designator matching flag: " + newDesignatorFlag);

    if ((newMarkerFlag < MARKER_MATCH_ID) || (newMarkerFlag > MARKER_MATCH_ANYTHING))
    	throw new IllegalArgumentException("Invalid marker matching flag: " + newMarkerFlag);

    if ((newArcFlag < ARC_MATCH_INSTANCE) || (newArcFlag > ARC_MATCH_ANYTHING))
    	throw new IllegalArgumentException("Invalid arc matching flag: " + newArcFlag);

    if ((newCorefAutoMatchFlag < COREF_AUTOMATCH_OFF) || (newCorefAutoMatchFlag > COREF_AUTOMATCH_ON))
    	throw new IllegalArgumentException("Invalid coreference auto-matching flag: " + newCorefAutoMatchFlag);

    if ((newCorefAgreementFlag < COREF_AGREE_OFF) || (newCorefAgreementFlag > COREF_AGREE_ON))
    	throw new IllegalArgumentException("Invalid coreference agreement flag: " + newCorefAgreementFlag);

    if ((newFoldingFlag < FOLD_MATCH_OFF) || (newFoldingFlag > FOLD_MATCH_ON))
    	throw new IllegalArgumentException("Invalid fold matching flag: " + newFoldingFlag);

    if ((newConnectedFlag < CONN_MATCH_OFF) || (newConnectedFlag > CONN_MATCH_ON))
    	throw new IllegalArgumentException("Invalid connected matching flag: " + newConnectedFlag);

    if (newMaxMatches < 0)
    	throw new IllegalArgumentException("Maximum number of matches must be zero or greater: " + newMaxMatches);

		// Check for null marker comparator and MARKER_MATCH_COMPARATOR
		if ((newMarkerComparator == null) && (newMarkerFlag == MARKER_MATCH_COMPARATOR))
    	throw new IllegalArgumentException("A non-null MarkerComparator must be specified if the MARKER_MATCH_COMPARATOR is used.");

		// Check for non-null marker comparator and non-MARKER_MATCH_COMPARATOR
		if ((newMarkerComparator != null) && (newMarkerFlag != MARKER_MATCH_COMPARATOR))
    	throw new IllegalArgumentException("A null MarkerComparator must be specified if the MARKER_MATCH_COMPARATOR is not used.");


    graphFlag = newGraphFlag;
    conceptFlag = newConceptFlag;
    relationFlag = newRelationFlag;
    conceptTypeFlag = newConceptTypeFlag;
    relationTypeFlag = newRelationTypeFlag;
    quantifierFlag = newQuantifierFlag;
    designatorFlag = newDesignatorFlag;
    markerFlag = newMarkerFlag;
    arcFlag = newArcFlag;
    corefAutoMatchFlag = newCorefAutoMatchFlag;
    corefAgreementFlag = newCorefAgreementFlag;
    foldingFlag = newFoldingFlag;
    connectedFlag = newConnectedFlag;

    maxMatches = newMaxMatches;

    nestedScheme = newNestedScheme;

    markerComparator = newMarkerComparator;
    }

    /**
     * Returns the graph matching control flag for this scheme.
     *
     * @return the graph matching control flag for this scheme.
     */
  public int getGraphFlag()
    {
    return graphFlag;
    }

    /**
     * Returns the concept matching control flag for this scheme.
     *
     * @return the concept matching control flag for this scheme.
     */
  public int getConceptFlag()
    {
    return conceptFlag;
    }

    /**
     * Returns the relation matching control flag for this scheme.
     *
     * @return the relation matching control flag for this scheme.
     */
  public int getRelationFlag()
    {
    return relationFlag;
    }

    /**
     * Returns the concept type matching control flag for this scheme.
     *
     * @return the concept type matching control flag for this scheme.
     */
  public int getConceptTypeFlag()
    {
    return conceptTypeFlag;
    }

    /**
     * Returns the relation type matching control flag for this scheme.
     *
     * @return the relation type matching control flag for this scheme.
     */
  public int getRelationTypeFlag()
    {
    return relationTypeFlag;
    }

    /**
     * Returns the quantifier matching control flag for this scheme.
     *
     * @return the quantifier matching control flag for this scheme.
     */
  public int getQuantifierFlag()
    {
    return quantifierFlag;
    }

    /**
     * Returns the designator matching control flag for this scheme.
     *
     * @return the designator matching control flag for this scheme.
     */
  public int getDesignatorFlag()
    {
    return designatorFlag;
    }

    /**
     * Returns the arc matching control flag for this scheme.
     *
     * @return the arc matching control flag for this scheme.
     */
  public int getArcFlag()
    {
    return arcFlag;
    }

    /**
     * Returns the marker matching control flag for this scheme.
     *
     * @return the marker matching control flag for this scheme.
     */
  public int getMarkerFlag()
    {
    return markerFlag;
    }

    /**
     * Returns the coreference auto-matching control flag for this scheme.
     *
     * @return the coreference auto-matching control flag for this scheme.
     */
  public int getCoreferenceAutoMatchFlag()
    {
    return corefAutoMatchFlag;
    }

    /**
     * Returns the coreference agreement control flag for this scheme.
     *
     * @return the coreference agreement control flag for this scheme.
     */
  public int getCoreferenceAgreementFlag()
    {
    return corefAgreementFlag;
    }

    /**
     * Returns the folding matching control flag for this scheme.
     *
     * @return the folding matching control flag for this scheme.
     */
  public int getFoldingFlag()
    {
    return foldingFlag;
    }

    /**
     * Returns the connected graph matching control flag for this scheme.
     *
     * @return the connected graph matching control flag for this scheme.
     */
  public int getConnectedFlag()
    {
    return connectedFlag;
    }

    /**
     * Returns the maximum number of graph matches to be generated this scheme.
     *
     * @return the maximum number of graph matches to be generated this scheme.
     */
  public int getMaxMatches()
    {
    return maxMatches;
    }

    /**
     * Returns the nested matching scheme or null.
     * The nested matching scheme is used for matching nested graphs.
     * If it is set to null, the current scheme is used.
     *
     * @return the nested matching scheme or null.
     */
  public MatchingScheme getNestedMatchingScheme()
    {
    return nestedScheme;
    }
    
    /**
     * Returns the MarkerComparator to be used in this scheme, if any.
     *
     * @return the MarkerComparator to be used in this scheme, if any.
     */
  public MarkerComparator getMarkerComparator()
    {
    return markerComparator;
    }
  }

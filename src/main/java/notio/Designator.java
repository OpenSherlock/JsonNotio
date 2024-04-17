package notio;

import java.util.Hashtable;
import java.io.Serializable;

    /**
     * Base class for designators.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.44 $, $Date: 1999/09/10 00:25:12 $
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
		 *
     * @bug Must rework all matching-related comments.
     */
abstract public class Designator implements Serializable
  {
  	/** Indicates that a designator is a LiteralDesignator. **/
  public final static int DESIGNATOR_LITERAL = 0;
  
  	/** Indicates that a designator is a LocatorDesignator. **/
  public final static int DESIGNATOR_MARKER = 1;
  
  	/** Indicates that a designator is a DefinedDesignator. **/
  public final static int DESIGNATOR_DEFINED = 2;
  
	  /** Indicates that a designator is a NameDesignator. **/
  public final static int DESIGNATOR_NAME = 3;


	  /** Flag indicating whether labels (e.g. names) are case sensitive or not. **/
  private boolean caseSensitiveLabels = false;
  
	  /** The referent to which this designator belongs. **/
  private Referent enclosingReferent = null;

    /**
     * Returns a constant indicating which kind of designator this is.
     * The constant is one of:
     * Designator.DESIGNATOR_LITERAL
     * Designator.DESIGNATOR_MARKER
     * Designator.DESIGNATOR_DEFINED
     * Designator.DESIGNATOR_NAME
     *
     * @return a constant indicating the kind of the designator.
     */
  abstract public int getDesignatorKind();

  	/**
     * Performs a copy operation on this designator according to the
     * the specified CopyingScheme.
     * The result may be a new designator or simply a reference to this designator
     * depending on the scheme.
  	 *
  	 * @param copyScheme  the copying scheme used to control the copy operation.
  	 * @param substitutionTable  a hashtable containing copied objects available due to 
     * earlier copy operations.
  	 * @return the result of the copy operation.
  	 */
  abstract public Designator copy(CopyingScheme copyScheme, Hashtable substitutionTable);

    /**
     * Sets a flag indicating whether or not the processing of labels within this
     * designator is case-sensitive.
     *
     * @param flag  the flag setting for case-sensitivity.
     */
  public void setCaseSensitiveLabels(boolean flag)
  	{
  	caseSensitiveLabels = flag;
  	}

    /**
     * Returns true if the processing of labels in this designator is case-sensitive.
     *
     * @return true if the processing of labels in this designator is case-sensitive.
     */
  public boolean getCaseSensitiveLabels()
  	{
  	return caseSensitiveLabels;
  	}
  	
  	/**
  	 * Sets the enclosing referent for this designator.
  	 *
  	 * @param newReferent  the enclosing referent for this designator.
  	 */
  void setEnclosingReferent(Referent newReferent)
  	{
  	enclosingReferent = newReferent;
  	}
  	
  	/**
  	 * Returns the enclosing referent for this designator or null if there isn't one.
  	 *
  	 * @return the enclosing referent for this designator or null.
  	 */
  public Referent getEnclosingReferent()
  	{
  	return enclosingReferent;
  	}

		/**
		 * Matches two markers according to the specified matching scheme.
		 *
		 * @param firstMarker  the first of two markers being compared.
		 * @param secondMarker  the second of two markers being compared.
		 * @param matchingScheme  the matching scheme used in matching.
		 * @return true if the two markers match according to the matching scheme.
		 */
  private static boolean matchMarker(Marker firstMarker, Marker secondMarker, 
  	MatchingScheme matchingScheme)
  	{
		switch (matchingScheme.getMarkerFlag())
			{
			case MatchingScheme.MARKER_MATCH_ID:
				{
				return firstMarker.getMarkerID().equals(secondMarker.getMarkerID());
				}
				
			case MatchingScheme.MARKER_MATCH_COMPARATOR:
				{
				return matchingScheme.getMarkerComparator().compareMarkers(firstMarker, secondMarker);
				}

			case MatchingScheme.MARKER_MATCH_ANYTHING:
				{
				return true;
				}
				
			default:
        throw new UnimplementedFeatureException("Specified Marker match control flag is unknown.");				
			}
  	}
  	
    /**
     * Matches a literal designator against some other designator according to the specified
     * matching scheme.
     *
     * @param subject  the first designator being matched.
     * @param object  the second designator being matched.
     * @param matchingScheme  the matching scheme used in matching.
     * @return true if the designators match.
     */
  static MatchResult matchLiteralDesignator(LiteralDesignator subject, Designator object,
    MatchingScheme matchingScheme)
    {
    // Equivalent
    switch (object.getDesignatorKind())
      {
      case Designator.DESIGNATOR_LITERAL:
        return new MatchResult(matchMarker(subject.getMarker(), ((LiteralDesignator)object).getMarker(), matchingScheme));

      case Designator.DESIGNATOR_MARKER:
        return new MatchResult(matchMarker(subject.getMarker(), ((MarkerDesignator)object).getMarker(), matchingScheme));

      case Designator.DESIGNATOR_NAME:
      case Designator.DESIGNATOR_DEFINED:
        return new MatchResult(false);

      default:
        throw new UnimplementedFeatureException("Unknown designator type in matching.");
      }
    }

    /**
     * Matches a marker designator against some other designator according to the specified
     * matching scheme.
     *
     * @param subject  the first designator being matched.
     * @param object  the second designator being matched.
     * @param matchingScheme  the matching scheme used in matching.
     * @return true if the designators match.
     */
  static MatchResult matchMarkerDesignator(MarkerDesignator subject, Designator object,
    MatchingScheme matchingScheme)
    {
    // Equivalent
    switch (object.getDesignatorKind())
      {
      case Designator.DESIGNATOR_LITERAL:
        return new MatchResult(matchMarker(subject.getMarker(), ((LiteralDesignator)object).getMarker(), matchingScheme));

      case Designator.DESIGNATOR_MARKER:
        return new MatchResult(matchMarker(subject.getMarker(), ((MarkerDesignator)object).getMarker(), matchingScheme));

      case Designator.DESIGNATOR_NAME:
      case Designator.DESIGNATOR_DEFINED:
        return new MatchResult(false);

      default:
        throw new UnimplementedFeatureException("Unknown designator type in matching.");
      }
    }

    /**
     * Matches a name designator against some other designator according to the specified
     * matching scheme.
     *
     * @param subject  the first designator being matched.
     * @param object  the second designator being matched.
     * @param matchingScheme  the matching scheme used in matching.
     * @return true if the designators match.
     * @bug Should case-sensitivity be a static instead?
     */
  static MatchResult matchNameDesignator(NameDesignator subject, Designator object,
    MatchingScheme matchingScheme)
    {
    // Equivalent
    switch (object.getDesignatorKind())
      {
      case Designator.DESIGNATOR_LITERAL:
      case Designator.DESIGNATOR_MARKER:
        return new MatchResult(false);

      case Designator.DESIGNATOR_NAME:
      	{
      	if (subject.getCaseSensitiveLabels())     	
	        return new MatchResult(subject.getName().equals( ((NameDesignator)object).getName()) );
        else
	        return new MatchResult((subject.getName().toLowerCase()).equals(((NameDesignator)object).getName().toLowerCase()) );
	      }

      case Designator.DESIGNATOR_DEFINED:
        return new MatchResult(false);

      default:
        throw new UnimplementedFeatureException("Unknown designator type in matching.");
      }
    }


    /**
     * Matches a defined designator against some other designator according to the specified
     * matching scheme.
     *
     * @param subject  the first designator being matched.
     * @param object  the second designator being matched.
     * @param matchingScheme  the matching scheme used in matching.
     * @return true if the designators match.
     */
  static MatchResult matchDefinedDesignator(DefinedDesignator subject, Designator object,
    MatchingScheme matchingScheme)
    {
    // Equivalent
    switch (object.getDesignatorKind())
      {
      case Designator.DESIGNATOR_LITERAL:
      case Designator.DESIGNATOR_MARKER:
      case Designator.DESIGNATOR_NAME:
      case Designator.DESIGNATOR_DEFINED:
        return new MatchResult(false);

      default:
        throw new UnimplementedFeatureException("Unknown designator type in matching.");
      }
    }


    /**
     * Returns true if the subject Designator is identical to the object
     * designator.
     * Designators are regarded as identical if they both resolve to the same
     * individual (or individual marker).
     * (e.g. a literal and a locator that points to the same object are
     *  considered identical).
     * If the designators can not be resolved to the same individual, or if
     * either of the designators can not be resolved to any individual, then
     * this method will return false.
     *
     * @param subject  the subject designator.
     * @param object  the object designator.
     * @param matchingScheme  the matching scheme.
     * @return true if the two designators are identical.
     *
     * @bug What about LambdaDesignators?
     * @bug Compare sets.
     */
  static MatchResult isEquivalentTo(Designator subject, Designator object,
    MatchingScheme matchingScheme)
    {
    if (object == subject)
      return new MatchResult(true);

    switch (subject.getDesignatorKind())
      {
      case Designator.DESIGNATOR_LITERAL:
        return matchLiteralDesignator((LiteralDesignator)subject, object, matchingScheme);

      case Designator.DESIGNATOR_MARKER:
        return matchMarkerDesignator((MarkerDesignator)subject, object, matchingScheme);

      case Designator.DESIGNATOR_NAME:
        return matchNameDesignator((NameDesignator)subject, object, matchingScheme);

      case Designator.DESIGNATOR_DEFINED:
        return matchDefinedDesignator((DefinedDesignator)subject, object, matchingScheme);

      default:
        throw new UnimplementedFeatureException("Unknown designator type in matching.");
      }
    }

    /**
     * Compares two designators to decide if they match.  The exact semantics of matching
     * are determined by the match control flag.
     *
     * @param first  the first designators being matched.
     * @param second  the second designators being matched.
     * @param matchingScheme  the matching scheme that determines how the match is performed.
     * @return true if the two designators match according to the scheme's criteria.
     * @bug What about restriction matching against unbound lambdas?
     * @bug Does the restriction/generalization/equivalent terminology makes sense here?
     * Equivalent doesn't really seem to be correct here.
     * @bug MATCH_INDIVIDUAL can be applied to designators other than MARKER (eg. LITERAL).
     */
  public static MatchResult matchDesignators(Designator first, Designator second,
    MatchingScheme matchingScheme)
    {
    switch (matchingScheme.getDesignatorFlag())
      {
      case MatchingScheme.DG_MATCH_INSTANCE:
      	{
        return new MatchResult(first == second);
        }

      case MatchingScheme.DG_MATCH_INDIVIDUAL:
      	{
        if (first.getDesignatorKind() != Designator.DESIGNATOR_MARKER)
          return new MatchResult(false);
          
        if (second.getDesignatorKind() != Designator.DESIGNATOR_MARKER)
          return new MatchResult(false);
          
        return new MatchResult(matchMarker(((MarkerDesignator)first).getMarker(), 
        	((MarkerDesignator)second).getMarker(), matchingScheme));
      	}

      case MatchingScheme.DG_MATCH_EQUIVALENT:
      	{
        if (first == null)
          return new MatchResult(true);
        if (second == null)
          return new MatchResult(true);

        return isEquivalentTo(first, second, matchingScheme);
        }

      case MatchingScheme.DG_MATCH_RESTRICTION:
      	{
      	// Arguments must be equivalent or the first specific and the second generic

      	// Arguments can both be generic
      	if ((first == null) && (second == null))
      		return new MatchResult(true);

				// Arguments can both be specific, in which case they must be equivalent
      	if ((first != null) && (second != null))
	        return isEquivalentTo(first, second, matchingScheme);

        // Second argument must be generic
        if (second != null)
          return new MatchResult(false);

        // First argument must be specific
        if (first == null)
          return new MatchResult(false);

        return new MatchResult(true);
      	}

      case MatchingScheme.DG_MATCH_GENERALIZATION:
      	{
      	// Arguments must be equivalent or the first generic and the second specific

      	// Arguments can both be generic
      	if ((first == null) && (second == null))
      		return new MatchResult(true);

				// Arguments can both be specific, in which case they must be equivalent
      	if ((first != null) && (second != null))
	        return isEquivalentTo(first, second, matchingScheme);

				// Otherwise,
        // Second argument must be specific
        if (second == null)
          return new MatchResult(false);

        // First argument must be generic
        if (first != null)
          return new MatchResult(false);

        return new MatchResult(true);
      	}

      case MatchingScheme.DG_MATCH_PROPER_RESTRICTION:
      	{
        // Second argument must be generic
        if (second != null)
          return new MatchResult(false);

        // First argument must be specific
        if (first == null)
          return new MatchResult(false);

        return new MatchResult(true);
        }

      case MatchingScheme.DG_MATCH_PROPER_GENERALIZATION:
      	{
        // Second argument must be specific
        if (second == null)
          return new MatchResult(false);

        // First argument must be generic
        if (first != null)
          return new MatchResult(false);

        return new MatchResult(true);
        }

      case MatchingScheme.DG_MATCH_ANYTHING:
      	{
        return new MatchResult(true);
        }

      default:
        throw new UnimplementedFeatureException("Specified Designator match control flag is unknown.");
      }
    }
  }

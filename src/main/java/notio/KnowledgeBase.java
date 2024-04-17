package notio;

import java.io.Serializable;

    /**
     * A class that defines a knowledge base.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.17 $, $Date: 1999/09/10 00:25:13 $
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
     */
public class KnowledgeBase implements Serializable
  {
  	/** The concept type hierarchy for the knowledge base. **/
  private ConceptTypeHierarchy conceptHierarchy;
  
  	/** The relation type hierarchy for the knowledge base. **/
  private RelationTypeHierarchy relationHierarchy;
  
  	/** The marker set for the knowledge base. **/
  private MarkerSet markerSet;
  
  	/** The outermost context of this knowledge base. **/
  private Concept outermostContext;

    /**
     * Constructs a new knowledge base with the specified components.
     *
     * @param newConceptHierarchy  the concept type hierarchy.
     * @param newRelationHierarchy  the relation type hierarchy.
     * @param newMarkerSet  the marker set.
     * @param newOutermostContext  the outermost context.
     */
  public KnowledgeBase(ConceptTypeHierarchy newConceptHierarchy,
                       RelationTypeHierarchy newRelationHierarchy,
                       MarkerSet newMarkerSet, 
                       Concept newOutermostContext)
    {
    conceptHierarchy = newConceptHierarchy;
    relationHierarchy = newRelationHierarchy;
    markerSet = newMarkerSet;
    outermostContext = newOutermostContext;
    }

    /**
     * Constructs a new knowledge base and creates required components
     * automatically.  
     * Currently, this automatically creates a ConceptType with the
     * label "Proposition" and adds it to the concept type hierarchy 
     * with the universal and absurd types as super- and subtype
     * respectively.  It then creates a Concept with the proposition
     * type, a null (existential) quantifier, and a DescriptorDesignator
     * that references an empty Graph.
     */
  public KnowledgeBase()
    {
    ConceptType propType;
    
		propType = new ConceptType("Proposition");
    conceptHierarchy = new ConceptTypeHierarchy();
    
   	conceptHierarchy.addTypeToHierarchy(propType);
    	
    relationHierarchy = new RelationTypeHierarchy();
    markerSet = new MarkerSet();
    
    outermostContext = new Concept(propType, new Referent(new Graph()));
    }
    
    /**
     * Returns the concept type hierarchy.
     *
     * @return the concept type hierarchy.
     */
  public ConceptTypeHierarchy getConceptTypeHierarchy()
    {
    return conceptHierarchy;
    }

    /**
     * Returns the relation type hierarchy.
     *
     * @return the relation type hierarchy.
     */
  public RelationTypeHierarchy getRelationTypeHierarchy()
    {
    return relationHierarchy;
    }

    /**
     * Returns the marker set.
     *
     * @return the marker set.
     */
  public MarkerSet getMarkerSet()
    {
    return markerSet;
    }

    /**
     * Returns the outermost context.
     *
     * @return the outermost context.
     */
  public Concept getOutermostContext()
    {
    return outermostContext;
    }
  }

package notio;

    /** 
     * A class used to specify how copying of graph elements should be performed.
     * When copying graphs or graph components, a CopyingScheme instance is used to 
     * describe exactly how copying should be performed.  Not all copying schemes need be
     * implemented by a given implementation and many schemes would not make sense.  The
     * exact behaviour of an implementation under these circumstances is undefined but it
     * is recommended that the implementation throw notio.UnimplementedFeatureException
     * either when an invalid scheme is constructed or when it is used in a copying method.
     * CopyingScheme instances may be reused as they are not altered by the copying process.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.10 $, $Date: 1999/05/04 01:35:49 $
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
     */

public class CopyingScheme
  {
  /** Graph copy control flag: a duplicate graph will be created.  **/
  public final static int GR_COPY_DUPLICATE = 0;
  /** Graph copy control flag: a reference to the existing graph will be used.  **/
  public final static int GR_COPY_REFERENCE = 1;
  
  /** Concept copy control flag: a duplicate node will be created.  **/
  public final static int CN_COPY_DUPLICATE = 10;
  /** Concept copy control flag: a reference to the existing node will be used.  **/
  public final static int CN_COPY_REFERENCE = 11;
  
  /** Relation copy control flag: a duplicate node will be created.  **/
  public final static int RN_COPY_DUPLICATE = 20;
  /** Relation copy control flag: a reference to the existing node will be used.  **/
  public final static int RN_COPY_REFERENCE = 21;
  
  /** Designator copy control flag: a duplicate designator will be created.  **/
  public final static int DG_COPY_DUPLICATE = 40;
  /** Designator copy control flag: a reference to the existing designator will be used.  **/
  public final static int DG_COPY_REFERENCE = 41;
    
  /** Comment copy control flag: node and graph comments will not be copied.  **/
  public final static int COMM_COPY_OFF = 50;
  /** Comment copy control flag: node and graph comments will be copied.  **/
  public final static int COMM_COPY_ON = 51;
  
  /** The flag for copying graphs. **/
  private int graphFlag;
  /** The flag for copying concepts. **/
  private int conceptFlag;
  /** The flag for copying relations. **/
  private int relationFlag;
  /** The flag for copying designators. **/
  private int designatorFlag;
  /** The flag for copying comments. **/
  private int commentFlag;
  /** The nested copying scheme. **/
  private CopyingScheme nestedScheme;

    /**
     * Constructs a copying scheme with the specified control flags.
     * 
     * @param newGraphFlag  Copying flag for graphs.
     * @param newConceptFlag  Copying flag for concepts.
     * @param newRelationFlag  Copying flag for relations.
     * @param newDesignatorFlag  Copying flag for designators.
     * @param newCommentFlag  Copying flag for comments.
     * @param newNestedScheme  A nested copying scheme to be used 
     * for copying nested graphs (null means use present scheme).
     */
  public CopyingScheme(int newGraphFlag, int newConceptFlag, int newRelationFlag,
  	int newDesignatorFlag, int newCommentFlag,
  	CopyingScheme newNestedScheme)
    {
    if ((newGraphFlag < GR_COPY_DUPLICATE) || (newGraphFlag > GR_COPY_REFERENCE))
    	throw new IllegalArgumentException("Invalid graph copying flag: " + newGraphFlag);
    	
    if ((newConceptFlag < CN_COPY_DUPLICATE) || (newConceptFlag > CN_COPY_REFERENCE))
    	throw new IllegalArgumentException("Invalid concept copying flag: " + newConceptFlag);
    	
    if ((newRelationFlag < RN_COPY_DUPLICATE) || (newRelationFlag > RN_COPY_REFERENCE))
    	throw new IllegalArgumentException("Invalid relation copying flag: " + newRelationFlag);
    	
    if ((newDesignatorFlag < DG_COPY_DUPLICATE) || (newDesignatorFlag > DG_COPY_REFERENCE))
    	throw new IllegalArgumentException("Invalid designator copying flag: " + newDesignatorFlag);

    if ((newCommentFlag < COMM_COPY_OFF) || (newCommentFlag > COMM_COPY_ON))
    	throw new IllegalArgumentException("Invalid comment copying flag: " + newCommentFlag);

    graphFlag = newGraphFlag;
    conceptFlag = newConceptFlag;
    relationFlag = newRelationFlag;
		designatorFlag = newDesignatorFlag;
		commentFlag = newCommentFlag;
    nestedScheme = newNestedScheme;
    }
    
    /**
     * Returns the graph copying control flag for this scheme.
     *
     * @return the graph copying control flag for this scheme.
     */
  public int getGraphFlag()
    {
    return graphFlag;
    }

    /**
     * Returns the concept copying control flag for this scheme.
     *
     * @return the concept copying control flag for this scheme.
     */
  public int getConceptFlag()
    {
    return conceptFlag;
    }

    /**
     * Returns the relation copying control flag for this scheme.
     *
     * @return the relation copying control flag for this scheme.
     */
  public int getRelationFlag()
    {
    return relationFlag;
    }
    
    /**
     * Returns the designator copying control flag for this scheme.
     *
     * @return the designator copying control flag for this scheme.
     */
  public int getDesignatorFlag()
    {
    return designatorFlag;
    }
    
    /**
     * Returns the comment copying control flag for this scheme.
     *
     * @return the comment copying control flag for this scheme.
     */
  public int getCommentFlag()
    {
    return commentFlag;
    }
    
    /**
     * Returns the nested copying scheme or null.
     * The nested copying scheme is used for copying nested graphs.
     * If it is set to null, the current scheme is used.
     *
     * @return the nested copying scheme or null.
     */
  public CopyingScheme getNestedCopyingScheme()
    {
    return nestedScheme;
    }
  }

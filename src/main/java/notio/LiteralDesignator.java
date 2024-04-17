package notio;

import java.util.Hashtable;
import java.io.Serializable;

    /** 
     * Class for literal designators.  This is used for references to any Java
     * Object or Object sub-class.  This allows for Strings, Integers, Images,
     * or any type of data.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.15 $, $Date: 1999/08/02 08:11:20 $
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

public class LiteralDesignator extends Designator implements Serializable
  {
  	/** Literal object associated with this designator. **/
  private Object literal;
  
  	/** Marker associated with the literal object. **/
  private Marker marker;

    /**
     * Constructs a new LiteralDesignator unassociated with any literal object.
     * A literal may be associated using setLiteral().
     *
     * @see notio.LiteralDesignator#setLiteral
     */
  public LiteralDesignator()
    {
    }

    /**
     * Constructs a new LiteralDesignator with the specified Object.
     *
     * @param newLiteral  the literal object.
     * @param markerSet  the marker set associated with this designator.
     */
  public LiteralDesignator(Object newLiteral, MarkerSet markerSet)
    {
    literal = newLiteral;
    // Lookup marker or create one if necessary.
    establishMarker(markerSet);
    }

    /**
     * Returns a constant indicating which kind of designator this is.
     * In this case the constant will be: Designator.DESIGNATOR_LITERAL
     *
     * @return a constant indicating the kind of the designator.
     */
  public int getDesignatorKind()
    {
    return DESIGNATOR_LITERAL;
    }

    /**
     * Sets the literal object referenced by this designator.  
     * If a literal has already been specified, both the literal and the marker
     * will be replaced by the new values.
     * Calling this method will associate this designator with the marker for
     * the literal object or create one if necessary.
     *
     * @param newLiteral  the literal object to be referenced by this designator.
     * @param markerSet  the marker set associated with this designator.
     * @return the literal object referenced by this designator.
     */
  public void setLiteral(Object newLiteral, MarkerSet markerSet)
    {
    literal = newLiteral;
    establishMarker(markerSet);
    }
    
    /**
     * Returns the literal object referenced by this designator.  This  should
     * be the same as the result returned by a getIndividual() call to the
     * marker associated with this designator.
     *
     * @return the literal object referenced by this designator.
     */
  public Object getLiteral()
    {
    return literal;
    }

    /**
     * Returns the marker associated with this literal designator.
     *
     * @return the marker associated with this literal designator.
     */
  public Marker getMarker()
    {
    return marker;
    }

  	/**
     * Performs a copy operation on this designator according to the
     * the specified CopyingScheme.
     * The result may be a new designator or simply a reference to this designator
     * depending on the scheme.
     * If the a new designator instance is created, the literal is not copied.
  	 *
  	 * @param copyScheme  the copying scheme used to control the copy operation.
     * @param substitutionTable  a hashtable containing copied objects available due to 
     * earlier copy operations.
  	 * @return the result of the copy operation.
  	 */
  public Designator copy(CopyingScheme copyScheme, Hashtable substitutionTable)
  	{
  	switch (copyScheme.getDesignatorFlag())
  		{
  		case CopyingScheme.DG_COPY_DUPLICATE:
  			{
  			LiteralDesignator literalDesignator = null;
  			
  			try
					{
					literalDesignator = (LiteralDesignator)this.getClass().newInstance();
					}
				catch (java.lang.InstantiationException e)
					{
					// This will only occur in a poorly designed subclass
					e.printStackTrace();
					System.exit(1);
					}
				catch (java.lang.IllegalAccessException e)
					{
					// This will only occur in a poorly designed subclass
					e.printStackTrace();
					System.exit(1);
					}
					
  			literalDesignator.setLiteral(getLiteral(), getMarker().getMarkerSet());
  			
  			return literalDesignator;
  			}
  		
  		case CopyingScheme.DG_COPY_REFERENCE:
  			{
  			return this;
  			}
  			
  		default:
        throw new UnimplementedFeatureException("Specified Designator copy control flag is unknown.");    		  		
  		}
  	}
  
  	/**
  	 * Establishes a marker for this designator's literal by querying the marker set or
  	 * creating one.
  	 */
  private void establishMarker(MarkerSet markerSet)
  	{
    // Lookup marker or create one if necessary.
    marker = markerSet.getMarkerByIndividual(literal);
    if (marker == null)
      marker = new Marker(markerSet, literal);
  	}
  }

package notio;

import java.util.Hashtable;
import java.io.Serializable;

    /** 
     * Class for locator designators.  Used for references by individual marker.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.10 $, $Date: 1999/08/02 08:08:41 $
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
public class MarkerDesignator extends Designator implements Serializable
  {
  	/** Marker associated with this designator. **/
  private Marker marker;

    /**
     * Constructs a new MarkerDesignator with no associated marker.
     * A marker may be assigned later using setMarker().
     *
     * @see notio.MarkerDesignator#setMarker
     */
  public MarkerDesignator()
    {
    }
    
    /**
     * Constructs a new MarkerDesignator with the specified marker set.  A marker will be
     * constructed and added to the set automatically.
     *
     * @param markerSet  the marker set associated with this designator.
     */
  public MarkerDesignator(MarkerSet markerSet)
    {
    marker = new Marker(markerSet, null);
    }

    /**
     * Constructs a new MarkerDesignator with the specified marker.  The marker is assumed
     * to be valid and already a member of some marker set.
     *
     * @param newMarker  the marker associated with this designator.
     */
  public MarkerDesignator(Marker newMarker)
    {
    marker = newMarker;
    }

    /**
     * Returns a constant indicating which kind of designator is.
     * In this case the constant will be: Designator.DESIGNATOR_MARKER
     *
     * @return a constant indicating the kind of the designator.
     */
  public int getDesignatorKind()
    {
    return DESIGNATOR_MARKER;
    }

    /**
     * Sets the marker associated with this designator.
     *
     * @param newMarker  the marker to be associated with this designator.
     */
  public void setMarker(Marker newMarker)
    {
    marker = newMarker;
    }
    
    /**
     * Returns the marker associated with this designator.
     *
     * @return the marker associated with this designator.
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
  			MarkerDesignator markerDesignator = null;
  			
  			try
					{
					markerDesignator = (MarkerDesignator)this.getClass().newInstance();
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
					
  			markerDesignator.setMarker(getMarker());
  			
  			return markerDesignator;
  			}
  		
  		case CopyingScheme.DG_COPY_REFERENCE:
  			{
  			return this;
  			}
  			
  		default:
        throw new UnimplementedFeatureException("Specified Designator copy control flag is unknown.");    		  		
  		}
  	}
  }
package notio.translators;

import java.util.*;
import java.io.Serializable;
import notio.*;

    /** 
     * A TranslationInfoUnit that serves as a symbol table relating foreign markers to
     * native markers.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.2 $, $Date: 1999/05/06 04:43:37 $
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
public class MarkerTable extends SimpleInfoUnit
  {
	  /** A lookup table that maps foreign markers to native markers. **/
  private Hashtable foreignIDToNativeMarkerTable;

	  /** A lookup table that maps native markers to foreign markers. **/
  private Hashtable nativeMarkerToForeignIDTable;
  
    /**
     * Constructs a new MarkerTable.
     */
  public MarkerTable()
    {
    foreignIDToNativeMarkerTable = new Hashtable();
    nativeMarkerToForeignIDTable = new Hashtable();
    }

    /**
     * Constructs a new MarkerTable that is a copy of the specified original.
     * Changes to the new table will not affect the old one or vice versa.
     *
     * @param originalContext  the translation context to be copied.
     */
  public MarkerTable(MarkerTable originalTable)
    {
    foreignIDToNativeMarkerTable = copyHashtable(getForeignIDToNativeMarkerTable());
    nativeMarkerToForeignIDTable = copyHashtable(getNativeMarkerToForeignIDTable());
		}

    /**
     * Creates a two-way mapping between a foreign marker ID and a native marker instance.
     * This is used to facilitate marker assignment and lookup while translating.  During 
     * parsing, newly encountered foreign marker ID's can have a corresponding native marker
     * instantiated and a mapping created.  Subsequent references to the foreign ID during
     * during parsing can then use the same native marker.  The table may be used for 
     * generation purposes in order to preserve consistency between parsed and generated
     * markers.
     * This mapping may need to be cleared in some situations.  For example, when parsing two
     * graphs in succession that contain identical foreign marker ID's but whose resulting
     * markers should not be identical internally.  Exactly when the table should be cleared
     * should be decided by the application.
     *
     * @param foreignID  the foreign marker ID being mapped.
     * @param marker  the native marker being mapped.
     */
  public void mapForeignMarkerIDToNativeMarker(String foreignID, Marker marker)
    {
    foreignIDToNativeMarkerTable.put(foreignID, marker);
    nativeMarkerToForeignIDTable.put(marker, foreignID);
    }
    
    /**
     * Returns the native marker associated with the specified foreign marker ID in this 
     * translation context.
     *
     * @param foreignID  the foreign marker ID used for lookup.
     * @return the native marker associated with foreignID, or null if no mapping exists.
     */
  public Marker getNativeMarkerByForeignMarkerID(String foreignID)
    {
    return (Marker)foreignIDToNativeMarkerTable.get(foreignID);
    }

    /**
     * Returns the foreign marker ID associated with the specified native marker in this 
     * translation context.
     *
     * @param marker  the native marker used for lookup.
     * @return the foreign marker ID associated with the marker, or null if no mapping exists.
     */
  public String getForeignMarkerIDByNativeMarker(Marker marker)
    {
    return (String)nativeMarkerToForeignIDTable.get(marker);
    }
    
    /**
     * Clears all existing mappings between foreign marker ID's and native markers.
     */
  public void clearForeignMarkerIDToNativeMarkerMapping()
    {
    foreignIDToNativeMarkerTable.clear();
    nativeMarkerToForeignIDTable.clear();
    }

    /**
     * Returns the foreign ID to native marker table as a hashtable.
     *
     * @return the foreign ID to native marker table as a hashtable.
     */
  Hashtable getForeignIDToNativeMarkerTable()
  	{
  	return foreignIDToNativeMarkerTable;
  	}

    /**
     * Returns the native marker to foreign ID table as a hashtable.
     *
     * @return the native marker to foreign ID table as a hashtable.
     */
  Hashtable getNativeMarkerToForeignIDTable()
  	{
  	return nativeMarkerToForeignIDTable;
  	}

		/**
		 * Creates a deep copy of the specified hashtable.
		 *
		 * @param inTable  the hashtable to be copied.
		 * @return a copy of the hashtable.
		 */
	private Hashtable copyHashtable(Hashtable inTable)
		{
		Enumeration keys, elements;
		Hashtable newTable = new Hashtable();
		
		keys = inTable.keys();
		elements = inTable.elements();
		
		while (keys.hasMoreElements())
			newTable.put(keys.nextElement(), elements.nextElement());
			
		return newTable;
		}
	  /**
  	 * Resets this information unit to its initial state.  This method can be called
  	 * by translators in order to ensure that a unit contains no information relating
  	 * to earlier translation sessions.
  	 */
  public void resetUnit()
  	{
		clearForeignMarkerIDToNativeMarkerMapping();
		}

  	/**
  	 * Returns a duplicate of this information unit that is distinct from the original.
  	 * This means that changes to the original will not affect the duplicate and vice
  	 * versa.
  	 *
  	 * @return a duplicate of this unit.
  	 */
  public TranslationInfoUnit copyUnit()
  	{
  	return new MarkerTable(this);
  	}
  }

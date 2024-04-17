package notio;

import java.util.Hashtable;
import java.io.Serializable;

    /**
     * Class for name designators.  Used for references by name.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.8 $, $Date: 1999/08/02 01:34:49 $
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
     * @bug This class is really superfluous unless we want special treatment for names which
     * should really just be relations.
     * @idea NameMatchers like MarkerMatchers?
     */

public class NameDesignator extends Designator implements Serializable
  {
	  /** Name which is used as the locator in this designator. **/
  private String name;

    /**
     * Constructs a new NameDesignator unassociated with any name.
     */
  public NameDesignator()
    {
    }

    /**
     * Constructs a new NameDesignator with the specified name.
     *
     * @param newName  the name.
     */
  public NameDesignator(String newName)
    {
    name = newName;
    }

    /**
     * Returns a constant indicating which kind of designator is.
     * In this case the constant will be: Designator.DESIGNATOR_NAME
     *
     * @return a constant indicating the kind of the designator.
     */
  public int getDesignatorKind()
    {
    return DESIGNATOR_NAME;
    }

    /**
     * Sets the name used in this designator.
     *
     * @param newName  the name to be associated with this designator.
     */
  public void setName(String newName)
    {
    name = newName;
    }
    
    /**
     * Returns the name used in this designator.
     *
     * @return the name.
     */
  public String getName()
    {
    return name;
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
  			NameDesignator nameDesignator = null;
  			
  			try
					{
					nameDesignator = (NameDesignator)this.getClass().newInstance();
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
					
  			nameDesignator.setName(getName());
  			
  			return nameDesignator;
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

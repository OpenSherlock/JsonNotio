package notio;

    /** 
     * Class which may be queried to verify that specific, independant
     * extensions to the API are available and also to activate or deactivate
     * any extended features.  Note that all methods in this class are static
     * and specific instances of it are not needed.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.6 $, $Date: 1999/05/04 01:35:51 $
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
     * @bug What about activating different features for separate,
     * concurrently running databases?  Allow specific extensions instances?
     * @idea Should we allow people to query an implementation name here?
     */

public class Extensions
  {
    /**
     * Returns an array of strings that contains the names of all available
     * extensions.  Extension names should look something like:
     * EXTENSION_THREAD_SAFETY
     *
     * @return an array of strings containing the names of available
     * extensions, possibly empty, or null.
     */
  public static String[] listAvailableExtensions()
    {
    return null;
    }

    /**
     * Used to check if a particular extension is available.
     *
     * @param extensionName  the extension being looked for.
     * @return true if the extension is available.
     */
  public static boolean isExtensionAvailable(String extensionName)
    {
    return false;
    }

    /**
     * Used to check if a particular extension is 'active'.  Not all
     * extensions will have an active or inactive status.  The results for
     * calling this method for such extensions is undefined.
     *
     * @param extensionName  the extension being looked for.
     * @return true if the extension is active.
     */
  public static boolean isExtensionActive(String extensionName)
    {
    return false;
    }

    /**
     * Used to query the current configuration of an extension.  Not all
     * extensions will have configuration information.  The result of
     * calling this method for such extensions is undefined.
     *
     * @param extensionName  the extension being looked for.
     * @return an array of Objects containing the desired information,
     * possibly empty, or null.
     */
  public static Object[] getExtensionParameters(String extensionName)
    {
    return null;
    }

    /**
     * Used to activate or configure API extensions.  Obviously, not all
     * extensions will be optional or require configuration.  If an extension
     * does not require these services then the implementation can simply
     * ignore calls to this method with regards to that extension.
     *
     * @param extensionName  the extension being activated/configured
     * @param args  an array of objects used to configure the extension if
     * necessary.
     *
     * @bug Should this throw OperationException so people can specify their
     * own errors?
     */
  public static void activateExtension(String extensionName, Object args[])
    {
    }

    /**
     * Used to deactivate API extensions.  Obviously, not all
     * extensions will be optional.  If an extension
     * does not require these services then the implementation can simply
     * ignore calls to this method with regards to that extension.
     * The arguments are provided for deactivation in case any details need to
     * be specified about the deactivation process.
     *
     * @param extensionName  the extension being activated/configured
     * @param args  an array of objects used to configure the extension if
     * necessary.
     *
     * @bug Should this throw OperationException so people can specify their
     * own errors?
     */
  public static void deactivateExtension(String extensionName, Object args[])
    {
    }
  }

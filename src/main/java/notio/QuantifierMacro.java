package notio;

    /** 
     * Interface for quantifier macros.  This interface is an extension of
     * the Macro interface with no additions.  It is provided chiefly as a
     * means for differentiating quantifier macros from other macros and
     * also in case any quantifier specific interface needs to be added in
     * the future.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.3 $, $Date: 1999/05/04 01:35:58 $
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

public interface QuantifierMacro extends Macro
  {
  }

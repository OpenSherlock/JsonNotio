package notio;

    /** 
     * Class for testing the POSetNode class.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.7 $, $Date: 1999/05/04 01:36:02 $
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

class TestPOSet
  {
  /** Misc variables used in testing. **/
  private static POSetNode top, bottom;
  /** Misc variables used in testing. **/
  private static POSetNode a, b, c, d;

    /** 
     * Main function for test.
     * 
     * @param args  array of string arguments.
     */
  public static void main(String args[])
    {
    System.err.println(" --== POSet Testing ==--");
    top = new POSetNode();
    bottom = new POSetNode();
    a = new POSetNode();
    b = new POSetNode();
    c = new POSetNode();
    d = new POSetNode();

    top.setData("TOP");
    bottom.setData("BOTTOM");
    a.setData("A");
    b.setData("B");
    c.setData("C");
    d.setData("D");


    try
      {
//      top.addChild(bottom);
      bottom.addParent(top);
      }
    catch (OrderConflictException e)
      {
      e.printStackTrace();
      }

     try
      {
      a.addParent(top);
      a.addChild(bottom);
      b.addParent(top);
      b.addChild(bottom);
      b.addParent(a);
      }
    catch (OrderConflictException e)
      {
      e.printStackTrace();
      } 

    System.err.println("Testing isParentOf()...");

    if (top.isParentOf(bottom))
      System.err.println("Top is parent of bottom.");
    else
      System.err.println("isParentOf() failed.");

    if (bottom.isParentOf(top))
      System.err.println("isParentOf() failed.");
    else
      System.err.println("Bottom is NOT parent of top.");


    if (top.isParentOf(a))
      System.err.println("Top is parent of A.");
    else
      System.err.println("isParentOf() failed.");

    if (bottom.isParentOf(a))
      System.err.println("isParentOf() failed.");
    else
      System.err.println("Bottom is NOT parent of A.");


    if (top.isParentOf(b))
      System.err.println("Top is parent of B.");
    else
      System.err.println("isParentOf() failed.");

    if (bottom.isParentOf(b))
      System.err.println("isParentOf() failed.");
    else
      System.err.println("Bottom is NOT parent of B.");


    if (a.isParentOf(b))
      System.err.println("isParentOf() failed.");
    else
      System.err.println("A is NOT parent of B.");

    if (b.isParentOf(a))
      System.err.println("isParentOf() failed.");
    else
      System.err.println("B is NOT parent of A.");



    System.err.println("Testing isChildOf()...");

    if (top.isChildOf(bottom))
      System.err.println("isChildOf() failed.");
    else
      System.err.println("Top is NOT child of bottom.");

    if (bottom.isChildOf(top))
      System.err.println("Bottom is child of top.");
    else
      System.err.println("isChildOf() failed.");



    }
  }

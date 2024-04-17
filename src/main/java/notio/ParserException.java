package notio;

    /**
     * Exception thrown when some parser's operation gives rise to an
     * error.
     *
     * @author Finnegan Southey
     * @version $Name:  $ $Revision: 1.8 $, $Date: 1999/07/02 19:22:55 $
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
public class ParserException extends TranslationException
  {
  	/** The column in which the current token begins.**/
  private int beginColumn = 0;
  
  	/** The column in which the current token ends.**/
  private int endColumn = 0;
  
  	/** The line in which the current token begins.**/
  private int beginLine = 0;
  
  	/** The line in which the current token ends.**/
  private int endLine = 0;
  
  	/** A string containing the offending token. **/
  private String offendingToken;
  
  	/** An array of strings containing the possible expected tokens. **/
  private String expectedTokens[];
  
  	/** 
  	 * A flag indicating the error occurred sometime before the specified token or
  	 * location.
  	 */
  private boolean beforeFlag;
  
  	/** 
  	 * A flag indicating the error occurred sometime after the specified token or
  	 * location.
  	 */
  private boolean afterFlag;
  
    /**
     * Constructs an exception with the specified message.
     *
     * @param message  The details of the exception.
     */
  public ParserException(String message)
    {
    super(message);
    }

    /**
     * Constructs an exception with the specified message and sub-throwable.
     *
     * @param message  the details of the exception.
     * @param newSubThrowable  the sub-throwable to be embedded in this exception.
     */
  public ParserException(String message, Throwable newSubThrowable)
    {
    super(message, newSubThrowable);
    }

    /**
     * Constructs an exception with the specified message, sub-throwable, and details
     * of the token that triggered this exception.
     *
     * @param message  the details of the exception.
     * @param newSubThrowable  the sub-throwable to be embedded in this exception.
     * @param newOffendingToken  the offending token.
     * @param newBeginLine  the line on which the offending token begins.
     * @param newEndLine  the line on which the offending token ends.
     * @param newBeginColumn  the column at which the offending token begins.
     * @param newEndColumn  the column at which the offending token ends.
     * @param newExpectedTokens  an array containing tokens that were expected in place of
     * the offending token.
     * @param newBeforeFlag  a flag indicating that the cause of the exception is actually
     * somewhere before the specified token or location.
     * @param newAfterFlag  a flag indicating that the cause of the exception is actually
     * somewhere after the specified token or location.
     */
  public ParserException(String message, Throwable newSubThrowable,
  	String newOffendingToken, int newBeginLine, int newEndLine, 
  	int newBeginColumn, int newEndColumn, String newExpectedTokens[], 
  	boolean newBeforeFlag, boolean newAfterFlag)
    {
    this(message, newSubThrowable);
    offendingToken = newOffendingToken;
    beginLine = newBeginLine;
    endLine = newEndLine;
    beginColumn = newBeginColumn;
    endColumn = newEndColumn;
    if (newExpectedTokens != null)
    	{
    	expectedTokens = new String[newExpectedTokens.length];
    	System.arraycopy(newExpectedTokens, 0, expectedTokens, 0, newExpectedTokens.length);
    	}
    beforeFlag = newBeforeFlag;
    afterFlag = newAfterFlag;
    }  

    /**
     * Constructs an exception with the specified message and details
     * of the token that triggered this exception.
     *
     * @param message  the details of the exception.
     * @param newOffendingToken  the offending token.
     * @param newBeginLine  the line on which the offending token begins.
     * @param newEndLine  the line on which the offending token ends.
     * @param newBeginColumn  the column at which the offending token begins.
     * @param newEndColumn  the column at which the offending token ends.
     * @param newExpectedTokens  an array containing tokens that were expected in place of
     * the offending token.
     * @param newBeforeFlag  a flag indicating that the cause of the exception is actually
     * somewhere before the specified token or location.
     * @param newAfterFlag  a flag indicating that the cause of the exception is actually
     * somewhere after the specified token or location.
     */
  public ParserException(String message, 
  	String newOffendingToken, int newBeginLine, int newEndLine, 
  	int newBeginColumn, int newEndColumn, String newExpectedTokens[],
  	boolean newBeforeFlag, boolean newAfterFlag)
    {
    this(message, null, newOffendingToken, newBeginLine, newEndLine, newBeginColumn,
    	newEndColumn, newExpectedTokens, newBeforeFlag, newAfterFlag);
    }  
    
    /**
     * Returns the offending token related to this exception or null if none has been
     * specified.
     *
     * @return the offending token or null.
     */
  public String getOffendingToken()
  	{
  	return offendingToken;
  	}
    
    /**
     * Returns the beginning line of the offending token related to this exception or 
     * 0 if none has been specified.  Note that it is possible to have a beginning line
     * but still have null returned by getOffendingToken().  
     * Lines are numbered starting at 1.
     *
     * @return the beginning line of this exception or 0.
     */
  public int getBeginLine()
  	{
  	return beginLine;
  	}
    
    /**
     * Returns the ending line of the offending token related to this exception or 
     * 0 if none has been specified.  Note that it is possible to have a ending line
     * but still have null returned by getOffendingToken().  
     * Lines are numbered starting at 1.
     *
     * @return the ending line of this exception or 0.
     */
  public int getEndLine()
  	{
  	return endLine;
  	}
    
    /**
     * Returns the beginning column of the offending token related to this exception or 
     * 0 if none has been specified.  Note that it is possible to have a beginning column
     * but still have null returned by getOffendingToken().  
     * Columns are numbered starting at 1.
     *
     * @return the beginning column of this exception or 0.
     */
  public int getBeginColumn()
  	{
  	return beginColumn;
  	}
    
    /**
     * Returns the ending column of the offending token related to this exception or 
     * 0 if none has been specified.  Note that it is possible to have a ending column
     * but still have null returned by getOffendingToken().  
     * Columns are numbered starting at 1.
     *
     * @return the ending column of this exception or 0.
     */
  public int getEndColumn()
  	{
  	return endColumn;
  	}
    
    /**
     * Returns an array containing tokens that would have been accept in place of the
     * offending token related to this exceptionm, or null if none have been
     * specified.
     *
     * @return the array of expected tokens or null.
     */
  public String[] getExpectedTokens()
  	{
  	String arr[];
  	
  	arr = new String[expectedTokens.length];
  	System.arraycopy(expectedTokens, 0, arr, 0, expectedTokens.length);
  	
  	return arr;
  	}

    /**
     * Returns a string containing information about the token and/or location involved
     * in this exception, or null if no details are available.
     * The standard getMessage() method in this exception should only return an
     * explanation of the error.  It will frequently be desirable to provide both
     * messages, but on occasion only one will be desired or the formatting must
     * be tightly controlled.  The message will always be a single line.  If more control
     * is needed, the methods for accessing individual details about the exception should
     * be used to construct a message.  Parsing the results of this method is strongly
     * discouraged since no guarantees are provided as to its format.
     *
     * @return a string containing the message, or null.
     *
     * @bug Does not give information about expected tokens.
     */
  public String getOccurranceMessage()
  	{
  	String mess;
  	
  	if (offendingToken != null)
  		{
 			mess = "Token: \"" + offendingToken + "\" Line: " + beginLine + " Column: " + beginColumn;
  		}
  	else
  		if (beginLine != 0)
  			{
  			mess = "Line: " + beginLine + " Column: " + beginColumn;
  			}
  		else
  			{
  			return null;
  			}
  			
  	if (beforeFlag)
  		return "Before " + mess;

  	if (afterFlag)
  		return "After " + mess;
  		
  	return mess;
  	}
  }

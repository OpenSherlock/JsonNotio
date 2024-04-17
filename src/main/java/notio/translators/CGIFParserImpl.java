package notio.translators;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import notio.*;
import com.metamata.parse.*;



	/**
	 * CGIF Parser implementation based on a JavaCC grammar.
	 * This class should never be used directly.  Use CGIFParser instead.
	 *
	 * @author Finnegan Southey
	 * @version $Name:  $ $Revision: 1.68.2.1 $, $Date: 1999/09/17 15:54:31 $
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
	 * @see notio.ParserException
	 * @see notio.translators.CGIFParser
	 *
	 * @idea Should some or all macros be handled by a preprocessor?  It could
	 * certainly make things like "If-Then" and universal quantification
	 * macros much simpler.
	 * @idea Option to trim trailing/leading spaces from comments.
	 * @idea Make some Vectors global to improve speed.
	 * @bug Token coverage is probably inadequate.  We sometimes throw TokenMgrError.
	 * @bug ParserExceptions are still poor at providing locations.  Need access to 
	 * scanner's state information.
	 * @bug Graph termination problem remains.  Can use <EOF> at the end but doesn't
	 * work for nested graphs.  Should probably use <EOF> to terminate Wrapper().
	 * @bug Currently, Concept/Relation/Actor tokens may not use any of each other's closing
	 * delimiter (e.g. you can not use a right paren in a concept comment).  Looks like the
	 * only way around this is to start setting flags inside different node types or use
	 * lexical states.  Node comments are a particularly bad design flaw in CGIF.
	 * @bug Lots of ugly stuff in this class relates to outdated coreference scope checking.
	 * @bug Ok: Need to pass several components down so we can connect them together when we 
	 * know they will really be used.  
	 * @bug What about the state of the defining label table after a ParserException?  
	 * Currently, the state should be assumed to be bad, so a new table or translation
	 * context should be used in the future.  Should we try to handle this elegantly?
	 * Perhaps in CGIFParser.java?
	 */
class CGIFParserImpl
	{
		/** Name of MarkerTable unit in context. **/
	
  public interface ScannerConstants {
  
    int EOF = 0;
    int IF = 6;
    int THEN = 7;
    int ELSE = 8;
    int EITHER = 9;
    int OR = 10;
    int SCOPE = 11;
    int LAMBDA = 12;
    int LEFT_PAREN = 13;
    int RIGHT_PAREN = 14;
    int LEFT_BRACKET = 15;
    int RIGHT_BRACKET = 16;
    int LEFT_BRACE = 17;
    int RIGHT_BRACE = 18;
    int LEFT_ANGLE = 19;
    int RIGHT_ANGLE = 20;
    int COMMA = 21;
    int PERIOD = 22;
    int SEMICOLON = 23;
    int COLON = 24;
    int QUESTIONMARK = 25;
    int TILDE = 26;
    int AT = 27;
    int HASH = 28;
    int PERCENT = 29;
    int ASTERISK = 30;
    int VERT_BAR = 31;
    int CONCEPT_COMMENT = 32;
    int RELATION_COMMENT = 33;
    int ACTOR_COMMENT = 34;
    int FORMAL_COMMENT = 37;
    int MULTI_LINE_COMMENT = 38;
    int INTEGER_LITERAL = 40;
    int DECIMAL_LITERAL = 41;
    int HEX_LITERAL = 42;
    int OCTAL_LITERAL = 43;
    int FLOATING_POINT_LITERAL = 44;
    int EXPONENT = 45;
    int STRING_LITERAL = 46;
    int NAME_LITERAL = 47;
    int IDENTIFIER = 48;
    int LETTER = 49;
    int DIGIT = 50;
    int __jjVif = IF;
    int __jjVthen = THEN;
    int __jjVelse = ELSE;
    int __jjVeither = EITHER;
    int __jjVor = OR;
    int __jjVsc = SCOPE;
    int __jjVlambda = LAMBDA;
    int __jjV_iB = LEFT_PAREN;
    int __jjV_jB = RIGHT_PAREN;
    int __jjV_1C = LEFT_BRACKET;
    int __jjV_3C = RIGHT_BRACKET;
    int __jjV_1D = LEFT_BRACE;
    int __jjV_3D = RIGHT_BRACE;
    int __jjV_2B = LEFT_ANGLE;
    int __jjV_4B = RIGHT_ANGLE;
    int __jjV_mB = COMMA;
    int __jjV_oB = PERIOD;
    int __jjV_1B = SEMICOLON;
    int __jjV_0B = COLON;
    int __jjV_5B = QUESTIONMARK;
    int __jjV_4D = TILDE;
    int __jjV_aC = AT;
    int __jjV_dB = HASH;
    int __jjV_fB = PERCENT;
    int __jjV_kB = ASTERISK;
    int __jjV_2D = VERT_BAR;
  
    int DEFAULT = 0;
    int IN_FORMAL_COMMENT = 1;
    int IN_MULTI_LINE_COMMENT = 2;
  
    String[] tokenImage = {
      "<EOF>",
      "\" \"",
      "\"\\t\"",
      "\"\\r\"",
      "\"\\n\"",
      "\"\\f\"",
      "\"if\"",
      "\"then\"",
      "\"else\"",
      "\"either\"",
      "\"or\"",
      "\"sc\"",
      "\"lambda\"",
      "\"(\"",
      "\")\"",
      "\"[\"",
      "\"]\"",
      "\"{\"",
      "\"}\"",
      "\"<\"",
      "\">\"",
      "\",\"",
      "\".\"",
      "\";\"",
      "\":\"",
      "\"?\"",
      "\"~\"",
      "\"@\"",
      "\"#\"",
      "\"%\"",
      "\"*\"",
      "\"|\"",
      "<CONCEPT_COMMENT>",
      "<RELATION_COMMENT>",
      "<ACTOR_COMMENT>",
      "<token of kind 35>",
      "\"/*\"",
      "\"*/\"",
      "\"*/\"",
      "<token of kind 39>",
      "<INTEGER_LITERAL>",
      "<DECIMAL_LITERAL>",
      "<HEX_LITERAL>",
      "<OCTAL_LITERAL>",
      "<FLOATING_POINT_LITERAL>",
      "<EXPONENT>",
      "<STRING_LITERAL>",
      "<NAME_LITERAL>",
      "<IDENTIFIER>",
      "<LETTER>",
      "<DIGIT>",
    };
  
  }
  public static final int EOF = 0;
  public static final int IF = 6;
  public static final int THEN = 7;
  public static final int ELSE = 8;
  public static final int EITHER = 9;
  public static final int OR = 10;
  public static final int SCOPE = 11;
  public static final int LAMBDA = 12;
  public static final int LEFT_PAREN = 13;
  public static final int RIGHT_PAREN = 14;
  public static final int LEFT_BRACKET = 15;
  public static final int RIGHT_BRACKET = 16;
  public static final int LEFT_BRACE = 17;
  public static final int RIGHT_BRACE = 18;
  public static final int LEFT_ANGLE = 19;
  public static final int RIGHT_ANGLE = 20;
  public static final int COMMA = 21;
  public static final int PERIOD = 22;
  public static final int SEMICOLON = 23;
  public static final int COLON = 24;
  public static final int QUESTIONMARK = 25;
  public static final int TILDE = 26;
  public static final int AT = 27;
  public static final int HASH = 28;
  public static final int PERCENT = 29;
  public static final int ASTERISK = 30;
  public static final int VERT_BAR = 31;
  public static final int CONCEPT_COMMENT = 32;
  public static final int RELATION_COMMENT = 33;
  public static final int ACTOR_COMMENT = 34;
  public static final int FORMAL_COMMENT = 37;
  public static final int MULTI_LINE_COMMENT = 38;
  public static final int INTEGER_LITERAL = 40;
  public static final int DECIMAL_LITERAL = 41;
  public static final int HEX_LITERAL = 42;
  public static final int OCTAL_LITERAL = 43;
  public static final int FLOATING_POINT_LITERAL = 44;
  public static final int EXPONENT = 45;
  public static final int STRING_LITERAL = 46;
  public static final int NAME_LITERAL = 47;
  public static final int IDENTIFIER = 48;
  public static final int LETTER = 49;
  public static final int DIGIT = 50;
  public static final int __jjVif = IF;
  public static final int __jjVthen = THEN;
  public static final int __jjVelse = ELSE;
  public static final int __jjVeither = EITHER;
  public static final int __jjVor = OR;
  public static final int __jjVsc = SCOPE;
  public static final int __jjVlambda = LAMBDA;
  public static final int __jjV_iB = LEFT_PAREN;
  public static final int __jjV_jB = RIGHT_PAREN;
  public static final int __jjV_1C = LEFT_BRACKET;
  public static final int __jjV_3C = RIGHT_BRACKET;
  public static final int __jjV_1D = LEFT_BRACE;
  public static final int __jjV_3D = RIGHT_BRACE;
  public static final int __jjV_2B = LEFT_ANGLE;
  public static final int __jjV_4B = RIGHT_ANGLE;
  public static final int __jjV_mB = COMMA;
  public static final int __jjV_oB = PERIOD;
  public static final int __jjV_1B = SEMICOLON;
  public static final int __jjV_0B = COLON;
  public static final int __jjV_5B = QUESTIONMARK;
  public static final int __jjV_4D = TILDE;
  public static final int __jjV_aC = AT;
  public static final int __jjV_dB = HASH;
  public static final int __jjV_fB = PERCENT;
  public static final int __jjV_kB = ASTERISK;
  public static final int __jjV_2D = VERT_BAR;
  public static final int DEFAULT = 0;
  public static final int IN_FORMAL_COMMENT = 1;
  public static final int IN_MULTI_LINE_COMMENT = 2;
  public static final String[] tokenImage = ScannerConstants.tokenImage;

  public class __jjScanner extends com.metamata.parse.Scanner {

    private com.metamata.parse.MParseReader __jjcs;
    private __jjCGIFParserImplTokenManager __jjtm;

    public __jjScanner(java.io.Reader input) {
      __jjcs = input instanceof com.metamata.parse.MParseReader ? (com.metamata.parse.MParseReader)input : new com.metamata.parse.SimpleReader(input, 1, 1);
      __jjtm = new __jjCGIFParserImplTokenManager(__jjcs);
    }

    public com.metamata.parse.Token getNextToken() {
      return __jjtm.getNextToken();
    }

    public void switchTo(int state) {
      __jjtm.SwitchTo(state);
    }

    public int tokenCount() {
      return ScannerConstants.tokenImage.length;
    }

    public java.lang.String tokenImage(int kind) {
      return ScannerConstants.tokenImage[kind];
    }

    public char readChar() throws java.io.IOException {
      return __jjcs.readChar();
    }

    public void backup(int amount) {
      __jjcs.backup(amount);
    }

    public int getBeginLine() {
      return __jjcs.getBeginLine();
    }

    public int getBeginColumn() {
      return __jjcs.getBeginColumn();
    }

    public int getEndLine() {
      return __jjcs.getEndLine();
    }

    public int getEndColumn() {
      return __jjcs.getEndColumn();
    }

    /**
     * Lexical error occured.
     */
   final int LEXICAL_ERROR = 0;

    /**
     * An attempt wass made to create a second instance of a static token manager.
     */
    final int STATIC_LEXER_ERROR = 1;

    /**
     * Tried to change to an invalid lexical state.
     */
    final int INVALID_LEXICAL_STATE = 2;

    /**
     * Detected (and bailed out of) an infinite loop in the token manager.
     */
    final int LOOP_DETECTED = 3;

    class TokenMgrError extends com.metamata.parse.ScanError
    {
       /*
        * Ordinals for various reasons why an Error of this type can be thrown
        * have been moved out of the class above.
        */

       /**
        * Indicates the reason why the exception is thrown. It will have
        * one of the above 4 values.
        */
       int errorCode;

       /**
        * You can also modify the body of this method to customize your error messages.
        * For example, cases like LOOP_DETECTED and INVALID_LEXICAL_STATE are not
        * of end-users concern, so you can return something like : 
        *
        *     "Internal Error : Please file a bug report .... "
        *
        * from this method for such cases in the release version of your parser.
        */
       public String getMessage() {
          return super.getMessage();
       }

       /*
        * Constructors of various flavors follow.
        */

       public TokenMgrError() {
       }

       public TokenMgrError(String message, int reason) {
          super(message);
          errorCode = reason;
       }

       public TokenMgrError(boolean EOFSeen, int lexState, int errorLine, int errorColumn, String errorAfter, char curChar, int reason) {
         this(
           ("Lexical error at line " +
           errorLine + ", column " +
           errorColumn + ".  Encountered: " +
           (EOFSeen ? "<EOF> " : ("\"" + 
           com.metamata.parse.Utils.addEscapes(String.valueOf(curChar)) +
           "\"") + " (" + (int)curChar + "), ") +
           "after : \"" +
           com.metamata.parse.Utils.addEscapes(errorAfter) + "\"")
           , reason);
       }

    }

     class __jjCGIFParserImplTokenManager 
    {
    private final int jjStopAtPos(int pos, int kind)
    {
       jjmatchedKind = kind;
       jjmatchedPos = pos;
       return pos + 1;
    }
    private final int jjMoveStringLiteralDfa0_0()
    {
       switch(curChar)
       {
          case 9:
             jjmatchedKind = 2;
             return jjMoveNfa_0(3, 0);
          case 10:
             jjmatchedKind = 4;
             return jjMoveNfa_0(3, 0);
          case 12:
             jjmatchedKind = 5;
             return jjMoveNfa_0(3, 0);
          case 13:
             jjmatchedKind = 3;
             return jjMoveNfa_0(3, 0);
          case 32:
             jjmatchedKind = 1;
             return jjMoveNfa_0(3, 0);
          case 35:
             jjmatchedKind = 28;
             return jjMoveNfa_0(3, 0);
          case 37:
             jjmatchedKind = 29;
             return jjMoveNfa_0(3, 0);
          case 40:
             jjmatchedKind = 13;
             return jjMoveNfa_0(3, 0);
          case 41:
             jjmatchedKind = 14;
             return jjMoveNfa_0(3, 0);
          case 42:
             jjmatchedKind = 30;
             return jjMoveNfa_0(3, 0);
          case 44:
             jjmatchedKind = 21;
             return jjMoveNfa_0(3, 0);
          case 46:
             jjmatchedKind = 22;
             return jjMoveNfa_0(3, 0);
          case 47:
             return jjMoveStringLiteralDfa1_0(0x1000000000L);
          case 58:
             jjmatchedKind = 24;
             return jjMoveNfa_0(3, 0);
          case 60:
             jjmatchedKind = 19;
             return jjMoveNfa_0(3, 0);
          case 62:
             jjmatchedKind = 20;
             return jjMoveNfa_0(3, 0);
          case 63:
             jjmatchedKind = 25;
             return jjMoveNfa_0(3, 0);
          case 64:
             jjmatchedKind = 27;
             return jjMoveNfa_0(3, 0);
          case 69:
             return jjMoveStringLiteralDfa1_0(0x300L);
          case 73:
             return jjMoveStringLiteralDfa1_0(0x40L);
          case 76:
             return jjMoveStringLiteralDfa1_0(0x1000L);
          case 79:
             return jjMoveStringLiteralDfa1_0(0x400L);
          case 83:
             return jjMoveStringLiteralDfa1_0(0x800L);
          case 84:
             return jjMoveStringLiteralDfa1_0(0x80L);
          case 91:
             jjmatchedKind = 15;
             return jjMoveNfa_0(3, 0);
          case 93:
             jjmatchedKind = 16;
             return jjMoveNfa_0(3, 0);
          case 101:
             return jjMoveStringLiteralDfa1_0(0x300L);
          case 105:
             return jjMoveStringLiteralDfa1_0(0x40L);
          case 108:
             return jjMoveStringLiteralDfa1_0(0x1000L);
          case 111:
             return jjMoveStringLiteralDfa1_0(0x400L);
          case 115:
             return jjMoveStringLiteralDfa1_0(0x800L);
          case 116:
             return jjMoveStringLiteralDfa1_0(0x80L);
          case 123:
             jjmatchedKind = 17;
             return jjMoveNfa_0(3, 0);
          case 124:
             jjmatchedKind = 31;
             return jjMoveNfa_0(3, 0);
          case 125:
             jjmatchedKind = 18;
             return jjMoveNfa_0(3, 0);
          case 126:
             jjmatchedKind = 26;
             return jjMoveNfa_0(3, 0);
          default :
             return jjMoveNfa_0(3, 0);
       }
    }
    private final int jjMoveStringLiteralDfa1_0(long active0)
    {
       try { curChar = input_stream.readChar(); }
       catch(java.io.IOException e) {
       return jjMoveNfa_0(3, 0);
       }
       switch(curChar)
       {
          case 42:
             if ((active0 & 0x1000000000L) != 0L)
             {
                jjmatchedKind = 36;
                jjmatchedPos = 1;
             }
             break;
          case 65:
             return jjMoveStringLiteralDfa2_0(active0, 0x1000L);
          case 67:
             if ((active0 & 0x800L) != 0L)
             {
                jjmatchedKind = 11;
                jjmatchedPos = 1;
             }
             break;
          case 70:
             if ((active0 & 0x40L) != 0L)
             {
                jjmatchedKind = 6;
                jjmatchedPos = 1;
             }
             break;
          case 72:
             return jjMoveStringLiteralDfa2_0(active0, 0x80L);
          case 73:
             return jjMoveStringLiteralDfa2_0(active0, 0x200L);
          case 76:
             return jjMoveStringLiteralDfa2_0(active0, 0x100L);
          case 82:
             if ((active0 & 0x400L) != 0L)
             {
                jjmatchedKind = 10;
                jjmatchedPos = 1;
             }
             break;
          case 97:
             return jjMoveStringLiteralDfa2_0(active0, 0x1000L);
          case 99:
             if ((active0 & 0x800L) != 0L)
             {
                jjmatchedKind = 11;
                jjmatchedPos = 1;
             }
             break;
          case 102:
             if ((active0 & 0x40L) != 0L)
             {
                jjmatchedKind = 6;
                jjmatchedPos = 1;
             }
             break;
          case 104:
             return jjMoveStringLiteralDfa2_0(active0, 0x80L);
          case 105:
             return jjMoveStringLiteralDfa2_0(active0, 0x200L);
          case 108:
             return jjMoveStringLiteralDfa2_0(active0, 0x100L);
          case 114:
             if ((active0 & 0x400L) != 0L)
             {
                jjmatchedKind = 10;
                jjmatchedPos = 1;
             }
             break;
          default :
             break;
       }
       return jjMoveNfa_0(3, 1);
    }
    private final int jjMoveStringLiteralDfa2_0(long old0, long active0)
    {
       if (((active0 &= old0)) == 0L)
          return jjMoveNfa_0(3, 1);
       try { curChar = input_stream.readChar(); }
       catch(java.io.IOException e) {
       return jjMoveNfa_0(3, 1);
       }
       switch(curChar)
       {
          case 69:
             return jjMoveStringLiteralDfa3_0(active0, 0x80L);
          case 77:
             return jjMoveStringLiteralDfa3_0(active0, 0x1000L);
          case 83:
             return jjMoveStringLiteralDfa3_0(active0, 0x100L);
          case 84:
             return jjMoveStringLiteralDfa3_0(active0, 0x200L);
          case 101:
             return jjMoveStringLiteralDfa3_0(active0, 0x80L);
          case 109:
             return jjMoveStringLiteralDfa3_0(active0, 0x1000L);
          case 115:
             return jjMoveStringLiteralDfa3_0(active0, 0x100L);
          case 116:
             return jjMoveStringLiteralDfa3_0(active0, 0x200L);
          default :
             break;
       }
       return jjMoveNfa_0(3, 2);
    }
    private final int jjMoveStringLiteralDfa3_0(long old0, long active0)
    {
       if (((active0 &= old0)) == 0L)
          return jjMoveNfa_0(3, 2);
       try { curChar = input_stream.readChar(); }
       catch(java.io.IOException e) {
       return jjMoveNfa_0(3, 2);
       }
       switch(curChar)
       {
          case 66:
             return jjMoveStringLiteralDfa4_0(active0, 0x1000L);
          case 69:
             if ((active0 & 0x100L) != 0L)
             {
                jjmatchedKind = 8;
                jjmatchedPos = 3;
             }
             break;
          case 72:
             return jjMoveStringLiteralDfa4_0(active0, 0x200L);
          case 78:
             if ((active0 & 0x80L) != 0L)
             {
                jjmatchedKind = 7;
                jjmatchedPos = 3;
             }
             break;
          case 98:
             return jjMoveStringLiteralDfa4_0(active0, 0x1000L);
          case 101:
             if ((active0 & 0x100L) != 0L)
             {
                jjmatchedKind = 8;
                jjmatchedPos = 3;
             }
             break;
          case 104:
             return jjMoveStringLiteralDfa4_0(active0, 0x200L);
          case 110:
             if ((active0 & 0x80L) != 0L)
             {
                jjmatchedKind = 7;
                jjmatchedPos = 3;
             }
             break;
          default :
             break;
       }
       return jjMoveNfa_0(3, 3);
    }
    private final int jjMoveStringLiteralDfa4_0(long old0, long active0)
    {
       if (((active0 &= old0)) == 0L)
          return jjMoveNfa_0(3, 3);
       try { curChar = input_stream.readChar(); }
       catch(java.io.IOException e) {
       return jjMoveNfa_0(3, 3);
       }
       switch(curChar)
       {
          case 68:
             return jjMoveStringLiteralDfa5_0(active0, 0x1000L);
          case 69:
             return jjMoveStringLiteralDfa5_0(active0, 0x200L);
          case 100:
             return jjMoveStringLiteralDfa5_0(active0, 0x1000L);
          case 101:
             return jjMoveStringLiteralDfa5_0(active0, 0x200L);
          default :
             break;
       }
       return jjMoveNfa_0(3, 4);
    }
    private final int jjMoveStringLiteralDfa5_0(long old0, long active0)
    {
       if (((active0 &= old0)) == 0L)
          return jjMoveNfa_0(3, 4);
       try { curChar = input_stream.readChar(); }
       catch(java.io.IOException e) {
       return jjMoveNfa_0(3, 4);
       }
       switch(curChar)
       {
          case 65:
             if ((active0 & 0x1000L) != 0L)
             {
                jjmatchedKind = 12;
                jjmatchedPos = 5;
             }
             break;
          case 82:
             if ((active0 & 0x200L) != 0L)
             {
                jjmatchedKind = 9;
                jjmatchedPos = 5;
             }
             break;
          case 97:
             if ((active0 & 0x1000L) != 0L)
             {
                jjmatchedKind = 12;
                jjmatchedPos = 5;
             }
             break;
          case 114:
             if ((active0 & 0x200L) != 0L)
             {
                jjmatchedKind = 9;
                jjmatchedPos = 5;
             }
             break;
          default :
             break;
       }
       return jjMoveNfa_0(3, 5);
    }
    private final void jjCheckNAdd(int state)
    {
       if (jjrounds[state] != jjround)
       {
          jjstateSet[jjnewStateCnt++] = state;
          jjrounds[state] = jjround;
       }
    }
    private final void jjAddStates(int start, int end)
    {
       do {
          jjstateSet[jjnewStateCnt++] = jjnextStates[start];
       } while (start++ != end);
    }
    private final void jjCheckNAddTwoStates(int state1, int state2)
    {
       jjCheckNAdd(state1);
       jjCheckNAdd(state2);
    }
    private final void jjCheckNAddStates(int start, int end)
    {
       do {
          jjCheckNAdd(jjnextStates[start]);
       } while (start++ != end);
    }
    private final void jjCheckNAddStates(int start)
    {
       jjCheckNAdd(jjnextStates[start]);
       jjCheckNAdd(jjnextStates[start + 1]);
    }
    final long[] jjbitVec0 = {
       0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL
    };
    final long[] jjbitVec1 = {
       0x1ff00000fffffffeL, 0xffffffffffffc000L, 0xffffffffL, 0x600000000000000L
    };
    final long[] jjbitVec3 = {
       0x0L, 0x0L, 0x0L, 0xff7fffffff7fffffL
    };
    final long[] jjbitVec4 = {
       0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL, 0xffffffffffffffffL
    };
    final long[] jjbitVec5 = {
       0xffffffffffffffffL, 0xffffffffffffffffL, 0xffffL, 0x0L
    };
    final long[] jjbitVec6 = {
       0xffffffffffffffffL, 0xffffffffffffffffL, 0x0L, 0x0L
    };
    final long[] jjbitVec7 = {
       0x3fffffffffffL, 0x0L, 0x0L, 0x0L
    };
    private final int jjMoveNfa_0(int startState, int curPos)
    {
       int strKind = jjmatchedKind;
       int strPos = jjmatchedPos;
       int seenUpto;
       input_stream.backup(seenUpto = curPos + 1);
       try { curChar = input_stream.readChar(); }
       catch(java.io.IOException e) { throw new Error("Internal Error"); }
       curPos = 0;
       int[] nextStates;
       int startsAt = 0;
       jjnewStateCnt = 68;
       int i = 1;
       jjstateSet[0] = startState;
       int j, kind = 0x7fffffff;
       for (;;)
       {
          if (++jjround == 0x7fffffff)
             ReInitRounds();
          if (curChar < 64)
          {
             long l = 1L << curChar;
             MatchLoop: do
             {
                switch(jjstateSet[--i])
                {
                   case 3:
                      if ((0x3ff000000000000L & l) != 0L)
                         jjCheckNAddStates(0, 6);
                      else if (curChar == 59)
                         jjCheckNAddStates(7, 15);
                      else if (curChar == 36)
                      {
                         if (kind > 48)
                            kind = 48;
                         jjCheckNAdd(32);
                      }
                      else if (curChar == 39)
                         jjCheckNAddStates(16, 18);
                      else if (curChar == 34)
                         jjCheckNAddStates(19, 21);
                      else if (curChar == 46)
                         jjCheckNAdd(8);
                      else if (curChar == 47)
                         jjstateSet[jjnewStateCnt++] = 2;
                      if ((0x3fe000000000000L & l) != 0L)
                      {
                         if (kind > 40)
                            kind = 40;
                         jjCheckNAddTwoStates(5, 6);
                      }
                      else if (curChar == 48)
                      {
                         if (kind > 40)
                            kind = 40;
                         jjCheckNAddStates(22, 24);
                      }
                      break;
                   case 0:
                      if (curChar == 42)
                         jjstateSet[jjnewStateCnt++] = 1;
                      break;
                   case 1:
                      if ((0xffff7fffffffffffL & l) != 0L && kind > 35)
                         kind = 35;
                      break;
                   case 2:
                      if (curChar == 42)
                         jjstateSet[jjnewStateCnt++] = 0;
                      break;
                   case 4:
                      if ((0x3fe000000000000L & l) == 0L)
                         break;
                      if (kind > 40)
                         kind = 40;
                      jjCheckNAddTwoStates(5, 6);
                      break;
                   case 5:
                      if ((0x3ff000000000000L & l) == 0L)
                         break;
                      if (kind > 40)
                         kind = 40;
                      jjCheckNAddTwoStates(5, 6);
                      break;
                   case 7:
                      if (curChar == 46)
                         jjCheckNAdd(8);
                      break;
                   case 8:
                      if ((0x3ff000000000000L & l) == 0L)
                         break;
                      if (kind > 44)
                         kind = 44;
                      jjCheckNAddStates(25, 27);
                      break;
                   case 10:
                      if ((0x280000000000L & l) != 0L)
                         jjCheckNAdd(11);
                      break;
                   case 11:
                      if ((0x3ff000000000000L & l) == 0L)
                         break;
                      if (kind > 44)
                         kind = 44;
                      jjCheckNAddTwoStates(11, 12);
                      break;
                   case 13:
                      if (curChar == 34)
                         jjCheckNAddStates(19, 21);
                      break;
                   case 14:
                      if ((0xfffffffbffffdbffL & l) != 0L)
                         jjCheckNAddStates(19, 21);
                      break;
                   case 16:
                      if ((0x8400000000L & l) != 0L)
                         jjCheckNAddStates(19, 21);
                      break;
                   case 17:
                      if (curChar == 34 && kind > 46)
                         kind = 46;
                      break;
                   case 18:
                      if ((0xff000000000000L & l) != 0L)
                         jjCheckNAddStates(28, 31);
                      break;
                   case 19:
                      if ((0xff000000000000L & l) != 0L)
                         jjCheckNAddStates(19, 21);
                      break;
                   case 20:
                      if ((0xf000000000000L & l) != 0L)
                         jjstateSet[jjnewStateCnt++] = 21;
                      break;
                   case 21:
                      if ((0xff000000000000L & l) != 0L)
                         jjCheckNAdd(19);
                      break;
                   case 22:
                      if (curChar == 39)
                         jjCheckNAddStates(16, 18);
                      break;
                   case 23:
                      if ((0xffffff7fffffdbffL & l) != 0L)
                         jjCheckNAddStates(16, 18);
                      break;
                   case 25:
                      if ((0x8400000000L & l) != 0L)
                         jjCheckNAddStates(16, 18);
                      break;
                   case 26:
                      if (curChar == 39 && kind > 47)
                         kind = 47;
                      break;
                   case 27:
                      if ((0xff000000000000L & l) != 0L)
                         jjCheckNAddStates(32, 35);
                      break;
                   case 28:
                      if ((0xff000000000000L & l) != 0L)
                         jjCheckNAddStates(16, 18);
                      break;
                   case 29:
                      if ((0xf000000000000L & l) != 0L)
                         jjstateSet[jjnewStateCnt++] = 30;
                      break;
                   case 30:
                      if ((0xff000000000000L & l) != 0L)
                         jjCheckNAdd(28);
                      break;
                   case 31:
                      if (curChar != 36)
                         break;
                      if (kind > 48)
                         kind = 48;
                      jjCheckNAdd(32);
                      break;
                   case 32:
                      if ((0x3ff001000000000L & l) == 0L)
                         break;
                      if (kind > 48)
                         kind = 48;
                      jjCheckNAdd(32);
                      break;
                   case 33:
                      if ((0x3ff000000000000L & l) != 0L)
                         jjCheckNAddStates(0, 6);
                      break;
                   case 34:
                      if ((0x3ff000000000000L & l) != 0L)
                         jjCheckNAddTwoStates(34, 35);
                      break;
                   case 35:
                      if (curChar != 46)
                         break;
                      if (kind > 44)
                         kind = 44;
                      jjCheckNAddStates(36, 38);
                      break;
                   case 36:
                      if ((0x3ff000000000000L & l) == 0L)
                         break;
                      if (kind > 44)
                         kind = 44;
                      jjCheckNAddStates(36, 38);
                      break;
                   case 38:
                      if ((0x280000000000L & l) != 0L)
                         jjCheckNAdd(39);
                      break;
                   case 39:
                      if ((0x3ff000000000000L & l) == 0L)
                         break;
                      if (kind > 44)
                         kind = 44;
                      jjCheckNAddTwoStates(39, 12);
                      break;
                   case 40:
                      if ((0x3ff000000000000L & l) != 0L)
                         jjCheckNAddTwoStates(40, 41);
                      break;
                   case 42:
                      if ((0x280000000000L & l) != 0L)
                         jjCheckNAdd(43);
                      break;
                   case 43:
                      if ((0x3ff000000000000L & l) == 0L)
                         break;
                      if (kind > 44)
                         kind = 44;
                      jjCheckNAddTwoStates(43, 12);
                      break;
                   case 44:
                      if ((0x3ff000000000000L & l) != 0L)
                         jjCheckNAddStates(39, 41);
                      break;
                   case 46:
                      if ((0x280000000000L & l) != 0L)
                         jjCheckNAdd(47);
                      break;
                   case 47:
                      if ((0x3ff000000000000L & l) != 0L)
                         jjCheckNAddTwoStates(47, 12);
                      break;
                   case 48:
                      if (curChar != 48)
                         break;
                      if (kind > 40)
                         kind = 40;
                      jjCheckNAddStates(22, 24);
                      break;
                   case 50:
                      if ((0x3ff000000000000L & l) == 0L)
                         break;
                      if (kind > 40)
                         kind = 40;
                      jjCheckNAddTwoStates(50, 6);
                      break;
                   case 51:
                      if ((0xff000000000000L & l) == 0L)
                         break;
                      if (kind > 40)
                         kind = 40;
                      jjCheckNAddTwoStates(51, 6);
                      break;
                   case 52:
                      if (curChar == 59)
                         jjCheckNAddStates(7, 15);
                      break;
                   case 53:
                      if ((0xbffffdffffffffffL & l) != 0L)
                         jjCheckNAddStates(42, 44);
                      break;
                   case 58:
                      if ((0xbffffdffffffffffL & l) != 0L)
                         jjCheckNAddStates(45, 47);
                      break;
                   case 59:
                      if (curChar == 41 && kind > 33)
                         kind = 33;
                      break;
                   case 61:
                      if (curChar == 41)
                         jjCheckNAddStates(45, 47);
                      break;
                   case 63:
                      if ((0xbffffdffffffffffL & l) != 0L)
                         jjCheckNAddStates(48, 50);
                      break;
                   case 64:
                      if (curChar == 62 && kind > 34)
                         kind = 34;
                      break;
                   case 66:
                      if (curChar == 62)
                         jjCheckNAddStates(48, 50);
                      break;
                   default : break;
                }
             } while(i != startsAt);
          }
          else if (curChar < 128)
          {
             long l = 1L << (curChar & 077);
             MatchLoop: do
             {
                switch(jjstateSet[--i])
                {
                   case 3:
                   case 32:
                      if ((0x7fffffe87fffffeL & l) == 0L)
                         break;
                      if (kind > 48)
                         kind = 48;
                      jjCheckNAdd(32);
                      break;
                   case 1:
                      if (kind > 35)
                         kind = 35;
                      break;
                   case 6:
                      if ((0x100000001000L & l) != 0L && kind > 40)
                         kind = 40;
                      break;
                   case 9:
                      if ((0x2000000020L & l) != 0L)
                         jjAddStates(51, 52);
                      break;
                   case 12:
                      if ((0x5000000050L & l) != 0L && kind > 44)
                         kind = 44;
                      break;
                   case 14:
                      if ((0xffffffffefffffffL & l) != 0L)
                         jjCheckNAddStates(19, 21);
                      break;
                   case 15:
                      if (curChar == 92)
                         jjAddStates(53, 55);
                      break;
                   case 16:
                      if ((0x14404410000000L & l) != 0L)
                         jjCheckNAddStates(19, 21);
                      break;
                   case 23:
                      if ((0xffffffffefffffffL & l) != 0L)
                         jjCheckNAddStates(16, 18);
                      break;
                   case 24:
                      if (curChar == 92)
                         jjAddStates(56, 58);
                      break;
                   case 25:
                      if ((0x14404410000000L & l) != 0L)
                         jjCheckNAddStates(16, 18);
                      break;
                   case 37:
                      if ((0x2000000020L & l) != 0L)
                         jjAddStates(59, 60);
                      break;
                   case 41:
                      if ((0x2000000020L & l) != 0L)
                         jjAddStates(61, 62);
                      break;
                   case 45:
                      if ((0x2000000020L & l) != 0L)
                         jjAddStates(63, 64);
                      break;
                   case 49:
                      if ((0x100000001000000L & l) != 0L)
                         jjCheckNAdd(50);
                      break;
                   case 50:
                      if ((0x7e0000007eL & l) == 0L)
                         break;
                      if (kind > 40)
                         kind = 40;
                      jjCheckNAddTwoStates(50, 6);
                      break;
                   case 53:
                      if ((0xffffffffdfffffffL & l) != 0L)
                         jjCheckNAddStates(42, 44);
                      break;
                   case 54:
                      if (curChar == 93 && kind > 32)
                         kind = 32;
                      break;
                   case 55:
                      if (curChar == 92)
                         jjAddStates(65, 66);
                      break;
                   case 56:
                      if (curChar == 93)
                         jjCheckNAddStates(42, 44);
                      break;
                   case 57:
                      if (curChar == 92)
                         jjCheckNAddStates(42, 44);
                      break;
                   case 58:
                      if ((0xffffffffdfffffffL & l) != 0L)
                         jjCheckNAddStates(45, 47);
                      break;
                   case 60:
                      if (curChar == 92)
                         jjAddStates(67, 68);
                      break;
                   case 62:
                      if (curChar == 92)
                         jjCheckNAddStates(45, 47);
                      break;
                   case 63:
                      if ((0xffffffffdfffffffL & l) != 0L)
                         jjCheckNAddStates(48, 50);
                      break;
                   case 65:
                      if (curChar == 92)
                         jjAddStates(69, 70);
                      break;
                   case 67:
                      if (curChar == 92)
                         jjCheckNAddStates(48, 50);
                      break;
                   default : break;
                }
             } while(i != startsAt);
          }
          else
          {
             int i2 = (curChar & 0xff) >> 6;
             long l2 = 1L << (curChar & 077);
             MatchLoop: do
             {
                switch(jjstateSet[--i])
                {
                   case 3:
                   case 32:
                      if ((jjbitVec3[i2] & l2) == 0L)
                         break;
                      if (kind > 48)
                         kind = 48;
                      jjCheckNAdd(32);
                      break;
                   case 1:
                      if ((jjbitVec0[i2] & l2) != 0L && kind > 35)
                         kind = 35;
                      break;
                   case 14:
                      if ((jjbitVec0[i2] & l2) != 0L)
                         jjAddStates(19, 21);
                      break;
                   case 23:
                      if ((jjbitVec0[i2] & l2) != 0L)
                         jjAddStates(16, 18);
                      break;
                   case 53:
                      if ((jjbitVec0[i2] & l2) != 0L)
                         jjAddStates(42, 44);
                      break;
                   case 58:
                      if ((jjbitVec0[i2] & l2) != 0L)
                         jjAddStates(45, 47);
                      break;
                   case 63:
                      if ((jjbitVec0[i2] & l2) != 0L)
                         jjAddStates(48, 50);
                      break;
                   default : break;
                }
             } while(i != startsAt);
          }
          if (kind != 0x7fffffff)
          {
             jjmatchedKind = kind;
             jjmatchedPos = curPos;
             kind = 0x7fffffff;
          }
          ++curPos;
          if ((i = jjnewStateCnt) == (startsAt = 68 - (jjnewStateCnt = startsAt)))
             break;
          try { curChar = input_stream.readChar(); }
          catch(java.io.IOException e) { break; }
       }
       if (jjmatchedPos > strPos)
          return curPos;
    
       int toRet = Math.max(curPos, seenUpto);
    
       if (curPos < toRet)
          for (i = toRet - Math.min(curPos, seenUpto); i-- > 0; )
             try { curChar = input_stream.readChar(); }
             catch(java.io.IOException e) { throw new Error("Internal Error : Please send a bug report."); }
    
       if (jjmatchedPos < strPos)
       {
          jjmatchedKind = strKind;
          jjmatchedPos = strPos;
       }
       else if (jjmatchedPos == strPos && jjmatchedKind > strKind)
          jjmatchedKind = strKind;
    
       return toRet;
    }
    private final int jjMoveStringLiteralDfa0_2()
    {
       switch(curChar)
       {
          case 42:
             return jjMoveStringLiteralDfa1_2(0x4000000000L);
          default :
             return 1;
       }
    }
    private final int jjMoveStringLiteralDfa1_2(long active0)
    {
       try { curChar = input_stream.readChar(); }
       catch(java.io.IOException e) {
          return 1;
       }
       switch(curChar)
       {
          case 47:
             if ((active0 & 0x4000000000L) != 0L)
                return jjStopAtPos(1, 38);
             break;
          default :
             return 2;
       }
       return 2;
    }
    private final int jjMoveStringLiteralDfa0_1()
    {
       switch(curChar)
       {
          case 42:
             return jjMoveStringLiteralDfa1_1(0x2000000000L);
          default :
             return 1;
       }
    }
    private final int jjMoveStringLiteralDfa1_1(long active0)
    {
       try { curChar = input_stream.readChar(); }
       catch(java.io.IOException e) {
          return 1;
       }
       switch(curChar)
       {
          case 47:
             if ((active0 & 0x2000000000L) != 0L)
                return jjStopAtPos(1, 37);
             break;
          default :
             return 2;
       }
       return 2;
    }
    final int[] jjnextStates = {
       34, 35, 40, 41, 44, 45, 12, 53, 54, 58, 59, 63, 64, 65, 60, 55, 
       23, 24, 26, 14, 15, 17, 49, 51, 6, 8, 9, 12, 14, 15, 19, 17, 
       23, 24, 28, 26, 36, 37, 12, 44, 45, 12, 53, 54, 55, 58, 59, 60, 
       63, 64, 65, 10, 11, 16, 18, 20, 25, 27, 29, 38, 39, 42, 43, 46, 
       47, 56, 57, 61, 62, 66, 67, 
    };
    public final String[] jjstrLiteralImages = {
    "", null, null, null, null, null, null, null, null, null, null, null, null, 
    "\50", "\51", "\133", "\135", "\173", "\175", "\74", "\76", "\54", "\56", null, 
    "\72", "\77", "\176", "\100", "\43", "\45", "\52", "\174", null, null, null, null, 
    null, null, null, null, null, null, null, null, null, null, null, null, null, null, 
    null, };
    public final String[] lexStateNames = {
       "DEFAULT", 
       "IN_FORMAL_COMMENT", 
       "IN_MULTI_LINE_COMMENT", 
    };
    public final int[] jjnewLexState = {
       -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
       -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, 2, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
       -1, 
    };
    final long[] jjtoToken = {
       0x1d167ff7fffc1L, 
    };
    final long[] jjtoSkip = {
       0x3eL, 
    };
    final long[] jjtoMore = {
       0x9800000000L, 
    };
    private com.metamata.parse.MParseReader input_stream;
    private final int[] jjrounds = new int[68];
    private final int[] jjstateSet = new int[136];
    protected char curChar;
    public __jjCGIFParserImplTokenManager(com.metamata.parse.MParseReader stream)
    {
       if (false)
          throw new Error("ERROR: Cannot use a CharStream class with a non-static lexical analyzer.");
       input_stream = stream;
    }
    public __jjCGIFParserImplTokenManager(com.metamata.parse.MParseReader stream, int lexState)
    {
       this(stream);
       SwitchTo(lexState);
    }
    public void ReInit(com.metamata.parse.MParseReader stream)
    {
       jjmatchedPos = jjnewStateCnt = 0;
       curLexState = defaultLexState;
       input_stream = stream;
       ReInitRounds();
    }
    private final void ReInitRounds()
    {
       int i;
       jjround = 0x80000001;
       for (i = 68; i-- > 0;)
          jjrounds[i] = 0x80000000;
    }
    public void ReInit(com.metamata.parse.MParseReader stream, int lexState)
    {
       ReInit(stream);
       SwitchTo(lexState);
    }
    public void SwitchTo(int lexState)
    {
       if (lexState >= 3 || lexState < 0)
          throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", INVALID_LEXICAL_STATE);
       else
          curLexState = lexState;
    }
    
    private final com.metamata.parse.Token jjFillToken()
    {
       com.metamata.parse.Token t = com.metamata.parse.Token.newToken(jjmatchedKind);
       t.kind = jjmatchedKind;
       String im = jjstrLiteralImages[jjmatchedKind];
       t.image = (im == null) ? input_stream.GetImage() : im;
       t.beginLine = input_stream.getBeginLine();
       t.beginColumn = input_stream.getBeginColumn();
       t.endLine = input_stream.getEndLine();
       t.endColumn = input_stream.getEndColumn();
       return t;
    }
    
    int curLexState = 0;
    int defaultLexState = 0;
    int jjnewStateCnt;
    int jjround;
    int jjmatchedPos;
    int jjmatchedKind;
    
    public final com.metamata.parse.Token getNextToken() 
    {
      int kind;
      com.metamata.parse.Token specialToken = null;
      com.metamata.parse.Token matchedToken;
      int curPos = 0;
    
      EOFLoop :
      for (;;)
      {   
       try   
       {     
          curChar = input_stream.BeginToken();
       }     
       catch(java.io.IOException e)
       {        
          jjmatchedKind = 0;
          matchedToken = jjFillToken();
          return matchedToken;
       }
    
       for (;;)
       {
         switch(curLexState)
         {
           case 0:
             jjmatchedKind = 0x7fffffff;
             jjmatchedPos = 0;
             curPos = jjMoveStringLiteralDfa0_0();
             break;
           case 1:
             jjmatchedKind = 0x7fffffff;
             jjmatchedPos = 0;
             curPos = jjMoveStringLiteralDfa0_1();
             if (jjmatchedPos == 0 && jjmatchedKind > 39)
             {
                jjmatchedKind = 39;
             }
             break;
           case 2:
             jjmatchedKind = 0x7fffffff;
             jjmatchedPos = 0;
             curPos = jjMoveStringLiteralDfa0_2();
             if (jjmatchedPos == 0 && jjmatchedKind > 39)
             {
                jjmatchedKind = 39;
             }
             break;
         }
         if (jjmatchedKind != 0x7fffffff)
         {
            if (jjmatchedPos + 1 < curPos)
               input_stream.backup(curPos - jjmatchedPos - 1);
            if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
            {
               matchedToken = jjFillToken();
           if (jjnewLexState[jjmatchedKind] != -1)
             curLexState = jjnewLexState[jjmatchedKind];
               return matchedToken;
            }
            else if ((jjtoSkip[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
            {
             if (jjnewLexState[jjmatchedKind] != -1)
               curLexState = jjnewLexState[jjmatchedKind];
               continue EOFLoop;
            }
          if (jjnewLexState[jjmatchedKind] != -1)
            curLexState = jjnewLexState[jjmatchedKind];
            curPos = 0;
            jjmatchedKind = 0x7fffffff;
            try {
               curChar = input_stream.readChar();
               continue;
            }
            catch (java.io.IOException e1) { }
         }
         int error_line = input_stream.getEndLine();
         int error_column = input_stream.getEndColumn();
         String error_after = null;
         boolean EOFSeen = false;
         try { input_stream.readChar(); input_stream.backup(1); }
         catch (java.io.IOException e1) {
            EOFSeen = true;
            error_after = curPos <= 1 ? "" : input_stream.GetImage();
            if (curChar == '\n' || curChar == '\r') {
               error_line++;
               error_column = 0;
            }
            else
               error_column++;
         }
         if (!EOFSeen) {
            input_stream.backup(1);
            error_after = curPos <= 1 ? "" : input_stream.GetImage();
         }
         throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, LEXICAL_ERROR);
       }
      }
    }
    
    }

  }

  private static CGIFParserImpl __jjcns = new CGIFParserImpl((CGIFParserImpl)null);

  public static com.metamata.parse.Scanner createNewScanner(java.io.Reader input) {
    return __jjcns.new __jjScanner(input);
  }

  public static com.metamata.parse.Scanner createNewScanner(java.io.InputStream input) {
    return createNewScanner(new java.io.InputStreamReader(input));
  }

  public static com.metamata.parse.Scanner createNewScanner() {
    return createNewScanner(System.in);
  }

  public com.metamata.parse.ParserState __jjstate;
  public static com.metamata.parse.ParserState __jjstaticstate = new com.metamata.parse.ParserState();

  private CGIFParserImpl(CGIFParserImpl dummy) {
  }

  public CGIFParserImpl() {
    this(System.in);
  }

  public CGIFParserImpl(java.io.InputStream input) {
    this(new java.io.InputStreamReader(input));
  }

  public CGIFParserImpl(java.io.Reader input) {
    __jjstate = new com.metamata.parse.ParserState();
    __jjstate.initialize(createNewScanner(input));
  }

  public CGIFParserImpl(com.metamata.parse.Scanner scanner) {
    __jjstate = new com.metamata.parse.ParserState();
    __jjstate.initialize(scanner);
  }

  public void initializeParser() {
    initializeParser(System.in);
  }

  public void initializeParser(java.io.InputStream input) {
    initializeParser(new java.io.InputStreamReader(input));
  }

  public void initializeParser(java.io.Reader input) {
    __jjstate.initialize(createNewScanner(input));
  }

  public void initializeParser(com.metamata.parse.Scanner scanner) {
    __jjstate.initialize(scanner);
  }

  public static void initializeStaticParser() {
    initializeStaticParser(System.in);
  }

  public static void initializeStaticParser(java.io.InputStream input) {
    initializeStaticParser(new java.io.InputStreamReader(input));
  }

  public static void initializeStaticParser(java.io.Reader input) {
    __jjstaticstate.initialize(createNewScanner(input));
  }

  public static void initializeStaticParser(com.metamata.parse.Scanner scanner) {
    __jjstaticstate.initialize(scanner);
  }

  public com.metamata.parse.Token getNextToken() {
    return __jjstate.getNextToken();
  }

  public com.metamata.parse.Token getToken(int index) {
    return __jjstate.getToken(index);
  }

  public static com.metamata.parse.Token getNextStaticParserToken() {
    return __jjstaticstate.getNextToken();
  }

  public static com.metamata.parse.Token getStaticParserToken(int index) {
    return __jjstaticstate.getToken(index);
  }

private static final String MARKER_TABLE_NAME = "MARKER_TABLE";

		/** Type label for negation relation type. **/
	private static final String NEG_TYPE_LABEL = "Neg";


		/** A knowledge base. **/
	private KnowledgeBase knowledgeBase;
	
		/** The marker set from the knowledge base. **/
	private MarkerSet markerSet;
	
		/** The concept type hierarchy from the knowledge base. **/
	private ConceptTypeHierarchy conceptHierarchy;
	
		/** The relation type hierarchy from the knowledge base.  **/
	private RelationTypeHierarchy relationHierarchy;
	
		/** A translation context. **/
	private TranslationContext translationContext;

		/** Flag to indicate whether types should be created on demand or a parse
		 *  exception generated.
		 */
	private boolean createTypesOnDemand = true;

		/**
		 * Initializes a parser with the specified knowledge base, and translation
		 * context.
		 *
		 * @param newKnowledgeBase  the knowledge base to be used while parsing.
		 * @param newTranslationContext  the translation context to be used while
		 * parsing.
		 */
	public void initializeCGIFParserImpl(KnowledgeBase newKnowledgeBase,
		TranslationContext newTranslationContext)
		{
		knowledgeBase = newKnowledgeBase;
		translationContext = newTranslationContext;
		markerSet = knowledgeBase.getMarkerSet();
		conceptHierarchy = knowledgeBase.getConceptTypeHierarchy();
		relationHierarchy = knowledgeBase.getRelationTypeHierarchy();
		}

		/**
		 * Returns an instance of ParserException with the specified message and
		 * token as its offending token.
		 *
		 * @param message  the message to include.
		 * @param offToken  the offending token.
		 * @return an instance of ParserException.
		 *
		 * @bug Should include the expected tokens.
		 */
	private final ParserException generateParserException(String message, Token offToken)
		{
		return generateParserException(message, offToken, false, false);
		}

		/**
		 * Returns an instance of ParserException with the specified message and
		 * token as its offending token, and the before and after flags set as specified.
		 *
		 * @param message  the message to include.
		 * @param offToken  the offending token.
		 * @return an instance of ParserException.
		 *
		 * @bug Should include the expected tokens.
		 */
	private final ParserException generateParserException(String message, Token offToken,
		boolean beforeFlag, boolean afterFlag)
		{
		String expectedTokenStrings[] = null;
		
/*		
		if (__jjstate.expectedTokens != null)
			{
			expectedTokenStrings = new String[__jjstate.expectedTokens.length];
			
			for (int tok = 0; tok < __jjstate.expectedTokens.length; tok++)
				{
				expectedTokenStrings[tok] = __jjstate.scanner.getTokenImage(__jjstate.expectedTokens[tok]);
				}
			}
*/

		return new ParserException(message, offToken.image, offToken.beginLine, offToken.endLine,
			offToken.beginColumn, offToken.endColumn, expectedTokenStrings, afterFlag, beforeFlag);
		}
		
		/**
		 * Returns the MarkerTable currently in used by this parser.
		 *
		 * @return the MarkerTable currently in used by this parser.
		 */
	private final MarkerTable getMarkerTable()
		{
		MarkerTable markerTable;

		markerTable = (MarkerTable)translationContext.getUnit(MARKER_TABLE_NAME);

		if (markerTable == null)
			{
			markerTable = new MarkerTable();
			markerTable.setUnitName(MARKER_TABLE_NAME);
			translationContext.addUnit(markerTable);
			}

		return markerTable;
		}

		/**
		 * This method may be used to tell the parser whether it should create
		 * type objects for any parsed type labels not currently in the database,
		 * or throw an exception.  Created types have only the universal and
		 * absurd types as parents and children respectively.
		 * The flag is true by default.
		 *
		 * @param flag  true or false to turn automatic creation on or off.
		 */
	void setCreateTypesOnDemand(boolean flag)
		{
		createTypesOnDemand = flag;
		}

		/**
		 * This method may be used to check whether the parser will create
		 * type objects for any parsed type labels not currently in the database,
		 * or throw an exception.  This flag is set using
		 * setCreateTypesOnDemand().
		 *
		 * @return true if types will created on demand or false if an exception
		 * will be thrown.
		 */
	boolean getCreateTypesOnDemand()
		{
		return createTypesOnDemand;
		}

		/**
		 * Translates explicit escape sequences into their internal equivalent.
		 *
		 * @param input  the string to be translated.
		 * @return the translated string.
		 *
		 * @bug Must add support for numeric escape sequences.
		 */
	public final String translateEscapeSequences(String input)
		{
		StringBuffer in = new StringBuffer(input);
		StringBuffer output = new StringBuffer(input.length());
		int inLen = input.length();
		char c;

		for (int ch = 0; ch < inLen; ch++)
			{
			c = in.charAt(ch);
			if (c == '\\')
				{
				ch++;
				c = in.charAt(ch);
				switch (c)
					{
					case 'n':
						output.append('\n');
						break;

					case 't':
						output.append('\t');
						break;

					case 'b':
						output.append('\b');
						break;

					case 'r':
						output.append('\r');
						break;

					case 'f':
						output.append('\f');
						break;

					default:
						output.append(c);
					}
				}
			else
				output.append(c);
			}

		return output.toString();
		}

/* ** Skip Definitions ** */



/* ** Separators and Keywords ** */














/* ** Literals ** */



/* ** IDENTIFIERS ** */




/* ** RULES START HERE ** */


/* Graph Rules */


		/**
		 * Attempts to parse a graph from the input stream.
		 *
		 * @param newGraph  the graph into which to parse (null causes a new graph to be
		 * created).
		 * @param definingLabelTable  the current defining label table.
		 * @return the graph created by parsing.
		 * @exception ParserException  if an error occurs while parsing.
		 */
	public Graph Graph(Graph newGraph, DefiningLabelTable definingLabelTable) throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}Concept concept = null;
			Relation relation = null;
			Actor actor = null;
			String comment = null;

			if (newGraph == null)
				newGraph = new Graph();{
			}
		boolean __jjV0 = __jjstate.guessing;
int __jjV2 = 0;
com.metamata.parse.Token __jjV3 = null;
if (__jjV0) {
  __jjV2 = __jjstate.la;
  __jjV3 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV1 = true;
for (;;) {
  try {
boolean __jjV4 = __jjstate.guessing;
int __jjV6 = 0;
com.metamata.parse.Token __jjV7 = null;
if (__jjV4) {
  __jjV6 = __jjstate.la;
  __jjV7 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 1;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV5 = true;
for (;;) {
  try {
Concept(newGraph, definingLabelTable);  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV4) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV5 = false;
    if (__jjV4) {
      __jjstate.la = __jjV6;
      __jjstate.laToken = __jjV7;
    }
  }
  if (__jjV4 || !__jjstate.guessing || !__jjV5) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV4;
if (!__jjV5) {
boolean __jjV8 = __jjstate.guessing;
int __jjV10 = 0;
com.metamata.parse.Token __jjV11 = null;
if (__jjV8) {
  __jjV10 = __jjstate.la;
  __jjV11 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV9 = true;
for (;;) {
  try {
SpecialContext(newGraph, definingLabelTable);  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV8) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV9 = false;
    if (__jjV8) {
      __jjstate.la = __jjV10;
      __jjstate.laToken = __jjV11;
    }
  }
  if (__jjV8 || !__jjstate.guessing || !__jjV9) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV8;
if (!__jjV9) {
relation = NegatedConcept(newGraph, definingLabelTable);if (!__jjstate.guessing)
{
					newGraph.addRelation(relation);
					}}
}
  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV0) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV1 = false;
    if (__jjV0) {
      __jjstate.la = __jjV2;
      __jjstate.laToken = __jjV3;
    }
  }
  if (__jjV0 || !__jjstate.guessing || !__jjV1) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV0;
if (!__jjV1) {
boolean __jjV12 = __jjstate.guessing;
int __jjV14 = 0;
com.metamata.parse.Token __jjV15 = null;
if (__jjV12) {
  __jjV14 = __jjstate.la;
  __jjV15 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV13 = true;
for (;;) {
  try {
relation = Relation(newGraph, definingLabelTable);if (!__jjstate.guessing)
{
				newGraph.addRelation(relation);
				}  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV12) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV13 = false;
    if (__jjV12) {
      __jjstate.la = __jjV14;
      __jjstate.laToken = __jjV15;
    }
  }
  if (__jjV12 || !__jjstate.guessing || !__jjV13) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV12;
if (!__jjV13) {
boolean __jjV16 = __jjstate.guessing;
int __jjV18 = 0;
com.metamata.parse.Token __jjV19 = null;
if (__jjV16) {
  __jjV18 = __jjstate.la;
  __jjV19 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV17 = true;
for (;;) {
  try {
actor = Actor(newGraph, definingLabelTable);if (!__jjstate.guessing)
{
				newGraph.addRelation(actor);
				}  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV16) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV17 = false;
    if (__jjV16) {
      __jjstate.la = __jjV18;
      __jjstate.laToken = __jjV19;
    }
  }
  if (__jjV16 || !__jjstate.guessing || !__jjV17) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV16;
if (!__jjV17) {
comment = GraphComment();if (!__jjstate.guessing)
{
				newGraph.addComment(comment.substring(2, comment.length()-2));
				}}
}
}

boolean __jjV20 = __jjstate.guessing;
int __jjV21 = 0;
com.metamata.parse.Token __jjV22 = null;
for (;;) {
if (__jjV20) {
  __jjV21 = __jjstate.la;
  __jjV22 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV23 = true;
for (;;) {
  try {
boolean __jjV24 = __jjstate.guessing;
int __jjV26 = 0;
com.metamata.parse.Token __jjV27 = null;
if (__jjV24) {
  __jjV26 = __jjstate.la;
  __jjV27 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV25 = true;
for (;;) {
  try {
boolean __jjV28 = __jjstate.guessing;
int __jjV30 = 0;
com.metamata.parse.Token __jjV31 = null;
if (__jjV28) {
  __jjV30 = __jjstate.la;
  __jjV31 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 1;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV29 = true;
for (;;) {
  try {
Concept(newGraph, definingLabelTable);  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV28) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV29 = false;
    if (__jjV28) {
      __jjstate.la = __jjV30;
      __jjstate.laToken = __jjV31;
    }
  }
  if (__jjV28 || !__jjstate.guessing || !__jjV29) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV28;
if (!__jjV29) {
boolean __jjV32 = __jjstate.guessing;
int __jjV34 = 0;
com.metamata.parse.Token __jjV35 = null;
if (__jjV32) {
  __jjV34 = __jjstate.la;
  __jjV35 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV33 = true;
for (;;) {
  try {
SpecialContext(newGraph, definingLabelTable);  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV32) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV33 = false;
    if (__jjV32) {
      __jjstate.la = __jjV34;
      __jjstate.laToken = __jjV35;
    }
  }
  if (__jjV32 || !__jjstate.guessing || !__jjV33) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV32;
if (!__jjV33) {
relation = NegatedConcept(newGraph, definingLabelTable);if (!__jjstate.guessing)
{
					newGraph.addRelation(relation);
					}}
}
  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV24) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV25 = false;
    if (__jjV24) {
      __jjstate.la = __jjV26;
      __jjstate.laToken = __jjV27;
    }
  }
  if (__jjV24 || !__jjstate.guessing || !__jjV25) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV24;
if (!__jjV25) {
boolean __jjV36 = __jjstate.guessing;
int __jjV38 = 0;
com.metamata.parse.Token __jjV39 = null;
if (__jjV36) {
  __jjV38 = __jjstate.la;
  __jjV39 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV37 = true;
for (;;) {
  try {
relation = Relation(newGraph, definingLabelTable);if (!__jjstate.guessing)
{
				newGraph.addRelation(relation);
				}  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV36) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV37 = false;
    if (__jjV36) {
      __jjstate.la = __jjV38;
      __jjstate.laToken = __jjV39;
    }
  }
  if (__jjV36 || !__jjstate.guessing || !__jjV37) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV36;
if (!__jjV37) {
boolean __jjV40 = __jjstate.guessing;
int __jjV42 = 0;
com.metamata.parse.Token __jjV43 = null;
if (__jjV40) {
  __jjV42 = __jjstate.la;
  __jjV43 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV41 = true;
for (;;) {
  try {
actor = Actor(newGraph, definingLabelTable);if (!__jjstate.guessing)
{
				newGraph.addRelation(actor);
				}  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV40) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV41 = false;
    if (__jjV40) {
      __jjstate.la = __jjV42;
      __jjstate.laToken = __jjV43;
    }
  }
  if (__jjV40 || !__jjstate.guessing || !__jjV41) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV40;
if (!__jjV41) {
comment = GraphComment();if (!__jjstate.guessing)
{
				newGraph.addComment(comment.substring(2, comment.length()-2));
				}}
}
}
  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV20) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV23 = false;
    if (__jjV20) {
      __jjstate.la = __jjV21;
      __jjstate.laToken = __jjV22;
    }
  }
  if (__jjV20 || !__jjstate.guessing || !__jjV23) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV20;
if (!__jjV23) {
  break;
}
}
if (!__jjstate.guessing)
{
			
			return newGraph;
			}return null;

		}


		/**
		 * Attempts to parse a graph comment from the input stream.
		 *
		 * @return the graph comment created by parsing.
		 * @exception ParserException  if an error occurs while parsing.
		 *
		 * @idea Should this be split so we have a non-terminal for javadocs?
		 */
	public String GraphComment() throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}com.metamata.parse.Token commentToken = null;{
			}
		boolean __jjV0 = __jjstate.guessing;
int __jjV2 = 0;
com.metamata.parse.Token __jjV3 = null;
if (__jjV0) {
  __jjV2 = __jjstate.la;
  __jjV3 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV1 = true;
for (;;) {
  try {
commentToken = __jjstate.ematch(FORMAL_COMMENT);
if (!__jjstate.guessing)
{
		  	// Must prefix with single asterisk since it is swallowed by the token matcher.
			  return "*" + commentToken.image;
			  }  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV0) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV1 = false;
    if (__jjV0) {
      __jjstate.la = __jjV2;
      __jjstate.laToken = __jjV3;
    }
  }
  if (__jjV0 || !__jjstate.guessing || !__jjV1) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV0;
if (!__jjV1) {
commentToken = __jjstate.ematch(MULTI_LINE_COMMENT);
if (!__jjstate.guessing)
{
			  return commentToken.image;
			  }}
return null;

		}


/* Concept Rules */

		/**
		 * Attempts to parse a concept from the input stream.
		 *
		 * @param graph  the graph to which the new concept belongs.
		 * @param definingLabelTable  the current defining label table.
		 * @return the concept created by parsing.
		 * @exception ParserException  if an error occurs while parsing.
		 *
		 * @bug Some clarifying syntax removed to accomodate a bug in mparse 1.1.  Does not
		 * affect functionality.
		 * @bug Needs serious cleansing.
		 */
	public Concept Concept(Graph graph, DefiningLabelTable definingLabelTable) throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}Concept newConcept = null;
			ConceptType conType = null;
			String typeMacro = null;
			String comment = null;
			Token defLabelToken = null;
			Token boundLabelToken = null;			
			Referent referent = null;{
			}

		__jjstate.ematch(LEFT_BRACKET);

boolean __jjV1 = __jjstate.guessing;
int __jjV2 = 0;
com.metamata.parse.Token __jjV3 = null;
if (__jjV1) {
  __jjV2 = __jjstate.la;
  __jjV3 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV0 = true;
for (;;) {
  try {
conType = ConceptType();  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV1) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV0 = false;
    if (__jjV1) {
      __jjstate.la = __jjV2;
      __jjstate.laToken = __jjV3;
    }
  }
  if (__jjV1 || !__jjstate.guessing || !__jjV0) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV1;
if (!__jjstate.guessing)
{
		// We got this far so build the concept because we need it for the rest
		newConcept = new Concept(conType);
		
		// Add newly created concept to graph (if any) so we can do coreference stuff
		if (graph != null)
			graph.addConcept(newConcept);
		}
boolean __jjV5 = __jjstate.guessing;
int __jjV6 = 0;
com.metamata.parse.Token __jjV7 = null;
if (__jjV5) {
  __jjV6 = __jjstate.la;
  __jjV7 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV4 = true;
for (;;) {
  try {
__jjstate.ematch(COLON);
  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV5) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV4 = false;
    if (__jjV5) {
      __jjstate.la = __jjV6;
      __jjstate.laToken = __jjV7;
    }
  }
  if (__jjV5 || !__jjstate.guessing || !__jjV4) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV5;

boolean __jjV9 = __jjstate.guessing;
int __jjV10 = 0;
com.metamata.parse.Token __jjV11 = null;
if (__jjV9) {
  __jjV10 = __jjstate.la;
  __jjV11 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV8 = true;
for (;;) {
  try {
boolean __jjV12 = __jjstate.guessing;
int __jjV14 = 0;
com.metamata.parse.Token __jjV15 = null;
if (__jjV12) {
  __jjV14 = __jjstate.la;
  __jjV15 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV13 = true;
for (;;) {
  try {
defLabelToken = DefinedLabel();if (!__jjstate.guessing)
{
				processConceptDefiningLabel(defLabelToken, newConcept, graph, definingLabelTable);
				}  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV12) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV13 = false;
    if (__jjV12) {
      __jjstate.la = __jjV14;
      __jjstate.laToken = __jjV15;
    }
  }
  if (__jjV12 || !__jjstate.guessing || !__jjV13) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV12;
if (!__jjV13) {
boundLabelToken = BoundLabel();if (!__jjstate.guessing)
{ 
						processConceptBoundLabel(boundLabelToken, newConcept, graph, definingLabelTable);
						}
boolean __jjV16 = __jjstate.guessing;
int __jjV17 = 0;
com.metamata.parse.Token __jjV18 = null;
for (;;) {
if (__jjV16) {
  __jjV17 = __jjstate.la;
  __jjV18 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV19 = true;
for (;;) {
  try {
boundLabelToken = BoundLabel();if (!__jjstate.guessing)
{ 
						processConceptBoundLabel(boundLabelToken, newConcept, graph, definingLabelTable);
						}  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV16) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV19 = false;
    if (__jjV16) {
      __jjstate.la = __jjV17;
      __jjstate.laToken = __jjV18;
    }
  }
  if (__jjV16 || !__jjstate.guessing || !__jjV19) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV16;
if (!__jjV19) {
  break;
}
}
}
  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV9) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV8 = false;
    if (__jjV9) {
      __jjstate.la = __jjV10;
      __jjstate.laToken = __jjV11;
    }
  }
  if (__jjV9 || !__jjstate.guessing || !__jjV8) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV9;

boolean __jjV21 = __jjstate.guessing;
int __jjV22 = 0;
com.metamata.parse.Token __jjV23 = null;
if (__jjV21) {
  __jjV22 = __jjstate.la;
  __jjV23 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV20 = true;
for (;;) {
  try {
referent = Referent(referent, definingLabelTable);  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV21) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV20 = false;
    if (__jjV21) {
      __jjstate.la = __jjV22;
      __jjstate.laToken = __jjV23;
    }
  }
  if (__jjV21 || !__jjstate.guessing || !__jjV20) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV21;
boolean __jjV24 = __jjstate.guessing;
int __jjV26 = 0;
com.metamata.parse.Token __jjV27 = null;
if (__jjV24) {
  __jjV26 = __jjstate.la;
  __jjV27 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV25 = true;
for (;;) {
  try {
comment = ConceptComment();  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV24) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV25 = false;
    if (__jjV24) {
      __jjstate.la = __jjV26;
      __jjstate.laToken = __jjV27;
    }
  }
  if (__jjV24 || !__jjstate.guessing || !__jjV25) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV24;
if (!__jjV25) {
__jjstate.ematch(RIGHT_BRACKET);
}
if (!__jjstate.guessing)
{
/*			// Create new concept if it wasn't creating before parsing the referent
			if (newConcept == null)
				newConcept = new Concept(contype, referent);
*/
			// Set concept referent
			if (referent != null)
				newConcept.setReferent(referent);

			// Set concept comment
			if (comment != null)
				newConcept.setComment(comment);

/*			// Add newly created concept to graph (if any) so we can do coreference stuff
			if (graph != null)
				graph.addConcept(newConcept);
*/
/*			// Handle defining label, if  any
			if (defLabelToken != null)
				{
				processConceptDefiningLabel(defLabelToken, newConcept, graph, definingLabelTable);
				}
*/
/*			// Handle bound labels (if any)
			for (Enumeration enum = boundLabelTokenVec.elements(); enum.hasMoreElements();)
				{
				boundLabelToken = (Token)enum.nextElement();
				processConceptBoundLabel(boundLabelToken, newConcept, graph, definingLabelTable);
				}
*/			
			return newConcept;
			}return null;

		}


		/**
		 * Attempts to parse a special context from the input stream.
		 *
		 * @param graph  the graph to which the new context (concept) belongs.
		 * @param definingLabelTable  the current defining label table.
		 * @return the concept created by parsing.
		 * @exception ParserException  if an error occurs while parsing.
		 *
		 * @bug Special context label is parsed but not used.  It should probably be made
		 * into a type if the appropriate expansion is not to be performed.
		 */
	public Concept SpecialContext(Graph graph, DefiningLabelTable definingLabelTable) throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}Graph descriptorGraph = new Graph();
			Concept newConcept = null;
			Referent referent = null;
			String specialConLabel;{
			}

		__jjstate.ematch(LEFT_BRACKET);
specialConLabel = SpecialContextLabel();if (!__jjstate.guessing)
{
			referent = new Referent(descriptorGraph);
			newConcept = new Concept(referent);
			graph.addConcept(newConcept);
			}if (!__jjstate.guessing)
{
		// Push context on to stack
		definingLabelTable.pushContext();
		}Graph(descriptorGraph, definingLabelTable);if (!__jjstate.guessing)
{
		// Pop context from stack
		definingLabelTable.popContext();
		}__jjstate.ematch(RIGHT_BRACKET);
if (!__jjstate.guessing)
{
				return newConcept;
				}return null;

		}


		/**
		 * Attempts to parse a negated concept from the input stream.
		 *
		 * @param graph  the graph to which the negated concept should be added.
		 * @param definingLabelTable  the current defining label table.
		 * @return the Neg relation attached to the negated concept.
		 * @exception ParserException  if an error occurs while parsing.
		 */
	public Relation NegatedConcept(Graph graph, DefiningLabelTable definingLabelTable) throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}Concept con[] = null;
			Relation rel = null;
			RelationType negType;
			Graph descriptorGraph = null;
			Referent referent = null;{
			}

		__jjstate.ematch(TILDE);
__jjstate.ematch(LEFT_BRACKET);
if (!__jjstate.guessing)
{
			con = new Concept[1];
			descriptorGraph = new Graph();
			referent = new Referent(descriptorGraph);
			con[0] = new Concept(referent);
			graph.addConcept(con[0]);
			negType = establishNegRelationType();
			rel = new Relation(negType, con);
			}if (!__jjstate.guessing)
{
		// Push context on to stack
		definingLabelTable.pushContext();
		}Graph(descriptorGraph, definingLabelTable);if (!__jjstate.guessing)
{
		// Pop context from stack
		definingLabelTable.popContext();
		}__jjstate.ematch(RIGHT_BRACKET);
if (!__jjstate.guessing)
{
			return rel;
			}return null;

		}


		/**
		 * Attempts to parse a concept type from the input stream.
		 *
		 * @return the concept type created or looked up by parsing.
		 * @exception ParserException  if an error occurs while parsing.
		 */
	public ConceptType ConceptType() throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}ConceptType conType = null;{
			}

		boolean __jjV0 = __jjstate.guessing;
int __jjV2 = 0;
com.metamata.parse.Token __jjV3 = null;
if (__jjV0) {
  __jjV2 = __jjstate.la;
  __jjV3 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV1 = true;
for (;;) {
  try {
conType = ConceptTypeLabel();  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV0) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV1 = false;
    if (__jjV0) {
      __jjstate.la = __jjV2;
      __jjstate.laToken = __jjV3;
    }
  }
  if (__jjV0 || !__jjstate.guessing || !__jjV1) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV0;
if (!__jjV1) {
conType = MonadicLambdaExpression();}
if (!__jjstate.guessing)
{
			return conType;
			}return null;

		}


		/**
		 * Attempts to parse a concept type label from the input stream.
		 *
		 * @return the concept type created or looked up by parsing.
		 * @exception ParserException  if an error occurs while parsing.
		 */
	public ConceptType ConceptTypeLabel() throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}com.metamata.parse.Token labelToken;
			ConceptType conType;{
			}

		labelToken = __jjstate.ematch(IDENTIFIER);
if (!__jjstate.guessing)
{
			conType = (ConceptType)conceptHierarchy.getTypeByLabel(labelToken.image);

			if (conType == null)
				if (createTypesOnDemand)
					{
					// Must create the type in this case.  Assume universal and absurd as
					// parents and children.
					conType = new ConceptType(labelToken.image);
					try
						{
						conceptHierarchy.addTypeToHierarchy(conType);
						}
					catch (TypeAddError e)
						{
						throw new ParserException("Error adding concept type.", e);
						}
					catch (TypeChangeError e)
						{
						// Note: This should never happen since we don't specify parents or children.
						throw new ParserException("Error adding concept type.", e);
						}
					}
			else
				throw generateParserException("Specified concept type is not present in concept type hierarchy.",
					labelToken);

			return conType;
			}return null;

		}

		/**
		 * Attempts to parse a special context label from the input stream.
		 *
		 * @return the parsed label.
		 * @exception ParserException  if an error occurs while parsing.
		 */
	public String SpecialContextLabel() throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}com.metamata.parse.Token labelToken = null;{
			}

		boolean __jjV0 = __jjstate.guessing;
int __jjV2 = 0;
com.metamata.parse.Token __jjV3 = null;
if (__jjV0) {
  __jjV2 = __jjstate.la;
  __jjV3 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV1 = true;
for (;;) {
  try {
labelToken = __jjstate.ematch(IF);
  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV0) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV1 = false;
    if (__jjV0) {
      __jjstate.la = __jjV2;
      __jjstate.laToken = __jjV3;
    }
  }
  if (__jjV0 || !__jjstate.guessing || !__jjV1) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV0;
if (!__jjV1) {
boolean __jjV4 = __jjstate.guessing;
int __jjV6 = 0;
com.metamata.parse.Token __jjV7 = null;
if (__jjV4) {
  __jjV6 = __jjstate.la;
  __jjV7 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV5 = true;
for (;;) {
  try {
labelToken = __jjstate.ematch(THEN);
  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV4) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV5 = false;
    if (__jjV4) {
      __jjstate.la = __jjV6;
      __jjstate.laToken = __jjV7;
    }
  }
  if (__jjV4 || !__jjstate.guessing || !__jjV5) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV4;
if (!__jjV5) {
boolean __jjV8 = __jjstate.guessing;
int __jjV10 = 0;
com.metamata.parse.Token __jjV11 = null;
if (__jjV8) {
  __jjV10 = __jjstate.la;
  __jjV11 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV9 = true;
for (;;) {
  try {
labelToken = __jjstate.ematch(EITHER);
  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV8) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV9 = false;
    if (__jjV8) {
      __jjstate.la = __jjV10;
      __jjstate.laToken = __jjV11;
    }
  }
  if (__jjV8 || !__jjstate.guessing || !__jjV9) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV8;
if (!__jjV9) {
boolean __jjV12 = __jjstate.guessing;
int __jjV14 = 0;
com.metamata.parse.Token __jjV15 = null;
if (__jjV12) {
  __jjV14 = __jjstate.la;
  __jjV15 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV13 = true;
for (;;) {
  try {
labelToken = __jjstate.ematch(OR);
  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV12) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV13 = false;
    if (__jjV12) {
      __jjstate.la = __jjV14;
      __jjstate.laToken = __jjV15;
    }
  }
  if (__jjV12 || !__jjstate.guessing || !__jjV13) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV12;
if (!__jjV13) {
boolean __jjV16 = __jjstate.guessing;
int __jjV18 = 0;
com.metamata.parse.Token __jjV19 = null;
if (__jjV16) {
  __jjV18 = __jjstate.la;
  __jjV19 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV17 = true;
for (;;) {
  try {
labelToken = __jjstate.ematch(SCOPE);
  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV16) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV17 = false;
    if (__jjV16) {
      __jjstate.la = __jjV18;
      __jjstate.laToken = __jjV19;
    }
  }
  if (__jjV16 || !__jjstate.guessing || !__jjV17) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV16;
if (!__jjV17) {
labelToken = __jjstate.ematch(ELSE);
if (!__jjstate.guessing)
{
				throw generateParserException("The special context label \"else\" is reserved but not currently supported and should not be used.", labelToken);
				}}
}
}
}
}
if (!__jjstate.guessing)
{
			return labelToken.image;
			}return null;

		}

		/**
		 * Attempts to parse a monadic lambda expression from the input stream.
		 *
		 * @return a concept type corresponding to a monadic lambda expression created by parsing.
		 * @exception ParserException  if an error occurs while parsing.
		 *
		 * @bug Search concept type hierarchy for monadic before creating it?  Currently
		 * we just create an unlabelled type for every monadic we encounter.
		 */
	public ConceptType MonadicLambdaExpression() throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}Graph expression = null;
			Token defLabelToken;
			ConceptType paramType;
			ConceptTypeDefinition typeDef;
			Concept defConcept;
			ConceptType conType;
			DefiningLabelTable definingLabelTable = new DefiningLabelTable();{
			}

		__jjstate.ematch(LEFT_PAREN);
__jjstate.ematch(LAMBDA);
__jjstate.ematch(LEFT_PAREN);
paramType = ConceptType();defLabelToken = DefinedLabel();__jjstate.ematch(RIGHT_PAREN);
if (!__jjstate.guessing)
{
		// Push context on to stack
		definingLabelTable.pushContext();
		}
boolean __jjV1 = __jjstate.guessing;
int __jjV2 = 0;
com.metamata.parse.Token __jjV3 = null;
if (__jjV1) {
  __jjV2 = __jjstate.la;
  __jjV3 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV0 = true;
for (;;) {
  try {
expression = Graph(null, definingLabelTable);  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV1) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV0 = false;
    if (__jjV1) {
      __jjstate.la = __jjV2;
      __jjstate.laToken = __jjV3;
    }
  }
  if (__jjV1 || !__jjstate.guessing || !__jjV0) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV1;
__jjstate.ematch(RIGHT_PAREN);
if (!__jjstate.guessing)
{
			// We always create a type in this case.  Assume universal and absurd as
			// parents and children.
			if (expression == null)
				{
				// Create using signature alone
				conType = new ConceptType(new ConceptTypeDefinition(paramType));
				}
			else
				{
				// Create using formal parameter and graph
				// Find concept using defining label
				defConcept = definingLabelTable.getDefiningConceptByDefiningLabel(defLabelToken.image);
				if (defConcept == null)
					{
					throw generateParserException("The formal parameter concept labelled \""+defLabelToken.image+"\" is not defined in the associated graph.", defLabelToken);
					}
				else
					if (!defConcept.isEnclosedBy(expression))
						{
						throw generateParserException("The formal parameter concept labelled \""+defLabelToken.image+"\" is not part of the associated graph.", defLabelToken);
						}

				// Type check vs. signature
				if (!defConcept.getType().equals(paramType))
					{
					throw generateParserException("Type mismatch between signature and formal parameter: " + defLabelToken.image, defLabelToken);
					}

				conType = new ConceptType(new ConceptTypeDefinition(defConcept, expression));
				}

			try
				{
				conceptHierarchy.addTypeToHierarchy(conType);
				}
			catch (TypeAddError e)
				{
				throw new ParserException("Error adding concept type.", e);
				}
			catch (TypeChangeError e)
				{
				// Note: This should never happen since we don't specify parents or children.
				throw new ParserException("Error adding concept type.", e);
				}

			// Pop context from stack
			definingLabelTable.popContext();
			
			return conType;
			}return null;

		}


		/**
		 * Attempts to parse a defining label from the input stream.
		 *
		 * @return the defining label token created by parsing.
		 * @exception ParserException  if an error occurs while parsing.
		 *
		 * @bug Shouldn't this really be called a DefiningLabel?
		 */
	public Token DefinedLabel() throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}com.metamata.parse.Token labelToken;{
			}

		__jjstate.ematch(ASTERISK);
labelToken = __jjstate.ematch(IDENTIFIER);
if (!__jjstate.guessing)
{
			return labelToken;
			}return null;

		}


		/**
		 * Attempts to parse a bound label from the input stream.
		 *
		 * @return the bound label token created by parsing.
		 * @exception ParserException  if an error occurs while parsing.
		 */
	public Token BoundLabel() throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}com.metamata.parse.Token idenToken;{
			}

		__jjstate.ematch(QUESTIONMARK);
idenToken = __jjstate.ematch(IDENTIFIER);
if (!__jjstate.guessing)
{
			return idenToken;
			}return null;

		}


		/**
		 * Attempts to parse a referent from the input stream.
		 * This rule only matches non-blank referents.
		 *
		 * @param referent  a referent to be passed in or null.
		 * @param definingLabelTable  the current defining label table.
		 * @return the referent created by parsing.
		 * @exception ParserException  if an error occurs while parsing.
		 *
		 * @bug Certain quantifier/designator combinations don't make logical
		 * sense but I don't think we can do anything about that here.
		 */
	public Referent Referent(Referent referent, DefiningLabelTable definingLabelTable) throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}Macro quantifier = null;
			Designator designator = null;
			Graph descriptor = null;{
			}

			if (!__jjstate.guessing)
{
			// Push context on to stack
			definingLabelTable.pushContext();
			}
boolean __jjV1 = __jjstate.guessing;
int __jjV2 = 0;
com.metamata.parse.Token __jjV3 = null;
if (__jjV1) {
  __jjV2 = __jjstate.la;
  __jjV3 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV0 = true;
for (;;) {
  try {
quantifier = Quantifier();  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV1) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV0 = false;
    if (__jjV1) {
      __jjstate.la = __jjV2;
      __jjstate.laToken = __jjV3;
    }
  }
  if (__jjV1 || !__jjstate.guessing || !__jjV0) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV1;

boolean __jjV5 = __jjstate.guessing;
int __jjV6 = 0;
com.metamata.parse.Token __jjV7 = null;
if (__jjV5) {
  __jjV6 = __jjstate.la;
  __jjV7 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV4 = true;
for (;;) {
  try {
designator = Designator();  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV5) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV4 = false;
    if (__jjV5) {
      __jjstate.la = __jjV6;
      __jjstate.laToken = __jjV7;
    }
  }
  if (__jjV5 || !__jjstate.guessing || !__jjV4) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV5;

boolean __jjV9 = __jjstate.guessing;
int __jjV10 = 0;
com.metamata.parse.Token __jjV11 = null;
if (__jjV9) {
  __jjV10 = __jjstate.la;
  __jjV11 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV8 = true;
for (;;) {
  try {
descriptor = Graph(null, definingLabelTable);  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV9) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV8 = false;
    if (__jjV9) {
      __jjstate.la = __jjV10;
      __jjstate.laToken = __jjV11;
    }
  }
  if (__jjV9 || !__jjstate.guessing || !__jjV8) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV9;
if (!__jjstate.guessing)
{
			// Pop context from stack
			definingLabelTable.popContext();
			}if (!__jjstate.guessing)
{
			if (referent == null && (quantifier != null || designator != null || descriptor != null))
				referent = new Referent();
				
			if (referent != null)
				{
				referent.setQuantifier(quantifier);
				referent.setDesignator(designator);
				referent.setDescriptor(descriptor);
				}
			
			return referent;
			}return null;

		}


		/**
		 * Attempts to parse a quantifier from the input stream.
		 * This rule only matches non-blank quantifiers.
		 *
		 * @return the quantifier created or looked up by parsing (null indicates
		 * existential quantification).
		 * @exception ParserException  if an error occurs while parsing.
		 *
		 * @idea Should there be a sub-interface of Macro called QuantifierMacro?
		 */
	public Macro Quantifier() throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}Macro quantifier = null;{
			}

		quantifier = QuantifierMacro();if (!__jjstate.guessing)
{
			return quantifier;
			}return null;

		}


		/**
		 * Attempts to parse a quantifier macro from the input stream.
		 *
		 * @return the quantifier macro created or looked up by parsing.
		 * @exception ParserException  if an error occurs while parsing.
		 *
		 * @bug Should there be a sub-interface of Macro called QuantifierMacro?
		 * @bug Needs macro lookup.  Currently returns an UnimplementedMacro instance.
		 */
	public Macro QuantifierMacro() throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}com.metamata.parse.Token macroNameToken = null;
			Macro quantifier = null;{
			}

		__jjstate.ematch(AT);
macroNameToken = __jjstate.ematch(IDENTIFIER);
if (!__jjstate.guessing)
{
			// Must lookup macro here to provide appropriate object.  For now we will
			// create an instance of UnimplementedMacro.
			quantifier = new UnimplementedMacro(macroNameToken.image);
			return quantifier;
			}return null;

		}


		/**
		 * Attempts to parse a designator from the input stream.
		 * This rule only matches non-blank designators.
		 *
		 * @param referent  the referent to which this designator belongs.
		 * @param definingLabelTable  the current defining label table.
		 * @return the designator created by parsing.
		 * @exception ParserException  if an error occurs while parsing.
		 */
	public Designator Designator() throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}Designator designator = null;{
			}

		boolean __jjV0 = __jjstate.guessing;
int __jjV2 = 0;
com.metamata.parse.Token __jjV3 = null;
if (__jjV0) {
  __jjV2 = __jjstate.la;
  __jjV3 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV1 = true;
for (;;) {
  try {
designator = Literal();  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV0) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV1 = false;
    if (__jjV0) {
      __jjstate.la = __jjV2;
      __jjstate.laToken = __jjV3;
    }
  }
  if (__jjV0 || !__jjstate.guessing || !__jjV1) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV0;
if (!__jjV1) {
designator = Locator();}
if (!__jjstate.guessing)
{
			return designator;
			}return null;

		}


		/**
		 * Attempts to parse a literal designator from the input stream.
		 *
		 * @return the literal designator created by parsing.
		 * @exception ParserException  if an error occurs while parsing.
		 */
	public LiteralDesignator Literal() throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}Object literal = null;
			LiteralDesignator literalDesignator = null;{
			}

		boolean __jjV0 = __jjstate.guessing;
int __jjV2 = 0;
com.metamata.parse.Token __jjV3 = null;
if (__jjV0) {
  __jjV2 = __jjstate.la;
  __jjV3 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV1 = true;
for (;;) {
  try {
literal = Number();  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV0) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV1 = false;
    if (__jjV0) {
      __jjstate.la = __jjV2;
      __jjstate.laToken = __jjV3;
    }
  }
  if (__jjV0 || !__jjstate.guessing || !__jjV1) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV0;
if (!__jjV1) {
boolean __jjV4 = __jjstate.guessing;
int __jjV6 = 0;
com.metamata.parse.Token __jjV7 = null;
if (__jjV4) {
  __jjV6 = __jjstate.la;
  __jjV7 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV5 = true;
for (;;) {
  try {
literal = String();  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV4) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV5 = false;
    if (__jjV4) {
      __jjstate.la = __jjV6;
      __jjstate.laToken = __jjV7;
    }
  }
  if (__jjV4 || !__jjstate.guessing || !__jjV5) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV4;
if (!__jjV5) {
literalDesignator = EncodedLiteral();}
}
if (!__jjstate.guessing)
{
			if (literal != null)
				literalDesignator = new LiteralDesignator(literal, markerSet);
				
			return literalDesignator;
			}return null;

		}


		/**
		 * Attempts to parse an encoded literal from the input stream.
		 *
		 * @return the literal designator created by parsing.
		 * @exception ParserException  if an error occurs while parsing.
		 *
		 * @bug Need to invoke a decoder or create some dummy object.  Probably need a flag 
		 * indicating whether dummy's should be used, perhaps with a maximum size parameter.
		 */
	public LiteralDesignator EncodedLiteral() throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}com.metamata.parse.Token encodingNameToken = null;			
			com.metamata.parse.Token encodedDataToken = null;{			
			}

		__jjstate.ematch(PERCENT);
encodingNameToken = __jjstate.ematch(IDENTIFIER);
encodedDataToken = __jjstate.ematch(STRING_LITERAL);
if (!__jjstate.guessing)
{
			return new LiteralDesignator(encodedDataToken.image, markerSet);
			}return null;

		}


		/**
		 * Attempts to parse a locator designator from the input stream.  For a name designator
		 * it will return a NameDesignator instance and for a marker designator it will return
		 * a MarkerDesignator instance, creating a native marker and mapping it to the foreign
		 * marker ID, if necessary.  If a native marker exists that corresponds to the foreign
		 * ID, it will be used to construct the MarkerDesignator.
		 *
		 * @return the locator designator created by parsing.
		 * @exception ParserException  if an error occurs while parsing.
		 *
		 * @bug No support for indexicals.  Probably need a pluggable indexical handler.
		 * @bug The treatment of a plain hash ("#") is probably not appropriate for
		 * all situations.  In any event, it certainly shouldn't be added to the
		 * foreign ID translation table.  That is included temporarily to facilitate the
		 * Muto project's testing.
		 */
	public Designator Locator() throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}String name = null;
			String markerID = null;
			Marker marker = null;{
			}

		// ( Name() | IndividualMarker() | Indexical() )
		boolean __jjV0 = __jjstate.guessing;
int __jjV2 = 0;
com.metamata.parse.Token __jjV3 = null;
if (__jjV0) {
  __jjV2 = __jjstate.la;
  __jjV3 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV1 = true;
for (;;) {
  try {
name = Name();  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV0) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV1 = false;
    if (__jjV0) {
      __jjstate.la = __jjV2;
      __jjstate.laToken = __jjV3;
    }
  }
  if (__jjV0 || !__jjstate.guessing || !__jjV1) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV0;
if (!__jjV1) {
markerID = IndividualMarker();}
if (!__jjstate.guessing)
{
			if (name != null)
				return new NameDesignator(name);
			else
				{
				// If we just have a hash mark, we will create a new marker, taking it
				// to mean 'a previously unidentified individual'.
				if (markerID.equals("#"))
					{
			 	  marker = new Marker(markerSet, null);
					markerID = "#" + marker.getMarkerID();
		 	    getMarkerTable().mapForeignMarkerIDToNativeMarker(markerID, marker);
					}
				else
					{
			    marker = getMarkerTable().getNativeMarkerByForeignMarkerID(markerID);
			 	  if (marker == null)
				 	  {
					  marker = new Marker(markerSet, null);
			 	    getMarkerTable().mapForeignMarkerIDToNativeMarker(markerID, marker);
			 	  	}
					}


				return new MarkerDesignator(marker);
				}
			}return null;

		}


		/**
		 * Attempts to parse an individual marker from the input stream.
		 *
		 * @return the string used to indicate the marker created by parsing.
		 * @exception ParserException  if an error occurs while parsing.
		 *
		 * @bug We should do the individual marker handling here and return
		 * a MarkerDesignator.
		 */
	public String IndividualMarker() throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}com.metamata.parse.Token numberToken = null;{
			}

		__jjstate.ematch(HASH);

boolean __jjV1 = __jjstate.guessing;
int __jjV2 = 0;
com.metamata.parse.Token __jjV3 = null;
if (__jjV1) {
  __jjV2 = __jjstate.la;
  __jjV3 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV0 = true;
for (;;) {
  try {
numberToken = __jjstate.ematch(INTEGER_LITERAL);
  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV1) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV0 = false;
    if (__jjV1) {
      __jjstate.la = __jjV2;
      __jjstate.laToken = __jjV3;
    }
  }
  if (__jjV1 || !__jjstate.guessing || !__jjV0) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV1;
if (!__jjstate.guessing)
{
			if (numberToken == null)
				return "#";
			else
			  return "#" + numberToken.image;
			}return null;

		}


		/**
		 * Attempts to parse a concept comment from the input stream.
		 *
		 * @return the comment created by parsing.
		 * @exception ParserException  if an error occurs while parsing.
		 *
		 * @bug Must remove excess backslashes from final string.
		 */
	public String ConceptComment() throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}com.metamata.parse.Token commentToken;{
			}

		commentToken = __jjstate.ematch(CONCEPT_COMMENT);
if (!__jjstate.guessing)
{
			return commentToken.image.substring(1, commentToken.image.length() -1);
			}return null;

		}


/* Relation Rules */

		/**
		 * Attempts to parse a relation from the input stream.
		 *
		 * @param graph  the graph to which any related concepts should be added.
		 * @param definingLabelTable  the current defining label table.
		 * @return the relation created by parsing.
		 * @exception ParserException  if an error occurs while parsing.
		 *
		 * @bug No valence checking.  Probably should just be an exception thrown
		 * by the Relation class.  Actually, valence _is_ checked on construction.  
		 * Relation should probably be throwing something other than IllegalArgumentException.
		 */
	public Relation Relation(Graph graph, DefiningLabelTable definingLabelTable) throws ParserException, com.metamata.parse.ParseException 
		{
			{
			// Create vector with initial size of 5.  More than 5 arcs should be rare.
			}Vector arcVector = new Vector(5);
			Concept arcConcept, arguments[];
			String relComment = null;
			RelationType relationType = null;
			Relation rel;{
			}

		__jjstate.ematch(LEFT_PAREN);
boolean __jjV0 = __jjstate.guessing;
int __jjV2 = 0;
com.metamata.parse.Token __jjV3 = null;
if (__jjV0) {
  __jjV2 = __jjstate.la;
  __jjV3 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV1 = true;
for (;;) {
  try {
relationType = RelationTypeLabel();  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV0) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV1 = false;
    if (__jjV0) {
      __jjstate.la = __jjV2;
      __jjstate.laToken = __jjV3;
    }
  }
  if (__jjV0 || !__jjstate.guessing || !__jjV1) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV0;
if (!__jjV1) {
relationType = LambdaExpression();}

boolean __jjV4 = __jjstate.guessing;
int __jjV5 = 0;
com.metamata.parse.Token __jjV6 = null;
for (;;) {
if (__jjV4) {
  __jjV5 = __jjstate.la;
  __jjV6 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV7 = true;
for (;;) {
  try {
arcConcept = Arc(graph, definingLabelTable);if (!__jjstate.guessing)
{ arcVector.addElement(arcConcept); }  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV4) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV7 = false;
    if (__jjV4) {
      __jjstate.la = __jjV5;
      __jjstate.laToken = __jjV6;
    }
  }
  if (__jjV4 || !__jjstate.guessing || !__jjV7) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV4;
if (!__jjV7) {
  break;
}
}
boolean __jjV8 = __jjstate.guessing;
int __jjV10 = 0;
com.metamata.parse.Token __jjV11 = null;
if (__jjV8) {
  __jjV10 = __jjstate.la;
  __jjV11 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV9 = true;
for (;;) {
  try {
relComment = RelationComment();  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV8) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV9 = false;
    if (__jjV8) {
      __jjstate.la = __jjV10;
      __jjstate.laToken = __jjV11;
    }
  }
  if (__jjV8 || !__jjstate.guessing || !__jjV9) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV8;
if (!__jjV9) {
__jjstate.ematch(RIGHT_PAREN);
}
if (!__jjstate.guessing)
{
			arguments = new Concept[arcVector.size()];
			arcVector.copyInto(arguments);
			try
				{
				if (relationType == null)
				  rel = new Relation(arguments);
				else
					rel = new Relation(relationType, arguments);
				}
			catch(IllegalArgumentException e)
				{
				throw new ParserException("Number of arguments does not match specified valence of relation.", e);
				}
			rel.setComment(relComment);
			return rel;
			}return null;

		}


		/**
		 * Attempts to parse a relation type Label from the input stream.
		 *
		 * @return the relation type created or looked up by parsing.
		 * @exception ParserException  if an error occurs while parsing.
		 */
	public RelationType RelationTypeLabel()  throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}com.metamata.parse.Token labelToken = null;
			RelationType relationType;{
			}

		labelToken = __jjstate.ematch(IDENTIFIER);
if (!__jjstate.guessing)
{
			relationType = (RelationType)relationHierarchy.getTypeByLabel(labelToken.image);

			if (relationType == null)
				{
				// Must create the type in this case.  Assume universal and absurd as
				// parents and children.
				relationType = new RelationType(labelToken.image);
				try
					{
					relationHierarchy.addTypeToHierarchy(relationType);
					}
				catch (TypeAddError e)
					{
					// Have to do something better than this.
					e.printStackTrace();
					System.exit(1);
					}
				catch (TypeChangeError e)
					{
					// Have to do something better than this.
					e.printStackTrace();
					System.exit(1);
					}
				}

			return relationType;
			}return null;

		}


		/**
		 * Attempts to parse a lambda expression from the input stream.
		 *
		 * @return a relation type based on the parsed lambda expression.
		 * @exception ParserException  if an error occurs while parsing.
		 *
		 * @bug Need to fix some problems creating RelationTypeDefinitions
		 * for an unlabelled RelationType.
		 * @bug Do we really need to push a context here?  Really don't need to pop it since
		 * we just throw the table away.
		 */
	public RelationType LambdaExpression() throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}Graph expr = null;
			RelationType relationType = null;
			Vector formalLabelVec = new Vector(10);
			Vector formalTypeVec = new Vector(10);
			Token paramLabelToken;
			ConceptType paramType;
			DefiningLabelTable definingLabelTable = new DefiningLabelTable();{
			}

		__jjstate.ematch(LEFT_PAREN);
__jjstate.ematch(LAMBDA);
__jjstate.ematch(LEFT_PAREN);

boolean __jjV1 = __jjstate.guessing;
int __jjV2 = 0;
com.metamata.parse.Token __jjV3 = null;
if (__jjV1) {
  __jjV2 = __jjstate.la;
  __jjV3 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV0 = true;
for (;;) {
  try {
paramType = ConceptType();paramLabelToken = DefinedLabel();if (!__jjstate.guessing)
{
					formalLabelVec.addElement(paramLabelToken.image);
					formalTypeVec.addElement(paramType);
					}__jjstate.ematch(COMMA);
paramType = ConceptType();paramLabelToken = DefinedLabel();if (!__jjstate.guessing)
{
					formalLabelVec.addElement(paramLabelToken.image);
					formalTypeVec.addElement(paramType);
					}
boolean __jjV4 = __jjstate.guessing;
int __jjV5 = 0;
com.metamata.parse.Token __jjV6 = null;
for (;;) {
if (__jjV4) {
  __jjV5 = __jjstate.la;
  __jjV6 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV7 = true;
for (;;) {
  try {
__jjstate.ematch(COMMA);
paramType = ConceptType();paramLabelToken = DefinedLabel();if (!__jjstate.guessing)
{
					formalLabelVec.addElement(paramLabelToken.image);
					formalTypeVec.addElement(paramType);
					}  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV4) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV7 = false;
    if (__jjV4) {
      __jjstate.la = __jjV5;
      __jjstate.laToken = __jjV6;
    }
  }
  if (__jjV4 || !__jjstate.guessing || !__jjV7) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV4;
if (!__jjV7) {
  break;
}
}
  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV1) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV0 = false;
    if (__jjV1) {
      __jjstate.la = __jjV2;
      __jjstate.laToken = __jjV3;
    }
  }
  if (__jjV1 || !__jjstate.guessing || !__jjV0) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV1;
__jjstate.ematch(RIGHT_PAREN);
if (!__jjstate.guessing)
{
		// Push context on to stack
		definingLabelTable.pushContext();
		}
boolean __jjV9 = __jjstate.guessing;
int __jjV10 = 0;
com.metamata.parse.Token __jjV11 = null;
if (__jjV9) {
  __jjV10 = __jjstate.la;
  __jjV11 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV8 = true;
for (;;) {
  try {
expr = Graph(null, definingLabelTable);  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV9) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV8 = false;
    if (__jjV9) {
      __jjstate.la = __jjV10;
      __jjstate.laToken = __jjV11;
    }
  }
  if (__jjV9 || !__jjstate.guessing || !__jjV8) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV9;
__jjstate.ematch(RIGHT_PAREN);
if (!__jjstate.guessing)
{
			if (expr == null)
				{
				// Build type using signature only

				ConceptType signature[];

				signature = new ConceptType[formalTypeVec.size()];
				formalTypeVec.copyInto(signature);

				relationType = new RelationType(new RelationTypeDefinition(signature));
				}
			else
				{
				// Build type using graph and formal parameters

				Concept formalParameters[];
				ConceptType signature[];
				int paramCount;
				String defLabel;

				signature = new ConceptType[formalTypeVec.size()];
				formalTypeVec.copyInto(signature);

				formalParameters = new Concept[formalLabelVec.size()];
				paramCount = 0;
				for (Enumeration labelEnum = formalLabelVec.elements(); labelEnum.hasMoreElements();)
					{
					defLabel = (String)labelEnum.nextElement();
					formalParameters[paramCount] = definingLabelTable.getDefiningConceptByDefiningLabel(defLabel);
					if (formalParameters[paramCount] == null)
						{
						// No such defining concept, throw exception
						throw new ParserException("The formal parameter concept labelled \""+defLabel+"\" is not defined in the associated graph.");
						}
					else
						if (!formalParameters[paramCount].isEnclosedBy(expr))
							{
							// Defining concept not part of graph, throw exception
							throw new ParserException("The formal parameter concept labelled \""+defLabel+"\" is not part of the associated graph.");
							}

					paramCount++;
					}

				// Perform type check vs. signature
				for (int param = 0; param < signature.length; param++)
					{
					if (!formalParameters[param].getType().equals(signature[param]))
						{
						throw new ParserException("Type mismatch between signature and formal parameter: "+ (param + 1));
						}
					}

				relationType = new RelationType(new RelationTypeDefinition(formalParameters, expr));
				}

			try
				{
				relationHierarchy.addTypeToHierarchy(relationType);
				}
			catch (TypeAddError e)
				{
				// Have to do something better than this.
				e.printStackTrace();
				System.exit(1);
				}
			catch (TypeChangeError e)
				{
				// Have to do something better than this.
				e.printStackTrace();
				System.exit(1);
				}

			// Pop context from stack
			definingLabelTable.popContext();
			
			return relationType;
			}return null;

		}


		/**
		 * Attempts to parse a relational arc from the input stream.
		 *
		 * @param graph  the graph to which the concept should be added.
		 * @param definingLabelTable  the current defining label table.
		 * @return the concept created by parsing or by lookup via bound label.
		 * @exception ParserException  if an error occurs while parsing.
		 */
	public Concept Arc(Graph graph, DefiningLabelTable definingLabelTable) throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}Concept arcConcept = null;
			Concept defConcept = null;
			Token boundLabelToken = null;{
			}

		// Probably need some extra stuff here for literal concepts
		 boolean __jjV0 = __jjstate.guessing;
int __jjV2 = 0;
com.metamata.parse.Token __jjV3 = null;
if (__jjV0) {
  __jjV2 = __jjstate.la;
  __jjV3 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV1 = true;
for (;;) {
  try {
boundLabelToken = BoundLabel();  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV0) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV1 = false;
    if (__jjV0) {
      __jjstate.la = __jjV2;
      __jjstate.laToken = __jjV3;
    }
  }
  if (__jjV0 || !__jjstate.guessing || !__jjV1) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV0;
if (!__jjV1) {
arcConcept = Concept(graph, definingLabelTable);}
if (!__jjstate.guessing)
{
			// If we were given a bound label then we need to lookup the defining
			// concept for the corresponding coreference set or check in defining concept table.
			if (boundLabelToken != null)
				{
				defConcept = definingLabelTable.getDefiningConceptByDefiningLabel(boundLabelToken.image);

				// Check to see if it was found in the definingConceptTable
				if (defConcept == null)
					{
					// Not found in the definingConceptTable, so checking the definingLabelTable
					// Note: It shouldn't be there either unless imported with the context.
					CoreferenceSet corefSet;

					corefSet = definingLabelTable.getCoreferenceSetByDefiningLabel(boundLabelToken.image);
					if (corefSet == null)
						{
						// Unbound label.
						throw generateParserException("Bound reference to unbound label in relation: " + boundLabelToken.image, boundLabelToken);
						}

					defConcept = corefSet.getDefiningConcept();

					if (defConcept == null)
						{
						// Defining concept was never set, horrible horrible.  Should never happen.
						throw generateParserException("Can't find defining concept associated with bound label: " + boundLabelToken.image, boundLabelToken);
						}
					}

				// Check to see if defining concept is within the context of this relation
				if (defConcept.getEnclosingGraph() == graph)
					{
					// They are in the same context so we can use the defining concept
					arcConcept = defConcept;
					}
				else
					{
					// Defining concept is not in the same context
					// We must either find a coreferent concept within the same context or create one
					CoreferenceSet corefSets[];

					corefSets = defConcept.getCoreferenceSets();
					if (corefSets.length == 0)
						{
						// No coreference set so we create one and add both the dummy concept and the
						// defining concept.

						arcConcept = new Concept();
						graph.addConcept(arcConcept);
						processConceptBoundLabel(boundLabelToken, arcConcept, graph, definingLabelTable);
						}
					else
						{
						// Found a coreference set, check it for a coreferent concept in the same context
						Concept corefConcepts[];
						int con;

						corefConcepts = corefSets[0].getCoreferentConcepts();

						con = 0;
						while ((con < corefConcepts.length) && (arcConcept == null))
							{
							if (corefConcepts[con].getEnclosingGraph() == graph)
								{
								arcConcept = corefConcepts[con];
								}

							con++;
							}

						if (arcConcept == null)
							{
							// Did not find a suitable concept in the same context so we must create one
							// and add it to the coreference set.
							arcConcept = new Concept();
							graph.addConcept(arcConcept);
							processConceptBoundLabel(boundLabelToken, arcConcept, graph, definingLabelTable);
							}
						}
					}
				}

			return arcConcept;
			}return null;

		}


		/**
		 * Attempts to parse a relation comment from the input stream.
		 *
		 * @return the comment created by parsing.
		 * @exception ParserException  if an error occurs while parsing.
		 *
		 * @bug Must remove excess backslashes from final string.
		 */
	public String RelationComment() throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}com.metamata.parse.Token commentToken;{
			}

		commentToken = __jjstate.ematch(RELATION_COMMENT);
if (!__jjstate.guessing)
{
			return commentToken.image.substring(1, commentToken.image.length() -1);
			}return null;

		}


/* Actor Rules */

		/**
		 * Attempts to parse an actor from the input stream.
		 *
		 * @param graph  the graph to which any related concepts should be added.
		 * @param definingLabelTable  the current defining label table.
		 * @return the actor created by parsing.
		 * @exception ParserException  if an error occurs while parsing.
		 */
	public Actor Actor(Graph graph, DefiningLabelTable definingLabelTable) throws  ParserException, com.metamata.parse.ParseException 
		{
			{
			}Actor actor = null;
			RelationType relationType = null;
			Vector inArcsVec = new Vector(10);
			Vector outArcsVec = new Vector(10);
			Concept inArcs[] = null;
			Concept outArcs[] = null;
			Concept arc = null;
			String comment = null;{
			}

		__jjstate.ematch(LEFT_ANGLE);
boolean __jjV0 = __jjstate.guessing;
int __jjV2 = 0;
com.metamata.parse.Token __jjV3 = null;
if (__jjV0) {
  __jjV2 = __jjstate.la;
  __jjV3 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV1 = true;
for (;;) {
  try {
relationType = RelationTypeLabel();  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV0) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV1 = false;
    if (__jjV0) {
      __jjstate.la = __jjV2;
      __jjstate.laToken = __jjV3;
    }
  }
  if (__jjV0 || !__jjstate.guessing || !__jjV1) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV0;
if (!__jjV1) {
relationType = LambdaExpression();}

boolean __jjV4 = __jjstate.guessing;
int __jjV5 = 0;
com.metamata.parse.Token __jjV6 = null;
for (;;) {
if (__jjV4) {
  __jjV5 = __jjstate.la;
  __jjV6 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV7 = true;
for (;;) {
  try {
arc = Arc(graph, definingLabelTable);if (!__jjstate.guessing)
{
					inArcsVec.addElement(arc);
					}  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV4) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV7 = false;
    if (__jjV4) {
      __jjstate.la = __jjV5;
      __jjstate.laToken = __jjV6;
    }
  }
  if (__jjV4 || !__jjstate.guessing || !__jjV7) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV4;
if (!__jjV7) {
  break;
}
}
__jjstate.ematch(VERT_BAR);

boolean __jjV8 = __jjstate.guessing;
int __jjV9 = 0;
com.metamata.parse.Token __jjV10 = null;
for (;;) {
if (__jjV8) {
  __jjV9 = __jjstate.la;
  __jjV10 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV11 = true;
for (;;) {
  try {
arc = Arc(graph, definingLabelTable);if (!__jjstate.guessing)
{
					outArcsVec.addElement(arc);
					}  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV8) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV11 = false;
    if (__jjV8) {
      __jjstate.la = __jjV9;
      __jjstate.laToken = __jjV10;
    }
  }
  if (__jjV8 || !__jjstate.guessing || !__jjV11) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV8;
if (!__jjV11) {
  break;
}
}
boolean __jjV12 = __jjstate.guessing;
int __jjV14 = 0;
com.metamata.parse.Token __jjV15 = null;
if (__jjV12) {
  __jjV14 = __jjstate.la;
  __jjV15 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV13 = true;
for (;;) {
  try {
comment = ActorComment();  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV12) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV13 = false;
    if (__jjV12) {
      __jjstate.la = __jjV14;
      __jjstate.laToken = __jjV15;
    }
  }
  if (__jjV12 || !__jjstate.guessing || !__jjV13) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV12;
if (!__jjV13) {
__jjstate.ematch(RIGHT_ANGLE);
}
if (!__jjstate.guessing)
{
			inArcs = new Concept[inArcsVec.size()];
			inArcsVec.copyInto(inArcs);
			outArcs = new Concept[outArcsVec.size()];
			outArcsVec.copyInto(outArcs);
			actor = new Actor(relationType, inArcs, outArcs);
			if (comment != null)
				actor.setComment(comment);

			return actor;
			}return null;

		}


		/**
		 * Attempts to parse a actor comment from the input stream.
		 *
		 * @return the comment created by parsing.
		 * @exception ParserException  if an error occurs while parsing.
		 *
		 * @bug Must remove excess backslashes from final string.
		 */
	public String ActorComment() throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}com.metamata.parse.Token commentToken;{
			}

		commentToken = __jjstate.ematch(ACTOR_COMMENT);
if (!__jjstate.guessing)
{
			return commentToken.image.substring(1, commentToken.image.length() -1);
			}return null;

		}



/* Rules for primitives */

		/**
		 * Attempts to parse a name from the input stream.
		 *
		 * @return the name created by parsing.
		 * @exception ParserException  if an error occurs while parsing.
		 *
		 * @bug Need to add support for unquoted names, if that ends up in the standard.
		 */
	public String Name() throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}com.metamata.parse.Token nameToken;{
			}

		nameToken = __jjstate.ematch(NAME_LITERAL);
if (!__jjstate.guessing)
{
			return translateEscapeSequences(nameToken.image.substring(1, nameToken.image.length() - 1));
			}return null;

		}


		/**
		 * Attempts to parse a number from the input stream.
		 *
		 * @return the number created by parsing.
		 * @exception ParserException  if an error occurs while parsing.
		 *
		 * @bug Currently all numbers are converted to doubles.  Might want to be
		 * more intelligent about this.
		 */
	public Number Number() throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}Number number = null;
			com.metamata.parse.Token intToken = null, floatToken = null;{
			}

		boolean __jjV0 = __jjstate.guessing;
int __jjV2 = 0;
com.metamata.parse.Token __jjV3 = null;
if (__jjV0) {
  __jjV2 = __jjstate.la;
  __jjV3 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV1 = true;
for (;;) {
  try {
intToken = __jjstate.ematch(INTEGER_LITERAL);
  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV0) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV1 = false;
    if (__jjV0) {
      __jjstate.la = __jjV2;
      __jjstate.laToken = __jjV3;
    }
  }
  if (__jjV0 || !__jjstate.guessing || !__jjV1) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV0;
if (!__jjV1) {
floatToken = __jjstate.ematch(FLOATING_POINT_LITERAL);
}
if (!__jjstate.guessing)
{
		 try
			 {
			 if (intToken != null)
				number = new Long(intToken.image);
			 else
				 number = new Double(floatToken.image);
			 }
		 catch (NumberFormatException e)
			 {
			 throw new ParserException("Invalid literal number format.", e);
			 }

		 return number;
		 }return null;

		}


		/**
		 * Attempts to parse a string from the input stream.
		 *
		 * @return the string created by parsing.
		 * @exception ParserException  if an error occurs while parsing.
		 */
	public String String() throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}com.metamata.parse.Token strToken;{
			}

		strToken = __jjstate.ematch(STRING_LITERAL);
if (!__jjstate.guessing)
{
			return translateEscapeSequences(strToken.image.substring(1, strToken.image.length() - 1));
			}return null;

		}


	/* ** Support methods for parsing ** */

		/**
		 * Performs the necessary processing on a concept's defining label.
		 *
		 * @param defLabelToken  the defining label token.
		 * @param newConcept  the newly created concept.
		 * @param graph  the graph to which the concept should have been added.
		 */
	private final void processConceptDefiningLabel(Token defLabelToken, Concept newConcept, Graph graph, DefiningLabelTable definingLabelTable) throws ParserException
		{
		// Check for duplicate coreference label in coref sets or defining concepts.
		if ((definingLabelTable.getCoreferenceSetByDefiningLabel(defLabelToken.image) != null) ||
			(definingLabelTable.getDefiningConceptByDefiningLabel(defLabelToken.image) != null))
			{
			// Duplicate coreference label definition.  Choke and die.
			// Problem here is that we're not sure when to clear the corefTable.
			throw generateParserException("Duplicate defining label.", defLabelToken);
			}

		// When we see a defining label, add it to the defining concept table
		// If it's later seen as a bound label in another concept, then we create a
		// coreference set for it and add it.
		// If it's later seen as a bound label in a relation, we can reference it.
		definingLabelTable.mapDefiningLabelToDefiningConcept(defLabelToken.image, newConcept);
		}


		/**
		 * Performs the necessary processing on a concept's bound label.
		 *
		 * @param boundLabelToken  the bound label token.
		 * @param newConcept  the newly created concept.
		 * @param graph  the graph to which the concept should have been added.
		 */
	private final void processConceptBoundLabel(Token boundLabelToken, Concept newConcept, Graph graph, DefiningLabelTable definingLabelTable) throws ParserException
		{
		CoreferenceSet corefSet;

		// Check to see if there is a coref set corresponding to the bound label
		corefSet = definingLabelTable.getCoreferenceSetByDefiningLabel(boundLabelToken.image);
		if (corefSet == null)
			{
			Concept definingConcept;

			// Check to see if there is a defining concept corresponding to the bound label
			definingConcept = definingLabelTable.getDefiningConceptByDefiningLabel(boundLabelToken.image);

			if (definingConcept == null)
				{
				// Bound label reference without a corresponding corefSet in the table
				// Spooky... means either bad syntax (labels must be defined first) or a
				// problem in the table.
				throw generateParserException("Bound reference to unbound label: " + boundLabelToken.image, boundLabelToken);
				}
			else
				{
				// This must be the first bound label occurance since establishing the defining
				// concept.  Therefore, we must create a new CoreferenceSet and add the
				// defining concept to it.

				corefSet = new CoreferenceSet();
				try
					{
					// Disable scope checking if the concept is not part of a graph
					if (graph == null)
						{
				    // Disable coreference set's scope checking
			  	  try
			    		{
							corefSet.setEnableScopeChecking(false);
							}
						catch (CorefAddException e1)
							{
							// Can't happen when we're setting it to false.
							}
						catch (InvalidDefiningConceptException e2)
							{
							// Can't happen when we're setting it to false.
							}
						}

					corefSet.addCoreferentConcept(definingConcept);
					}
				catch (CorefAddException e)
					{
					// Should never happen since this is an empty coref set.
					throw new ParserException("Problem adding defining concept to coreference set.", e);
					}

				try
					{
		  	  corefSet.setDefiningConcept(definingConcept);
			    }
				catch (InvalidDefiningConceptException e)
					{
					// Should never happen since this is an empty coref set.
					throw new ParserException("Problem setting defining concept in coreference set.", e);
					}

				// Add newly defined coreference set to table
				definingLabelTable.mapDefiningLabelToCoreferenceSet(boundLabelToken.image, corefSet);
				}
			}

		// Having found or created the coreference set associated with this bound label,
		// we add the new concept to the set.
		try
			{
			// Disable scope checking if the concept is not part of a graph
			if (graph == null)
				{
		    // Disable coreference set's scope checking
		    try
		    	{
					corefSet.setEnableScopeChecking(false);
					}
				catch (CorefAddException e1)
					{
					// Can't happen when we're setting it to false.
					}
				catch (InvalidDefiningConceptException e2)
					{
					// Can't happen when we're setting it to false.
					}
				}

			corefSet.addCoreferentConcept(newConcept);
			}
		catch (CorefAddException ex)
			{
			throw new ParserException("Problem adding concept to coreference set.", ex);
			}
		}
				
		
		/**
		 * Creates and/or returns the "Neg" relation type.
		 *
		 * @return  the "Neg" relation type.
		 */
	private final RelationType establishNegRelationType() throws ParserException
		{
		RelationType negType;
		
		// Lookup Neg relation type.  Create it if necessary and allowed.
		negType = relationHierarchy.getTypeByLabel(NEG_TYPE_LABEL);
		
		if (negType == null)
			{
			if (createTypesOnDemand)
				{
				negType = new RelationType(NEG_TYPE_LABEL);
				try
					{
					relationHierarchy.addTypeToHierarchy(negType);
					}
				catch (TypeAddError e)
					{
					throw new ParserException("Error adding auto-created "+NEG_TYPE_LABEL+" relation type.", e);
					}
				catch (TypeChangeError e)
					{
					// Note: This should never happen since we aren't specifying any parents or children.
					throw new ParserException("Error adding auto-created "+NEG_TYPE_LABEL+" relation type.", e);
					}
				}
			else
				{
				throw new ParserException(NEG_TYPE_LABEL+" relation type is missing in relation type hierarchy.");
				}
			}

		return negType;
		}

	}

package notio.translators;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import notio.*;
import com.metamata.parse.*;



  /**
   * LF Parser implementation based on a Metamata Parse grammar.
   * This class should never be used directly.  Use LFParser instead.
   *
   * @author Finnegan Southey
   * @version $Name:  $ $Revision: 1.14 $, $Date: 1999/08/01 22:17:43 $
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
   * @see notio.translators.LFParser
   */
class LFParserImpl
	{
		/** Name of DefiningLabelTable unit in context. **/
	
  public interface ScannerConstants {
  
    int EOF = 0;
    int NEWLINE = 5;
    int LAMBDA = 6;
    int SCOPE = 7;
    int TYPEMACRO = 8;
    int LEFT_PAREN = 9;
    int RIGHT_PAREN = 10;
    int LEFT_BRACKET = 11;
    int RIGHT_BRACKET = 12;
    int LEFT_BRACE = 13;
    int RIGHT_BRACE = 14;
    int LEFT_ARROW = 15;
    int RIGHT_ARROW = 16;
    int LEFT_ANGLE = 17;
    int RIGHT_ANGLE = 18;
    int COMMA = 19;
    int PERIOD = 20;
    int COLON = 21;
    int SEMICOLON = 22;
    int QUESTIONMARK = 23;
    int HYPHEN = 24;
    int TILDE = 25;
    int AT = 26;
    int HASH = 27;
    int ASTERISK = 28;
    int VERT_BAR = 29;
    int CONCEPT_COMMENT = 30;
    int RELATION_COMMENT = 31;
    int ACTOR_COMMENT = 32;
    int FORMAL_COMMENT = 35;
    int MULTI_LINE_COMMENT = 36;
    int INTEGER_LITERAL = 38;
    int DECIMAL_LITERAL = 39;
    int HEX_LITERAL = 40;
    int OCTAL_LITERAL = 41;
    int FLOATING_POINT_LITERAL = 42;
    int EXPONENT = 43;
    int CHARACTER_LITERAL = 44;
    int STRING_LITERAL = 45;
    int NAME_LITERAL = 46;
    int IDENTIFIER = 47;
    int LETTER = 48;
    int DIGIT = 49;
    int __jjV_K = NEWLINE;
    int __jjV_iBlambda = LAMBDA;
    int __jjV_1CScope = SCOPE;
    int __jjV_iBtypeMacro = TYPEMACRO;
    int __jjV_iB = LEFT_PAREN;
    int __jjV_jB = RIGHT_PAREN;
    int __jjV_1C = LEFT_BRACKET;
    int __jjV_3C = RIGHT_BRACKET;
    int __jjV_1D = LEFT_BRACE;
    int __jjV_3D = RIGHT_BRACE;
    int __jjV_2B_nB = LEFT_ARROW;
    int __jjV_nB_4B = RIGHT_ARROW;
    int __jjV_2B = LEFT_ANGLE;
    int __jjV_4B = RIGHT_ANGLE;
    int __jjV_mB = COMMA;
    int __jjV_oB = PERIOD;
    int __jjV_0B = COLON;
    int __jjV_1B = SEMICOLON;
    int __jjV_5B = QUESTIONMARK;
    int __jjV_nB = HYPHEN;
    int __jjV_4D = TILDE;
    int __jjV_aC = AT;
    int __jjV_dB = HASH;
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
      "\"\\f\"",
      "\"\\n\"",
      "\"(lambda\"",
      "\"[Scope\"",
      "\"(typeMacro\"",
      "\"(\"",
      "\")\"",
      "\"[\"",
      "\"]\"",
      "\"{\"",
      "\"}\"",
      "\"<-\"",
      "\"->\"",
      "\"<\"",
      "\">\"",
      "\",\"",
      "\".\"",
      "\":\"",
      "\";\"",
      "\"?\"",
      "\"-\"",
      "\"~\"",
      "\"@\"",
      "\"#\"",
      "\"*\"",
      "\"|\"",
      "<CONCEPT_COMMENT>",
      "<RELATION_COMMENT>",
      "<ACTOR_COMMENT>",
      "<token of kind 33>",
      "\"/*\"",
      "\"*/\"",
      "\"*/\"",
      "<token of kind 37>",
      "<INTEGER_LITERAL>",
      "<DECIMAL_LITERAL>",
      "<HEX_LITERAL>",
      "<OCTAL_LITERAL>",
      "<FLOATING_POINT_LITERAL>",
      "<EXPONENT>",
      "<CHARACTER_LITERAL>",
      "<STRING_LITERAL>",
      "<NAME_LITERAL>",
      "<IDENTIFIER>",
      "<LETTER>",
      "<DIGIT>",
    };
  
  }
  public static final int EOF = 0;
  public static final int NEWLINE = 5;
  public static final int LAMBDA = 6;
  public static final int SCOPE = 7;
  public static final int TYPEMACRO = 8;
  public static final int LEFT_PAREN = 9;
  public static final int RIGHT_PAREN = 10;
  public static final int LEFT_BRACKET = 11;
  public static final int RIGHT_BRACKET = 12;
  public static final int LEFT_BRACE = 13;
  public static final int RIGHT_BRACE = 14;
  public static final int LEFT_ARROW = 15;
  public static final int RIGHT_ARROW = 16;
  public static final int LEFT_ANGLE = 17;
  public static final int RIGHT_ANGLE = 18;
  public static final int COMMA = 19;
  public static final int PERIOD = 20;
  public static final int COLON = 21;
  public static final int SEMICOLON = 22;
  public static final int QUESTIONMARK = 23;
  public static final int HYPHEN = 24;
  public static final int TILDE = 25;
  public static final int AT = 26;
  public static final int HASH = 27;
  public static final int ASTERISK = 28;
  public static final int VERT_BAR = 29;
  public static final int CONCEPT_COMMENT = 30;
  public static final int RELATION_COMMENT = 31;
  public static final int ACTOR_COMMENT = 32;
  public static final int FORMAL_COMMENT = 35;
  public static final int MULTI_LINE_COMMENT = 36;
  public static final int INTEGER_LITERAL = 38;
  public static final int DECIMAL_LITERAL = 39;
  public static final int HEX_LITERAL = 40;
  public static final int OCTAL_LITERAL = 41;
  public static final int FLOATING_POINT_LITERAL = 42;
  public static final int EXPONENT = 43;
  public static final int CHARACTER_LITERAL = 44;
  public static final int STRING_LITERAL = 45;
  public static final int NAME_LITERAL = 46;
  public static final int IDENTIFIER = 47;
  public static final int LETTER = 48;
  public static final int DIGIT = 49;
  public static final int __jjV_K = NEWLINE;
  public static final int __jjV_iBlambda = LAMBDA;
  public static final int __jjV_1CScope = SCOPE;
  public static final int __jjV_iBtypeMacro = TYPEMACRO;
  public static final int __jjV_iB = LEFT_PAREN;
  public static final int __jjV_jB = RIGHT_PAREN;
  public static final int __jjV_1C = LEFT_BRACKET;
  public static final int __jjV_3C = RIGHT_BRACKET;
  public static final int __jjV_1D = LEFT_BRACE;
  public static final int __jjV_3D = RIGHT_BRACE;
  public static final int __jjV_2B_nB = LEFT_ARROW;
  public static final int __jjV_nB_4B = RIGHT_ARROW;
  public static final int __jjV_2B = LEFT_ANGLE;
  public static final int __jjV_4B = RIGHT_ANGLE;
  public static final int __jjV_mB = COMMA;
  public static final int __jjV_oB = PERIOD;
  public static final int __jjV_0B = COLON;
  public static final int __jjV_1B = SEMICOLON;
  public static final int __jjV_5B = QUESTIONMARK;
  public static final int __jjV_nB = HYPHEN;
  public static final int __jjV_4D = TILDE;
  public static final int __jjV_aC = AT;
  public static final int __jjV_dB = HASH;
  public static final int __jjV_kB = ASTERISK;
  public static final int __jjV_2D = VERT_BAR;
  public static final int DEFAULT = 0;
  public static final int IN_FORMAL_COMMENT = 1;
  public static final int IN_MULTI_LINE_COMMENT = 2;
  public static final String[] tokenImage = ScannerConstants.tokenImage;

  public class __jjScanner extends com.metamata.parse.Scanner {

    private com.metamata.parse.MParseReader __jjcs;
    private __jjLFParserImplTokenManager __jjtm;

    public __jjScanner(java.io.Reader input) {
      __jjcs = input instanceof com.metamata.parse.MParseReader ? (com.metamata.parse.MParseReader)input : new com.metamata.parse.SimpleReader(input, 1, 1);
      __jjtm = new __jjLFParserImplTokenManager(__jjcs);
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

     class __jjLFParserImplTokenManager 
    {
    private final int jjStopStringLiteralDfa_0(int pos, long active0)
    {
       switch (pos)
       {
          case 0:
             if ((active0 & 0x400000000L) != 0L)
                return 2;
             if ((active0 & 0x100000L) != 0L)
                return 8;
             return -1;
          case 1:
             if ((active0 & 0x400000000L) != 0L)
                return 0;
             return -1;
          default :
             return -1;
       }
    }
    private final int jjStartNfa_0(int pos, long active0)
    {
       return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
    }
    private final int jjStopAtPos(int pos, int kind)
    {
       jjmatchedKind = kind;
       jjmatchedPos = pos;
       return pos + 1;
    }
    private final int jjStartNfaWithStates_0(int pos, int kind, int state)
    {
       jjmatchedKind = kind;
       jjmatchedPos = pos;
       try { curChar = input_stream.readChar(); }
       catch(java.io.IOException e) { return pos + 1; }
       return jjMoveNfa_0(state, pos + 1);
    }
    private final int jjMoveStringLiteralDfa0_0()
    {
       switch(curChar)
       {
          case 10:
             return jjStopAtPos(0, 5);
          case 35:
             return jjStopAtPos(0, 27);
          case 40:
             jjmatchedKind = 9;
             return jjMoveStringLiteralDfa1_0(0x140L);
          case 41:
             return jjStopAtPos(0, 10);
          case 42:
             return jjStopAtPos(0, 28);
          case 44:
             return jjStopAtPos(0, 19);
          case 45:
             jjmatchedKind = 24;
             return jjMoveStringLiteralDfa1_0(0x10000L);
          case 46:
             return jjStartNfaWithStates_0(0, 20, 8);
          case 47:
             return jjMoveStringLiteralDfa1_0(0x400000000L);
          case 58:
             return jjStopAtPos(0, 21);
          case 60:
             jjmatchedKind = 17;
             return jjMoveStringLiteralDfa1_0(0x8000L);
          case 62:
             return jjStopAtPos(0, 18);
          case 63:
             return jjStopAtPos(0, 23);
          case 64:
             return jjStopAtPos(0, 26);
          case 91:
             jjmatchedKind = 11;
             return jjMoveStringLiteralDfa1_0(0x80L);
          case 93:
             return jjStopAtPos(0, 12);
          case 123:
             return jjStopAtPos(0, 13);
          case 124:
             return jjStopAtPos(0, 29);
          case 125:
             return jjStopAtPos(0, 14);
          case 126:
             return jjStopAtPos(0, 25);
          default :
             return jjMoveNfa_0(3, 0);
       }
    }
    private final int jjMoveStringLiteralDfa1_0(long active0)
    {
       try { curChar = input_stream.readChar(); }
       catch(java.io.IOException e) {
          jjStopStringLiteralDfa_0(0, active0);
          return 1;
       }
       switch(curChar)
       {
          case 42:
             if ((active0 & 0x400000000L) != 0L)
                return jjStartNfaWithStates_0(1, 34, 0);
             break;
          case 45:
             if ((active0 & 0x8000L) != 0L)
                return jjStopAtPos(1, 15);
             break;
          case 62:
             if ((active0 & 0x10000L) != 0L)
                return jjStopAtPos(1, 16);
             break;
          case 83:
             return jjMoveStringLiteralDfa2_0(active0, 0x80L);
          case 108:
             return jjMoveStringLiteralDfa2_0(active0, 0x40L);
          case 116:
             return jjMoveStringLiteralDfa2_0(active0, 0x100L);
          default :
             break;
       }
       return jjStartNfa_0(0, active0);
    }
    private final int jjMoveStringLiteralDfa2_0(long old0, long active0)
    {
       if (((active0 &= old0)) == 0L)
          return jjStartNfa_0(0, old0); 
       try { curChar = input_stream.readChar(); }
       catch(java.io.IOException e) {
          jjStopStringLiteralDfa_0(1, active0);
          return 2;
       }
       switch(curChar)
       {
          case 97:
             return jjMoveStringLiteralDfa3_0(active0, 0x40L);
          case 99:
             return jjMoveStringLiteralDfa3_0(active0, 0x80L);
          case 121:
             return jjMoveStringLiteralDfa3_0(active0, 0x100L);
          default :
             break;
       }
       return jjStartNfa_0(1, active0);
    }
    private final int jjMoveStringLiteralDfa3_0(long old0, long active0)
    {
       if (((active0 &= old0)) == 0L)
          return jjStartNfa_0(1, old0); 
       try { curChar = input_stream.readChar(); }
       catch(java.io.IOException e) {
          jjStopStringLiteralDfa_0(2, active0);
          return 3;
       }
       switch(curChar)
       {
          case 109:
             return jjMoveStringLiteralDfa4_0(active0, 0x40L);
          case 111:
             return jjMoveStringLiteralDfa4_0(active0, 0x80L);
          case 112:
             return jjMoveStringLiteralDfa4_0(active0, 0x100L);
          default :
             break;
       }
       return jjStartNfa_0(2, active0);
    }
    private final int jjMoveStringLiteralDfa4_0(long old0, long active0)
    {
       if (((active0 &= old0)) == 0L)
          return jjStartNfa_0(2, old0); 
       try { curChar = input_stream.readChar(); }
       catch(java.io.IOException e) {
          jjStopStringLiteralDfa_0(3, active0);
          return 4;
       }
       switch(curChar)
       {
          case 98:
             return jjMoveStringLiteralDfa5_0(active0, 0x40L);
          case 101:
             return jjMoveStringLiteralDfa5_0(active0, 0x100L);
          case 112:
             return jjMoveStringLiteralDfa5_0(active0, 0x80L);
          default :
             break;
       }
       return jjStartNfa_0(3, active0);
    }
    private final int jjMoveStringLiteralDfa5_0(long old0, long active0)
    {
       if (((active0 &= old0)) == 0L)
          return jjStartNfa_0(3, old0); 
       try { curChar = input_stream.readChar(); }
       catch(java.io.IOException e) {
          jjStopStringLiteralDfa_0(4, active0);
          return 5;
       }
       switch(curChar)
       {
          case 77:
             return jjMoveStringLiteralDfa6_0(active0, 0x100L);
          case 100:
             return jjMoveStringLiteralDfa6_0(active0, 0x40L);
          case 101:
             if ((active0 & 0x80L) != 0L)
                return jjStopAtPos(5, 7);
             break;
          default :
             break;
       }
       return jjStartNfa_0(4, active0);
    }
    private final int jjMoveStringLiteralDfa6_0(long old0, long active0)
    {
       if (((active0 &= old0)) == 0L)
          return jjStartNfa_0(4, old0); 
       try { curChar = input_stream.readChar(); }
       catch(java.io.IOException e) {
          jjStopStringLiteralDfa_0(5, active0);
          return 6;
       }
       switch(curChar)
       {
          case 97:
             if ((active0 & 0x40L) != 0L)
                return jjStopAtPos(6, 6);
             return jjMoveStringLiteralDfa7_0(active0, 0x100L);
          default :
             break;
       }
       return jjStartNfa_0(5, active0);
    }
    private final int jjMoveStringLiteralDfa7_0(long old0, long active0)
    {
       if (((active0 &= old0)) == 0L)
          return jjStartNfa_0(5, old0); 
       try { curChar = input_stream.readChar(); }
       catch(java.io.IOException e) {
          jjStopStringLiteralDfa_0(6, active0);
          return 7;
       }
       switch(curChar)
       {
          case 99:
             return jjMoveStringLiteralDfa8_0(active0, 0x100L);
          default :
             break;
       }
       return jjStartNfa_0(6, active0);
    }
    private final int jjMoveStringLiteralDfa8_0(long old0, long active0)
    {
       if (((active0 &= old0)) == 0L)
          return jjStartNfa_0(6, old0); 
       try { curChar = input_stream.readChar(); }
       catch(java.io.IOException e) {
          jjStopStringLiteralDfa_0(7, active0);
          return 8;
       }
       switch(curChar)
       {
          case 114:
             return jjMoveStringLiteralDfa9_0(active0, 0x100L);
          default :
             break;
       }
       return jjStartNfa_0(7, active0);
    }
    private final int jjMoveStringLiteralDfa9_0(long old0, long active0)
    {
       if (((active0 &= old0)) == 0L)
          return jjStartNfa_0(7, old0); 
       try { curChar = input_stream.readChar(); }
       catch(java.io.IOException e) {
          jjStopStringLiteralDfa_0(8, active0);
          return 9;
       }
       switch(curChar)
       {
          case 111:
             if ((active0 & 0x100L) != 0L)
                return jjStopAtPos(9, 8);
             break;
          default :
             break;
       }
       return jjStartNfa_0(8, active0);
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
       int[] nextStates;
       int startsAt = 0;
       jjnewStateCnt = 76;
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
                      else if (curChar == 39)
                         jjCheckNAddStates(7, 11);
                      else if (curChar == 59)
                         jjCheckNAddStates(12, 20);
                      else if (curChar == 36)
                      {
                         if (kind > 47)
                            kind = 47;
                         jjCheckNAdd(23);
                      }
                      else if (curChar == 34)
                         jjCheckNAddStates(21, 23);
                      else if (curChar == 46)
                         jjCheckNAdd(8);
                      else if (curChar == 47)
                         jjstateSet[jjnewStateCnt++] = 2;
                      if ((0x3fe000000000000L & l) != 0L)
                      {
                         if (kind > 38)
                            kind = 38;
                         jjCheckNAddTwoStates(5, 6);
                      }
                      else if (curChar == 48)
                      {
                         if (kind > 38)
                            kind = 38;
                         jjCheckNAddStates(24, 26);
                      }
                      break;
                   case 0:
                      if (curChar == 42)
                         jjstateSet[jjnewStateCnt++] = 1;
                      break;
                   case 1:
                      if ((0xffff7fffffffffffL & l) != 0L && kind > 33)
                         kind = 33;
                      break;
                   case 2:
                      if (curChar == 42)
                         jjstateSet[jjnewStateCnt++] = 0;
                      break;
                   case 4:
                      if ((0x3fe000000000000L & l) == 0L)
                         break;
                      if (kind > 38)
                         kind = 38;
                      jjCheckNAddTwoStates(5, 6);
                      break;
                   case 5:
                      if ((0x3ff000000000000L & l) == 0L)
                         break;
                      if (kind > 38)
                         kind = 38;
                      jjCheckNAddTwoStates(5, 6);
                      break;
                   case 7:
                      if (curChar == 46)
                         jjCheckNAdd(8);
                      break;
                   case 8:
                      if ((0x3ff000000000000L & l) == 0L)
                         break;
                      if (kind > 42)
                         kind = 42;
                      jjCheckNAddStates(27, 29);
                      break;
                   case 10:
                      if ((0x280000000000L & l) != 0L)
                         jjCheckNAdd(11);
                      break;
                   case 11:
                      if ((0x3ff000000000000L & l) == 0L)
                         break;
                      if (kind > 42)
                         kind = 42;
                      jjCheckNAddTwoStates(11, 12);
                      break;
                   case 13:
                      if (curChar == 34)
                         jjCheckNAddStates(21, 23);
                      break;
                   case 14:
                      if ((0xfffffffbffffdbffL & l) != 0L)
                         jjCheckNAddStates(21, 23);
                      break;
                   case 16:
                      if ((0x8400000000L & l) != 0L)
                         jjCheckNAddStates(21, 23);
                      break;
                   case 17:
                      if (curChar == 34 && kind > 45)
                         kind = 45;
                      break;
                   case 18:
                      if ((0xff000000000000L & l) != 0L)
                         jjCheckNAddStates(30, 33);
                      break;
                   case 19:
                      if ((0xff000000000000L & l) != 0L)
                         jjCheckNAddStates(21, 23);
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
                      if (curChar != 36)
                         break;
                      if (kind > 47)
                         kind = 47;
                      jjCheckNAdd(23);
                      break;
                   case 23:
                      if ((0x3ff001000000000L & l) == 0L)
                         break;
                      if (kind > 47)
                         kind = 47;
                      jjCheckNAdd(23);
                      break;
                   case 24:
                      if ((0x3ff000000000000L & l) != 0L)
                         jjCheckNAddStates(0, 6);
                      break;
                   case 25:
                      if ((0x3ff000000000000L & l) != 0L)
                         jjCheckNAddTwoStates(25, 26);
                      break;
                   case 26:
                      if (curChar != 46)
                         break;
                      if (kind > 42)
                         kind = 42;
                      jjCheckNAddStates(34, 36);
                      break;
                   case 27:
                      if ((0x3ff000000000000L & l) == 0L)
                         break;
                      if (kind > 42)
                         kind = 42;
                      jjCheckNAddStates(34, 36);
                      break;
                   case 29:
                      if ((0x280000000000L & l) != 0L)
                         jjCheckNAdd(30);
                      break;
                   case 30:
                      if ((0x3ff000000000000L & l) == 0L)
                         break;
                      if (kind > 42)
                         kind = 42;
                      jjCheckNAddTwoStates(30, 12);
                      break;
                   case 31:
                      if ((0x3ff000000000000L & l) != 0L)
                         jjCheckNAddTwoStates(31, 32);
                      break;
                   case 33:
                      if ((0x280000000000L & l) != 0L)
                         jjCheckNAdd(34);
                      break;
                   case 34:
                      if ((0x3ff000000000000L & l) == 0L)
                         break;
                      if (kind > 42)
                         kind = 42;
                      jjCheckNAddTwoStates(34, 12);
                      break;
                   case 35:
                      if ((0x3ff000000000000L & l) != 0L)
                         jjCheckNAddStates(37, 39);
                      break;
                   case 37:
                      if ((0x280000000000L & l) != 0L)
                         jjCheckNAdd(38);
                      break;
                   case 38:
                      if ((0x3ff000000000000L & l) != 0L)
                         jjCheckNAddTwoStates(38, 12);
                      break;
                   case 39:
                      if (curChar != 48)
                         break;
                      if (kind > 38)
                         kind = 38;
                      jjCheckNAddStates(24, 26);
                      break;
                   case 41:
                      if ((0x3ff000000000000L & l) == 0L)
                         break;
                      if (kind > 38)
                         kind = 38;
                      jjCheckNAddTwoStates(41, 6);
                      break;
                   case 42:
                      if ((0xff000000000000L & l) == 0L)
                         break;
                      if (kind > 38)
                         kind = 38;
                      jjCheckNAddTwoStates(42, 6);
                      break;
                   case 43:
                      if (curChar == 59)
                         jjCheckNAddStates(12, 20);
                      break;
                   case 44:
                      if ((0xbffffdffffffffffL & l) != 0L)
                         jjCheckNAddStates(40, 42);
                      break;
                   case 49:
                      if ((0xbffffdffffffffffL & l) != 0L)
                         jjCheckNAddStates(43, 45);
                      break;
                   case 50:
                      if (curChar == 41 && kind > 31)
                         kind = 31;
                      break;
                   case 52:
                      if (curChar == 41)
                         jjCheckNAddStates(43, 45);
                      break;
                   case 54:
                      if ((0xbffffdffffffffffL & l) != 0L)
                         jjCheckNAddStates(46, 48);
                      break;
                   case 55:
                      if (curChar == 62 && kind > 32)
                         kind = 32;
                      break;
                   case 57:
                      if (curChar == 62)
                         jjCheckNAddStates(46, 48);
                      break;
                   case 59:
                      if (curChar == 39)
                         jjCheckNAddStates(7, 11);
                      break;
                   case 60:
                      if ((0xffffff7fffffdbffL & l) != 0L)
                         jjCheckNAdd(61);
                      break;
                   case 61:
                      if (curChar == 39 && kind > 44)
                         kind = 44;
                      break;
                   case 63:
                      if ((0x8400000000L & l) != 0L)
                         jjCheckNAdd(61);
                      break;
                   case 64:
                      if ((0xff000000000000L & l) != 0L)
                         jjCheckNAddTwoStates(65, 61);
                      break;
                   case 65:
                      if ((0xff000000000000L & l) != 0L)
                         jjCheckNAdd(61);
                      break;
                   case 66:
                      if ((0xf000000000000L & l) != 0L)
                         jjstateSet[jjnewStateCnt++] = 67;
                      break;
                   case 67:
                      if ((0xff000000000000L & l) != 0L)
                         jjCheckNAdd(65);
                      break;
                   case 68:
                      if ((0xffffff7fffffdbffL & l) != 0L)
                         jjCheckNAddStates(49, 51);
                      break;
                   case 70:
                      if ((0x8400000000L & l) != 0L)
                         jjCheckNAddStates(49, 51);
                      break;
                   case 71:
                      if (curChar == 39 && kind > 46)
                         kind = 46;
                      break;
                   case 72:
                      if ((0xff000000000000L & l) != 0L)
                         jjCheckNAddStates(52, 55);
                      break;
                   case 73:
                      if ((0xff000000000000L & l) != 0L)
                         jjCheckNAddStates(49, 51);
                      break;
                   case 74:
                      if ((0xf000000000000L & l) != 0L)
                         jjstateSet[jjnewStateCnt++] = 75;
                      break;
                   case 75:
                      if ((0xff000000000000L & l) != 0L)
                         jjCheckNAdd(73);
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
                   case 23:
                      if ((0x7fffffe87fffffeL & l) == 0L)
                         break;
                      if (kind > 47)
                         kind = 47;
                      jjCheckNAdd(23);
                      break;
                   case 1:
                      if (kind > 33)
                         kind = 33;
                      break;
                   case 6:
                      if ((0x100000001000L & l) != 0L && kind > 38)
                         kind = 38;
                      break;
                   case 9:
                      if ((0x2000000020L & l) != 0L)
                         jjAddStates(56, 57);
                      break;
                   case 12:
                      if ((0x5000000050L & l) != 0L && kind > 42)
                         kind = 42;
                      break;
                   case 14:
                      if ((0xffffffffefffffffL & l) != 0L)
                         jjCheckNAddStates(21, 23);
                      break;
                   case 15:
                      if (curChar == 92)
                         jjAddStates(58, 60);
                      break;
                   case 16:
                      if ((0x14404410000000L & l) != 0L)
                         jjCheckNAddStates(21, 23);
                      break;
                   case 28:
                      if ((0x2000000020L & l) != 0L)
                         jjAddStates(61, 62);
                      break;
                   case 32:
                      if ((0x2000000020L & l) != 0L)
                         jjAddStates(63, 64);
                      break;
                   case 36:
                      if ((0x2000000020L & l) != 0L)
                         jjAddStates(65, 66);
                      break;
                   case 40:
                      if ((0x100000001000000L & l) != 0L)
                         jjCheckNAdd(41);
                      break;
                   case 41:
                      if ((0x7e0000007eL & l) == 0L)
                         break;
                      if (kind > 38)
                         kind = 38;
                      jjCheckNAddTwoStates(41, 6);
                      break;
                   case 44:
                      if ((0xffffffffdfffffffL & l) != 0L)
                         jjCheckNAddStates(40, 42);
                      break;
                   case 45:
                      if (curChar == 93 && kind > 30)
                         kind = 30;
                      break;
                   case 46:
                      if (curChar == 92)
                         jjAddStates(67, 68);
                      break;
                   case 47:
                      if (curChar == 93)
                         jjCheckNAddStates(40, 42);
                      break;
                   case 48:
                      if (curChar == 92)
                         jjCheckNAddStates(40, 42);
                      break;
                   case 49:
                      if ((0xffffffffdfffffffL & l) != 0L)
                         jjCheckNAddStates(43, 45);
                      break;
                   case 51:
                      if (curChar == 92)
                         jjAddStates(69, 70);
                      break;
                   case 53:
                      if (curChar == 92)
                         jjCheckNAddStates(43, 45);
                      break;
                   case 54:
                      if ((0xffffffffdfffffffL & l) != 0L)
                         jjCheckNAddStates(46, 48);
                      break;
                   case 56:
                      if (curChar == 92)
                         jjAddStates(71, 72);
                      break;
                   case 58:
                      if (curChar == 92)
                         jjCheckNAddStates(46, 48);
                      break;
                   case 60:
                      if ((0xffffffffefffffffL & l) != 0L)
                         jjCheckNAdd(61);
                      break;
                   case 62:
                      if (curChar == 92)
                         jjAddStates(73, 75);
                      break;
                   case 63:
                      if ((0x14404410000000L & l) != 0L)
                         jjCheckNAdd(61);
                      break;
                   case 68:
                      if ((0xffffffffefffffffL & l) != 0L)
                         jjCheckNAddStates(49, 51);
                      break;
                   case 69:
                      if (curChar == 92)
                         jjAddStates(76, 78);
                      break;
                   case 70:
                      if ((0x14404410000000L & l) != 0L)
                         jjCheckNAddStates(49, 51);
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
                   case 23:
                      if ((jjbitVec3[i2] & l2) == 0L)
                         break;
                      if (kind > 47)
                         kind = 47;
                      jjCheckNAdd(23);
                      break;
                   case 1:
                      if ((jjbitVec0[i2] & l2) != 0L && kind > 33)
                         kind = 33;
                      break;
                   case 14:
                      if ((jjbitVec0[i2] & l2) != 0L)
                         jjAddStates(21, 23);
                      break;
                   case 44:
                      if ((jjbitVec0[i2] & l2) != 0L)
                         jjAddStates(40, 42);
                      break;
                   case 49:
                      if ((jjbitVec0[i2] & l2) != 0L)
                         jjAddStates(43, 45);
                      break;
                   case 54:
                      if ((jjbitVec0[i2] & l2) != 0L)
                         jjAddStates(46, 48);
                      break;
                   case 60:
                      if ((jjbitVec0[i2] & l2) != 0L)
                         jjstateSet[jjnewStateCnt++] = 61;
                      break;
                   case 68:
                      if ((jjbitVec0[i2] & l2) != 0L)
                         jjAddStates(49, 51);
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
          if ((i = jjnewStateCnt) == (startsAt = 76 - (jjnewStateCnt = startsAt)))
             return curPos;
          try { curChar = input_stream.readChar(); }
          catch(java.io.IOException e) { return curPos; }
       }
    }
    private final int jjMoveStringLiteralDfa0_2()
    {
       switch(curChar)
       {
          case 42:
             return jjMoveStringLiteralDfa1_2(0x1000000000L);
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
             if ((active0 & 0x1000000000L) != 0L)
                return jjStopAtPos(1, 36);
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
             return jjMoveStringLiteralDfa1_1(0x800000000L);
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
             if ((active0 & 0x800000000L) != 0L)
                return jjStopAtPos(1, 35);
             break;
          default :
             return 2;
       }
       return 2;
    }
    final int[] jjnextStates = {
       25, 26, 31, 32, 35, 36, 12, 60, 62, 68, 69, 71, 44, 45, 49, 50, 
       54, 55, 56, 51, 46, 14, 15, 17, 40, 42, 6, 8, 9, 12, 14, 15, 
       19, 17, 27, 28, 12, 35, 36, 12, 44, 45, 46, 49, 50, 51, 54, 55, 
       56, 68, 69, 71, 68, 69, 73, 71, 10, 11, 16, 18, 20, 29, 30, 33, 
       34, 37, 38, 47, 48, 52, 53, 57, 58, 63, 64, 66, 70, 72, 74, 
    };
    public final String[] jjstrLiteralImages = {
    "", null, null, null, null, "\12", "\50\154\141\155\142\144\141", 
    "\133\123\143\157\160\145", "\50\164\171\160\145\115\141\143\162\157", "\50", "\51", "\133", "\135", 
    "\173", "\175", "\74\55", "\55\76", "\74", "\76", "\54", "\56", "\72", null, "\77", 
    "\55", "\176", "\100", "\43", "\52", "\174", null, null, null, null, null, null, null, 
    null, null, null, null, null, null, null, null, null, null, null, null, null, };
    public final String[] lexStateNames = {
       "DEFAULT", 
       "IN_FORMAL_COMMENT", 
       "IN_MULTI_LINE_COMMENT", 
    };
    public final int[] jjnewLexState = {
       -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
       -1, -1, -1, -1, -1, -1, -1, -1, 1, 2, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
    };
    final long[] jjtoToken = {
       0xf459ffbfffe1L, 
    };
    final long[] jjtoSkip = {
       0x1eL, 
    };
    final long[] jjtoMore = {
       0x2600000000L, 
    };
    private com.metamata.parse.MParseReader input_stream;
    private final int[] jjrounds = new int[76];
    private final int[] jjstateSet = new int[152];
    protected char curChar;
    public __jjLFParserImplTokenManager(com.metamata.parse.MParseReader stream)
    {
       if (false)
          throw new Error("ERROR: Cannot use a CharStream class with a non-static lexical analyzer.");
       input_stream = stream;
    }
    public __jjLFParserImplTokenManager(com.metamata.parse.MParseReader stream, int lexState)
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
       for (i = 76; i-- > 0;)
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
             try { input_stream.backup(0);
                while (curChar <= 32 && (0x100003200L & (1L << curChar)) != 0L)
                   curChar = input_stream.BeginToken();
             }
             catch (java.io.IOException e1) { continue EOFLoop; }
             jjmatchedKind = 0x7fffffff;
             jjmatchedPos = 0;
             curPos = jjMoveStringLiteralDfa0_0();
             break;
           case 1:
             jjmatchedKind = 0x7fffffff;
             jjmatchedPos = 0;
             curPos = jjMoveStringLiteralDfa0_1();
             if (jjmatchedPos == 0 && jjmatchedKind > 37)
             {
                jjmatchedKind = 37;
             }
             break;
           case 2:
             jjmatchedKind = 0x7fffffff;
             jjmatchedPos = 0;
             curPos = jjMoveStringLiteralDfa0_2();
             if (jjmatchedPos == 0 && jjmatchedKind > 37)
             {
                jjmatchedKind = 37;
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

  private static LFParserImpl __jjcns = new LFParserImpl((LFParserImpl)null);

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

  private LFParserImpl(LFParserImpl dummy) {
  }

  public LFParserImpl() {
    this(System.in);
  }

  public LFParserImpl(java.io.InputStream input) {
    this(new java.io.InputStreamReader(input));
  }

  public LFParserImpl(java.io.Reader input) {
    __jjstate = new com.metamata.parse.ParserState();
    __jjstate.initialize(createNewScanner(input));
  }

  public LFParserImpl(com.metamata.parse.Scanner scanner) {
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

private static final String DEFINING_LABEL_TABLE_NAME = "DEFINING_LABEL_TABLE";
		/** Name of MarkerTable unit in context. **/
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
		/** The DefiningLabelTable for the current context. **/
	private DefiningLabelTable definingLabelTable;
		/** The MarkerTable for the current context. **/
	private MarkerTable markerTable;
  /** Flag to indicate whether types should be created on demand or a parse
   *  exception generated.
   */
  private boolean createTypesOnDemand = true;


    /**
     * Constructs a parser with the specified knowledge base, translation
     * context, and input stream.
     *
     * @param newKnowledgeBase  the knowledge base to be used while parsing.
     * @param newTranslationContext  the translation context to be used while
     * parsing.
     */
  public void initializeLFParserImpl(KnowledgeBase newKnowledgeBase,
    TranslationContext newTranslationContext)
    {
    knowledgeBase = newKnowledgeBase;
    translationContext = newTranslationContext;
    markerSet = knowledgeBase.getMarkerSet();
    conceptHierarchy = knowledgeBase.getConceptTypeHierarchy();
    relationHierarchy = knowledgeBase.getRelationTypeHierarchy();
    definingLabelTable = getDefiningLabelTable();
    markerTable = getMarkerTable();
    }

		/**
		 * Returns an instance of ParseException.
		 *
		 * @return an instance of ParseException.
		 */
	public ParseException generateParseException()
		{
		return new ParseException(null);
		}

		/**
		 * Returns the DefiningLabelTable currently in used by this parser.
		 *
		 * @return the DefiningLabelTable currently in used by this parser.
		 */
	private DefiningLabelTable getDefiningLabelTable()
		{
		DefiningLabelTable table;

		table = (DefiningLabelTable)translationContext.getUnit(DEFINING_LABEL_TABLE_NAME);

		if (table == null)
			{
			table = new DefiningLabelTable();
			table.setUnitName(DEFINING_LABEL_TABLE_NAME);
			translationContext.addUnit(table);
			}

		return table;
		}

		/**
		 * Returns the MarkerTable currently in used by this parser.
		 *
		 * @return the MarkerTable currently in used by this parser.
		 */
	private MarkerTable getMarkerTable()
		{
		MarkerTable table;

		table = (MarkerTable)translationContext.getUnit(MARKER_TABLE_NAME);

		if (table == null)
			{
			table = new MarkerTable();
			table.setUnitName(MARKER_TABLE_NAME);
			translationContext.addUnit(table);
			}

		return table;
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
     * @bug Must add support for numeric escape sequences.
     */
  public String translateEscapeSequences(String input)
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

/* Skip Definitions */



/* Separators and Keywords */












/* Literals */



/* IDENTIFIERS */




/* ** RULES START HERE ** */


/* Graph Rules */

    /**
     * Attempts to parse a sentence from the input stream.
     *
     * @return the graph created by parsing.
     * @exception ParserException  if an error occurs while parsing.
     */
	public Graph Sentence() throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}Graph newGraph;{
			}

		newGraph = Graph();boolean __jjV0 = __jjstate.guessing;
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
__jjstate.ematch(PERIOD);
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
__jjstate.ematch(QUESTIONMARK);
}
if (!__jjstate.guessing)
{
			return newGraph;
			}return null;

		}

    /**
     * Attempts to parse a graph from the input stream.
     *
     * @return the graph created by parsing.
     * @exception ParserException  if an error occurs while parsing.
     */
	public Graph Graph() throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}Graph newGraph = new Graph();{
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
ConceptAndLinks(newGraph);  } catch (com.metamata.parse.GuessSucc __jjGC) {
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
RelationAndLinks(newGraph, null, 0);}
if (!__jjstate.guessing)
{
			return newGraph;
			}return null;

		}

    /**
     * Attempts to parse a concept and any links from the input stream.
     *
		 * @param graph  the graph to which nodes are being added.
		 * @return the parsed concept.
     * @exception ParserException  if an error occurs while parsing.
     */
	public Concept ConceptAndLinks(Graph graph) throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}Concept newConcept;
			int linkingArc = 0;{
			}

		newConcept = Concept();if (!__jjstate.guessing)
{
			graph.addConcept(newConcept);
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
linkingArc = Arc();RelationAndLinks(graph, newConcept, linkingArc);  } catch (com.metamata.parse.GuessSucc __jjGC) {
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
__jjstate.ematch(HYPHEN);
__jjstate.ematch(NEWLINE);
linkingArc = Arc();RelationAndLinks(graph, newConcept, linkingArc);
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
__jjstate.ematch(NEWLINE);
linkingArc = Arc();RelationAndLinks(graph, newConcept, linkingArc);  } catch (com.metamata.parse.GuessSucc __jjGC) {
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
__jjstate.ematch(COMMA);
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
if (!__jjstate.guessing)
{
			return newConcept;
			}return null;

		}

		/**
     * Attempts to parse a relation and any links from the input stream.
     *
		 * @param graph  the graph to which nodes are being added.
		 * @param preceedingConcept  the Concept linked immediately to the left of the
		 * relation to be parsed, or null if no such concept is present.
		 * @param preceedingArc  the value of the preceeding arc which is only meaningful
		 * if the preceedingConcept is not null.
     * @exception ParserException  if an error occurs while parsing.
     */
	public void RelationAndLinks(Graph graph, Concept preceedingConcept,
		int preceedingArc)
		throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}Relation newRelation;
			Concept followingConcept;
			int followingArc;
			Vector argConcepts = new Vector(10);
			Vector argArcs = new Vector(10);{
			}

		newRelation = Relation();if (!__jjstate.guessing)
{
			if (preceedingConcept != null)
				{
				// Add the preceeding concept and arc to list of arguments, flipping the sign of
				// of the arc to reflect that it's coming from the left.  That way, we can simply
				// regard all positive arc values as outgoing.
				argConcepts.addElement(preceedingConcept);
				argArcs.addElement(new Integer(-preceedingArc));
				}
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
followingArc = Arc();followingConcept = ConceptAndLinks(graph);if (!__jjstate.guessing)
{
				// Add following concept as argument.
				argConcepts.addElement(followingConcept);
				argArcs.addElement(new Integer(followingArc));
				}  } catch (com.metamata.parse.GuessSucc __jjGC) {
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
__jjstate.ematch(HYPHEN);
__jjstate.ematch(NEWLINE);
followingArc = Arc();followingConcept = ConceptAndLinks(graph);if (!__jjstate.guessing)
{
				// Add following concept as argument.
				argConcepts.addElement(followingConcept);
				argArcs.addElement(new Integer(followingArc));
				}
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
__jjstate.ematch(NEWLINE);
followingArc = Arc();followingConcept = ConceptAndLinks(graph);if (!__jjstate.guessing)
{
				// Add following concept as argument.
				argConcepts.addElement(followingConcept);
				argArcs.addElement(new Integer(followingArc));
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
__jjstate.ematch(COMMA);
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
if (!__jjstate.guessing)
{
			int highestIndex = -1;
			int currArc;
			int simpleIn = 0;
			int simpleOut = 0;
			Concept argArr[] = null;
			Enumeration arcEnum, conEnum;

			for (arcEnum = argArcs.elements(); arcEnum.hasMoreElements();)
				{
				currArc = ((Integer)arcEnum.nextElement()).intValue();
				if (currArc == 1)
					simpleOut++;
				else
					if (currArc == -1)
						simpleIn++;
					else
						{
						// What do we do with the direction of numbered arcs?
						// Shall we assume that they are all incoming?
						currArc = Math.abs(currArc) - 2;
						if (currArc > highestIndex)
							highestIndex = currArc;
						}
				}

			if (highestIndex == -1)
				{
				// No numbered arcs present
				int inCount, outCount;
				Concept currConcept;

				// We'll build the array with the simple ins taken in order followed by the
				// simple outs taken in order
				argArr = new Concept[simpleIn + simpleOut];

				arcEnum = argArcs.elements();
				conEnum = argConcepts.elements();

				inCount = 0;
				outCount = 0;

				while (arcEnum.hasMoreElements())
					{
					currArc = ((Integer)arcEnum.nextElement()).intValue();
					currConcept = (Concept)conEnum.nextElement();

					if (currArc == 1)
						{
						argArr[outCount + simpleIn] = currConcept;
						outCount++;
						}
					else
						{
						argArr[inCount] = currConcept;
						inCount++;
						}
					}
				}
			else
				{
				// One or more numbered arcs present

				// Currently can't handle it so we'll throw an exception.
				throw new ParserException("Current implementation cannot handle numbered arcs.");
				}

			// Add arguments to relation
			newRelation.setArguments(argArr);

			// Add relation to graph
			graph.addRelation(newRelation);
			}
		}


    /**
     * Attempts to parse a relational arc from the input stream.
     * The integer value returned is determined as follows: <BR>
     * ->   ==   1	<BR>
     * <-   ==  -1	<BR>
     * -1-> ==   3	<BR>
     * <-1- ==  -3	<BR>
     * etc...<BR>
     *
     * @return the index of the arc plus two and negated if left-pointing,
     * or -1 to indicate an unnumbered left arc, or +1 to indicate an unnumbered right arc.
     * @exception ParserException  if an error occurs while parsing.
     *
     * @bug We should probably throw an exception if an arc is numbered <= 0.
     */
	public int Arc() throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}com.metamata.parse.Token intToken = null;
			int arcVal = -1;
			boolean leftArc = false;{
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
__jjstate.ematch(LEFT_ARROW);

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
intToken = __jjstate.ematch(INTEGER_LITERAL);
__jjstate.ematch(HYPHEN);
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
if (!__jjstate.guessing)
{
				leftArc = true;
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
__jjstate.ematch(HYPHEN);
intToken = __jjstate.ematch(INTEGER_LITERAL);
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
__jjstate.ematch(RIGHT_ARROW);
if (!__jjstate.guessing)
{
				leftArc = false;
				}}
if (!__jjstate.guessing)
{
			if (intToken == null)
				{
				if (leftArc)
					return -1;
				else
					return +1;
				}
			else
				{
				try
					{
					arcVal = Integer.parseInt(intToken.image);
					if (leftArc)
						return -(arcVal + 2);
					else
						return (arcVal + 2);
					}
				catch (NumberFormatException e)
					{
					// Shouldn't happen since we've already parsed it as an integer
					e.printStackTrace();
					System.exit(1);
					}
				}
			}return 0;

		}


    /**
     * Attempts to parse a graph comment from the input stream.
     *
     * @return the graph comment created by parsing.
     * @exception ParserException  if an error occurs while parsing.
     */
	public String GraphComment() throws ParserException, com.metamata.parse.ParseException 
		{
		  return null;
		}


/* Concept Rules */

    /**
     * Attempts to parse a concept from the input stream.
     *
     * @return the concept created by parsing.
     * @exception ParserException  if an error occurs while parsing.
     */
	public Concept Concept() throws ParserException, com.metamata.parse.ParseException 
		{
		  {
		  }ConceptType conType = null;
		  Referent referent = null;
		  String defLabel = null;
		  String boundLabel = null;{
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
conType = ConceptTypeLabel();
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
__jjstate.ematch(COLON);

boolean __jjV13 = __jjstate.guessing;
int __jjV14 = 0;
com.metamata.parse.Token __jjV15 = null;
if (__jjV13) {
  __jjV14 = __jjstate.la;
  __jjV15 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV12 = true;
for (;;) {
  try {
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
referent = Referent();  } catch (com.metamata.parse.GuessSucc __jjGC) {
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
defLabel = DefiningLabel();}
  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV13) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV12 = false;
    if (__jjV13) {
      __jjstate.la = __jjV14;
      __jjstate.laToken = __jjV15;
    }
  }
  if (__jjV13 || !__jjstate.guessing || !__jjV12) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV13;
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
boundLabel = BoundLabel();}
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
__jjstate.ematch(RIGHT_BRACKET);
if (!__jjstate.guessing)
{
		  if (conType != null)
		    if (referent != null)
		    	{
		      return new Concept(conType, referent);
		      }
		    else
		    	{
		    	Concept newConcept;

		      newConcept = new Concept(conType);
					// Add to defining label table
					return newConcept;
		      }
		  else
		    if (boundLabel != null)
		      {
		      // Should return the defining concept corresponding to this label
		      return null;
		      }
		    else
		      {
		      // Return empty concept
		      return new Concept();
		      }
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
						// Note: This should never happen since we don't specify parents
						// or children.
						throw new ParserException("Error adding concept type.", e);
						}
					}
				else
					throw new ParserException(
						"Specified concept type is not present in concept type hierarchy.",
						generateParseException());

				return conType;
				}return null;

		}

    /**
     * Attempts to parse a defining label from the input stream.
     *
     * @return the defining label created by parsing.
     * @exception ParserException  if an error occurs while parsing.
     */
	public String DefiningLabel() throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}com.metamata.parse.Token labelToken;{
			}
		__jjstate.ematch(ASTERISK);
labelToken = __jjstate.ematch(IDENTIFIER);
if (!__jjstate.guessing)
{
			return labelToken.image;
			}return null;

		}


    /**
     * Attempts to parse a bound label from the input stream.
     *
     * @return the bound label created by parsing.
     * @exception ParserException  if an error occurs while parsing.
     */
	public String BoundLabel() throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}com.metamata.parse.Token idenToken;{
			}
			__jjstate.ematch(QUESTIONMARK);
idenToken = __jjstate.ematch(IDENTIFIER);
if (!__jjstate.guessing)
{
				return idenToken.image;
				}return null;

		}


    /**
     * Attempts to parse a referent from the input stream.
     *
     * @return the referent created by parsing.
     * @exception ParserException  if an error occurs while parsing.
     */
	public Referent Referent() throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}Designator newDesignator;
			Graph newDescriptor;{
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
newDesignator = Designator();if (!__jjstate.guessing)
{
  			return new Referent(newDesignator);
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
newDescriptor = Graph();if (!__jjstate.guessing)
{
  			return new Referent(newDescriptor);
  			}}
return null;

		}


    /**
     * Attempts to parse a quantifier from the input stream.
     *
     * @return the quantifier created or looked up by parsing (null indicates
     * existential quantification).
     * @exception ParserException  if an error occurs while parsing.
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
     *
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
			}Object literal = null;{
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
literal = String();}
if (!__jjstate.guessing)
{
			return new LiteralDesignator(literal, markerSet);
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
     */
	public Designator Locator() throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}String name = null;
			String markerID = null;
			Marker marker = null;{
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
					markerTable.mapForeignMarkerIDToNativeMarker(markerID, marker);
					}
				else
					{
					marker = markerTable.getNativeMarkerByForeignMarkerID(markerID);
					if (marker == null)
						{
						marker = new Marker(markerSet, null);
						markerTable.mapForeignMarkerIDToNativeMarker(markerID, marker);
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

/* Relation Rules */

    /**
     * Attempts to parse a relation from the input stream.
     *
     * @return the relation created by parsing.
     * @exception ParserException  if an error occurs while parsing.
     */
	public Relation Relation() throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}RelationType relType;{
			}
		__jjstate.ematch(LEFT_PAREN);
relType = RelationTypeLabel();__jjstate.ematch(RIGHT_PAREN);
if (!__jjstate.guessing)
{
		  return new Relation(relType);
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
			}com.metamata.parse.Token labelToken;
			RelationType relType;{
			}
		labelToken = __jjstate.ematch(IDENTIFIER);
if (!__jjstate.guessing)
{
			relType = (RelationType)relationHierarchy.getTypeByLabel(labelToken.image);

			if (relType == null)
				{
				// Must create the type in this case.  Assume universal and absurd as
				// parents and children.
				relType = new RelationType(labelToken.image);
				try
					{
					relationHierarchy.addTypeToHierarchy(relType);
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

		return relType;
		}return null;

	}


    /**
     * Attempts to parse a lambda expression from the input stream.
     *
     * @exception ParserException  if an error occurs while parsing.
     */
	public RelationType LambdaExpression() throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}Graph expr = null;
			RelationType relType = null;{
			}
		__jjstate.ematch(LAMBDA);
DefiningLabel();
boolean __jjV0 = __jjstate.guessing;
int __jjV1 = 0;
com.metamata.parse.Token __jjV2 = null;
for (;;) {
if (__jjV0) {
  __jjV1 = __jjstate.la;
  __jjV2 = __jjstate.laToken;
} else {
  __jjstate.guessing = true;
  __jjstate.lamax = 0;
  __jjstate.la = 0;
  __jjstate.laToken = __jjstate.token;
}
boolean __jjV3 = true;
for (;;) {
  try {
DefiningLabel();  } catch (com.metamata.parse.GuessSucc __jjGC) {
    if (__jjV0) {
      throw __jjGC;
    }
  } catch (com.metamata.parse.GuessFail __jjGC) {
    __jjV3 = false;
    if (__jjV0) {
      __jjstate.la = __jjV1;
      __jjstate.laToken = __jjV2;
    }
  }
  if (__jjV0 || !__jjstate.guessing || !__jjV3) {
    break;
  }
  __jjstate.guessing = false;
}
__jjstate.guessing = __jjV0;
if (!__jjV3) {
  break;
}
}
expr = Graph();__jjstate.ematch(RIGHT_PAREN);
if (!__jjstate.guessing)
{
			return new RelationType(new RelationTypeDefinition(null, expr));
			}return null;

		}



/* Actor Rules */

    /**
     * Attempts to parse an actor from the input stream.
     *
     * @return the actor created by parsing.
     * @exception ParserException  if an error occurs while parsing.
     */
	public Actor Actor() throws  ParserException, com.metamata.parse.ParseException 
		{
	
  		return null;
  		
		}


    /**
     * Attempts to parse a actor type Label from  the input stream.
     *
     * @return the actor  type created or looked up by parsing.
     * @exception ParserException  if an error occurs while parsing.
     */
	public RelationType ActorTypeLabel() throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}com.metamata.parse.Token labelToken;
			RelationType relationType;{
			}
		labelToken = __jjstate.ematch(IDENTIFIER);
if (!__jjstate.guessing)
{
			relationType = relationHierarchy.getTypeByLabel(labelToken.image);

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
     * Attempts to parse a actor comment from the input stream.
     *
     * @return the comment created by parsing.
     * @exception ParserException  if an error occurs while parsing.
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
     */
	public String Name() throws ParserException, com.metamata.parse.ParseException 
		{
			{
			}com.metamata.parse.Token nameToken;{
			}
		nameToken = __jjstate.ematch(NAME_LITERAL);
if (!__jjstate.guessing)
{
			return translateEscapeSequences(
				nameToken.image.substring(1, nameToken.image.length() - 1));
			}return null;

		}


    /**
     * Attempts to parse a number from the input stream.
     *
     * @return the number created by parsing.
     * @exception ParserException  if an error occurs while parsing.
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
			return translateEscapeSequences(
				strToken.image.substring(1, strToken.image.length() - 1));
			}return null;

		}


	}

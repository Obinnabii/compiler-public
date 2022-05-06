package apd67.lexer;

import java_cup.runtime.Symbol;
import apd67.parser.sym;
import apd67.util.exception.*;

%%

%public
%class XiLexer

%unicode

%pack

%line
%column

%cup

%{
public Boolean isInterface;
%}

%ctorarg boolean isInterface

%init{
this.isInterface = isInterface;
%init}

%{

  // ERROR MESSAGES
  String defaultErrorMessage = "Lexing Error";
  String MultiLineStringErrorMessage = "String longer than one line";
  String invalidCharConstMessage = "Invalid character constant";

  /** Used to compile strings when lexing. Particularly useful for  */
  StringBuilder s = new StringBuilder();
  int line_tmp = 0, column_tmp = 0;

  private Symbol token(int type) {
    return new Token(type, yyline+1, yycolumn+1, null);
  }

  private Symbol token(int type, Object attr) {
    return new Token(type, yyline+1, yycolumn+1, attr);
  }

  private Symbol token(int type, int line, int column, Object attr) {
    return new Token(type, line+1, column+1, attr);
  }

%}

// LEXING SYMBOLS
Whitespace = [ \t\f\r\n]
Letter = [a-zA-Z]
Digit = [0-9]
Endline = \R
Hex_Symbols = {Digit}| [a-fA-F]

// CHARACTERS
Hex = "\\x"{Hex_Symbols}{Hex_Symbols}?{Hex_Symbols}?{Hex_Symbols}?
NonHex_String = [^\r\n\"\\]
NonHex_Char = [^\r\n\'\\]
Escape_Char = "\\\\" | "\\\'" | "\\\"" | "\\n" | "\\t"
StringInput_NonHex = {NonHex_String}+ | {Escape_Char}

// COMMENTS
Comment = "//"

Identifier = {Letter}({Digit}|{Letter}|_|"\'")*
Integer = "0"|[1-9]{Digit}*
MinInt = "9223372036854775808"

// Symbol = "\(" | "\)" | "[" | "]" | "{" | "}" | ":" | ";" | "+" | "-" | "!"
// | "*" | "*>>" | "/" | "%" | "<" | "<=" | ">=" | ">" | "==" | "!=" | "&" | "|"
// | "," | "=" | "_"

%state STRING
%state CHAR
%state COMMENT
%state YYBASE

%%
<YYINITIAL> {
    ""            { yybegin(YYBASE); if(isInterface) return token(sym.IXI, "ixi"); }
    <<EOF>>       { return token(sym.EOF, "end of file"); }
}

<YYBASE> {
    // end of file
    <<EOF>>       { return token(sym.EOF, "end of file"); }
    // KEYWORDS
    "int"         { return token(sym.INT_TYPE, "int"); }
    "bool"        { return token(sym.BOOL_TYPE, "bool"); }
    "true"        { return token(sym.TRUE, "true" ); }
    "false"       { return token(sym.FALSE, "false"); }
    "while"       { return token(sym.WHILE,  "while"); }
    "if"          { return token(sym.IF, "if" ); }
    "else"        { return token(sym.ELSE, "else"); }
    "length"      { return token(sym.LENGTH, "length"); }
    "return"      { return token(sym.RETURN, "return"); }
    "use"         { return token(sym.USE, "use"); }

    // SYMBOLS
    // {Symbol}      { return token(SYMBOL); }
    "\("          { return token(sym.LPAREN, "("); }
    "\)"          { return token(sym.RPAREN, ")"); }
    "["           { return token(sym.LBRACK, "["); }
    "]"           { return token(sym.RBRACK, "]"); }
    "{"           { return token(sym.LBRACE, "{"); }
    "}"           { return token(sym.RBRACE, "}"); }
    ":"           { return token(sym.COLON, ":"); }
    ";"           { return token(sym.SEMICOLON, ";"); }
    "+"           { return token(sym.PLUS, "+"); }
    "-"           { return token(sym.MINUS,  "-"); }
    "!"           { return token(sym.NOT, "!"); }
    "*"           { return token(sym.TIMES, "*"); }
    "*>>"         { return token(sym.HTIMES, "*>>"); }
    "/"           { return token(sym.DIV, "/"); }
    "%"           { return token(sym.MOD, "%"); }
    "<"           { return token(sym.LT, "<"); }
    "<="          { return token(sym.LTE, "<="); }
    ">"           { return token(sym.GT, ">"); }
    ">="          { return token(sym.GEQ, ">="); }
    "=="          { return token(sym.EQ,  "=="); }
    "!="          { return token(sym.NEQ, "!="); }
    "&"           { return token(sym.AND, "&"); }
    "|"           { return token(sym.OR, "|"); }
    ","           { return token(sym.COMMA, ","); }
    "="           { return token(sym.ASSIGN, "="); }
    "_"           { return token(sym.USCORE, "_"); }


    // STRINGS
    "\""          { yybegin(STRING); s.setLength(0);
                    line_tmp = yyline; column_tmp = yycolumn;}

    "\'"          { yybegin(CHAR); line_tmp = yyline; column_tmp = yycolumn;}

    {Identifier}  { return token(sym.ID, yytext()); }
    {MinInt}      { return token(sym.MIN_INT, Long.MIN_VALUE); }
    {Integer}     { try { return token(sym.INT,
                                       Long.parseLong(yytext())); }
                    catch(NumberFormatException e) {
                        throw new LexerException(defaultErrorMessage,
                        yyline + 1, yycolumn + 1);
                    }
                  }

    // IGNORE
    {Whitespace}  { }
    {Comment}     { yybegin(COMMENT); }

    [^]           { throw new LexerException(defaultErrorMessage,
                    yyline + 1, yycolumn + 1);
                  }
}

<STRING> {
    "\""                         { yybegin(YYBASE);
                                   return token(sym.STRING,
                                   line_tmp, column_tmp, s.toString() );
                                 }

    // Common non-printable characters - hex -> escaped
    "\\x"((a|A) | (0|00|000)(a|A))     { s.append("\\n"); }

    "\\x"(27 | (0|00)27)               { s.append("\\\'"); }

    "\\x"(5 | (0|00)5)(c|C)            { s.append("\\\\");  }

    "\\x"(7 | (0|00|000)7)             { s.append("\\a"); }

    "\\x"(1 | 01 | 001)(b|B)           { s.append("\\e"); }

    "\\x"((c|C) | (0|00|000)(c|C))     { s.append("\\f"); }

    "\\x"((d|D) | (0|00|000)(d|D))     { s.append("\\r"); }

    "\\x"((9) | (0|00|000)9)           { s.append("\\t"); }

    "\\x"((b|B) | (0|00|000)(b|B))     { s.append("\\v"); }

    "\\x"(8 | (0|00|000)8)             { s.append("\\b"); }

    // Other hexadecimal
    {Hex}         { char val = (char) Integer.parseInt(yytext()
                    .substring(2),16);
                    s.append( val ); }

    {StringInput_NonHex}      { s.append(yytext()); }

    {Endline}     { throw new LexerException(MultiLineStringErrorMessage,
                    line_tmp + 1, column_tmp + 1); 
                    }
    <<EOF>>       { throw new LexerException(defaultErrorMessage,
                    line_tmp + 1, column_tmp + 1);
                  }
    [\n\r]        { throw new LexerException(defaultErrorMessage,
                    line_tmp + 1, column_tmp + 1);
                  }
    [^]           { throw new LexerException(defaultErrorMessage,
                    yyline + 1, yycolumn + 1);
                  }
}

<CHAR> {
    // Common non-printable characters - hex -> escaped
    "\\x"((a|A) | (0|00|000)(a|A))"\'"   { yybegin(YYBASE);
                                           return token(sym.CHAR,
                                           line_tmp, column_tmp,
                                           "\\n"); }
    "\\x"(5 | (0|00)5)(c|C)"\'"          { yybegin(YYBASE);
                                           return token(sym.CHAR,
                                           line_tmp, column_tmp,
                                           "\\\\"); }
    "\\x"(27 | (0|00)27)"\'"             { yybegin(YYBASE);
                                           return token(sym.CHAR,
                                           line_tmp, column_tmp,
                                           "\\\'"); }
    "\\x"(7 | (0|00|000)7)"\'"           { yybegin(YYBASE);
                                           return token(sym.CHAR,
                                           line_tmp, column_tmp,
                                           "\\a"); }
    "\\x"(1 | 01 | 001)(b|B)"\'"         { yybegin(YYBASE);
                                           return token(sym.CHAR,
                                           line_tmp, column_tmp,
                                           "\\e"); }
    "\\x"((c|C) | (0|00|000)(c|C))"\'"   { yybegin(YYBASE);
                                           return token(sym.CHAR,
                                           line_tmp, column_tmp,
                                           "\\f"); }
    "\\x"((d|D) | (0|00|000)(d|D))"\'"   { yybegin(YYBASE);
                                           return token(sym.CHAR,
                                           line_tmp, column_tmp,
                                           "\\r"); }
    "\\x"(9 | (0|00|000)9)"\'"           { yybegin(YYBASE);
                                           return token(sym.CHAR,
                                           line_tmp, column_tmp,
                                           "\\t"); }
    "\\x"((b|B) | (0|00|000)(b|B))"\'"   { yybegin(YYBASE);
                                           return token(sym.CHAR,
                                           line_tmp, column_tmp,
                                           "\\v"); }
    "\\x"(8 | (0|00|000)8)"\'"           { yybegin(YYBASE);
                                           return token(sym.CHAR,
                                           line_tmp, column_tmp,
                                           "\\b"); }

    // Other hexadecimal
    {Hex}"\'"             { yybegin(YYBASE);
                            return token(sym.CHAR, line_tmp, column_tmp,
                            "" + ((char) Integer.parseInt(yytext()
                            .substring(2,yytext().length() - 1),16)));
                          }

    {Escape_Char}"\'"     { yybegin(YYBASE);
                            return token(sym.CHAR, line_tmp, column_tmp,
                            yytext().substring(0,yytext().length() - 1));
                          }
    {NonHex_Char}"\'"     { yybegin(YYBASE); return token(sym.CHAR,
                            line_tmp, column_tmp, "" + yytext().charAt(0));
                          }
    "\'"                  { throw new LexerException(invalidCharConstMessage,
                            line_tmp + 1, column_tmp + 1);
                          }
    <<EOF>>               { throw new LexerException(defaultErrorMessage,
                            line_tmp + 1, column_tmp + 1);
                          }
    [^]                   { throw new LexerException(defaultErrorMessage,
                            line_tmp + 1, column_tmp + 1);
                          }
}

<COMMENT> {
    [\n\r]                { yybegin(YYBASE); }
    <<EOF>>               { yybegin(YYBASE); }
    [^]                   { }
}

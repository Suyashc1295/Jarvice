import java.util.*;
public class UCI {
    static String ENGINENAME="Jarvice v1";
    public static void uciCommunication() {
        Scanner input = new Scanner(System.in);
        while (true)
        {
            String inputString=input.nextLine();
            if ("uci".equals(inputString))
            {
                inputUCI();
            }
            else if (inputString.startsWith("setoption"))
            {
                inputSetOption(inputString);
            }
            else if ("isready".equals(inputString))
            {
                inputIsReady();
            }
            else if ("ucinewgame".equals(inputString))
            {
                inputUCINewGame();
            }
            else if (inputString.startsWith("position"))
            {
                inputPosition(inputString);
            }
            else if (inputString.startsWith("go"))
            {
                inputGo();
            }
            else if (inputString.equals("quit"))
            {
                inputQuit();
            }
            else if ("print".equals(inputString))
            {
                inputPrint();
            }
        }
    }
    public static void inputUCI() {
        System.out.println("id name "+ENGINENAME);
        System.out.println("id author Suyash");
        //options go here
        System.out.println("uciok");
    }
    public static void inputSetOption(String inputString) {
        //set options
    }
    public static void inputIsReady() {
         System.out.println("readyok");
    }
    public static void inputUCINewGame() {
        //add code here
    }
    public static void inputPosition(String input) {
        input=input.substring(9).concat(" ");
        if (input.contains("startpos ")) {
            input=input.substring(9);
            BoardGeneration.importFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        }
        else if (input.contains("fen")) {
            input=input.substring(4);
            BoardGeneration.importFEN(input);
        }
        if (input.contains("moves")) {
            input=input.substring(input.indexOf("moves")+6);
            while (input.length()>0)
            {
                String moves;
                if (Jarvice.WhiteToMove) {
                    moves=Moves.possibleMovesW(Jarvice.WP,Jarvice.WN,Jarvice.WB,Jarvice.WR,Jarvice.WQ,Jarvice.WK,Jarvice.BP,Jarvice.BN,Jarvice.BB,Jarvice.BR,Jarvice.BQ,Jarvice.BK,Jarvice.EP,Jarvice.CWK,Jarvice.CWQ,Jarvice.CBK,Jarvice.CBQ);
                } else {
                    moves=Moves.possibleMovesB(Jarvice.WP,Jarvice.WN,Jarvice.WB,Jarvice.WR,Jarvice.WQ,Jarvice.WK,Jarvice.BP,Jarvice.BN,Jarvice.BB,Jarvice.BR,Jarvice.BQ,Jarvice.BK,Jarvice.EP,Jarvice.CWK,Jarvice.CWQ,Jarvice.CBK,Jarvice.CBQ);
                }
                algebraToMove(input,moves);
                input=input.substring(input.indexOf(' ')+1);
            }
        }
    }
    public static void inputGo() {
        String move;
        if (Jarvice.WhiteToMove) {
            move=Moves.possibleMovesW(Jarvice.WP,Jarvice.WN,Jarvice.WB,Jarvice.WR,Jarvice.WQ,Jarvice.WK,Jarvice.BP,Jarvice.BN,Jarvice.BB,Jarvice.BR,Jarvice.BQ,Jarvice.BK,Jarvice.EP,Jarvice.CWK,Jarvice.CWQ,Jarvice.CBK,Jarvice.CBQ);
        } else {
            move=Moves.possibleMovesB(Jarvice.WP,Jarvice.WN,Jarvice.WB,Jarvice.WR,Jarvice.WQ,Jarvice.WK,Jarvice.BP,Jarvice.BN,Jarvice.BB,Jarvice.BR,Jarvice.BQ,Jarvice.BK,Jarvice.EP,Jarvice.CWK,Jarvice.CWQ,Jarvice.CBK,Jarvice.CBQ);
        }
        int index=(int)(Math.floor(Math.random()*(move.length()/4))*4);
        System.out.println("bestmove "+moveToAlgebra(move.substring(index,index+4)));
    }
    public static String moveToAlgebra(String move) {
        String append="";
        int start=0,end=0;
        if (Character.isDigit(move.charAt(3))) {//'regular' move
            start=(Character.getNumericValue(move.charAt(0))*8)+(Character.getNumericValue(move.charAt(1)));
            end=(Character.getNumericValue(move.charAt(2))*8)+(Character.getNumericValue(move.charAt(3)));
        } else if (move.charAt(3)=='P') {//pawn promotion
            if (Character.isUpperCase(move.charAt(2))) {
                start=Long.numberOfTrailingZeros(Moves.FileMasks8[move.charAt(0)-'0']&Moves.RankMasks8[1]);
                end=Long.numberOfTrailingZeros(Moves.FileMasks8[move.charAt(1)-'0']&Moves.RankMasks8[0]);
            } else {
                start=Long.numberOfTrailingZeros(Moves.FileMasks8[move.charAt(0)-'0']&Moves.RankMasks8[6]);
                end=Long.numberOfTrailingZeros(Moves.FileMasks8[move.charAt(1)-'0']&Moves.RankMasks8[7]);
            }
            append=""+Character.toLowerCase(move.charAt(2));
        } else if (move.charAt(3)=='E') {//en passant
            if (move.charAt(2)=='W') {
                start=Long.numberOfTrailingZeros(Moves.FileMasks8[move.charAt(0)-'0']&Moves.RankMasks8[3]);
                end=Long.numberOfTrailingZeros(Moves.FileMasks8[move.charAt(1)-'0']&Moves.RankMasks8[2]);
            } else {
                start=Long.numberOfTrailingZeros(Moves.FileMasks8[move.charAt(0)-'0']&Moves.RankMasks8[4]);
                end=Long.numberOfTrailingZeros(Moves.FileMasks8[move.charAt(1)-'0']&Moves.RankMasks8[5]);
            }
        }
        String returnMove="";
        returnMove+=(char)('a'+(start%8));
        returnMove+=(char)('8'-(start/8));
        returnMove+=(char)('a'+(end%8));
        returnMove+=(char)('8'-(end/8));
        returnMove+=append;
        return returnMove;
    }
    public static void algebraToMove(String input,String moves) {
        int start=0,end=0;
        int from=(input.charAt(0)-'a')+(8*('8'-input.charAt(1)));
        int to=(input.charAt(2)-'a')+(8*('8'-input.charAt(3)));
        for (int i=0;i<moves.length();i+=4) {
            if (Character.isDigit(moves.charAt(i+3))) {//'regular' move
                start=(Character.getNumericValue(moves.charAt(i+0))*8)+(Character.getNumericValue(moves.charAt(i+1)));
                end=(Character.getNumericValue(moves.charAt(i+2))*8)+(Character.getNumericValue(moves.charAt(i+3)));
            } else if (moves.charAt(i+3)=='P') {//pawn promotion
                if (Character.isUpperCase(moves.charAt(i+2))) {
                    start=Long.numberOfTrailingZeros(Moves.FileMasks8[moves.charAt(i+0)-'0']&Moves.RankMasks8[1]);
                    end=Long.numberOfTrailingZeros(Moves.FileMasks8[moves.charAt(i+1)-'0']&Moves.RankMasks8[0]);
                } else {
                    start=Long.numberOfTrailingZeros(Moves.FileMasks8[moves.charAt(i+0)-'0']&Moves.RankMasks8[6]);
                    end=Long.numberOfTrailingZeros(Moves.FileMasks8[moves.charAt(i+1)-'0']&Moves.RankMasks8[7]);
                }
            } else if (moves.charAt(i+3)=='E') {//en passant
                if (moves.charAt(i+2)=='W') {
                    start=Long.numberOfTrailingZeros(Moves.FileMasks8[moves.charAt(i+0)-'0']&Moves.RankMasks8[3]);
                    end=Long.numberOfTrailingZeros(Moves.FileMasks8[moves.charAt(i+1)-'0']&Moves.RankMasks8[2]);
                } else {
                    start=Long.numberOfTrailingZeros(Moves.FileMasks8[moves.charAt(i+0)-'0']&Moves.RankMasks8[4]);
                    end=Long.numberOfTrailingZeros(Moves.FileMasks8[moves.charAt(i+1)-'0']&Moves.RankMasks8[5]);
                }
            }
            if ((start==from) && (end==to)) {
                if ((input.charAt(4)==' ') || (Character.toUpperCase(input.charAt(4))==Character.toUpperCase(moves.charAt(i+2)))) {
                    if (Character.isDigit(moves.charAt(i+3))) {//'regular' move
                        start=(Character.getNumericValue(moves.charAt(i))*8)+(Character.getNumericValue(moves.charAt(i+1)));
                        if (((1L<<start)&Jarvice.WK)!=0) {Jarvice.CWK=false; Jarvice.CWQ=false;}
                        else if (((1L<<start)&Jarvice.BK)!=0) {Jarvice.CBK=false; Jarvice.CBQ=false;}
                        else if (((1L<<start)&Jarvice.WR&(1L<<63))!=0) {Jarvice.CWK=false;}
                        else if (((1L<<start)&Jarvice.WR&(1L<<56))!=0) {Jarvice.CWQ=false;}
                        else if (((1L<<start)&Jarvice.BR&(1L<<7))!=0) {Jarvice.CBK=false;}
                        else if (((1L<<start)&Jarvice.BR&1L)!=0) {Jarvice.CBQ=false;}
                    }
                    Jarvice.EP=Moves.makeMoveEP(Jarvice.WP|Jarvice.BP,moves.substring(i,i+4));
                    Jarvice.WR=Moves.makeMoveCastle(Jarvice.WR, Jarvice.WK|Jarvice.BK, moves.substring(i,i+4), 'R');
                    Jarvice.BR=Moves.makeMoveCastle(Jarvice.BR, Jarvice.WK|Jarvice.BK, moves.substring(i,i+4), 'r');
                    Jarvice.WP=Moves.makeMove(Jarvice.WP, moves.substring(i,i+4), 'P');
                    Jarvice.WN=Moves.makeMove(Jarvice.WN, moves.substring(i,i+4), 'N');
                    Jarvice.WB=Moves.makeMove(Jarvice.WB, moves.substring(i,i+4), 'B');
                    Jarvice.WR=Moves.makeMove(Jarvice.WR, moves.substring(i,i+4), 'R');
                    Jarvice.WQ=Moves.makeMove(Jarvice.WQ, moves.substring(i,i+4), 'Q');
                    Jarvice.WK=Moves.makeMove(Jarvice.WK, moves.substring(i,i+4), 'K');
                    Jarvice.BP=Moves.makeMove(Jarvice.BP, moves.substring(i,i+4), 'p');
                    Jarvice.BN=Moves.makeMove(Jarvice.BN, moves.substring(i,i+4), 'n');
                    Jarvice.BB=Moves.makeMove(Jarvice.BB, moves.substring(i,i+4), 'b');
                    Jarvice.BR=Moves.makeMove(Jarvice.BR, moves.substring(i,i+4), 'r');
                    Jarvice.BQ=Moves.makeMove(Jarvice.BQ, moves.substring(i,i+4), 'q');
                    Jarvice.BK=Moves.makeMove(Jarvice.BK, moves.substring(i,i+4), 'k');
                    Jarvice.WhiteToMove=!Jarvice.WhiteToMove;
                    break;
                }
            }
        }
    }
    public static void inputQuit() {
        System.exit(0);
    }
    public static void inputPrint() {
        BoardGeneration.drawArray(Jarvice.WP,Jarvice.WN,Jarvice.WB,Jarvice.WR,Jarvice.WQ,Jarvice.WK,Jarvice.BP,Jarvice.BN,Jarvice.BB,Jarvice.BR,Jarvice.BQ,Jarvice.BK);
        System.out.print("Zobrist Hash: ");
        System.out.println(Zobrist.getZobristHash(Jarvice.WP,Jarvice.WN,Jarvice.WB,Jarvice.WR,Jarvice.WQ,Jarvice.WK,Jarvice.BP,Jarvice.BN,Jarvice.BB,Jarvice.BR,Jarvice.BQ,Jarvice.BK,Jarvice.EP,Jarvice.CWK,Jarvice.CWQ,Jarvice.CBK,Jarvice.CBQ,Jarvice.WhiteToMove));
    }
}
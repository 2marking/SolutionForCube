package com.example.leegyowon.solutionforcube;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cube {

    private Cubie[][][] cubiePos = new Cubie[3][3][3];

    public Cube() {
        cubiePos[0][0][0] = new Cubie(0, 0, 0,
                new CubieColor[]{new CubieColor('Y', 'U'), new CubieColor('R', 'L'), new CubieColor('G', 'F')}, true, false);
        cubiePos[1][0][0] = new Cubie(1, 0, 0,
                new CubieColor[]{new CubieColor('Y', 'U'), new CubieColor('G', 'F')}, false, true);
        cubiePos[2][0][0] = new Cubie(2, 0, 0,
                new CubieColor[]{new CubieColor('Y', 'U'), new CubieColor('G', 'F'), new CubieColor('O', 'R')}, true, false);

        cubiePos[0][0][1] = new Cubie(0, 0, 1,
                new CubieColor[]{new CubieColor('R', 'L'), new CubieColor('G', 'F')}, false, true);
        cubiePos[1][0][1] = new Cubie(1, 0, 1,
                new CubieColor[]{new CubieColor('G', 'F')}, false, false);
        cubiePos[2][0][1] = new Cubie(2, 0, 1,
                new CubieColor[]{new CubieColor('G', 'F'), new CubieColor('O', 'R')}, false, true);

        cubiePos[0][0][2] = new Cubie(0, 0, 2,
                new CubieColor[]{new CubieColor('W', 'D'), new CubieColor('R', 'L'), new CubieColor('G', 'F')}, true, false);
        cubiePos[1][0][2] = new Cubie(1, 0, 2,
                new CubieColor[]{new CubieColor('W', 'D'), new CubieColor('G', 'F')}, false, true);
        cubiePos[2][0][2] = new Cubie(2, 0, 2,
                new CubieColor[]{new CubieColor('W', 'D'), new CubieColor('G', 'F'), new CubieColor('O', 'R')}, true, false);

        cubiePos[0][1][0] = new Cubie(0, 1, 0,
                new CubieColor[]{new CubieColor('R', 'L'), new CubieColor('Y', 'U')}, false, true);
        cubiePos[1][1][0] = new Cubie(1, 1, 0,
                new CubieColor[]{new CubieColor('Y', 'U')}, false, false);
        cubiePos[2][1][0] = new Cubie(2, 1, 0,
                new CubieColor[]{new CubieColor('Y', 'U'), new CubieColor('O', 'R')}, false, true);

        cubiePos[0][1][1] = new Cubie(0, 1, 1,
                new CubieColor[]{new CubieColor('R', 'L')}, false, false);
        cubiePos[1][1][1] = new Cubie(1, 1, 1,
                new CubieColor[]{new CubieColor('A', 'A')}, //Just giving random, non-legitimate values for color and direction
                false, false);
        cubiePos[2][1][1] = new Cubie(2, 1, 1,
                new CubieColor[]{new CubieColor('O', 'R')}, false, false);

        cubiePos[0][1][2] = new Cubie(0, 1, 2,
                new CubieColor[]{new CubieColor('R', 'L'), new CubieColor('W', 'D')}, false, true);
        cubiePos[1][1][2] = new Cubie(1, 1, 2,
                new CubieColor[]{new CubieColor('W', 'D')}, false, false);
        cubiePos[2][1][2] = new Cubie(2, 1, 2,
                new CubieColor[]{new CubieColor('W', 'D'), new CubieColor('O', 'R')}, false, true);

        cubiePos[0][2][0] = new Cubie(0, 2, 0,
                new CubieColor[]{new CubieColor('Y', 'U'), new CubieColor('R', 'L'), new CubieColor('B', 'B')}, true, false);
        cubiePos[1][2][0] = new Cubie(1, 2, 0,
                new CubieColor[]{new CubieColor('Y', 'U'), new CubieColor('B', 'B')}, false, true);
        cubiePos[2][2][0] = new Cubie(2, 2, 0,
                new CubieColor[]{new CubieColor('Y', 'U'), new CubieColor('B', 'B'), new CubieColor('O', 'R')}, true, false);

        cubiePos[0][2][1] = new Cubie(0, 2, 1,
                new CubieColor[]{new CubieColor('R', 'L'), new CubieColor('B', 'B')}, false, true);
        cubiePos[1][2][1] = new Cubie(1, 2, 1,
                new CubieColor[]{new CubieColor('B', 'B')}, false, false);
        cubiePos[2][2][1] = new Cubie(2, 2, 1,
                new CubieColor[]{new CubieColor('B', 'B'), new CubieColor('O', 'R')}, false, true);

        cubiePos[0][2][2] = new Cubie(0, 2, 2,
                new CubieColor[]{new CubieColor('W', 'D'), new CubieColor('R', 'L'), new CubieColor('B', 'B')}, true, false);
        cubiePos[1][2][2] = new Cubie(1, 2, 2,
                new CubieColor[]{new CubieColor('W', 'D'), new CubieColor('B', 'B')}, false, true);
        cubiePos[2][2][2] = new Cubie(2, 2, 2,
                new CubieColor[]{new CubieColor('W', 'D'), new CubieColor('B', 'B'), new CubieColor('O', 'R')}, true, false);

    }

    public static boolean isSolvable(char[][][] colors) {
        List<Character> possibleColors = new ArrayList<>(Arrays.asList('R', 'Y', 'G', 'B', 'O', 'W'));
        int[] timesInputted = new int[6];

        for(char[][] i : colors) {
            for(char[] j : i) {
                for(char color : j) {
                    timesInputted[possibleColors.indexOf(color)] += 1;
                }
            }
        }

        for(int times : timesInputted) {
            if(times != 9) return false;
        }

        Cube cube = new Cube();
        cube.setAllColors(colors);

        for (Cubie[][] i : cube.cubiePos) {
            for (Cubie[] j : i) {
                for (Cubie cubie : j) {
                    CubieColor[] tempColors = cubie.getColors();
                    List<Character> tempList = new ArrayList<>();
                    for (CubieColor color : tempColors) {
                        tempList.add(color.getColor());
                    }

                    for (int k = 0; k < tempList.size() && k < 2; k++) {
                        if(tempList.lastIndexOf(tempList.get(k)) > k)
                            return false;
                    }
                }
            }
        }

        List<Character> opposites = new ArrayList<>(Arrays.asList('O', 'W', 'B', 'G', 'R', 'Y'));

        for (Cubie[][] i : cube.cubiePos) {
            for (Cubie[] j : i) {
                for (Cubie cubie : j) {
                    CubieColor[] tempColors = cubie.getColors();

                    for (int k = 0; k < tempColors.length && k < 2; k++) {
                        char color = tempColors[k].getColor();
                        if(color != 'A' &&
                                cubie.getDirOfColor((opposites
                                        .get(possibleColors
                                                .indexOf(color)))) != 'A')
                            return false;
                    }
                }
            }
        }
        cube.makeSunflower();
        cube.makeWhiteCross();
        cube.finishWhiteLayer();
        cube.insertAllEdges();

        int oriented = 0;
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (cube.cubiePos[x][y][0].isEdgeCubie() &&
                        cube.cubiePos[x][y][0].getDirOfColor('Y') == 'U')
                    oriented++;
            }
        }

        if (oriented % 2 != 0) {
            return false;
        }

        int twists = 0;
        for (int i = 0; i < 4; i++) {
            char dir = cube.cubiePos[0][0][0].getDirOfColor('Y');
            if (dir == 'L')
                twists++;
            else if (dir == 'F')
                twists += 2;

            cube.turn("U");
        }

        if(twists % 3 != 0) {
            return false;
        }

        return true;
    }

    public static String generateRandScramble() {
        String scramble = "";
        char[] possMoves = new char[]{'U', 'D', 'R', 'L', 'F', 'B'}; //The allowed set of moves
        char prevMove = possMoves[(int) (Math.random() * 6)]; //Pick random moves as prevMove and secondLastMove for now
        char secondLastMove = possMoves[(int) (Math.random() * 6)];
        for (int numMoves = 0; numMoves < 20; ) {
            char move = possMoves[(int) (Math.random() * 6)]; //Pick a random move
            if (move != prevMove && move != secondLastMove) {
                int rand = (int) (Math.random() * 100);
                if (rand < 33) {
                    scramble += move + "2 ";
                } else if (rand < 67) {
                    scramble += move + "' ";
                } else {
                    scramble += move + " ";
                }
                secondLastMove = prevMove;
                prevMove = move;
                numMoves++;
            }
        }
        return scramble;
    }

    public void turn(String turn) {
        char[] preChange; //Directions prior to turning
        char[] postChange; //What the directions change to after the turn
        Cubie[][] matrix = new Cubie[3][3]; //matrix to be rotated

        switch (turn) {
            case "B":
                preChange = new char[]{'B', 'U', 'R', 'D', 'L'};
                postChange = new char[]{'B', 'L', 'U', 'R', 'D'};
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        matrix[i][j] = cubiePos[Math.abs(j - 2)][2][i];
                    }
                }
                matrix = rotateMatrix(matrix, 90, preChange, postChange);
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        cubiePos[Math.abs(j - 2)][2][i] = matrix[i][j];
                    }
                }
                break;

            case "B'":
                preChange = new char[]{'B', 'U', 'R', 'D', 'L'};
                postChange = new char[]{'B', 'R', 'D', 'L', 'U'};
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        matrix[i][j] = cubiePos[Math.abs(j - 2)][2][i];
                    }
                }
                matrix = rotateMatrix(matrix, -90, preChange, postChange);

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        cubiePos[Math.abs(j - 2)][2][i] = matrix[i][j];
                    }
                }
                break;

            case "D":
                preChange = new char[]{'D', 'L', 'B', 'R', 'F'};
                postChange = new char[]{'D', 'F', 'L', 'B', 'R'};
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        matrix[i][j] = cubiePos[j][i][2];
                    }
                }
                matrix = rotateMatrix(matrix, 90, preChange, postChange);

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        cubiePos[j][i][2] = matrix[i][j];
                    }
                }
                break;

            case "D'":
                preChange = new char[]{'D', 'F', 'L', 'B', 'R'};
                postChange = new char[]{'D', 'L', 'B', 'R', 'F'};
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        matrix[i][j] = cubiePos[j][i][2];
                    }
                }
                matrix = rotateMatrix(matrix, -90, preChange, postChange);

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        cubiePos[j][i][2] = matrix[i][j];
                    }
                }
                break;

            case "E":
                preChange = new char[]{'L', 'B', 'R', 'F'};
                postChange = new char[]{'F', 'L', 'B', 'R'};
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        matrix[i][j] = cubiePos[j][i][1];
                    }
                }
                matrix = rotateMatrix(matrix, 90, preChange, postChange);

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        cubiePos[j][i][1] = matrix[i][j];
                    }
                }
                break;

            case "E'":
                preChange = new char[]{'F', 'L', 'B', 'R'};
                postChange = new char[]{'L', 'B', 'R', 'F'};
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        matrix[i][j] = cubiePos[j][i][1];
                    }
                }
                matrix = rotateMatrix(matrix, -90, preChange, postChange);

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        cubiePos[j][i][1] = matrix[i][j];
                    }
                }
                break;

            case "F":
                preChange = new char[]{'F', 'U', 'R', 'D', 'L'};
                postChange = new char[]{'F', 'R', 'D', 'L', 'U'};
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        matrix[i][j] = cubiePos[j][0][i];
                    }
                }
                matrix = rotateMatrix(matrix, 90, preChange, postChange);

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        cubiePos[j][0][i] = matrix[i][j];
                    }
                }
                break;

            case "F'":
                preChange = new char[]{'F', 'U', 'R', 'D', 'L'};
                postChange = new char[]{'F', 'L', 'U', 'R', 'D'};
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        matrix[i][j] = cubiePos[j][0][i];
                    }
                }
                matrix = rotateMatrix(matrix, -90, preChange, postChange);

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        cubiePos[j][0][i] = matrix[i][j];
                    }
                }
                break;

            case "L":
                preChange = new char[]{'L', 'B', 'D', 'F', 'U'};
                postChange = new char[]{'L', 'U', 'B', 'D', 'F'};
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        matrix[i][j] = cubiePos[0][Math.abs(j - 2)][i];
                    }
                }
                matrix = rotateMatrix(matrix, 90, preChange, postChange);

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        cubiePos[0][Math.abs(j - 2)][i] = matrix[i][j];
                    }
                }
                break;

            case "L'":
                preChange = new char[]{'L', 'U', 'B', 'D', 'F'};
                postChange = new char[]{'L', 'B', 'D', 'F', 'U'};
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        matrix[i][j] = cubiePos[0][Math.abs(j - 2)][i];
                    }
                }
                matrix = rotateMatrix(matrix, -90, preChange, postChange);

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        cubiePos[0][Math.abs(j - 2)][i] = matrix[i][j];
                    }
                }
                break;

            case "M":
                preChange = new char[]{'B', 'D', 'F', 'U'};
                postChange = new char[]{'U', 'B', 'D', 'F'};
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        matrix[i][j] = cubiePos[1][Math.abs(j - 2)][i];
                    }
                }
                matrix = rotateMatrix(matrix, 90, preChange, postChange);

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        cubiePos[1][Math.abs(j - 2)][i] = matrix[i][j];
                    }
                }
                break;

            case "M'":
                preChange = new char[]{'U', 'B', 'D', 'F'};
                postChange = new char[]{'B', 'D', 'F', 'U'};
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        matrix[i][j] = cubiePos[1][Math.abs(j - 2)][i];
                    }
                }
                matrix = rotateMatrix(matrix, -90, preChange, postChange);

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        cubiePos[1][Math.abs(j - 2)][i] = matrix[i][j];
                    }
                }
                break;

            case "R":
                preChange = new char[]{'R', 'U', 'B', 'D', 'F'};
                postChange = new char[]{'R', 'B', 'D', 'F', 'U'};
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        matrix[i][j] = cubiePos[2][j][i];
                    }
                }
                matrix = rotateMatrix(matrix, 90, preChange, postChange);

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        cubiePos[2][j][i] = matrix[i][j];
                    }
                }
                break;

            case "R'":
                preChange = new char[]{'R', 'B', 'D', 'F', 'U'};
                postChange = new char[]{'R', 'U', 'B', 'D', 'F'};
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        matrix[i][j] = cubiePos[2][j][i];
                    }
                }
                matrix = rotateMatrix(matrix, -90, preChange, postChange);

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        cubiePos[2][j][i] = matrix[i][j];
                    }
                }
                break;

            case "S":
                preChange = new char[]{'U', 'R', 'D', 'L'};
                postChange = new char[]{'R', 'D', 'L', 'U'};
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        matrix[i][j] = cubiePos[j][1][i];
                    }
                }
                matrix = rotateMatrix(matrix, 90, preChange, postChange);

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        cubiePos[j][1][i] = matrix[i][j];
                    }
                }
                break;

            case "S'":
                preChange = new char[]{'U', 'R', 'D', 'L'};
                postChange = new char[]{'L', 'U', 'R', 'D'};
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        matrix[i][j] = cubiePos[j][1][i];
                    }
                }
                matrix = rotateMatrix(matrix, -90, preChange, postChange);

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        cubiePos[j][1][i] = matrix[i][j];
                    }
                }
                break;

            case "U":
                preChange = new char[]{'U', 'F', 'L', 'B', 'R'};
                postChange = new char[]{'U', 'L', 'B', 'R', 'F'};
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        matrix[i][j] = cubiePos[j][Math.abs(i - 2)][0];
                    }
                }
                matrix = rotateMatrix(matrix, 90, preChange, postChange);

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        cubiePos[j][Math.abs(i - 2)][0] = matrix[i][j];
                    }
                }
                break;

            case "U'":
                preChange = new char[]{'U', 'L', 'B', 'R', 'F'};
                postChange = new char[]{'U', 'F', 'L', 'B', 'R'};
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        matrix[i][j] = cubiePos[j][Math.abs(i - 2)][0];
                    }
                }
                matrix = rotateMatrix(matrix, -90, preChange, postChange);

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        cubiePos[j][Math.abs(i - 2)][0] = matrix[i][j];
                    }
                }
                break;
            case "x":
                performMoves("R M' L'");
                break;
            case "x'":
                performMoves("R' M L");
                break;
            case "y":
                performMoves("U E' D'");
                break;
            case "y'":
                performMoves("U' E D");
                break;
            case "z":
                performMoves("F S B'");
                break;
            case "z'":
                performMoves("F' S' B");
                break;
        }
    }

    private Cubie[][] rotateMatrix(Cubie[][] orig, int degrees, char[] preChange,
                                   char[] postChange) {
        Cubie[][] rotated = new Cubie[3][3];
        if (degrees == 90) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    rotated[i][j] = orig[j][i];
                }
            }

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < rotated[0].length / 2; j++) {
                    Cubie tempCubie = rotated[i][3 - j - 1];
                    rotated[i][3 - j - 1] = rotated[i][j];
                    rotated[i][j] = tempCubie;
                }
            }
        } else if (degrees == -90) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    rotated[i][j] = orig[j][i];
                }
            }

            for (int i = 0; i < rotated[0].length / 2; i++) {
                for (int j = 0; j < 3; j++) {
                    Cubie tempCubie = rotated[3 - i - 1][j];
                    rotated[3 - i - 1][j] = rotated[i][j];
                    rotated[i][j] = tempCubie;
                }
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                CubieColor[] tempColors = rotated[i][j].getColors();
                for (CubieColor tempColor : tempColors) {
                    int index = 6;
                    for (int x = 0; x < preChange.length; x++) {
                        if (tempColor.getDir() == preChange[x]) {
                            index = x;
                        }
                    }
                    if (index < postChange.length)
                        tempColor.setDir(postChange[index]);
                }
                rotated[i][j].setColors(tempColors);
            }
        }
        return rotated;
    }

    public String performMoves(String moves) {
        for (int i = 0; i < moves.length(); i++) {
            if (!moves.substring(i, i + 1).equals(" ")) {
                if (i != moves.length() - 1) {
                    if (moves.substring(i + 1, i + 2).compareTo("2") == 0) {
                        turn(moves.substring(i, i + 1));
                        turn(moves.substring(i, i + 1));
                        i++;
                    } else if (moves.substring(i + 1, i + 2).compareTo("'") == 0) {
                        turn(moves.substring(i, i + 2));
                        i++;
                    } else {
                        turn(moves.substring(i, i + 1));
                    }
                } else {
                    turn(moves.substring(i, i + 1));
                }
            }
        }
        return moves;
    }

    public void reverseMoves(String moves) {
        for (int i = 0; i < moves.length(); i++) {
            if (!moves.substring(i, i + 1).equals(" ")) { //Only check if there is a meaningful character
                if (i != moves.length() - 1) {
                    if (moves.substring(i + 1, i + 2).compareTo("2") == 0) {
                        turn(moves.substring(i, i + 1));
                        turn(moves.substring(i, i + 1));
                        i++;
                    } else if (moves.substring(i + 1, i + 2).compareTo("'") == 0) {
                        turn(moves.substring(i, i + 1));
                        i++;
                    } else {
                        turn(moves.substring(i, i + 1) + "'");
                    }
                } else {
                    turn(moves.substring(i, i + 1) + "'");
                }
            }
        }
    }

    private String optimizeMoves(String moves) {
        for (int i = 0; i < moves.length(); i++) {
            String move = moves.substring(i, i + 1);
            if (!move.equals(" ") && !move.equals("'") && !move.equals("2")) {
                if (i <= moves.length() - 3) {
                    if (moves.substring(i + 1, i + 2).compareTo("2") == 0) {
                        if (i <= moves.length() - 4 && moves.charAt(i + 3) == moves.charAt(i)) {
                            if (i <= moves.length() - 5) {
                                if (moves.substring(i + 4, i + 5).compareTo("2") == 0) {
                                    moves = moves.substring(0, i) + moves.substring(i + 5);
                                    i--;
                                } else if (moves.substring(i + 4, i + 5).compareTo("'") == 0) {
                                    moves = moves.substring(0, i) + moves.substring(i, i + 1)
                                            + moves.substring(i + 5);
                                    i--;
                                } else {
                                    moves = moves.substring(0, i) + moves.substring(i, i + 1) + "'"
                                            + moves.substring(i + 4);
                                    i--;
                                }
                            } else {
                                moves = moves.substring(0, i) + moves.substring(i, i + 1) + "'"
                                        + moves.substring(i + 4);
                                i--;
                            }
                        }
                    } else if (moves.substring(i + 1, i + 2).compareTo("'") == 0) { //Clockwise turn
                        if (i <= moves.length() - 4 && moves.charAt(i + 3) == moves.charAt(i)) {
                            if (i <= moves.length() - 5) {
                                if (moves.substring(i + 4, i + 5).compareTo("2") == 0) {
                                    moves = moves.substring(0, i) + moves.substring(i, i + 1)
                                            + moves.substring(i + 5);
                                    i--;
                                } else if (moves.substring(i + 4, i + 5).compareTo("'") == 0) {
                                    moves = moves.substring(0, i) + moves.substring(i, i + 1) + "2"
                                            + moves.substring(i + 5);
                                    i--;
                                } else {
                                    moves = moves.substring(0, i) + moves.substring(i + 4);
                                    i--;
                                }
                            } else {
                                moves = moves.substring(0, i) + moves.substring(i + 4);
                                i--;
                            }
                        }
                    } else {
                        if (i <= moves.length() - 3 && moves.charAt(i + 2) == moves.charAt(i)) {
                            if (i <= moves.length() - 4) {
                                if (moves.substring(i + 3, i + 4).compareTo("2") == 0) {
                                    moves = moves.substring(0, i) + moves.substring(i, i + 1) + "'"
                                            + moves.substring(i + 4);
                                    i--;
                                } else if (moves.substring(i + 3, i + 4).compareTo("'") == 0) {
                                    moves = moves.substring(0, i) + moves.substring(i + 4);
                                    i--;
                                } else {
                                    moves = moves.substring(0, i) + moves.substring(i, i + 1) + "2"
                                            + moves.substring(i + 3);
                                    i--;
                                }
                            } else {
                                moves = moves.substring(0, i) + moves.substring(i, i + 1) + "2"
                                        + moves.substring(i + 3);
                                i--;
                            }
                        }

                    }
                }
            }
        }

        return moves;
    }

    public Cube scramble(String scramble) {
        performMoves("z2 " + scramble + " z2");
        return this;
    }

    public String makeWhiteCross() {
        String moves = "";

        while (numWhiteEdgesOriented() != 0) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (cubiePos[i][j][0].isEdgeCubie()) {
                        CubieColor[] tempColors = cubiePos[i][j][0].getColors();
                        if (tempColors[0].getColor() == 'W' || tempColors[1].getColor() == 'W') {
                            for (int k = 0; k < 2; k++) {
                                if ((tempColors[k].getColor() == 'R' && tempColors[k].getDir() == 'L') ||
                                        (tempColors[k].getColor() == 'G' && tempColors[k].getDir() == 'F') ||
                                        (tempColors[k].getColor() == 'O' && tempColors[k].getDir() == 'R') ||
                                        (tempColors[k].getColor() == 'B' && tempColors[k].getDir() == 'B')) {
                                    moves += performMoves(cubiePos[i][j][0].verticalFace(i, j) + "2 ");
                                }
                            }
                        }
                    }
                }
            }
            moves += performMoves("U ");
        }
        return optimizeMoves(moves);
    }

    public String makeSunflower() {
        String moves = "";

        if (numWhiteEdgesOriented() < 5) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (cubiePos[i][j][2].isEdgeCubie()) {
                        if (cubiePos[i][j][2].getDirOfColor('W') == 'D') {
                            moves += prepareSlot(i, j, 0, 'W');
                            char turnToMake = cubiePos[i][j][2].verticalFace(i, j);
                            moves += performMoves("" + turnToMake + "2 ");
                        }
                    }
                }
            }
        }

        if (numWhiteEdgesOriented() < 5) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (cubiePos[i][j][2].isEdgeCubie()) {
                        if (cubiePos[i][j][2].getDirOfColor('W') != 'A' && cubiePos[i][j][2].getDirOfColor('W') != 'D') {
                            char vert = cubiePos[i][j][2].verticalFace(i, j);
                            moves += prepareSlot(i, j, 0, 'W');
                            if (vert == 'F') {
                                moves += performMoves("F' U' R ");
                            } else if (vert == 'R') {
                                moves += performMoves("R' U' B ");
                            } else if (vert == 'B') {
                                moves += performMoves("B' U' L ");
                            } else if (vert == 'L') {
                                moves += performMoves("L' U' F ");
                            }
                        }
                    }
                }

            }
        }

        if (numWhiteEdgesOriented() < 5) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (cubiePos[i][j][1].isEdgeCubie()) {
                        CubieColor[] tempColors = cubiePos[i][j][1].getColors();
                        for (int k = 0; k < 2; k++) {
                            if (tempColors[k].getColor() == 'W') {
                                if (i == 0 && j == 0) {
                                    if (tempColors[k].getDir() == 'L') {
                                        moves += prepareSlot(1, 0, 0, 'W') + performMoves("F ");
                                    } else {
                                        moves += prepareSlot(0, 1, 0, 'W') + performMoves("L' ");
                                    }
                                } else if (i == 2 && j == 0) {
                                    if (tempColors[k].getDir() == 'F') {
                                        moves += prepareSlot(2, 1, 0, 'W') + performMoves("R ");
                                    } else {
                                        moves += prepareSlot(1, 0, 0, 'W') + performMoves("F' ");
                                    }
                                } else if (i == 2 && j == 2) {
                                    if (tempColors[k].getDir() == 'B') {
                                        moves += prepareSlot(2, 1, 0, 'W') + performMoves("R' ");
                                    } else {
                                        moves += prepareSlot(1, 2, 0, 'W') + performMoves("B ");
                                    }
                                } else {
                                    if (tempColors[k].getDir() == 'B') {
                                        moves += prepareSlot(0, 1, 0, 'W') + performMoves("L ");
                                    } else {
                                        moves += prepareSlot(1, 2, 0, 'W') + performMoves("B' ");
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }

        if (numWhiteEdgesOriented() < 5) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (cubiePos[i][j][0].isEdgeCubie()) {
                        if (cubiePos[i][j][0].getDirOfColor('W') != 'A' && cubiePos[i][j][0].getDirOfColor('W') != 'U') {
                            char vert = cubiePos[i][j][0].verticalFace(i, j);
                            if (vert == 'F') {
                                moves += performMoves("F U' R ");
                            } else if (vert == 'R') {
                                moves += performMoves("R U' B ");
                            } else if (vert == 'B') {
                                moves += performMoves("B U' L ");
                            } else if (vert == 'L') {
                                moves += performMoves("L U' F ");
                            }
                        }
                    }
                }

            }
        }

        if (numWhiteEdgesOriented() < 4) {
            moves += makeSunflower();
        }

        return optimizeMoves(moves);
    }

    private String prepareSlot(int x, int y, int z, char color) {
        int numUTurns = 0;
        CubieColor[] tempColor = cubiePos[x][y][z].getColors();
        while ((tempColor[0].getColor() == color || tempColor[1].getColor() == color) && numUTurns < 5) {
            performMoves("U");
            tempColor = cubiePos[x][y][z].getColors();
            numUTurns++;
        }

        if (numUTurns == 0 || numUTurns == 4) {
            return "";
        } else if (numUTurns == 1) {
            return "U ";
        } else if (numUTurns == 2) {
            return "U2 ";
        } else return "U' ";
    }

    private int numWhiteEdgesOriented() {
        int numOriented = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (cubiePos[i][j][0].isEdgeCubie()) {
                    if (cubiePos[i][j][0].getDirOfColor('W') == 'U') {
                        numOriented++;
                    }
                }
            }
        }
        return numOriented;
    }

    public String finishWhiteLayer() {
        String moves = "";
        moves += insertCornersInU() + " ";
        moves += insertMisorientedCorners() + " ";
        while (whiteCornerinU()) {
            moves += insertCornersInU() + " ";
            moves += insertMisorientedCorners() + " ";
        }
        return optimizeMoves(moves);
    }

    private boolean whiteCornerinU() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (cubiePos[i][j][0].isCornerCubie()) {
                    if (cubiePos[i][j][0].getDirOfColor('W') != 'A')
                        return true;
                }
            }
        }
        return false;
    }

    private String insertCornersInU() {
        String moves = "";

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (cubiePos[x][y][0].isCornerCubie() && cubiePos[x][y][0].isWhiteCorner()) {
                    if (x == 0) {
                        if (y == 0) {
                            moves += performMoves("U' ");
                        } else {
                            moves += performMoves("U2 ");
                        }
                    } else {
                        if (y == 2) {
                            moves += performMoves("U ");
                        }
                    }
                    y = 0;
                    x = 0;

                    int numUTurns = 0;
                    int yRotations = 0;
                    while (!whiteCornerPrepared()) {
                        performMoves("U y'");
                        numUTurns++;
                        yRotations++;
                    }
                    if (numUTurns == 1) {
                        moves += "U ";
                    } else if (numUTurns == 2) {
                        moves += "U2 ";
                    } else if (numUTurns == 3) {
                        moves += "U' ";
                    }
                    if (yRotations == 1) {
                        moves += "y' ";
                    } else if (yRotations == 2) {
                        moves += "y2 ";
                    } else if (yRotations == 3) {
                        moves += "y ";
                    }

                    int numSexyMoves = 0;
                    while (!cornerInserted(2, 0, 2)) {
                        performMoves("R U R' U'");
                        numSexyMoves++;
                    }
                    if (numSexyMoves == 5) { //5 sexy moves can be condensed into "U R U' R'"
                        moves += "U R U' R' ";
                    } else {
                        for (int k = 0; k < numSexyMoves; k++) {
                            moves += "R U R' U' ";
                        }
                    }
                }
            }
        }

        return moves;
    }

    private String insertMisorientedCorners() {
        String moves = "";
        for (int i = 0; i < 4; i++) {
            moves += performMoves("y ");
            if (!cornerInserted(2, 0, 2)) {
                if (cubiePos[2][0][2].isWhiteCorner()) {
                    if (!cornerInserted(2, 0, 2)) {
                        moves += performMoves("R U R' U' ");
                        moves += insertCornersInU();
                    }
                }
            }
        }
        return moves;
    }

    private boolean whiteCornerPrepared() {
        boolean whiteUp = false;

        if (cubiePos[2][0][0].isCornerCubie() && cubiePos[2][0][0].getDirOfColor('W') == 'A') {
            return false;
        }

        if (cubiePos[2][0][0].getDirOfColor('W') == 'U')
            whiteUp = true;

        if (whiteUp) {
            return (cubiePos[2][0][0].getColorOfDir('R') == cubiePos[1][0][1].getColors()[0].getColor() &&
                    cubiePos[2][0][0].getColorOfDir('F') == cubiePos[2][1][1].getColors()[0].getColor());
        } else {
            return (cubiePos[2][0][0].getColorOfDir('R') == cubiePos[2][1][1].getColors()[0].getColor() ||
                    cubiePos[2][0][0].getColorOfDir('F') == cubiePos[1][0][1].getColors()[0].getColor());
        }
    }

    private boolean cornerInserted(int x, int y, int z) {
        return cubiePos[x][y][z].isCornerCubie() &&
                ((cubiePos[x][y][z].getColorOfDir('D') == cubiePos[1][1][2].getColors()[0].getColor()) &&
                        (cubiePos[x][y][z].getColorOfDir('F') == cubiePos[1][0][1].getColors()[0].getColor()) &&
                        (cubiePos[x][y][z].getColorOfDir('R') == cubiePos[2][1][1].getColors()[0].getColor()));
    }

    public String insertAllEdges() {
        String moves = "";
        moves += insertEdgesInU() + " ";
        moves += insertMisorientedEdges() + " ";
        while (nonYellowEdgesInU()) {
            moves += insertEdgesInU() + " ";
            moves += insertMisorientedEdges() + " ";
        }
        return optimizeMoves(moves);
    }

    private boolean nonYellowEdgesInU() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (cubiePos[i][j][0].isEdgeCubie()) {
                    if (cubiePos[i][j][0].getDirOfColor('Y') == 'A')
                        return true;
                }
            }
        }
        return false;
    }

    private String insertEdgesInU() {
        String moves = "";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (cubiePos[i][j][0].isEdgeCubie() && cubiePos[i][j][0].getDirOfColor('Y') == 'A') {
                    if (j == 1) {
                        if (i == 0) {
                            moves += performMoves("U' ");
                        } else {
                            moves += performMoves("U ");
                        }
                    } else if (j == 2) {
                        moves += performMoves("U2 ");
                    }

                    int numUTurns = 0;
                    int yRotations = 0;
                    while (cubiePos[1][0][0].getColorOfDir('F') != cubiePos[1][0][1].getColors()[0].getColor()) {
                        performMoves("U y' ");
                        numUTurns++;
                        yRotations++;
                    }

                    if (numUTurns == 1) {
                        moves += "U ";
                    } else if (numUTurns == 2) {
                        moves += "U2 ";
                    } else if (numUTurns == 3) {
                        moves += "U' ";
                    }
                    if (yRotations == 1) {
                        moves += "y' ";
                    } else if (yRotations == 2) {
                        moves += "y2 ";
                    } else if (yRotations == 3) {
                        moves += "y ";
                    }

                    if (cubiePos[1][0][0].getColorOfDir('U') == cubiePos[0][1][1].getColors()[0].getColor()) {
                        moves += performMoves("U' L' U L U F U' F' ");
                    } else if (cubiePos[1][0][0].getColorOfDir('U') == cubiePos[2][1][1].getColors()[0].getColor()) {
                        moves += performMoves("U R U' R' U' F' U F ");
                    }

                    i = 0;
                    j = 0;
                }
            }
        }
        return moves;
    }

    private String insertMisorientedEdges() {
        String moves = "";
        for (int i = 0; i < 4; i++) {
            moves += performMoves("y ");
            if (cubiePos[2][0][1].getDirOfColor('Y') == 'A' &&
                    cubiePos[2][0][1].getColorOfDir('F') != cubiePos[1][0][1].getColors()[0].getColor()) {
                if (cubiePos[2][0][1].getColorOfDir('F') == cubiePos[2][1][1].getColors()[0].getColor() &&
                        cubiePos[2][0][1].getColorOfDir('R') == cubiePos[1][0][1].getColors()[0].getColor()) {
                    moves += performMoves("R U R' U2 R U2 R' U F' U' F ");
                } else {
                    moves += performMoves("R U R' U' F' U' F ");
                    moves += insertEdgesInU();
                }
            }
        }
        return moves;
    }

    private int numYellowEdgesOriented() {
        int numOriented = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (cubiePos[i][j][0].isEdgeCubie() && cubiePos[i][j][0].getDirOfColor('Y') == 'U')
                    numOriented++;
            }
        }
        return numOriented;
    }

    private int numYellowCornersOriented() {
        int numOriented = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (cubiePos[i][j][0].isCornerCubie() && cubiePos[i][j][0].getDirOfColor('Y') == 'U')
                    numOriented++;
            }
        }
        return numOriented;
    }

    private String yellowEdgeOrientation() {
        String status = "";
        int numOriented = numYellowEdgesOriented();

        if (numOriented == 4) {
            status = "Cross";
        } else if (numOriented == 0) {
            status = "Dot";
        } else if (numOriented == 2) {
            int[] xValues = new int[2];
            int index = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (cubiePos[i][j][0].isEdgeCubie() && cubiePos[i][j][0].getDirOfColor('Y') == 'U') {
                        xValues[index] = i;
                        index++;
                    }
                }
            }
            if (Math.abs(xValues[0] - xValues[1]) % 2 == 0) {
                status = "Bar";
            } else {
                status = "L";
            }
        }

        return status;
    }

    public String makeYellowCross() {
        String moves = "";
        String status = yellowEdgeOrientation();

        if (status.compareTo("Dot") == 0) {
            moves += performMoves("F R U R' U' F' U2 F U R U' R' F' ");
        } else if (status.compareTo("L") == 0) {
            while (cubiePos[0][1][0].getDirOfColor('Y') != 'U' || cubiePos[1][2][0].getDirOfColor('Y') != 'U') {
                moves += performMoves("U ");
            }
            moves += performMoves("F U R U' R' F' ");
        } else if (status.compareTo("Bar") == 0) {
            while (cubiePos[0][1][0].getDirOfColor('Y') != 'U' || cubiePos[2][1][0].getDirOfColor('Y') != 'U') {
                moves += performMoves("U ");
            }
            moves += performMoves("F R U R' U' F' ");
        }
        return moves;
    }

    public String orientLastLayer() {
        String moves = "";
        int numOriented = numYellowCornersOriented();
        while (numOriented != 4) {
            if (numOriented == 0) {
                while (cubiePos[0][0][0].getDirOfColor('Y') != 'L') {
                    moves += performMoves("U ");
                }
                moves += performMoves("R U R' U R U2 R' ");
            } else if (numOriented == 1) {
                while (cubiePos[0][0][0].getDirOfColor('Y') != 'U') {
                    moves += performMoves("U ");
                }
                moves += performMoves("R U R' U R U2 R' ");
            } else if (numOriented == 2) {
                while (cubiePos[0][0][0].getDirOfColor('Y') != 'F') {
                    moves += performMoves("U ");
                }
                moves += performMoves("R U R' U R U2 R' ");
            }
            numOriented = numYellowCornersOriented();
        }
        return moves;
    }

    public String permuteLastLayer() {
        String moves = "";
        int numHeadlights = 0;
        for (int i = 0; i < 4; i++) {
            turn("y");
            if (cubiePos[0][0][0].getColorOfDir('F') == cubiePos[2][0][0].getColorOfDir('F'))
                numHeadlights++;
        }

        if (numHeadlights == 0) {
            moves += performMoves("R' F R' B2 R F' R' B2 R2 ");
            numHeadlights = 1;
        }
        if (numHeadlights == 1) {
            while (cubiePos[0][2][0].getColorOfDir('B') != cubiePos[2][2][0].getColorOfDir('B')) {
                moves += performMoves("U ");
            }
            moves += performMoves("R' F R' B2 R F' R' B2 R2 ");
        }

        int numSolved = 0;
        for (int i = 0; i < 4; i++) {
            turn("y");
            if (cubiePos[0][0][0].getColorOfDir('F') == cubiePos[1][0][0].getColorOfDir('F'))
                numSolved++;
        }
        if (numSolved == 0) {
            moves += performMoves("R2 U R U R' U' R' U' R' U R' ");
            numSolved = 1;
        }
        if (numSolved == 1) {
            while (cubiePos[0][2][0].getColorOfDir('B') != cubiePos[1][2][0].getColorOfDir('B')) {
                moves += performMoves("U ");
            }
            if (cubiePos[1][0][0].getColorOfDir('F') == cubiePos[0][0][0].getColorOfDir('L')) {
                moves += performMoves("R2 U R U R' U' R' U' R' U R' ");
            } else {
                moves += performMoves("R U' R U R U R U' R' U' R2 ");
            }
        }

        while (cubiePos[0][0][0].getColorOfDir('F') != cubiePos[1][0][1].getColors()[0].getColor()) {
            moves += performMoves("U ");
        }

        return moves;
    }

    public char[][][] getColors() {
        char[][][] allSets = new char[6][3][3];
        char[][] left = new char[3][3];
        char[][] up = new char[3][3];
        char[][] front = new char[3][3];
        char[][] back = new char[3][3];
        char[][] right = new char[3][3];
        char[][] down = new char[3][3];

        for (int y = 2; y >= 0; y--) {
            for (int z = 2; z >= 0; z--) {
                left[Math.abs(y - 2)][Math.abs(z - 2)] = cubiePos[0][y][z].getColorOfDir('L');
            }
        }

        for (int x = 0; x <= 2; x++) {
            for (int y = 2; y >= 0; y--) {
                up[Math.abs(y - 2)][x] = cubiePos[x][y][0].getColorOfDir('U');
            }
        }

        for (int z = 0; z <= 2; z++) {
            for (int x = 0; x <= 2; x++) {
                front[z][x] = cubiePos[x][0][z].getColorOfDir('F');
            }
        }

        for (int x = 0; x <= 2; x++) {
            for (int z = 2; z >= 0; z--) {
                back[Math.abs(z - 2)][x] = cubiePos[x][2][z].getColorOfDir('B');
            }
        }

        for (int y = 2; y >= 0; y--) {
            for (int z = 0; z <= 2; z++) {
                right[Math.abs(y - 2)][z] = cubiePos[2][y][z].getColorOfDir('R');
            }
        }

        for (int x = 2; x >= 0; x--) {
            for (int y = 2; y >= 0; y--) {
                down[Math.abs(y - 2)][Math.abs(x - 2)] = cubiePos[x][y][2].getColorOfDir('D');
            }
        }

        allSets[0] = left;
        allSets[1] = up;
        allSets[2] = front;
        allSets[3] = back;
        allSets[4] = right;
        allSets[5] = down;

        return allSets;
    }

    public void setCubieColor(int x, int y, int z, char dir, char ncolor) {
        cubiePos[x][y][z].setColorOfDir(dir, ncolor);
    }

    public void testTurning() {
        for (int i = 0; i < cubiePos.length; i++) {
            for (int j = 0; j < cubiePos[0].length; j++) {
                for (int k = 0; k < cubiePos[0][0].length; k++) {
                    CubieColor[] tempColor = cubiePos[i][j][k].getColors();
                    Log.e("" + i + ", " + j + ", " + k + ", ", "" + k);
                    for (CubieColor aTempColor : tempColor) {
                        Log.e("" + aTempColor.getColor(), "" + aTempColor.getDir());
                    }
                }
            }
        }
    }

    public void setAllColors(char[][][] colors) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cubiePos[0][Math.abs(j - 2)][i].setColorOfDir('L', colors[0][i][j]);
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cubiePos[j][Math.abs(i - 2)][0].setColorOfDir('U', colors[1][i][j]);
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cubiePos[j][0][i].setColorOfDir('F', colors[2][i][j]);
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cubiePos[Math.abs(j - 2)][2][i].setColorOfDir('B', colors[3][i][j]);
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cubiePos[2][j][i].setColorOfDir('R', colors[4][i][j]);
                colors[4][i][j] = cubiePos[2][j][i].getColorOfDir('R');
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cubiePos[j][i][2].setColorOfDir('D', colors[5][i][j]);
            }
        }
    }

    public char getColor(int x, int y, int z, char dir) {
        return cubiePos[x][y][z].getColorOfDir(dir);
    }

}

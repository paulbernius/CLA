import java.util.Scanner;

/**
 * Paul Bernius
 *
 * PGM-CLA
 * April 29, 2022
 *
 * References:
 * "Chapter_3-indepth1-ALU-new.ppt" pg. 30
 */

public class CLA {
    public static void main(String[] args) {

        while (true) {
            Scanner scnr = new Scanner(System.in);
            String operand1, operand2;

            System.out.print("Enter operand 1 (binary) ('q' to quit): ");
            operand1 = scnr.nextLine();

            if (operand1.equalsIgnoreCase("q") || operand1.equalsIgnoreCase("quit")) {
                break;
            }

            System.out.print("Enter operand 2 (binary): ");
            operand2 = scnr.nextLine();

            operand1 = operand1.replaceAll(" ", "");
            operand2 = operand2.replaceAll(" ", "");
            CLAData test = new CLAData(operand1, operand2);

            test.printAllData();
        }
    }
}

class CLAData {
    // Initialize variables
    String operand1, operand2, g_i, p_i;
    int[] superProp = new int[4];
    int[] superGen = new int [4];

    public CLAData() { // Default constructor
        // Assign variables
        this.operand1 = "";
        this.operand2 = "";
        this.g_i = "";
        this.p_i = "";

        for (int i = 0; i < superProp.length; i++) {
            superProp[i] = 0;
            superGen[i] = 0;
        }
    }

    public CLAData(String operand1, String operand2) { // Overloaded constructor
        // Assign variables
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.p_i = computeP_i();
        this.g_i = computeG_i();

        // Create a temporary array with substrings of p_i to make computation of SuperProp easier
        String[] P = new String[4];
        P[0] = p_i.substring(12, 16);
        P[1] = p_i.substring(8, 12);
        P[2] = p_i.substring(4, 8);
        P[3] = p_i.substring(0, 4);

        // Starting from end of array, compute each section of SuperProp
        for (int i = 3; i >= 0; i--) {
            superProp[i] = computeSuperProp(P[i]);
        }

        // Computer SuperGen
        superGen = computeSuperGen();

    }

    public int computeCarryOut() {
        // Return result to CarryOut equation 'C4 = G3 + (P3 * G2) + (P3 * P2 * G1) + (P3 * P2 * P1 * G0) + (P3 * P2 * P1 * P0 * c0)'
        return superGen[3] + (superProp[3] * superGen[2]) + (superProp[3] * superProp[2] * superGen[1]) + (superProp[3] * superProp[2] * superProp[1] * superGen[0]) + (superProp[3] * superProp[2] * superProp[1] * superProp[0] * 0); // c0 = 0, no CarryIn
    }

    public int[] computeSuperGen() {
        // Return result to Super Generate equations
        int[] G = new int[4];

        // 'G0 = g3 + (p3 * g2) + (p3 * p2 * g1) + (p3 * p2 * p1 * g0)'
        G[0] = Character.getNumericValue(g_i.charAt(12)) + (Character.getNumericValue(p_i.charAt(12)) * Character.getNumericValue(g_i.charAt(13))) + (Character.getNumericValue(p_i.charAt(12)) * Character.getNumericValue(p_i.charAt(13)) * Character.getNumericValue(g_i.charAt(14))) + (Character.getNumericValue(p_i.charAt(12)) * Character.getNumericValue(p_i.charAt(13)) * Character.getNumericValue(p_i.charAt(14)) * Character.getNumericValue(g_i.charAt(15)));
        // 'G1 = g7 + (p7 * g6) + (p7 * p6 * g5) + (p7 * p6 * p5 * g4)'
        G[1] = Character.getNumericValue(g_i.charAt(8)) + (Character.getNumericValue(p_i.charAt(8)) * Character.getNumericValue(g_i.charAt(9))) + (Character.getNumericValue(p_i.charAt(8)) * Character.getNumericValue(p_i.charAt(9)) * Character.getNumericValue(g_i.charAt(10))) + (Character.getNumericValue(p_i.charAt(8)) * Character.getNumericValue(p_i.charAt(9)) * Character.getNumericValue(p_i.charAt(10)) * Character.getNumericValue(g_i.charAt(11)));
        // 'G2 = g11 + (p11 * g10) + (p11 * p10 * g9) + (p11 * p10 * p9 * g8)'
        G[2] = Character.getNumericValue(g_i.charAt(4)) + (Character.getNumericValue(p_i.charAt(4)) * Character.getNumericValue(g_i.charAt(5))) + (Character.getNumericValue(p_i.charAt(4)) * Character.getNumericValue(p_i.charAt(5)) * Character.getNumericValue(g_i.charAt(6))) + (Character.getNumericValue(p_i.charAt(4)) * Character.getNumericValue(p_i.charAt(5)) * Character.getNumericValue(p_i.charAt(6)) * Character.getNumericValue(g_i.charAt(7)));
        // 'G3 = g15 + (p15 * g14) + (p15 * p14 * g13) + (p15 * p14 * p13 * g12)'
        G[3] = Character.getNumericValue(g_i.charAt(0)) + (Character.getNumericValue(p_i.charAt(0)) * Character.getNumericValue(g_i.charAt(1))) + (Character.getNumericValue(p_i.charAt(0)) * Character.getNumericValue(p_i.charAt(1)) * Character.getNumericValue(g_i.charAt(2))) + (Character.getNumericValue(p_i.charAt(0)) * Character.getNumericValue(p_i.charAt(1)) * Character.getNumericValue(p_i.charAt(2)) * Character.getNumericValue(g_i.charAt(3)));

        return G;
    }

    public int computeSuperProp(String x) {
        // Return result to Super Propagate equations
        // "Super propagates (P3, P2, P1, P0) are simply the AND of the lower-level propagates"
        return Character.getNumericValue(x.charAt(0)) * Character.getNumericValue(x.charAt(1)) * Character.getNumericValue(x.charAt(2)) * Character.getNumericValue(x.charAt(3));
    }

    public String computeP_i() {
        // Return result to propagate which is just the OR function
        return OR(operand1, operand2);
    }

    public String computeG_i() {
        // Return result to generate which is just the AND function
        return AND(operand1, operand2);
    }

    public String OR(String operand1, String operand2) {
        // Returns result to OR function
        // 1 & 1 = 1
        // 1 & 0 = 1
        // 0 & 1 = 1
        // 0 & 0 = 0

        String result = "";

        for (int i = 0; i < operand1.length(); i ++) {
            if (operand1.charAt(i) == '1' || operand2.charAt(i) == '1') {
                result = result + "1";
            } else {
                result = result + "0";
            }
        }

        return result;
    }

    public String AND(String operand1, String operand2) {
        // Returns result to AND function
        // 1 & 1 = 1
        // 1 & 0 = 0
        // 0 & 1 = 0
        // 0 & 0 = 0

        String result = "";

        for (int i = 0; i < operand1.length(); i ++) {
            if (operand1.charAt(i) == '1' && operand2.charAt(i) == '1') {
                result = result + "1";
            } else {
                result = result + "0";
            }
        }

        return result;
    }

    public void printAllData() {
        // Calls all print functions to print all data
        printBinaryOperands();
        printGenAndProp();
        printSuperProp();
        printSuperGen();
        printCarryOut();
        System.out.println();
    }

    public void printBinaryOperands() {
        // Print binary operators
        System.out.println();
        System.out.println("Operand 1: " + operand1.substring(0, 4) + " " + operand1.substring(4, 8) + " " + operand1.substring(8, 12) + " " + operand1.substring(12, 16));
        System.out.println("Operand 2: " + operand2.substring(0, 4) + " " + operand2.substring(4, 8) + " " + operand2.substring(8, 12) + " " + operand2.substring(12, 16));
    }

    public void printGenAndProp() {
        // Print Generate and Propagate
        System.out.println();
        System.out.println("g_i: " + g_i.substring(0, 4) + " " + g_i.substring(4, 8) + " " + g_i.substring(8, 12) + " " + g_i.substring(12, 16));
        System.out.println("p_i: " + p_i.substring(0, 4) + " " + p_i.substring(4, 8) + " " + p_i.substring(8, 12) + " " + p_i.substring(12, 16));
    }

    public void printSuperProp() {
        // Print Super Propagate
        System.out.println();
        for (int i = superProp.length - 1; i >= 0; i--) {
            System.out.println("P" + i + " = " + superProp[i]);
        }
    }

    public void printSuperGen() {
        // Print Super Generate
        System.out.println();
        for (int i = 0; i < superGen.length; i++) {
            System.out.println("G" + i + " = " + superGen[i]);
        }
    }

    public void printCarryOut() {
        // Print Carry Out
        System.out.println();
        System.out.println("CarryOut_15: " + computeCarryOut());
    }
}

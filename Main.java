package encryptdecrypt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

abstract class Algo {
    public abstract char[] codeUnicode(String input, int key);
    public abstract char[] codeShift(String input, int key);
}

class Encoding extends Algo {

    @Override
    public char[] codeUnicode(String input, int key) {
        char[] charsFromInput = new char[input.length()];
        charsFromInput = input.toCharArray();
        for (int i = 0; i < input.length(); i++) {
            charsFromInput[i] += key;
        }
        return charsFromInput;
    }

    @Override
    public char[] codeShift(String input, int key) {
        char[] charsFromInput = new char[input.length()];
        int j = 0;
        for (char character : input.toCharArray()) {
            if (character >= 'a' && character <= 'z') {
                int originalAlphabetPosition = character - 'a';
                int newAlphabetPosition = (originalAlphabetPosition + key) % 26;
                char newCharacter = (char) ('a' + newAlphabetPosition);
                charsFromInput[j] = newCharacter;
                j++;
            } else if (character >= 'A' && character <= 'Z') {
                int originalAlphabetPosition = character - 'A';
                int newAlphabetPosition = (originalAlphabetPosition + key) % 26;
                char newCharacter = (char) ('A' + newAlphabetPosition);
                charsFromInput[j] = newCharacter;
                j++;
            } else {
                charsFromInput[j] = character;
                j++;
            }
        }
        return charsFromInput;
    }
}

class Decoding extends Algo {
    @Override
    public char[] codeUnicode(String input, int key) {
        char[] charsFromInput = new char[input.length()];
        charsFromInput = input.toCharArray();
        for (int i = 0; i < input.length(); i++) {
            charsFromInput[i] -= key;
        }
        return charsFromInput;
    }

    @Override
    public char[] codeShift(String input, int key) {
        char[] neverToBeCalled = new char[0];
        return neverToBeCalled;
    }
}

public class Main {

    public static void main(String[] args) {

        String op = "enc";
        int key = 0;
        String input = "";
        String in = "";
        String out = "";
        File outFile = null;
        String alg = "shift";
        char[] charsFromInput = input.toCharArray();

        for (int i = 0; i < args.length; i++) {
            if (args[i].contains("-mode")) {
                op = args[i + 1];
            }
            if (args[i].contains("-key")) {
                key = Integer.parseInt(args[i + 1]);
            }
            if (args[i].contains("-data")) {
                input = args[i + 1];
            }
            if (args[i].contains("-in")) {
                in = args[i + 1];
                File inFile = new File(in);
                try (Scanner scanner = new Scanner(inFile)) {
                    input = scanner.nextLine();
                } catch (FileNotFoundException e) {
                    System.out.println("No file found: " + inFile);
                }
            }
            if (args[i].contains("-out")) {
                out = args[i + 1];
                outFile = new File(out);
            }
            if (args[i].contains("-alg")) {
                alg = args[i + 1];
            }
        }

        Algo algo = null;
        if (op.equals("enc")) {
            algo = new Encoding();
            if (alg.equals("shift")) {
                charsFromInput = algo.codeShift(input, key);
            } else if (alg.equals("unicode")) {
                charsFromInput = algo.codeUnicode(input, key);
            }
        } else if (op.equals("dec")) {
            if (alg.equals("shift")) {
                algo = new Encoding();
                charsFromInput = algo.codeShift(input, 26 - (key % 26));
            } else if (alg.equals("unicode")) {
                algo = new Decoding();
                charsFromInput = algo.codeUnicode(input, key);
            }
        }

        if (out != "") {
            try (FileWriter writer = new FileWriter(outFile) ) {
                writer.write(charsFromInput);
            } catch (IOException e) {
                System.out.println("No file found: " + outFile);
            }
        } else {
            System.out.print(charsFromInput);
        }


    }
}
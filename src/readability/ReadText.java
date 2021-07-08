package readability;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

@SuppressWarnings("unused")
public class ReadText implements IndexRead {
    private final File file;
    private final ArrayList<String> words = new ArrayList<>();
    private int characters = 0;
    private int sentences = 0;
    private int syllables = 0;
    private int polysyllables = 0;

    public ReadText(String path) {
        this.file = new File(path);
        checkWords();
        checkSyllables();
        printStructure();
        printResults();
    }

    private void checkWords() {
        try (var scanner = new Scanner(file)) {
            System.out.println("The text is:");
            while (scanner.hasNextLine()) {
                var line = scanner.nextLine();
                System.out.println(line);
                this.words.addAll(Arrays.asList(line.split("\\s+")));
                this.sentences += line.split("[.?!]+").length;
                this.characters += line.replaceAll("\\s", "").length();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
    }

    private void checkSyllables() {
        for (var i = 0; i < words.size(); i++) {
            words.set(i, words.get(i).replaceAll("e\\b", "").
                    replaceAll("[aeiouy]{2}", "a"));
            this.syllables += countSyllables(words.get(i));
            if (countSyllables(words.get(i)) > 2)
                this.polysyllables += 1;
        }
    }

    private int countSyllables(String word) {
        String vowels = word.replaceAll("(?i)[^aeiouy]+", "");
        return vowels.isEmpty() ? 1 : vowels.length();
    }

    private void printStructure() {
        System.out.println("\nWords: " + this.words.size());
        System.out.println("Sentences: " + this.sentences);
        System.out.println("Characters: " + this.characters);
        System.out.println("Syllables: " + this.syllables);
        System.out.println("Polysyllables: " + this.polysyllables);
    }

    private void printResults() {
        System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): all\n");
        System.out.printf("Automated Readability Index: %.2f (about %d-year-olds).%n",
                checkARI(this.characters, this.words.size(), this.sentences),
                checkAge((int) Math.round(checkARI(this.characters, this.words.size(), this.sentences))));
        System.out.printf("Flesch–Kincaid readability tests: %.2f (about %d-year-olds).%n",
                checkFK(this.words.size(), this.sentences, this.syllables),
                checkAge((int) Math.round(checkFK(this.words.size(), this.sentences, this.syllables))));
        System.out.printf("Simple Measure of Gobbledygook: %.2f (about %d-year-olds).%n",
                checkSMOG(this.polysyllables, this.sentences),
                checkAge((int) Math.round(checkSMOG(this.polysyllables, this.sentences))));
        System.out.printf("Coleman–Liau index: %.2f (about %d-year-olds).%n%n",
                checkCL(this.words.size(), this.characters, this.sentences),
                checkAge((int) Math.round(checkCL(this.words.size(), this.characters, this.sentences))));
        System.out.printf("This text should be understood in average by %.2f-year-olds.%n",
                checkAverageIndex(this.characters, this.words.size(), this.sentences, this.syllables,
                        this.polysyllables));
    }
}
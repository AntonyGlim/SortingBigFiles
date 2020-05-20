package glim.antony.generator;

import glim.antony.generator.dictionaries.AuthorDictionary;
import glim.antony.generator.dictionaries.QuoteDictionary;

import java.io.FileWriter;
import java.io.IOException;
import java.util.SplittableRandom;

public class Generator {

    private static final int ROWS_COUNT = 10;
    private static final String FILE_NAME = "QuotesAndAuthors.txt";
    private static final SplittableRandom RANDOM = new SplittableRandom();

    public static void main(String[] args) throws IOException {
        try(FileWriter fileWriter = new FileWriter(FILE_NAME, true);) {
            for (int i = 0; i < ROWS_COUNT; i++) {
                String quote = QuoteDictionary.getQUOTES()[RANDOM.nextInt(0, QuoteDictionary.getQUOTES().length)];
                String author = AuthorDictionary.getAUTHORS()[RANDOM.nextInt(0, AuthorDictionary.getAUTHORS().length)];
                fileWriter.write(quote + "\t" + author + "\n");
                fileWriter.flush();
            }
        }
    }
}

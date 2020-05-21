package glim.antony.sorter;

import java.io.*;
import java.util.*;

public class Sorter {

    private static final String INPUT_FILE_NAME = "QuotesAndAuthors.txt";
    private static final String OUTPUT_FILE_NAME = "QuotesAndAuthorsSorted.txt";

    public static void main(String[] args) throws IOException {

        Map<String, List<Long>> authorsAndFirstSymbolsInLine = new TreeMap<>();

        try (FileReader fileReader = new FileReader(INPUT_FILE_NAME);
             BufferedReader bufferedReader = new BufferedReader(fileReader)
        ) {
            long index = 0;
            String line = bufferedReader.readLine();

            while (line != null) {

                String author = line.split("\t")[1];

                if (!authorsAndFirstSymbolsInLine.containsKey(author)) {
                    authorsAndFirstSymbolsInLine.put(author, new ArrayList<>());
                }
                authorsAndFirstSymbolsInLine.get(author).add(index);
                index += line.length() + 1;
                line = bufferedReader.readLine();
            }

            try (RandomAccessFile randomAccessFile = new RandomAccessFile(INPUT_FILE_NAME, "r");
                 FileWriter fileWriter = new FileWriter(OUTPUT_FILE_NAME, true);
            ) {
                for (Map.Entry<String, List<Long>> entry : authorsAndFirstSymbolsInLine.entrySet()) {
                    for (Long coordinates : entry.getValue()) {
                        randomAccessFile.seek(coordinates);
                        line = randomAccessFile.readLine();
                        fileWriter.write(line + "\n");
                        fileWriter.flush();
                    }
                }
            }
        }
    }
}

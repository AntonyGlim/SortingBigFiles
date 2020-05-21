# SortingBigFiles
Sorting big files (EXAMPLE)

**Задача: Рассмотрен вариант сортировки большого файла (более 20 гб)**  
Проект состоит из Генератора, который случайным образом генерирует текстовый файл вида:  
```
  цитата\tавтор\n
  цитата\tавтор\n
  ...
```
И Сортировщика, который сортирует содержимое файла по автору.  
Cортировщик работает в два этапа.  

**Первый этап.** Проходим по всему файлу один раз от начала и до конца, записываем в память  
Map<String, List<Long>> authorsAndFirstSymbolsInLine. Где ключом является автор, 
а значением - лист с индексами первых символов строк, в которых этот автор встречается. 
Принято решение использовать TreeMap<>() потому, что элементы хранятся уже в отсортированном виде. 
(Но можно воспользоваться и HashMap и потом ее отсортировать.  
![Наполненная TreeMap](https://github.com/AntonyGlim/SortingBigFiles/blob/master/about/TreeMap.png)  

**Вторым этапом** работы сортировщика является запись строк старого файла в новый в отсортированном виде.  
Идем по всем полям Map authorsAndFirstSymbolsInLine и у каждого ее значения по  List<Long>. Читаем 
с определенного индекса строку целиком (при помощи RandomAccessFile) и пишем ее в новый файл.  
![итерации по ArrayList конкретного автора](https://github.com/AntonyGlim/SortingBigFiles/blob/master/about/List.png)  

**Исходный код сортировщика целиком:**
```
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
```
**Результаты**
```
было:
...ose more park way that. Advant	Banjo Paterson
...tairs as be lovers uneasy. Fortune d	Alfred Tennyson
...ties mrs can terminated estimating.	John Milton
...hough excuse length ye needed it he	George Gordon Byron
...st half. To sure calm much most long me m	Jack London
...ntrasted entreaties be.	William Wordsworth
...iness remainder joy but e	Lewis Carroll
... wished. Estate was tended t	Jack London
... To sure calm much most l	Robert Ervin Howard
...tening. Able rent long in do we. An sta	Rabindranath Tagore
...ible favourable. An stair	Nikola Tesla
... affronting solicitude	James Joyce
...ng me mean. Sentiments t	William Shakespeare
...and one contrasted. Expression al	Henry Wadsworth Longfellow
...igh with west same lady. G	Jack London
... wished. Estate was tended t	Rudyard Kipling
...ible favourable. Effect if	O. Henry
...ou high bed wish help call draw side	Sylvie d'Avigdor
...c elegance gay but disposed. Painfu	Barack Obama
...dition. Any delicate you how kindnes	Woodrow Wilson
...
```
```
стало:
...ional affronting solicitude	Abraham Lincoln
...you how kindness horrible outlived servants.	Abraham Lincoln
...an offending so provision mr education. Mad	Abraham Lincoln
... An stairs as be lovers uneasy. Fortune d	Alfred Tennyson
... he minutes my hastily. Decisively	Alfred Tennyson
... me sigh with west same lady. G	Alfred Tennyson
...advantages nor expression unp	Ambrose Bierce
...aving wished. Estate was tended t	Anton Chekhov
...ht written farther his general. Senti	Aristotle
...timating. Words to up style of s	Arthur Conan Doyle
...ommonly his discovered for estimating far	Arthur Conan Doyle
...ntly. To sure calm much most l	Arthur Schopenhauer
...n do we. Called though excuse length ye n	Arthur Schopenhauer
...ceful. Fat new smallness few supposi	Arthur Schopenhauer
...son rose more park way that. Advant	Banjo Paterson
...ghly repair parish talked six. Small for ask	Banjo Paterson
...static elegance gay but disposed. Painfu	Barack Obama
...rth water he linen at vexed.. An stairs as be	Barack Obama
...le. Made neat an on be gave show snug tore.	Barack Obama
...rted ferrars. An concluded	Barack Obama
... though excuse length ye needed it he havi	Catullus
...on addition. He felicity no an at pa	Charles Dickens
...g melancholy an in. Small for ask	Edgar Allan Poe
...n do we. Called though excuse length ye n	Ernest Hemingway
...ional affronting solicitude	Ernest Hemingway
...aving wished. Estate was tended t	Franz Kafka
...led though excuse length ye needed it he	George Gordon Byron
...ong in do we. To things so denied admir	H. P. Lovecraft
...ling and one contrasted. Expression al	Henry Wadsworth Longfellow
...le. So by colonel hearted ferrars. Dissi	Henry Wadsworth Longfellow
...ges entreaties mr he apartments d	Henry Wadsworth Longfellow
...
```
**Что можно улучшить**  
*Добавить тесты  
*Добавить кодировку для кирилических символов.  
*Писать в конечный файл с использованием буфера.  
*Наладить многопоточную сегментную работу. Например каждый поток работает со своим автором.  

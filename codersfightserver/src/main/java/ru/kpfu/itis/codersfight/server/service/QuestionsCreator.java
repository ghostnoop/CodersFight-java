/**
 * @author Gilyazov Marat
 * 11-905
 */

package ru.kpfu.itis.codersfight.server.service;

import ru.kpfu.itis.codersfight.server.model.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Random;

public class QuestionsCreator {
    private static LinkedList<Question> questions=new LinkedList<>();

    public static void setQuestions(String path) throws IOException {
        InputStream inputStream = QuestionsCreator.class.getResourceAsStream(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        while (reader.ready()) {
            String line = reader.readLine();
            String[] attrs = line.split("::");
            questions.add(new Question(attrs));

        }
    }

    public static Question getNewQuestion() {
        Random random = new Random(System.currentTimeMillis());
        return questions.get(random.nextInt((questions.size() - 1) + 1));
    }
}

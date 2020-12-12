/**
 * @author Gilyazov Marat
 * 11-905
 */

package ru.kpfu.itis.codersfight.server.model;

import lombok.Getter;
import lombok.Setter;


public class Question {
    @Getter
    @Setter
    private String title;
    @Getter
    @Setter
    private String text;
    @Getter
    @Setter
    private String[] answers;
    private int rightAnswer;


    public Question(String title, String text, String[] answers, int rightAnswer) {
        this.title = title;
        this.text = text;
        this.answers = answers;
        this.rightAnswer = rightAnswer;
    }

    public boolean checkAnswer(int answer) {
        return answer==rightAnswer;
    }
}

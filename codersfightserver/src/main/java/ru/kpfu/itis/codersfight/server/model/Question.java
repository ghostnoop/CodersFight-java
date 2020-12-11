/**
 * @author Gilyazov Marat
 * 11-905
 */

package ru.kpfu.itis.codersfight.server.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Question {
    private String title;
    private String[] answers;
    private String rightAnswer;


    public Question(String title, String[] answers, String rightAnswer) {
        this.title = title;
        this.answers = answers;
        this.rightAnswer = rightAnswer;
    }
}

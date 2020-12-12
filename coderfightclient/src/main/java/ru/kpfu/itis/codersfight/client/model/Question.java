/**
 * @author Gilyazov Marat
 * 11-905
 */

package ru.kpfu.itis.codersfight.client.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Question {
    private String title;
    private String text;
    private String[] answers;
}

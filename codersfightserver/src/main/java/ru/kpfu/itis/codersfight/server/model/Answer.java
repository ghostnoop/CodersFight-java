/**
 * @author Gilyazov Marat
 * 11-905
 */

package ru.kpfu.itis.codersfight.server.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Answer {
    private boolean isRight;
    private long startTime;
    private long endTime;
    public boolean isGameEnd = false;


    public Answer(long startTime) {
        this.startTime = startTime;
    }

    public void gameEnd() {
        this.endTime = System.currentTimeMillis() - startTime;
        this.isGameEnd = Boolean.TRUE;
    }
}

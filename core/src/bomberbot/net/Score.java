package bomberbot.net;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author fiolet.bastien
 */
public class Score {
    private int score = 0;

    public Score() {
        this.score = 0;
    }

    public Score(int score) {
        this.score = score;
    }

    public void addDestroyBlockScore() {
        this.score += 50;
    }

    public void addKillScore() {
        this.score += 500;
    }

    public void resetScore() {
        this.score = 0;
    }

    public int getScore() {
        return score;
    }

    public String toString() {
        return this.score + "";
    }
}

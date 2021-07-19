package View;

import Database.User;

import java.util.List;

public class ScoreboardView {
    public void showScoreboard(List<User> users,List<Integer> ranks) {
        for (int i = 0; i < users.size(); i++) {
            System.out.println(ranks.get(i) + "-" + users.get(i).getNickname() + ":" + users.get(i).getScore() );
        }
    }
}

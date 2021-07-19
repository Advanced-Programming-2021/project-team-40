package Database.Cards;

import Gameplay.MonsterFieldArea;

public interface EquipEffect {
    void activate(MonsterFieldArea toEquipMonster);
    void deactivate(MonsterFieldArea toDeequipMonster);
    boolean isMonsterCorrect(MonsterFieldArea chosenMonster);
}

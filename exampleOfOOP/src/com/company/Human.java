package com.company;

public class Human extends Creature {
    Human(int damage, int armour, int range, int MAX_HEALTH, int movePoints, int attackPoints, String name,MainFrame frame){
        super(damage,armour,range,MAX_HEALTH,movePoints,attackPoints,name,frame);
    }

    @Override
    public void someAction() {
        if (this.isBotMode()){
            while (this.getMovePoints()>0 && !this.checkRange()) {
                this.findTo();
            }
            setMovePoints(this.getMAX_MOVE_POINTS());
        }
        if (this.checkRange()){
            while (this.getAttackPoints()>0 && checkTargetLife())
            this.attack();
        }
        setMovePoints(this.getMAX_MOVE_POINTS());
        setAttackPoints(this.getMAX_ATTACK_POINTS());
    }
}

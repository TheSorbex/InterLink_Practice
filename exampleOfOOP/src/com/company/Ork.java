package com.company;

public class Ork extends Creature{
    private int regeneration;

    Ork(int damage, int armour, int range, int MAX_HEALTH, int movePoints, int attackPoints, String name,MainFrame frame){
        super(damage,armour,range,MAX_HEALTH,movePoints,attackPoints,name,frame);
        this.regeneration = 1;
    }

    public void regenerate(){
        this.setHealth(this.getHealth()+this.regeneration);
    }

    @Override
    public void someAction() {
        if (this.isBotMode()){
            regenerate();
            while (this.getMovePoints()>0 && !this.checkRange()) {
                this.findTo();
            }
            if (this.checkRange()){
                while (this.getAttackPoints()>0)
                    this.attack();
            }
            setMovePoints(this.getMAX_MOVE_POINTS());
            setAttackPoints(this.getMAX_ATTACK_POINTS());
        }
    }
}

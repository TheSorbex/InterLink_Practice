package com.company;

import java.util.Random;

public class Elf extends Creature {
    private Random rand = new Random();
    private boolean evasion = false;
    private int armourKeeper = getArmour();
    Elf(int damage, int armour, int range, int MAX_HEALTH, int movePoints, int attackPoints, String name,MainFrame frame){
        super(damage,armour,range,MAX_HEALTH,movePoints,attackPoints,name,frame);
    }

    @Override
    public void someAction() {
        if (this.isBotMode()){
            if(evasion){
                evasion = false;
                setArmour(armourKeeper);
            } else {
                if (rand.nextInt(10)>8){
                    this.setArmour(1000);
                }
            }
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

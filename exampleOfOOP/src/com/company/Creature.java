package com.company;

import oracle.jrockit.jfr.JFR;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;


public abstract class Creature implements Bot {
    private Random rand = new Random();
    private boolean botMode = true;
    public boolean alive = true;
    private MainFrame frame;
    private int health;
    private int damage;
    private int armour;
    private int range;
    private int MAX_HEALTH;
    private int movePoints;
    private int attackPoints;
    public int MAX_MOVE_POINTS;
    public int MAX_ATTACK_POINTS;
    private String name;
    private String raceName;
    private Creature target;
    private ArrayList<Creature> enemyCreatures;
    private ArrayList<Creature> friendlyCreatures;
    private int x;
    private int y;

    Creature(int damage, int armour, int range, int MAX_HEALTH, int movePoints, int attackPoints, String name, MainFrame frame) {
        this.MAX_HEALTH = MAX_HEALTH;
        this.health = MAX_HEALTH;
        this.damage = damage;
        this.armour = armour;
        this.range = range;
        this.movePoints = movePoints;
        this.attackPoints = attackPoints;
        this.MAX_MOVE_POINTS = movePoints;
        this.MAX_ATTACK_POINTS = attackPoints;
        this.name = name;
        this.raceName = this.getClass().getSimpleName();
        this.frame = frame;
        this.enemyCreatures = new ArrayList<>();
        this.friendlyCreatures = new ArrayList<>();
        makeRandomCoordinates();
    }

    public void attack() {
        System.out.println(this.name + " атаковал " + this.getTarget().getName());
        target.receiveDamage(this.damage);
        this.attackPoints--;
    }

    public void receiveDamage(int incomeDamage) {
        if (this.armour < incomeDamage) {
            this.health = this.health - (incomeDamage - this.armour);
            System.out.println(this.name + " получил " + (incomeDamage - this.armour) + " урона");
        }
        if (this.health <= 0) {
            this.alive = false;
            System.out.println(this.name + " умер");
        }
    }

    public void sortingEnemiesAndFriends(ArrayList<Creature> creatures) {
        ArrayList<Creature> enemyCreatures = new ArrayList<>();
        ArrayList<Creature> friendlyCreatures = new ArrayList<>();
        for (Creature creature : creatures) {
            if (!this.getRaceName().equals(creature.getRaceName())) {
                enemyCreatures.add(creature);
            } else {
                friendlyCreatures.add(creature);
            }
        }
        this.enemyCreatures = enemyCreatures;
        this.friendlyCreatures = friendlyCreatures;

    }

    public void cleanUp() {
        if (!enemyCreatures.isEmpty()) {
            for (Creature creature : enemyCreatures) {
                if (!creature.isAlive()) {
                    this.enemyCreatures.remove(creature);
                }
            }
        }
        if (!friendlyCreatures.isEmpty()) {
            for (Creature creature : friendlyCreatures) {
                if (!creature.isAlive()) {
                    this.friendlyCreatures.remove(creature);
                }
            }
        }
    }

    public boolean checkTargetLife() {
        return this.getTarget().isAlive();
    }

    public void makeRandomCoordinates() {
        this.x = rand.nextInt(12);
        this.y = rand.nextInt(12);
    }

    public void chooseTarget() {
        if (!this.enemyCreatures.isEmpty()) {
            int k = rand.nextInt(this.enemyCreatures.size());
            this.target = this.enemyCreatures.get(k);
        }
    }

    public boolean checkRange() {
        int n, k;
        if ((this.x - this.target.getX()) < 0) {
            n = (this.x - this.target.getX()) * (-1);
        } else {
            n = this.x - this.target.getX();
        }
        if ((this.y - this.target.getY()) < 0) {
            k = (this.y - this.target.getY()) * (-1);
        } else {
            k = (this.y - this.target.getY());
        }
        if (this.range >= n && this.range >= k) {
            return true;
        } else {
            return false;
        }
    }

    public void moveTo(String where) {
        boolean bool = false;
        if (this.movePoints > 0) {
            if (where == "UP" && this.y != 0 && frame.getInfoFromField(this.x, this.y - 1) == " ") {
                bool = true;
                frame.setInfoToField(" ", this.x, this.y);
                this.y--;
            } else if (where == "DOWN" && this.y != frame.getMapLength() - 1 && frame.getInfoFromField(this.x, this.y + 1) == " ") {
                bool = true;
                frame.setInfoToField(" ", this.x, this.y);
                this.y++;
            } else if (where == "LEFT" && this.x != 0 && frame.getInfoFromField(this.x - 1, this.y) == " ") {
                bool = true;
                frame.setInfoToField(" ", this.x, this.y);
                this.x--;
            } else if (where == "RIGHT" && this.x != frame.getMapLength() - 1 && frame.getInfoFromField(this.x + 1, this.y + 1) == " ") {
                bool = true;
                frame.setInfoToField(" ", this.x, this.y);
                this.x++;
                ;
            }
        }
        if (bool) {
            this.movePoints--;
            frame.writeCreatures();
            frame.writeMap();
        }
    }

    public void findTo() {
        Random random = new Random();
        int a = random.nextInt(2);
        if (this.target.getX() > this.x && this.target.getY() > this.y) {
            if (a == 0) {
                moveTo("RIGHT");
            } else {
                moveTo("DOWN");
            }
        } else if (this.target.getX() > this.x && this.target.getY() < this.y) {
            if (a == 0) {
                moveTo("RIGHT");
            } else {
                moveTo("UP");
            }
        } else if (this.target.getX() < this.x && this.target.y > this.getY()) {
            if (a == 0) {
                moveTo("LEFT");
            } else {
                moveTo("DOWN");
            }
        } else if (this.target.getX() < this.x && this.target.getY() < this.y) {
            if (a == 0) {
                moveTo("LEFT");
            } else {
                moveTo("UP");
            }
        } else if (this.target.getX() == this.x && this.target.y > this.getY()) {
            moveTo("DOWN");
        } else if (this.target.getX() == this.x && this.target.y < this.getY()) {
            moveTo("UP");
        } else if (this.target.getX() < this.x && this.target.y == this.getY()) {
            moveTo("LEFT");
        } else if (this.target.getX() > this.x && this.target.y == this.getY()) {
            moveTo("RIGHT");
        }
    }

    public void cleverMoving() {
        int a = new Random().nextInt(2);
        if (this.x != this.frame.getMapLength() - 1 && this.y != this.frame.getMapLength() - 1 && this.x != 0 && this.y != 0) {
            if (this.getTarget().getX() > this.x && this.getTarget().getY() >= this.y) {
                if (this.frame.getInfoFromField(this.x,this.y+1).equals(" ") || this.frame.getInfoFromField(this.x+1,this.y+1).equals(" ")) {
                    if (a == 1 && this.frame.getInfoFromField(this.x,this.y-1).equals(" ")) {
                        moveTo("UP");
                    } else if (this.frame.getInfoFromField(this.x-1,this.y).equals(" ")) {
                        moveTo("LEFT");
                    } else {
                        findTo();
                    }
                } else {
                    findTo();
                }
            } else if (this.getTarget().getX() <= this.x && this.getTarget().getY() > this.y) {
                if (this.frame.getInfoFromField(this.x,this.y+1).equals(" ") || this.frame.getInfoFromField(this.x-1,this.y).equals(" ")) {
                    if (a == 1 && this.frame.getInfoFromField(this.x,this.y-1).equals(" ")) {
                        moveTo("UP");
                    } else if (this.frame.getInfoFromField(this.x+1,this.y).equals(" ")) {
                        moveTo("RIGHT");
                    } else {
                        findTo();
                    }
                } else {
                    findTo();
                }
            } else if (this.getTarget().getX() < this.x && this.getTarget().getY() <= this.y) {
                if (this.frame.getInfoFromField(this.x,this.y-1).equals(" ") || this.frame.getInfoFromField(this.x-1,this.y).equals(" ")) {
                    if (a == 1 && this.frame.getInfoFromField(this.x,this.y+1).equals(" ")) {
                        moveTo("DOWN");
                    } else if (this.frame.getInfoFromField(this.x+1,this.y).equals(" ")) {
                        moveTo("RIGHT");
                    } else {
                        findTo();
                    }
                } else {
                    findTo();
                }
            } else if (this.getTarget().getX() >= this.x && this.getTarget().getY() < this.y) {
                if (this.frame.getInfoFromField(this.x,this.y-1).equals(" ") || this.frame.getInfoFromField(this.x,this.y+1).equals(" ")) {
                    if (a == 1 && this.frame.getInfoFromField(this.x,this.y-1).equals(" ")) {
                        moveTo("DOWN");
                    } else if (this.frame.getInfoFromField(this.x-1,this.y).equals(" ")) {
                        moveTo("LEFT");
                    } else {
                        findTo();
                    }
                } else {
                    findTo();
                }
            } else {
                findTo();
            }
        } else {
            findTo();
        }
    }


    public void switchBotMode() {
        if (this.botMode) {
            this.botMode = false;
        } else {
            this.botMode = true;
        }
    }

    //Getters and Setters
    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public String getName() {
        return this.name;
    }

    public String getRaceName() {
        return raceName;
    }

    public boolean isBotMode() {
        return this.botMode;
    }

    public int getArmour() {
        return armour;
    }

    public int getAttackPoints() {
        return attackPoints;
    }

    public boolean isAlive() {
        return alive;
    }

    public int getDamage() {
        return damage;
    }

    public int getMAX_ATTACK_POINTS() {
        return MAX_ATTACK_POINTS;
    }

    public int getHealth() {
        return health;
    }

    public int getMAX_HEALTH() {
        return MAX_HEALTH;
    }

    public int getMAX_MOVE_POINTS() {
        return MAX_MOVE_POINTS;
    }

    public int getMovePoints() {
        return movePoints;
    }

    public Creature getTarget() {
        return target;
    }

    public void setArmour(int armour) {
        this.armour = armour;
    }

    public ArrayList<Creature> getEnemyCreatures() {
        return enemyCreatures;
    }

    public int getRange() {
        return range;
    }

    public void setHealth(int health) {
        if (health<=this.MAX_HEALTH){
            this.health = health;
        }
    }

    public void setAttackPoints(int attackPoints) {
        if (attackPoints <= this.MAX_ATTACK_POINTS){
            this.attackPoints = attackPoints;
        }

    }

    public void setMovePoints(int movePoints) {
        if (movePoints <= this.MAX_MOVE_POINTS){
            this.movePoints = movePoints;
        }
    }
}

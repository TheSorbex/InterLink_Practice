package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class MainFrame extends JFrame {
    private String name;
    private JFrame frame = new JFrame("Game");
    private JTextField[][] textFields = new JTextField[12][12];
    private String[][] map = new String[12][12];
    private ArrayList<Creature> creatures = new ArrayList<>();
    private Object ActionEvent;

    public MainFrame(String name) {
        this.name = name;
    }

    public void init() throws InterruptedException {
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridBagLayout());
        createMap();
        creatures.add(new Human(3, 1, 1, 15, 1, 1, "Petya", this));
        creatures.add(new Ork(4, 0, 1, 10, 1, 1, "Alesha", this));
        creatures.add(new Elf(2, 0, 1, 10, 1, 1, "Julia", this));


        spawn(creatures.get(0));
        spawn(creatures.get(1));
        spawn(creatures.get(2));
        writeCreatures();
        writeMap();
        for (Creature creature : creatures){
            creature.sortingEnemiesAndFriends(creatures);
            creature.chooseTarget();
        }
        frame.setVisible(true);

        while (creatures.size()!=1){
            Thread.sleep(1000);
            nextStep();
        }
    }

    public void createMap() {
        for (int i = 0; i < textFields.length; i++) {
            for (int j = 0; j < textFields.length; j++) {
                map[i][j] = " ";
                textFields[i][j] = new JTextField(10);
                textFields[i][j].setText(map[i][j]);
                textFields[i][j].setEditable(false);
                textFields[i][j].addKeyListener(new MyKeyListener());
                textFields[i][j].setBackground(Color.white);
                textFields[i][j].setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                textFields[i][j].setHorizontalAlignment(JTextField.CENTER);
                frame.add(textFields[i][j], new GridBagConstraints(i, j, 1, 1, 1, 1,
                        GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 40, 0));
            }
        }
    }

    public void writeMap() {
        for (int i = 0; i < textFields.length; i++) {
            for (int j = 0; j < textFields.length; j++) {
                textFields[i][j].setText(map[i][j]);
            }
        }
    }

    public void writeCreatures() {
        for (int i = 0; i < creatures.size(); i++) {
            map[creatures.get(i).getX()][creatures.get(i).getY()] = creatures.get(i).getRaceName() + " " + creatures.get(i).getName();
        }
    }

    public void nextStep() {
        if (!creatures.isEmpty()){
            for (Creature creature : creatures) {
                creature.someAction();
                cleanUp();
                creature.cleanUp();
                if (!creature.getTarget().isAlive()){
                    creature.chooseTarget();
                }
            }
            writeCreatures();
            writeMap();
        }
    }

    public void cleanUp(){
        for (int i = 0; i < creatures.size(); i++){
            if (!creatures.get(i).isAlive()){
                setInfoToField(" ",creatures.get(i).getX(),creatures.get(i).getY());
                creatures.remove(i);
                writeMap();
            }
        }
    }

    public int getMapLength() {
        return this.textFields.length;
    }

    public String getInfoFromField(int x, int y) {
        return this.map[x][y];
    }

    public void setInfoToField(String str, int x, int y) {
        this.map[x][y] = str;
    }

    public boolean checkPlaceForEmpty(Creature creature) {
        boolean res = true;
        for (int i = 0; i < this.creatures.size(); i++) {
            if (creature.getX() == this.creatures.get(i).getX() && creature.getY() == this.creatures.get(i).getY() &&
                    !creature.equals(this.creatures.get(i))) {
                res = false;
            }
        }

        return res;
    }

    public void spawn(Creature creature) {
        while (!checkPlaceForEmpty(creature)) {
            creature.makeRandomCoordinates();
        }
    }

    public ArrayList<Creature> getCreatures() {
        return creatures;
    }

    class MyKeyListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (KeyEvent.VK_SPACE == e.getKeyChar()) {
                nextStep();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
}

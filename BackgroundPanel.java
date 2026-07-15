/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package ui;

import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {

    private Image bgImage;

    public BackgroundPanel(String imagePath) {
        bgImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
        setLayout(null); // absolute positioning
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
    }
}

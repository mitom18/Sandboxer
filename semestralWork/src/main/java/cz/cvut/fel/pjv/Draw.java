/*
 * The MIT License
 *
 * Copyright 2019 Michal-jr.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package cz.cvut.fel.pjv;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Class maintaining the GUI of the game.
 * 
 * @author Michal-jr
 * @version 1.0
 */
public class Draw {
    
    JFrame frame;
    Canvas canvas;
    
    BufferStrategy bufferStrategy;
    
    private final int WIDTH = 640;
    private final int HEIGHT = 480;

    /**
     * Create game window and inicialize the key handler.
     * @since 1.0
     */
    public Draw() {
        frame = new JFrame("Basic Game");
        JPanel panel = (JPanel) frame.getContentPane();
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        panel.setLayout(null);
        
        canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        
        frame.setResizable(false);
        frame.setVisible(true);
        
        panel.add(canvas);
        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        
        canvas.requestFocus();
        
        canvas.setBackground(Color.black);
        
        canvas.addKeyListener(new ButtonHandler());
    }
    
    void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);
        render(g);
        g.dispose();
        bufferStrategy.show();
    }
    
    /**
     * Draw the world and entities in it.
     * 
     * @param g a canvas 2D rendering context
     * @since 1.0
     */
    protected void render(Graphics2D g) {
        for (Block block : Instances.blocks) {
            g.setColor(block.getColor());
            g.fillRect(block.getX(), block.getY(), block.getWIDTH(), block.getHEIGHT());
        }
        g.setColor(Color.white);
        g.fillRect(Instances.player.getX(), Instances.player.getY(), Instances.player.getWIDTH(), Instances.player.getHEIGHT());
    }
    
}

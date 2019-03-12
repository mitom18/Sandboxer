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

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Class handling keyboard events.
 * 
 * @author Michal-jr
 * @version 1.0
 */
public class ButtonHandler extends KeyAdapter {
    
    @Override
    public void keyPressed(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_UP:
                Instances.player.setUp(true);
                break;
            case KeyEvent.VK_LEFT:
                Instances.player.setLeft(true);
                break;
            case KeyEvent.VK_RIGHT:
                Instances.player.setRight(true);
                break;
        }
    }
    
    @Override
    public void keyReleased(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_UP:
                Instances.player.setUp(false);
                break;
            case KeyEvent.VK_LEFT:
                Instances.player.setLeft(false);
                break;
            case KeyEvent.VK_RIGHT:
                Instances.player.setRight(false);
                break;
        }
    }
    
    @Override
    public void keyTyped(KeyEvent key) {}
    
}

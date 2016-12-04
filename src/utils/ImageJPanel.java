package utils;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ImageJPanel extends JPanel {
	
	BufferedImage bi = null;
	
	public void setImage(BufferedImage a) {
		bi = a;
		this.repaint();
		Container c = this.getTopLevelAncestor();
		((JFrame) c).setSize(bi.getWidth(), bi.getHeight());
	}
	
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
        if(bi != null)
           g.drawImage(bi, 0, 0, null);
    }

}

package sdf;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class Fleche implements Paintable{

    private Rectangle2D bounds;
    int x,y;
    int dim=9;
    int longeur;
    int largeur;
    int pos;
    private BufferedImage image[]=new BufferedImage[4];
    
    public Fleche(int x, int y,int longeur,int largeur,int pos) {
        this.x=x;
        this.y=y;
        this.longeur = longeur;
        this.largeur = largeur;
        this.pos = pos;
        move(x,y);
        try {
            setImage();
        } catch (IOException ex) {
            Logger.getLogger(Fleche.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void paint(JComponent parent, Graphics2D g) {
        g.drawImage(image[pos-1], x, y, parent);
    }

    @Override
    public boolean contains(Point p) {
        return bounds.contains(p);
    }

    @Override
    public void moveTo(Point2D p) {
        x = (int) p.getX();
        y = (int) p.getY();
        bounds = new Rectangle2D.Double(p.getX(), p.getY(),18,18);
    }

    @Override
    public Rectangle2D getBounds() {
        return bounds.getBounds2D();
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public void move(int xp, int yp) {
        this.x=xp;
        this.y=yp;
        switch(pos)
        {
            case 1:
                x+= -dim/2+largeur/2;
                y+= -dim;
                break;
            case 2:
                x+= largeur;
                y+= -dim/2+longeur/2;
                break;
            case 3:
                x+= -dim/2+largeur/2;
                y+= +longeur;
                break;
            case 4:
                x+= -dim;
                y+= -dim/2+longeur/2;
                break;
        }
    }

    private void setImage() throws IOException {   
        image = new BufferedImage[]{ImageIO.read(new File("fleche1.png")),ImageIO.read(new File("fleche2.png")),ImageIO.read(new File("fleche3.png")),ImageIO.read(new File("fleche4.png"))};
    }
    
}

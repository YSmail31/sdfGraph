package sdf;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JComponent;
import org.jdom2.DataConversionException;
import org.jdom2.Element;

public class Actor implements Paintable{
    
    String nom;
    HashMap<String,Port> ports;
    ArrayList<Actor> srcActors;
    ArrayList<Actor> dstActors;
    ArrayList<Fleche> fleches;
    
    int largeur = 20;
    final int longueur = 20;
    int x=20,y=20;
    public Actor(Element actor) throws DataConversionException
    {
        nom = actor.getAttributeValue("name");
        largeur += nom.length()*6;
        ports = new HashMap<>();
        fleches = new ArrayList<>();
        for(Element port:actor.getChildren("port"))
            ports.put(port.getAttributeValue("name"), new Port(port));
        srcActors = new ArrayList<>();
        dstActors = new ArrayList<>();
    }

    public String getNom() {
        return nom;
    }

    public HashMap<String, Port> getPorts() {
        return ports;
    }

    public ArrayList<Actor> getSrcActors() {
        return srcActors;
    }

    public ArrayList<Actor> getDstActors() {
        return dstActors;
    }
    public void addRelatedActor(Actor actor)
    {
        actor.srcActors.add(this);
        this.dstActors.add(actor);
        actor.addFleche(new Fleche(actor.x, actor.y, actor.longueur, actor.largeur, actor.calculPos(this)));
    }
    public void addFleche(Fleche fleche)
    {
        fleches.add(fleche);
    }
    @Override
    public void paint(JComponent parent, Graphics2D g2d) {
        g2d.setColor(Color.PINK);
        g2d.fill3DRect(x, y, largeur, longueur, true);
        g2d.setColor(Color.BLACK);
        g2d.draw3DRect(x, y, largeur, longueur, true);
        g2d.drawString(nom, x+10, y+longueur/2+6);
        for(Fleche fleche:fleches)
            fleche.paint(parent, g2d);
    }

    @Override
    public boolean contains(Point p) {
        return getBounds().contains(p);
    }

    @Override
    public void moveTo(Point2D p) {
        x =(int) p.getX();
        y =(int) p.getY();
        for(int i=0;i<srcActors.size();i++)
        {
            srcActors.get(i).iMoved(this);
            int pos=calculPos(srcActors.get(i));
            fleches.get(i).setPos(pos);
            fleches.get(i).move(x,y);
        }
        for(Actor dst:dstActors)
        {
            dst.setPos(this);
        }
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D.Double(x, y, largeur, longueur);
    }

    int calculPos(Actor c2) {
        if(c2.y<y)
            return 1;
        else if(c2.y>y+largeur)
            return 3;
        else if(c2.x<x)
            return 4;
        else
            return 2;
    }

    void setX(int x) {
        this.x = x;
    }

    void setY(int y) {
        this.y = y;
    }

    private void iMoved(Actor cible) {
        int pos=calculPos(cible);
        int index=srcActors.indexOf(cible);
        if(index!=-1)
        {
            fleches.get(index).setPos(pos);
            fleches.get(index).move(x, y);
        }
    }

    private void setPos(Actor src) {
            int pos=calculPos(src);
            int index=srcActors.indexOf(src);
            fleches.get(index).setPos(pos);
            fleches.get(index).move(x,y);
    }
    
}

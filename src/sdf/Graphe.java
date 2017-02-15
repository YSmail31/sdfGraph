package sdf;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JPanel;
import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class Graphe extends JPanel {

    private Point2D offset;
    private static final int H = 800;
    private static final int W = 1150;

    private Paintable selectedClasse;

    private List<Relation> relationships;
    private HashMap<String,Actor> actors;
    private Element racine;

    public Graphe(String nomF) {
        relationships = new ArrayList<>();
        ouvrir(nomF);
        relation();
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                for (Actor p : getPaintable().values()) {
                    if (p.contains(e.getPoint())) {
                        selectedClasse = p;
                        offset = new Point2D.Double(e.getX() - p.getBounds().getX(), e.getY() - p.getBounds().getY());
                        break;
                    }
                }
            }
            public void mouseReleased(MouseEvent e) {
                selectedClasse = null;
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedClasse != null) {
                    Point2D p = new Point2D.Double(e.getX() - offset.getX(), e.getY() - offset.getY());
                    selectedClasse.moveTo(p);
                }
                repaint();
            }
        });
        
    }
    
    protected HashMap<String,Actor> getPaintable() {
        return actors;
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        dessinerTraitRelation(g2);
        dessiner(g2);
        g2.dispose();
    }
    
    private int[] getPosition(Actor c1, Actor c2) {
        int res[]=new int[4];
        int pos[];
        pos=calculePos(c1,c2);
        res[0]=pos[0];
        res[1]=pos[1];
        pos=calculePos(c2,c1);
        res[2]=pos[0];
        res[3]=pos[1];
        return res;
    }
    private int[] calculePos(Actor c1, Actor c2) {
        int res[]=new int[2];
        int pos=c1.calculPos(c2);
        int dim=0;
        if(c1.srcActors.contains(c2))
            dim=9;
        switch(pos)
            {
                case 1:
                    res[0]=c1.x+c1.largeur/2;
                    res[1]=c1.y-dim;
                    break;
                case 2:
                    res[0]=dim+c1.x+c1.largeur;
                    res[1]=c1.y+c1.longueur/2;
                    break;
                case 3:
                    res[0]=c1.x+c1.largeur/2;
                    res[1]=c1.y+c1.longueur+dim;
                    break;
                case 4:
                    res[0]=c1.x-dim;
                    res[1]=c1.y+c1.longueur/2;
                    break;
            }
        return res;
    }
    private void dessinerTraitRelation(Graphics2D g2) {
        for (Relation relationship : relationships) {
            Actor c1=(Actor) relationship.getParent();
            Actor c2=(Actor) relationship.getChild();
            int pos[] = getPosition(c1,c2);
            Point2D p1 = new Point2D.Double(pos[0], pos[1]);
            Point2D p2 = new Point2D.Double(pos[2], pos[3]);
            if(!c1.equals(c2))
                g2.draw(new Line2D.Double(p1, p2));
            else
                g2.drawRect(c1.x+c1.largeur*3/4, c1.y-c1.longueur/2,c1.largeur/2+12 , c1.longueur);
        }
    }
    private void dessiner(Graphics2D g2) {
        for (Actor actor : actors.values()) {
            actor.paint(this, g2);
        }
    }
    private void ouvrir(String nomF) {
        SAXBuilder sxb = new SAXBuilder();
        actors = new HashMap<>();
        try
        {
            Document document = sxb.build(new File(nomF));
            racine = document.getRootElement();
            Element a = racine.getChild("applicationGraph");
            Element b = a.getChild("sdf");
            for(Element actor:b.getChildren("actor"))
            {
                Actor act = new Actor(actor);
                if(actors.size()>0)
                {
                    int size = actors.size();
                    act.setX(act.x+((act.largeur+120)*(size%2)));
                    act.setY(act.y+((act.longueur+40)*(size/2)));
                }
                actors.put(act.getNom(),act);
            }
        }
        catch(IOException e){
            System.out.println("erreur fichier "+nomF+" "+e.getMessage());
        } 
        catch (DataConversionException ex) {
            System.out.println("erreur conversion"+ex.getMessage());
        } catch (JDOMException ex) {
            System.out.println("erreur jdom "+ex.getMessage());
        }
    }
    private void relation() {
        Element a = racine.getChild("applicationGraph");
        Element b = a.getChild("sdf");
        for(Element channel:b.getChildren("channel"))
        {
            Actor actSrc=actors.get(channel.getAttributeValue("srcActor"));
            Actor actDst=actors.get(channel.getAttributeValue("dstActor"));
            //actDst.addRelatedActor(actSrc);
            actSrc.addRelatedActor(actDst);
            relationships.add(new Relation(actSrc, actDst));
        }
    }
}
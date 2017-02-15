/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdf;

public class Relation {
    private Paintable parent;
    private Paintable child;
    private String rateSrc,rateDst;
    
    public Relation(Paintable parent, Paintable child) {
        this.parent = parent;
        this.child = child;
    }

    public Relation(Paintable parent, Paintable child, String rateSrc, String rateDst) {
        this.parent = parent;
        this.child = child;
        this.rateSrc = rateSrc;
        this.rateDst = rateDst;
    }

    public Paintable getChild() {
        return child;
    }

    public Paintable getParent() {
        return parent;
    }

    public String getRateSrc() {
        return rateSrc;
    }

    public String getRateDst() {
        return rateDst;
    }
    
    
}

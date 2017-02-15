/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdf;

public class Relation {
    private Paintable parent;
    private Paintable child;

    public Relation(Paintable parent, Paintable child) {
        this.parent = parent;
        this.child = child;
    }

    public Paintable getChild() {
        return child;
    }

    public Paintable getParent() {
        return parent;
    }

}

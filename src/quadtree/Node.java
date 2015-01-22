/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quadtree;

import static java.lang.Math.pow;

/**
 *
 * @author shivanshu
 */
public class Node {

    double x = -1;
    double y = -1;
    Rect boundingRect;
    Node quad1 = null;
    Node quad2 = null;
    Node quad3 = null;
    Node quad4 = null;

    Node(Rect boundingRect) {
        this.boundingRect = boundingRect;
    }
    
    public void setPoint(double x, double y){
        this.x = x;
        this.y = y;
    }
    
    public boolean isLeaf() {
        if(this.quad1 == null && this.quad2 == null
                && this.quad3 == null && this.quad4 == null) {
            return(true);
        }
        return(false);
    }
    
    public boolean isEmpty(){
        if(this.x < 0 && this.y < 0) {
            return(true);
        }
        return(false);
    }
    
    public void createQuads() {
        double midX = (this.boundingRect.maxX + this.boundingRect.minX)/2;
        double midY = (this.boundingRect.maxY + this.boundingRect.minY)/2;
        this.quad1 = new Node(new Rect(this.boundingRect.maxX,
                    this.boundingRect.maxY,
                    midX,
                    midY));
        this.quad2 = new Node(new Rect(midX,
                    this.boundingRect.maxY,
                    this.boundingRect.minX,
                    midY));
        this.quad3 = new Node(new Rect(midX,
                    midY,
                    this.boundingRect.minX,
                    this.boundingRect.minY));
        this.quad4 = new Node(new Rect(this.boundingRect.maxX,
                    midY,
                    midX,
                    this.boundingRect.minY));
        if(this.quad1.boundingRect.inside(this.x, this.y))
            this.quad1.setPoint(this.x, this.y);
        if(this.quad2.boundingRect.inside(this.x, this.y))
            this.quad2.setPoint(this.x, this.y);
        if(this.quad3.boundingRect.inside(this.x, this.y))
            this.quad3.setPoint(this.x, this.y);
        if(this.quad4.boundingRect.inside(this.x, this.y))
            this.quad4.setPoint(this.x, this.y);
        this.x = -1;
        this.y = -1;
    }
    
    public double distance(double x, double y){
        double difX = x - this.x;
        double difY = y - this.y;
        return (pow(difX,2) + pow(difY,2));
    }

    public boolean intersect(Rect rectangle){
        Rect boundingRect = this.boundingRect;
        if(boundingRect.maxX < rectangle.minX ||
                rectangle.maxX < boundingRect.minX)
            return(false);
        else if(boundingRect.maxY < rectangle.minY ||
                rectangle.maxY < boundingRect.minY)
            return(false);
        return(true);
    }
    

    
}

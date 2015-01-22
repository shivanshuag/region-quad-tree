/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quadtree;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

/**
 *
 * @author shivanshu
 */
public class Rect {
    
    double maxX;
    double maxY;
    double minX;
    double minY;
    Rect(double maxX, double maxY, double minX, double minY) {
        this.maxX = maxX;
        this.maxY = maxY;
        this.minX = minX;
        this.minY = minY;
    }
    
    public boolean inside(double x, double y){
        if(x <= this.maxX && x >= this.minX && y <= this.maxY && y >= this.minY){
          return(true);
        }
        return(false);
    }
    
    public double minDis(double x, double y){
        if(this.inside(x,y))
            return(0);
        else{
            double centerX = (this.minX+this.maxX)/2;
            double centerY = (this.minY+this.maxY)/2;
            double width = this.maxX - this.minX;
            double height = this.maxY - this.minY;
            double dx = abs(x - centerX) - width/2;
            double dy = abs(y - centerY) - height/2;
            return(pow(dx,2) + pow(dy,2));
        }
    }
    
    public double maxDis(double x, double y) {
        if(this.inside(x,y))
            return(0);
        else{
            double centerX = (this.minX+this.maxX)/2;
            double centerY = (this.minY+this.maxY)/2;
            double width = this.maxX - this.minX;
            double height = this.maxY - this.minY;
            double dx = abs(x - centerX) + width/2;
            double dy = abs(y - centerY) + height/2;
            return(pow(dx,2) + pow(dy,2));
        }

    }

}

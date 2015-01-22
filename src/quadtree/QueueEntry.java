/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quadtree;

/**
 *
 * @author shivanshu
 */
public class QueueEntry {
    Node node;
    double comp;
    
    QueueEntry(Node node, double comp) {
        this.node = node;
        this.comp = comp;        
    }
}

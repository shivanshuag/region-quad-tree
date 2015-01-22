/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quadtree;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.Math.pow;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 *
 * @author shivanshu
 */
public class QuadTree {
    
    Node root;
    
    Comparator<QueueEntry> NodeComparator = new Comparator<QueueEntry>() {
        @Override
        public int compare(QueueEntry e1, QueueEntry e2){
            return e1.comp <= e2.comp ? 1 : -1;
        }
    };
    QuadTree(Rect boundingRect){
        this.root = new Node(boundingRect);
    }

    void insertNode(double x, double y){
        Node node = this.root;
        while(true){
            if(node.isLeaf() && node.isEmpty()){
                node.setPoint(x,y);
                break;
            }
            else if(node.isLeaf()){
                if(x == node.x && y == node.y){
                    break;
                }
                node.createQuads();
            }
            else{
                if(node.quad1.boundingRect.inside(x,y))
                    node = node.quad1;
                else if(node.quad2.boundingRect.inside(x,y))
                    node = node.quad2;
                else if(node.quad3.boundingRect.inside(x,y))
                    node = node.quad3;
                else if(node.quad4.boundingRect.inside(x,y))
                    node = node.quad4;
            }
        }
    }
    
    boolean pointQuery(double x, double y){
        Node node = this.root;
        while(true){
            if(node.isLeaf()){
                if(node.isEmpty())
                    return(false);
                else if(node.x == x  && node.y == y)
                    return(true);
                else
                    return(false);
            }
            else{
                if(node.quad1.boundingRect.inside(x,y))
                    node = node.quad1;
                else if(node.quad2.boundingRect.inside(x,y))
                    node = node.quad2;
                else if(node.quad3.boundingRect.inside(x,y))
                    node = node.quad3;
                else if(node.quad4.boundingRect.inside(x,y))
                    node = node.quad4;
            }
        }
    }
    
    List windowQuery(Rect boundingRect){
        List<Node> points = new ArrayList();
        Queue<Node> queue = new LinkedList();
        queue.add(this.root);
        while(!queue.isEmpty()){
            Node node = queue.remove();
            if(node.isLeaf()){
                if(!node.isEmpty() && boundingRect.inside(node.x, node.y))
                    points.add(node);
            }
            else{
                if(node.quad1.intersect(boundingRect))
                    queue.add(node.quad1);
                if(node.quad2.intersect(boundingRect))
                    queue.add(node.quad2);
                if(node.quad3.intersect(boundingRect))
                    queue.add(node.quad3);
                if(node.quad4.intersect(boundingRect))
                    queue.add(node.quad4);
            }
        }
        return(points);
    }

    List rangeQuery(double x, double y, double distance){
        distance = pow(distance,2);
        List<Node> points = new ArrayList();
        Queue<Node> queue = new LinkedList();
        queue.add(this.root);
        while(!queue.isEmpty()){
            Node node = queue.remove();
            if(node.isLeaf()){
                if(!node.isEmpty() && node.distance(x, y) <= distance)
                    points.add(node);
            }
            else{
                if(node.quad1.boundingRect.minDis(x, y) <= distance)
                    queue.add(node.quad1);
                if(node.quad2.boundingRect.minDis(x, y) <= distance)
                    queue.add(node.quad2);
                if(node.quad3.boundingRect.minDis(x, y) <= distance)
                    queue.add(node.quad3);
                if(node.quad4.boundingRect.minDis(x, y) <= distance)
                    queue.add(node.quad4);
            }
        }
        return(points);
    }

    PriorityQueue knnQuery(double x, double y, int k){
        PriorityQueue<QueueEntry> h = new PriorityQueue(k, this.NodeComparator);
        double dk = 1000000;
        int i = 0;
        for(i=0;i<k;i++)
            h.add(new QueueEntry(null, -1*dk));
        PriorityQueue<QueueEntry> queue = new PriorityQueue(1, this.NodeComparator);
        queue.add(new QueueEntry(this.root, 0));
        while(!queue.isEmpty()){
            Node node = queue.poll().node;
            if(node.isLeaf()){
                if(!node.isEmpty()){
                    double dis = node.distance(x,y);
                    if(dis < dk){
                        h.poll();
                        h.offer(new QueueEntry(node, -1*dis));
                        dk = -1*h.peek().comp;
                    }
                }
            }
            else{
                double dis = node.quad1.boundingRect.minDis(x,y);
                if(node.quad1.boundingRect.inside(x,y) || dis < dk)
                    queue.offer(new QueueEntry(node.quad1, dis));
                dis = node.quad2.boundingRect.minDis(x,y);
                if(node.quad2.boundingRect.inside(x,y) || dis < dk)
                    queue.offer(new QueueEntry(node.quad2, dis));
                dis = node.quad3.boundingRect.minDis(x,y);
                if(node.quad3.boundingRect.inside(x,y) || dis < dk)
                    queue.offer(new QueueEntry(node.quad3, dis));
                dis = node.quad4.boundingRect.minDis(x,y);
                if(node.quad4.boundingRect.inside(x,y) || dis < dk)
                    queue.offer(new QueueEntry(node.quad4, dis));
            }
        }
        return(h);
    }

        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        QuadTree tree = new QuadTree(new Rect(1,1,0,0));
        BufferedReader insertFile = new BufferedReader(new FileReader("/home/shivanshu/cs618ass2/assgn2data.txt"));
        String line;
        while ((line = insertFile.readLine()) != null) {
            // process the line.
            String[] xny = line.split("\\t");
            //System.out.println(xny[0]+" "+xny[1]);
            tree.insertNode(Double.parseDouble(xny[0]), Double.parseDouble(xny[1]));
        }
        int count = 0;
        BufferedReader queryFile = new BufferedReader(new FileReader("/home/shivanshu/cs618ass2/assgn2querysample1.txt"));
        while ((line = queryFile.readLine()) != null) {
            count++;
            String[] query = line.split("   ");
            if(query[0].equals("0")){
                tree.insertNode(Double.parseDouble(query[1].trim()), Double.parseDouble(query[2].trim()));
            }
            else if(query[0].equals("1")){
                tree.pointQuery(Double.parseDouble(query[1].trim()), Double.parseDouble(query[2].trim()));
            }
            else if(query[0].equals("2")){
                List points = tree.rangeQuery(Double.parseDouble(query[1].trim()),
                                              Double.parseDouble(query[2].trim()),
                                              Double.parseDouble(query[3].trim()));
                System.out.println(points.size());
            }
            else if(query[0].equals("3")){
                PriorityQueue points = tree.knnQuery(Double.parseDouble(query[1].trim()),
                              Double.parseDouble(query[2].trim()),
                              Integer.parseInt(query[3].trim()) );
            }
            else if(query[0].equals("4")) {
                Rect box = new Rect(Double.parseDouble(query[1].trim()),
                                    Double.parseDouble(query[2].trim()),
                                    Double.parseDouble(query[3].trim()),
                                    Double.parseDouble(query[4].trim())); 
                List points = tree.windowQuery(box);
            }
            System.out.println(count);
        }
        boolean res = tree.pointQuery(0.521625,0.687809);
        if(res)
            System.out.println("found");
        
    }
    
}

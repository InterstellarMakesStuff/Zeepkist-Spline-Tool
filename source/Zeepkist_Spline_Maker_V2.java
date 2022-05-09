import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Zeepkist_Spline_Maker_V2 extends PApplet {

PrintWriter out;

PVector mouseStart;
PVector mouseEnd;
int Selected = -1;
int SelectedHandle = -1;

float res = 40;

Boolean hideConnectingLines = false;
Boolean SolidLineTrack = true;
Boolean HideNodes = false;
Boolean HideHandles = false;
Boolean ShowAltitude = true;
Boolean AltorRot = true;
Boolean SimplerTrackBuilding = false;

//0,5,24,30,24,30
int roadmaterial = 0;
int terrainmaterial = 5;
int leftamat = 24;
int leftbmat = 30;

int rightamat = 24;
int rightbmat = 30;

ArrayList<Node> nodes = new ArrayList();
ArrayList<Arc> arcs = new ArrayList();
ArrayList<PVector> points = new ArrayList();
ArrayList<Float> rots = new ArrayList();

// todo:
/*
rotation and banking - check
fix scaling overlap - sorta check
color and road settings
*/

public void setup() {
  
  render();
}

public void draw() {
}

public void render() {
  background(0);
  
  text("Z- Hide Connecting Lines, X- Solid/Dotted Line, C- Hide Nodes, v- Hide Handles, B- Display Alt/Rot Info, N- Alt/Rot Mode Toggle R- Build Level", 0, 15);
  text("If the builder starts lagging, you can temporarily reduce the resolution by hitting M, remember to reenable this before pressing R", 0, 30);
  
  //println(nodes.size());
  
  for(Node n : nodes) {
    stroke(255);
 
    noStroke();
    fill(255);
    if (!HideNodes) {
      circle(n.pos.x, n.pos.y, 10);
    }
    if (ShowAltitude) {
      if (AltorRot) {
        text(n.pos.z, n.pos.x, n.pos.y-5);
      } else {
        text(n.rot, n.pos.x, n.pos.y-5);
      }
    } 
    
    if (n.hasHandles) {
      noFill();
      stroke(255);
      if (!HideHandles) {
        line(n.a.x, n.a.y, n.b.x, n.b.y);
        if (ShowAltitude) {
          if (AltorRot) {
            text(n.a.z, n.a.x, n.a.y-5);
            text(n.b.z, n.b.x, n.b.y-5);
          }    
        }
        circle(n.a.x, n.a.y, 10);
        circle(n.b.x, n.b.y, 10);
      }     
    }
  }
  
  arcs.clear();
  for (int i = 1; i < nodes.size(); i++) {
    stroke(127);
    
    Arc a = new Arc(null, null, null, null, 0, 0);
    if (!nodes.get(i-1).hasHandles && !nodes.get(i).hasHandles) { // single to single
    if (!hideConnectingLines) line(nodes.get(i-1).pos.x, nodes.get(i-1).pos.y, nodes.get(i).pos.x, nodes.get(i).pos.y);
      a = new Arc(nodes.get(i-1).pos, nodes.get(i).pos, null, null, nodes.get(i-1).rot, nodes.get(i).rot); 
      //println("opt 1");
    } else if (!nodes.get(i-1).hasHandles && nodes.get(i).hasHandles) { // single to handles
      if (!hideConnectingLines) line(nodes.get(i-1).pos.x, nodes.get(i-1).pos.y, nodes.get(i).pos.x, nodes.get(i).pos.y);
      a = new Arc(nodes.get(i-1).pos, nodes.get(i).b, nodes.get(i).pos, null, nodes.get(i-1).rot, nodes.get(i).rot);
      //println("opt 2");
    } else if (nodes.get(i-1).hasHandles && !nodes.get(i).hasHandles) { // handles to single
      if (!hideConnectingLines) line(nodes.get(i-1).pos.x, nodes.get(i-1).pos.y, nodes.get(i).pos.x, nodes.get(i).pos.y);
      a = new Arc(nodes.get(i-1).pos, nodes.get(i-1).a, nodes.get(i).pos, null, nodes.get(i-1).rot, nodes.get(i).rot);
      //println("opt 3");
    } else if (nodes.get(i-1).hasHandles && nodes.get(i).hasHandles) { // handles to handles
      if (!hideConnectingLines) line(nodes.get(i-1).a.x, nodes.get(i-1).a.y, nodes.get(i).b.x, nodes.get(i).b.y);
      a = new Arc(nodes.get(i-1).pos, nodes.get(i-1).a, nodes.get(i).b, nodes.get(i).pos, nodes.get(i-1).rot, nodes.get(i).rot);
      //println("opt 4");
    }
    arcs.add(a);
    //println(arcs.size());
  }
  
  //println(nodes.size());
  //println(arcs.size());
  
  points.clear();
  rots.clear();
  
  if (SimplerTrackBuilding) res = 10; else res = 50;
  
  for (Arc n : arcs) {
    //println(n.abcd);
    switch (n.pointcount) {
      case 2:
        for (float i = 0; i<1; i+= 1/res) {
          PVector a = PVector.lerp(n.abcd[0], n.abcd[1], i);
          
          points.add(a);
          rots.add(lerp(n.Rota, n.Rotb, i));
        }
        break;
        
      case 3:
        for (float i = 0; i<1; i+= 1/res) {
          PVector a = PVector.lerp(n.abcd[0], n.abcd[1], i);
          PVector b = PVector.lerp(n.abcd[1], n.abcd[2], i);
          
          PVector c = PVector.lerp(a, b, i);
          points.add(c);
          rots.add(lerp(n.Rota, n.Rotb, i));
          println(lerp(n.Rota, n.Rotb, i));
        }
        break;
        
      case 4:
        for (float i = 0; i<1; i+= 1/res) {
          PVector a = PVector.lerp(n.abcd[0], n.abcd[1], i);
          PVector b = PVector.lerp(n.abcd[1], n.abcd[2], i);
          PVector c = PVector.lerp(n.abcd[2], n.abcd[3], i);
          
          PVector d = PVector.lerp(a, b, i);
          PVector e = PVector.lerp(b, c, i);
          
          PVector f = PVector.lerp(d, e, i);
          points.add(f);
          rots.add(lerp(n.Rota, n.Rotb, i));
          println(lerp(n.Rota, n.Rotb, i));
        }
        break;
    }
  }

  if (SolidLineTrack) {
    for (int i = 1; i<points.size(); i++) {
      line(points.get(i-1).x, points.get(i-1).y, points.get(i).x, points.get(i).y);
    }
  } else {
    for (PVector p : points) {
      point(p.x, p.y);
    }
  }
  //println(points.size());
}

public void mousePressed() {
  
  
  
  for (int i = 0; i < nodes.size(); i++) {
    if (nodes.get(i).pos.dist(new PVector(mouseX, mouseY, nodes.get(i).pos.z)) < 5) {
      Selected = i;
      //println(i);
    }
    
    if (nodes.get(i).hasHandles) {
      if (nodes.get(i).a.dist(new PVector(mouseX, mouseY, nodes.get(i).a.z)) < 5) {
        Selected = i;
        SelectedHandle = 0;
      }
      
      if (nodes.get(i).b.dist(new PVector(mouseX, mouseY, nodes.get(i).b.z)) < 5) {
        Selected = i;
        SelectedHandle = 1;
      }
    }
    
    println(Selected);
    println(SelectedHandle);
  }
  
  mouseStart = new PVector(mouseX, mouseY);
  //println(nodes.size());
  render();
}

public void mouseDragged() {
  
  if (Selected != -1) {
    PVector mouse;
    //println(Selected);
    switch (SelectedHandle) {
      case -1:
        mouse = new PVector(mouseX, mouseY, nodes.get(Selected).pos.z);
        nodes.get(Selected).MoveNode(mouse);
        break;
      case 0:
        mouse = new PVector(mouseX, mouseY, nodes.get(Selected).a.z);
        nodes.get(Selected).MoveHandle(mouse, 0);
        break;
      case 1:
        mouse = new PVector(mouseX, mouseY, nodes.get(Selected).b.z);
        nodes.get(Selected).MoveHandle(mouse, 1);
        break;
    }
  }
  render();
}

public void mouseWheel(MouseEvent event) {
  float e = -event.getCount();
  //println(e);
  if (Selected != -1) {
    //println(Selected);
    
    switch (SelectedHandle) {
      case -1:
      if (AltorRot) {
        nodes.get(Selected).NodeAddAlt(e);
        //println("skrrt");
      } else {
        nodes.get(Selected).RotateNode(e);
        println("rotat e");
      }   
        break;
      case 0:
        if (AltorRot) nodes.get(Selected).HandleAddAlt(e, 0);
        //println("skrrt");
        break;
      case 1:
        if (AltorRot) nodes.get(Selected).HandleAddAlt(e, 1);
        //println("skrrt");
        break;
    }
  }
  render();
}

public void mouseReleased() {
  
  mouseEnd = new PVector(mouseX, mouseY);
  
  if (Selected == -1) {
    if (mouseStart.dist(mouseEnd) == 0) {
      Node node = new Node(new PVector(mouseX, mouseY), null, null);
      nodes.add(node);
    } else {
      Node node = new Node(mouseStart, mouseEnd, PVector.add(mouseStart,PVector.sub(mouseStart,mouseEnd)));
      nodes.add(node);
      //println("made node");
    }
  }
  Selected = -1;
  SelectedHandle = -1;
  render();
}

public void keyPressed() {
  switch (key) {
    case 'z':
      if (hideConnectingLines) hideConnectingLines = false; else hideConnectingLines = true;
      render();
      break;
    case 'x':
      if (SolidLineTrack) SolidLineTrack = false; else SolidLineTrack = true;
      render();
      break;
    case 'c':
      if (HideNodes) HideNodes = false; else HideNodes = true;
      render();
      break;
    case 'v':
      if (HideHandles) HideHandles = false; else HideHandles = true;
      render();
      break;
    case 'b':
      if (ShowAltitude) ShowAltitude = false; else ShowAltitude = true;
      render();
      break;
    case 'n':
      if (AltorRot) AltorRot = false; else AltorRot = true;
      render();
      break;
    case 'm':
      if (SimplerTrackBuilding) SimplerTrackBuilding = false; else SimplerTrackBuilding = true;
      render();
      break;
    case 'r':
      makelevel();
      break;
  }
}

public void makelevel() {
  out = createWriter("track.txt");
  for (int i=1; i<points.size(); i++) {
    PVector a = points.get(i-1);
    PVector b = points.get(i);
    PVector pos = PVector.add(a, b).div(2);
    float xRot = degrees(new PVector(a.dist(new PVector(b.x, b.y, a.z)), (b.z-a.z)).heading());
    float zRot = degrees(PVector.sub(b, a).heading());
    println(rots.get(i));
    if (a.dist(b) > 0.1f) out.println("0," + pos.x + "," + (pos.z) + "," + -pos.y + "," + (xRot) + "," + (zRot-90) + "," + rots.get(i) + ",1,1," + ((a.dist(b)/16)) + "," + roadmaterial + "," + terrainmaterial + "," + leftamat + "," + leftbmat + "," + rightamat + "," + rightbmat + ",0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0");
  }
  
  out.flush();
  out.close();
}

class Node {
  PVector pos;
  PVector a;
  PVector b;
  float rot = 0;
  float invertrot = 180;
  Boolean hasHandles = false;
  Boolean Invertedish = false;
  Node(PVector pos, PVector a, PVector b) {
    this.pos = pos;
    this.a = a;
    this.b = b;
    if (a != null && b != null) {
      hasHandles = true;
    }
  }
  
  public void MoveNode(PVector newPos) {
    //println("moving");
    if (hasHandles) {
      a = PVector.add(newPos, PVector.sub(a, pos));
      b = PVector.add(newPos, PVector.sub(b, pos));
    }
    pos = newPos;
  }
  
  public void MoveHandle(PVector newPos, int handle) {
    if (handle == 0) {
      a = newPos;
      b = PVector.add(pos, PVector.sub(pos,a));
    } else {
      b = newPos;
      a = PVector.add(pos, PVector.sub(pos,b));
    }
  }
  
  public void NodeAddAlt(float alt) {
    pos.add(new PVector(0,0,alt));
    if (hasHandles) {
      a.add(new PVector(0,0,alt));
      b.add(new PVector(0,0,alt));
    }
  }
  
  public void HandleAddAlt(float alt, int handle) {
    if (handle == 0) {
      a.add(new PVector(0,0,alt));
      b.sub(new PVector(0,0,alt));
    } else {
      b.add(new PVector(0,0,alt));
      a.sub(new PVector(0,0,alt));
    }
  }
  
  public void RotateNode(float rotat) {
    rot += rotat;
    invertrot = rot+180;
  }
  
  public void Invert(Boolean inverted) {
    Invertedish = inverted;
  }
}

class Arc {
  PVector[] abcd = new PVector[4];
  float Rota;
  float Rotb;
  
  float Inverteda;
  float Invertedb;
  
  int pointcount = 0;
  Arc(PVector a, PVector b, PVector c, PVector d, float rota, float rotb) {
    abcd[0] = a;
    abcd[1] = b;
    abcd[2] = c;
    abcd[3] = d;
    for (int i = 0; i<4; i++) {
      if (abcd[i] != null) pointcount++;
    }
    
    Rota = rota;
    Rotb = rotb;
    
  }
}
  public void settings() {  size(1000, 1000, P2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Zeepkist_Spline_Maker_V2" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}

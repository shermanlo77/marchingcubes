package shermanlo77.marchingcubes;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import peasy.PeasyCam;

public class MCApplet extends PApplet {

  private PeasyCam cam;

  //PShape for object
  public Material material;

  boolean isWireFrame = false;

  //SETUP
  @Override
  public void setup() {
    //set up materials
    material = new Material(this, Main.N_FILE, Main.FILE_NAME, true);
    this.setShape();
    this.instantiateCam();
  }

  @Override
  public void settings() {
    this.fullScreen(PConstants.P3D);
  }

  void instantiateCam() {
    //make a new camera and set distance
    float camDistance;
    if (this.material.imageWidth < this.material.nSlices) {
      camDistance = this.material.nSlices;
    } else {
      camDistance = this.material.imageWidth;
    }
    camDistance *= 2*Lookup.UNIT_LENGTH;
    this.cam = new PeasyCam(this, camDistance);
  }

  void setShape() {
    if (this.isWireFrame) {
      this.material.setFill(this.color(0));
      this.material.setStroke(true);
      this.material.setStroke(this.color(0, 255, 0));
    } else {
      this.material.setFill(this.color(255));
      this.material.setStroke(false);
    }
  }

  //DRAW
  @Override
  public void draw() {
    this.background(0); //set background to black
    this.lighting(); //set up the lights
    this.shape(material.getShape()); //draw the chromosome
  }

  //lighting
  public void lighting() {
    this.ambientLight(50, 50, 50);
    PVector pos = new PVector();
    pos.set(this.cam.getPosition());
    pos.normalize();
    this.directionalLight(128, 128, 128, -pos.x, -pos.y, -pos.z);
  }

  @Override
  public void keyPressed() {
    if (this.key == ' ') {
      this.isWireFrame = !this.isWireFrame;
      this.setShape();
    }
  }

}

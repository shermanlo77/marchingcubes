package shermanlo77.marchingcubes;

public class Chromo3 extends MCApplet {
  /**Example code for showing internal and external features
   * The member variable material represent internal features and material2 for external.
   */

  private Material material2;

  //SETUP
  @Override
  public void setup(){
    //set up materials
    this.material = new Material(this, 48, "chromo3_internal/features", true, 1.81f);
    this.material2 = new Material(this, 24, "chromo3_external/Z", false, 1.81f);
    this.setShape();
    this.instantiateCam();
  }

  @Override
  public void draw(){
    super.draw();
    this.pushMatrix();
    this.scale(2); //scale the shell
    this.shape(this.material2.getShape()); //draw the chromosome
    this.popMatrix();
  }

  @Override
  void setShape() {
    this.material.setFill(true);
    this.material.setFill(this.color(255, 0, 0));
    this.material.setStroke(false);

    this.material2.setFill(false);
    this.material2.setStroke(true);
    this.material2.setStroke(this.color(0, 0, 255));
  }

  @Override
  public void keyPressed() {
    //do nothing
  }
}

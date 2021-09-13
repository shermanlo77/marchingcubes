package shermanlo77.marchingcubes;

public class Chromo0 extends MCApplet {

  //SETUP
  @Override
  public void setup(){

    //set up materials
    this.material = new Material(this, 26, "chromo0/X", true, 1.85f);
    this.setShape();
    this.instantiateCam();

  }

}

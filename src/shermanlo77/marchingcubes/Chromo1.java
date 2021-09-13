package shermanlo77.marchingcubes;

public class Chromo1 extends MCApplet {

  //SETUP
  @Override
  public void setup(){

    //set up materials
    this.material = new Material(this, 28, "chromo1/y", true, 1.85f);
    this.setShape();
    this.instantiateCam();

  }

}

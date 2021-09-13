package shermanlo77.marchingcubes;

public class Chromo2 extends MCApplet {

  //SETUP
  @Override
  public void setup(){

    //set up materials
    this.material = new Material(this, 59, "chromo2/Z", true, 1.81f);
    this.setShape();
    this.instantiateCam();

  }

}

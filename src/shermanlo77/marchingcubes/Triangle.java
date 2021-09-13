package shermanlo77.marchingcubes;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

/**An instance from this class can return a triangle PShape according to the 3 edgeIndexes
 * inputted in the constructor
 */
class Triangle {
  
  //MEMBER VARIABLES
  private PShape shape; //PShape representing the triangle
  //position vectors of the vertices of the triangle relative to cubeCenter
  private PVector a, b, c;
  private PVector cubeCenter; //the co-od of the center of the cube
  private PVector n; //normal of the triangle
  private PVector nBar; //the mean normal of the triangle
  private float area; //the area of the triangle

  //CONSTRUCTOR
  /**An instance from this class can return a triangle PShape according to the 3 edgeIndexes
   * inputted in the constructor
   * @param applet
   * @param index1 which edges the vertices of the triangle should be on
   * @param index2 which edges the vertices of the triangle should be on
   * @param index3 which edges the vertices of the triangle should be on
   * @param x coordinates of the centre of the cube
   * @param y coordinates of the centre of the cube
   * @param z coordinates of the centre of the cube
   */
  public Triangle(PApplet applet, int index1, int index2, int index3, float x, float y, float z) {
    
    //convert the edge indexes to position vectors
    this.a = Lookup.getEdgeCo(index1);
    this.b = Lookup.getEdgeCo(index2);
    this.c = Lookup.getEdgeCo(index3);

    //get the position vector of the center
    this.cubeCenter = new PVector(x, y, z);

    //work out the normal vector and the area
    PVector r1, r2;
    r1 = PVector.sub(this.b, this.a);
    r2 = PVector.sub(this.b, this.c);
    this.n = r1.cross(r2);
    this.area = this.n.mag()/2;
    this.n.normalize();
    this.nBar = this.n;
  }
  //METHODS
  
  public void createShape(PApplet applet) {
    //create a PShape
    this.shape = applet.createShape();
    //draw the triangle and return it
    this.shape.beginShape();
    this.shape.normal(this.nBar.x, this.nBar.z, this.nBar.y);
    this.shape.vertex(this.a.x, this.a.z, this.a.y);
    this.shape.vertex(this.b.x, this.b.z, this.b.y);
    this.shape.vertex(this.c.x, this.c.z, this.c.y);
    this.shape.vertex(this.a.x, this.a.z, this.a.y);
    this.shape.endShape(PConstants.CLOSE);
  }

  //getShape of the triangle
  public PShape getShape() {
    return this.shape;
  }

  //get the normal vector
  public PVector getNormal() {
    return this.n;
  }

  //update nBar, the averaged normal
  public void newNormal(PVector normal) {
    this.nBar = normal;
    this.nBar.normalize();
  }

  //GET AREA of triangle
  public float getArea() {
    return this.area;
  }

  //get array of vertices vectors of this triangle
  public PVector[] getVerticesVectors() {
    PVector [] verticesArray;
    verticesArray = new PVector[3];
    verticesArray[0] = PVector.add(this.cubeCenter,a);
    verticesArray[1] = PVector.add(this.cubeCenter,b);
    verticesArray[2] = PVector.add(this.cubeCenter,c);
    return verticesArray;
  }

  //IsShareVertex
  //does this triangle share a vertex with that triangle?
  public boolean isShareVertex(Triangle that) {
    boolean share = false; //assume false

    //get the 2 triangles vertices vectors
    PVector [] vertexArray1, vertexArray2;
    vertexArray1 = this.getVerticesVectors();
    vertexArray2 = that.getVerticesVectors();

    PVector r1, r2;

    //for every vertex
    for (int i=0; i<3; i++) {
      //compare this vertex
      r1 = vertexArray1[i];
      //for every that vertex
      for (int j=0; j<3; j++) {
        r2 = vertexArray2[j];
        //if the cood of both vertex is the same, then they share the same vertex
        if (isClose(r1, r2)) {
          share = true;
          //break the loop
          j = 3;
          i = 3;
        }
      }
    }
    return share;
  }
  
  private static boolean isClose(PVector r1, PVector r2) {
    float e = 1E-2f;
    return (PApplet.abs(r1.x - r2.x) < e
        && PApplet.abs(r1.y - r2.y) < e && PApplet.abs(r1.z - r2.z) < e);
  }

}

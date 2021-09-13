package shermanlo77.marchingcubes;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

/**A marching cube analyse 4 voxels and store/draw triangles according to the state of the voxels
 * The marching cube contains triangles to represent the surface of the chromosome according if the
 * voxel is white or not.
 */
class Cube {

  //MEMBER VARIABLES
  private PShape shape; //PShape representing the cube
  private PVector position; //the position of the cube
  private ArrayList<Triangle> triangleArray; //array list of Triangles

  //CONSTRUCTOR
  //Position of the cube (x,y,z)
  public Cube(float x, float y, float z) {
    this.position = new PVector(x, y, z);
    this.triangleArray = new ArrayList<Triangle>();
  }

  //METHODS

  //addTriangle to the marching cube
  public void addTriangle(PApplet applet, int index1, int index2, int index3) {
    //add triangle object to the triangle array list
    this.triangleArray.add(new Triangle(applet, index1, index2, index3,
        this.position.x, this.position.y, this.position.z));
  }

  //get the PShape for all the triangles in the cube
  public void createShape(PApplet applet) {
    //create a PShape for the marching cube
    Triangle triangle;
    this.shape = applet.createShape(PConstants.GROUP);
    //for every triangle in the array list
    for (int i=0; i<this.triangleArray.size(); i++) {
      //add the triangle to the cube PShape
      triangle = this.triangleArray.get(i);
      triangle.createShape(applet);
      this.shape.addChild(triangle.getShape());
    }
    //translate the cube according to its position and return it
    this.shape.translate(this.position.x, this.position.y, this.position.z);
  }

  public PShape getShape() {
    return this.shape;
  }

  //get the array of triangles
  public ArrayList<Triangle> getTriangleArray() {
    return this.triangleArray;
  }

  //recalculate the normals of each triangle in the marching cube
  public void recalculateNormal(int x, int y, int z, int imageWidth, int imageHeight, int nSlices,
      Cube[][][] cubeArray) {
    //recalculate normals for the triangles by taking the average of the normals of triangles
    //sharing its vertices

    //set the boundary on which neighbouring cubes to analyse
    int xLower, xUpper, yLower, yUpper, zLower, zUpper;
    xLower = -1;
    yLower = -1;
    zLower = -1;

    xUpper = 1;
    yUpper = 1;
    zUpper = 1;

    //if the marching cube is on the boundary, than it has less neighbours
    if (x == 0) {
      xLower = 0;
    } else if (x == imageWidth) {
      xUpper = 0;
    }

    if (y == 0) {
      yLower = 0;
    } else if (y == imageHeight) {
      yUpper = 0;
    }

    if (z == 0) {
      zLower = 0;
    } else if (z == nSlices) {
      zUpper = 0;
    }

    //for each triangle in the cube
    for (int i=0; i<this.triangleArray.size(); i++) {
      //prepare this triangle and declare statistical variables
      Triangle triangle = this.triangleArray.get(i);
      PVector nBar; //average normal vector
      nBar = new PVector(0, 0, 0);
      float weight = 0; //the sum of all weights used for averaging

      //compare this triangle to the triangles in neighbouring cubes
      //for every neighbouring cube
      for (int u=xLower; u<=xUpper; u++) {
        for (int v=yLower; v<=yUpper; v++) {
          for (int w=zLower; w<=zUpper; w++) {

            //get all the triangles in the neighbouring cube
            ArrayList<Triangle> thatTriangleArray = cubeArray[x+u][y+v][z+w].getTriangleArray();

            //for every triangle in the cube
            for (int j=0; j<thatTriangleArray.size(); j++) {
              Triangle that = thatTriangleArray.get(j);
              //if this triangle share a vertex with that triangle
              if (triangle.isShareVertex(that)) {
                //update the normal statistics with the reciprocal area as the weight
                nBar.add(PVector.mult(that.getNormal(), 1/that.getArea()));
                weight += 1/that.getArea();
              }
            }
          }
        }
      }
      //work out the mean normal and update that triangle's normal
      nBar.mult(1/weight);
      triangle.newNormal(nBar);
    }
  }

}

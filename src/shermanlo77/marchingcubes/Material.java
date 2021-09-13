package shermanlo77.marchingcubes;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PShape;
import processing.core.PVector;

/**Contains an array of marching cubes
 *
 * Reads a sequence of binary png images and construct a PShape from it. The algorithm used is the
 * marching cubes with normal averaging. Use getShape() to get a PShape of all the polygons.
 * Methods for setting the stroke and fill are provided.
 */
public class Material {

  //MEMBER VARIABLES
  private PShape shape;
  private Cube [][][] cubeArray; //array of  marching cubes
  int imageWidth, imageHeight, nSlices; //dimensions of the stack
  float zScale; //how much to scale the slices

  //CONSTRUCTOR
  /**Reads a sequence of binary png images and construct a PShape
   * @param applet
   * @param nSlices Number of images to use
   * @param filename path and suffix of the image sequence, two digits will be appended. For
   *     example, providing x/y will look for the images x/y00.png, x/y01.png, ...etc.
   * @param wantAverageNormal true to average the normals of each triangle to look smoother in
   *     lighting
   */
  public Material(PApplet applet, int nSlices, String filename, boolean wantAverageNormal) {
    this(applet, nSlices, filename, wantAverageNormal, 1.0f);
  }

  //CONSTRUCTOR
  /**Reads a sequence of binary png images and construct a PShape
   * @param applet
   * @param nSlices Number of images to use
   * @param filename path and suffix of the image sequence, two digits will be appended. For
   *     example, providing x/y will look for the images x/y00.png, x/y01.png, ...etc.
   * @param wantAverageNormal true to average the normals of each triangle to look smoother in
   *     lighting
   * @param zScale How much to scale the z-axis (stack dimension) by
   */
  public Material(PApplet applet, int nSlices, String filename, boolean wantAverageNormal,
      float zScale) {
    //LOAD IMAGES---------------------------
    //create an arraylist of PImages
    ArrayList <PImage> imageArray = new ArrayList<PImage>();

    //load all the images and add them in imageArray
    for (int i=0; i<10; i++) {
      imageArray.add(applet.loadImage(filename+"0"+i+".png")); //load image
    }
    for (int i=10; i<nSlices; i++) {
      imageArray.add(applet.loadImage(filename+i+".png")); //load image
    }

    //get imageWidth, imageHeight, nSlices
    this.imageWidth = imageArray.get(0).width;
    this.imageHeight = imageArray.get(0).height;
    this.nSlices = nSlices;
    this.zScale = zScale;

    //work out the center of the image
    PVector center = new PVector((float)this.imageWidth/2.0f, (float)this.imageHeight/2.0f,
        (float)nSlices/2.0f);

    //CREATE ARRAY OF MARCHINGS CUBES---------

    //create the pixelGrid and cubeArray of size imageWidth+2 x imageHeight+2 x nSlices+2
    //fill pixelGrid with all entries as false
    //fill cubeArray with cube with the position relative to the center
    boolean[][][] pixelGrid = new boolean[this.imageWidth+2][this.imageHeight+2][nSlices+2];
    this.cubeArray = new Cube [this.imageWidth+2][this.imageHeight+2][nSlices+2];
    //fill the pixelGrid with falses
    float cubeLength = 2*Lookup.UNIT_LENGTH;
    for (int i=0; i<this.imageWidth+2; i++) {
      for (int j=0; j<this.imageHeight+2; j++) {
        for (int k=0; k<nSlices+2; k++) {
          pixelGrid[i][j][k] = false;
          this.cubeArray[i][j][k] = new Cube(cubeLength*((float)i-center.x),
              cubeLength*((float)k-center.z), cubeLength*((float)j-center.y));
        }
      }
    }

    //for every image in imageArray
    for (int z=0; z<nSlices; z++) {
      PImage slice = imageArray.get(z);
      //load the pixels in the image
      slice.loadPixels();

      //declare x and y co-od
      int x = 0;
      int y = 0;
      //for every pixel in the image
      for (int i=0; i<slice.pixels.length; i++) {
        if (slice.pixels[i] == applet.color(255)) { //if that pixel is white
          pixelGrid[x+1][y+1][z+1] = true; //put true in the pixelGrid
        }
        //increase x and y co-od according
        x++;
        if (x == this.imageWidth) {
          x = 0;
          y++;
        }
      }
    }

    //SETUP PSHAPE---------------------------------------------------------------------------------
    //set the shape is a group of PShapes
    this.shape = applet.createShape(PConstants.GROUP);

    //for every slice
    for (int z=0; z<=nSlices; z++) {

      //for every pixel in the slice
      for (int y=0; y<=this.imageHeight; y++) {
        for (int x=0; x<=this.imageWidth; x++) {

          //declare a variable called vertices
          //this variable indicates which vertices has a true value in the pixelGrid as an 8 bit
          //binary number
          int verticesIndex = 0;
          //each digit in the binary number represent a vertex

          //work out verticesIndex by analyzing each corner
          if (pixelGrid[x][y+1][z]) { //0
            verticesIndex += 1;
          }
          if (pixelGrid[x+1][y+1][z]) { //1
            verticesIndex += 2;
          }
          if (pixelGrid[x+1][y+1][z+1]) { //2
            verticesIndex += 4;
          }
          if (pixelGrid[x][y+1][z+1]) { //3
            verticesIndex += 8;
          }
          if (pixelGrid[x][y][z]) { //4
            verticesIndex += 16;
          }
          if (pixelGrid[x+1][y][z]) { //5
            verticesIndex += 32;
          }
          if (pixelGrid[x+1][y][z+1]) { //6
            verticesIndex += 64;
          }
          if (pixelGrid[x][y][z+1]) { //7
            verticesIndex += 128;
          }

          //look up what polygon to draw using vertices and draw it at x,y,z
          this.drawPolygon(applet, x, y, z, verticesIndex);

        }
      }

      //tell the user the progress of rendering each slice
      System.out.println("Analyzing "+filename+" images: "+((float)z)*100/((float)nSlices)+"%");

    }

    //if we want averaging the normals
    if (wantAverageNormal) {
      this.averageNormal();
    }
    this.createShape(applet);
  }

  //METHODS

  //saveShape
  private void createShape(PApplet applet) {
    this.shape = null;
    this.shape = applet.createShape(PConstants.GROUP);
    Cube cube;
    for (int z=0; z<=this.nSlices; z++) {
      //for every pixel in the slice
      for (int y=0; y<=this.imageHeight; y++) {
        for (int x=0; x<=this.imageWidth; x++) {
          cube = this.cubeArray[x][y][z];
          cube.createShape(applet);
          this.shape.addChild(cube.getShape());
        }
      }
    }
    this.shape.scale(1.0f, this.zScale, 1.0f);
  }

  private void averageNormal() {
    //for every slice
    for (int z=0; z<=nSlices; z++) {
      //for every pixel in the slice
      for (int y=0; y<=imageHeight; y++) {
        for (int x=0; x<=imageWidth; x++) {
          //recalculate the normal in the cube
          this.cubeArray[x][y][z].recalculateNormal(x, y, z, this.imageWidth, this.imageHeight,
              this.nSlices, this.cubeArray);
          //add the cube shape to the shape
        }
      }
      //tell the user the progress of rendering each slice
      System.out.println("Calculating normals: "+((float)z)*100/((float)nSlices)+"%");
    }
  }

  //getShape
  public PShape getShape() {
    return this.shape;
  }

  //drawPolygon at (x,y,z) by looking up what triangles to draw using verticesIndex
  private void drawPolygon(PApplet applet, int x, int y, int z, int verticesIndex) {
    //make an array of edgeIndexs from the lookup table
    //this contains which 3 edges the 3 vertices of the triangles are on, each group of 3 entries
    //are for each triangle
    //the edgeIndexArray will contain a -1 to indicate when there are no more triangles to be drawn
    int [] edgeIndexArray = Lookup.getEdges(verticesIndex);

    //pointer points to entries in edgesArray
    int pointer = 0;

    boolean trianglesToRender = true; //boolean to indicate when there is a triangle to render

    //while there are triangles to render
    while (trianglesToRender) {
      if (edgeIndexArray[pointer]!=-1) { //if the edgeIndex is not -1
        //add a new triangle with 3 vertices on the 3 edges in the edgeIndexArray to the cube
        this.cubeArray[x][y][z].addTriangle(applet, edgeIndexArray[pointer],
            edgeIndexArray[pointer+1], edgeIndexArray[pointer+2]);
        //increase the pointer by 3
        pointer += 3;
      }
      else { //else there are no more triangles to render
        trianglesToRender = false;
      }
    }
  }

  //COLOUR METHODS
  public void setFill(boolean wantFill) {
    this.shape.setFill(wantFill);
  }

  public void setStroke(boolean wantStroke) {
    Cube cube;
    ArrayList<Triangle> triangleArray;
    for (int z=0; z<=this.nSlices; z++) {
      //for every pixel in the slice
      for (int y=0; y<=this.imageHeight; y++) {
        for (int x=0; x<=this.imageWidth; x++) {
          cube = this.cubeArray[x][y][z];
          triangleArray = cube.getTriangleArray();
          for (int triId=0; triId<triangleArray.size(); triId++) {
            triangleArray.get(triId).getShape().setStroke(wantStroke);
          }
        }
      }
    }
  }

  public void setStroke(int colour) {
    Cube cube;
    ArrayList<Triangle> triangleArray;
    for (int z=0; z<=this.nSlices; z++) {
      //for every pixel in the slice
      for (int y=0; y<=this.imageHeight; y++) {
        for (int x=0; x<=this.imageWidth; x++) {
          cube = cubeArray[x][y][z];
          triangleArray = cube.getTriangleArray();
          for (int triId=0; triId<triangleArray.size(); triId++) {
            triangleArray.get(triId).getShape().setStroke(colour);
          }
        }
      }
    }
  }

  public void setFill(int colour) {
    this.shape.setFill(colour);
  }

}

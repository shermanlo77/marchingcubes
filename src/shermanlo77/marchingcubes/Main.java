package shermanlo77.marchingcubes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

import processing.core.PApplet;

/**Main class for Marching Cubes
 * Useage:
 * [-demo0] [-demo1] [-demo2] [-demo3] for an example applet
 * [-file FILE_NAME N_FILE] where FILE_NAME is the location of image sequences and any other
 * suffixes of the png file. Two digits and ".png" are appended to the file name. For example.
 * FILE_NAME = x/y will search for the files x/y00.png and x/y01.png and so on
 * N_FILE is an integer, the number of images in the image sequence
 */
public class Main {

  static String FILE_NAME; //user input for location of image sequence
  static int N_FILE; //number of images in the sequence

  public static void main(String[] args) throws FileNotFoundException {
    //print license
    InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("LICENSE");
    Scanner scanner = new Scanner(inputStream);
    scanner.useDelimiter("\n");
    while (scanner.hasNext()) {
      System.out.println(scanner.next());
    }
    scanner.close();

    System.out.println();
    System.out.println("Please also visit https://github.com/shermanlo77/marchingcubes");
    System.out.println();

    //parse args
    if (args.length > 0) {
      if (args[0].equals("-demo0")) {
        PApplet.main("shermanlo77.marchingcubes.Chromo0");
      } else if (args[0].equals("-demo1")) {
        PApplet.main("shermanlo77.marchingcubes.Chromo1");
      } else if (args[0].equals("-demo2")) {
        PApplet.main("shermanlo77.marchingcubes.Chromo2");
      } else if (args[0].equals("-demo3")) {
        PApplet.main("shermanlo77.marchingcubes.Chromo3");
      } else if (args[0].equals("-file") && args.length == 3) {
        FILE_NAME = args[1];
        N_FILE = Integer.parseInt(args[2]);
        File file = new File(FILE_NAME + "00.png");
        if (file.exists()) {
          PApplet.main("shermanlo77.marchingcubes.MCApplet");
        } else {
          throw new FileNotFoundException(file.toString());
        }
      }

    }
  }

}

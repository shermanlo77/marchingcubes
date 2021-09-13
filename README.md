# marchingcubes

MIT License

Copyright (c) 2013-2021 Sherman Lo

A *Java/Processing* implementation of the marching cubes algorithm, including normal averaging [[cite]](http://paulbourke.net/geometry/polygonise/). A sequence of binary `.png` files can be provided to show a 3D render of the resulting polygon.

![Binary images left, polygon render right](cover.jpg)

## How to Use

**IMPORTANT**:
- *Java* 8 is recommended as the latest *Java* versions are not compatible [[for example]](https://stackoverflow.com/questions/59217351/exception-while-setting-up-jogl-with-intellij).

Download from the `.jar` file from the release or compile it using the instructions below. Call the `.jar` file using

```
java -jar marchingcubes-1.0.0-jar-with-dependencies.jar -file [FILE_NAME] [N_IMAGES]
```
where `FILE_NAME` is the location of image sequences and any other suffixes of the `.png` files. Two digits and `.png` are appended to the file name. For example, providing `x/y` will search for the files `x/y00.png`, `x/y01.png` and so on. The parameter `N_IMAGES` is an integer, the number of images in the image sequence.

Use the mouse (click, drag, scroll) to move the camera. Push the space to toggle the wire frame render.

*Issues*:
- An error may occur when exiting the program, this behaviour is expected. See discussions for further information: [[X11Util.Display: Shutdown]](https://discourse.processing.org/t/x11util-display-shutdown/7292), [[Can any one tell me why do I get this error while quitting any P3D seketch]](https://discourse.processing.org/t/can-any-one-tell-me-why-do-i-get-this-error-while-quitting-any-p3d-seketch/13800)

## How to Compile
*Maven* required.

Clone this repository.

Go to the repository and run
```
mvn package
```
and the `.jar` files are located in target/.

## Demos

Demos, using SBFSEM images of a chromosome [[cite]](https://www.science.org/doi/10.1126/sciadv.1602231?intcmp=trendmd-adv), are provided by running
```
java -jar marchingcubes-1.0.0-jar-with-dependencies.jar [-option]
```
where `[-option]` can be one of the following:

### `-demo0`
26 images of size 72 x 57

![Image of chromosome using demo0](demo0.png)

### `-demo1`
28 images of size 174 x 222

![Image of chromosome using demo1](demo1.png)

### `-demo2`
59 images of size 106 x 125

![Image of chromosome using demo2](demo2.png)

### `-demo3`
- External shell (blue) created using 24 images of size 50 x 67
- Internal features (red) created using 48 images of size 101 x 134

![Image of chromosome using demo3](demo3.png)

## References

- Bourke, P. (1994). Polygonising a scalar field (Marching Cubes). http://paulbourke.net/geometry/polygonise/. Accessed September 2013.
- Chen, B., Yusuf, M., Hashimoto, T., Estandarte, A.K., Thompson, G. and Robinson, I. (2017). Three-dimensional positioning and structure of chromosomes in a human prophase nucleus. *Science Advances*, 3(7):e1602231.
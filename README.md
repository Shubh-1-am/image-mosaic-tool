# image-mosaic-tool
Create composite images using a directory of smaller images with this photomosaic program. Customize the size and arrangement of the smaller images, and use the color matching and image scaling options to create visually interesting and unique images. Use the program for creating wallpapers, social media posts, and more

## Demo

Input Image

![Demo_InputImage](https://github.com/Shubh-1-am/image-mosaic-tool/blob/main/alien.png?raw=true)

Mosaic

![Demo_OutputImage](https://github.com/Shubh-1-am/image-mosaic-tool/blob/main/OUTPUT_Image_alien.png?raw=true)



## Usage

To use the program, run the following command in the terminal:


```java
java Driver <inputImagePath> <sourceImagesDirectoryPath> <processedSourceImagesDirectoryPath> [downscaleSizeOfSourceImages] [downScaleSizeOfInputImage] [tileSize]

```
The first three arguments are required and should be the path to the input image, the directory containing the source images, and the directory where the processed source images should be stored, respectively.<
The last three arguments are optional and have default values.
The **downscaleSizeOfSourceImages** argument specifies the size to which the source images should be downscaled (default value is 32). The **downScaleSizeOfInputImage** argument specifies the size to which the input image should be downscaled (default value is 512). The **tileSize** argument specifies the size of the patches in the output image (should be a proper divisor of **downScaleSizeOfInputImage**, default value is 8).

**Note**: The tileSize should be a proper divisor of the downScaleSizeOfInputImage. If it is not, the program will throw an exception.

**Note:** The program may take some time to execute depending on the number of source images and the size of the input image.

The program will create an output image file in the project directory with the name "**OUTPUT_Image_<inputImageFileName>**" where <inputImageFileName> is the file name of the input image. The program will also create a downscaled version of the input image in the project directory with the name "**Scale_Image_<inputImageFileName>**" if the downScaleSizeOfInputImage argument is provided.

## Example 

1. Install Java Development Kit (JDK) on your system if it is not already installed. You can check if JDK is installed by running the command java -version in the terminal. If JDK is not installed, you can install it using your system's package manager. For example, on Fedora Linux, you can use the command sudo dnf install java-11-openjdk-devel to install JDK.

2. Clone or download the project repository and navigate to the directory containing the project files.

3. Compile the project by running the following command in the terminal.

```java
javac *.java
```
4. Run the Driver file.

The following command will generate a mosaic image with a downscaled input image of size 512 and a patch size of 8, using the default downscaled source image size of 32:

```java
java Driver "input.jpg" "source_images" "processed_images"
```

The following command will generate a mosaic image with a downscaled input image of size 256 and a patch size of 4, using a downscaled source image size of 64:

```java
java Driver "input.jpg" "source_images" "processed_images" 64 256 4

```

## Working

DBProcessor is a class that processes the source images by downscaling them and calculating their average RGB values. These processed images and their average RGB values are then stored in a CSV file. This CSV file is used by the Mosaic class to generate the mosaic image.

The Mosaic class takes as input the path to the input image, the path to the CSV file containing the processed source images, the size to which the input image should be downscaled, and the size of the patches in the output image. It first downscales the input image and then generates the mosaic image by replacing each patch of the input image with the most similar image from the database. The similarity between two images is measured using the Euclidean distance between their average RGB values. The Mosaic class also displays the output image using Java Swing.

**Note:** The source images are taken from [Kaggle](https://www.kaggle.com/). However, you can also use your own source images by replacing the ones in the source_images directory. Just make sure that the source images are in the .jpg or .png format and that the directory structure remains the same.

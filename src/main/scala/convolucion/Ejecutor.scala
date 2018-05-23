package convolucion

import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.awt.Image
import java.awt.image.ConvolveOp
import java.awt.image.Kernel
import util.control.Breaks._

object Ejecutor extends App{
  def convolucion(datosImagen: BufferedImage, ancho: Int, alto: Int, filtro: Array[Float]): BufferedImage ={
    //int limiteArrayImagen = 640*200;
    var bisalida = new BufferedImage(datosImagen.getWidth(null), datosImagen.getHeight(null), BufferedImage.TYPE_BYTE_GRAY);
    for (y <- 0 to (alto-1)) {

      for (x <- 0 to (ancho-1)) {
        var pixelNuevo = 0.0;

        for (j <- 0 to (3-1)) {
          breakable {
            if (((y + j) < 1) || ((y + j - 1) >= alto))
              break;

            for (i <- 0 to (3 - 1)) {
              breakable {
                if (((x + i) < 1) || ((x + i - 1) >= ancho))
                  break;

                val k = filtro(i + j * 3);
                val indice = ((y + j - 1) * ancho) + x + i - 1;
                val pixel = datosImagen.getRaster().getSample(x + i - 1, y + j - 1, 0);
                pixelNuevo += k * pixel;
              }
            }
          }
        }
        if(pixelNuevo<0)pixelNuevo=0;
        if(pixelNuevo>255)pixelNuevo=255;
        bisalida.getRaster().setSample(x,y,0,pixelNuevo)
      }
    }
    return bisalida
  }

    println("Hello, world!")
    val imagen = ImageIO.read(new File("000000000139.jpg"))
    val imagenMenor = imagen.getScaledInstance(640,200,Image.SCALE_SMOOTH)
    val bImagenMenor = new BufferedImage(imagenMenor.getWidth(null), imagenMenor.getHeight(null), BufferedImage.TYPE_INT_ARGB);

    // Draw the image on to the buffered image
    val bGr = bImagenMenor.createGraphics()
    bGr.drawImage(imagenMenor, 0, 0, null)
    bGr.dispose()

    val filtroAfilador = Array(0.0, -1.0, 0.0,
                        -1.0,  5.0, -1.0,
                        0.0, -1.0, 0.0)
    val filtroBordeador = Array(-1.0,-1.0,-1.0,
                                -1.0,8.0,-1.0,
                                -1.0,-1.0,-1.0)
  val filtroAfiladorF = Array(0.0f, -1.0f, 0.0f,
    -1.0f,  5.0f, -1.0f,
    0.0f, -1.0f, 0.0f)
  val filtroBordeadorF = Array(-1.0f,-1.0f,-1.0f,
    -1.0f,8.0f,-1.0f,
    -1.0f,-1.0f,-1.0f)

    val imagenGris = ImageUtil.toGrayscale(bImagenMenor)
    ImageIO.write(imagenGris, "jpg", new File("redim.jpg"))

    /*val afilador = new ConvolveOp(new Kernel(3, 3, filtroAfiladorF))
    val bordeador = new ConvolveOp(new Kernel(3, 3, filtroBordeadorF))
    val bipaso1 = afilador.filter(imagenGris, null)
    ImageIO.write(bipaso1, "jpg", new File("paso1.jpg"))
    val bipaso2 = bordeador.filter(bipaso1, null)*/

    val bipaso1 = convolucion(imagenGris,640,200,filtroAfiladorF);
    ImageIO.write(bipaso1, "jpg", new File("paso1.jpg"))
    val bipaso2 = convolucion(bipaso1,640,200,filtroBordeadorF);

    ImageIO.write(bipaso2, "jpg", new File("paso2.jpg"))

}

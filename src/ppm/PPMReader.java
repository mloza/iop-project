package ppm;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * User: scroot
 * Date: 10.09.12
 * Time: 20:38
 */
public class PPMReader {

	public static BufferedImage readFile(String filename) throws IncompatibleFormat, FileNotFoundException, BrokenImageException, IOException {
		Integer width = 0, height = 0;
		BufferedImage image;

		RandomAccessFile f = new RandomAccessFile(filename, "r");
		if(!f.readLine().contentEquals("P6")) throw new IncompatibleFormat();
		String wh[] = f.readLine().split(" ");
		width = Integer.parseInt(wh[0]);
		if(wh.length > 1)
		{
			height = Integer.parseInt(wh[1]);
			if(height < 1) {
				height = Integer.parseInt(f.readLine());
				if(height < 1) throw new BrokenImageException("Something wrong with format");
			}
		}

		image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		WritableRaster raster = image.getRaster();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				for (int i = 0; i < 3; ++i) {
					raster.setSample(x, y, i, f.read());
				}
			}
		}

		return image;
	}

}


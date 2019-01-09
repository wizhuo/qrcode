package com.wizhuo.qrcode.util;

import com.google.zxing.common.BitMatrix;
import com.wizhuo.qrcode.factory.QrCodeFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * @author lizhuo
 * @since 2019/1/8 15:50
 */
public class MatrixToImageWriter {

    private static Logger LOGGER = LogManager.getLogger();
    //用于设置图案的颜色
    private static final int BLACK = 0xFF000000;
    //用于背景色
    private static final int WHITE = 0xFFFFFFFF;

    private MatrixToImageWriter() {
    }

    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, (matrix.get(x, y) ? BLACK : WHITE));
            }
        }
        return image;
    }

    /**
     * 返回图片url
     *
     * @param matrix
     * @param format
     * @param file
     * @return
     * @throws IOException
     */
    public static String writeToFile(BitMatrix matrix, String format, File file) throws IOException {

        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, file)) {
            LOGGER.info("生成图片失败");
            throw new IOException("Could not write an image of format " + format + " to " + file);
        } else {
            LOGGER.info("图片生成成功！");
            uploadToOss(file);
            return file.getName();


        }
    }

    /**
     * 返回图片url
     *
     * @param matrix
     * @param format
     * @param file
     * @param logUri
     * @throws IOException
     */
    public static String writeToFileWithLogo(BitMatrix matrix, String format, File file, String logUri) throws IOException {

        BufferedImage image = toBufferedImage(matrix);
        //设置logo图标
        QrCodeFactory logoConfig = new QrCodeFactory();
        image = logoConfig.setMatrixLogo(image, logUri);

        if (!ImageIO.write(image, format, file)) {
            LOGGER.warn("生成图片失败");
            throw new IOException("Could not write an image of format " + format + " to " + file);
        } else {
            LOGGER.info("图片生成成功！");
            uploadToOss(file);
            return file.getName();
        }
    }

    //上传图片到oos
    private static void uploadToOss(File file) {
      /*  FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            AliyunOssClientTokenUtil aliyunOssClientTokenUtil = new AliyunOssClientTokenUtil();
            aliyunOssClientTokenUtil.uploadToAliyunOss(file.getName(), fileInputStream);
            //关闭流
            if (Objects.nonNull(fileInputStream)) {
                fileInputStream.close();
            }
        } catch (IOException e) {
            LOGGER.error(e);
            throw new RuntimeException("上传文件到oss 失败", e);
        } finally {
            if (Objects.nonNull(fileInputStream)) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    LOGGER.error(e);
                    throw new RuntimeException("上传文件到oss 失败", e);
                }

            }
        }*/
    }


}

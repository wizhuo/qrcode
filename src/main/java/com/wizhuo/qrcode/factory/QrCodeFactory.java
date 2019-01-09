package com.wizhuo.qrcode.factory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.wizhuo.qrcode.util.MatrixToImageWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

/**
 * @author lizhuo
 * @since 2019/1/8 15:51
 */
public class QrCodeFactory {

    private static Logger LOGGER = LogManager.getLogger();

    /**
     * 给生成的二维码添加中间的logo
     *
     * @param matrixImage 生成的二维码
     * @param logUri      logo地址
     * @return 带有logo的二维码
     * @throws IOException logo地址找不到会有io异常
     */
    public BufferedImage setMatrixLogo(BufferedImage matrixImage, String logUri) throws IOException {
        /**
         * 读取二维码图片，并构建绘图对象
         */
        Graphics2D g2 = matrixImage.createGraphics();
        int matrixWidth = matrixImage.getWidth();
        int matrixHeigh = matrixImage.getHeight();

        /**
         * 读取Logo图片
         */
        BufferedImage logo = ImageIO.read(new File(logUri));

        //开始绘制图片
        g2.drawImage(logo, matrixWidth / 5 * 2, matrixHeigh / 5 * 2, matrixWidth / 5, matrixHeigh / 5, null);
        //绘制边框
        BasicStroke stroke = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        // 设置笔画对象
        g2.setStroke(stroke);
        //指定弧度的圆角矩形
        RoundRectangle2D.Float round = new RoundRectangle2D.Float(matrixWidth / 5 * 2, matrixHeigh / 5 * 2, matrixWidth / 5, matrixHeigh / 5, 20, 20);
        g2.setColor(Color.white);
        // 绘制圆弧矩形
        g2.draw(round);

        //设置logo 有一道灰色边框
        BasicStroke stroke2 = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        // 设置笔画对象
        g2.setStroke(stroke2);
        RoundRectangle2D.Float round2 = new RoundRectangle2D.Float(matrixWidth / 5 * 2 + 2, matrixHeigh / 5 * 2 + 2, matrixWidth / 5 - 4, matrixHeigh / 5 - 4, 20, 20);
        g2.setColor(new Color(128, 128, 128));
        // 绘制圆弧矩形
        g2.draw(round2);


        g2.dispose();
        matrixImage.flush();
        return matrixImage;

    }


    /**
     * 创建我们的二维码图片,并且返回文件名称
     *
     * @param content    二维码内容
     * @param format     生成二维码的格式 例如jpg
     * @param outFileUri 二维码的生成地址
     * @param logUri     二维码中间logo的地址
     * @param width      用于设定图片大小（可变参数，宽，高）默认为430
     * @param height     用于设定图片大小（可变参数，宽，高） 默认为430
     * @throws IOException     抛出io异常
     * @throws WriterException 抛出书写异常
     */
    public String CreatQrImage(String content, String format, String outFileUri, String logUri, int width, int height) throws IOException, WriterException {


        if (width == 0) {
            width = 430;
        }
        if (height == 0) {
            height = 430;

        }
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        // 指定纠错等级,纠错级别（L 7%、M 15%、Q 25%、H 30%）
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 内容所使用字符集编码
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        //hints.put(EncodeHintType.MAX_SIZE, 350);//设置图片的最大值
        //hints.put(EncodeHintType.MIN_SIZE, 100);//设置图片的最小值
        //设置二维码边的空度，非负数
        hints.put(EncodeHintType.MARGIN, 1);

        BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
                //编码类型，目前zxing支持：Aztec 2D,CODABAR 1D format,Code 39 1D,Code 93 1D ,Code 128 1D,
                //Data Matrix 2D , EAN-8 1D,EAN-13 1D,ITF (Interleaved Two of Five) 1D,
                //MaxiCode 2D barcode,PDF417,QR Code 2D,RSS 14,RSS EXPANDED,UPC-A 1D,UPC-E 1D,UPC/EAN extension,UPC_EAN_EXTENSION
                BarcodeFormat.QR_CODE,
                width,
                height,
                hints);

        // 生成二维码图片文件
        File outputFile = new File(outFileUri);

        //指定输出路径
        LOGGER.info("输出文件路径为" + outputFile.getPath());

        if (!StringUtils.isEmpty(logUri)) {
            return MatrixToImageWriter.writeToFileWithLogo(bitMatrix, format, outputFile, logUri);
        } else {
            return MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);
        }


    }


    public static void main(String[] args) {

        String content = "https://baidu.com";
        String outFileUri = "E:\\aa.jpg";
        String format = "jpg";

        try {
            String result = new QrCodeFactory().CreatQrImage(content, format, outFileUri, null, 0, 0);
            System.out.println("result=" + result);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

}

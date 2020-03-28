/*
 * Copyright (c) 2015 by vincent.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package cn.vincent.taste.spicy.helper.image;

import org.apache.commons.lang3.RandomStringUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

/**
 * @author vincent
 * @version 1.0 2017/8/20 03:55
 */
public class ImageHelper {

    /** 创建随机数产生函数 */
    private static final Random RANDOM = new Random();

    public static void main(String[] args) throws IOException {
        String path = "/Users/vincent/Downloads/1.jpg";
        String code = RandomStringUtils.randomAlphanumeric(4);
        BufferedImage image = createCaptcha(94, 35, code, Color.white, Color.gray, new Font("Fixedsys", Font.PLAIN, 15));
        ImageIO.write(image, "jpg", new File(path));

    }

    public static BufferedImage createCaptcha(int width, int height, String code, Color bgColor, Color lineColor, Font font) {
        // 获取每个字占用的宽度
        int eachWidth = width / (code.length() + 2);
        // 设置验证码图片中显示的字体高度
        int eachHight = height - 4;

        // 定义验证码图像的缓冲流
        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 产生图形上下文
        Graphics2D g = buffImg.createGraphics();

        // 将验证码图像背景填充为白色
//        g.setColor(Color.white); // 将验证码图像背景填充为白色
        g.setColor(bgColor == null ? Color.white : bgColor);
        g.fillRect(0, 0, width, height);

        // 创建字体格式，字体的大小则根据验证码图片的高度来设定。
//        Font font = new Font("Fixedsys", Font.PLAIN, fontHeight);
        // 设置字体。
        g.setFont(font);

        // 为验证码图片画边框，为一个像素。
//        g.setColor(Color.GRAY);
        g.setColor(lineColor == null ? Color.GRAY : lineColor);
        g.drawRect(0, 0, width - 1, height - 1);

        // 随机产生图片干扰线条，使验证码图片中的字符不被轻易识别
        int lineCount = 10 + RANDOM.nextInt(10);
        for (int i = 0; i < lineCount; i++) {
            int l = RANDOM.nextInt(width);
            int y = RANDOM.nextInt(height);
            int xl = RANDOM.nextInt(width / 2);
            int yl = RANDOM.nextInt(height / 2);

            g.setColor(new Color(RANDOM.nextInt(255)));
            g.drawLine(l, y, l + xl, y + yl);
        }

        char[] codeChars = code.toCharArray();
        for (int i = 0; i < codeChars.length; i++) {
            // 用随机产生的颜色将验证码绘制到图像中。
            g.setColor(new Color(RANDOM.nextInt(255)));
            g.drawString(String.valueOf(codeChars[i]), (i + 1) * eachWidth, eachHight);
        }
        g.dispose();

        return buffImg;
    }

    /**
     * 调整图片大小
     *
     * @param file   本地文件
     * @param width  宽度
     * @param height 高度
     * @return BufferedImage 图片
     * @throws IOException 异常
     */
    public static BufferedImage resizeImage(File file, Integer width, Integer height) throws IOException {
        Image srcImg = ImageIO.read(file);

        int w = srcImg.getWidth(null);
        int h = srcImg.getHeight(null);
        if (width != null && height != null && width > 0 && height > 0) {
            w = width;
            h = height;
        }

        BufferedImage buffImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        buffImg.getGraphics().drawImage(srcImg.getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, null);
        return buffImg;
    }

    public static void zoom(BufferedImage srcBufferedImage, File destFile, int destHeight, int destWidth, String formatName) throws IOException {
        int imgWidth = destWidth;
        int imgHeight = destHeight;
        int srcWidth = srcBufferedImage.getWidth();
        int srcHeight = srcBufferedImage.getHeight();
        if (srcHeight >= srcWidth) {
            imgWidth = (int) Math.round(((destHeight * 1.0 / srcHeight) * srcWidth));
        } else {
            imgHeight = (int) Math.round(((destWidth * 1.0 / srcWidth) * srcHeight));
        }
        BufferedImage destBufferedImage = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = destBufferedImage.createGraphics();
        graphics2D.setBackground(Color.WHITE);
        graphics2D.clearRect(0, 0, destWidth, destHeight);
        graphics2D.drawImage(srcBufferedImage.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH), (destWidth / 2) - (imgWidth / 2), (destHeight / 2) - (imgHeight / 2), null);
        graphics2D.dispose();
        ImageIO.write(destBufferedImage, formatName, destFile);
    }

    /**
     * 图片缩放(图片等比例缩放为指定大小，空白部分以白色填充)
     *
     * @param srcBufferedImage 源图片
     * @param destFile         缩放后的图片文件
     * @param destHeight       高度
     * @param destWidth        宽度
     * @throws IOException 异常
     */
    public static void zoom(BufferedImage srcBufferedImage, File destFile, int destHeight, int destWidth) throws IOException {
        zoom(srcBufferedImage, destFile, destHeight, destWidth, "jpg");
    }

    /**
     * 添加图片水印
     *
     * @param srcBufferedImage  需要处理的源图片
     * @param destFile          处理后的图片文件
     * @param watermarkFile     水印图片文件
     * @param watermarkPosition 水印坐标
     * @param alpha             透明度
     * @throws IOException 异常
     */
    public static void imageWatermark(BufferedImage srcBufferedImage, File destFile, File watermarkFile, WatermarkPosition watermarkPosition, int alpha) throws IOException {
        int srcWidth = srcBufferedImage.getWidth();
        int srcHeight = srcBufferedImage.getHeight();
        BufferedImage destBufferedImage = new BufferedImage(srcWidth, srcHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = destBufferedImage.createGraphics();
        graphics2D.setBackground(Color.WHITE);
        graphics2D.clearRect(0, 0, srcWidth, srcHeight);
        graphics2D.drawImage(srcBufferedImage.getScaledInstance(srcWidth, srcHeight, Image.SCALE_SMOOTH), 0, 0, null);

        drawWaterMarkImage(watermarkFile, watermarkPosition, alpha, srcWidth, srcHeight, graphics2D);
        graphics2D.dispose();
        ImageIO.write(destBufferedImage, "JPEG", destFile);
    }

    private static void drawWaterMarkImage(File watermarkFile, WatermarkPosition watermarkPosition, int alpha, int srcWidth, int srcHeight, Graphics2D graphics2D) throws IOException {
        if (watermarkFile != null && watermarkFile.exists() && watermarkPosition != null && watermarkPosition != WatermarkPosition.no) {
            BufferedImage watermarkBufferedImage = ImageIO.read(watermarkFile);
            int watermarkImageWidth = watermarkBufferedImage.getWidth();
            int watermarkImageHeight = watermarkBufferedImage.getHeight();
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha / 100.0F));
            int x = 0;
            int y = 0;

            if (watermarkPosition == WatermarkPosition.topRight) {
                x = srcWidth - watermarkImageWidth;
                y = 0;
            } else if (watermarkPosition == WatermarkPosition.center) {
                x = (srcWidth - watermarkImageWidth) / 2;
                y = (srcHeight - watermarkImageHeight) / 2;
            } else if (watermarkPosition == WatermarkPosition.bottomLeft) {
//                x = 0;
                y = srcHeight - watermarkImageHeight;
            } else if (watermarkPosition == WatermarkPosition.bottomRight) {
                x = srcWidth - watermarkImageWidth;
                y = srcHeight - watermarkImageHeight;
            }
//            else if (watermarkPosition == WatermarkPosition.topLeft) {
//                x = 0;
//                y = 0;
//            }
            graphics2D.drawImage(watermarkBufferedImage, x, y, watermarkImageWidth, watermarkImageHeight, null);
        }
    }

    /**
     * 图片缩放并添加图片水印(图片等比例缩放为指定大小，空白部分以白色填充)
     *
     * @param srcBufferedImage 需要处理的图片
     * @param destFile         处理后的图片文件
     * @param watermarkFile    水印图片文件
     * @throws IOException 异常
     */
    public static void zoomAndWatermark(BufferedImage srcBufferedImage, File destFile, int destHeight, int destWidth, File watermarkFile, WatermarkPosition watermarkPosition, int alpha) throws IOException {
        int imgWidth = destWidth;
        int imgHeight = destHeight;
        int srcWidth = srcBufferedImage.getWidth();
        int srcHeight = srcBufferedImage.getHeight();
        if (srcHeight >= srcWidth) {
            imgWidth = (int) Math.round(((destHeight * 1.0 / srcHeight) * srcWidth));
        } else {
            imgHeight = (int) Math.round(((destWidth * 1.0 / srcWidth) * srcHeight));
        }

        BufferedImage destBufferedImage = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = destBufferedImage.createGraphics();
        graphics2D.setBackground(Color.WHITE);
        graphics2D.clearRect(0, 0, destWidth, destHeight);
        graphics2D.drawImage(srcBufferedImage.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH), (destWidth / 2) - (imgWidth / 2), (destHeight / 2) - (imgHeight / 2), null);
        drawWaterMarkImage(watermarkFile, watermarkPosition, alpha, destWidth, destHeight, graphics2D);
        graphics2D.dispose();
        ImageIO.write(destBufferedImage, "JPEG", destFile);
    }

    /**
     * 获取图片文件的真实类型.
     *
     * @param imageFile 图片文件对象.
     * @return 图片文件类型
     * @throws IOException 异常
     */
    public static String getImageFormatName(File imageFile) throws IOException {
        ImageInputStream imageInputStream = ImageIO.createImageInputStream(imageFile);
        Iterator<ImageReader> iterator = ImageIO.getImageReaders(imageInputStream);
        if (!iterator.hasNext()) {
            return null;
        }
        ImageReader imageReader = iterator.next();
        imageInputStream.close();
        return imageReader.getFormatName().toLowerCase();
    }

    public enum WatermarkPosition {

        // 水印位置（无、左上、右上、居中、左下、右下）

        no, topLeft, topRight, center, bottomLeft, bottomRight
    }
}

package com.bhd.create;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

public class CreateImage {
	
	// 图片的宽度。
    private int width;
    // 图片的高度。
    private int height;
    // 字体的宽度
    int fontSize;
    // 验证码干扰线数
    private int lineCount;
    //干扰线倍数
    private int multiple = 20;
    // 验证码图片Buffer
    private BufferedImage buffImg = null;
    Random random = new Random();
	
	/**
	 * 初始化參數
	 * @param fontSize 字体大小
	 * @param multiple 干扰线倍数，默认20倍
	 * @param str 要绘制的文字
	 */
    public CreateImage(int fontSize,int multiple,String str) {
    	
    	this.fontSize = fontSize;
    	this.multiple = multiple;
    	//调用方法绘制图形
    	this.creatImageStr(str);
    }
    
    /**
     * 繪製圖形
     * @param str
     */
    private void creatImageStr(String str) {
    	//根据换行分割字符串
    	String[] infos = str.split("\\n");
    	//行数
    	int row = infos.length;
    	int column = 0;
    	for (String s : infos) {
			if(column < s.length()){
				column = s.length();
			}
		}
    	this.width = column*fontSize+30;
    	this.height = row*(fontSize+10)+20;

        this.lineCount = row*5;
        

        // 图像buffer
        buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = buffImg.getGraphics();
        //Graphics2D g = buffImg.createGraphics();
        // 设置背景色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // 设置字体
        //Font font1 = getFont(fontHeight);
        Font font = new Font("Fixedsys", Font.BOLD, fontSize);
        g.setFont(font);

        // 设置干扰线
        for (int i = 0; i < lineCount*multiple; i++) {
            int xs = random.nextInt(width);
            int ys = random.nextInt(width);
            int xe = random.nextInt(width);
            int ye = random.nextInt(height);
            g.setColor(getRandColor(1, 255));
            g.drawLine(xs, ys, xe, ye);
        }

        // 添加噪点
        float yawpRate = 0.03f;// 噪声率
        int area = (int) (yawpRate * width * height);
        for (int i = 0; i < area; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);

            buffImg.setRGB(x, y, random.nextInt(255));
        }

        //文字绘制x坐标
        int codeX = fontSize/3;
        //文字绘制y坐标
        int codeY = fontSize + 10;
        
        for (int i = 0; i < row; i++) {
        	//绘制的字符串（按行绘制）
        	String strRand = infos[i];
            g.setColor(getRandColor(0, 100));
            // g.drawString(a,x,y);
            // a为要画出来的东西，x和y表示要画的东西最左侧字符的基线位于此图形上下文坐标系的 (x, y) 位置处

            g.drawString(strRand, codeX, codeY);
            codeY += fontSize+10;
        }
    }
    
    public void write(OutputStream sos) throws IOException {
        ImageIO.write(buffImg, "png", sos);
        sos.close();
    }
    
    // 得到随机颜色
    private Color getRandColor(int fc, int bc) {// 给定范围获得随机颜色
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    
    public static void main(String[] args) throws IOException {
    	
        for(int i=0;i<30;i++){
        	String imgname = "newImg"+i+".png";
        	String codeStr = "投资朝鲜股市30年经验：\n一开始以为是在做投资，\n后来才发现这是投机，\n再后来发现这是在赌博，\n最后发现这分明是诈骗，\n直到最终才明白这就是抢劫。";
        	CreateImage createImage = new CreateImage(48,20,codeStr);
        	
        	FileOutputStream fos = new FileOutputStream(new File("D:/upfiles/xxx/",imgname));
        	createImage.write(fos);
        }
	}

}

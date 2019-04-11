package com.chieftain.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;

public class FileNIOUtils {

    /**
     * @param file
     * @return
     * @throws Exception
     */
    public static String read(String file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        // 文件通道
        FileChannel fc = fis.getChannel();
        // 分配和文件同等的缓存区
        ByteBuffer bf = ByteBuffer.allocate((int) fc.size());
        // 文件内容读入缓冲区
        fc.read(bf);
        // 缓冲中位置回复零
        bf.rewind();
        StringBuilder sb = new StringBuilder();
        while (bf.hasRemaining()) {
            sb.append((char) bf.get());
        }
        // 关闭文件通道
        fc.close();
        // 关闭文件输入流
        fis.close();
        return sb.toString();
    }

    /**
     * @param file
     * @return
     * @throws Exception
     */
    public static String read2(String file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        // 文件通道
        FileChannel fc = fis.getChannel();
        // 分配和文件同等的缓存区
        ByteBuffer bf = ByteBuffer.allocate((int) fc.size());
        // 文件内容读入缓冲区
        fc.read(bf);
        // 缓冲中位置回复零
        bf.rewind();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bf.capacity(); i++) {
            sb.append((char) bf.get());
        }
        // 关闭文件通道
        fc.close();
        // 关闭文件输入流
        fis.close();
        return sb.toString();
    }

    /**
     * @param file
     * @return
     * @throws Exception
     */
    public static String read3(String file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        // 文件通道
        FileChannel fc = fis.getChannel();
        // 分配缓存区
        ByteBuffer bf = ByteBuffer.allocate(512);
        StringBuilder sb = new StringBuilder();
        while (fc.read(bf) != -1) {
            //当缓冲区的 limit 设置为之前 position 值时，把缓冲中当前位置回复为零，
            bf.flip();
            while (bf.hasRemaining()) {
                sb.append((char) bf.get());
            }
            // 清理缓冲区，准备再次读取数据
            bf.clear();
        }
        // 缓冲中位置回复零
        bf.rewind();
        // 关闭文件通道
        fc.close();
        // 关闭文件输入流
        fis.close();
        return sb.toString();
    }

    /**
     * @param file
     * @return
     * @throws Exception
     */
    public static String read4(String file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        // 文件通道
        FileChannel fc = fis.getChannel();
        // 映射文件到内存
        MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
        byte[] bt = mbb.array();
        mbb.rewind();
        // 关闭文件通道
        fc.close();
        // 关闭文件输入流
        fis.close();
        return new String(bt);
    }

    /**
     * @param file
     * @param context
     * @throws Exception
     */
    public static void write(String file, String context) throws Exception {
        Charset charset = Charset.forName("UTF-8");//Java.nio.charset.Charset处理了字符转换问题。它通过构造CharsetEncoder和CharsetDecoder将字符序列转换成字节和逆转换。
        CharsetDecoder decoder = charset.newDecoder();

        FileOutputStream fos = new FileOutputStream(file);
        // 文件通道
        FileChannel fc = fos.getChannel();
        int len = context.getBytes().length;
        // 缓冲区
        ByteBuffer bf = ByteBuffer.allocate(len * 2);
        // 放入缓冲区
        for (int j = 0; j < context.length(); j++) {
            bf.putChar(context.charAt(j));
        }
        bf.flip();
        // 缓冲区数据写入到文件中
        fc.write(bf);
        // 关闭文件通道
        fc.close();
        //关闭文件输出流
        fos.close();
    }

    /**
     * @param file
     * @param context
     * @throws Exception
     */
    public static void write2(String file, String context) throws Exception {
        FileOutputStream fos = new FileOutputStream(file);
        // 文件通道
        FileChannel fc = fos.getChannel();
        // 缓冲区
        ByteBuffer bf = ByteBuffer.allocate(512);

        while (bf.limit() < bf.position()) {
            // 放入缓冲区
            for (int j = 0; j < context.length(); j++) {
                bf.putChar(context.charAt(j));
            }
            // 缓冲区数据写入到文件中
            fc.write(bf);
            bf.flip();
        }
        // 关闭文件通道
        fc.close();
        //关闭文件输出流
        fos.close();
    }

    public static String readFile(String filePath) throws Exception {
        Charset charset = Charset.forName("UTF-8");//Java.nio.charset.Charset 处理了字符转换问题。它通过构造 CharsetEncoder 和 CharsetDecoder 将字符序列转换成字节和逆转换。
        CharsetDecoder decoder = charset.newDecoder();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
            FileChannel fileChannel = fis.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            CharBuffer charBuffer = CharBuffer.allocate(1024);
            StringBuffer stringBuffer = new StringBuffer();
            int bytes = fileChannel.read(byteBuffer);
            while (bytes != -1) {
                byteBuffer.flip();
                decoder.decode(byteBuffer, charBuffer, false);
                charBuffer.flip();
                stringBuffer.append(charBuffer);
                charBuffer.clear();
                byteBuffer.clear();
                bytes = fileChannel.read(byteBuffer);
            }
            if (fis != null) {
                fis.close();
            }
            return stringBuffer.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String readFile(File file) throws Exception {
        // Charset.forName("UTF-8");//Java.nio.charset.Charset 处理了字符转换问题。它通过构造 CharsetEncoder 和 CharsetDecoder 将字符序列转换成字节和逆转换。
        Charset charset = StandardCharsets.UTF_8;
        CharsetDecoder decoder = charset.newDecoder();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            FileChannel fileChannel = fis.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            CharBuffer charBuffer = CharBuffer.allocate(1024);
            StringBuffer stringBuffer = new StringBuffer();
            int bytes = fileChannel.read(byteBuffer);
            while (bytes != -1) {
                byteBuffer.flip();
                decoder.decode(byteBuffer, charBuffer, false);
                charBuffer.flip();
                stringBuffer.append(charBuffer);
                charBuffer.clear();
                byteBuffer.clear();
                bytes = fileChannel.read(byteBuffer);
            }
            if (fis != null) {
                fis.close();
            }
            return stringBuffer.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * writeFile
     * @param content
     * @param fileName
     * @throws IOException
     */
    private static void writeFile(String content, String folder, String fileName) throws IOException {
        File f = new File(folder);
        f.setWritable(true);
        if (!f.exists()) {  // 如果该路径不存在，就创建该路径
            f.mkdir();
        }
        String filePath = folder + "/" + fileName;  // 得到完整文件路径
        FileOutputStream fos = null;
        FileChannel fc_out = null;
        try {
            fos = new FileOutputStream(filePath, true);
            fc_out = fos.getChannel();
            ByteBuffer buf = ByteBuffer.wrap(content.getBytes("UTF-8"));
            buf.put(content.getBytes());
            buf.flip();
            fc_out.write(buf);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != fc_out) {
                fc_out.close();
            }
            if (null != fos) {
                fos.close();
            }
        }
    }
    public static boolean isExcel2003(String filePath)  {  
        return filePath.matches("^.+\\.(?i)(xls)$");  
  }  
       
  /**
   * @描述：是否是2007的excel，返回true是2007
   * @param filePath
   * @return
   */
  public static boolean isExcel2007(String filePath)  {  
        return filePath.matches("^.+\\.(?i)(xlsx)$");  
  }
    
  /**
   * 验证是否是EXCEL文件
   * @param filePath
   * @return
   */
  public static boolean validateExcel(String filePath){
        if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))){  
            return false;  
        }  
        return true;
  }

    /**
     * 传统IO赋值
     * @param sourceFile
     * @param targetFile
     * @throws Exception
     */
    public static void traditionCopy(File sourceFile, File targetFile, boolean discardSourceFile) throws Exception {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        byte[] buffer = new byte[2097152];
        try {
            fis = new FileInputStream(sourceFile);
            fos = new FileOutputStream(targetFile);
            int read = 0;
            long startTime = System.currentTimeMillis();
            while ((read = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, read);
            }
            long EndTime = System.currentTimeMillis();
            System.out.print("traditionCopy用了毫秒数：");
            System.out.println(EndTime - startTime);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                fos.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        if(discardSourceFile) {
            sourceFile.deleteOnExit();
        }
    }

    public static void nioCopy(File sourceFile, File targetFile, boolean discardSourceFIle) throws Exception {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel inFiC = null;
        FileChannel outFoC = null;
        int length = 2097152;
        try {
            fis = new FileInputStream(sourceFile);
            fos = new FileOutputStream(targetFile);
            inFiC = fis.getChannel();
            outFoC = fos.getChannel();
            long startTime = System.currentTimeMillis();
            while (inFiC.position() != inFiC.size()) {
                if ((inFiC.size() - inFiC.position()) < length) {
                    length = (int) (inFiC.size() - inFiC.position());// 读取剩下的
                }
                inFiC.transferTo(inFiC.position(), length, outFoC);
                inFiC.position(inFiC.position() + length);
            }
            long EndTime = System.currentTimeMillis();
            System.out.print("nioCopy,用了毫秒总数：");
            System.out.println(EndTime - startTime);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outFoC != null) {
                outFoC.close();
            }
            if (inFiC != null) {
                inFiC.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        if(discardSourceFIle) {
            sourceFile.deleteOnExit();
        }
    }

    public static void nioMappedCopy(File sourceFile, File targetFile, boolean discardSourceFile) throws Exception {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel inFiC = null;
        FileChannel outFoC = null;
        int length = 2097152;
        try {
            fis = new FileInputStream(sourceFile);
            fos = new FileOutputStream(targetFile);
            inFiC = fis.getChannel();
            outFoC = fos.getChannel();
            long startTime = System.currentTimeMillis();
            while (inFiC.position() != inFiC.size()) {
                if ((inFiC.size() - inFiC.position()) < length) {
                    length = (int) (inFiC.size() - inFiC.position());
                }
                MappedByteBuffer inbuffer = inFiC.map(FileChannel.MapMode.READ_ONLY, inFiC.position(), length);
                outFoC.write(inbuffer);
                inFiC.position(inFiC.position() + length);
            }
            long EndTime = System.currentTimeMillis();
            System.out.print("nioMappedCopy用了毫秒数：");
            System.out.println(EndTime - startTime);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outFoC != null) {
                outFoC.close();
            }
            if (inFiC != null) {
                inFiC.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        if(discardSourceFile) {
            sourceFile.deleteOnExit();
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
//        String text = read("C:\\DEMO.java");
//        String text2 = read2("C:\\DEMO.java");
//        String text3 = read3("C:\\DEMO.java");
//        System.out.println(text3);
//        write("C:\\DEMO2.java",text);
//        write("C:\\DEMO3.java",text2);
//        String text4 = read4("C:\\DEMO.java");
//        System.out.println(text4);

        System.out.println(readFile("F:\\桌面文件\\note.txt"));
        writeFile(readFile("F:\\桌面文件\\note.txt"),"F:\\桌面文件","note2.txt");
    }

}
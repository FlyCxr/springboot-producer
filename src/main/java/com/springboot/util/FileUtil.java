package com.springboot.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * File工具类
 */
@SuppressWarnings("all")
public class FileUtil {
	
	public static final int BUFFER_SIZE = 4096;
	
	/**
	 * 清空并删除指定目录
	 * @param dir
	 */
	public static void delete(File dir){
		if(dir != null){
			dir.listFiles(new FileFilter() {
				@Override
				public boolean accept(File f) {
					if(f.isDirectory()){
						delete(f);
					}else{
						f.delete();
					}
					return false;
				}
			});
			dir.delete();
		}
	}
	
	/**
	 * 清空并删除指定路径
	 * @param pathNme
	 */
	public static void delete(String pathNme){
		if(!pathNme.isEmpty()){
			File file = new File(pathNme);
			if(!file.isDirectory()){
				file.delete();
			}else{
				String[] list = file.list();
				for (int i = 0; i < list.length; i++) {
					File delFile = new File(pathNme + File.separator + list[i]);
					if(!delFile.isDirectory()){
						delFile.delete();
					}else{
						delete(pathNme + File.separator + list[i]);
					}
				}
				file.delete();
			}
		}
	}
	
	/**
	 * 
	 * @param src 源文件
	 * @param dest 目标文件
	 * @param position 从position+1字节位置开始
	 * @param count	复制count个字节
	 * @return
	 * @throws IOException
	 */
	public static long copy(File src, File dest, long position, long count) throws IOException {
		FileInputStream in = new FileInputStream(src);
		FileOutputStream out = new FileOutputStream(dest);
		FileChannel inChannel = in.getChannel();
		FileChannel outChannel = out.getChannel();
		try {
			return inChannel.transferTo(position, count, outChannel);
		} finally {
			if (inChannel != null) {
				inChannel.close();
			}
			if (outChannel != null) {
				outChannel.close();
			}
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}
	
	/**
	 * 复制(字节流缓冲区,BufferedInputStream/BufferedOutputStream)
	 * @param in 带缓冲区的输入流
	 * @param out 带缓冲区的输出流
	 * @return 读书或写入的字节数
	 * @throws IOException
	 */
	public static int copy(File in, File out) throws IOException {
		return copy(new BufferedInputStream(new FileInputStream(in)), new BufferedOutputStream(new FileOutputStream(out)));
	}
	
	/**
	 * 复制(字节流超类,InputStream/OutputStream)
	 * @param in 输入流读取
	 * @param out 输出流写入
	 * @return 读书或写入的字节数
	 * @throws IOException
	 */
	public static int copy(InputStream in, OutputStream out) throws IOException {
		int byteCount = 0;
		byte[] buffer = new byte[BUFFER_SIZE];
		int bytesRead = -1;
		try {
			while((bytesRead=in.read(buffer)) != -1){
				out.write(buffer, 0, bytesRead);
				byteCount += bytesRead;
			}
			out.flush();
			return byteCount;
		} finally{
			if(null != in){
				in.close();
			}
			if(null != out){
				out.close();
			}
		}
	}
	
	/**
	 * 
	 * @param in byte数组
	 * @param out 输出文件File
	 * @throws IOException
	 */
	public static void copy(byte[] in, File out) throws IOException {
		ByteArrayInputStream inStream = new ByteArrayInputStream(in);
		OutputStream outStream = new BufferedOutputStream(new FileOutputStream(out));
		copy(inStream, outStream);
	}
	
	/**
	 * 
	 * @param in 输入流
	 * @return byte数组
	 * @throws IOException
	 */
	public static byte[] copyToByteArray(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(BUFFER_SIZE);
		copy(in, out);
		return out.toByteArray();
	}
	
	/**
	 * 复制(字符流超类,Reader/Writer)
	 * @param in Reader字符输入流超类
	 * @param out Writer字符输出流超类
	 * @return
	 * @throws IOException
	 */
	public static int copy(Reader in, Writer out) throws IOException {
		int byteCount = 0;
		char[] buffer = new char[BUFFER_SIZE];
		int bytesRead = -1;
		try {
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
				byteCount += bytesRead;
			}
			out.flush();
			return byteCount;
		} finally {
			if(null != in){
				in.close();
			}
			if(null != out){
				out.close();
			}
		}
	}
	
	/**
	 * 将指定字符串写到输出流Writer
	 * @param out
	 * @throws IOException
	 */
	public static void copy(String str, Writer out) throws IOException {
		try {
			out.write(str);
		} finally {
			if(null != out){
				out.close();
			}
		}
	}
	
	/**
	 * 读取输入流中的数据,返回字符串
	 * @param in
	 * @return String str
	 * @throws IOException
	 */
	public static String copyToString(Reader in) throws IOException {
		StringWriter out = new StringWriter();
		copy(in, out);
		return out.toString();
	}
	
	/**
	 * 读取指定文件，以行为单位返回List
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static List<String> readLines(Reader input) throws IOException {
		BufferedReader reader = new BufferedReader(input);
		List<String> list = new ArrayList<String>();
		String line = reader.readLine();
		while (line != null) {
			list.add(line);
			line = reader.readLine();
		}
		return list;
	}
	
	/**
	 * 获取输入流中的内容
	 * @param file
	 * @return 字符串
	 * @throws IOException
	 */
	public static String readFile(File file) throws IOException {
		Reader in = new FileReader(file);
		StringWriter out = new StringWriter();
		copy(in, out);
		return out.toString();
	}
	
	/**
	 * 根据指定编码集读取File文件内容
	 * @param file
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
	public static String readFile(File file, String encoding) throws IOException {
		InputStream inputStream = new FileInputStream(file);
		return toString(inputStream, encoding);
	}
	
	/**
	 * 读取InputStream里的内容，返回字符串
	 * @param inputStream
	 * @return 字符串
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public static String toString(InputStream inputStream) throws UnsupportedEncodingException, IOException {
		Reader reader = new InputStreamReader(inputStream);
		StringWriter writer = new StringWriter();
		copy(reader, writer);
		return writer.toString();
	}
	
	/**
	 * 根据指定编码集读取InputStream里的内容，返回字符串
	 * @param  inputStream
	 * @param encoding
	 * @return 字符串
	 * @throws IOException 
	 */
	public static String toString(InputStream inputStream,String encoding) throws UnsupportedEncodingException, IOException {
		StringWriter writer = null;
		Reader reader = new InputStreamReader(inputStream, encoding);
		writer = new StringWriter();
		copy(reader, writer);
		return writer.toString();
	}
	
	/**
	 * 将指定字符串追加到指定文件
	 * @param file 目标文件
	 * @param content 保存的字符串
	 * @param encoding 编码格式
	 * @param append true在末尾追加，false先覆盖再追加
	 * @throws IOException
	 */
	public static void saveFile(File file, String content, String encoding, boolean append) throws IOException {
		FileOutputStream output = new FileOutputStream(file, append);
		Writer writer = encoding == null ? new OutputStreamWriter(output) : new OutputStreamWriter(output, encoding);
		writer.write(content);
		writer.close();
	}
	
	/**
	 * 将指定字符串追加到指定文件,默认覆盖再追加
	 * @param file
	 * @param content
	 * @throws IOException
	 */
	public static void saveFile(File file, String content) throws IOException {
		saveFile(file, content, null, false);
	}
	
	/**
	 * 将指定字符串追加到指定文件,默认覆盖再追加
	 * @param file
	 * @param content
	 * @param append
	 * @throws IOException
	 */
	public static void saveFile(File file, String content, boolean append) throws IOException {
		saveFile(file, content, null, append);
	}
	
	/**
	 * 根据指定编码将指定字符串追加到指定文件，默认覆盖
	 * @param file
	 * @param content
	 * @param encoding
	 * @throws IOException
	 */
	public static void saveFile(File file, String content, String encoding) throws IOException {
		saveFile(file, content, encoding, false);
	}
	
	/**
	 * 获取相对路径
	 * @param baseDir File
	 * @param file File
	 * @return
	 */
	public static String getRelativePath(File baseDir, File file) {
		if (baseDir.equals(file)) {
			return "";
		}
		if (baseDir.getParentFile() == null) {
			return file.getAbsolutePath().substring(baseDir.getAbsolutePath().length());
		}
		return file.getAbsolutePath().substring(baseDir.getAbsolutePath().length() + 1);
	}
	
	/**
	 * 获取输入流对象。
	 * 如果以"classpath:"开头特殊处理
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	public static InputStream getInputStream(String file) throws FileNotFoundException {
		InputStream inputStream = null;
		if (file.startsWith("classpath:")) {
			inputStream = FileUtil.class.getClassLoader().getResourceAsStream(file.substring("classpath:".length()));
		} else {
			inputStream = new FileInputStream(file);
		}
		return inputStream;
	}
	
	/**
	 * 创建多层目录文件夹
	 * @param dir
	 * @param file
	 * @return
	 */
	public static File mkdir(String dir, String file) {
		if (dir == null)
			throw new IllegalArgumentException("dir must be not null");
		File result = new File(dir, file);
		parnetMkdir(result);
		return result;
	}
	
	/**
	 * 创建多层目录文件夹
	 * @param outputFile
	 */
	public static void parnetMkdir(File outputFile) {
		if (outputFile.getParentFile() != null) {
			outputFile.getParentFile().mkdirs();
		}
	}
	
	/**
	 * 删除目录
	 * @param directory
	 * @throws IOException
	 */
	public static void deleteDirectory(File directory) throws IOException {
		if (!directory.exists()) {
			return;
		}
		cleanDirectory(directory);
		if (!directory.delete()) {
			//无法删除目录
			String message = "Unable to delete directory " + directory + ".";
			throw new IOException(message);
		}
	}
	
	public static boolean deleteQuietly(File file) {
		if (file == null) {
			return false;
		}
		try {
			if (file.isDirectory()) {
				cleanDirectory(file);
			}
		} catch (Exception e) {
		}

		try {
			return file.delete();
		} catch (Exception e) {
			return false;
		}
	}
	
	public static void cleanDirectory(File directory) throws IOException {
		if (!directory.exists()) { //目录不存在
			String message = directory + " does not exist";
			throw new IllegalArgumentException(message);
		}
		if (!directory.isDirectory()) {// 不是一个目录
			String message = directory + " is not a directory";
			throw new IllegalArgumentException(message);
		}
		File[] files = directory.listFiles();
		if (files == null) { // 没有内容的数组
			throw new IOException("Failed to list contents of " + directory);
		}
		IOException exception = null;
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			try {
				forceDelete(file);
			} catch (IOException ioe) {
				exception = ioe;
			}
		}

		if (null != exception) {
			throw exception;
		}
	}

	public static void forceDelete(File file) throws IOException {
		if (file.isDirectory()) {
			deleteDirectory(file);
		} else {
			boolean filePresent = file.exists();
			if (!file.delete()) {
				if (!filePresent) {
					throw new FileNotFoundException("File does not exist: " + file);
				}
				String message = "Unable to delete file: " + file;
				throw new IOException(message);
			}
		}
	}
	
	/**
	 * 判断文件是否存在
	 * @param filePath
	 * @return
	 */
	public static boolean exists(String filePath) {
		return new File(filePath).exists();
	}
	
	/**
	 * 判断是否文件
	 * @param filePath
	 * @return
	 */
	public static boolean isFile(String filePath) {
		return new File(filePath).exists() && new File(filePath).isFile();
	}
	
	/**
	 * 判断是否文件夹
	 * @param filePath
	 * @return
	 */
	public static boolean isDir(String filePath) {
		return new File(filePath).exists() && new File(filePath).isDirectory();
	}
	
	/**
	 * 获取文件后缀(带点)
	 * 
	 * @param filename
	 * @return
	 */
	public static String getExtension(String filename) {
		if (filename == null) {
			return null;
		}
		int index = filename.lastIndexOf(".");
		if (index == -1) {
			return "";
		} else {
			return filename.substring(index);
		}
	}
	
	/**
	 * 获取文件后缀(不带点)
	 * @param fileName
	 * @return
	 */
	public static String getSuffix(String fileName) {
		return fileName.substring(fileName.lastIndexOf(".") + 1);
	}

	/**
	 * 获取文件名
	 * @param filePath
	 * @return
	 */
	public static String getFileName(String filePath) {
		return filePath.substring(filePath.lastIndexOf("/") + 1);
	}

	/**
	 * 获取文件不带后缀的全路径
	 * @param filePath
	 * @return
	 */
	public static String getNoSuffixFilePath(String filePath) {
		return filePath.substring(0, filePath.lastIndexOf("."));
	}
	
	/**
	 * 根据类加载器获取File对象
	 * @param resourceName
	 * @return
	 * @throws IOException
	 */
	public static File getFileByClassLoader(String resourceName) throws IOException {
		Enumeration<URL> urls = FileUtil.class.getClassLoader().getResources(resourceName);
		while (urls.hasMoreElements()) {
			return new File(urls.nextElement().getFile());
		}
		throw new FileNotFoundException(resourceName);
	}
}

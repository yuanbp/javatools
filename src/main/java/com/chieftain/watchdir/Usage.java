package com.chieftain.watchdir;

import java.io.File;

/**
 * 
 * @author Goofy
 * @Date 2015年7月6日 上午11:22:32
 */
public class Usage {

	public static void main(String[] args) throws Exception {
		final File file = new File("D:\\upload");
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					new WatchDir(file, true, new FileActionCallback() {
						@Override
						public void create(File file) {
							System.out.println("文件已创建\t" + file.getAbsolutePath());
						}

						@Override
						public void delete(File file) {
							System.out.println("文件已删除\t" + file.getAbsolutePath());
						}

						@Override
						public void modify(File file) {
							System.out.println("文件已修改\t" + file.getAbsolutePath());
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

		System.out.println("正在监视文件夹:" + file.getAbsolutePath() + "的变化");
	}

}

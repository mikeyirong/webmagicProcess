package com.factory;

/**
 * @author mike_yi
 * @description 定时任务
 * @since 
 */
public class IpInfoSchedule {
    @SuppressWarnings("static-access")
	public static void main(String[] args) {
		while(true) {
		  try {
		    new	KuaidaiProcess().execute();
		    Thread.currentThread().sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		}
	}
}

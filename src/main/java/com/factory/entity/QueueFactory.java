package com.factory.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mike_yi
 * @description 队列工厂
 * @since 2018-1-17
 */
public class QueueFactory<T> {
	
	private ArrayList<T> list = new ArrayList<T>();
	// 单个入队列
	public void push(T t) {
		list.add(t);
	}
    // 多个入队列
	public void pushs(List<T> tList) {
		list.addAll(tList);
	}
	// 出队列
	public T pop() {
		if(list.size()<1) {
			return null;
		}
		T t = list.get(list.size() - 1);
		list.remove(t);
		return t;
	}
	
	//获取队列的大小
	public int getSize(){
		return list.size();
	}
	
	//判断是否为空
	public boolean isEmpty() {
		if(list.size()>0) {
			return false;
		}
		return true;
	}
}

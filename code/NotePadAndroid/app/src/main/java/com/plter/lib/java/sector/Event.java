package com.plter.lib.java.sector;

import com.plter.lib.java.sector.protocols.IEvent;


/**
 * 事件类
 * @author xtiqin
 *
 */
public class Event extends Message implements IEvent{

	
	/**
	 * 构建一个事件
	 * @param name		事件的名称
	 * @param content	事件的内容
	 * @param level		事件的级别 
	 */
	public Event(String name, Object content, int level) {
		super(name, content, level);
	}

	
	/**
	 * 构建一个事件
	 * @param name		事件的名称
	 * @param content	事件的内容
	 */
	public Event(String name, Object content) {
		super(name, content);
	}

	/**
	 * 构建一个事件
	 * @param name	事件的名称
	 * @param level	事件的级别
	 */
	public Event(String name, int level) {
		super(name, level);
	}

	/**
	 * 构建一个事件
	 * @param name	事件的名称
	 */
	public Event(String name) {
		super(name);
	}

}

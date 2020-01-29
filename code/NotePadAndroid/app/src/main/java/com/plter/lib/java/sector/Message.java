package com.plter.lib.java.sector;

import com.plter.lib.java.sector.protocols.IMessage;

/**
 * 消息类
 * @author xtiqin
 *
 */
public class Message implements IMessage {
	
	/**
	 * 构建一个消息
	 * @param name		消息的名称
	 * @param content	消息的内容
	 * @param level		消息的级别
	 */
	public Message(String name,Object content,int level) {
		this.name=name;
		this.content=content;
		this.level=level;
	}
	
	/**
	 * 构建一个消息
	 * @param name		消息的名称
	 * @param content	消息的内容
	 */
	public Message(String name,Object content) {
		this.name=name;
		this.content=content;
	}
	
	/**
	 * 构建一个消息
	 * @param name	消息的名称
	 * @param level	消息的级别
	 */
	public Message(String name,int level) {
		this.name=name;
		this.level=level;
	}
	
	/**
	 * 构建一个消息
	 * @param name	消息的名称
	 */
	public Message(String name) {
		this.name=name;
	}
	
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public Object getContent() {
		return content;
	}

	@Override
	public int getLevel() {
		return level;
	}

	private String name=null;
	private Object content=null;
	private int level=0;
}

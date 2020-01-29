/**
   Copyright [plter] [xtiqin]
   http://plter.sinaapp.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

   This is a part of PlterAndroidLib on 
   http://plter.sinaapp.com/?cat=14 
	https://github.com/xtiqin/plter-android-lib
 */

package com.plter.lib.java.sector.protocols;

import java.util.List;


/**
 * 应用程序系统的部门
 * @author xtiqin
 */
public interface ISector {
	
	
	/**
	 * 取得部门名称
	 * @return
	 */
	public String getName();
	
	
	/**
	 * 添加子部门
	 * @param section
	 */
	public void addSector(ISector section);
	
	
	/**
	 * 移除子部门
	 * @param section
	 */
	public void removeSector(ISector section);
	
	
	/**
	 * 根据子部门名称移除子部门
	 * @param name
	 */
	public void removeSector(String name);
	
	
	/**
	 * 根据名称取得子部门
	 * @param name
	 * @return
	 */
	public ISector getSector(String name);
	
	
	/**
	 * 取得所有子部门
	 * @return
	 */
	public List<ISector> getSectors();
	
	
	/**
	 * 为当前部门添加一个职能
	 */
	public void addFunction(IFunction function);
	
	
	/**
	 * 从当前的部门中移除一个职能
	 * @param function
	 */
	public void removeFunction(IFunction function);
	
	
	/**
	 * 根据名称移除职能
	 * @param name
	 */
	public void removeFunction(String name);
	
	
	/**
	 * 根据名称取得职能
	 * @param name
	 * @return
	 */
	public IFunction getFunction(String name);
	
	
	/**
	 * 取得当前部门的所有职能 
	 * @return
	 */
	public List<IFunction> getFunctions();
	
	
	/**
	 * 发送消息
	 * @param message 消息，如果消息是命令，则会发给其所对应的职能，如果是事件，则只发送给部门和部门经理，而不发送给职能
	 * @return 如果消息成功发送(所有职能部门都成功执行了消息)，则返回true,否则返回false
	 */
	public boolean sendMessage(IMessage message);
	
	
	/**
	 * 发送命令
	 * @param command	命令
	 * @return	如果命令成功发送(所有职能部门都成功执行了命令)，则返回true,否则返回false
	 */
	public boolean sendCommand(ICommand command);
	public boolean sendCommand(String commandName);
	public boolean sendCommand(String commandName, Object content);
	public boolean sendCommand(String commandName, int level);
	public boolean sendCommand(String commandName, Object content, int level);
	
	
	/**
	 * 派发事件
	 * @param event	事件
	 * @return	如果事件成功派发，返回true，否则返回false
	 */
	public boolean dispatchEvent(IEvent event);
	public boolean dispatchEvent(String eventName);
	public boolean dispatchEvent(String eventName, Object content);
	public boolean dispatchEvent(String eventName, Object content, int level);
	public boolean dispatchEvent(String eventName, int level);
	
	
	
	
	/**
	 * 处理命令，如果该方法返回值为false，则命令不会发送
	 * @param command
	 * @return
	 */
	public boolean handleCommand(ICommand command);
	
	/**
	 * 处理事件，如果该方法返回值为false，则事件不会发送
	 * @param event
	 * @return
	 */
	public boolean handleEvent(IEvent event);
	
	
	/**
	 * 处理消息，如果该方法返回值为false，则消息不会发送
	 * @param message
	 * @return
	 */
	public boolean handleMessage(IMessage message);
	
	
	
	/**
	 * 取得上级部门
	 * @return
	 */
	public ISector getSuperior();
	
	
	/**
	 * 取得顶级部门
	 * @return
	 */
	public ISector getRoot();
	
	/**
	 * 设置部门经理
	 * @param manager
	 */
	public void setManager(IManager manager);
	
	
	/**
	 * 取得部门经理
	 * @return
	 */
	public IManager getManager();
	
}

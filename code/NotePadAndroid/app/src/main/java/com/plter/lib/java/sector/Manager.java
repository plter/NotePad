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

package com.plter.lib.java.sector;

import com.plter.lib.java.sector.protocols.ICommand;
import com.plter.lib.java.sector.protocols.IEvent;
import com.plter.lib.java.sector.protocols.IManager;
import com.plter.lib.java.sector.protocols.IMessage;
import com.plter.lib.java.sector.protocols.ISector;


/**
 * 部门经理
 * @author xtiqin
 *
 */
public class Manager implements IManager {
	
	/**
	 * 创建一个部门经理，并指定该部门经理所管理的目标组件
	 * @param component
	 */
	public Manager(Object component) {
		this.component=component;
	}
	
	/**
	 * 创建一个部门经理，并指定该部门经理所管理的组件及名称
	 * @param component	所管理的组件
	 * @param name		经理的名称
	 */
	public Manager(Object component,String name) {
		this.component=component;
		this.name=name;
	}
	

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ISector getSector() {
		return sector;
	}

	@Override
	public boolean sendMessage(IMessage message) {
		if (getSector()!=null) {
			return getSector().sendMessage(message);
		}
		return false;
	}
	
	@Override
	public boolean sendCommand(ICommand command) {
		return sendMessage(command);
	}


	@Override
	public boolean dispatchEvent(IEvent event) {
		return sendMessage(event);
	}
	
	@Override
	public boolean sendCommand(String commandName) {
		return sendCommand(new Command(commandName));
	}

	@Override
	public boolean sendCommand(String commandName, Object content) {
		return sendCommand(new Command(commandName, content));
	}

	@Override
	public boolean sendCommand(String commandName, int level) {
		return sendCommand(new Command(commandName, level));
	}

	@Override
	public boolean sendCommand(String commandName, Object content, int level) {
		return sendCommand(new Command(commandName, content, level));
	}

	@Override
	public boolean dispatchEvent(String eventName) {
		return dispatchEvent(new Event(eventName));
	}

	@Override
	public boolean dispatchEvent(String eventName, Object content) {
		return dispatchEvent(new Event(eventName, content));
	}

	@Override
	public boolean dispatchEvent(String eventName, Object content, int level) {
		return dispatchEvent(new Event(eventName, content, level));
	}

	@Override
	public boolean dispatchEvent(String eventName, int level) {
		return dispatchEvent(eventName, level);
	}
	
	
	@Override
	public boolean handleMessage(IMessage message) {
		if (message instanceof ICommand) {
			return handleCommand((ICommand) message);
		}
		if (message instanceof IEvent) {
			return handleEvent((IEvent) message);
		}
		return true;
	}
	
	@Override
	public boolean handleCommand(ICommand command) {
		return true;
	}


	@Override
	public boolean handleEvent(IEvent event) {
		return true;
	}
	

	@Override
	public Object getComponent() {
		return component;
	}
	
	
	void setSector(ISector sector){
		this.sector=sector;
	}
	
	
	private Object component=null;
	private ISector sector=null;
	private String name=null;

}

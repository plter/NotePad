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

import java.util.ArrayList;
import java.util.List;

import com.plter.lib.java.sector.protocols.ICommand;
import com.plter.lib.java.sector.protocols.IEvent;
import com.plter.lib.java.sector.protocols.IFunction;
import com.plter.lib.java.sector.protocols.IManager;
import com.plter.lib.java.sector.protocols.IMessage;
import com.plter.lib.java.sector.protocols.ISector;


/**
 * 部门
 * @author xtiqin
 */
public class Sector implements ISector {
	
	/**
	 * 构造一个部门
	 * @param	name 部门的名称
	 */
	public Sector(String name) {
		this.name=name;
	}
	
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void addSector(ISector sector) {
		sectors.add(sector);
		
		((Sector)sector).setSuperior(this);
	}

	@Override
	public void removeSector(ISector sector) {
		sectors.remove(sector);
		
		((Sector)sector).setSuperior(null);
	}

	@Override
	public void removeSector(String name) {
		Sector s=null;
		for (int i = 0; i < sectors.size(); i++) {
			s=(Sector) sectors.get(i);
			if (s.getName().equals(name)) {
				sectors.remove(i);
				s.setSuperior(null);
				break;
			}
		}
	}
	
	
	@Override
	public ISector getSector(String name) {
		ISector s=null;
		for (int i = 0; i < sectors.size(); i++) {
			s=sectors.get(i);
			if (s.getName().equals(name)) {
				return s;
			}
		}
		return null;
	}
	

	@Override
	public List<ISector> getSectors() {
		return sectors;
	}

	@Override
	public void addFunction(IFunction function) {
		functions.add(function);
		((Function)function).setSector(this);
	}

	@Override
	public void removeFunction(IFunction function) {
		functions.remove(function);
		((Function)function).setSector(null);
	}

	@Override
	public void removeFunction(String name) {
		Function f=null;
		for (int i = 0; i < functions.size(); i++) {
			f=(Function) functions.get(i);
			if (f.getName().equals(name)) {
				functions.remove(i);
				f.setSector(null);
				break;
			}
		}
	}
	
	
	@Override
	public IFunction getFunction(String name) {
		IFunction f=null;
		for (int i = 0; i < functions.size(); i++) {
			f=functions.get(i);
			if (f.getName().equals(name)) {
				return f;
			}
		}
		return null;
	}
	

	@Override
	public List<IFunction> getFunctions() {
		return functions;
	}
	
	
	@Override
	public final boolean sendCommand(ICommand command) {
		return sendMessage(command);
	}
	
	@Override
	public final boolean dispatchEvent(IEvent event) {
		return sendMessage(event);
	}

	@Override
	public final boolean sendMessage(IMessage message) {
		
		IFunction f=null;
		
		//如果不存在经理，则部门默认允许的命令都会下发，如果存在经理，则经理有权决定是否要下发命令
		if (handleMessage(message)&&
				getManager()!=null?getManager().handleMessage(message):true) {
			
			boolean suc=true;
			
			//向当前部门发送指令
			if (message instanceof ICommand) {
				for (int i = 0; i < getFunctions().size(); i++) {
					f=getFunctions().get(i);
					if (message.getName().equals(f.getCommandName())) {
						if (!f.execute((ICommand) message)) {
							suc=false;
						}
					}
				}
			}
			
			//向子部门发送指令
			for (int j = 0; j < getSectors().size(); j++) {
				if(!getSectors().get(j).sendMessage(message)){
					suc=false;
				}
			}
			
			return suc;
		}else{
			return false;
		}		
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
	public ISector getSuperior() {
		return superior;
	}
	
	//internal
	void setSuperior(ISector superior) {
		this.superior = superior;
	}
	

	@Override
	public ISector getRoot() {
		if (getSuperior()!=null) {
			return getSuperior().getRoot();
		}
		return this;
	}
	
	
	@Override
	public IManager getManager() {
		return manager;
	}
	
	
	@Override
	public void setManager(IManager manager) {
		if (this.manager!=null) {
			//如果当前已经有经理人，则该经理人的管理权力取消
			((Manager)(this.manager)).setSector(null);
		}
		
		this.manager=manager;
		((Manager)(this.manager)).setSector(this);
	}
	
	/**
	 * 处理命令，如果该方法返回值为false，则命令不会发送
	 * @param command
	 * @return
	 */
	public boolean handleCommand(ICommand command){
		return true;
	}
	
	/**
	 * 处理事件，如果该方法返回值为false，则事件不会发送
	 * @param event
	 * @return
	 */
	public boolean handleEvent(IEvent event){
		return true;
	}
	
	
	/**
	 * 处理消息，如果该方法返回值为false，则消息不会发送
	 * @param message
	 * @return
	 */
	public boolean handleMessage(IMessage message){
		if (message instanceof ICommand) {
			return handleCommand((ICommand) message);
		}
		if (message instanceof IEvent) {
			return handleEvent((IEvent) message);
		}
		return true;
	}

	private IManager manager=null;
	private ISector superior=null;
	private String name=null;
	private final List<ISector> sectors=new ArrayList<ISector>();
	private final List<IFunction> functions=new ArrayList<IFunction>();
}

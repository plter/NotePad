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


/**
 * 部门职能
 * @author xtiqin
 */
public interface IFunction {
	
	
	/**
	 * 取得该职能的名称
	 * @return 职能名称
	 */
	public String getName();
	
	
	/**
	 * 取得职能接受的命令名称
	 * @return	命令名称
	 */
	public String getCommandName();
	
	
	/**
	 * 该职能执行命令
	 * @param command	要执行的命令
	 * @return	如果成功执行命令，返回true，否则返回false
	 */
	public boolean execute(ICommand command);
	
	
	/**
	 * 取得职能所在的部门
	 * @return
	 */
	public ISector getSector();
	
}

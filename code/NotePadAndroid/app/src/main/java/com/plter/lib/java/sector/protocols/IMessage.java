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
 * 消息
 * @author xtiqin
 *
 */
public interface IMessage {

	/**
	 * 取得消息的名称
	 * @return
	 */
	public String getName();
	
	
	/**
	 * 取得消息附加的的内容
	 * @return
	 */
	public Object getContent();
	
	
	/**
	 * 取得消息的级别，部门(Sector)可以在handleCommand方法中根据级别选择性的允许消息通过
	 * @return
	 */
	public int getLevel();
}

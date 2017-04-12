/**
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  
 *  You may obtain a copy of the License at 
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  
 */

package com.globalsight.dispatcher.model;

public class Translate
{
    private int id;
    private int accountId;
    private String accountName;
    private String sourceLocale;
    private String targetLocale;
    private String source;
    private String target;
    private String domain;
    private String mtCategory;
    private int charactersCount;
		
		public int getId () {
			return id;
		}
		
		public void setId (int id) {
			this.id = id;
		}

		public int getAccountId () {
			return accountId;
		}

		public void setAccountId (int accountId) {
			this.accountId = accountId;
		}

		public String getSourceLocale () {
			return sourceLocale;
		}
		
		public void setSourceLocale (String sourceLocale) {
			this.sourceLocale = sourceLocale;
		}
		
		public String getTargetLocale () {
			return targetLocale;
		}
		
		public void setTargetLocale (String targetLocale) {
			this.targetLocale = targetLocale;
		}
		
		public String getSource () {
			return source;
		}
		
		public void setSource (String source) {
			this.source = source;
		}
		
		public String getTarget () {
			return target;
		}
		
		public void setTarget (String target) {
			this.target = target;
		}

		public String getAccountName() {
			return accountName;
		}

		public void setAccountName(String accountName) {
			this.accountName = accountName;
		}

		public String getDomain() {
			return domain;
		}

		public void setDomain(String category) {
			this.domain = category;
		}

		public String getMtCategory() {
			return mtCategory;
		}

		public void setMtCategory(String mtCategory) {
			this.mtCategory = mtCategory;
		}

		public int getCharactersCount() {
			return charactersCount;
		}

		public void setCharactersCount(int charactersCount) {
			this.charactersCount = charactersCount;
		}

}

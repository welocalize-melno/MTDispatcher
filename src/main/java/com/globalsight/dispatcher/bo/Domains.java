/**
 *  Copyright 2014 Welocalize, Inc. 
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
package com.globalsight.dispatcher.bo;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Domains")
public class Domains
{
    private AtomicInteger autoIncrement = new AtomicInteger(0);
    private Set<Domain> domains = new CopyOnWriteArraySet<Domain>();
    
    public Domains()
    {
        super();
    }

    public Set<Domain> getDomains()
    {
        return domains;
    }

    @XmlElement(name = "Domain")
    public void setDomains(Set<Domain> p_domains)
    {
        this.domains = p_domains;
    }

    public int getAutoIncrement()
    {
        return autoIncrement.get();
    }

    @XmlAttribute(name="autoIncrement")
    public void setAutoIncrement(int newValue)
    {
        autoIncrement.set(newValue);
    }
    public int getAndIncrement()
    {
        return autoIncrement.getAndIncrement();
    }

	@Override
	public String toString() {
		return "Domains [autoIncrement=" + autoIncrement + ", domains="
				+ domains + "]";
	}
}

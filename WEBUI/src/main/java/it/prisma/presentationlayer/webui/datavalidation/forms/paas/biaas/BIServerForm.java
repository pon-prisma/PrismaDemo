/*
 * Copyright 2014 PRISMA by MIUR
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.prisma.presentationlayer.webui.datavalidation.forms.paas.biaas;

import it.prisma.utils.validation.RegularExpressionList;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


/**
 * 
 * @author <a href="mailto:m.bassi@reply.it">Matteo Bassi</a>
 * @version 0.1.0
 * @since 0.1.0
 * @see <a
 *      href="http://www.istc.cnr.it/project/prisma-piattaforme-cloud-interoperabili-smart-government">Progetto
 *      PRISMA</a>
 */
public class BIServerForm extends BIForm {

	private static final long serialVersionUID = 1L;

	@Size(min = 4, max = 40)
	@Pattern(regexp = RegularExpressionList.ALPHANUMERIC_PATTERN)
	private String rootPassword;

	@Size(min = 4, max = 40)
	@Pattern(regexp = RegularExpressionList.ALPHANUMERIC_PATTERN)
	private String repeatRootPassword;

	public String getRootPassword() {
		return rootPassword;
	}

	public void setRootPassword(String rootPassword) {
		this.rootPassword = rootPassword;
	}

	public String getRepeatRootPassword() {
		return repeatRootPassword;
	}

	public void setRepeatRootPassword(String repeatRootPassword) {
		this.repeatRootPassword = repeatRootPassword;
	}

	
}

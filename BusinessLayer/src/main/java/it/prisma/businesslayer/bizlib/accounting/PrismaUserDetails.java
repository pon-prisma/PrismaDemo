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

package it.prisma.businesslayer.bizlib.accounting;

import it.prisma.domain.dsl.accounting.users.UserRepresentation;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class PrismaUserDetails extends User {

	private static final long serialVersionUID = -5110365676724009049L;

	private UserRepresentation userData;

	public PrismaUserDetails(String username, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities,
			UserRepresentation userData) {
		super(username, new BigInteger(130, new SecureRandom()).toString(32),
				enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, authorities);
		this.userData = userData;
		
	}

	public UserRepresentation getUserData() {
		return userData;
	}

	public void setUserData(UserRepresentation userData) {
		this.userData = userData;
	}

}

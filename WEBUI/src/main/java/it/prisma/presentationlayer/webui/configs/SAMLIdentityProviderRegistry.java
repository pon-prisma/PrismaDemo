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

package it.prisma.presentationlayer.webui.configs;

import it.prisma.presentationlayer.webui.vos.accounting.SAMLIdentityProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.stereotype.Component;

@Component
public class SAMLIdentityProviderRegistry {


	static final Logger LOG = LogManager.getLogger(SAMLIdentityProviderRegistry.class.getName());


	private String PRISMA_IDP_ENTITY_ID = "https://prisma-idp.cloud.ba.infn.it/simplesaml/saml2/idp/metadata.php";
	
	private final SAMLIdentityProvider prismaIdentityProvider;
	private final SAMLIdentityProvider replyIdentityProvider;
	private final SAMLIdentityProvider infnIdentityProvider;
	private final SAMLIdentityProvider sielteIdentityProvider;
	private final SAMLIdentityProvider uniBaIdentityProvider;

	// Class initializer
	SAMLIdentityProviderRegistry() throws MetadataProviderException {
		prismaIdentityProvider = initPrismaIdP();
		replyIdentityProvider = initReplyIdP();
		infnIdentityProvider = initInfnIdP();
		sielteIdentityProvider = initSielteIdP();
		uniBaIdentityProvider = initUniBaIdP();
	}

	private SAMLIdentityProvider initPrismaIdP() {
		
		return new SAMLIdentityProvider(
				Long.valueOf(2),
				"Prisma",
				PRISMA_IDP_ENTITY_ID,
				"PRISMA by MIUR", true);
	}

	private SAMLIdentityProvider initReplyIdP() {
		return new SAMLIdentityProvider(
				Long.valueOf(3),
				"Reply",
				"http://ststest-replynet.reply.it/adfs/services/trust",
				"Reply is a leading Consulting, Systems Integration and Application Management "
						+ "company, specialising in the creation and implementation of solutions based "
						+ "on new communication networks and digital media.",
				false);
	}

	private SAMLIdentityProvider initInfnIdP() {
		return new SAMLIdentityProvider(Long.valueOf(4), "INFN",
				"https://idp.infn.it/saml2/idp/metadata.php",
				"INFN Bari - Istituto Nazionale di Fisica Nucleare", false);
	}

	private SAMLIdentityProvider initSielteIdP() {
		return new SAMLIdentityProvider(Long.valueOf(5), "Sielte",
				"http://sielte-idp.sielte.it/adfs/services/trust",
				"Sielte - Public Cloud Computing Operator", false);
	}

	private SAMLIdentityProvider initUniBaIdP() {
		return new SAMLIdentityProvider(Long.valueOf(6), "UniBA",
				"https://idpuniba.uniba.it/simplesaml/saml2/idp/metadata.php",
				"Università degli Studi di Bari Aldo Moro", false);
	}

	public final SAMLIdentityProvider getPrismaIdP() {
		return prismaIdentityProvider;
	}

	public final SAMLIdentityProvider getInfnIdP() {
		return infnIdentityProvider;
	}

	public final SAMLIdentityProvider getReplyIdP() {
		return replyIdentityProvider;
	}

	public final SAMLIdentityProvider getSielteIdP() {
		return sielteIdentityProvider;
	}

	public final SAMLIdentityProvider getUniBaIdP() {
		return uniBaIdentityProvider;
	}

	@Component
	public static class SAMLIdentityProviderRegistryProcessor {

		@Autowired
		private SAMLIdentityProviderRegistry samlIdentityProviderRegistry;

		public String getNameId(SAMLCredential credential) {
			if (credential.getRemoteEntityID().compareTo(samlIdentityProviderRegistry.getInfnIdP().getEntityId()) == 0)
				return credential.getAttributeAsString("urn:oid:0.9.2342.19200300.100.1.1");
			else if (credential.getRemoteEntityID().compareTo(samlIdentityProviderRegistry.getUniBaIdP().getEntityId()) == 0)
				return credential.getAttributeAsString("urn:oid:0.9.2342.19200300.100.1.1");
			return credential.getNameID().getValue();
		}

		public String getEmail(SAMLCredential credential) {
			if (credential.getRemoteEntityID().compareTo(
					samlIdentityProviderRegistry.getPrismaIdP().getEntityId()) == 0)
				return credential
						.getAttributeAsString("urn:oid:0.9.2342.19200300.100.1.3");
			else if (credential.getRemoteEntityID().compareTo(
					samlIdentityProviderRegistry.getReplyIdP().getEntityId()) == 0)
				return credential
						.getAttributeAsString("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/emailaddress");
			else if (credential.getRemoteEntityID().compareTo(
					samlIdentityProviderRegistry.getInfnIdP().getEntityId()) == 0)
				return credential
						.getAttributeAsString("urn:oid:0.9.2342.19200300.100.1.3");   
			else if (credential.getRemoteEntityID().compareTo(
					samlIdentityProviderRegistry.getSielteIdP().getEntityId()) == 0)
				return credential
						.getAttributeAsString("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/emailaddress");
			else if (credential.getRemoteEntityID().compareTo(
					samlIdentityProviderRegistry.getUniBaIdP().getEntityId()) == 0)
				return credential
						.getAttributeAsString("urn:oid:0.9.2342.19200300.100.1.3");
			else
				throw new AuthenticationServiceException(
						"Selected IdP not exists.");
		}

		public Long getIdentityProviderId(SAMLCredential credential) {
			if (credential.getRemoteEntityID().compareTo(
					samlIdentityProviderRegistry.getPrismaIdP().getEntityId()) == 0)
				return samlIdentityProviderRegistry.getPrismaIdP().getId();
			else if (credential.getRemoteEntityID().compareTo(
					samlIdentityProviderRegistry.getReplyIdP().getEntityId()) == 0)
				return samlIdentityProviderRegistry.getReplyIdP().getId();
			else if (credential.getRemoteEntityID().compareTo(
					samlIdentityProviderRegistry.getInfnIdP().getEntityId()) == 0)
				return samlIdentityProviderRegistry.getInfnIdP().getId();
			else if (credential.getRemoteEntityID().compareTo(
					samlIdentityProviderRegistry.getSielteIdP().getEntityId()) == 0)
				return samlIdentityProviderRegistry.getSielteIdP().getId();
			else if (credential.getRemoteEntityID().compareTo(
					samlIdentityProviderRegistry.getUniBaIdP().getEntityId()) == 0)
				return samlIdentityProviderRegistry.getUniBaIdP().getId();
			else
				throw new AuthenticationServiceException(
						"Selected IdP not exists.");
		}

		public String getIdentityProviderLabel(SAMLCredential credential) {
			if (credential.getRemoteEntityID().compareTo(
					samlIdentityProviderRegistry.getPrismaIdP().getEntityId()) == 0)
				return samlIdentityProviderRegistry.getPrismaIdP().getLabel();
			else if (credential.getRemoteEntityID().compareTo(
					samlIdentityProviderRegistry.getReplyIdP().getEntityId()) == 0)
				return samlIdentityProviderRegistry.getReplyIdP().getLabel();
			else if (credential.getRemoteEntityID().compareTo(
					samlIdentityProviderRegistry.getInfnIdP().getEntityId()) == 0)
				return samlIdentityProviderRegistry.getInfnIdP().getLabel();
			else if (credential.getRemoteEntityID().compareTo(
					samlIdentityProviderRegistry.getSielteIdP().getEntityId()) == 0)
				return samlIdentityProviderRegistry.getSielteIdP().getLabel();
			else if (credential.getRemoteEntityID().compareTo(
					samlIdentityProviderRegistry.getUniBaIdP().getEntityId()) == 0)
				return samlIdentityProviderRegistry.getUniBaIdP().getLabel();
			else
				throw new AuthenticationServiceException(
						"Selected IdP not exists.");
		}

	}

}

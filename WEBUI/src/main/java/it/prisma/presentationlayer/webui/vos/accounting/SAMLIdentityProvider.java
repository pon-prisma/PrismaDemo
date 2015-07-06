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

package it.prisma.presentationlayer.webui.vos.accounting;

public class SAMLIdentityProvider {

	private final Long id;
	private final String label;
	private final String entityId;
	private final String description;
	private final boolean isDefault;

	public SAMLIdentityProvider(final Long id, final String label,
			final String entityId, final String description,
			final boolean isDefault) {
		this.id = id;
		this.label = label;
		this.entityId = entityId;
		this.description = description;
		this.isDefault = isDefault;
	}

	public final Long getId() {
		return id;
	}

	public final String getLabel() {
		return label;
	}

	public final String getEntityId() {
		return entityId;
	}

	public final String getDescription() {
		return description;
	}

	public final boolean isDefault() {
		return isDefault;
	}

}

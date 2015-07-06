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

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.cache.CacheBuilder;

@Configuration
@EnableCaching
public class CacheConfig{

	
	@Bean
	public CacheManager cacheManager() {
	 
		SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
	 
		GuavaCache cacheIaaSStatus = new GuavaCache("iaasStatus", CacheBuilder.newBuilder().expireAfterWrite(180, TimeUnit.SECONDS).build());
		GuavaCache cacheIaaSStatusV2 = new GuavaCache("iaasStatusV2", CacheBuilder.newBuilder().expireAfterWrite(180, TimeUnit.SECONDS).build());
		GuavaCache cacheDns = new GuavaCache("dns", CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS).build());
		GuavaCache cacheName = new GuavaCache("name", CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS).build());

		simpleCacheManager.setCaches(Arrays.asList(cacheIaaSStatus, cacheIaaSStatusV2, cacheDns, cacheName));
		
		return simpleCacheManager;
	}
		
	
}
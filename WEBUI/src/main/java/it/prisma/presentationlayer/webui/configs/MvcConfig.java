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

import it.prisma.presentationlayer.webui.argumentresolver.RestWSParamsResolver;
import it.prisma.presentationlayer.webui.controllers.commons.PrismaUserDetailsDataInterceptor;
import it.prisma.presentationlayer.webui.security.userdetails.CurrentUserHandlerMethodArgumentResolver;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
@EnableWebMvc
public class MvcConfig extends WebMvcConfigurerAdapter {

	// Cache refresh period
	private static final Integer CACHE_PERIOD = 0;

	@Autowired
	private CurrentUserHandlerMethodArgumentResolver currentUserHandlerMethodArgumentResolver;

	@Autowired
	private RestWSParamsResolver restWSParamsResolver;
	
	@Autowired
	private PrismaUserDetailsDataInterceptor allControllesInterceptor;

	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
		return new EmbeddedServletContainerCustomizer() {
			@Override
			public void customize(ConfigurableEmbeddedServletContainer arg0) {
				
				ErrorPage error400Page = new ErrorPage(HttpStatus.BAD_REQUEST,
						"/400");
				ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED,
						"/401");
				ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND,
						"/404");
				ErrorPage error500Page = new ErrorPage(
						HttpStatus.INTERNAL_SERVER_ERROR, "/500");
				arg0.addErrorPages(error400Page, error401Page, error404Page, error500Page);
			}
		};
	}

	/**
	 * This function is used for apply i18n internationalization. With basenames
	 * is specified the location of languages resources.
	 * 
	 * @return messageSource
	 */
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource
				.setBasenames("classpath:templates/languages_i18n/messages");
		return messageSource;
	}

	/**
	 * In this function is defined the parameter that permit to change languages
	 * from different country This function customize the request parameter that
	 * the interceptor check for call the setParamName method and then add it to
	 * the registry.
	 * 
	 * @return
	 */
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		return localeChangeInterceptor;
	}

	/**
	 * In this function is defined the default local language because locale
	 * settings can be customized The default language used in this case is
	 * italian. LocaleResolver is responsible for determining how to retrieve
	 * the locale and how to set locale.
	 * 
	 * @return result
	 */
	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver result = new SessionLocaleResolver();
		result.setDefaultLocale(Locale.ITALIAN);
		return result;
	}

	@Override
	public void configureDefaultServletHandling(
			DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		super.addResourceHandlers(registry);
		if (!registry.hasMappingForPattern("/static/**")) {
			registry.addResourceHandler("/static/**")
					.addResourceLocations("/static/")
					.setCachePeriod(CACHE_PERIOD);
		}
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(currentUserHandlerMethodArgumentResolver);
		argumentResolvers.add(restWSParamsResolver);
	}

	/***
	 * addInterceptors function need in case user wants a different locale. It
	 * determines how to intercept requests handled in the DispatcherServlet
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor()).excludePathPatterns(
				"/async/**", "/monitoring/**");
		registry.addInterceptor(allControllesInterceptor)
				.addPathPatterns("/dashboard", "/iaas/**", "/paas/**",
						"/terms/**", "/support/**", "/workgroups/**")
				.excludePathPatterns("/async/**", "/monitoring/**");
	}

}
package com.appdeveloperblog.estore.productservice;

import com.appdeveloperblog.estore.productservice.command.interceptor.CreateProductCommandInterceptor;
import com.appdeveloperblog.estore.productservice.config.AxonConfig;
import com.appdeveloperblog.estore.productservice.core.errorhandling.ProductServiceEventsErrorHandling;
import com.thoughtworks.xstream.XStream;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.EventProcessingConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import static com.appdeveloperblog.estore.productservice.core.procgroups.Groups.PRODUCT_GROUP;

@EnableDiscoveryClient
@SpringBootApplication
@Import({ AxonConfig.class })
public class ProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}


	@Autowired
	public void registerCreateProductCommandInterceptor(ApplicationContext context, CommandBus commandBus) {
		commandBus.registerDispatchInterceptor(context.getBean(CreateProductCommandInterceptor.class));
	}

	@Autowired
	public void configEventErrorHandling(EventProcessingConfigurer config) {
		config.registerListenerInvocationErrorHandler(PRODUCT_GROUP,
				conf -> new ProductServiceEventsErrorHandling());

		//if you don't need custom error handling than you can use one provided axon framework. And you can use in the same way
		//config.registerListenerInvocationErrorHandler(PRODUCT_GROUP,
		// 		conf -> PropagatingErrorHandler.instance());
	}

}

package com.appdeveloperblog.estore.productservice.command.interceptor;

import com.appdeveloperblog.estore.productservice.command.CreateProductCommand;
import com.appdeveloperblog.estore.productservice.core.entity.ProductLookupEntity;
import com.appdeveloperblog.estore.productservice.core.repository.ProductLookupRepository;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiFunction;

@Component
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private final ProductLookupRepository productLookupRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateProductCommandInterceptor.class);

    public CreateProductCommandInterceptor(ProductLookupRepository productLookupRepository) {
        this.productLookupRepository = productLookupRepository;
    }


    /**
     * TODO 3 3-th option valid object message dispatcher interceptor
     */
    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(@Nonnull List<? extends CommandMessage<?>> list) {


        return (index, command) -> {
            LOGGER.error("Intercept command: " + command.getPayloadType());

            if (CreateProductCommand.class.equals(command.getPayloadType())) {
                CreateProductCommand cmd = (CreateProductCommand) command.getPayload();

                ProductLookupEntity entity = productLookupRepository.findByProductIdOrTitle(cmd.getProductId(), cmd.getTitle());

                if (entity != null) {
                    throw new IllegalStateException(String.format("Product with productId %s or title %s already exists",
                            cmd.getProductId(), cmd.getTitle()));
                }
            }

            return command;
        };
    }

}

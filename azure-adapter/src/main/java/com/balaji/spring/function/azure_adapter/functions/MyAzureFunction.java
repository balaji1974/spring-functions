package com.balaji.spring.function.azure_adapter.functions;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.balaji.spring.function.azure_adapter.model.Subscriber;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.stereotype.Component;

@Component
public class MyAzureFunction {

    
    /**
     * The FunctionCatalog leverages the Spring Cloud Function framework.
     */
    @Autowired
    private FunctionCatalog functionCatalog;


    @FunctionName("create")
    public void springCloudFunctionCreate(
            @HttpTrigger(name = "req", methods = {
                    HttpMethod.POST }, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            ExecutionContext context) {

    	Consumer<String> create = this.functionCatalog.lookup("create");

        create.accept(request.getBody().get());
    }
    
    @FunctionName("findAll")
    public List<Subscriber> springCloudFunctionFindAll(
            @HttpTrigger(name = "req", methods = {
                    HttpMethod.GET }, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            ExecutionContext context) {

    	Supplier<List<Subscriber>> findAll = this.functionCatalog.lookup("findAll");

        return (List<Subscriber>) findAll.get();
    }
}
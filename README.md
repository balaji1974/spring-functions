# Spring Functions

## First Sample - newsletter-demo
```xml

1. Create a new maven project and add following dependencies to it 

   Spring web:  
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
   Function: 
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-function-web</artifactId>
    </dependency>

2. Create a model class called Subscriber inside the model package

This class will have two private fields id & email

Create getters, setters, constructor (using both fields) and toString method (using both fields)

3. Create a service class called SubscriberService inside the service package

Create the @Service annotation on the class level

Create the following: 
  List<Subscriber> subscribers=new ArrayList<>();
  AtomicInteger id=new AtomicInteger(0);

  //function to get all subscribers 
  public List<Subscriber> findAll() {
    return subscribers;
  }

  // function to create a new subscriber
  public void create(String email) {
    subscribers.add(new Subscriber(id.addAndGet(1), email));
  }
  
4. Create a class called Subscribers inside the function package

Create the @Configuration annotation on the class level

Autowire SubscriberService into this class

Spring cloud function is build on top of 3 functional interfaces in Java8:
Supplier<O>
Function<I, O>
Consumer<I>

Create the following: 
  @Bean
  public Supplier<List<Subscriber>> findAll() {
    return () -> subscriberService.findAll();
  }
  
  @Bean
  public Consumer<String> create() {
    return (email) -> subscriberService.create(email);
  }


5. Run the server and execute the following curl commands

curl 'http://localhost:8080/create' --header 'Content-Type: application/json' --data-raw 'balaji@gmail.com'

curl 'http://localhost:8080/findAll'


6. That's it. We have created our first Cloud Function

```

## Adding Cloud Platform Adapters

### AWS Adapter - aws-adapter
```xml

1. Copy the newletter-demo project and create a new project called aws-adapter 


2. Next add the cloud platform adapter

For AWS
  <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-function-adapter-aws</artifactId>
    </dependency>

We will the deploying our package on AWS but the steps are same on all cloud platforms.


3. Add the maven thin plugin and shade plugin 

In the plug in section comment the following:

        <!--
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
      </plugin>
      -->

Next add the following plugins:

      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
        <dependencies>
          <dependency>
            <groupId>org.springframework.boot.experimental</groupId>
            <artifactId>spring-boot-thin-layout</artifactId>
            <version>1.0.31.RELEASE</version>
          </dependency>
        </dependencies>
      </plugin>
      
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-shade-plugin</artifactId>
        <dependencies>
          <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <version>2.7.4</version>
          </dependency>
        </dependencies>
        <executions>
          <execution>
            <goals>
                 <goal>shade</goal>
            </goals>
            <configuration>
              <createDependencyReducedPom>false</createDependencyReducedPom>
              <shadedArtifactAttached>true</shadedArtifactAttached>
              <shadedClassifierName>aws</shadedClassifierName>
              <transformers>
                <transformer implementation="org.apache.maven.plugins.shade.resource.AppendingTransformer">
                  <resource>META-INF/spring.handlers</resource>
                </transformer>
                <transformer implementation="org.springframework.boot.maven.PropertiesMergingResourceTransformer">
                  <resource>META-INF/spring.factories</resource>
                </transformer>
                <transformer implementation="org.apache.maven.plugins.shade.resource.AppendingTransformer">
                  <resource>META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports</resource>
                </transformer>
                <transformer implementation="org.apache.maven.plugins.shade.resource.AppendingTransformer">
                  <resource>META-INF/spring/org.springframework.boot.actuate.autoconfigure.web.ManagementContextConfiguration.imports</resource>
                </transformer>
                <transformer implementation="org.apache.maven.plugins.shade.resource.AppendingTransformer">
                  <resource>META-INF/spring.schemas</resource>
                </transformer>
                <transformer implementation="org.apache.maven.plugins.shade.resource.AppendingTransformer">
                  <resource>META-INF/spring.components</resource>
                </transformer>
              </transformers>
            </configuration>
          </execution>
        </executions>
      </plugin>


4. Create the jar file: 

Execute the following from the IDE:
mvn cleam
mvn install 

(or)

From the command line run the following: 
mvn clean package 

This will create 2 jar files, the usual standalone .jar file and AWS jar file ending with -aws.jar 
eg. aws-adapter-0.0.1-SNAPSHOT-aws.jar

This package can be deployed on AWS


5. Deploy the package on AWS 
Login into the AWS console 

Go to Lambda screen 

Create function -> Author from scratch -> Function name: mySubscribers -> Runtime: Java 17 -> Architecture: arm64 
-> Create Function 

Once created -> Go to code source section -> Upload from .zip or .jar file 

Next go to Run time settings -> Edit -> Handler : org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest -> Save

Now the Environment is ready for running

6. Function configration settings

Since we have multiple functions wrapped into a single jar, we have to instruct AWS how to invoke them. 
Go to Configuration tab -> Function URL -> Auth : None (this is just for testing purpose and not for production use case, where you will use IAM) 
-> Additional Settings -> Configure Cors : (tick) -> Allow Origin: * (do not do this in production) -> Save

Next copy the URL of the function 

7. Invoke the function 
Go to postman -> 

Method: POST 
URL: <url that was copied>
Header: spring.cloud.function.definition: create (Since we have mulitple functions in our jar we need to make sure which function to call)
Body: raw : balaji@gmail.com
-> Send
This will create a record using our lambda function


Method: GET 
URL: <url that was copied>
Header: spring.cloud.function.definition: findAll (Since we have mulitple functions in our jar we need to make sure which function to call)
-> Send
This will fetch all the records using our lambda function


8. Monitoring the log
Go to Cloud Watch -> Logs Group -> (Select our function) -> (click on the appropriate log stream)


9. We have now run our Spring Cloud Function using AWS Adapter 

```


### Google Adapter - gcp-adapter

### [At present GCP has a bug which is not solved as of date of uploading this example and hence Spring boot 3.1.X only works and any version above 3.2.x does not work]
Ref link: https://github.com/spring-cloud/spring-cloud-function/issues/1085

```xml

1. Copy the newletter-demo project and create a new project called gcp-adapter 

2. Downgrade Spring boot to 3.1.x version

<parent>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-parent</artifactId>
  <version>3.1.10</version>
  <relativePath/> 
</parent>

3. Next add the cloud platform adapter

For GCP
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-function-adapter-gcp</artifactId>
    </dependency>

We will the deploying our package on GCP using the same steps as before (but with lesser version  dependency.

4. Add the following plugin 

In the plug in section comment the following:

        <!--
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
      </plugin>
      -->

Next add the following plugins:

  <plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
      <outputDirectory>target/deploy</outputDirectory>
    </configuration>
    <dependencies>
      <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-function-adapter-gcp</artifactId>
        <version>4.1.1</version> <!-- Lesser version than AWS -->
      </dependency>
    </dependencies>
  </plugin>
  <plugin>
    <groupId>com.google.cloud.functions</groupId>
    <artifactId>function-maven-plugin</artifactId>
    <version>0.9.1</version> <!-- Lesser version than AWS -->
    <configuration>
      <functionTarget>org.springframework.cloud.function.adapter.gcp.GcfJarLauncher</functionTarget>
      <port>8080</port>
    </configuration>
  </plugin>


5. Create the jar file: 

Execute the following from the IDE:
mvn cleam
mvn install 

(or)

From the command line run the following: 
mvn clean package 

This will create 2 jar files, the usual standalone .jar file and AWS jar file ending with -aws.jar 
eg. google-adapter-0.0.1-SNAPSHOT.jar

This package can be deployed on GCP


6. Install gcloud -> as GCP web console deploy still not supporting jar file 
For installation follow the steps from the below link, if already not installed:
https://cloud.google.com/sdk/docs/downloads-interactive


7. Deploy the package on GCP 
Login into gcloud and run the following command

gcloud functions deploy <function-name> --entry-point org.springframework.cloud.function.adapter.gcp.GcfJarLauncher --runtime <java-runtime> --trigger-http --source target/deploy --memory <memory-size>
gcloud functions deploy function-spring-cloud-gcp --entry-point org.springframework.cloud.function.adapter.gcp.GcfJarLauncher --runtime java17 --trigger-http --source target/deploy --memory 512MB


It will ask if anynamous login is allowed (y/N) -> y (press)

Now the function is deployed and running


8. Invoke the function 
Go to postman -> 

Method: POST 
URL: <url that was copied>
Header: spring.cloud.function.definition: create (Since we have mulitple functions in our jar we need to make sure which function to call)
Body: raw : balaji@gmail.com
-> Send
This will create a record using our cloud run function


Method: GET 
URL: <url that was copied>
Header: spring.cloud.function.definition: findAll (Since we have mulitple functions in our jar we need to make sure which function to call)
-> Send
This will fetch all the records using our cloud run function


9. Monitoring the log
Go to GCP Console -> Search and select 'cloud run function' -> Click on our function (eg.function-spring-cloud-gcp) -> Click the logs tab 


10. We have now run our Spring Cloud Function using GCP Adapter 

```


### Azure Adapter - azure-adapter
```xml

1. Note: Make sure the correct correct JDK path is set in the JAVA_HOME env variable 

2. Copy the newletter-demo project and create a new project called azure-adapter 


3. Next add the cloud platform adapter

For Azure
  <dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-function-adapter-azure</artifactId>
  </dependency>

We will the deploying our package on Azure cloud platforms.


4. Add the following plugin 

In the plug in section comment the following:

        <!--
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
      </plugin>
      -->

Next add the following plugins:

      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-deploy-plugin</artifactId>
        <configuration>
          <skip>true</skip>
        </configuration>
      </plugin>
      <plugin>
        <groupId>com.microsoft.azure</groupId>
        <artifactId>azure-functions-maven-plugin</artifactId>
        <version>${azure.functions.maven.plugin.version}</version>

        <configuration>
          <appName>${functionAppName}</appName>
          <resourceGroup>${functionResourceGroup}</resourceGroup>
          <region>${functionAppRegion}</region>
          <appServicePlanName>${functionAppServicePlanName}</appServicePlanName>
          <pricingTier>${functionPricingTier}</pricingTier>

          <hostJson>${project.basedir}/src/main/resources/host.json</hostJson>
          <localSettingsJson>${project.basedir}/src/main/resources/local.settings.json</localSettingsJson>


          <runtime>
            <os>linux</os>
            <javaVersion>17</javaVersion>
          </runtime>

          <funcPort>7072</funcPort>

          <appSettings>
            <property>
              <name>FUNCTIONS_EXTENSION_VERSION</name>
              <value>~4</value>
            </property>
          </appSettings>
        </configuration>
        <executions>
          <execution>
            <id>package-functions</id>
            <goals>
              <goal>package</goal>
            </goals>
          </execution>
        </executions>
      </plugin>

      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
        <dependencies>
          <dependency>
            <groupId>org.springframework.boot.experimental</groupId>
            <artifactId>spring-boot-thin-layout</artifactId>
            <version>${spring-boot-thin-layout.version}</version>
          </dependency>
        </dependencies>
      </plugin>


5. Add the following properties 
    <java.version>17</java.version>
    <spring-boot-thin-layout.version>1.0.28.RELEASE</spring-boot-thin-layout.version>

    <!-- Spring Boot start class! WARING: correct class must be set! -->
    <start-class>com.balaji.spring.function.azure_adapter.AzureAdapterApplication</start-class>

    <!-- AZURE FUNCTION CONFIG ---- WARING: set your correct  Azure function app settings! -->

    <azure.functions.maven.plugin.version>1.28.0</azure.functions.maven.plugin.version>
    <functionAppName>function-spring-cloud-azure</functionAppName>
    <functionAppRegion>westus</functionAppRegion>
    <functionResourceGroup>java-function-group</functionResourceGroup>
    <functionAppServicePlanName>java-function-app-service-plan</functionAppServicePlanName>
    <functionPricingTier>Consumption</functionPricingTier>

The values for the above setting can also be referenced from the below link:
https://github.com/microsoft/azure-maven-plugins/wiki/Azure-Functions


6. Add the class MyAzureFunction and check this code. 
This function acts as the azure function exposed API translating it to the internal functions of the spring boot world


7. Create configuation json files: 
host.json
and 
local.settings.json


8. Create the jar file: 

Execute the following from the IDE:
mvn cleam
mvn install 

(or)

From the command line run the following: 
mvn clean package 

This will create 1 jar file, the usual standalone .jar file 
eg. azure-adapter-0.0.1-SNAPSHOT-aws.jar

This package can be deployed on Azure


9. Install azure core tools to test the function locally 

Make sure Homebrew is installed on MAC OS 
(for other OS please follow this link 
https://learn.microsoft.com/en-us/azure/azure-functions/functions-run-local?tabs=macos%2Cisolated-process%2Cnode-v4%2Cpython-v2%2Chttp-trigger%2Ccontainer-apps&pivots=programming-language-java
)

brew tap azure/functions
brew install azure-functions-core-tools@4
# if upgrading on a machine that has 2.x or 3.x installed:
brew link --overwrite azure-functions-core-tools@4

10. Run locally and test the function with the following command using core tools:  
mvn azure-functions:run     

11. Invoke the function locally and test with POSTMAN

Go to postman -> 

Method: POST 
URL: http://localhost:7072/api/create
Body: raw : balaji@gmail.com
-> Send
This will create a record using our azure function


Method: GET 
URL: http://localhost:7072/api/findAll
-> Send
This will fetch all the records using our azure function


12. Next Deploy the function on to Azure Cloud Environment
Login into the Azure console from the command promt by running the  command
az login 


13. Deploy the function app to azure 
mvn azure-functions:run 


14. Open the function app for public access 
After successfull installation, go to Azure Web Console -> Function App -> <Select the installed function> -> 
Settings -> Networking -> Public Access Network: Enabled with no access restrictions  (click) -> Enable from all networks -> Save


15. Next copy the function app URL from the overview tab

16. Invoke the function 
Go to postman -> 

Method: POST 
URL: <url that was copied>/api/create 
Body: raw : balaji@gmail.com
-> Send
This will create a record using our lambda function


Method: GET 
URL: <url that was copied>/api/get
-> Send
This will fetch all the records using our lambda function


17. We have now run our Spring Cloud Function using Azure Adapter 

```


### References:
https://www.udemy.com/course/devops-with-docker-kubernetes-and-azure-devops
https://docs.spring.io/spring-cloud-function/reference/spring-cloud-function/serverless-platform-adapters.html
https://github.com/microsoft/azure-maven-plugins/wiki/Azure-Functions
https://github.com/microsoft/azure-maven-plugins/wiki/Common-Configuration
https://learn.microsoft.com/en-us/azure/azure-functions/functions-run-local

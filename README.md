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

Create the following: 
  @Bean
  public Supplier<List<Subscriber>> findAll() {
    return () -> subscriberService.findAll();
  }
  
  @Bean
  public Consumer<String> create() {
    return (email) -> subscriberService.create(email);
  }


5. This standard package can be deployed on any Cloud autoscaller (Azure, GCP, AWS)



```


### References:
https://www.udemy.com/course/devops-with-docker-kubernetes-and-azure-devops

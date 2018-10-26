# This project act as plug and play code base for general java most used utils for backend code development.


* To build jar or executables : 
```
mvn clean install
```


#Steps to create maven project from commandline.

```
mvn archetype:generate -DgroupId=com.herle.hibernate -DartifactId=Hibernatelearning -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
```

Where

* groupId : Refers to project packaging and identifies your project uniquely across all projects. (com.herle.hibernate)
* artifactId : Name of jar of your project without version. (Hibernatelearning)
* version : Version number of your choice.
* archetypeArtifactId : Template type.Several templates are available to choose from.
* interactiveMode : If set to true, maven will ask confirmation on each step of project generation.

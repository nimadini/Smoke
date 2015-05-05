# Smoke
An Automated Test Suite Selection Framework

This maven project consists of two modules, which are: agent and other.
The agent module is our framework. The other module is a sample client code 
that uses our framework. 

To run, first go to the resources directory within the agent project to set 
the concrete path for the Smoke's MS Excel report. Then run the mvn package
lifecycle on the agent plugin.

Next run the mvn test/package on the other (user's) project to see how our tool
works. The smoke code is well documented using Java doc.

Here is how I learned working with Javassist. The credit goes to: https://github.com/tomsquest/java-agent-asm-javassist-sample

The credit for using the MS Excel exporter goes to here: http://www.vogella.com/tutorials/JavaExcel/article.html

Note: We have tested our approach on OSX Yosemite using Java 7.

Here is the project repository: https://github.com/nimadini/Smoke.git

Enjoy!

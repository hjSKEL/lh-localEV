# LOCAL-CSMS 외부 의존성 라이브러리 목록

> 갱신일: 2026-04-01
> 목적: 다른 프로젝트에서 버전 업그레이드 시 참고
> 추출 방식: `mvn dependency:list` 기반, 내부 모듈(`kr.co.kevit`) 제외
> Java: 17 / Spring Boot: 2.7.18

---

## 1. Spring Framework

| GroupId | ArtifactId | Version | Scope |
|---------|-----------|---------|-------|
| org.springframework.boot | spring-boot | 2.7.18 | compile |
| org.springframework.boot | spring-boot-autoconfigure | 2.7.18 | compile |
| org.springframework.boot | spring-boot-starter | 2.7.18 | compile |
| org.springframework.boot | spring-boot-starter-web | 2.7.18 | compile |
| org.springframework.boot | spring-boot-starter-websocket | 2.7.18 | compile |
| org.springframework.boot | spring-boot-starter-security | 2.7.18 | compile |
| org.springframework.boot | spring-boot-starter-thymeleaf | 2.7.18 | compile |
| org.springframework.boot | spring-boot-starter-aop | 2.7.18 | compile |
| org.springframework.boot | spring-boot-starter-jdbc | 2.7.18 | compile |
| org.springframework.boot | spring-boot-starter-json | 2.7.18 | compile |
| org.springframework.boot | spring-boot-starter-logging | 2.7.18 | compile |
| org.springframework.boot | spring-boot-starter-tomcat | 2.7.18 | compile |
| org.springframework | spring-core | 5.3.39 | compile |
| org.springframework | spring-beans | 5.3.39 | compile |
| org.springframework | spring-context | 5.3.39 | compile |
| org.springframework | spring-context-support | 5.3.39 | compile |
| org.springframework | spring-aop | 5.3.39 | compile |
| org.springframework | spring-web | 5.3.39 | compile |
| org.springframework | spring-webmvc | 5.3.39 | compile |
| org.springframework | spring-websocket | 5.3.39 | compile |
| org.springframework | spring-jdbc | 5.3.39 | compile |
| org.springframework | spring-tx | 5.3.39 | compile |
| org.springframework | spring-messaging | 5.3.39 | compile |
| org.springframework | spring-expression | 5.3.39 | compile |
| org.springframework | spring-jcl | 5.3.39 | compile |
| org.springframework | spring-test | 5.3.39 | compile/test |

## 2. Spring Security

| GroupId | ArtifactId | Version | Scope |
|---------|-----------|---------|-------|
| org.springframework.security | spring-security-core | 5.7.11 | compile |
| org.springframework.security | spring-security-config | 5.7.11 | compile |
| org.springframework.security | spring-security-web | 5.7.11 | compile |
| org.springframework.security | spring-security-crypto | 5.7.11 | compile |

## 3. Database / ORM

| GroupId | ArtifactId | Version | Scope |
|---------|-----------|---------|-------|
| com.mysql | mysql-connector-j | 9.2.0 | compile |
| org.mariadb.jdbc | mariadb-java-client | 2.2.3 | compile |
| org.mybatis.spring.boot | mybatis-spring-boot-starter | 2.3.1 | compile |
| org.mybatis.spring.boot | mybatis-spring-boot-autoconfigure | 2.3.1 | compile |
| org.mybatis | mybatis | 3.5.16 | compile |
| org.mybatis | mybatis-spring | 2.1.1 | compile |
| com.zaxxer | HikariCP | 4.0.3 | compile |
| commons-dbcp | commons-dbcp | 1.4 | compile |
| commons-pool | commons-pool | 1.5.4 | compile |
| com.google.protobuf | protobuf-java | 4.29.0 | compile |

## 4. Jackson (JSON)

| GroupId | ArtifactId | Version | Scope |
|---------|-----------|---------|-------|
| com.fasterxml.jackson.core | jackson-core | 2.13.5 | compile |
| com.fasterxml.jackson.core | jackson-databind | 2.13.5 | compile |
| com.fasterxml.jackson.core | jackson-annotations | 2.13.5 | compile |
| com.fasterxml.jackson.datatype | jackson-datatype-jdk8 | 2.13.5 | compile |
| com.fasterxml.jackson.datatype | jackson-datatype-jsr310 | 2.13.5 | compile |
| com.fasterxml.jackson.module | jackson-module-parameter-names | 2.13.5 | compile |

## 5. Logging

| GroupId | ArtifactId | Version | Scope |
|---------|-----------|---------|-------|
| org.slf4j | slf4j-api | 1.7.25 | compile |
| org.slf4j | jul-to-slf4j | 1.7.36 | compile |
| ch.qos.logback | logback-classic | 1.2.9 | runtime |
| ch.qos.logback | logback-core | 1.2.12 | runtime |
| org.apache.logging.log4j | log4j-core | 2.17.2 | compile |
| org.apache.logging.log4j | log4j-api | 2.17.2 | compile |
| org.apache.logging.log4j | log4j-to-slf4j | 2.17.2 | compile |
| org.lazyluke | log4jdbc-remix | 0.2.7 | compile |

## 6. Thymeleaf

| GroupId | ArtifactId | Version | Scope |
|---------|-----------|---------|-------|
| org.thymeleaf | thymeleaf | 3.0.15.RELEASE | compile |
| org.thymeleaf | thymeleaf-spring5 | 3.0.15.RELEASE | compile |
| org.thymeleaf.extras | thymeleaf-extras-springsecurity5 | 3.0.5.RELEASE | compile |
| org.thymeleaf.extras | thymeleaf-extras-java8time | 3.0.4.RELEASE | compile |
| nz.net.ultraq.thymeleaf | thymeleaf-layout-dialect | 3.0.0 | compile |
| nz.net.ultraq.thymeleaf | thymeleaf-expression-processor | 3.0.0 | runtime |
| nz.net.ultraq.extensions | groovy-extensions | 1.1.0 | runtime |
| org.attoparser | attoparser | 2.0.5.RELEASE | compile |
| org.unbescape | unbescape | 1.1.6.RELEASE | compile |

## 7. Apache Camel

| GroupId | ArtifactId | Version | Scope |
|---------|-----------|---------|-------|
| org.apache.camel | camel-core | 3.14.10 | compile |
| org.apache.camel | camel-spring | 3.14.10 | compile |
| org.apache.camel | camel-spring-xml | 3.14.10 | compile |
| org.apache.camel | camel-jetty | 3.14.10 | compile |
| org.apache.camel | camel-xml-jaxb | 3.14.10 | compile |
| org.apache.camel | (기타 camel-* 30+ 모듈) | 3.14.10 | compile |

## 8. HTTP Client / Embedded Server

| GroupId | ArtifactId | Version | Scope |
|---------|-----------|---------|-------|
| org.apache.tomcat.embed | tomcat-embed-core | 9.0.83 | compile |
| org.apache.tomcat.embed | tomcat-embed-el | 9.0.83 | compile |
| org.apache.tomcat.embed | tomcat-embed-websocket | 9.0.83 | compile |
| org.eclipse.jetty | jetty-server | 9.4.51.v20230217 | compile |
| org.eclipse.jetty | jetty-servlet | 9.4.51.v20230217 | compile |
| org.eclipse.jetty | (기타 jetty-* 9개 모듈) | 9.4.51.v20230217 | compile |
| org.apache.httpcomponents | httpclient | 4.5.14 | compile |
| org.apache.httpcomponents | httpcore | 4.4.16 | compile |
| org.apache.httpcomponents | httpasyncclient | 4.1.5 | compile |
| org.apache.httpcomponents | httpcore-nio | 4.4.16 | compile |
| org.apache.httpcomponents | httpmime | 4.5.14 | compile |
| com.mashape.unirest | unirest-java | 1.4.9 | compile |

## 9. Security / Crypto

| GroupId | ArtifactId | Version | Scope |
|---------|-----------|---------|-------|
| org.bouncycastle | bcprov-jdk15on | 1.69 | compile |
| org.bouncycastle | bcpkix-jdk15on | 1.69 | compile |
| org.bouncycastle | bcutil-jdk15on | 1.69 | compile |

## 10. Excel / POI

| GroupId | ArtifactId | Version | Scope |
|---------|-----------|---------|-------|
| org.apache.poi | poi | 3.14 | compile |
| org.apache.poi | poi-ooxml | 3.14 | compile |
| org.apache.poi | poi-ooxml-schemas | 3.14 | compile |
| org.apache.poi | poi-contrib | 3.7-beta3 | compile |
| org.apache.xmlbeans | xmlbeans | 2.6.0 | compile |
| net.sf.jxls | jxls-core | 1.0.5 | compile |
| com.github.virtuald | curvesapi | 1.03 | compile |

## 11. XML / JAXB

| GroupId | ArtifactId | Version | Scope |
|---------|-----------|---------|-------|
| javax.xml.bind | jaxb-api | 2.3.1 | compile |
| com.sun.xml.bind | jaxb-core | 2.2.11 | compile |
| com.sun.xml.bind | jaxb-impl | 2.2.11 | compile |
| jakarta.xml.bind | jakarta.xml.bind-api | 2.3.3 | compile |
| org.glassfish.jaxb | jaxb-runtime | 2.3.8 | compile |
| org.glassfish.jaxb | txw2 | 2.3.8 | compile |
| xerces | xercesImpl | 2.12.0 | compile |
| xml-apis | xml-apis | 1.4.01 | compile |
| stax | stax-api | 1.0.1 | compile |

## 12. EXI (V2G / ISO 15118)

| GroupId | ArtifactId | Version | Scope |
|---------|-----------|---------|-------|
| com.siemens.ct.exi | exificient | 1.0.4 | compile |
| com.siemens.ct.exi | exificient-core | 1.0.4 | compile |
| com.siemens.ct.exi | exificient-grammars | 1.0.4 | compile |
| xmlpull | xmlpull | 1.1.3.1 | compile |

## 13. Utility / Commons

| GroupId | ArtifactId | Version | Scope |
|---------|-----------|---------|-------|
| org.apache.commons | commons-lang3 | 3.12.0 | compile |
| org.apache.commons | commons-jexl | 2.1.1 | compile |
| commons-beanutils | commons-beanutils | 1.9.2 | compile |
| commons-codec | commons-codec | 1.15 | compile |
| commons-collections | commons-collections | 3.2.2 | compile |
| commons-digester | commons-digester | 2.0 | compile |
| commons-fileupload | commons-fileupload | 1.5 | compile |
| commons-io | commons-io | 2.6 | compile |
| commons-logging | commons-logging | 1.2 | compile |
| com.google.code.gson | gson | 2.9.1 | compile |
| org.json | json | 20160212 | compile |
| org.jsoup | jsoup | 1.11.3 | compile |
| net.sf.jagg | jagg-core | 0.9.0 | compile |
| org.projectlombok | lombok | 1.18.30 | compile |
| org.yaml | snakeyaml | 1.30 | compile |
| ognl | ognl | 3.1.26 | compile |

## 14. AOP / Bytecode

| GroupId | ArtifactId | Version | Scope |
|---------|-----------|---------|-------|
| org.aspectj | aspectjweaver | 1.9.21 | compile |
| org.javassist | javassist | 3.20.0-GA | compile |
| org.codehaus.groovy | groovy | 3.0.19 | compile |

## 15. Servlet / Activation

| GroupId | ArtifactId | Version | Scope |
|---------|-----------|---------|-------|
| javax.servlet | javax.servlet-api | 3.1.0 | compile |
| javax.activation | javax.activation-api | 1.2.0 | compile |
| javax.activation | activation | 1.1.1 | compile |
| com.sun.activation | javax.activation | 1.2.0 | compile |
| com.sun.activation | jakarta.activation | 1.2.2 | runtime |
| jakarta.annotation | jakarta.annotation-api | 1.3.5 | compile |
| com.sun.istack | istack-commons-runtime | 3.0.12 | compile |

## 16. Test

| GroupId | ArtifactId | Version | Scope |
|---------|-----------|---------|-------|
| junit | junit | 4.12 | compile |
| org.junit.jupiter | junit-jupiter | 5.8.2 | test |
| org.junit.jupiter | junit-jupiter-api | 5.8.2 | test |
| org.junit.jupiter | junit-jupiter-engine | 5.8.2 | test |
| org.junit.jupiter | junit-jupiter-params | 5.8.2 | test |
| org.junit.platform | junit-platform-commons | 1.8.2 | test |
| org.junit.platform | junit-platform-engine | 1.8.2 | test |
| org.hamcrest | hamcrest | 2.2 | compile |
| org.hamcrest | hamcrest-core | 2.2 | compile |
| org.dbunit | dbunit | 2.5.4 | compile |
| com.github.springtestdbunit | spring-test-dbunit | 1.3.0 | compile |
| org.easymock | easymock | 3.5.1 | compile |
| org.objenesis | objenesis | 2.6 | compile |
| org.awaitility | awaitility | 4.1.1 | compile |
| org.mockito | mockito-core | 4.5.1 | test |
| org.mockito | mockito-junit-jupiter | 4.5.1 | test |
| org.assertj | assertj-core | 3.22.0 | test |
| org.springframework.boot | spring-boot-starter-test | 2.7.18 | test |
| org.springframework.boot | spring-boot-test | 2.7.18 | test |
| org.springframework.boot | spring-boot-test-autoconfigure | 2.7.18 | test |

---

## 버전 통일 이력 (2026-04-01)

Root POM `<dependencyManagement>` 및 개별 pom.xml 수정으로 전이 의존성 버전 통일 완료.

| 라이브러리 | 변경 전 | 변경 후 | 비고 |
|-----------|---------|---------|------|
| Spring Framework 전체 | 5.3.27 / 5.3.31 / 5.3.39 혼재 | **5.3.39** 통일 | root dependencyManagement |
| commons-lang3 | 3.7 | **3.12.0** | common-util + dependencyManagement |
| commons-codec | 1.10 | **1.15** | common-util + dependencyManagement |
| commons-beanutils | 1.9.2 | **1.9.4** | dependencyManagement |
| commons-collections | 3.2.1 | **3.2.2** | admin-web + dependencyManagement |
| commons-logging | 1.1 / 1.2 혼재 | **1.2** | dependencyManagement |
| mybatis | 3.4.1 / 3.5.13 | **3.5.16** | common-util + dependencyManagement |
| mybatis-spring | 1.3.1 | **2.1.1** | common-testcase |
| aspectjweaver | 1.7.4 / 1.9.7 | **1.9.21** | common-testcase + dependencyManagement |
| gson | 2.8.2 | **2.9.1** | common-util, eai-adr + dependencyManagement |
| httpclient | 4.5.2 | **4.5.14** | eai-adr, ocpp16/20-daemon |
| log4j-core / log4j-api | 2.17.0 | **2.17.2** | root pom |
| logback-core | 1.2.9 / 1.2.12 혼재 | **1.2.12** | dependencyManagement |
| mysql-connector-java | 5.1.6 | **mysql-connector-j 9.2.0** | common-testcase |

## 남은 test scope 전이 의존성 차이 (무시 가능)

| 라이브러리 | 버전 | 원인 |
|-----------|------|------|
| hamcrest | 2.1 / 2.2 | awaitility(2.1) vs Boot BOM(2.2) |
| hamcrest-core | 1.3 / 2.2 | junit4(1.3) vs Boot BOM(2.2) |
| objenesis | 2.6 / 3.2 | easymock(2.6) vs mockito(3.2) |

> test scope만 해당하므로 런타임 영향 없음.

## Root POM 프로퍼티

```xml
<spring.version>5.3.39</spring.version>
<spring.security.version>5.8.14</spring.security.version>
<java.version>17</java.version>
<camel.version>3.14.10</camel.version>
```

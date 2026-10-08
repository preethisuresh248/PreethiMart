package com.preethi.preethimart;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

public class PreethiMartServer {

    public static void main(String[] args) throws Exception {
        int port = 8080;
        String portEnv = System.getenv("PORT");
        if (portEnv != null && !portEnv.trim().isEmpty()) {
            try {
                port = Integer.parseInt(portEnv.trim());
            } catch (NumberFormatException ignored) {}
        }

        File baseDir = new File("target/tomcat-embed");
        baseDir.mkdirs();

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.setBaseDir(baseDir.getAbsolutePath());
        tomcat.getConnector(); // Initialize default HTTP connector

        File webappDir = new File("src/main/webapp");
        StandardContext ctx = (StandardContext) tomcat.addWebapp("", webappDir.getAbsolutePath());
        ctx.setParentClassLoader(PreethiMartServer.class.getClassLoader());

        File additionWebInfClasses = new File("target/classes");
        if (additionWebInfClasses.exists()) {
            WebResourceRoot resources = new StandardRoot(ctx);
            resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                    additionWebInfClasses.getAbsolutePath(), "/"));
            ctx.setResources(resources);
        }

        System.out.println("==========================================================");
        System.out.println("   PreethiMart Web Application Started Successfully");
        System.out.println("   Live URL: http://localhost:" + port + "/");
        System.out.println("   Health:   http://localhost:" + port + "/api/v1/health");
        System.out.println("   Catalog:  http://localhost:" + port + "/products");
        System.out.println("==========================================================");

        tomcat.start();
        tomcat.getServer().await();
    }
}

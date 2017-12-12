package com.herle.java.utils;

import java.io.FileInputStream;
import java.io.InputStream;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

public class CamelUtils {

	private static void camelCopy() throws Exception {
		CamelContext context = new DefaultCamelContext();
		context.addRoutes(new RouteBuilder() {
			public void configure() {
				from("file:d://temp/in?noop=true").process(new Processor() {
					public void process(Exchange exchange) throws Exception {
						System.out.println("Within Interim Processor ");
						System.out.println(
								"We just processed the file named: " + exchange.getIn().getHeader("CamelFileName"));
					}
				}).to("file:d://temp/out");
			}
		});
		System.out.println("Context starts here..");
		context.start();
		Thread.sleep(10000 * 60);
		context.stop();
		System.out.println("Context ends here..");
	}

	private static void FileToMsg() throws Exception {

		CamelContext context = new DefaultCamelContext();
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");
		context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() {
				// load files into the JMS queue
				from("file:d://temp/in?noop=true").to("jms:incomingOrders");

				// content-based router
				from("jms:incomingOrders").choice().when(header("CamelFileName").endsWith(".xml")).to("jms:xmlOrders")
						.when(header("CamelFileName").regex("^.*(csv|csl)$")).to("jms:csvOrders").otherwise()
						.to("jms:badOrders").stop().end().to("jms:continuedProcessing");

				from("jms:xmlOrders").process(new Processor() {
					public void process(Exchange exchange) throws Exception {
						System.out.println("Received XML order: " + exchange.getIn().getHeader("CamelFileName"));
					}
				});

				from("jms:csvOrders").process(new Processor() {
					public void process(Exchange exchange) throws Exception {
						System.out.println("Received CSV order: " + exchange.getIn().getHeader("CamelFileName"));
					}
				});
				from("jms:badOrders").process(new Processor() {
					public void process(Exchange exchange) throws Exception {
						System.out.println("Received bad order: " + exchange.getIn().getHeader("CamelFileName"));
					}
				});

				// test that our route is working
				from("jms:continuedProcessing").process(new Processor() {
					public void process(Exchange exchange) throws Exception {
						System.out.println("Received continued order: " + exchange.getIn().getHeader("CamelFileName"));
					}
				});
			}
		});
		System.out.println("Content Based Route starts here..");
		context.start();
		Thread.sleep(10000 * 60);
		System.out.println("Content Based Route ends here..");
		context.stop();
	}

	private static void xmlXpathFilter() throws Exception {

		/***
		 * use follwoing xml.
		 * 
		 * <?xml version="1.0" encoding="UTF-8"?> <orders>
		 * <order product="electronics"> <items> <item>Laptop</item>
		 * <item>Mobile</item> </items> </order> <order product="books"> <items>
		 * <item>Design Patterns</item> <item>XML</item> </items> </order>
		 * </orders>
		 * 
		 */

		CamelContext context = new DefaultCamelContext();

		try {

			context.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					from("direct:JavaDSLRouteStart").

					// To filter routing message to the external file
					split(xpath("//order[@product='electronics']/items")).to("file:src/main/resources/orderxmlroute/")
							.to("stream:out");

				}
			});
			context.start();
			ProducerTemplate template = context.createProducerTemplate();
			InputStream orderxml = new FileInputStream("src/main/resources/order.xml");
			template.sendBody("direct:JavaDSLRouteStart", orderxml);
		} finally {
			context.stop();
		}

	}

	private static void activeMQProducer() throws Exception {

		CamelContext context = new DefaultCamelContext();
		try {
			context.addComponent("activemq",
					ActiveMQComponent.activeMQComponent("vm://localhost?broker.persistent=false"));
			context.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					from("timer://myTimer?period=2000").setBody()
							.simple("Hello World Camel fired at ${header.firedTime}").to("activemq:test.queue");

					// from("activemq:queue:test.queue").to("stream:out");
				}
			});
			context.start();

			ProducerTemplate template = context.createProducerTemplate();
			template.sendBody("activemq:test.queue", "Hello World 1");

			ConsumerTemplate consumer = context.createConsumerTemplate();
			System.err.println("OUTPUT : " + (String) consumer.receiveBody("activemq:test.queue"));

			Thread.sleep(2000 * 2000);

		} finally {
			context.stop();
		}

	}

	public static void main(String[] args) throws Exception {
		activeMQProducer();
		xmlXpathFilter();
		FileToMsg();
		camelCopy();
	}

}
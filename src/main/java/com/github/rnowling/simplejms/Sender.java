package com.github.rnowling.simplejms;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Sender
{
	TopicConnection connection;
	TopicPublisher publisher;
	TopicSession session;
	String username;

	private final static SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	public Sender(String topicFactory, String topicName, String username) throws NamingException, JMSException
	{
		InitialContext ctx = new InitialContext();
		
		TopicConnectionFactory connFactory = (TopicConnectionFactory) ctx.lookup(topicFactory);
		connection = connFactory.createTopicConnection();
		
		session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		
		Topic chatTopic = (Topic) ctx.lookup(topicName);
		
		publisher = session.createPublisher(chatTopic);
		
		this.username = username;
	}
	
	public void close() throws JMSException
	{
		connection.close();
	}
	
	public void sendMessage(String text) throws JMSException
	{
		String msg  = String.format( "%s@%s : %s",  fmt.format(new Date()), username ,text);
		TextMessage message = session.createTextMessage(msg);
		publisher.publish(message);
	}
}

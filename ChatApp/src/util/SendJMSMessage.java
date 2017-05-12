package util;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;


public class SendJMSMessage implements MessageListener{
	
	public SendJMSMessage(String chatPair) {
		instantiateJMS(chatPair);
	}
	
	private void instantiateJMS(String chatPair) {
    	
    	try {
			Context context = new InitialContext();
			ConnectionFactory cf = (ConnectionFactory) context.lookup("ConnectionFactory");
			final Topic topic = (Topic) context.lookup("jms/topic/informTopic");
			context.close();
    		
			Connection connection = cf.createConnection("guest", "guestguest");
			final javax.jms.Session session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);

			connection.start();

			MessageConsumer consumer = session.createConsumer(topic);
			consumer.setMessageListener(this);
			
			// create and publish a message
			TextMessage msg = session.createTextMessage();
			msg.setText(chatPair);
			MessageProducer producer = session.createProducer(topic);
			producer.send(msg);
			//Thread.sleep(1000);
			System.out.println("Message published. Please check application server's console to see the response from MDB.");
			
			producer.close();
			consumer.close();
			connection.stop();
			connection.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    }
	
	
	public void onMessage(javax.jms.Message msg) {
		System.out.println("Received a message.");
		if (msg instanceof TextMessage) {
			TextMessage tm = (TextMessage) msg;
			try {
				String text = tm.getText();
				System.out.println("Received new message : " + text);
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

}

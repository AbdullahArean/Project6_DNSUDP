//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.xbill.DNS.*;
//
//import java.io.IOException;
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.net.InetAddress;
//import java.time.Duration;
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class TestResolver {
//    private static final Logger logger = LoggerFactory.getLogger(TestResolver.class);
//
//    @Test
//    public void simpleResolver() throws IOException {
//        TestDNSServer server = new TestDNSServer(53);
//        server.start();
//        SimpleResolver resolver = new SimpleResolver(InetAddress.getLocalHost());
//        resolver.setTimeout(Duration.ofSeconds(1));
//
//        Lookup lookup = new Lookup(Name.root, Type.A);
//        lookup.setResolver(resolver);
//        lookup.setCache(null);
//
//        lookup.run();
//
//        Assertions.assertEquals(1, server.getRequestCount());
//
//        server.stop();
//    }
//
//    @Test
//    public void extendedResolver() throws IOException {
//        List<TestDNSServer> servers = new ArrayList<>();
//        List<Resolver> resolvers = new ArrayList<>();
//        for (int i = 0; i<5; i++) {
//            int port = 1000 + i * 100 + 53;
//            TestDNSServer s = new TestDNSServer(port);
//            s.start();
//            servers.add(s);
//            SimpleResolver r = new SimpleResolver(InetAddress.getLocalHost());
//            r.setPort(port);
//            // r.setTimeout(Duration.ofSeconds(1));  // Timeout of each resolver will be overwritten to ExtendedResolver.DEFAULT_TIMEOUT
//            resolvers.add(r);
//        }
//
//        ExtendedResolver resolver = new ExtendedResolver(resolvers);
//        resolver.setTimeout(Duration.ofSeconds(1));
//        resolver.setRetries(5);
//
//        Lookup lookup = new Lookup(Name.root, Type.A);
//        lookup.setResolver(resolver);
//        lookup.setCache(null);
//
//        long startTime = System.currentTimeMillis();
//        lookup.run();
//        logger.error(String.format("time: %d", (System.currentTimeMillis() - startTime)/1000));
//
//        for (TestDNSServer s: servers) {
//            Assertions.assertEquals(5, s.getRequestCount()); // This will fail as ExtendedResolver does not work as I expected
//            s.stop();
//        }
//    }
//
//    private static class TestDNSServer {
//        private Thread thread = null;
//        private volatile boolean running = false;
//        private static final int UDP_SIZE = 512;
//        private final int port;
//        private int requestCount = 0;
//
//        TestDNSServer(int port) {
//            this.port = port;
//        }
//
//        public void start() {
//            running = true;
//            thread = new Thread(() -> {
//                try {
//                    serve();
//                } catch (IOException ex) {
//                    stop();
//                    throw new RuntimeException(ex);
//                }
//            });
//            thread.start();
//        }
//
//        public void stop() {
//            running = false;
//            thread.interrupt();
//            thread = null;
//        }
//
//        public int getRequestCount() {
//            return requestCount;
//        }
//
//        private void serve() throws IOException {
//            DatagramSocket socket = new DatagramSocket(port);
//            while (running) {
//                process(socket);
//            }
//        }
//
//        private void process(DatagramSocket socket) throws IOException {
//            byte[] in = new byte[UDP_SIZE];
//
//            // Read the request
//            DatagramPacket indp = new DatagramPacket(in, UDP_SIZE);
//            socket.receive(indp);
//            ++requestCount;
//            logger.info(String.format("processing... %d", requestCount));
//
//            // Build the response
//            Message request = new Message(in);
//            Message response = new Message(request.getHeader().getID());
//            response.addRecord(request.getQuestion(), Section.QUESTION);
//            // Add answers as needed
//            response.addRecord(Record.fromString(Name.root, Type.A, DClass.IN, 86400, "1.2.3.4", Name.root), Section.ANSWER);
//
//            // Make it timeout, comment this section if a success response is needed
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException ex) {
//                logger.error("Interrupted");
//                return;
//            }
//
//            byte[] resp = response.toWire();
//            DatagramPacket outdp = new DatagramPacket(resp, resp.length, indp.getAddress(), indp.getPort());
//            socket.send(outdp);
//        }
//    }
//}
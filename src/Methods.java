
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Methods {

    public static void Send(CustomPacket obj, int port) {
        try {

            try (DatagramSocket sendSocket = new DatagramSocket()) {
                InetAddress IPAddress = InetAddress.getByName("localhost");
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ObjectOutputStream os = new ObjectOutputStream(outputStream);
                os.writeObject(obj);
                byte[] data = outputStream.toByteArray();
                DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, port);
                sendSocket.send(sendPacket);
                sendSocket.close();
            }
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        } catch (SocketException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public static CustomPacket Recieve(int port) throws ClassNotFoundException, SocketException {
        CustomPacket obj = new CustomPacket();
        try (DatagramSocket RecieveSocket = new DatagramSocket(port)) {
            try {
                byte[] incomingData = new byte[1024];
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                RecieveSocket.receive(incomingPacket);
                byte[] ReceiveData = incomingPacket.getData();
                ByteArrayInputStream in = new ByteArrayInputStream(ReceiveData);
                ObjectInputStream is = new ObjectInputStream(in);
                obj = (CustomPacket) is.readObject();
            } catch (UnknownHostException e) {
                System.out.println(e.getMessage());
            } catch (SocketException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            RecieveSocket.close();
        }
        return obj;
    }
}

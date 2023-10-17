using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;

namespace Chat_Server
{
    public class App
    {
        private static List<TcpClient> clients = new List<TcpClient>();
        private static TcpListener listener;

        static void Main()
        {
            listener = new TcpListener(IPAddress.Any, 1234);
            listener.Start();
            Console.WriteLine("Server is running...");

            while (true)
            {
                TcpClient client = listener.AcceptTcpClient();
                clients.Add(client);

                Thread clientThread = new Thread(() => HandleClient(client));
                clientThread.Start();
            }
        }

        static void HandleClient(TcpClient client)
        {
            using (NetworkStream stream = client.GetStream())
            {
                byte[] buffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = stream.Read(buffer, 0, buffer.Length)) > 0)
                {
                    string message = Encoding.ASCII.GetString(buffer, 0, bytesRead);
                    Console.WriteLine("Received from C#: " + message);

                    // Encaminhar a mensagem para clientes Java
                    Broadcast(message);
                }
            }
        }

        static void Broadcast(string message)
        {
            foreach (TcpClient client in clients)
            {
                using (NetworkStream stream = client.GetStream())
                {
                    byte[] data = Encoding.ASCII.GetBytes(message);
                    stream.Write(data, 0, data.Length);
                }
            }
        }
    }
}

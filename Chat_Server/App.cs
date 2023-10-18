using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.NetworkInformation;
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
            var enderecoIPLocal = IPAddress.Parse(RecuperarEnderecoIPLocal());
            listener = new TcpListener(enderecoIPLocal, 6969);
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

        private static string RecuperarEnderecoIPLocal()
        {

            // Obtém a lista de todas as interfaces de rede da máquina.
            var networkInterfaces = NetworkInterface.GetAllNetworkInterfaces().ToList();

            // Filtra as interfaces de rede que têm o tipo "Wireless80211"
            var networkInterface = networkInterfaces.Where(x => x.NetworkInterfaceType == NetworkInterfaceType.Wireless80211 && x.Name == "Wi-Fi").FirstOrDefault();

            if (networkInterface == null)
                throw new Exception("Erro ao recuperar o endereço IP da márquina.");


            // Recupera o endereço IPv4 da rede Wi-Fi
            var ipv4 = networkInterface.GetIPProperties().UnicastAddresses.Where(x => x.Address.AddressFamily == AddressFamily.InterNetwork).FirstOrDefault();

            if (ipv4 == null)
                throw new Exception("Endereço IPv4 não encontrado");

            return ipv4.Address.ToString();
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

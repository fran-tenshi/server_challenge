package com.socket.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class SocketListener extends Thread {

	private ServerSocket serverSocket;
	private Socket socket;
	private final static int TCP_SERVER_PORT = 2222;
	private String message;

	public SocketListener(Socket socket) {
		this.socket = socket;
	}

	@Bean
	public void init() {
		try {
			log.info("Iniciando socket");
			serverSocket = new ServerSocket(TCP_SERVER_PORT);
			while (true) {
				socket = serverSocket.accept();
				new SocketListener(socket).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			DataInputStream clientinp = new DataInputStream(socket.getInputStream());
			DataOutputStream clientout = new DataOutputStream(socket.getOutputStream());
			
			byte[] array = new byte[1024];
			int length = clientinp.read(array);
			
			if (length > -1) {
				log.info("recebendo");
				message = new String(array, 0, length, StandardCharsets.UTF_8);
				log.info("retorno");
				clientout.flush();
				log.info(message);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

package com.carry.carryouhome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.carry.carryouhome.websocket.CarryWebSocket;

@SpringBootApplication
public class CarryouhomeApplication {
private static CarryWebSocket websocket;
	public static void main(String[] args) {
		websocket = new CarryWebSocket();
		try {
			websocket.ServerStart();

		} catch (Exception e) {
			// TODO: handle exception
		}
		SpringApplication.run(CarryouhomeApplication.class, args);
	}
}

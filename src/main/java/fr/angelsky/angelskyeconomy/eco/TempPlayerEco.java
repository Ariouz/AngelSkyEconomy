package fr.angelsky.angelskyeconomy.eco;

import java.util.UUID;

public class TempPlayerEco {

	private double balance;
	private final UUID uuid;

	public TempPlayerEco(UUID uuid, double balance)
	{
		this.uuid = uuid;
		this.balance = balance;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public UUID getUuid() {
		return uuid;
	}
}

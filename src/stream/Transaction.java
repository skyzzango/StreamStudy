package stream;

import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class Transaction {
	private final Trader trader;
	private int year;
	private int value;
	private Currency currency;

	public Transaction(Trader trader, int year, int value, Locale locale) {
		this.trader = trader;
		this.year = year;
		this.value = value;
		this.currency = Currency.getInstance(locale);
	}

	public Trader getTrader() {
		return trader;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public static List<Transaction> createTransaction() {
		Trader raoul = new Trader("Raoul", "Cambridge");
		Trader mario = new Trader("Mario", "Milan");
		Trader alan = new Trader("Alan", "Cambridge");
		Trader brian = new Trader("Brian", "Cambridge");

		 return Arrays.asList(
				new Transaction(brian, 2011, 300, Locale.UK),
				new Transaction(raoul, 2012, 1000, Locale.UK),
				new Transaction(raoul, 2011, 400, Locale.UK),
				new Transaction(mario, 2012, 710, Locale.ITALY),
				new Transaction(alan, 2012, 950, Locale.GERMANY)
		);
	}

	@Override
	public String toString() {
		return "Transaction{" +
				"trader=" + trader +
				", year=" + year +
				", value=" + value +
				"}";
	}
}

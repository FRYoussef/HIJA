package model.representation;

public class Stat {

	private String field = null;
	private String text = null;
	private int percentage;

	public Stat(String field, String text, int percentage) {
		this.field = field;
		this.text = text;
		this.percentage = percentage;
	}

	public Stat(String field, int percentage) {
		this.field = field;
		this.percentage = percentage;
	}

	public String getField() {
		return field;
	}

	public String getText() {
		return text;
	}

	public int getPercentage() {
		return percentage;
	}

	@Override
	public String toString() {
		String aux = field + ": " + percentage + "%";
		if(text != null)
			return aux + "\n" + text;
		else
			return aux;
	}
}

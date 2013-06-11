package com.corridor9design.mfdpaycalculator.database;

public class Deduction {

	// create variables
	private int _id;
	private String _deduction_name;
	private String _deduction_amount;
	private String _first_payday;
	private String _second_payday;
	private String _third_payday;

	public Deduction() {

	}

	public Deduction(int id, String name, String amount, String first, String second, String third) {
		super();
		this._id = id;
		this._deduction_name = name;
		this._deduction_amount = amount;
		this._first_payday = first;
		this._second_payday = second;
		this._third_payday = third;
	}
	
	public Deduction(String name, String amount, String first, String second, String third) {
		super();
		this._deduction_name = name;
		this._deduction_amount = amount;
		this._first_payday = first;
		this._second_payday = second;
		this._third_payday = third;
	}

	public Deduction(String name, String amount) {
		super();
		this._deduction_name = name;
		this._deduction_amount = amount;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String get_deduction_name() {
		return _deduction_name;
	}

	public void set_deduction_name(String _deduction_name) {
		this._deduction_name = _deduction_name;
	}

	public String get_deduction_amount() {
		return _deduction_amount;
	}

	public void set_deduction_amount(String _deduction_amount) {
		this._deduction_amount = _deduction_amount;
	}

	public String get_first_payday() {
		return _first_payday;
	}

	public void set_first_payday(String _first_payday) {
		this._first_payday = _first_payday;
	}

	public String get_second_payday() {
		return _second_payday;
	}

	public void set_second_payday(String _second_payday) {
		this._second_payday = _second_payday;
	}

	public String get_third_payday() {
		return _third_payday;
	}

	public void set_third_payday(String _third_payday) {
		this._third_payday = _third_payday;
	}

}

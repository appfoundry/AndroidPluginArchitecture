package com.siebesysmans.moreadvancedoperations;

import be.appfoundry.simplecalculatorplugin.PluginService;

public class MultiplyPlugin extends PluginService {
	@Override
	protected double getResult(double number1, double number2) {
		return number1 * number2;
	}
}

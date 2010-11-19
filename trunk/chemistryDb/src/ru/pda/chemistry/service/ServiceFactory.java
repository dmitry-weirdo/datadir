package ru.pda.chemistry.service;

/**
 * User: 1
 * Date: 27.04.2010
 * Time: 0:31:25
 */
public class ServiceFactory
{
	public static ConstructorService getConstructorService() {
		return new ConstructorServiceImpl();
	}

	public static GeneratorService getGeneratorService() {
		return new GeneratorServiceImpl();
	}
}
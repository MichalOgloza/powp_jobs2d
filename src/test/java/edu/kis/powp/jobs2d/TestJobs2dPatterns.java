package edu.kis.powp.jobs2d;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.kis.legacy.drawer.panel.DefaultDrawerFrame;
import edu.kis.legacy.drawer.panel.DrawPanelController;
import edu.kis.legacy.drawer.shape.LineFactory;
import edu.kis.powp.appbase.Application;
import edu.kis.powp.command.*;
import edu.kis.powp.jobs2d.drivers.IModifiableLine;
import edu.kis.powp.jobs2d.drivers.adapter.LineDrawerAdapter;
import edu.kis.powp.jobs2d.drivers.adapter.ModifiableLineAdapter;
import edu.kis.powp.jobs2d.events.SecondTestFigureOptionListener;
import edu.kis.powp.jobs2d.events.SelectChangeVisibleOptionListener;
import edu.kis.powp.jobs2d.events.FirstTestFigureOptionListener;
import edu.kis.powp.jobs2d.features.DrawerFeature;
import edu.kis.powp.jobs2d.features.DriverFeature;

public class TestJobs2dPatterns {
	private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	/**
	 * Setup test concerning preset figures in context.
	 * 
	 * @param application Application context.
	 */
	private static void setupPresetTests(Application application) {
		FirstTestFigureOptionListener firstTestFigureOptionListener = new FirstTestFigureOptionListener(
				DriverFeature.getDriverManager());
		SecondTestFigureOptionListener secondTestFigureOptionListener = new SecondTestFigureOptionListener(
				DriverFeature.getDriverManager());

		application.addTest("Figure Joe 1", firstTestFigureOptionListener);
		application.addTest("Figure Joe 2", secondTestFigureOptionListener);
		application.addTest("Horizontal line", event ->
		{
			Job2dDriver driver = DriverFeature.getDriverManager().getCurrentDriver();
			DriverCommand command = new SetPositionCommand(driver, -200,0);
			command.execute();
			command = new OperateToCommand(driver, 200,0);
			command.execute();
		});
		application.addTest("V sign", event ->
		{
			Job2dDriver driver = DriverFeature.getDriverManager().getCurrentDriver();
			ArrayList<DriverCommand> list = new ArrayList<>();
			list.add(new SetPositionCommand(driver, -200,-200));
			list.add(new OperateToCommand(driver, 0,200));
			list.add(new OperateToCommand(driver, 200, -200));

			DriverCommand command = new ComplexCommand(list);
			command.execute();
		});
		application.addTest("Square", event ->
		{
			Job2dDriver driver = DriverFeature.getDriverManager().getCurrentDriver();
			DriverCommand command = ComplexCommand.getSquareCommand(driver);
			command.execute();
		});
		application.addTest("Triangle", event ->
		{
			Job2dDriver driver = DriverFeature.getDriverManager().getCurrentDriver();
			DriverCommand command = ComplexCommand.getTriangleCommand(driver);
			command.execute();
		});
		application.addTest("Figure Joe 1 using builder", event ->
		{
			Job2dDriver driver = DriverFeature.getDriverManager().getCurrentDriver();

			new BuilderCommand().withCommand(new SetPositionCommand(driver, -120, -120))
								.withCommand(new OperateToCommand(driver, 120, -120))
								.withCommand(new OperateToCommand(driver, 120, 120))
								.withCommand(new OperateToCommand(driver, -120, 120))
								.withCommand(new OperateToCommand(driver, -120, -120))
								.withCommand(new OperateToCommand(driver, 120, 120))
								.withCommand(new SetPositionCommand(driver, 120, -120))
								.withCommand(new OperateToCommand(driver, -120, 120))
								.buildCommand()
								.execute();
		});
	}

	/**
	 * Setup driver manager, and set default driver for application.
	 * 
	 * @param application Application context.
	 */
	private static void setupDrivers(Application application) {
		Job2dDriver loggerDriver = new LoggerDriver();
		DriverFeature.addDriver("Logger Driver", loggerDriver);
		DriverFeature.getDriverManager().setCurrentDriver(loggerDriver);

		IModifiableLine basicThinLine = new ModifiableLineAdapter();
		basicThinLine.setColor(Color.RED);
		basicThinLine.setThickness(1);
		Job2dDriver basicThinLineDriver = new LineDrawerAdapter(DrawerFeature.getDrawerController(), basicThinLine);
		DriverFeature.addDriver("Basic Thin Red Simulator", basicThinLineDriver);

		IModifiableLine basicThickLine = new ModifiableLineAdapter();
		basicThickLine.setColor(Color.BLUE);
		basicThickLine.setThickness(5);
		Job2dDriver basicThickLineDriver = new LineDrawerAdapter(DrawerFeature.getDrawerController(), basicThickLine);
		DriverFeature.addDriver("Basic Thick Blue Simulator", basicThickLineDriver);

		IModifiableLine dottedThinLine = new ModifiableLineAdapter();
		dottedThinLine.setColor(Color.YELLOW);
		dottedThinLine.setThickness(1);
		dottedThinLine.setDotted(true);
		Job2dDriver dottedThinLineDriver = new LineDrawerAdapter(DrawerFeature.getDrawerController(), dottedThinLine);
		DriverFeature.addDriver("Dotted Thin Yellow Simulator", dottedThinLineDriver);

		IModifiableLine dottedThickLine = new ModifiableLineAdapter();
		dottedThickLine.setColor(Color.GREEN);
		dottedThickLine.setThickness(5);
		dottedThickLine.setDotted(true);
		Job2dDriver dottedThickLineDriver = new LineDrawerAdapter(DrawerFeature.getDrawerController(), dottedThickLine);
		DriverFeature.addDriver("Dotted Thick Green Simulator", dottedThickLineDriver);

		DriverFeature.updateDriverInfo();
	}

	/**
	 * Auxiliary routines to enable using Buggy Simulator.
	 * 
	 * @param application Application context.
	 */
	private static void setupDefaultDrawerVisibilityManagement(Application application) {
		DefaultDrawerFrame defaultDrawerWindow = DefaultDrawerFrame.getDefaultDrawerFrame();
		application.addComponentMenuElementWithCheckBox(DrawPanelController.class, "Default Drawer Visibility",
				new SelectChangeVisibleOptionListener(defaultDrawerWindow), false);
		defaultDrawerWindow.setVisible(false);
	}

	/**
	 * Setup menu for adjusting logging settings.
	 * 
	 * @param application Application context.
	 */
	private static void setupLogger(Application application) {
		application.addComponentMenu(Logger.class, "Logger", 0);
		application.addComponentMenuElement(Logger.class, "Clear log",
				(ActionEvent e) -> application.flushLoggerOutput());
		application.addComponentMenuElement(Logger.class, "Fine level", (ActionEvent e) -> logger.setLevel(Level.FINE));
		application.addComponentMenuElement(Logger.class, "Info level", (ActionEvent e) -> logger.setLevel(Level.INFO));
		application.addComponentMenuElement(Logger.class, "Warning level",
				(ActionEvent e) -> logger.setLevel(Level.WARNING));
		application.addComponentMenuElement(Logger.class, "Severe level",
				(ActionEvent e) -> logger.setLevel(Level.SEVERE));
		application.addComponentMenuElement(Logger.class, "OFF logging", (ActionEvent e) -> logger.setLevel(Level.OFF));
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Application app = new Application("2d jobs Visio");
				DrawerFeature.setupDrawerPlugin(app);
				setupDefaultDrawerVisibilityManagement(app);

				DriverFeature.setupDriverPlugin(app);
				setupDrivers(app);
				setupPresetTests(app);
				setupLogger(app);

				app.setVisibility(true);
			}
		});
	}

}

V4.0

1. Consolidated Hooks in 1 class;
2. Updated naming for scenarios in UILogin feature;
3. Updated timeout - excluded hardcoded value, moved into config.properties file;
4. BrowserActions - detailed logging with specification of fieldName+value inserted;
5. Login - moved OpenHomePage into Hooks class;
6. Background step for UI-Login feature (To be revised)


V4.3-UNSTABLE
---------------------->>>PRE-assessment #1 observations:<<<---------------------------------------------------------

-TODO remove comments on assessment
-Inform about Appender -configLog
-Created new packages for actions (ui, api side), config (driverManager, logging), context (ScenarioContext), enums (Contextkey, TBA);
-Hooks class - navigateToLoginPage method to be moved in BrowserActions; Logger - replace {} with %s;

-TestRunner class:
 - //TODO add StepNotifications-OPTIONAL
 - "html:target/reports/Reports.html", //TODO add logs in reports
 - //TODO add git ignore for target folder!!! Why required all evidence in target, but not in project

-UI-LoginPage class
 - fieldName either variable or Enum (LogInWithCredentials method); DurationofSeconds 
 - retrieve from config (getErrorMessage method); 
 - Logger - replace {} with %s;


-UI-BuzzPage class
 - populateWhatsOnYourMindField(String text) {//TODO naming of method too long
 - LOGGER.info("Clicked on the post button");//TODO click button name, exclude hard code
 - WebElement postElement = new WebDriverWait(driver, Duration.ofSeconds(20)) //TODO exclude hardcoded value, map to config VALUE

-BuzzPageSteps class
 - public void whatSOnYourMindFieldIsPopulatedWithRandomText() {//TODO long naming
 - post_button_is_clicked() {//TODO camel case, not snake - rename all methods in all classes
 - 

-Rename API classes

-API-GetCustomerInfo class
 - Add background title
 - Rename When clause
 - Rename fetchCustomerInfo
 - When: //TODO save as object, use Pojo class. Save sts code into scenContext, retrieve in Then for validation
 - And: clause to be removed. Redo - compare 2 objects, dataTable remains, but converted in obj.
 - And: compareCustomerData method if will remain after above point - move in actions package
 - Logger - replace {} with %s;

-API-PostHolidayDay class
 - Add background title
 - When: requestBodySent = requestBody;//TODO  requestBodySent object (pojo)
 - When: generateHolidayRequestBody method - more likely it's an action - to move
 - Then: retrieve sts code from scenarioContext
 - Logger - replace {} with %s;

-PropertiesUtil class
 - loadProperties method - add 2 custom exceptions for try and catch
 - Logger - replace {} with %s;

-ScreenshotUtils class
 - scenario.attach(screenshotBytes, "image/png", "Screenshot"); //TODO variable or enum
 - String screenshotFilename = scenarioName + "_" + timestamp + ".png";//TODO variable or Enum
 - File screenshotDirectory = new File("target/screenshots");//TODO variable
 - Logger - replace {} with %s;

-CommonSteps class
 - getBearerToken method - Move to actions. Actions package to dif by ui or api

-BrowserActions class
 - private final WebDriverWait wait; //TODO understand static vs final variables usage!!!
    private static final Logger LOGGER = LoggerFactory.getLogger(BrowserActions.class);
 - this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));//TODO retrieve from property
 - catch (Exception e) {LOGGER.error("Attempt {}: Exception occurred while populating the field. Retrying...", attempts + 1, e);//TODO add custom Exception
 - Logger - replace {} with %s;

-ApiTokenGenerator class
 - private static final String PASSWORD = PropertiesUtil.getProperty("password"); //TODO enums for variables OPTIONAL
 - public static String generateBearerToken() throws Exception { //TODO dif btw throws vs throw. Why Exception is not used
 - RestAssured.given().formParam("client_id", CLIENT_ID) //TODO apply enums
 - hrow new RuntimeException("Failed to generate bearer token"); //TODO enums for error msgs

-DriverManager class
 - private static String getBrowserType() {return PropertiesUtil.getProperty("browser.type").toUpperCase();}//TODO Variable or ENUM
 - switch (browserType.toUpperCase()) {//TODO refactor switch for java 21
 - Logger - replace {} with %s;

-Logging class
 - String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss.SS"));//TODO as variable or dateTimeNow
 - String featureLogFileName = featureName + "_" + timestamp + ".log"; //TODO keep formating, directory in this class, rest of code logic handled in Logconfig
 - LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();//TODO understand (LoggerContext) usage (casting)?
 - TODO FileAppender<ILoggingEvent> - for study
 - Set up the log message format, Set up the encoder to convert log events into byte streams, Create and configure the file appender, Add the file appender to the feature logger - move this logic to config
 - To study: ...string (string with 3 dots)
 -  Files.createDirectories(Paths.get(path)); //TODO how it works: createDirectories. Why this method is chosen?
 - ensureDirectoriesExist method: TODO add custom Exception
 - public void afterScenario(Scenario scenario) {//TODO why scenario is not used, if not required-to remove

-ScenarioContext class
 - public static ScenarioContext getInstance() {
        if (instance == null) {
            instance = new ScenarioContext();
        }
        return instance;//TODO Singleton & scenarioContext (why it's singleton?). 3 characterstics of Singleton?!
 - public <T> T getValueFromScenarioContext(ContextKey key) {
        return (T) contextMap.get(key); //TODO correct this error. Method name to be shorter

-ContextKey class
 - WEBDRIVER_INSTANCE //TODO bracket mark

-Hooks class
 - navigateToLoginPage(WebDriver driver, String baseUrl) {//TODO move to browserActions
 - Logger - replace {} with %s;

---------------------->>>PRE-assessment #1 observations:<<<---------------------------------------------------------

V4.0

1. Consolidated Hooks in 1 class;
2. Updated naming for scenarios in UILogin feature;
3. Updated timeout - excluded hardcoded value, moved into config.properties file;
4. BrowserActions - detailed logging with specification of fieldName+value inserted;
5. Login - moved OpenHomePage into Hooks class;
6. Background step for UI-Login feature (To be revised)

--------------->>>PRE-assessment #1 observations:<<<---------------------------------------------------------

-TODO remove comments on assessment
-Inform about Appender -configLog
-logback.xml - To study in detail each value.Set max size
-Created new packages for actions (ui, api side), config (driverManager, logging), context (ScenarioContext), enums (Contextkey, TBA);
-Hooks class - navigateToLoginPage method to be moved in BrowserActions; Logger - replace {} with %s;

-TestRunner class
 - //TODO add StepNotifications-OPTIONAL
 - "html:target/reports/Reports.html", //TODO add logs in reports
 - //TODO add git ignore for target folder!!! Why required all evidence in target, but not in project

-UI-LoginPage class  ---DONE+++
 - fieldName either variable or Enum (LogInWithCredentials method + custom method); -- ENUM  ---DONE+++
 - retrieve from config (getErrorMessage method), DurationofSeconds;  ---DONE+++
 - Logger - replace {} with %s;  ---DONE+++


-UI-BuzzPage class ---DONE+++
 - populateWhatsOnYourMindField(String text) {//TODO naming of method too long ---DONE+++
 - LOGGER.info("Clicked on the post button");//TODO click button name, exclude hard code
 - WebElement postElement = new WebDriverWait(driver, Duration.ofSeconds(20)) //TODO exclude hardcoded value, map to config VALUE ---DONE+++

-BuzzPageSteps class ---DONE+++
 - public void whatSOnYourMindFieldIsPopulatedWithRandomText() {//TODO long naming ---DONE+++
 - post_button_is_clicked() {//TODO camel case, not snake - rename all methods in all classes ---DONE+++


-Rename API classes    ---DONE+++

-API-GetCustomerInfo class    ---DONE+++
 - Add background title    ---DONE+++
 - Rename When clause    ---DONE+++
 - Rename fetchCustomerInfo    ---DONE+++
 - When: //TODO save as object, use Pojo class. Save sts code, retrieve in Then for validation    ---DONE+++
 - And: clause to be removed - NOT removed, not required. Redo - compare 2 objects, dataTable remains, but converted in obj.     ---DONE+++
 - And: compareCustomerData method if will remain after above point - move in actions package    ---DONE+++
 - Logger - replace {} with %s; DONE++

-API-PostHolidayDay class    ---DONE+++
 - Add background title    ---DONE+++
 - When: requestBodySent = requestBody;//TODO  requestBodySent object (pojo)    ---DONE+++
 - When: generateHolidayRequestBody method - more likely it's an action - to move    ---DONE+++
 - Then: retrieve sts code from scenarioContext    ---DONE+++
 - Logger - replace {} with %s; DONE++

-PropertiesUtil class  ---DONE+++
 - loadProperties method - add 2 custom exceptions for try and catch  ---DONE+++
 - Logger - replace {} with %s;  ---DONE+++

-ScreenshotUtils class  ---DONE+++
 - scenario.attach(screenshotBytes, "image/png", "Screenshot"); //TODO variable or Enum ---Constants: ATTACHMENT_TYPE and ATTACHMENT_DESCRIPTION  ---DONE+++
 - String screenshotFilename = scenarioName + "_" + timestamp + ".png";//TODO variable or Enum  ---Constants: ATTACHMENT_TYPE and ATTACHMENT_DESCRIPTION  ---DONE+++
 - File screenshotDirectory = new File("target/screenshots");//TODO variable Directory Path: SCREENSHOT_DIRECTORY as constant   ---DONE+++
 - Logger - replace {} with %s;  ---DONE+++

-CommonSteps class  ---DONE+++
 - getBearerToken method - Move to actions. Actions package to dif by ui or api  ---DONE+++

-BrowserActions class (New name: UIActions)   ---DONE+++
 - private final WebDriverWait wait; //TODO understand static vs final variables usage!!!
    private static final Logger LOGGER = LoggerFactory.getLogger(BrowserActions.class);
 - this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));//TODO retrieve from property ---DONE+++
 - catch (Exception e) {LOGGER.error("Attempt {}: Exception occurred while populating the field. Retrying...", attempts + 1, e);//TODO add custom Exception  ---DONE+++
 - Logger - replace {} with %s; ---DONE+++

-ApiTokenGenerator class    ---DONE+++
 - private static final String PASSWORD = PropertiesUtil.getProperty("password"); //TODO enums for variables OPTIONAL    ---DONE+++
 - public static String generateBearerToken() throws Exception { //TODO dif btw throws vs throw. Why Exception is not used --throws was unused, because specific EXP not stated +++
 - RestAssured.given().formParam("client_id", CLIENT_ID) //TODO apply enums     ---DONE+++
 - hrow new RuntimeException("Failed to generate bearer token"); //TODO enums for error msgs    ---DONE+++

-DriverManager class   -- DONE+++
 - private static String getBrowserType() {return PropertiesUtil.getProperty("browser.type").toUpperCase();}//TODO Variable or ENUM --Left retrieving from config logic+++
 - switch (browserType.toUpperCase()) {//TODO refactor switch for java 21   -- DONE+++
 - Logger - replace {} with %s;

-Logging class   -- DONE+++
 - String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss.SS"));//TODO as variable or dateTimeNow
 - String featureLogFileName = featureName + "_" + timestamp + ".log"; //TODO keep formating, directory in this class, rest of code logic handled in Logconfig
 - LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();//TODO understand (LoggerContext) usage (casting)?
 - TODO FileAppender<ILoggingEvent> - for study
 - Set up the log message format, Set up the encoder to convert log events into byte streams, Create and configure the file appender, Add the file appender to the feature logger - move this logic to config ---Left, app.log handled in config
 - To study: ...string (string with 3 dots)
 -  Files.createDirectories(Paths.get(path)); //TODO how it works: createDirectories. Why this method is chosen?
 - ensureDirectoriesExist method: TODO add custom Exception ---
 - public void afterScenario(Scenario scenario) {//TODO why scenario is not used, if not required-to remove

       work done: removed ensureDirectoriesExist, arguments from afterScenario method. Added fileSize limit, added config for app.log.
                  LocalDateTime as variable for timestamp. Moved Before/After logging logic into Hooks.

-ScenarioContext class   -- DONE+++
 - public static ScenarioContext getInstance() {
        if (instance == null) {
            instance = new ScenarioContext();
        }
        return instance;//TODO Singleton & scenarioContext (why it's singleton?). 3 characterstics of Singleton?!
 - public <T> T getValueFromScenarioContext(ContextKey key) {
        return (T) contextMap.get(key); //TODO correct this error. Method name to be shorter   -- DONE+++

-ContextKey class   -- DONE+++
 - WEBDRIVER_INSTANCE //TODO bracket mark --syntax complaint - WEBDRIVER - 2 separate words, should be divided by underscore: WEB_DRIVER

-Hooks class   -- DONE+++
 - navigateToLoginPage(WebDriver driver, String baseUrl) {//TODO move to browserActions   -- DONE+++
 - Logger - replace {} with %s; DONE++

--------------->>>PRE-assessment #1 observations:<<<---------------------------------------------------------

--------------->>>PRE-assessment #2 observations:<<<---------------------------------------------------------
-Hooks class - TBD
 - optional: move extractFeatureName to utils. consider beforeScenario method


-API-PostHolidayDay class - TBD
- sendHolidayData method - convert asMaps
- move utility methods to apiActions
- extractFieldFromRequestBody method - provide specific return instead of null


-API-ApiTokenGenerator class - TBD
- eliminate enums from method or class
- getString("access_token");//TODO move to enume apiproperties
- getStatusCode() != 200) {//TODO as variable sts code
- formParam(FormParams.GRANT_TYPE.getParam(), "client_credentials")//TODO to correct


-UI-LoginPage class - TBD
- timeout.seconds")); //TODO declare as variable

-UI-UIActions class - TBD
- "timeout.seconds"));//TODO as variable


- UI-BasePage class - TBD
- getProperty("timeout.seconds");//TODO as variable


-DriverManager class  - TBD
- getProperty("browser.type").toUpperCase();//TODO as variable
- getProperty("browser.headless"));//TODO as variable


- Logging class - TBD
- fusion /evidence folder with screenshots+logs sub-folders
- DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss.SS")); //TODO as variable
- featureLogDirectoryPath = "target/logs/featureLogs"; //TODO as variable
- setPattern("%date{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"); //TODO as variable
- setPattern("%date [%thread] %-5level %logger{35} - %msg%n"); //TODO as variable



-ScenarioContext class - TBD
- return (T) value; //TODO unchecked cast error
- add custom explicit custom EXP for getContext method


-ApiProperties overrides ApiPropertyValues - handled in ApiTokenGenerator - TBD



- ScreenshotUtils class - TBD
- getProperty("timeout.seconds"));//TODO as variable
- screenshotDirectory.mkdirs(); // TODO correct save/notSave dir logic

--------------->>>PRE-assessment #2 observations:<<<---------------------------------------------------------

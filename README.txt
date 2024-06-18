V4.0

1. Consolidated Hooks in 1 class;
2. Updated naming for scenarios in UILogin feature;
3. Updated timeout - excluded hardcoded value, moved into config.properties file;
4. BrowserActions - detailed logging with specification of fieldName+value inserted;
5. Login - moved OpenHomePage into Hooks class;
6. Background step for UI-Login feature (To be revised)

-------------------------------------------------------------->>>PRE-assessment #2 observations:<<<---------------------------------------------------------
-Hooks class   -- DONE +++
 - optional: move extractFeatureName to utils. consider beforeScenario method -UNCHANGED
 - added logic for Logs to be attached into cucumber reports   -- DONE +++


-API-PostHolidayDay class   -- DONE +++
- sendHolidayData method - convert asMaps---UNCHANGED-doesn't handle dynamic fields properly+++
- move utility methods to apiActions   -- DONE +++
- extractFieldFromRequestBody method - provide specific return instead of null   -- DONE +++
- written switch for java21 -- DONE +++


-API-ApiTokenGenerator class   -- DONE+++
- eliminate enums from method or class   -- DONE+++
- getString("access_token");//TODO move to enume apiproperties   -- DONE+++
- getStatusCode() != 200) {//TODO as variable sts code   -- DONE+++
- formParam(FormParams.GRANT_TYPE.getParam(), "client_credentials")//TODO to correct   -- DONE+++


-UI-LoginPage class --  DONE+++
- timeout.seconds")); //TODO declare as variable   -- DONE+++

-UI-UIActions class    -- DONE ----
- "timeout.seconds"));//TODO as variable   -- DONE+++
- added staleElementExp for click + catch in the inputWithretry   -- DONE+++
- Assert for newPost- intermmitent failures!!!!!!


- UI-BasePage class   -- DONE+++
- getProperty("timeout.seconds");//TODO as variable   -- DONE+++


-DriverManager class  --  DONE+++
- getProperty("browser.type").toUpperCase();//TODO as variable  --  DONE+++
- getProperty("browser.headless"));//TODO as variable  --  DONE+++


- Logging class  --  DONE+++
- fusion /evidence folder with screenshots+logs sub-folders - updated paths in ScreenshotUtils+this class DONE +++
- DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss.SS")); //TODO as variable --  DONE+++
- featureLogDirectoryPath = "target/logs/featureLogs"; //TODO as variable --  DONE+++
- setPattern("%date{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"); //TODO as variable --  DONE+++
- setPattern("%date [%thread] %-5level %logger{35} - %msg%n"); //TODO as variable --  DONE+++



-ScenarioContext class  --  DONE+++
- return (T) value; //TODO unchecked cast error  --  DONE+++
- add custom explicit custom EXP for getContext method  --  DONE+++


-ApiProperties overrides ApiPropertyValues - handled in ApiTokenGenerator   -- DONE+++



- ScreenshotUtils class   -- DONE+++
- getProperty("timeout.seconds"));//TODO as variable   -- DONE+++
- screenshotDirectory.mkdirs(); // TODO correct save/notSave dir logic   -- DONE+++


-------------------------------------------------------------->>>PRE-assessment #2 observations:<<<---------------------------------------------------------
